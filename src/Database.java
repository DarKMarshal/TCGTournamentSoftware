import java.io.*;
import java.util.*;

public class Database implements iDatabase{
    private File databaseFile;
    private Map<Integer, Player> players;
    private Map<Integer, Tournament> tournaments;

    public Database(String filepath) {
        this.databaseFile = new File(filepath);
        this.players = new HashMap<>();
        this.tournaments = new HashMap<>();
    }

    public File connect(){
        try {
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
                saveDatabase();
            } else {
                loadDatabase();
            }
            return databaseFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void disconnect(){
        saveDatabase();
        players.clear();
        tournaments.clear();
    }
    public void saveTournament(Tournament tournament){
        tournaments.put(tournament.id, tournament);
        saveDatabase();
    }
    public void savePlayer(Player player){
        players.put(player.getId(), player);
        saveDatabase();
    }

    private void saveDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(databaseFile))) {
            oos.writeObject(players);
            oos.writeObject(tournaments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadDatabase() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(databaseFile))) {
            players = (Map<Integer, Player>) ois.readObject();
            tournaments = (Map<Integer, Tournament>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
