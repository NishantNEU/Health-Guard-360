package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Complete Person base class
 * Parent class for Patient and Employee
 */
public class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private String personId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private Address address;
    private LocalDate createdDate;
    
    /**
     * Complete constructor
     */
    public Person(String firstName, String lastName, LocalDate dateOfBirth, 
                  String gender, String email, String phoneNumber, Address address) {
        this.personId = generatePersonId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdDate = LocalDate.now();
    }
    
    /**
     * Default constructor
     */
    public Person() {
        this.personId = generatePersonId();
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = LocalDate.now();
        this.gender = "Not Specified";
        this.email = "";
        this.phoneNumber = "";
        this.address = new Address();
        this.createdDate = LocalDate.now();
    }
    
    /**
     * Generate unique person ID
     */
    private String generatePersonId() {
        return "PER-" + System.currentTimeMillis();
    }
    
    // Getters
    public String getPersonId() {
        return personId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    // Setters
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    /**
     * Get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Calculate age from date of birth
     */
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    /**
     * Get formatted date of birth
     */
    public String getFormattedDateOfBirth() {
        if (dateOfBirth == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return dateOfBirth.format(formatter);
    }
    
    /**
     * Validate email format
     */
    public boolean isEmailValid() {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validate phone number format (US format)
     */
    public boolean isPhoneNumberValid() {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        // Accepts: 1234567890, 123-456-7890, (123) 456-7890
        String phoneRegex = "^(\\d{10}|\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\)\\s*\\d{3}-\\d{4})$";
        return phoneNumber.matches(phoneRegex);
    }
    
    /**
     * Validate all required fields
     */
    public boolean isValid() {
        return !firstName.isEmpty() && 
               !lastName.isEmpty() && 
               dateOfBirth != null && 
               isEmailValid() && 
               isPhoneNumberValid() &&
               address != null && 
               address.isValid();
    }
    
    /**
     * Get contact information as formatted string
     */
    public String getContactInfo() {
        return "Email: " + email + "\n" +
               "Phone: " + phoneNumber + "\n" +
               "Address: " + (address != null ? address.getFormattedAddress() : "N/A");
    }
    
    @Override
    public String toString() {
        return getFullName() + " (" + personId + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return personId.equals(person.personId);
    }
    
    @Override
    public int hashCode() {
        return personId.hashCode();
    }
}