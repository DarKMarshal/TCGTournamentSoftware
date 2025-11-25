import java.time.LocalDateTime;
import java.util.List;

public class DivisionTournament implements iTournament{
    private AgeDivision ageDivision;
    private String tournamentType;
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

    public DivisionTournament(String division, String type, List<PlayerResult> results){
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
        this.tournamentType = type;
        this.results = results;
        this.pointCalculator = createPointCalculator(type);
    }

    private pointCalculator createPointCalculator(String type) {
        return switch(type.toLowerCase()) {
            case "casual" -> new CasualPointCalculator();
            case "challenge" -> new ChallengePointCalculator();
            case "cup" -> new CupPointCalculator();
            default -> throw new IllegalArgumentException("Unknown tournament type: " + type);
        };
    }
    public void calculateChampionshipPoints() {
        pointCalculator.calculateChampionshipPoints(this);
    }
}
