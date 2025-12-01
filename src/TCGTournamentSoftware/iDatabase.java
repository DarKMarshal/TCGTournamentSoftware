package TCGTournamentSoftware;

public interface iDatabase {
    public void connect();
    public void disconnect();
    public void saveTournament(Tournament tournament);
    public void savePlayer(Player player);
    
}
