package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Implements IBillable để bắt buộc Lịch khám phải tính được tiền
public class Appointment implements IBillable {
    private int appointmentId;
    private Doctor doctor;   // Chứa đối tượng Bác sĩ
    private Patient patient; // Chứa đối tượng Bệnh nhân
    private LocalDateTime appointmentDate;
    private String status;
    private List<Service> services; // Danh sách các dịch vụ được chỉ định

    public Appointment(int appointmentId, Doctor doctor, Patient patient, LocalDateTime appointmentDate) {
        this.appointmentId = appointmentId;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        this.status = "Pending";
        this.services = new ArrayList<>(); // Khởi tạo danh sách rỗng
    }

    // Hàm thêm dịch vụ vào lịch khám
    public void addService(Service service) {
        this.services.add(service);
    }

    // Ghi đè hàm tính tiền từ IBillable
    @Override
    public double calculateTotal() {
        double total = 0;
        // Duyệt qua tất cả dịch vụ và cộng dồn tiền lại
        for (Service s : services) {
            total += s.getPrice();
        }

        // Logic phụ thu: Nếu bệnh nhân thuộc diện Cấp cứu, tăng thêm 20% phí dịch vụ
        if (patient.isEmergency()) {
            total = total * 1.2;
        }
        return total;
    }

    // Hàm in thông tin hóa đơn ra màn hình
    public void printInvoice() {
        System.out.println("=== HÓA ĐƠN KHÁM BỆNH ===");
        System.out.println("Mã LH: " + appointmentId + " | Ngày: " + appointmentDate);
        System.out.println("Bác sĩ phụ trách: " + doctor.getName());
        System.out.println("Bệnh nhân: " + patient.getName() + (patient.isEmergency() ? " (CẤP CỨU)" : ""));
        System.out.println("Các dịch vụ đã sử dụng:");
        for (Service s : services) {
            System.out.println(" - " + s.getName() + ": " + s.getPrice() + " VNĐ");
        }
        System.out.println("-------------------------");
        System.out.println("TỔNG THANH TOÁN: " + calculateTotal() + " VNĐ");
        System.out.println("=========================\n");
    }
    // Thêm hàm này vào cuối class Appointment
    public List<Service> getServices() {
        return services;
    }

    public int getAppointmentId() {
        return appointmentId;
    }
    // Thêm 3 hàm Getter này vào để lấy thông tin Bác sĩ, Bệnh nhân và Ngày khám
    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }
}