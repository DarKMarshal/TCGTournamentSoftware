import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Tournament implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public int id;
    public String name;
//    public LocalDateTime date;
    public List<DivisionTournament> tournamentsList;

    public Tournament(int id, String name, List<DivisionTournament> tournamentsList) {
        this.id = id;
        this.name = name;
//        this.date = date;
        this.tournamentsList = tournamentsList;
    }

    public static Tournament getOrCreate(String name, List<DivisionTournament> tournamentsList) {
        database db = database.getInstance();

        // Check if a tournament with this name already exists
        Tournament existing = db.getTournaments().values().stream()
                .filter(t -> t.name.equals(name))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            return existing;
        }

        // Generate next available ID
        int nextId = db.getTournaments().keySet().stream()
                .max(Integer::compare)
                .orElse(0) + 1;

        Tournament newTournament = new Tournament(nextId, name, tournamentsList);
        db.saveTournament(newTournament);
        return newTournament;
    }
}
