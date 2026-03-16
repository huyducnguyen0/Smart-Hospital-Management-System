package dao;

import model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    // Hàm này sẽ moi toàn bộ dịch vụ từ DB lên để Bác sĩ chọn
    public List<Service> getAllServices() {
        List<Service> serviceList = new ArrayList<>();
        String sql = "SELECT * FROM Services";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("service_id");
                String name = rs.getString("service_name");
                double price = rs.getDouble("price");

                serviceList.add(new Service(id, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceList;
    }
}