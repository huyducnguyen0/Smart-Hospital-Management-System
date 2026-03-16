package dao;

import model.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // 1. HÀM LƯU XUỐNG DB (Đã cập nhật thêm cột is_emergency)
    public void addPatient(Patient patient) {
        String sql = "INSERT INTO Users (full_name, phone, dob, role, username, password, is_emergency, medical_history) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getPhone());
            pstmt.setDate(3, java.sql.Date.valueOf(patient.getDob()));
            pstmt.setInt(4, 2); // Role = 2 là Patient
            pstmt.setString(5, patient.getPhone()); // Username tạm thời
            pstmt.setString(6, "123456");           // Password mặc định
            pstmt.setBoolean(7, patient.isEmergency()); // Lấy cờ cấp cứu
            pstmt.setString(8, "Chưa có");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. HÀM MÓC DỮ LIỆU TỪ DB LÊN (HÀM MỚI)
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        // Chỉ lấy những người có role = 2 (Bệnh nhân)
        String sql = "SELECT * FROM Users WHERE role = 2";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Lôi từng cột dưới DB lên
                int id = rs.getInt("user_id");
                String name = rs.getString("full_name");
                String phone = rs.getString("phone");
                // Chuyển từ java.sql.Date sang LocalDate của Java 8
                LocalDate dob = rs.getDate("dob").toLocalDate();
                boolean isEmergency = rs.getBoolean("is_emergency");
                String history = rs.getString("medical_history");

                // Đúc thành đối tượng Patient và nhét vào giỏ
                Patient p = new Patient(id, name, phone, dob, history, isEmergency);
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}