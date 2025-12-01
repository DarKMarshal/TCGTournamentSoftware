package TCGTournamentSoftware;

import org.jetbrains.annotations.NotNull;

public class Player{
    private int id;
    private String name;

    Player(int id, String name) {
        this.id = id;
        this.name = name;
    }


    @NotNull
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

    //TODO Implement getters for TCGTournamentSoftware.database stored information
    public int getTotalPoints() {
        return 0;
    }
    public double getWinPercentage() {
        return 0;
    }
}
