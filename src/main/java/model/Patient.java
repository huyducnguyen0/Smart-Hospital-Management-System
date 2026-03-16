package model;

import java.time.LocalDate;

public class Patient extends Person {
    private String medicalHistory;
    private boolean isEmergency;

    public Patient(int id, String name, String phone, LocalDate dob, String medicalHistory, boolean isEmergency) {
        super(id, name, phone, dob);
        this.medicalHistory = medicalHistory;
        this.isEmergency = isEmergency;
    }

    @Override
    public void displayInfo() {
        String type = isEmergency ? "[CẤP CỨU]" : "[Bình thường]";
        System.out.println("Bệnh nhân: " + getName() + " " + type);
    }

    // Getters and Setters
    public boolean isEmergency() { return isEmergency; }
    public void setEmergency(boolean emergency) { isEmergency = emergency; }
}