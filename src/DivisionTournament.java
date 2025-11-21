import java.time.LocalDateTime;
import java.util.List;

public class DivisionTournament implements iTournament{
    private AgeDivision division;
    private pointCalculator pointCalculator;
    private List<PlayerResult> results;
    public int getId(){
        return 0;
    }
    public String getName(){
        return "null";
    }
    public LocalDateTime getDate(){
        return null;
    }
    public List<PlayerResult> getResults(){
        return null;
    }
    public void calculateChampionshipPoints(){

    }
}
