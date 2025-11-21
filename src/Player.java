import java.io.Serializable;

public class Player implements Serializable {
    private int id;
    private String name;

    //TODO Change constructor to accept database stored information
    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    //TODO Implement getters for database stored information
    public int getTotalPoints() {
        return 0;
    }
    public double getWinPercentage() {
        return 0;
    }
}
