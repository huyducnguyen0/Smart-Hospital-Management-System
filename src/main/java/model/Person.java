package model;

import java.time.LocalDate;

public abstract class Person {
    private int id;
    private String name;
    private String phone;
    private LocalDate dob;

    public Person(int id, String name, String phone, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.dob = dob;
    }

    public abstract void displayInfo();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
}