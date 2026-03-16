package dao;

import model.Doctor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE role = 1"; // Role 1 là Bác sĩ

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("full_name");
                String phone = rs.getString("phone");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                String specialty = rs.getString("specialty"); // Lấy chuyên khoa

                list.add(new Doctor(id, name, phone, dob, specialty));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}