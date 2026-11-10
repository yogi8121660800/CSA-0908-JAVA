import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Layered Swing/JDBC registration application. Database work runs in SwingWorker. */
public class EventRegistrationApp extends JFrame {
    private final EventService service = new EventService();
    private final JTextField studentId = new JTextField(12);
    private final JTextField studentName = new JTextField(16);
    private final JComboBox<String> eventChoice = new JComboBox<>();
    private final JComboBox<String> filterChoice = new JComboBox<>(new String[]{"All"});
    private final JLabel status = new JLabel("Ready.");
    private final DefaultTableModel model = new DefaultTableModel(
        new Object[]{"ID", "Student ID", "Student Name", "Event", "Registered At"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private int page = 0;
    private static final int PAGE_SIZE = 50;

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(8, 8));

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export current page");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(export); file.add(exit); menuBar.add(file);
        setJMenuBar(menuBar);
        export.addActionListener(e -> exportCurrentPage());
        exit.addActionListener(e -> dispose());

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Student Registration"));
        JButton register = new JButton("Register");
        form.add(new JLabel("Student ID:")); form.add(studentId);
        form.add(new JLabel("Name:")); form.add(studentName);
        form.add(new JLabel("Event:")); form.add(eventChoice);
        form.add(register);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton applyFilter = new JButton("Apply Filter");
        JButton previous = new JButton("Previous");
        JButton next = new JButton("Next");
        filters.add(new JLabel("Filter event:")); filters.add(filterChoice);
        filters.add(applyFilter); filters.add(previous); filters.add(next);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.NORTH);
        north.add(filters, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        register.addActionListener(e -> registerStudent());
        applyFilter.addActionListener(e -> { page = 0; loadPage(); });
        previous.addActionListener(e -> { if (page > 0) page--; loadPage(); });
        next.addActionListener(e -> { page++; loadPage(); });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 520);
        setLocationRelativeTo(null);
        loadEvents();
    }

    private void loadEvents() {
        status.setText("Loading events...");
        new SwingWorker<List<String>, Void>() {
            @Override protected List<String> doInBackground() throws Exception {
                return service.getEvents();
            }
            @Override protected void done() {
                try {
                    List<String> events = get();
                    for (String event : events) {
                        eventChoice.addItem(event);
                        filterChoice.addItem(event);
                    }
                    status.setText("Events loaded.");
                    loadPage();
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void registerStudent() {
        String id = studentId.getText().trim();
        String name = studentName.getText().trim();
        String event = (String) eventChoice.getSelectedItem();
        if (id.isEmpty() || name.isEmpty() || event == null) {
            status.setText("Enter student ID, name and select an event.");
            return;
        }
        setBusy(true, "Registering...");
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                service.register(id, name, event);
                return null;
            }
            @Override protected void done() {
                setBusy(false, "Ready.");
                try {
                    get();
                    status.setText("Registration successful.");
                    studentId.setText("");
                    studentName.setText("");
                    page = 0;
                    loadPage();
                } catch (Exception ex) {
                    Throwable cause = ex.getCause();
                    String message = cause == null ? ex.getMessage() : cause.getMessage();
                    if (message != null && message.toLowerCase().contains("duplicate")) {
                        status.setText("Duplicate: this student is already registered for that event.");
                    } else {
                        status.setText("Registration failed: " + message);
                    }
                }
            }
        }.execute();
    }

    private void loadPage() {
        String filter = (String) filterChoice.getSelectedItem();
        if (filter == null) filter = "All";
        final String selectedFilter = filter;
        status.setText("Loading registrations...");
        new SwingWorker<List<Object[]>, Void>() {
            @Override protected List<Object[]> doInBackground() throws Exception {
                return service.getRegistrations(selectedFilter, PAGE_SIZE, page * PAGE_SIZE);
            }
            @Override protected void done() {
                try {
                    List<Object[]> rows = get();
                    model.setRowCount(0);
                    for (Object[] row : rows) model.addRow(row);
                    status.setText("Showing " + rows.size() + " registration(s), page " + (page + 1));
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void exportCurrentPage() {
        try (FileWriter writer = new FileWriter("registrations_page.csv")) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) writer.append(',');
                writer.append(csv(model.getColumnName(c)));
            }
            writer.append('\n');
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) writer.append(',');
                    writer.append(csv(String.valueOf(model.getValueAt(r, c))));
                }
                writer.append('\n');
            }
            status.setText("Exported current page to registrations_page.csv.");
        } catch (IOException ex) {
            status.setText("Export failed: " + ex.getMessage());
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private void setBusy(boolean busy, String message) {
        status.setText(message);
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                       : Cursor.getDefaultCursor());
    }

    private void showError(Exception ex) {
        Throwable cause = ex.getCause();
        status.setText("Error: " + (cause == null ? ex.getMessage() : cause.getMessage()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true));
    }
}
