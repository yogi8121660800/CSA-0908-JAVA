import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** AWT-based fee window; service/database work is kept outside this class. */
public class FeeWindow extends Frame {
    private final TextField regNo = new TextField(18);
    private final TextField amount = new TextField(18);
    private final Label status = new Label("Enter fee details.");
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));

        Panel form = new Panel(new GridLayout(2, 2, 8, 8));
        form.add(new Label("Registration number:"));
        form.add(regNo);
        form.add(new Label("Amount:"));
        form.add(amount);

        Panel actions = new Panel(new FlowLayout());
        Button pay = new Button("Pay");
        Button clear = new Button("Clear");
        Button receipt = new Button("Receipt");
        actions.add(pay);
        actions.add(clear);
        actions.add(receipt);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        add(status, BorderLayout.NORTH);

        pay.addActionListener(e -> pay());
        clear.addActionListener(e -> {
            regNo.setText("");
            amount.setText("");
            status.setText("Fields cleared.");
        });
        receipt.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Latest payment receipt is not available in this sample.",
                "Receipt", JOptionPane.INFORMATION_MESSAGE));

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(430, 190);
        setLocationRelativeTo(null);
    }

    private void pay() {
        // Database work is performed on a worker thread, not the AWT event thread.
        status.setText("Processing payment...");
        String reg = regNo.getText();
        String amt = amount.getText();
        Thread worker = new Thread(() -> {
            String message = service.payFee(reg, amt);
            EventQueue.invokeLater(() -> status.setText(message));
        });
        worker.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
