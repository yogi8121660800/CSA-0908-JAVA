import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    public List<String> getEvents() throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT event_name FROM events ORDER BY event_name";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(rs.getString("event_name"));
        }
        return result;
    }

    public void register(String studentId, String studentName, String eventName)
            throws SQLException {
        String sql = "INSERT INTO registrations(student_id, student_name, event_id) "
                   + "SELECT ?, ?, event_id FROM events WHERE event_name = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, studentName);
            ps.setString(3, eventName);
            int inserted = ps.executeUpdate();
            if (inserted == 0) throw new SQLException("Selected event was not found.");
        }
    }

    public List<Object[]> getRegistrations(String eventFilter, int limit, int offset)
            throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT r.registration_id, r.student_id, r.student_name, "
                   + "e.event_name, r.registered_at "
                   + "FROM registrations r JOIN events e ON r.event_id=e.event_id "
                   + "WHERE (? = 'All' OR e.event_name = ?) "
                   + "ORDER BY r.registration_id DESC LIMIT ? OFFSET ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eventFilter);
            ps.setString(2, eventFilter);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getInt("registration_id"),
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("event_name"),
                        rs.getTimestamp("registered_at")
                    });
                }
            }
        }
        return rows;
    }
}
