package TCGTournamentSoftware;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResultEntryService {
    public static void enterResultData() {
        int playerId;
        Player player;
        String tournamentName;
        String playerName;
        String divisionName;
        String multipleDivisions;
        String tournamentType;
        int playerCount;
        int placement;
        int matchPoints;
        double opponentWinPercentage;

        Scanner scanner = new Scanner(System.in);
        List<PlayerResult> results;
        List<DivisionTournament> divisions = new ArrayList<>();


        System.out.println("Enter the tournament name: ");
        tournamentName = scanner.nextLine();

        System.out.println("What type of tournament is this? (Casual/Challenge/Cup): ");
        tournamentType = scanner.nextLine();

        do {
            results = new ArrayList<>();

            System.out.println("Enter Age Division (Junior/Senior/Master): ");
            divisionName = scanner.nextLine();

            System.out.println("Enter the number of players: ");
            playerCount = scanner.nextInt();

            scanner.nextLine();

//            System.out.println("Enter the tournament date: ");
//            String tournamentDate = scanner.nextLine();
//            scanner.nextLine();

            System.out.println("Please enter player results in the order they finished.");

            for (int i = 1; i <= playerCount; i++) {
                placement = i;

                System.out.println("Enter Player " + i + " ID: ");
                playerId = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Enter Player " + i + " Name: ");
                playerName = scanner.nextLine();

                player = Player.getOrCreate(playerId, playerName);

                System.out.println("Enter Player " + i + " Match Points: ");
                matchPoints = scanner.nextInt();

                scanner.nextLine();

                System.out.println("Enter Player " + i + " Opponent Win Percentage: ");
                opponentWinPercentage = scanner.nextDouble();

                scanner.nextLine();

                results.add(new PlayerResult(player, placement, matchPoints, opponentWinPercentage));
            }

            divisions.add(new DivisionTournament(divisionName, tournamentType, results));

            System.out.println("Do you want to add another Age Division? (Y/N)");
            multipleDivisions = scanner.nextLine();

        }while (multipleDivisions.equalsIgnoreCase("Y"));

        Tournament.getOrCreate(tournamentName, divisions);
        System.out.println("Tournament saved successfully!");
    }

}
