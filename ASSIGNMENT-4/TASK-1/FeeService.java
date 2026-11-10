import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Keeps validation and fee-payment persistence outside the GUI. */
public class FeeService {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public String payFee(String regNo, String amountText) {
        if (regNo == null || regNo.trim().isEmpty()) {
            return "Enter a registration number.";
        }

        final double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
            if (!Double.isFinite(amount) || amount <= 0) {
                return "Enter an amount greater than zero.";
            }
        } catch (NumberFormatException | NullPointerException ex) {
            return "Enter a valid amount.";
        }

        String sql = "INSERT INTO fees (reg_no, amount) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, regNo.trim());
            ps.setDouble(2, amount);
            ps.executeUpdate();
            return "Payment recorded successfully.";
        } catch (SQLException ex) {
            return "Payment could not be recorded: " + ex.getMessage();
        }
    }
}
