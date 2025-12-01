package TCGTournamentSoftware;

public class PlayerResult {
    private Player player;
    private int placement;
    private int matchPoints;
    private double opponentWinPercentage;
    private int championshipPointsEarned;

    public PlayerResult(Player player, int placement){
        this.player = player;
        this.placement = placement;
    }

    public PlayerResult(Player player, int placement, int matchPoints, double opponentWinPercentage){
        this.player = player;
        this.placement = placement;
        this.matchPoints = matchPoints;
        this.opponentWinPercentage = opponentWinPercentage;
    }

    public Player getPlayer() {
        return player;
    }
    public int getPlacement() {
        return placement;
    }
    public int getMatchPoints() {
        return matchPoints;
    }
    public double getOpponentWinPercentage() {
        return opponentWinPercentage;
    }
    public int getChampionshipPointsEarned() {
        return championshipPointsEarned;
    }
    public void setChampionshipPointsEarned(int championshipPointsEarned) {
        this.championshipPointsEarned = championshipPointsEarned;
    }
}
