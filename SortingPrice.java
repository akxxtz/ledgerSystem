import java.util.Scanner;
class User {
    String name, email, password;
    
    User(String name, String email, String password) {
     this.name = name;
     this.email = email;
     this.password = password;
     
    }
}

public class SortingPrice {
    private static User[] users = new User[50];
    private static int userCount = 0;
    
public static boolean ValidName(String name) {

    if(name == null || name.isEmpty()) {
        return false;
    }
    for(char LetterinName : name.toCharArray()) {
        if(!(Character.isLetter(LetterinName) || LetterinName == ' ')) {
            return false;
        }
    }
    if(name.trim().length() < 2) {
        return false;
    }
    
    return true;
}

public static boolean ValidEmail(String email) {
    
    if(email == null || email.isEmpty()) {
        return false;
    }
    char[] LetterinEmail = email.toCharArray();
    
    boolean hasAt = false;
    boolean hasDot = false;
    
    for(int i = 0; i < LetterinEmail.length; i++) {
        char letter = LetterinEmail[i];
        
        if(letter == '@') {
            if(hasAt) {
                return false;
            }
            hasAt = true;
            
            if(i == 0 || i == LetterinEmail.length - 1) {
                return false;
            }
        }
            
        if(letter == '.' && hasAt) {
            if(i == 0 || i == LetterinEmail.length - 1 || LetterinEmail[i - 1] == '@') {
                return false;
            }
        hasDot = true;
        }
    }
    
    return hasAt && hasDot;     
}

public static boolean ValidPassword(String password) {
    return password != null && password.length() >= 6;
}

private static void addUser(User user) {
    if(userCount == users.length) {
        User[] newUsers = new User[users.length * 2];
        System.arraycopy(users, 0, userCount, 0, users.length);
        users = newUsers;
    }
    users[userCount++] = user;
}

private static User findUserByEmail(String email) {
    for(int i = 0; i < userCount; i++) {
        if(users[i].email.equals(email)) {
            return users[i];
        }
    }
    return null;
}

//public static boolean BalanceValue(Integer balance) {
//    
//    
//    
//}
        
public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    
    //Main method
     System.out.println("== Ledger System (name can created yourself) ==");
     System.out.println("Login or Register:");
     System.out.println("1.Register");
     System.out.println("2.Login");
     
     System.out.print("\n>");
     Integer LoginRegister = sc.nextInt();
     sc.nextLine();
     
     if(LoginRegister == 1) {
         String name;
         String email;
         String password;
         do {
             System.out.println("\n== Please fill in the form == ");
             
             System.out.print("Name: ");
             name = sc.nextLine();
             
             System.out.print("Email: ");
             email = sc.nextLine();
             
             System.out.print("Password: ");
             password = sc.nextLine();
             
             if(!ValidName(name)) {
                 System.out.println("Invalid name!");
             }
             if(!ValidEmail(email)) {
                 System.out.println("Invalid email!");
             }
             if(!ValidPassword(password)) {
                 System.out.println("Invalid password!");
             }
             
         } while ((!ValidName(name)) || !ValidEmail(email) || !ValidPassword(password));
         
         addUser(new User(name, email, password));
         System.out.println("\nRegister Successful!!! \n");
         
         System.out.println("== Welcome, " + name + " ==");
         System.out.println("Balance: ");
         System.out.println("Savings: ");
         System.out.println("Loan: ");
         
         System.out.println("\n== Transaction ==");
         System.out.println("1.Debit");
         System.out.println("2.Credit");
         System.out.println("3.History");
         System.out.println("4.History");
         System.out.println("5.Savings");
         System.out.println("6.Credit Loan");
         System.out.println("7.Deposit Interest Predictor");
         System.out.println("8.Logout");
         
         boolean choice = true;
         while(choice) {
             System.out.print("\n>");
             int TransactionChoice = sc.nextInt();
             
             if(!(TransactionChoice <= 8)) {
                 System.out.println("Invalid choice!!!");
                 choice = true;
             }
             else {
                 choice = false;
             }
         }
             
     }
     
     else if(LoginRegister == 2) {
         System.out.println("== Please enter your email and password ==");
         
         System.out.print("Email: ");
         String email = sc.nextLine();
         
         System.out.print("Password: ");
         String password = sc.nextLine();
         
         User user = findUserByEmail(email);
         
         if(user != null && user.password.equals(password)) {
             System.out.println("Login Successful!!!");
         }
         else {
             System.out.println("Invalid email or password, Please try again");
         }
         
     }
     sc. close();
}
}
