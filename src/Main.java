import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ClaimsManager manager = new ClaimsManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("  ShieldSecure Claims Intake & Adjuster System   ");
        System.out.println("=================================================");

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. File a claim");
            System.out.println("2. Retry queued claims");
            System.out.println("3. Approve claim");
            System.out.println("4. Reject claim");
            System.out.println("5. Settle claim");
            System.out.println("6. Show adjuster caseloads");
            System.out.println("7. Show claim status");
            System.out.println("8. Calculate payout");
            System.out.println("0. Exit");
            System.out.print("\nEnter choice: ");

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid menu number (0-8).");
                continue;
            }

            switch (choice) {
                case 1:
                    handleFileClaim(manager, scanner);
                    break;
                case 2:
                    handleRetryQueuedClaims(manager);
                    break;
                case 3:
                    handleApproveClaim(manager, scanner);
                    break;
                case 4:
                    handleRejectClaim(manager, scanner);
                    break;
                case 5:
                    handleSettleClaim(manager, scanner);
                    break;
                case 6:
                    System.out.println("\n" + manager.getCaseloadsReport());
                    break;
                case 7:
                    handleShowClaimStatus(manager, scanner);
                    break;
                case 8:
                    handleCalculatePayout(manager, scanner);
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting ShieldSecure Claims System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option between 0 and 8.");
            }
        }

        scanner.close();
    }

    private static void handleFileClaim(ClaimsManager manager, Scanner scanner) {
        System.out.println("\n--- File a Claim ---");
        System.out.print("Enter Policy ID (e.g., POL001): ");
        String policyId = scanner.nextLine().trim();

        System.out.print("Enter Policyholder ID (e.g., PH001): ");
        String policyholderId = scanner.nextLine().trim();

        System.out.print("Enter Claim Type (AUTO, HEALTH, PROPERTY): ");
        String typeStr = scanner.nextLine().trim().toUpperCase();
        ClaimType type;
        try {
            type = ClaimType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid claim type. Valid options are AUTO, HEALTH, PROPERTY.");
            return;
        }

        System.out.print("Enter Claimed Amount ($): ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount entered.");
            return;
        }

        System.out.print("Enter Claim Description: ");
        String description = scanner.nextLine().trim();

        String result = manager.fileClaim(policyId, policyholderId, type, amount, description);
        System.out.println("\n" + result);
    }

    private static void handleRetryQueuedClaims(ClaimsManager manager) {
        System.out.println("\n--- Retrying Queued Claims ---");
        int assignedCount = manager.retryQueuedClaims(manager.getDefaultStrategy());
        System.out.println("Retried queued claims. Successfully reassigned " + assignedCount + " claim(s).");
    }

    private static void handleApproveClaim(ClaimsManager manager, Scanner scanner) {
        System.out.println("\n--- Approve Claim ---");
        System.out.print("Enter Claim ID (e.g., CLM001): ");
        String claimId = scanner.nextLine().trim();

        System.out.print("Enter Adjuster ID (e.g., ADJ001): ");
        String adjusterId = scanner.nextLine().trim();

        String result = manager.approveClaim(claimId, adjusterId);
        System.out.println("\n" + result);
    }

    private static void handleRejectClaim(ClaimsManager manager, Scanner scanner) {
        System.out.println("\n--- Reject Claim ---");
        System.out.print("Enter Claim ID (e.g., CLM001): ");
        String claimId = scanner.nextLine().trim();

        System.out.print("Enter Adjuster ID (e.g., ADJ001): ");
        String adjusterId = scanner.nextLine().trim();

        String result = manager.rejectClaim(claimId, adjusterId);
        System.out.println("\n" + result);
    }

    private static void handleSettleClaim(ClaimsManager manager, Scanner scanner) {
        System.out.println("\n--- Settle Claim ---");
        System.out.print("Enter Claim ID (e.g., CLM001): ");
        String claimId = scanner.nextLine().trim();

        System.out.print("Enter Adjuster ID (e.g., ADJ001): ");
        String adjusterId = scanner.nextLine().trim();

        String result = manager.settleClaim(claimId, adjusterId);
        System.out.println("\n" + result);
    }

    private static void handleShowClaimStatus(ClaimsManager manager, Scanner scanner) {
        System.out.println("\n--- Show Claim Status ---");
        System.out.print("Enter Claim ID (e.g., CLM001): ");
        String claimId = scanner.nextLine().trim();

        System.out.println("\n" + manager.getClaimStatusReport(claimId));
    }

    private static void handleCalculatePayout(ClaimsManager manager, Scanner scanner) {
        System.out.println("\n--- Calculate Payout ---");
        System.out.print("Enter Claim ID (e.g., CLM001): ");
        String claimId = scanner.nextLine().trim();

        Claim claim = manager.getClaim(claimId);
        if (claim == null) {
            System.out.println("\nError: Claim " + claimId + " does not exist.");
            return;
        }

        double payout = manager.calculatePayout(claimId);
        System.out.println(String.format("\nClaim ID: %s | Status: %s | Calculated Payout: $%,.2f",
                claim.getClaimId(), claim.getCurrentStatus(), payout));
    }
}
