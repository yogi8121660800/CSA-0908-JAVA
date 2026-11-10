import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Library search layout: fixed-size toolbars and an expanding results region. */
public class LibrarySearchScreen extends JFrame {
    private final JTextField searchField = new JTextField(22);
    private final JComboBox<String> category =
        new JComboBox<>(new String[]{"All", "Book", "Journal", "Thesis"});
    private final JLabel status = new JLabel("Ready.");
    private final DefaultTableModel model =
        new DefaultTableModel(new Object[]{"Title", "Category", "Availability"}, 0);

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setLayout(new BorderLayout(6, 6));

        JLabel title = new JLabel("Campus Library", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search:"));
        searchRow.add(searchField);
        searchRow.add(category);
        JButton search = new JButton("Search");
        searchRow.add(search);
        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.NORTH);
        header.add(searchRow, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JTable results = new JTable(model);
        add(new JScrollPane(results), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        buttons.add(prev); buttons.add(next); buttons.add(reserve);

        JPanel south = new JPanel(new BorderLayout());
        south.add(buttons, BorderLayout.CENTER);
        south.add(status, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        search.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        prev.addActionListener(e -> status.setText("Previous results requested."));
        next.addActionListener(e -> status.setText("Next results requested."));
        reserve.addActionListener(e -> status.setText("Select a result to reserve."));

        model.addRow(new Object[]{"Introduction to Java", "Book", "Available"});
        model.addRow(new Object[]{"Database Systems", "Book", "Issued"});
        model.addRow(new Object[]{"Computing Research", "Journal", "Available"});

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 360));
        setSize(760, 500);
        setLocationRelativeTo(null);
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        String selectedCategory = (String) category.getSelectedItem();
        model.setRowCount(0);
        String[][] sample = {
            {"Introduction to Java", "Book", "Available"},
            {"Database Systems", "Book", "Issued"},
            {"Computing Research", "Journal", "Available"}
        };
        int found = 0;
        for (String[] row : sample) {
            boolean matchesText = query.isEmpty() || row[0].toLowerCase().contains(query);
            boolean matchesCategory = "All".equals(selectedCategory)
                || row[1].equals(selectedCategory);
            if (matchesText && matchesCategory) {
                model.addRow(row);
                found++;
            }
        }
        status.setText(found + " result(s) found.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}
