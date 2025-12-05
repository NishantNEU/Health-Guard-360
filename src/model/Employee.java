package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Employee class - extends Person
 * Represents an employee working in an organization
 */
public class Employee extends Person implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private String employeeId;
    private String organizationId; // ID of organization employee works in
    private Role role;
    private double salary;
    private LocalDate hireDate;
    private boolean isActive;
    
    /**
     * Complete constructor
     */
    public Employee(Person person, Role role, String organizationId) {
        super(person.getFirstName(), person.getLastName(), person.getDateOfBirth(),
              person.getGender(), person.getEmail(), person.getPhoneNumber(), person.getAddress());
        this.employeeId = generateEmployeeId();
        this.role = role;
        this.organizationId = organizationId;
        this.salary = 0.0;
        this.hireDate = LocalDate.now();
        this.isActive = true;
    }
    
    /**
     * Simple constructor (for quick creation)
     */
    public Employee(String firstName, String lastName, Role role, String organizationId) {
        super();
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.employeeId = generateEmployeeId();
        this.role = role;
        this.organizationId = organizationId;
        this.salary = 0.0;
        this.hireDate = LocalDate.now();
        this.isActive = true;
    }
    
    /**
     * Default constructor
     */
    public Employee() {
        super();
        this.employeeId = generateEmployeeId();
        this.role = Role.PATIENT;
        this.organizationId = "";
        this.salary = 0.0;
        this.hireDate = LocalDate.now();
        this.isActive = true;
    }
    
    /**
     * Generate unique employee ID
     */
    private String generateEmployeeId() {
        return "EMP-" + System.currentTimeMillis();
    }
    
    // Getters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public Role getRole() {
        return role;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    // Setters
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return getFullName() + " (" + role + ")";
    }
}