import java.awt.*;
import java.awt.event.*;

public class ConverterNamedInner extends Frame {
    private final TextField amount = new TextField(10);
    private final Choice target = new Choice();
    private final Label result = new Label(" ");
    public ConverterNamedInner() {
        super("Converter – Named Inner Class");
        setLayout(new FlowLayout());
        target.add("USD"); target.add("EUR");
        Button convert = new Button("Convert");
        add(new Label("INR")); add(amount); add(target); add(convert); add(result);
        ConvertHandler handler = new ConvertHandler();
        convert.addActionListener(handler);
        amount.addActionListener(handler);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(360, 150);
    }
    private class ConvertHandler implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            try {
                double value = Double.parseDouble(amount.getText().trim());
                double rate = target.getSelectedItem().equals("USD") ? 0.012 : 0.011;
                result.setText(String.format("%.2f %s", value * rate, target.getSelectedItem()));
            } catch (NumberFormatException ex) {
                result.setText("Invalid amount");
            }
        }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new ConverterNamedInner().setVisible(true));
    }
}
