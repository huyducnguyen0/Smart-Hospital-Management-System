package model;

import java.time.LocalDate;

public class Doctor extends Person {
    private String specialty;

    public Doctor(int id, String name, String phone, LocalDate dob, String specialty) {
        super(id, name, phone, dob); // Gọi constructor của class cha (Person)
        this.specialty = specialty;
    }

    @Override
    public void displayInfo() {
        System.out.println("Bác sĩ: " + getName() + " - Chuyên khoa: " + specialty);
    }

    // Getters and Setters
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}