import java.util.Scanner;

public class Q1_ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double balance = 5000.0;

        try {
            System.out.print("Enter account number: ");
            int accountNumber = Integer.parseInt(sc.nextLine());

            System.out.print("Enter withdrawal amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive.");
            }
            if (amount > balance) {
                throw new ArithmeticException("Insufficient balance.");
            }

            balance -= amount;
            System.out.println("Withdrawal successful.");
            System.out.printf("Remaining balance: %.2f%n", balance);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter numbers only.");
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } finally {
            System.out.println("ATM transaction completed.");
            sc.close();
        }
    }
}
