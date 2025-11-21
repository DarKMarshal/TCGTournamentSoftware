import java.time.LocalDateTime;
import java.util.List;

public class DivisionTournament implements iTournament{
    private AgeDivision ageDivision;
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

    public DivisionTournament(String division, List<PlayerResult> results){
        division = division.toLowerCase();

        switch(division){
            case "junior":
                ageDivision = AgeDivision.Junior;
                break;
            case "senior":
                ageDivision = AgeDivision.Senior;
                break;
            case "master":
                ageDivision = AgeDivision.Master;
                break;
        }

        this.results = results;
    }
    public void calculateChampionshipPoints(){

    }
}
