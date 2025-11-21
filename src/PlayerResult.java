import java.io.Serializable;

public class PlayerResult implements Serializable {
    private Player player;
    private int placement;
    private int matchPoints;
    private double opponentWinPercentage;
    //private int championshipPointsEarned;

    public PlayerResult(Player player, int placement, int matchPoints, double opponentWinPercentage){
        this.player = player;
        this.placement = placement;
        this.matchPoints = matchPoints;
        this.opponentWinPercentage = opponentWinPercentage;
    }
}
