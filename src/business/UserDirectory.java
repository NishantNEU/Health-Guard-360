package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Person;
import model.Role;
import model.User;

/**
 * Complete UserDirectory class
 * Manages all users and authentication
 */
public class UserDirectory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private List<User> users;
    
    /**
     * Constructor
     */
    public UserDirectory() {
        this.users = new ArrayList<>();
        createDefaultUsers(); // Create default admin and test users
    }
    
    /**
     * Create default users for testing
     */
    private void createDefaultUsers() {
        // 1. System Admin user
        Person adminPerson = new Person();
        adminPerson.setFirstName("System");
        adminPerson.setLastName("Administrator");
        User adminUser = new User("admin", "admin123", Role.SYSTEM_ADMIN, adminPerson);
        users.add(adminUser);
        
        // 2. Patient users
        Person patientPerson1 = new Person();
        patientPerson1.setFirstName("John");
        patientPerson1.setLastName("Doe");
        User patientUser1 = new User("patient", "patient123", Role.PATIENT, patientPerson1);
        users.add(patientUser1);
        
        Person patientPerson2 = new Person();
        patientPerson2.setFirstName("Sarah");
        patientPerson2.setLastName("Johnson");
        User patientUser2 = new User("sarah", "pass123", Role.PATIENT, patientPerson2);
        users.add(patientUser2);
        
        // 3. Doctor users
        Person doctorPerson1 = new Person();
        doctorPerson1.setFirstName("Dr. Emily");
        doctorPerson1.setLastName("Smith");
        User doctorUser1 = new User("doctor", "doctor123", Role.DOCTOR, doctorPerson1);
        users.add(doctorUser1);
        
        Person doctorPerson2 = new Person();
        doctorPerson2.setFirstName("Dr. Michael");
        doctorPerson2.setLastName("Chen");
        User doctorUser2 = new User("drchen", "pass123", Role.DOCTOR, doctorPerson2);
        users.add(doctorUser2);
        
        // 4. Nurse users
        Person nursePerson1 = new Person();
        nursePerson1.setFirstName("Maria");
        nursePerson1.setLastName("Garcia");
        User nurseUser1 = new User("nurse", "nurse123", Role.NURSE, nursePerson1);
        users.add(nurseUser1);
        
        Person nursePerson2 = new Person();
        nursePerson2.setFirstName("Jennifer");
        nursePerson2.setLastName("Williams");
        User nurseUser2 = new User("jennifer", "pass123", Role.NURSE, nursePerson2);
        users.add(nurseUser2);
        
        // 5. Hospital Admin
        Person hospitalAdminPerson = new Person();
        hospitalAdminPerson.setFirstName("Robert");
        hospitalAdminPerson.setLastName("Thompson");
        User hospitalAdminUser = new User("hospitaladmin", "admin123", Role.HOSPITAL_ADMIN, hospitalAdminPerson);
        users.add(hospitalAdminUser);
        
        // 6. Claims Processor users
        Person claimsPerson1 = new Person();
        claimsPerson1.setFirstName("Lisa");
        claimsPerson1.setLastName("Anderson");
        User claimsUser1 = new User("claimsprocessor", "claims123", Role.CLAIMS_PROCESSOR, claimsPerson1);
        users.add(claimsUser1);
        
        Person claimsPerson2 = new Person();
        claimsPerson2.setFirstName("David");
        claimsPerson2.setLastName("Martinez");
        User claimsUser2 = new User("davidm", "pass123", Role.CLAIMS_PROCESSOR, claimsPerson2);
        users.add(claimsUser2);
        
        // 7. Insurance Admin
        Person insuranceAdminPerson = new Person();
        insuranceAdminPerson.setFirstName("Patricia");
        insuranceAdminPerson.setLastName("Brown");
        User insuranceAdminUser = new User("insuranceadmin", "admin123", Role.INSURANCE_ADMIN, insuranceAdminPerson);
        users.add(insuranceAdminUser);
        
        // 8. Pharmacist users
        Person pharmacistPerson1 = new Person();
        pharmacistPerson1.setFirstName("James");
        pharmacistPerson1.setLastName("Wilson");
        User pharmacistUser1 = new User("pharmacist", "pharm123", Role.PHARMACIST, pharmacistPerson1);
        users.add(pharmacistUser1);
        
        Person pharmacistPerson2 = new Person();
        pharmacistPerson2.setFirstName("Rachel");
        pharmacistPerson2.setLastName("Taylor");
        User pharmacistUser2 = new User("rachel", "pass123", Role.PHARMACIST, pharmacistPerson2);
        users.add(pharmacistUser2);
        
        // 9. Pharmacy Technician
        Person pharmTechPerson = new Person();
        pharmTechPerson.setFirstName("Kevin");
        pharmTechPerson.setLastName("Lee");
        User pharmTechUser = new User("pharmtech", "tech123", Role.PHARMACY_TECHNICIAN, pharmTechPerson);
        users.add(pharmTechUser);
        
        // 10. Pharmacy Admin
        Person pharmacyAdminPerson = new Person();
        pharmacyAdminPerson.setFirstName("Amanda");
        pharmacyAdminPerson.setLastName("Davis");
        User pharmacyAdminUser = new User("pharmacyadmin", "admin123", Role.PHARMACY_ADMIN, pharmacyAdminPerson);
        users.add(pharmacyAdminUser);
        
        // 11. Underwriter
        Person underwriterPerson = new Person();
        underwriterPerson.setFirstName("Christopher");
        underwriterPerson.setLastName("Moore");
        User underwriterUser = new User("underwriter", "under123", Role.UNDERWRITER, underwriterPerson);
        users.add(underwriterUser);
        
        // 12. Supplier Admin
        Person supplierAdminPerson = new Person();
        supplierAdminPerson.setFirstName("Michelle");
        supplierAdminPerson.setLastName("White");
        User supplierAdminUser = new User("supplieradmin", "admin123", Role.SUPPLIER_ADMIN, supplierAdminPerson);
        users.add(supplierAdminUser);
        
        // 13. Supply Chain Manager
        Person supplyManagerPerson = new Person();
        supplyManagerPerson.setFirstName("Brian");
        supplyManagerPerson.setLastName("Clark");
        User supplyManagerUser = new User("supplymanager", "supply123", Role.SUPPLIER_MANAGER, supplyManagerPerson);
        users.add(supplyManagerUser);
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Create and add new user
     */
    public User createUser(String username, String password, Role role, Person person) {
        User user = new User(username, password, role, person);
        users.add(user);
        return user;
    }
    
    /**
     * Add existing user
     */
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }
    
    /**
     * Remove user
     */
    public boolean removeUser(String userId) {
        return users.removeIf(u -> u.getUserId().equals(userId));
    }
    
    /**
     * Find user by ID
     */
    public User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Find user by username
     */
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Check if username already exists
     */
    public boolean isUsernameExists(String username) {
        return users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
    
    /**
     * Authenticate user (login)
     */
    public User authenticate(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.isActive() && user.verifyPassword(password)) {
            user.recordLogin();
            return user;
        }
        return null;
    }
    
    /**
     * Get users by role
     */
    public List<User> getUsersByRole(Role role) {
        return users.stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active users
     */
    public List<User> getActiveUsers() {
        return users.stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all inactive users
     */
    public List<User> getInactiveUsers() {
        return users.stream()
                .filter(u -> !u.isActive())
                .collect(Collectors.toList());
    }
    
    /**
     * Get user count
     */
    public int getUserCount() {
        return users.size();
    }
    
    /**
     * Get active user count
     */
    public int getActiveUserCount() {
        return (int) users.stream().filter(User::isActive).count();
    }
    
    /**
     * Deactivate user
     */
    public boolean deactivateUser(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.deactivate();
            return true;
        }
        return false;
    }
    
    /**
     * Activate user
     */
    public boolean activateUser(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.activate();
            return true;
        }
        return false;
    }
    
    /**
     * Change user password
     */
    public boolean changeUserPassword(String userId, String oldPassword, String newPassword) {
        User user = findUserById(userId);
        if (user != null) {
            try {
                user.changePassword(oldPassword, newPassword);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Get usernames for dropdown (UI helper)
     */
    public List<String> getUsernamesForDropdown() {
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all users (for testing)
     */
    public void clearAll() {
        users.clear();
        createDefaultUsers(); // Recreate default users
    }
    
    /**
     * Check if directory is empty
     */
    public boolean isEmpty() {
        return users.isEmpty();
    }
    
    /**
     * Find user by person ID
     */
    public User findUserByPersonId(String personId) {
        for (User user : users) {
            if (user.getPerson().getPersonId().equals(personId)) {
                return user;
            }
        }
        return null;
    }
}