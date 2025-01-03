import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class LedgerSystem {
    private Map<String, User> users;
    private List<Transaction> transactions;
    private List<Loan> loans;
    private User currentUser;
    private Map<String, Double> banks;
    private Scanner scanner;

    public LedgerSystem() {
        users = new HashMap<>();
        transactions = new ArrayList<>();
        loans = new ArrayList<>();
        banks = new HashMap<>();
        scanner = new Scanner(System.in);

        // Initialize bank interest rates
        banks.put("RHB", 2.6);
        banks.put("Maybank", 2.5);
        banks.put("Hong Leong", 2.3);
        banks.put("Alliance", 2.85);
        banks.put("AmBank", 2.55);
        banks.put("Standard Chartered", 2.65);

        setupFiles();
        loadInitialData();
    }

    private void setupFiles() {
        String[] files = {"users.csv", "transactions.csv", "savings.csv", "loans.csv"};
        for (String file : files) {
            try {
                File f = new File(file);
                if (!f.exists()) {
                    PrintWriter writer = new PrintWriter(new FileWriter(f));
                    switch (file) {
                        case "users.csv":
                            writer.println("user_id,name,email,password_hash");
                            break;
                        case "transactions.csv":
                            writer.println("transaction_id,user_id,type,amount,description,date");
                            break;
                        case "savings.csv":
                            writer.println("savings_id,user_id,status,percentage");
                            break;
                        case "loans.csv":
                            writer.println("loan_id,user_id,principal_amount,interest_rate," +
                                    "repayment_period,outstanding_balance,status,created_at");
                            break;
                    }
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + file);
            }
        }
    }

    private void loadInitialData() {
        try {
            // Load users
            List<String> userLines = Files.readAllLines(Paths.get("users.csv"));
            for (int i = 1; i < userLines.size(); i++) {
                String[] parts = userLines.get(i).split(",");
                User user = new User(
                        Integer.parseInt(parts[0]), // userId
                        parts[1], // name
                        parts[2], // email
                        parts[3]  // passwordHash
                );
                users.put(parts[2], user);
            }

            // Load transactions
            List<String> transactionLines = Files.readAllLines(Paths.get("transactions.csv"));
            for (int i = 1; i < transactionLines.size(); i++) {
                String[] parts = transactionLines.get(i).split(",");
                Transaction transaction = new Transaction(
                        Integer.parseInt(parts[0]), // transactionId
                        Integer.parseInt(parts[1]), // userId
                        parts[2], // type
                        new BigDecimal(parts[3]), // amount
                        parts[4], // description
                        LocalDate.parse(parts[5]) // date
                );
                transactions.add(transaction);
            }

            // Load loans
            List<String> loanLines = Files.readAllLines(Paths.get("loans.csv"));
            for (int i = 1; i < loanLines.size(); i++) {
                String[] parts = loanLines.get(i).split(",");
                Loan loan = new Loan(
                        Integer.parseInt(parts[0]), // loanId
                        Integer.parseInt(parts[1]), // userId
                        new BigDecimal(parts[2]), // principalAmount
                        Double.parseDouble(parts[3]), // interestRate
                        Integer.parseInt(parts[4]), // repaymentPeriod
                        new BigDecimal(parts[5]), // outstandingBalance
                        parts[6], // status
                        LocalDate.parse(parts[7]) // createdAt
                );
                loans.add(loan);
            }
        } catch (IOException e) {
            System.out.println("Error loading initial data: " + e.getMessage());
        }
    }

    private void loadUserData() {
        try {
            // Calculate balance from transactions
            BigDecimal balance = BigDecimal.ZERO;
            for (Transaction transaction : transactions) {
                if (transaction.getUserId() == currentUser.getUserId()) {
                    if (transaction.getType().equals("debit")) {
                        balance = balance.add(transaction.getAmount());
                    } else {
                        balance = balance.subtract(transaction.getAmount());
                    }
                }
            }
            currentUser.setBalance(balance);

            // Load savings settings
            List<String> savingsLines = Files.readAllLines(Paths.get("savings.csv"));
            for (int i = 1; i < savingsLines.size(); i++) {
                String[] parts = savingsLines.get(i).split(",");
                if (Integer.parseInt(parts[1]) == currentUser.getUserId()) {
                    currentUser.setSavingsActive(parts[2].equals("active"));
                    currentUser.setSavingsPercentage(Integer.parseInt(parts[3]));
                    break;
                }
            }

            // Load active loan
            for (Loan loan : loans) {
                if (loan.getUserId() == currentUser.getUserId() &&
                        loan.getStatus().equals("active")) {
                    currentUser.setLoan(loan.getOutstandingBalance());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean validatePassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*");
    }

    public boolean register(String name, String email, String password) {
        if (!validateEmail(email)) {
            System.out.println("Invalid email format!");
            return false;
        }

        if (!validatePassword(password)) {
            System.out.println("Password must be at least 6 characters and contain both letters and numbers!");
            return false;
        }

        if (users.containsKey(email)) {
            System.out.println("Email already registered!");
            return false;
        }

        int userId = users.size() + 1;
        String passwordHash = hashPassword(password);
        User user = new User(userId, name, email, passwordHash);
        users.put(email, user);

        // Save to CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.csv", true))) {
            writer.println(userId + "," + name + "," + email + "," + passwordHash);
        } catch (IOException e) {
            System.out.println("Error saving user data!");
            return false;
        }

        return true;
    }

    public boolean login(String email, String password) {
        User user = users.get(email);
        if (user == null) return false;

        if (user.getPasswordHash().equals(hashPassword(password))) {
            currentUser = user;
            loadUserData();
            return true;
        }
        return false;
    }

    public boolean recordTransaction(String type, String amountStr, String description) {
        // Check if user has overdue loan
        boolean hasOverdueLoan = loans.stream()
                .anyMatch(l -> l.getUserId() == currentUser.getUserId() &&
                        l.getStatus().equals("active") &&
                        l.getCreatedAt().plusMonths(l.getRepaymentPeriod())
                                .isBefore(LocalDate.now()));

        if (hasOverdueLoan) {
            System.out.println("Cannot perform transactions! You have an overdue loan!");
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be positive!");
                return false;
            }

            if (description.length() > 100) {
                System.out.println("Description too long!");
                return false;
            }

            int transactionId = transactions.size() + 1;
            LocalDate date = LocalDate.now();

            Transaction transaction = new Transaction(
                    transactionId, currentUser.getUserId(), type, amount, description, date);
            transactions.add(transaction);

            // Update balance
            if (type.equals("debit")) {
                currentUser.setBalance(currentUser.getBalance().add(amount));
                if (currentUser.isSavingsActive()) {
                    BigDecimal savingsAmount = amount.multiply(
                            new BigDecimal(currentUser.getSavingsPercentage())
                                    .divide(new BigDecimal("100")));
                    currentUser.setSavings(currentUser.getSavings().add(savingsAmount));
                    currentUser.setBalance(currentUser.getBalance().subtract(savingsAmount));
                }
            } else {
                currentUser.setBalance(currentUser.getBalance().subtract(amount));
            }

            // Save to CSV
            try (PrintWriter writer = new PrintWriter(new FileWriter("transactions.csv", true))) {
                writer.println(transactionId + "," + currentUser.getUserId() + "," +
                        type + "," + amount + "," + description + "," + date);
            } catch (IOException e) {
                System.out.println("Error saving transaction!");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
            return false;
        }
    }

    public boolean setSavings(int percentage) {
        if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid percentage!");
            return false;
        }

        currentUser.setSavingsActive(true);
        currentUser.setSavingsPercentage(percentage);

        // Save to CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter("savings.csv", true))) {
            writer.println(transactions.size() + 1 + "," + currentUser.getUserId() +
                    ",active," + percentage);
        } catch (IOException e) {
            System.out.println("Error saving savings settings!");
            return false;
        }

        return true;
    }

    public void viewHistory() {
        System.out.println("== History ==");
        System.out.println("Date\tDescription\tDebit\tCredit\tBalance");

        BigDecimal runningBalance = BigDecimal.ZERO;
        for (Transaction t : transactions.stream()
                .filter(t -> t.getUserId() == currentUser.getUserId())
                .sorted(Comparator.comparing(Transaction::getDate))
                .toList()) {
            if (t.getType().equals("debit")) {
                runningBalance = runningBalance.add(t.getAmount());
                System.out.printf("%s\t%s\t%.2f\t\t%.2f%n",
                        t.getDate(), t.getDescription(), t.getAmount(), runningBalance);
            } else {
                runningBalance = runningBalance.subtract(t.getAmount());
                System.out.printf("%s\t%s\t\t%.2f\t%.2f%n",
                        t.getDate(), t.getDescription(), t.getAmount(), runningBalance);
            }
        }

        // Export to CSV
        try (PrintWriter writer = new PrintWriter("history_" + currentUser.getUserId() + ".csv")) {
            writer.println("Date,Description,Debit,Credit,Balance");
            runningBalance = BigDecimal.ZERO;
            for (Transaction t : transactions.stream()
                    .filter(t -> t.getUserId() == currentUser.getUserId())
                    .sorted(Comparator.comparing(Transaction::getDate))
                    .toList()) {
                if (t.getType().equals("debit")) {
                    runningBalance = runningBalance.add(t.getAmount());
                    writer.printf("%s,%s,%.2f,,%.2f%n",
                            t.getDate(), t.getDescription(), t.getAmount(), runningBalance);
                } else {
                    runningBalance = runningBalance.subtract(t.getAmount());
                    writer.printf("%s,%s,,%.2f,%.2f%n",
                            t.getDate(), t.getDescription(), t.getAmount(), runningBalance);
                }
            }
            System.out.println("File Exported!");
        } catch (IOException e) {
            System.out.println("Error exporting history!");
        }
    }

    private void handleCreditLoan() {
        System.out.println("== Credit Loan ==");
        System.out.println("1. Apply for Loan");
        System.out.println("2. Repay Loan");
        System.out.print(">");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                applyForLoan();
                break;
            case "2":
                repayLoan();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private void applyForLoan() {
        // Check if user has any active loans
        boolean hasActiveLoan = loans.stream()
                .anyMatch(l -> l.getUserId() == currentUser.getUserId() &&
                        l.getStatus().equals("active"));

        if (hasActiveLoan) {
            System.out.println("You already have an active loan!");
            return;
        }

        System.out.println("== Apply for Loan ==");
        System.out.print("Enter principal amount: ");
        try {
            BigDecimal principalAmount = new BigDecimal(scanner.nextLine());
            if (principalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be positive!");
                return;
            }

            System.out.print("Enter interest rate (%): ");
            double interestRate = Double.parseDouble(scanner.nextLine());
            if (interestRate <= 0) {
                System.out.println("Interest rate must be positive!");
                return;
            }

            System.out.print("Enter repayment period (months): ");
            int repaymentPeriod = Integer.parseInt(scanner.nextLine());
            if (repaymentPeriod <= 0) {
                System.out.println("Repayment period must be positive!");
                return;
            }

            // Calculate total repayment amount
            BigDecimal totalInterest = principalAmount
                    .multiply(BigDecimal.valueOf(interestRate))
                    .multiply(BigDecimal.valueOf(repaymentPeriod))
                    .divide(BigDecimal.valueOf(1200));
            BigDecimal totalRepayment = principalAmount.add(totalInterest);
            BigDecimal monthlyPayment = totalRepayment
                    .divide(BigDecimal.valueOf(repaymentPeriod), 2, RoundingMode.HALF_UP);

            System.out.println("\nLoan Summary:");
            System.out.printf("Principal Amount: $%.2f\n", principalAmount);
            System.out.printf("Total Interest: $%.2f\n", totalInterest);
            System.out.printf("Total Repayment: $%.2f\n", totalRepayment);
            System.out.printf("Monthly Payment: $%.2f\n", monthlyPayment);

            System.out.print("\nConfirm loan application? (Y/N): ");
            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                int loanId = loans.size() + 1;
                Loan loan = new Loan(
                        loanId,
                        currentUser.getUserId(),
                        principalAmount,
                        interestRate,
                        repaymentPeriod,
                        totalRepayment,
                        "active",
                        LocalDate.now()
                );
                loans.add(loan);
                currentUser.setLoan(totalRepayment);

                // Save to CSV
                try (PrintWriter writer = new PrintWriter(new FileWriter("loans.csv", true))) {
                    writer.printf("%d,%d,%.2f,%.2f,%d,%.2f,%s,%s\n",
                            loanId, currentUser.getUserId(), principalAmount, interestRate,
                            repaymentPeriod, totalRepayment, "active", LocalDate.now());
                    System.out.println("Loan application successful!");
                } catch (IOException e) {
                    System.out.println("Error saving loan data!");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void repayLoan() {
        Optional<Loan> activeLoan = loans.stream()
                .filter(l -> l.getUserId() == currentUser.getUserId() &&
                        l.getStatus().equals("active"))
                .findFirst();

        if (activeLoan.isEmpty()) {
            System.out.println("You don't have any active loans!");
            return;
        }

        Loan loan = activeLoan.get();
        System.out.println("== Repay Loan ==");
        System.out.printf("Outstanding balance: $%.2f\n", loan.getOutstandingBalance());
        System.out.print("Enter repayment amount: ");

        try {
            BigDecimal repaymentAmount = new BigDecimal(scanner.nextLine());
            if (repaymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be positive!");
                return;
            }

            if (repaymentAmount.compareTo(loan.getOutstandingBalance()) > 0) {
                System.out.println("Amount exceeds outstanding balance!");
                return;
            }

            // Process repayment
            BigDecimal newBalance = loan.getOutstandingBalance().subtract(repaymentAmount);
            loan.setOutstandingBalance(newBalance);
            currentUser.setLoan(newBalance);

            if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
                loan.setStatus("repaid");
                System.out.println("Loan fully repaid!");
            } else {
                System.out.printf("Remaining balance: $%.2f\n", newBalance);
            }

            // Update CSV
            try {
                List<String> lines = Files.readAllLines(Paths.get("loans.csv"));
                try (PrintWriter writer = new PrintWriter("loans.csv")) {
                    writer.println(lines.get(0)); // Header
                    for (Loan l : loans) {
                        writer.printf("%d,%d,%.2f,%.2f,%d,%.2f,%s,%s\n",
                                l.getLoanId(), l.getUserId(), l.getPrincipalAmount(),
                                l.getInterestRate(), l.getRepaymentPeriod(),
                                l.getOutstandingBalance(), l.getStatus(), l.getCreatedAt());
                    }
                }
                System.out.println("Repayment recorded successfully!");
            } catch (IOException e) {
                System.out.println("Error updating loan data!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
        }
    }

    private void handleDepositInterest() {
        System.out.println("== Deposit Interest Predictor ==");
        System.out.println("Available Banks:");
        for (Map.Entry<String, Double> bank : banks.entrySet()) {
            System.out.printf("%s: %.2f%%\n", bank.getKey(), bank.getValue());
        }
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();

        BigDecimal interest = calculateDepositInterest(bankName);
        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf("Predicted monthly interest: $%.2f\n", interest);
        } else {
            System.out.println("Invalid bank name!");
        }
    }

    public BigDecimal calculateDepositInterest(String bankName) {
        if (!banks.containsKey(bankName)) {
            return BigDecimal.ZERO;
        }

        double rate = banks.get(bankName);
        BigDecimal interestRate = new BigDecimal(rate).divide(new BigDecimal("100"));
        return currentUser.getBalance()
                .multiply(interestRate)
                .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    public void run() {
        while (true) {
            System.out.println("\n== Ledger System ==");
            System.out.println("Login or Register:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print(">");
            String choice = scanner.nextLine();

            if (choice.equals("2")) {
                System.out.println("== Please fill in the form ==");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (register(name, email, password)) {
                    System.out.println("Register Successful!!!");
                } else {
                    System.out.println("Registration failed!");
                    continue;
                }
            } else if (choice.equals("1")) {
                System.out.println("== Please enter your email and password ==");
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (login(email, password)) {
                    System.out.println("Login Successful!!!");
                    mainMenu();
                } else {
                    System.out.println("Login failed!");
                    continue;
                }
            }
        }
    }

    private void mainMenu() {
        while (true) {
            System.out.printf("\n== Welcome, %s ==\n", currentUser.getName());
            System.out.printf("Balance: %.2f\n", currentUser.getBalance());
            System.out.printf("Savings: %.2f\n", currentUser.getSavings());
            System.out.printf("Loan: %.2f\n", currentUser.getLoan());
            System.out.println("== Transaction ==");
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.println("3. History");
            System.out.println("4. Savings");
            System.out.println("5. Credit Loan");
            System.out.println("6. Deposit Interest Predictor");
            System.out.println("7. Logout");
            System.out.print(">");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleDebit();
                    break;
                case "2":
                    handleCredit();
                    break;
                case "3":
                    viewHistory();
                    break;
                case "4":
                    handleSavings();
                    break;
                case "5":
                    handleCreditLoan();
                    break;
                case "6":
                    handleDepositInterest();
                    break;
                case "7":
                    System.out.println("Thank you for using \"Ledger System\"");
                    return;
            }
        }
    }

    private void handleDebit() {
        System.out.println("== Debit ==");
        System.out.print("Enter amount: ");
        String amount = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        if (recordTransaction("debit", amount, description)) {
            System.out.println("Debit Successfully Recorded!!!");
        }
    }

    private void handleCredit() {
        System.out.println("== Credit ==");
        System.out.print("Enter amount: ");
        String amount = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        if (recordTransaction("credit", amount, description)) {
            System.out.println("Credit Successfully Recorded!!!");
        }
    }

    private void handleSavings() {
        System.out.println("== Savings ==");
        System.out.print("Are you sure you want to activate it? (Y/N) : ");
        String activate = scanner.nextLine();

        if (activate.equalsIgnoreCase("Y")) {
            System.out.print("Please enter the percentage you wish to deduct from the next debit: ");
            try {
                int percentage = Integer.parseInt(scanner.nextLine());
                if (setSavings(percentage)) {
                    System.out.println("Savings Settings added successfully!!!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid percentage!");
            }
        }
    }
}