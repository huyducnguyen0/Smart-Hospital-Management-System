package dao;

import model.Appointment;
import model.Service;
import java.sql.*;

public class AppointmentDAO {

    public void addAppointment(Appointment app) {
        String sqlApp = "INSERT INTO Appointments (doctor_id, patient_id, appointment_date, status) VALUES (?, ?, ?, ?)";
        String sqlDetails = "INSERT INTO Appointment_Details (appointment_id, service_id, quantity) VALUES (?, ?, ?)";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();

            // 1. TẮT CHẾ ĐỘ AUTO-COMMIT (Bật chế độ Transaction)
            // Tức là: "Đợi tao làm xong hết cả 2 bảng rồi mới lưu thật nhé!"
            conn.setAutoCommit(false);

            // 2. LƯU VÀO BẢNG APPOINTMENTS
            // RETURN_GENERATED_KEYS: Lệnh này bảo SQL Server trả về cái ID vừa tự động tăng cho tao xin
            PreparedStatement pstmtApp = conn.prepareStatement(sqlApp, Statement.RETURN_GENERATED_KEYS);
            // Gọi qua các hàm Getter vừa tạo
            pstmtApp.setInt(1, app.getDoctor().getId());
            pstmtApp.setInt(2, app.getPatient().getId());
            pstmtApp.setTimestamp(3, Timestamp.valueOf(app.getAppointmentDate()));
            pstmtApp.setString(4, "Completed"); // Khám xong rồi

            pstmtApp.executeUpdate();

            // Lấy cái ID vừa được SQL Server tạo ra
            ResultSet rsKeys = pstmtApp.getGeneratedKeys();
            int newAppointmentId = -1;
            if (rsKeys.next()) {
                newAppointmentId = rsKeys.getInt(1); // Lấy khóa chính vừa sinh ra
            }

            // 3. LƯU VÀO BẢNG APPOINTMENT_DETAILS (BẢNG TRUNG GIAN)
            if (newAppointmentId != -1) {
                PreparedStatement pstmtDetails = conn.prepareStatement(sqlDetails);

                // Duyệt qua từng dịch vụ trong hóa đơn để lưu
                for (Service srv : app.getServices()) {
                    pstmtDetails.setInt(1, newAppointmentId); // Nhét cái ID hóa đơn vừa lấy được ở trên vào đây
                    pstmtDetails.setInt(2, srv.getId());      // ID của dịch vụ
                    pstmtDetails.setInt(3, 1);                // Số lượng mặc định là 1

                    pstmtDetails.executeUpdate(); // Lưu từng dòng
                }
            }

            // 4. CHỐT ĐƠN! TẤT CẢ ĐỀU NGON LÀNH THÌ LƯU VÀO Ổ CỨNG
            conn.commit();
            System.out.println(">>> Đã lưu thành công Hóa đơn và Chi tiết dịch vụ xuống Database!");

        } catch (SQLException e) {
            // NẾU CÓ LỖI Ở BẤT KỲ ĐÂU -> QUAY XE (Hủy bỏ tất cả, không lưu gì hết)
            System.out.println("Tịt rồi! Lỗi khi lưu Hóa đơn. Đang Rollback...");
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Nhớ đóng cửa nhà kho
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}