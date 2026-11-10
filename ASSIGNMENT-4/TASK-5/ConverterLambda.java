import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class ConverterLambda extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public ConverterLambda() {
        super("Currency Converter – Lambda");
        setLayout(new FlowLayout());
        JButton convertButton = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);

        // One listener instance is shared by the button and Enter key.
        java.awt.event.ActionListener convertAction = e -> convert();
        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void convert() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(value) || value < 0) {
                result.setText("Invalid amount");
                JOptionPane.showMessageDialog(this, "Invalid amount",
                    "Input error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String currency = (String) target.getSelectedItem();
            String converted = String.format("%.2f %s", value * rate(currency), currency);
            result.setText(converted);
            JOptionPane.showMessageDialog(this, converted,
                "Conversion result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount",
                "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterLambda().setVisible(true));
    }
}
