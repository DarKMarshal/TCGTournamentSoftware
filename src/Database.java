import java.io.*;
import java.util.*;

public class Database implements iDatabase{
    private static final Database INSTANCE = new Database("database.txt");

    private final File DATABASE_FILE;
    private Map<Integer, Player> players;
    private Map<Integer, Tournament> tournaments;

    private Database(String filepath) {
        this.DATABASE_FILE = new File(filepath);
        this.players = new HashMap<>();
        this.tournaments = new HashMap<>();
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public Player getPlayer(int id) {
        return players.get(id);
    }
    public Tournament getTournament(int id) {
        return tournaments.get(id);
    }
    public Map<Integer, Player> getPlayers() {
        return players;
    }
    public Map<Integer, Tournament> getTournaments() {
        return tournaments;
    }

    @Override
    public File connect(){
        try {
            if (DATABASE_FILE.createNewFile()) {
                System.out.println("Database file created\n");
                saveDatabase();
            } else {
                loadDatabase();
            }
            return DATABASE_FILE;
        } catch (IOException e) {
            System.out.println("Error creating database file");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void disconnect(){
        saveDatabase();
        players.clear();
        tournaments.clear();
    }

    @Override
    public void saveTournament(Tournament tournament){
        tournaments.put(tournament.id, tournament);
        saveDatabase();
    }

    @Override
    public void savePlayer(Player player){
        players.put(player.getId(), player);
        saveDatabase();
    }

    private void saveDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            oos.writeObject(players);
            oos.writeObject(tournaments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadDatabase() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            players = (Map<Integer, Player>) ois.readObject();
            tournaments = (Map<Integer, Tournament>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
