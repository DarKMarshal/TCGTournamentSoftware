import java.util.List;

public class ChallengePointCalculator implements pointCalculator{
    @Override
    public void calculateChampionshipPoints(DivisionTournament tournament) {
        int playerCount;
        List<PlayerResult> results = tournament.getResults();

        playerCount = results.size();

        for (PlayerResult result : results) {
            //int position = result.getPosition();

            //if (position == 1){

        }
    }
}
