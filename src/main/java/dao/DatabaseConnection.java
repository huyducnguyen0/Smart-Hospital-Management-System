package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Đã xóa integratedSecurity, thêm lại User và Password
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=hospital_management;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456"; // ĐIỀN MẬT KHẨU CỦA BẠN VÀO ĐÂY

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Ngon rồi! Kết nối SQL Server thành công mĩ mãn!");
        } catch (SQLException e) {
            System.out.println("Lỗi rồi:");
            e.printStackTrace();
        }
    }
}