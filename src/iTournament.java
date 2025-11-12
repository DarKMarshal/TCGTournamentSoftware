import java.time.LocalDateTime;
import java.util.List;

public interface iTournament {
    public int getId();
    public String getName();
    public LocalDateTime getDate();
    public List<PlayerResult> getResults();
    public void calculateChampionshipPoints();
}
