package TCGTournamentSoftware;

//import org.jetbrains.annotations.NotNull;

public class Player{
    private int id;
    private String name;
    private int championshipPoints;

    Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.championshipPoints = 0;
    }

    Player(int id, String name, int championshipPoints) {
        this.id = id;
        this.name = name;
        this.championshipPoints = championshipPoints;
    }


    //@NotNull
    public static Player getOrCreate(int id, String name) {
        database db = database.getInstance();
        Player existingPlayer = db.getPlayer(id);
        if (existingPlayer != null) {
            return existingPlayer;
        }

        Player newPlayer = new Player(id, name);
        db.savePlayer(newPlayer);
        return newPlayer;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    //TODO Implement getters for database stored information
    public int getChampionshipPoints() {
        return championshipPoints;
    }
    public  void setChampionshipPoints(int championshipPoints) {
        this.championshipPoints = championshipPoints;
    }
    public  void addChampionshipPoints(int points) {
        this.championshipPoints += points;
    }
}
