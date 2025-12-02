package TCGTournamentSoftware;

import java.util.List;
import java.util.Scanner;

public class SearchService {
    public static void searchTournament() {
        database db = database.getInstance();
        Scanner scanner = new Scanner(System.in);
        int selection;
        String rerun;

        do {
            System.out.println("Would you like to search by tournament name (1) or ID (2)?");
            selection = scanner.nextInt();
            scanner.nextLine();

            switch (selection) {
                case 1:
                    System.out.println("Enter tournament name: ");
                    String searchTerm = scanner.nextLine();
                    List<Tournament> results = db.getTournamentsByName(searchTerm);

                    if (results.isEmpty()) {
                        System.out.println("No tournaments found matching: " + searchTerm);
                    } else if (results.size() == 1) {
                        displayTournament(results.getFirst());
                    } else {
                        System.out.println("Multiple tournaments found:");
                        for (int i = 0; i < results.size(); i++) {
                            System.out.println((i + 1) + ". " + results.get(i).getName() + " (ID: " + results.get(i).getId() + ")");
                        }
                        System.out.println("Select a tournament (1-" + results.size() + "): ");
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice > 0 && choice <= results.size()) {
                            displayTournament(results.get(choice - 1));
                        } else {
                            System.out.println("Invalid selection");
                        }
                    }
                    break;

                case 2:
                    System.out.println("Enter tournament ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Tournament tournament = db.getTournament(id);
                    if (tournament != null) {
                        displayTournament(tournament);
                    } else {
                        System.out.println("Tournament not found");
                    }
                    break;

                default:
                    System.out.println("Invalid selection");
                    break;
            }

            System.out.println("Would you like to search again? (Y/N)");
            rerun = scanner.nextLine();
            rerun = rerun.toUpperCase();

        } while (rerun.equals("Y"));
    }

    private static void displayTournament(Tournament tournament) {
        System.out.println("Tournament " + tournament.getName() + " results: ");
        System.out.println("Tournament Type : " + tournament.tournamentsList.getFirst().getTournamentType());
        for (DivisionTournament division : tournament.getDivisions()) {
            System.out.println("Division: " + division.getAgeDivision());
            System.out.printf("%-10s %-20s %-15s %-18s %-20s%n",
                    "Placement", "Player Name", "Match Points", "Opponent Win %", "Championship Points");
            for (PlayerResult result : division.getResults()) {
                System.out.printf("%-10d %-20s %-15d %-18.2f %-20d%n",
                        result.getPlacement(),
                        result.getPlayer().getName(),
                        result.getMatchPoints(),
                        result.getOpponentWinPercentage(),
                        result.getChampionshipPointsEarned());
            }
            System.out.println();
        }
    }

}
