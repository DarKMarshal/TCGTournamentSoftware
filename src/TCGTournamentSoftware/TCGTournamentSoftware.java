package TCGTournamentSoftware;

public class TCGTournamentSoftware {
    static void main(String[] args) {
        database db = database.getInstance();
        menuHandler menu = new menuHandler();
        db.connect();
        menu.displayMenu();
        db.disconnect();
    }
}
