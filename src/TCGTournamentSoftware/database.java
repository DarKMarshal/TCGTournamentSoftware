package TCGTournamentSoftware;

import java.io.*;
import java.sql.*;
import java.util.*;

public class database implements iDatabase {
    private static final database INSTANCE = new database("tournament.db");
    private Connection connection;
    private final String DATABASE_PATH;

    private database(String filepath) {
        this.DATABASE_PATH = filepath;
    }

    public static database getInstance() {
        return INSTANCE;
    }

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);

            if (!tablesExist()) {
                System.out.println("New database created\n");
                createTables();
            } else {
                System.out.println("Connected to existing database\n");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    private boolean tablesExist() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet rs = meta.getTables(null, null, "players", null);
        return rs.next();
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        String createPlayers = """
            CREATE TABLE IF NOT EXISTS players (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL
            )
        """;

        String createTournaments = """
            CREATE TABLE IF NOT EXISTS tournaments (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL
            )
        """;

        String createDivisions = """
            CREATE TABLE IF NOT EXISTS divisions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tournament_id INTEGER NOT NULL,
                age_division TEXT NOT NULL,
                tournament_type TEXT NOT NULL,
                FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE
            )
        """;

        String createResults = """
            CREATE TABLE IF NOT EXISTS results (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                division_id INTEGER NOT NULL,
                player_id INTEGER NOT NULL,
                placement INTEGER NOT NULL,
                points INTEGER DEFAULT 0,
                match_points INTEGER DEFAULT 0,
                opponent_win_percentage REAL DEFAULT 0.0,
                FOREIGN KEY (division_id) REFERENCES divisions(id) ON DELETE CASCADE,
                FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
            )
        """;


        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPlayers);
            System.out.println("Players table created");
            stmt.execute(createTournaments);
            System.out.println("Tournaments table created");
            stmt.execute(createDivisions);
            System.out.println("Divisions table created");
            stmt.execute(createResults);
            System.out.println("Results table created");
            System.out.println("Database tables created successfully");
        }
    }

    @Override
    public void savePlayer(Player player) {
        String sql = "INSERT OR REPLACE INTO players (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, player.getId());
            pstmt.setString(2, player.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving player: " + player.getName());
            e.printStackTrace();
        }
    }

    public Player getPlayer(int id) {
        String sql = "SELECT * FROM players WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Player(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving player with ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, Player> getPlayers() {
        Map<Integer, Player> players = new HashMap<>();
        String sql = "SELECT * FROM players ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                players.put(id, new Player(id, rs.getString("name")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving players");
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public void saveTournament(Tournament tournament) {
        try {
            connection.setAutoCommit(false);

            // Save tournament
            String tournamentSql = "INSERT OR REPLACE INTO tournaments (id, name) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(tournamentSql)) {
                pstmt.setInt(1, tournament.getId());
                pstmt.setString(2, tournament.getName());
                pstmt.executeUpdate();
            }

            // Delete existing divisions for this tournament
            String deleteDivisions = "DELETE FROM divisions WHERE tournament_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteDivisions)) {
                pstmt.setInt(1, tournament.getId());
                pstmt.executeUpdate();
            }

            // Save divisions and results
            for (DivisionTournament division : tournament.getDivisions()) {
                saveDivision(tournament.getId(), division);
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error saving tournament: " + tournament.getName());
            e.printStackTrace();
        }
    }

    private void saveDivision(int tournamentId, DivisionTournament division) throws SQLException {
        String divisionSql = "INSERT INTO divisions (tournament_id, age_division, tournament_type) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(divisionSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, division.getAgeDivision().toString());
            pstmt.setString(3, division.getTournamentType());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int divisionId = rs.getInt(1);
                saveResults(divisionId, division.getResults());
            }
        }
    }

    private void saveResults(int divisionId, List<PlayerResult> results) throws SQLException {
        String resultSql = "INSERT INTO results (division_id, player_id, placement, points, match_points, opponent_win_percentage) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(resultSql)) {
            for (PlayerResult result : results) {
                pstmt.setInt(1, divisionId);
                pstmt.setInt(2, result.getPlayer().getId());
                pstmt.setInt(3, result.getPlacement());
                pstmt.setInt(4, result.getChampionshipPointsEarned());
                pstmt.setInt(5, result.getMatchPoints());
                pstmt.setDouble(6, result.getOpponentWinPercentage());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }


    public Tournament getTournament(int id) {
        String sql = "SELECT * FROM tournaments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                List<DivisionTournament> divisions = loadDivisionsForTournament(id);
                return new Tournament(id, name, divisions);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving tournament with ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public List<Tournament> getTournamentsByName(String searchTerm) {
        List<Tournament> matchingTournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournaments WHERE LOWER(name) LIKE LOWER(?) ORDER BY id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                List<DivisionTournament> divisions = loadDivisionsForTournament(id);
                matchingTournaments.add(new Tournament(id, name, divisions));
            }
        } catch (SQLException e) {
            System.out.println("Error searching tournaments by name");
            e.printStackTrace();
        }
        return matchingTournaments;
    }


    private List<DivisionTournament> loadDivisionsForTournament(int tournamentId) throws SQLException {
        List<DivisionTournament> divisions = new ArrayList<>();
        String sql = "SELECT * FROM divisions WHERE tournament_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tournamentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int divisionId = rs.getInt("id");
                String ageDivision = rs.getString("age_division");
                String tournamentType = rs.getString("tournament_type");
                List<PlayerResult> results = loadResults(divisionId);
                divisions.add(new DivisionTournament(ageDivision, tournamentType, results));
            }
        }
        return divisions;
    }

    private List<PlayerResult> loadResults(int divisionId) throws SQLException {
        List<PlayerResult> results = new ArrayList<>();
        String sql = """
            SELECT r.placement, r.points, r.match_points, r.opponent_win_percentage, p.id, p.name
                FROM results r
                JOIN players p ON r.player_id = p.id
                WHERE r.division_id = ?
                ORDER BY r.placement
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, divisionId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(rs.getInt("id"), rs.getString("name"));
                PlayerResult result = new PlayerResult(
                        player,
                        rs.getInt("placement"),
                        rs.getInt("match_points"),
                        rs.getDouble("opponent_win_percentage")
                );
                result.setChampionshipPointsEarned(rs.getInt("points"));
                results.add(result);
            }
        }
        return results;
    }

    public Map<Integer, Tournament> getTournaments() {
        Map<Integer, Tournament> tournaments = new HashMap<>();
        String sql = "SELECT * FROM tournaments ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                List<DivisionTournament> divisions = loadDivisionsForTournament(id);
                Tournament tournament = new Tournament(id, name, divisions);
                tournaments.put(id, tournament);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving tournaments");
            e.printStackTrace();
        }
        return tournaments;
    }

    public boolean databaseExists() {
        File dbFile = new File(DATABASE_PATH);
        return dbFile.exists() && dbFile.length() > 0;
    }

    public void deletePlayer(int playerId) {
        String sql = "DELETE FROM players WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting player");
            e.printStackTrace();
        }
    }

    public void deleteTournament(int tournamentId) {
        String sql = "DELETE FROM tournaments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tournamentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting tournament");
            e.printStackTrace();
        }
    }
}
