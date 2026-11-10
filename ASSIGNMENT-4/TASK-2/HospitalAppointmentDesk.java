import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/** Demonstrates dependent department/doctor controls and appointment validation. */
public class HospitalAppointmentDesk extends JFrame {
    private final JTextField patientId = new JTextField(12);
    private final JComboBox<String> department =
        new JComboBox<>(new String[]{"Cardiology", "Neurology", "Orthopedics"});
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JComboBox<String> slot =
        new JComboBox<>(new String[]{"09:00", "10:00", "11:00", "14:00", "15:00"});
    private final JLabel status = new JLabel("Enter details and book an appointment.");
    private final Map<String, java.util.List<String>> doctors = new LinkedHashMap<>();
    private boolean bookingInProgress = false;

    public HospitalAppointmentDesk() {
        super("Hospital Outpatient Appointment Desk");
        doctors.put("Cardiology", Arrays.asList("Dr. Kumar", "Dr. Priya"));
        doctors.put("Neurology", Arrays.asList("Dr. Arun", "Dr. Meena"));
        doctors.put("Orthopedics", Arrays.asList("Dr. Ravi", "Dr. Divya"));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Patient ID:")); form.add(patientId);
        form.add(new JLabel("Department:")); form.add(department);
        form.add(new JLabel("Doctor:")); form.add(doctor);
        form.add(new JLabel("Time slot:")); form.add(slot);

        JButton book = new JButton("Book Appointment");
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(book, BorderLayout.CENTER);
        bottom.add(status, BorderLayout.SOUTH);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        department.addActionListener(e -> refreshDoctors());
        book.addActionListener(e -> bookAppointment());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (bookingInProgress) {
                    int answer = JOptionPane.showConfirmDialog(
                        HospitalAppointmentDesk.this,
                        "A booking is in progress. Close the window?",
                        "Confirm close", JOptionPane.YES_NO_OPTION);
                    if (answer != JOptionPane.YES_OPTION) return;
                }
                dispose();
            }
        });

        refreshDoctors();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(470, 260);
        setLocationRelativeTo(null);
    }

    private void refreshDoctors() {
        String selectedDepartment = (String) department.getSelectedItem();
        doctor.removeAllItems();
        for (String name : doctors.getOrDefault(selectedDepartment, Collections.emptyList())) {
            doctor.addItem(name);
        }
    }

    private void bookAppointment() {
        String id = patientId.getText().trim();
        if (id.isEmpty()) {
            status.setText("Error: Patient ID is required.");
            return;
        }
        if (doctor.getSelectedItem() == null || slot.getSelectedItem() == null) {
            status.setText("Error: Select a doctor and time slot.");
            return;
        }
        bookingInProgress = true;
        status.setText("Booking...");
        // Sample assignment implementation: replace this block with a service/JDBC call.
        javax.swing.Timer timer = new javax.swing.Timer(700, e -> {
            bookingInProgress = false;
            status.setText("Appointment confirmed for " + id + " with "
                + doctor.getSelectedItem() + " at " + slot.getSelectedItem() + ".");
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalAppointmentDesk().setVisible(true));
    }
}
