/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

/**
 *
 * @author Sandeep Patil
 */
public class InsuranceProviderDashboard extends javax.swing.JPanel {

        /**
         * Creates new form InsuranceProviderDashboard
         */
        public InsuranceProviderDashboard() {
                initComponents();

                // FAILSAFE: If no data exists (e.g. fresh start or empty save), load samples
                business.SystemData systemData = business.SystemData.getInstance();
                if (systemData.getClaimDirectory().isEmpty()) {
                        System.out.println("⚠️ Dashboard: No claims found. Loading sample data...");
                        systemData.getClaimDirectory().reloadSampleData();
                }
                if (systemData.getPolicyDirectory().isEmpty()) {
                        System.out.println("⚠️ Dashboard: No policies found. Loading sample data...");
                        systemData.getPolicyDirectory().reloadSampleData();
                }

                // Load dashboard data
                loadUserInfo();
                loadDashboardStats();
                loadActivePolicies();
                loadRecentActivity();
                initializePolicyTypeCombo();

                // Add Action Listeners manually (using verbose syntax for compatibility)

                // 1. Active Policy Tab Buttons
                viewDetailsButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                viewDetailsButtonActionPerformed(evt);
                        }
                });
                exportToScvButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                exportToScvButtonActionPerformed(evt);
                        }
                });
                modifyCoverageButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                modifyCoverageButtonActionPerformed(evt);
                        }
                });

                // 2. Dashboard Overview Buttons (Redirects)
                if (viewPolicyApplicationButton != null) {
                        viewPolicyApplicationButton.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        viewPolicyApplicationButtonActionPerformed(evt);
                                }
                        });
                }
                if (managePoliciesButton != null) {
                        managePoliciesButton.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        managePoliciesButtonActionPerformed(evt);
                                }
                        });
                }

                // 3. Logout
                logoutButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                // Find parent frame and close/logout
                                java.awt.Window win = javax.swing.SwingUtilities
                                                .getWindowAncestor(InsuranceProviderDashboard.this);
                                if (win instanceof MainApplicationFrame) {
                                        ((MainApplicationFrame) win).showPanel("loginPanel");
                                }
                        }
                });
        }

        /**
         * Load current user information
         */
        private void loadUserInfo() {
                business.SystemData systemData = business.SystemData.getInstance();

                // Set user labels
                welcomeLabel.setText("Welcome: " + systemData.getCurrentUserFullName());
                roleLabel.setText(
                                "Role: " + (systemData.getCurrentUserRole() != null
                                                ? systemData.getCurrentUserRole().getDisplayName()
                                                : "Unknown"));
                enterpriseLabel.setText("Enterprise: Insurance Provider");
        }

        /**
         * Load dashboard statistics
         */
        private void loadDashboardStats() {
                business.SystemData systemData = business.SystemData.getInstance();

                // Get counts
                int totalClaims = systemData.getTotalClaimCount();
                int pendingClaims = systemData.getClaimDirectory()
                                .getClaimCountByStatus(model.Claim.ClaimStatus.SUBMITTED);
                int activePolicies = systemData.getPolicyDirectory().getActivePolicyCount();

                // Calculate processed today (claims processed today - approved/denied/paid)
                java.time.LocalDate today = java.time.LocalDate.now();
                long processedToday = systemData.getClaimDirectory().getAllClaims().stream()
                                .filter(c -> c.getProcessedDate() != null && c.getProcessedDate().equals(today))
                                .count();

                // Calculate total payouts
                double totalPayouts = systemData.getClaimDirectory().getAllClaims().stream()
                                .filter(c -> c.getClaimStatus() == model.Claim.ClaimStatus.APPROVED ||
                                                c.getClaimStatus() == model.Claim.ClaimStatus.PAID)
                                .mapToDouble(model.Claim::getApprovedAmount)
                                .sum();

                // Calculate approval rate
                long approvedCount = systemData.getClaimDirectory().getAllClaims().stream()
                                .filter(c -> c.getClaimStatus() == model.Claim.ClaimStatus.APPROVED ||
                                                c.getClaimStatus() == model.Claim.ClaimStatus.PAID)
                                .count();
                double approvalRate = totalClaims > 0 ? (approvedCount * 100.0 / totalClaims) : 0;

                // Update stat card labels (create value labels dynamically)
                updateStatCard(totalClaimsCard, String.valueOf(totalClaims));
                updateStatCard(pendingClaimsCard, String.valueOf(pendingClaims));
                updateStatCard(processedTodayCard, String.valueOf(processedToday));
                updateStatCard(activePoliciesCard, String.valueOf(activePolicies));
                updateStatCard(totalPayoutsCard, String.format("$%,.2f", totalPayouts));
                updateStatCard(approvalRateCard, String.format("%.1f%%", approvalRate));
        }

        /**
         * Helper to update stat card with value
         */
        /**
         * Helper to update stat card with value
         */
        private void updateStatCard(javax.swing.JPanel card, String value) {
                // Enforce GridLayout to ensure visibility of labels (Title top, Value bottom)
                if (!(card.getLayout() instanceof java.awt.GridLayout)) {
                        card.setLayout(new java.awt.GridLayout(2, 1));
                }

                // Check if value label already exists (component index 1)
                if (card.getComponentCount() > 1) {
                        java.awt.Component comp = card.getComponent(1);
                        if (comp instanceof javax.swing.JLabel) {
                                ((javax.swing.JLabel) comp).setText(value);
                                return;
                        }
                }

                // Add new value label if not exists
                javax.swing.JLabel valueLabel = new javax.swing.JLabel(value);
                valueLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24)); // Increased font size
                valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                card.add(valueLabel);
                card.revalidate();
                card.repaint();
        }

        /**
         * Load active policies into table
         */
        private void loadActivePolicies() {
                business.SystemData systemData = business.SystemData.getInstance();
                java.util.List<model.Policy> allPolicies = systemData.getPolicyDirectory().getAllPolicies();

                javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) policiesTable
                                .getModel();
                tableModel.setRowCount(0); // Clear existing rows

                java.time.LocalDate today = java.time.LocalDate.now();

                for (model.Policy policy : allPolicies) {
                        // Auto-expire policies
                        if (policy.getExpiryDate().isBefore(today)
                                        && policy.getPolicyStatus() == model.Policy.PolicyStatus.ACTIVE) {
                                policy.setPolicyStatus(model.Policy.PolicyStatus.EXPIRED);
                        }

                        // Get patient name
                        String patientName = getPatientName(policy.getPatientId());

                        // Calculate days until renewal
                        long daysUntilRenewal = java.time.temporal.ChronoUnit.DAYS.between(
                                        today, policy.getExpiryDate());

                        tableModel.addRow(new Object[] {
                                        policy.getPolicyNumber(),
                                        patientName,
                                        policy.getPolicyType().getDisplayName(),
                                        String.format("$%,.2f", policy.getCoverageAmount()),
                                        String.format("$%.2f", policy.getMonthlyPremium()),
                                        policy.getStartDate().toString(),
                                        policy.getExpiryDate().toString(),
                                        daysUntilRenewal > 0 ? daysUntilRenewal + " days" : "Expired",
                                        policy.getPolicyStatus().getDisplayName()
                        });
                }
        }

        /**
         * Load recent claims activity
         */
        private void loadRecentActivity() {
                business.SystemData systemData = business.SystemData.getInstance();
                java.util.List<model.Claim> recentClaims = systemData.getClaimDirectory().getAllClaims()
                                .stream()
                                .sorted((c1, c2) -> c2.getSubmittedDate().compareTo(c1.getSubmittedDate()))
                                .limit(10)
                                .collect(java.util.stream.Collectors.toList());

                javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) recentActivityTable
                                .getModel();
                tableModel.setRowCount(0); // Clear existing rows

                for (model.Claim claim : recentClaims) {
                        String patientName = getPatientName(claim.getPatientId());

                        tableModel.addRow(new Object[] {
                                        claim.getClaimNumber(),
                                        patientName,
                                        claim.getServiceDate().toString(),
                                        String.format("$%.2f", claim.getClaimAmount()),
                                        claim.getClaimStatus().getDisplayName()
                        });
                }
        }

        /**
         * Initialize policy type combo box
         */
        private void initializePolicyTypeCombo() {
                policyTypeCombo.removeAllItems();
                policyTypeCombo.addItem("All Types");
                for (model.Policy.PolicyType type : model.Policy.PolicyType.values()) {
                        policyTypeCombo.addItem(type.getDisplayName());
                }
        }

        /**
         * Helper to get patient name from ID
         */
        /**
         * Helper to get patient name from ID
         */
        private String getPatientName(String patientId) {
                // Handle dummy data for demo
                if (patientId != null && patientId.startsWith("PAT-")) {
                        return "Test Patient (" + patientId + ")";
                }

                // Try to find user by searching UserDirectory
                business.SystemData systemData = business.SystemData.getInstance();
                for (model.User user : systemData.getUserDirectory().getAllUsers()) {
                        if (user.getPerson() != null &&
                                        user.getPerson().getPersonId().equals(patientId)) {
                                return user.getPerson().getFullName();
                        }
                }
                return "Unknown (" + (patientId != null ? patientId : "N/A") + ")";
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
                welcomeLabel = new javax.swing.JLabel();
                roleLabel = new javax.swing.JLabel();
                enterpriseLabel = new javax.swing.JLabel();
                logoutButton = new javax.swing.JButton();
                jTabbedPane1 = new javax.swing.JTabbedPane();
                jPanel3 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                searchField = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                policyTypeCombo = new javax.swing.JComboBox<>();
                jLabel4 = new javax.swing.JLabel();
                statusCombo = new javax.swing.JComboBox<>();
                searchButton = new javax.swing.JButton();
                resetButton = new javax.swing.JButton();
                exportToScvButton = new javax.swing.JButton();
                jScrollPane2 = new javax.swing.JScrollPane();
                policiesTable = new javax.swing.JTable();
                viewDetailsButton = new javax.swing.JButton();
                generateDocButton = new javax.swing.JButton();
                sendRenewalButton = new javax.swing.JButton();
                modifyCoverageButton = new javax.swing.JButton();
                cancelPolicyButton = new javax.swing.JButton();
                statsCardsPanel = new javax.swing.JPanel();
                totalClaimsCard = new javax.swing.JPanel();
                jLabel5 = new javax.swing.JLabel();
                pendingClaimsCard = new javax.swing.JPanel();
                jLabel6 = new javax.swing.JLabel();
                processedTodayCard = new javax.swing.JPanel();
                jLabel7 = new javax.swing.JLabel();
                activePoliciesCard = new javax.swing.JPanel();
                jLabel8 = new javax.swing.JLabel();
                totalPayoutsCard = new javax.swing.JPanel();
                jLabel9 = new javax.swing.JLabel();
                approvalRateCard = new javax.swing.JPanel();
                jLabel10 = new javax.swing.JLabel();
                jLabel11 = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                recentActivityTable = new javax.swing.JTable();
                viewPolicyApplicationButton = new javax.swing.JButton();
                generateReportButton = new javax.swing.JButton();
                managePoliciesButton = new javax.swing.JButton();
                jPanel2 = new javax.swing.JPanel();
                jLabel12 = new javax.swing.JLabel();
                addProductButton = new javax.swing.JButton();
                editProductButton = new javax.swing.JButton();
                deactivateButton = new javax.swing.JButton();
                jPanel1 = new javax.swing.JPanel();
                jLabel13 = new javax.swing.JLabel();
                reportTypeCombo = new javax.swing.JComboBox<>();
                jLabel14 = new javax.swing.JLabel();
                timePeriodCombo = new javax.swing.JComboBox<>();
                exportPDFButton = new javax.swing.JButton();
                exportExcelButton = new javax.swing.JButton();
                jLabel15 = new javax.swing.JLabel();
                jLabel16 = new javax.swing.JLabel();
                jLabel17 = new javax.swing.JLabel();
                jLabel18 = new javax.swing.JLabel();
                jLabel19 = new javax.swing.JLabel();
                jLabel20 = new javax.swing.JLabel();
                jScrollPane3 = new javax.swing.JScrollPane();
                detailedStatsTable = new javax.swing.JTable();

                setPreferredSize(new java.awt.Dimension(1150, 731));

                jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel1.setText("Insurance Provider Dashboard");

                welcomeLabel.setText("Welcome: ");

                roleLabel.setText("Role: ");

                enterpriseLabel.setText("Enterprise: ");

                logoutButton.setText("Logout");
                logoutButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                logoutButtonActionPerformed(evt);
                        }
                });

                jLabel2.setText("Search Customer:");

                jLabel3.setText("Policy Type:");

                policyTypeCombo.setModel(
                                new javax.swing.DefaultComboBoxModel<>(
                                                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

                jLabel4.setText("Status:");

                statusCombo.setModel(
                                new javax.swing.DefaultComboBoxModel<>(
                                                new String[] { "All", "Active", "Expiring Soon", "Expired" }));

                searchButton.setText("Search");
                searchButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                searchButtonActionPerformed(evt);
                        }
                });

                resetButton.setText("Reset");

                exportToScvButton.setText("Export to CSV");

                policiesTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null, null, null }
                                },
                                new String[] {
                                                "Policy Number", "Customer Name", "Policy Type", "Coverage Amount",
                                                "Monthly Premium",
                                                "Start Date", "Expiry Date", "Days Until Renewal", "Status "
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jScrollPane2.setViewportView(policiesTable);

                viewDetailsButton.setText("View Full Details");

                generateDocButton.setText("Generate Policy Doc");

                sendRenewalButton.setText("Send Renewal Notice");

                modifyCoverageButton.setText("Modify Coverage");

                cancelPolicyButton.setText("Cancel Policy");
                cancelPolicyButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                cancelPolicyButtonActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel3Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(viewDetailsButton)
                                                                                                .addGap(32, 32, 32)
                                                                                                .addComponent(generateDocButton)
                                                                                                .addGap(37, 37, 37)
                                                                                                .addComponent(sendRenewalButton)
                                                                                                .addGap(37, 37, 37)
                                                                                                .addComponent(modifyCoverageButton)
                                                                                                .addGap(39, 39, 39)
                                                                                                .addComponent(cancelPolicyButton))
                                                                                .addComponent(jScrollPane2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                779,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel3Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(searchButton)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(resetButton)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(exportToScvButton))
                                                                                .addGroup(jPanel3Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel3Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jLabel2)
                                                                                                                .addComponent(jLabel3)
                                                                                                                .addComponent(jLabel4))
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(jPanel3Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                false)
                                                                                                                .addComponent(searchField)
                                                                                                                .addComponent(policyTypeCombo,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(statusCombo,
                                                                                                                                0,
                                                                                                                                168,
                                                                                                                                Short.MAX_VALUE))))
                                                                .addContainerGap(31, Short.MAX_VALUE)));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(35, 35, 35)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel2)
                                                                                .addComponent(searchField,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel3)
                                                                                .addComponent(policyTypeCombo,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(statusCombo,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(searchButton)
                                                                                .addComponent(resetButton)
                                                                                .addComponent(exportToScvButton))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                179,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(viewDetailsButton)
                                                                                .addComponent(generateDocButton)
                                                                                .addComponent(sendRenewalButton)
                                                                                .addComponent(modifyCoverageButton)
                                                                                .addComponent(cancelPolicyButton))
                                                                .addContainerGap(63, Short.MAX_VALUE)));

                jTabbedPane1.addTab("Active Policies", jPanel3);

                jLabel5.setText("Total Claims");

                javax.swing.GroupLayout totalClaimsCardLayout = new javax.swing.GroupLayout(totalClaimsCard);
                totalClaimsCard.setLayout(totalClaimsCardLayout);
                totalClaimsCardLayout.setHorizontalGroup(
                                totalClaimsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(totalClaimsCardLayout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(jLabel5)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                totalClaimsCardLayout.setVerticalGroup(
                                totalClaimsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(totalClaimsCardLayout.createSequentialGroup()
                                                                .addGap(32, 32, 32)
                                                                .addComponent(jLabel5)
                                                                .addContainerGap(31, Short.MAX_VALUE)));

                jLabel6.setText("Pending Review");

                javax.swing.GroupLayout pendingClaimsCardLayout = new javax.swing.GroupLayout(pendingClaimsCard);
                pendingClaimsCard.setLayout(pendingClaimsCardLayout);
                pendingClaimsCardLayout.setHorizontalGroup(
                                pendingClaimsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pendingClaimsCardLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel6)
                                                                .addContainerGap(15, Short.MAX_VALUE)));
                pendingClaimsCardLayout.setVerticalGroup(
                                pendingClaimsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pendingClaimsCardLayout.createSequentialGroup()
                                                                .addGap(30, 30, 30)
                                                                .addComponent(jLabel6)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jLabel7.setText("Processed Today");

                javax.swing.GroupLayout processedTodayCardLayout = new javax.swing.GroupLayout(processedTodayCard);
                processedTodayCard.setLayout(processedTodayCardLayout);
                processedTodayCardLayout.setHorizontalGroup(
                                processedTodayCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(processedTodayCardLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel7)
                                                                .addContainerGap(17, Short.MAX_VALUE)));
                processedTodayCardLayout.setVerticalGroup(
                                processedTodayCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(processedTodayCardLayout.createSequentialGroup()
                                                                .addGap(31, 31, 31)
                                                                .addComponent(jLabel7)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jLabel8.setText("Active Polics");

                javax.swing.GroupLayout activePoliciesCardLayout = new javax.swing.GroupLayout(activePoliciesCard);
                activePoliciesCard.setLayout(activePoliciesCardLayout);
                activePoliciesCardLayout.setHorizontalGroup(
                                activePoliciesCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(activePoliciesCardLayout.createSequentialGroup()
                                                                .addGap(28, 28, 28)
                                                                .addComponent(jLabel8)
                                                                .addContainerGap(19, Short.MAX_VALUE)));
                activePoliciesCardLayout.setVerticalGroup(
                                activePoliciesCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(activePoliciesCardLayout.createSequentialGroup()
                                                                .addGap(37, 37, 37)
                                                                .addComponent(jLabel8)
                                                                .addContainerGap(30, Short.MAX_VALUE)));

                jLabel9.setText("Total Payouts");

                javax.swing.GroupLayout totalPayoutsCardLayout = new javax.swing.GroupLayout(totalPayoutsCard);
                totalPayoutsCard.setLayout(totalPayoutsCardLayout);
                totalPayoutsCardLayout.setHorizontalGroup(
                                totalPayoutsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(totalPayoutsCardLayout.createSequentialGroup()
                                                                .addGap(16, 16, 16)
                                                                .addComponent(jLabel9)
                                                                .addContainerGap(18, Short.MAX_VALUE)));
                totalPayoutsCardLayout.setVerticalGroup(
                                totalPayoutsCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(totalPayoutsCardLayout.createSequentialGroup()
                                                                .addGap(33, 33, 33)
                                                                .addComponent(jLabel9)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jLabel10.setText("Approval Rate");

                javax.swing.GroupLayout approvalRateCardLayout = new javax.swing.GroupLayout(approvalRateCard);
                approvalRateCard.setLayout(approvalRateCardLayout);
                approvalRateCardLayout.setHorizontalGroup(
                                approvalRateCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                approvalRateCardLayout.createSequentialGroup()
                                                                                .addContainerGap(
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jLabel10)
                                                                                .addGap(15, 15, 15)));
                approvalRateCardLayout.setVerticalGroup(
                                approvalRateCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(approvalRateCardLayout.createSequentialGroup()
                                                                .addGap(28, 28, 28)
                                                                .addComponent(jLabel10)
                                                                .addContainerGap(31, Short.MAX_VALUE)));

                jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel11.setText("Recent Claims Activity");

                recentActivityTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null }
                                },
                                new String[] {
                                                "Claim#", "Patient", "Date", "Amount", "Status"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jScrollPane1.setViewportView(recentActivityTable);

                viewPolicyApplicationButton.setText("View Policy Applications");

                generateReportButton.setText("Generate Report");

                managePoliciesButton.setText("Manage Policies");

                javax.swing.GroupLayout statsCardsPanelLayout = new javax.swing.GroupLayout(statsCardsPanel);
                statsCardsPanel.setLayout(statsCardsPanelLayout);
                statsCardsPanelLayout.setHorizontalGroup(
                                statsCardsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(statsCardsPanelLayout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addGroup(statsCardsPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel11)
                                                                                .addGroup(statsCardsPanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(statsCardsPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                                false)
                                                                                                                .addComponent(activePoliciesCard,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(totalClaimsCard,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE))
                                                                                                .addGap(50, 50, 50)
                                                                                                .addGroup(statsCardsPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addComponent(pendingClaimsCard,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(totalPayoutsCard,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addGap(37, 37, 37)
                                                                                                .addGroup(statsCardsPanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                false)
                                                                                                                .addComponent(processedTodayCard,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(approvalRateCard,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)))
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                786,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(statsCardsPanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(13, 13, 13)
                                                                                                .addComponent(viewPolicyApplicationButton)
                                                                                                .addGap(58, 58, 58)
                                                                                                .addComponent(generateReportButton)
                                                                                                .addGap(69, 69, 69)
                                                                                                .addComponent(managePoliciesButton)))
                                                                .addContainerGap(22, Short.MAX_VALUE)));
                statsCardsPanelLayout.setVerticalGroup(
                                statsCardsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(statsCardsPanelLayout.createSequentialGroup()
                                                                .addGap(26, 26, 26)
                                                                .addGroup(statsCardsPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(pendingClaimsCard,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(totalClaimsCard,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(processedTodayCard,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(statsCardsPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(approvalRateCard,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(totalPayoutsCard,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(activePoliciesCard,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel11)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                200,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(statsCardsPanelLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(viewPolicyApplicationButton)
                                                                                .addComponent(generateReportButton)
                                                                                .addComponent(managePoliciesButton))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jTabbedPane1.addTab("DASHBOARD OVERVIEW", statsCardsPanel);

                jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel12.setText("Available Insurance Products");

                addProductButton.setText("Add New Product");

                editProductButton.setText("Edit Product");

                deactivateButton.setText("Deactivate Product");
                deactivateButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                deactivateButtonActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(addProductButton)
                                                                                                .addGap(63, 63, 63)
                                                                                                .addComponent(editProductButton)
                                                                                                .addGap(66, 66, 66)
                                                                                                .addComponent(deactivateButton))
                                                                                .addComponent(jLabel12))
                                                                .addContainerGap(340, Short.MAX_VALUE)));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(24, 24, 24)
                                                                .addComponent(jLabel12)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(addProductButton)
                                                                                .addComponent(editProductButton)
                                                                                .addComponent(deactivateButton))
                                                                .addContainerGap(371, Short.MAX_VALUE)));

                jTabbedPane1.addTab("Products Catalog", jPanel2);

                jLabel13.setText("Report Type:");

                reportTypeCombo.setModel(
                                new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales Performance Report ",
                                                "Policy Distribution Report", "Revenue Analysis ",
                                                "Customer Demographics", "Renewal Rate Analysis" }));

                jLabel14.setText("Time Period:");

                timePeriodCombo.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "This Month", "Last Month", "Quarter", "Yearly" }));

                exportPDFButton.setText("Export PDF");
                exportPDFButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                exportPDFButtonActionPerformed(evt);
                        }
                });

                exportExcelButton.setText("Export Excel");
                exportExcelButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                exportExcelButtonActionPerformed(evt);
                        }
                });

                jLabel15.setText("Performance Metrics Panel:");

                jLabel16.setText("Policies Sold this Period:");

                jLabel17.setText("Reveneus Generated This Period:");

                jLabel18.setText("Average Premium This Period: ");

                jLabel19.setText("Customer Growth Rate:");

                jLabel20.setText("DETAILED STATISTICS TABLE ");

                detailedStatsTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null }
                                },
                                new String[] {
                                                "Product Type", "Units Sold", "Revenue", "Avg Premium", "Market Share"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jScrollPane3.setViewportView(detailedStatsTable);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
                                jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup().addGap(24, 24, 24)
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jScrollPane3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                791,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel20).addComponent(jLabel15)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(exportPDFButton)
                                                                                .addGap(30, 30, 30)
                                                                                .addComponent(exportExcelButton))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(jLabel14)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(timePeriodCombo,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(jLabel13)
                                                                                .addPreferredGap(
                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(reportTypeCombo,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addComponent(jLabel16,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                160,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(jLabel17))
                                                                                .addGap(118, 118, 118)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addComponent(jLabel19)
                                                                                                .addComponent(jLabel18))))
                                                .addContainerGap(22, Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup().addGap(28, 28, 28)
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel13).addComponent(reportTypeCombo,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel14).addComponent(timePeriodCombo,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(exportPDFButton)
                                                                .addComponent(exportExcelButton))
                                                .addGap(18, 18, 18).addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel16).addComponent(jLabel18))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel17).addComponent(jLabel19))
                                                .addGap(18, 18, 18).addComponent(jLabel20)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 170,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(30, Short.MAX_VALUE)));

                jTabbedPane1.addTab("Sales Analysis", jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
                                this);
                this.setLayout(layout);
                layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addGroup(layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup().addComponent(welcomeLabel)
                                                                .addGap(92, 92, 92).addComponent(roleLabel)
                                                                .addGap(83, 83, 83).addComponent(enterpriseLabel)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addGroup(layout.createSequentialGroup().addComponent(jLabel1)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(logoutButton).addGap(140, 140, 140))
                                                .addGroup(layout.createSequentialGroup().addComponent(jTabbedPane1,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 837,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 292, Short.MAX_VALUE)))));
                layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup().addGroup(layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup().addGap(20, 20, 20)
                                                                .addComponent(jLabel1))
                                                .addGroup(layout.createSequentialGroup().addGap(44, 44, 44)
                                                                .addComponent(logoutButton)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(welcomeLabel).addComponent(roleLabel)
                                                                .addComponent(enterpriseLabel))
                                                .addGap(28, 28, 28)
                                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 492,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(469, Short.MAX_VALUE)));
        }// </editor-fold>//GEN-END:initComponents

        private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_logoutButtonActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_logoutButtonActionPerformed

        private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_searchButtonActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_searchButtonActionPerformed

        private void cancelPolicyButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelPolicyButtonActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cancelPolicyButtonActionPerformed

        private void deactivateButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deactivateButtonActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_deactivateButtonActionPerformed

        private void exportPDFButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportPDFButtonActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_exportPDFButtonActionPerformed

        private void exportExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportExcelButtonActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_exportExcelButtonActionPerformed

        // --- Helper Methods ---

        private void viewPolicyDetails(model.Policy policy) {
                StringBuilder info = new StringBuilder();
                info.append("Policy Number: ").append(policy.getPolicyNumber()).append("\n");
                info.append("Type: ").append(policy.getPolicyType().getDisplayName()).append("\n");
                info.append("Applicant: ").append(getPatientName(policy.getPatientId())).append("\n");
                info.append("Status: ").append(policy.getPolicyStatus().getDisplayName()).append("\n");
                info.append("Coverage: $").append(String.format("%,.2f", policy.getCoverageAmount())).append("\n");
                info.append("Premium: $").append(String.format("%.2f", policy.getMonthlyPremium())).append("\n");
                info.append("Start Date: ").append(policy.getStartDate()).append("\n");
                info.append("Expiry Date: ").append(policy.getExpiryDate()).append("\n");
                if (policy.getBeneficiaries() != null && !policy.getBeneficiaries().isEmpty()) {
                        info.append("Beneficiaries: ").append(String.join(", ", policy.getBeneficiaries()))
                                        .append("\n");
                }
                javax.swing.JOptionPane.showMessageDialog(this, info.toString(), "Policy Details",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

        private void managePolicy(model.Policy policy) {
                javax.swing.JTextField coverageField = new javax.swing.JTextField(
                                String.valueOf(policy.getCoverageAmount()));
                javax.swing.JTextField premiumField = new javax.swing.JTextField(
                                String.valueOf(policy.getMonthlyPremium()));
                javax.swing.JComboBox<model.Policy.PolicyStatus> statusCombo = new javax.swing.JComboBox<>(
                                model.Policy.PolicyStatus.values());
                statusCombo.setSelectedItem(policy.getPolicyStatus());

                Object[] message = {
                                "Policy: " + policy.getPolicyNumber(),
                                "Coverage Amount ($):", coverageField,
                                "Monthly Premium ($):", premiumField,
                                "Status:", statusCombo
                };

                int option = javax.swing.JOptionPane.showConfirmDialog(this, message, "Manage Policy",
                                javax.swing.JOptionPane.OK_CANCEL_OPTION);
                if (option == javax.swing.JOptionPane.OK_OPTION) {
                        try {
                                double newCoverage = Double.parseDouble(coverageField.getText());
                                double newPremium = Double.parseDouble(premiumField.getText());
                                model.Policy.PolicyStatus newStatus = (model.Policy.PolicyStatus) statusCombo
                                                .getSelectedItem();

                                policy.setCoverageAmount(newCoverage);
                                policy.setMonthlyPremium(newPremium);
                                policy.setPolicyStatus(newStatus);

                                loadActivePolicies();
                                loadDashboardStats();
                                javax.swing.JOptionPane.showMessageDialog(this, "Policy updated successfully!");

                        } catch (NumberFormatException e) {
                                javax.swing.JOptionPane.showMessageDialog(this, "Invalid number format.", "Error",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                }
        }

        // --- Action Handlers ---

        private void viewDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {
                int selectedRow = policiesTable.getSelectedRow();
                if (selectedRow < 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Please select a policy to view.", "Warning",
                                        javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
                String policyNumber = (String) policiesTable.getValueAt(selectedRow, 0);
                model.Policy policy = business.SystemData.getInstance().getPolicyDirectory()
                                .findPolicyByNumber(policyNumber);
                if (policy != null)
                        viewPolicyDetails(policy);
        }

        private void modifyCoverageButtonActionPerformed(java.awt.event.ActionEvent evt) {
                int selectedRow = policiesTable.getSelectedRow();
                if (selectedRow < 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Please select a policy to manage.", "Warning",
                                        javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
                String policyNumber = (String) policiesTable.getValueAt(selectedRow, 0);
                model.Policy policy = business.SystemData.getInstance().getPolicyDirectory()
                                .findPolicyByNumber(policyNumber);
                if (policy != null)
                        managePolicy(policy);
        }

        private void viewPolicyApplicationButtonActionPerformed(java.awt.event.ActionEvent evt) {
                int selectedRow = recentActivityTable.getSelectedRow();
                if (selectedRow >= 0) {
                        String claimNumber = (String) recentActivityTable.getValueAt(selectedRow, 0);
                        business.SystemData systemData = business.SystemData.getInstance();
                        model.Claim claim = systemData.getClaimDirectory().findClaimByNumber(claimNumber);

                        if (claim != null) {
                                String policyNumber = claim.getPolicyNumber();
                                model.Policy policy = systemData.getPolicyDirectory().findPolicyByNumber(policyNumber);
                                if (policy != null) {
                                        viewPolicyDetails(policy);
                                        return;
                                }
                        }
                }

                javax.swing.JOptionPane.showMessageDialog(this,
                                "Please select a row from the Recent Activity table to view the associated policy.",
                                "Selection Required", javax.swing.JOptionPane.WARNING_MESSAGE);
        }

        private void managePoliciesButtonActionPerformed(java.awt.event.ActionEvent evt) {
                int selectedRow = recentActivityTable.getSelectedRow();
                if (selectedRow >= 0) {
                        String claimNumber = (String) recentActivityTable.getValueAt(selectedRow, 0);
                        business.SystemData systemData = business.SystemData.getInstance();
                        model.Claim claim = systemData.getClaimDirectory().findClaimByNumber(claimNumber);

                        if (claim != null) {
                                String policyNumber = claim.getPolicyNumber();
                                model.Policy policy = systemData.getPolicyDirectory().findPolicyByNumber(policyNumber);
                                if (policy != null) {
                                        managePolicy(policy);
                                        return;
                                }
                        }
                }

                javax.swing.JOptionPane.showMessageDialog(this,
                                "Please select a row from the Recent Activity table to manage the associated policy.",
                                "Selection Required", javax.swing.JOptionPane.WARNING_MESSAGE);
        }

        private void exportToScvButtonActionPerformed(java.awt.event.ActionEvent evt) {
                javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
                fileChooser.setDialogTitle("Save CSV");
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
                        java.io.File fileToSave = fileChooser.getSelectedFile();
                        if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                                fileToSave = new java.io.File(fileToSave.getParent(), fileToSave.getName() + ".csv");
                        }

                        try (java.io.FileWriter fw = new java.io.FileWriter(fileToSave)) {
                                javax.swing.table.TableModel model = policiesTable.getModel();
                                for (int i = 0; i < model.getColumnCount(); i++) {
                                        fw.write(model.getColumnName(i) + (i < model.getColumnCount() - 1 ? "," : ""));
                                }
                                fw.write("\n");
                                for (int i = 0; i < model.getRowCount(); i++) {
                                        for (int j = 0; j < model.getColumnCount(); j++) {
                                                Object value = model.getValueAt(i, j);
                                                fw.write(value != null ? value.toString() : "");
                                                fw.write(j < model.getColumnCount() - 1 ? "," : "");
                                        }
                                        fw.write("\n");
                                }
                                javax.swing.JOptionPane.showMessageDialog(this, "Data exported successfully!",
                                                "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        } catch (java.io.IOException ex) {
                                javax.swing.JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(),
                                                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                }
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel activePoliciesCard;
        private javax.swing.JButton addProductButton;
        private javax.swing.JPanel approvalRateCard;
        private javax.swing.JButton cancelPolicyButton;
        private javax.swing.JButton deactivateButton;
        private javax.swing.JTable detailedStatsTable;
        private javax.swing.JButton editProductButton;
        private javax.swing.JLabel enterpriseLabel;
        private javax.swing.JButton exportExcelButton;
        private javax.swing.JButton exportPDFButton;
        private javax.swing.JButton exportToScvButton;
        private javax.swing.JButton generateDocButton;
        private javax.swing.JButton generateReportButton;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel13;
        private javax.swing.JLabel jLabel14;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel16;
        private javax.swing.JLabel jLabel17;
        private javax.swing.JLabel jLabel18;
        private javax.swing.JLabel jLabel19;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel20;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JButton logoutButton;
        private javax.swing.JButton managePoliciesButton;
        private javax.swing.JButton modifyCoverageButton;
        private javax.swing.JPanel pendingClaimsCard;
        private javax.swing.JTable policiesTable;
        private javax.swing.JComboBox<String> policyTypeCombo;
        private javax.swing.JPanel processedTodayCard;
        private javax.swing.JTable recentActivityTable;
        private javax.swing.JComboBox<String> reportTypeCombo;
        private javax.swing.JButton resetButton;
        private javax.swing.JLabel roleLabel;
        private javax.swing.JButton searchButton;
        private javax.swing.JTextField searchField;
        private javax.swing.JButton sendRenewalButton;
        private javax.swing.JPanel statsCardsPanel;
        private javax.swing.JComboBox<String> statusCombo;
        private javax.swing.JComboBox<String> timePeriodCombo;
        private javax.swing.JPanel totalClaimsCard;
        private javax.swing.JPanel totalPayoutsCard;
        private javax.swing.JButton viewDetailsButton;
        private javax.swing.JButton viewPolicyApplicationButton;
        private javax.swing.JLabel welcomeLabel;
        // End of variables declaration//GEN-END:variables
}
