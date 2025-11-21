import java.io.File;

public interface iDatabase {
    public File connect();
    public void disconnect();
    public void saveTournament(Tournament tournament);
    public void savePlayer(Player player);
    
}
