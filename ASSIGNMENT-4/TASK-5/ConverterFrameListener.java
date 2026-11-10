import java.awt.*;
import java.awt.event.*;

public class ConverterFrameListener extends Frame implements ActionListener {
    private final TextField amount = new TextField(10);
    private final Choice target = new Choice();
    private final Label result = new Label(" ");
    public ConverterFrameListener() {
        super("Converter – Frame Listener");
        setLayout(new FlowLayout());
        target.add("USD"); target.add("EUR");
        Button convert = new Button("Convert");
        add(new Label("INR")); add(amount); add(target); add(convert); add(result);
        convert.addActionListener(this);
        amount.addActionListener(this);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(360, 150);
    }
    @Override public void actionPerformed(ActionEvent e) { convert(); }
    private void convert() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            double rate = target.getSelectedItem().equals("USD") ? 0.012 : 0.011;
            result.setText(String.format("%.2f %s", value * rate, target.getSelectedItem()));
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
        }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new ConverterFrameListener().setVisible(true));
    }
}
