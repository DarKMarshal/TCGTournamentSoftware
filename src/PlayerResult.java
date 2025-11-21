import java.io.Serializable;

public class PlayerResult implements Serializable {
    private Player player;
    private int placement;
    private int matchPoints;
    private double opponentWinPercentage;
    private int championshipPointsEarned;
}
