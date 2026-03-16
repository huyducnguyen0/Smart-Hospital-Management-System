import controller.EmergencyRoom;
import model.Patient;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EmergencyRoom room = new EmergencyRoom();

        // Tạo ra 4 bệnh nhân đến bệnh viện lộn xộn
        Patient thanhNien = new Patient(1, "Thanh Niên Khỏe", "0111", LocalDate.of(2000, 1, 1), "Đau đầu", false);
        Patient cuOng = new Patient(2, "Cụ Ông 80 tuổi", "0222", LocalDate.of(1946, 5, 5), "Đau khớp", false);
        Patient capCuu1 = new Patient(3, "Bệnh nhân Nhồi máu cơ tim", "0333", LocalDate.of(1980, 2, 2), "Ngất xỉu", true);
        Patient capCuu2 = new Patient(4, "Bà lão Cấp cứu", "0444", LocalDate.of(1930, 8, 8), "Khó thở", true);

        System.out.println("--- BỆNH NHÂN ĐẾN BỐC SỐ ---");
        // Giả sử họ đến quầy lễ tân theo đúng thứ tự này:
        room.admitPatient(thanhNien); // Đến đầu tiên
        room.admitPatient(cuOng);     // Đến thứ 2
        room.admitPatient(capCuu1);   // Đến thứ 3
        room.admitPatient(capCuu2);   // Đến thứ 4

        System.out.println("\n--- BÁC SĨ BẮT ĐẦU GỌI KHÁM ---");
        // Gọi 4 lần để xem ai được vào trước
        room.callNextPatient();
        room.callNextPatient();
        room.callNextPatient();
        room.callNextPatient();

        // Gọi lần 5 xem hệ thống xử lý sao khi hết khách
        room.callNextPatient();
    }
}