package controller;

import model.Patient;
import java.util.Comparator;
import java.util.PriorityQueue;

public class EmergencyRoom {
    // Khai báo hàng đợi ưu tiên
    private PriorityQueue<Patient> waitingQueue;

    public EmergencyRoom() {
        // Định nghĩa "Luật ưu tiên" (Comparator)
        Comparator<Patient> priorityRule = new Comparator<Patient>() {
            @Override
            public int compare(Patient p1, Patient p2) {
                // Luật 1: Ưu tiên trạng thái Cấp cứu
                // Trả về -1 nghĩa là p1 được xếp lên trước, 1 là p2 lên trước
                if (p1.isEmergency() && !p2.isEmergency()) return -1;
                if (!p1.isEmergency() && p2.isEmergency()) return 1;

                // Luật 2: Nếu cả 2 cùng Cấp cứu, hoặc cùng Bình thường -> Ưu tiên người lớn tuổi hơn
                // Trong Java, LocalDate so sánh bằng compareTo: Ngày sinh nhỏ hơn (sinh trước) sẽ được ưu tiên
                return p1.getDob().compareTo(p2.getDob());
            }
        };

        // Khởi tạo hàng đợi với luật vừa tạo
        this.waitingQueue = new PriorityQueue<>(priorityRule);
    }

    // Hàm nhận bệnh nhân vào hàng đợi
    public void admitPatient(Patient patient) {
        waitingQueue.add(patient);
        System.out.println("Đã bốc số cho: " + patient.getName() + (patient.isEmergency() ? " [CẤP CỨU]" : " [Bình thường]"));
    }

    // Hàm gọi bệnh nhân tiếp theo vào phòng khám (Bác sĩ bấm nút "Next")
    // Sửa chữ 'void' thành 'Patient' để hàm có thể ném đối tượng ra ngoài
    public Patient callNextPatient() {
        // Hàm poll() sẽ lấy người có độ ưu tiên cao nhất ra khỏi hàng đợi và trả về
        return waitingQueue.poll();
    }
}