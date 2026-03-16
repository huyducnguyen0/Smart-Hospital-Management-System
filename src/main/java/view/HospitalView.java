package view;

import controller.EmergencyRoom;
import model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

// Kế thừa JFrame để class này biến thành một cái Cửa sổ phần mềm
public class HospitalView extends JFrame {
    private EmergencyRoom room; // Gọi Controller vào đây

    // Các thành phần giao diện
    private JTextField txtName, txtPhone, txtYear;
    private JCheckBox chkEmergency;
    private JTextArea txtDisplay;

    public HospitalView() {
        room = new EmergencyRoom(); // Khởi tạo phòng khám
        // --- ĐOẠN CODE MỚI THÊM: TẢI DỮ LIỆU TỪ DATABASE ---
        dao.PatientDAO patientDAO = new dao.PatientDAO();
        java.util.List<model.Patient> oldPatients = patientDAO.getAllPatients();

        for (model.Patient p : oldPatients) {
            room.admitPatient(p); // Nhét từng người vào hàng đợi PriorityQueue
        }
        // ---------------------------------------------------

        // 1. Cấu hình Cửa sổ chính (Frame)
        setTitle("Hệ thống Quản lý Bệnh viện Thông minh");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Bấm X là tắt app
        setLocationRelativeTo(null); // Hiện cửa sổ ở giữa màn hình máy tính
        setLayout(new BorderLayout()); // Chia bố cục trên-dưới

        // 2. Khu vực phía trên: Nhập liệu (Panel Input)
        JPanel panelInput = new JPanel(new GridLayout(5, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Tiếp nhận Bệnh nhân"));

        panelInput.add(new JLabel("Tên bệnh nhân:"));
        txtName = new JTextField();
        panelInput.add(txtName);

        panelInput.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        panelInput.add(txtPhone);

        panelInput.add(new JLabel("Năm sinh (VD: 1980):"));
        txtYear = new JTextField();
        panelInput.add(txtYear);

        panelInput.add(new JLabel("Trạng thái:"));
        chkEmergency = new JCheckBox("CẤP CỨU MỨC ĐỘ NẶNG");
        chkEmergency.setForeground(Color.RED);
        panelInput.add(chkEmergency);

        // Nút Bốc số
        JButton btnAdd = new JButton("Bốc Số / Xếp Hàng");
        panelInput.add(btnAdd);

        // Nút Gọi khám
        JButton btnCall = new JButton("Bác Sĩ Gọi Khám");
        btnCall.setBackground(Color.GREEN);
        panelInput.add(btnCall);

        add(panelInput, BorderLayout.NORTH); // Gắn khu vực nhập liệu lên phía Bắc (trên cùng)

        // 3. Khu vực phía dưới: Màn hình hiển thị (Panel Display)
        txtDisplay = new JTextArea();
        txtDisplay.setEditable(false); // Chỉ để đọc, không cho người dùng gõ linh tinh vào
        txtDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // --- THÊM DÒNG NÀY ĐỂ BÁO CÁO SỐ LƯỢNG ---
        txtDisplay.append(">>> Đã tải thành công " + oldPatients.size() + " bệnh nhân từ Database!\n");
        txtDisplay.append("--------------------------------------------------\n");
        JScrollPane scrollPane = new JScrollPane(txtDisplay); // Thêm thanh cuộn
        scrollPane.setBorder(BorderFactory.createTitledBorder("Màn hình Thông báo"));
        add(scrollPane, BorderLayout.CENTER); // Gắn màn hình xuống giữa

        // 4. Bắt sự kiện khi bấm nút "Bốc Số"
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = txtName.getText();
                    String phone = txtPhone.getText();
                    int year = Integer.parseInt(txtYear.getText());
                    boolean isEmergency = chkEmergency.isSelected();

                    // Tạo bệnh nhân (Tạm thời fix cứng tháng/ngày sinh cho lẹ)
                    // ... (Các dòng lấy dữ liệu từ ô text giữ nguyên) ...

                        // Tạo đối tượng Bệnh nhân (RAM)
                    Patient p = new Patient((int)(Math.random()*100), name, phone, LocalDate.of(year, 1, 1), "Chưa có", isEmergency);

                    // 1. Đẩy vào hàng đợi PriorityQueue (Xử lý logic ưu tiên gọi khám)
                    room.admitPatient(p);

                    // 2. GỌI SHIPPER ĐẨY XUỐNG DATABASE (Lưu vĩnh viễn)
                    dao.PatientDAO patientDAO = new dao.PatientDAO();
                    patientDAO.addPatient(p);

                    // In ra màn hình View


                    // ... (Các dòng xóa trắng ô text giữ nguyên) ...

                    // Ném xuống Controller xử lý
                    room.admitPatient(p);

                    // In ra màn hình View
                    txtDisplay.append(">>> Đã tiếp nhận: " + name + (isEmergency ? " [CẤP CỨU]\n" : " [Bình thường]\n"));

                    // Xóa trắng các ô nhập liệu để nhập người tiếp theo
                    txtName.setText(""); txtPhone.setText(""); txtYear.setText(""); chkEmergency.setSelected(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi rồi! Vui lòng nhập đúng năm sinh bằng số.");
                }
            }
        });

        // 5. Bắt sự kiện khi bấm nút "Gọi Khám"
        // 5. Bắt sự kiện khi bấm nút "Gọi Khám"
        btnCall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Kéo người ưu tiên nhất khỏi hàng đợi
                Patient nextP = room.callNextPatient();

                if (nextP != null) {
                    txtDisplay.append("\n🔊 LOA: Mời bệnh nhân " + nextP.getName() + " vào phòng khám!\n");

                    // 2. Load Bác sĩ và Dịch vụ từ DB lên (Làm nhanh để test logic OOP)
                    dao.DoctorDAO docDAO = new dao.DoctorDAO();
                    java.util.List<model.Doctor> docs = docDAO.getAllDoctors();
                    model.Doctor assignedDoc = docs.get(0); // Bốc tạm ông bác sĩ đầu tiên làm nhiệm vụ

                    dao.ServiceDAO srvDAO = new dao.ServiceDAO();
                    java.util.List<model.Service> services = srvDAO.getAllServices();

                    // 3. TẠO HÓA ĐƠN (APPOINTMENT)
                    model.Appointment app = new model.Appointment((int)(Math.random()*1000), assignedDoc, nextP, java.time.LocalDateTime.now());

                    // Giả lập bác sĩ chỉ định 2 dịch vụ đầu tiên trong DB
                    if(services.size() >= 2) {
                        app.addService(services.get(0)); // Khám tổng quát
                        app.addService(services.get(1)); // Xét nghiệm máu
                    }

                    // 4. IN HÓA ĐƠN RA MÀN HÌNH VIEW (Test tính năng Đa hình calculateTotal)
                    txtDisplay.append("---------- HÓA ĐƠN KHÁM BỆNH ----------\n");
                    txtDisplay.append("Bác sĩ thực hiện: " + assignedDoc.getName() + " (" + assignedDoc.getSpecialty() + ")\n");

                    // Thử check xem cái logic +20% tiền cho Cấp cứu hôm trước có chạy chuẩn không
                    txtDisplay.append("Tổng thanh toán: " + String.format("%,.0f", app.calculateTotal()) + " VNĐ\n");
                    txtDisplay.append("---------------------------------------\n");
                    // ... (Đoạn in hóa đơn ra màn hình giữ nguyên) ...
                    txtDisplay.append("---------------------------------------\n");

                    // --- GỌI SHIPPER LƯU HÓA ĐƠN XUỐNG DATABASE ---
                    dao.AppointmentDAO appDAO = new dao.AppointmentDAO();
                    appDAO.addAppointment(app);
                    txtDisplay.append("💾 [Hệ thống]: Đã lưu hóa đơn vào CSDL!\n");
                } else {
                    txtDisplay.append("\n🔊 Hàng đợi trống. Bác sĩ có thể nghỉ ngơi!\n");
                }
            }
        });
    }

    // Hàm Main để chạy nguyên cái giao diện này
    public static void main(String[] args) {
        HospitalView view = new HospitalView();
        view.setVisible(true); // Lệnh này làm cái cửa sổ hiện lên
    }
}