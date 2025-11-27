import java.util.Scanner;

public class menuHandler {
    Scanner scanner = new Scanner(System.in);
    int selection;
    public void displayMenu() {
        do {
            System.out.println("Welcome to the Tournament Manager!\n");
            System.out.println("Please select an option:");
            System.out.println("1. Create a new tournament");
            System.out.println("2. View a tournament");
            System.out.println("3. Compare players");
            System.out.println("4. Exit\n");
            System.out.print("Enter your selection: ");

            selection = scanner.nextInt();
            if(selection != 4){
                handleMenuSelection(selection);
            } else {
                System.out.println("Goodbye!");
            }
        }while (selection != 4);
    }
    public void handleMenuSelection(int selection) {
        switch (selection) {
            case 1:
                System.out.println("Creating a new tournament...\n");
                ResultEntryService.enterResultData();
                break;
            case 2:
                System.out.println("Viewing a tournament...\n");
                SearchService.searchTournament();
                break;
            case 3:
                System.out.println("Comparing players...\n");
                break;
            default:
                System.out.println("Invalid selection\n");
                break;
        }
    }
}
