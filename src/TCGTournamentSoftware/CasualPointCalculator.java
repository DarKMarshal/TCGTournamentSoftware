package TCGTournamentSoftware;

import java.util.List;

public class CasualPointCalculator implements pointCalculator{
    @Override
    public void calculateChampionshipPoints(DivisionTournament tournament) {
        List<PlayerResult> results = tournament.getResults();

        for (PlayerResult result : results) {
            result.setChampionshipPointsEarned(0);
        }
    }
}
