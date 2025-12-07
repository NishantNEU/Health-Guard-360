/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import business.SystemData;
import model.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sandeep Patil
 */
public class ManageEmployeesPanel extends javax.swing.JPanel {

    /**
     * Creates new form ManageEmployeesPanel
     */
    private SystemData systemData;

    public ManageEmployeesPanel() {
        initComponents();
        systemData = SystemData.getInstance();
        customInit();
    }

    private void customInit() {
        // Populate dropdowns
        loadEnterprises();
        loadPersons();

        // Set table to non-editable
        employeesTable.setEnabled(false);

        // Load existing employees
        loadEmployeesTable();
    }

    private void loadEnterprises() {
        enterpriseComboBox.removeAllItems();
        enterpriseComboBox.addItem("-- Select Enterprise --");

        for (model.Enterprise enterprise : systemData.getEnterpriseDirectory().getAllEnterprises()) {
            enterpriseComboBox.addItem(enterprise.getEnterpriseName());
        }
    }

    private void loadOrganizations() {
        organizationComboBox.removeAllItems();
        organizationComboBox.addItem("-- Select Organization --");

        String selectedEnterprise = (String) enterpriseComboBox.getSelectedItem();
        if (selectedEnterprise == null || selectedEnterprise.equals("-- Select Enterprise --")) {
            return;
        }

        // Find the enterprise by name
        model.Enterprise enterprise = null;
        for (model.Enterprise ent : systemData.getEnterpriseDirectory().getAllEnterprises()) {
            if (ent.getEnterpriseName().equals(selectedEnterprise)) {
                enterprise = ent;
                break;
            }
        }

        if (enterprise != null) {
            // Load organizations belonging to this enterprise
            for (model.Organization org : systemData.getOrganizationDirectory().getAllOrganizations()) {
                if (org.getEnterpriseId().equals(enterprise.getEnterpriseId())) {
                    organizationComboBox.addItem(org.getOrganizationName());
                }
            }
        }
    }

    private void loadPersons() {
        personComboBox.removeAllItems();
        personComboBox.addItem("-- Select Person --");

        // Load only users who are NOT patients (employees only)
        for (model.User user : systemData.getUserDirectory().getAllUsers()) {
            // Skip patient role
            if (user.getRole() != model.Role.PATIENT) {
                personComboBox.addItem(user.getUsername() + " - " + user.getPerson().getFirstName() + " "
                        + user.getPerson().getLastName());
            }
        }
    }

    private void loadEmployeesTable() {
        DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
        model.setRowCount(0); // Clear existing rows

        // Get all enterprises from SystemData
        for (int i = 0; i < systemData.getEnterpriseDirectory().getAllEnterprises().size(); i++) {
            model.Enterprise enterprise = systemData.getEnterpriseDirectory().getAllEnterprises().get(i);

            // Find organizations for this enterprise
            for (int j = 0; j < systemData.getOrganizationDirectory().getAllOrganizations().size(); j++) {
                model.Organization organization = systemData.getOrganizationDirectory().getAllOrganizations().get(j);

                // Check if organization belongs to this enterprise
                if (organization.getEnterpriseId().equals(enterprise.getEnterpriseId())) {
                    // Get all employee IDs in this organization
                    for (String employeeId : organization.getEmployeeIds()) {
                        // Find employee details from UserDirectory
                        model.Employee employee = findEmployeeById(employeeId);
                        if (employee != null) {
                            Object[] row = new Object[5];
                            row[0] = employee.getEmployeeId();
                            row[1] = employee.getFullName();
                            row[2] = organization.getOrganizationName();
                            row[3] = employee.getRole().toString();
                            row[4] = enterprise.getEnterpriseName();
                            model.addRow(row);
                        }
                    }
                }
            }
        }
    }

    private model.Employee findEmployeeById(String employeeId) {
        // Search through all users to find the employee
        for (model.User user : systemData.getUserDirectory().getAllUsers()) {
            if (user.getPerson() instanceof model.Employee) {
                model.Employee emp = (model.Employee) user.getPerson();
                if (emp.getEmployeeId().equals(employeeId)) {
                    return emp;
                }
            }
        }
        return null;
    }

    private void assignEmployee() {
        // Validate selections
        String selectedEnterprise = (String) enterpriseComboBox.getSelectedItem();
        String selectedOrganization = (String) organizationComboBox.getSelectedItem();
        String selectedPerson = (String) personComboBox.getSelectedItem();
        String selectedRole = (String) roleComboBox.getSelectedItem();

        if (selectedEnterprise == null || selectedEnterprise.equals("-- Select Enterprise --")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an enterprise", "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if role is enterprise-level (Claims Processor doesn't need
        // organization)
        boolean isEnterpriseLevel = selectedRole.equals("Claims Processor");

        if (!isEnterpriseLevel) {
            if (selectedOrganization == null || selectedOrganization.equals("-- Select Organization --")) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please select an organization", "Validation Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (selectedPerson == null || selectedPerson.equals("-- Select Person --")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a person", "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedRole == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a role", "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Find the organization (only if not enterprise-level role)
            model.Organization organization = null;
            if (!isEnterpriseLevel) {
                for (model.Organization org : systemData.getOrganizationDirectory().getAllOrganizations()) {
                    if (org.getOrganizationName().equals(selectedOrganization)) {
                        organization = org;
                        break;
                    }
                }

                if (organization == null) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Organization not found", "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Find the enterprise
            model.Enterprise enterprise = null;
            for (model.Enterprise ent : systemData.getEnterpriseDirectory().getAllEnterprises()) {
                if (ent.getEnterpriseName().equals(selectedEnterprise)) {
                    enterprise = ent;
                    break;
                }
            }

            if (enterprise == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Enterprise not found", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extract username from selected person (format: "username - FirstName
            // LastName")
            String username = selectedPerson.split(" - ")[0];
            model.User user = systemData.getUserDirectory().findUserByUsername(username);

            if (user == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "User not found", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convert role string to Role enum
            String roleStr = selectedRole.toUpperCase().replace(" ", "_");
            model.Role role = model.Role.valueOf(roleStr);

            // Create employee from person
            // For enterprise-level roles, use enterprise ID instead of organization ID
            String orgIdForEmployee = isEnterpriseLevel ? enterprise.getEnterpriseId()
                    : organization.getOrganizationId();
            model.Employee employee = new model.Employee(user.getPerson(), role, orgIdForEmployee);

            // Add employee ID to organization (only if not enterprise-level)
            if (!isEnterpriseLevel && organization != null) {
                organization.addEmployee(employee.getEmployeeId());
            } else {
                // For enterprise-level roles, add to enterprise's employee list
                enterprise.addEmployee(employee.getEmployeeId());
            }

            // Update user's person to be an employee
            user.setPerson(employee);

            // Success message
            String assignmentLevel = isEnterpriseLevel ? "Enterprise: " + enterprise.getEnterpriseName()
                    : "Organization: " + organization.getOrganizationName();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Employee assigned successfully!\nEmployee ID: " + employee.getEmployeeId() + "\nAssigned to: "
                            + assignmentLevel,
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            // Refresh table
            loadEmployeesTable();

            // Clear form
            clearForm();

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error assigning employee: " + ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        enterpriseComboBox.setSelectedIndex(0);
        organizationComboBox.setSelectedIndex(0);
        personComboBox.setSelectedIndex(0);
        roleComboBox.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        assignEmployeePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        enterpriseComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        organizationComboBox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        personComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        assignEmployeeBtn = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1150, 720));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Manage Employees");

        jLabel2.setText("View and assign employees to organizations");

        jLabel3.setText("Assign Employee to Organization ");

        jLabel4.setText("Select Enterprise:");

        enterpriseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Enterprise --" }));
        enterpriseComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterpriseComboBoxActionPerformed(evt);
            }
        });

        jLabel5.setText("Select Organization:");

        organizationComboBox
                .setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Organization --" }));

        jLabel6.setText("Select Person: ");

        personComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Person --" }));

        jLabel7.setText("Employee Role: ");

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Manager", "Staff", "Doctor ", "Nurse", "Claims Processor", "Pharmacist" }));

        assignEmployeeBtn.setText("Assign Employee");
        assignEmployeeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignEmployeeBtnActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout assignEmployeePanelLayout = new javax.swing.GroupLayout(assignEmployeePanel);
        assignEmployeePanel.setLayout(assignEmployeePanelLayout);
        assignEmployeePanelLayout.setHorizontalGroup(
                assignEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(assignEmployeePanelLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(assignEmployeePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addGroup(assignEmployeePanelLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(assignEmployeePanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(assignEmployeePanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addGap(26, 26, 26)
                                                                .addComponent(enterpriseComboBox,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(assignEmployeePanelLayout.createSequentialGroup()
                                                                .addGroup(assignEmployeePanelLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel5)
                                                                        .addComponent(jLabel6)
                                                                        .addGroup(assignEmployeePanelLayout
                                                                                .createParallelGroup(
                                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(assignEmployeeBtn)
                                                                                .addComponent(jLabel7)))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(assignEmployeePanelLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(assignEmployeePanelLayout
                                                                                .createSequentialGroup()
                                                                                .addGap(6, 6, 6)
                                                                                .addComponent(clearButton))
                                                                        .addComponent(personComboBox,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(organizationComboBox,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(roleComboBox,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap(560, Short.MAX_VALUE)));
        assignEmployeePanelLayout.setVerticalGroup(
                assignEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(assignEmployeePanelLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(assignEmployeePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(enterpriseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(assignEmployeePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(organizationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(assignEmployeePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(personComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(assignEmployeePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(assignEmployeePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(assignEmployeeBtn)
                                        .addComponent(clearButton))
                                .addContainerGap(21, Short.MAX_VALUE)));

        jLabel8.setText("Current Employees");

        employeesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Emp ID", "Name", "Organization", "Role", "Enterprise"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(employeesTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 760,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(assignEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel2))
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(assignEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(190, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void assignEmployeeBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_assignEmployeeBtnActionPerformed
        // TODO add your handling code here:
        assignEmployee();
    }// GEN-LAST:event_assignEmployeeBtnActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        clearForm();
    }// GEN-LAST:event_clearButtonActionPerformed

    private void enterpriseComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        loadOrganizations();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton assignEmployeeBtn;
    private javax.swing.JPanel assignEmployeePanel;
    private javax.swing.JButton clearButton;
    private javax.swing.JTable employeesTable;
    private javax.swing.JComboBox<String> enterpriseComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> organizationComboBox;
    private javax.swing.JComboBox<String> personComboBox;
    private javax.swing.JComboBox<String> roleComboBox;
    // End of variables declaration//GEN-END:variables
}
