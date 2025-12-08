package TCGTournamentSoftware;

import java.util.Map;
import java.util.Scanner;

public class ComparisonService {
    public static void comparePlayersMenu() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        String rerun;

        do {
            System.out.println("Welcome to the Championship Point Comparison Tool!");
            System.out.println("==============================================");
            System.out.println("Would you like to compare two players or see a leaderboard of all players?");
            System.out.println("1. Compare two players");
            System.out.println("2. View leaderboard");
            System.out.println("3. Return to main menu");
            System.out.print("Enter your selection: ");
            selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
                case 1:
                    comparePlayers();

                    System.out.println("Would you like to do another comparison? (Y/N)");
                    rerun = scanner.nextLine();
                    rerun = rerun.toUpperCase();
                    if (rerun.equals("Y")) {
                        break;
                    } else {
                        selection = 3;
                    }
                    break;
                case 2:
                    displayLeaderboard();

                    System.out.println("Would you like to do another comparison? (Y/N)");
                    rerun = scanner.nextLine();
                    rerun = rerun.toUpperCase();
                    if (rerun.equals("Y")) {
                        break;
                    } else {
                        selection = 3;
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid selection");
            }


        } while (selection != 3);
    }

    public static void comparePlayers() {
        Player player1;
        Player player2;
        database db = database.getInstance();
        int player1Id;
        int player2Id;
        Scanner scanner = new Scanner(System.in);

        System.out.println("This tool compares two players' Championship Points.");

        System.out.println("Please enter the first player's ID: ");
        player1Id = scanner.nextInt();

        scanner.nextLine();

        System.out.println("Please enter the second player's ID: ");
        player2Id = scanner.nextInt();

        scanner.nextLine();

        player1 = db.getPlayer(player1Id);
        player2 = db.getPlayer(player2Id);

        System.out.println(player1.getName() + " vs " + player2.getName());
        System.out.println(player1.getChampionshipPoints() + " to " + player2.getChampionshipPoints());

        if (player1.getChampionshipPoints() > player2.getChampionshipPoints()) {
            System.out.println(player1.getName() + " is ahead of " + player2.getName() + " by " + (player1.getChampionshipPoints() - player2.getChampionshipPoints()) + " points!");
        } else if (player1.getChampionshipPoints() < player2.getChampionshipPoints()) {
            System.out.println(player2.getName() + " is ahead of " + player1.getName() + " by " + (player2.getChampionshipPoints() - player1.getChampionshipPoints()) + " points!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public static void displayLeaderboard() {
        database db = database.getInstance();
        Map<Integer, Player> players = db.getPlayers();


        System.out.println("Championship Point Leaderboard:");
        System.out.println("==============================================");

        if (players.isEmpty()) {
            System.out.println("No players found in the database.");
        } else {
            players.values().stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getChampionshipPoints(), p1.getChampionshipPoints()))
                    .forEach(player -> {
                        System.out.printf("%-30s | %5d points%n",
                                player.getName(),
                                player.getChampionshipPoints());
                    });
        }

        System.out.println("==============================================\n");
    }
}
