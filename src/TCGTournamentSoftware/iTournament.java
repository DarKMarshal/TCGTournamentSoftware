package TCGTournamentSoftware;

import java.time.LocalDateTime;
import java.util.List;

public interface iTournament {
    int getId();
    String getName();
    LocalDateTime getDate();
    List<PlayerResult> getResults();
    void calculateChampionshipPoints();
}
