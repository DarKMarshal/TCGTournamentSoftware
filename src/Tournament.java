import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Tournament implements Serializable {
    public int id;
    public String name;
    public LocalDateTime date;
    public List<PlayerResult> results;
}
