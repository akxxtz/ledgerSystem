package ledgersystem;

import java.util.Scanner;


 class User{
       
        
        String name, email, password;
        
         User() {
        this.name = "";
        this.email = "";
        this.password = "";
    }
    
        User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
         }
         
         
        double loan = 0.0;
  
        
        public void applyloan(double principal, double rate, int period){
            loan+= principal +(principal*rate*period / 100);
        }
        
       
        public void repayloan(double amount){
            if (loan>= amount){
                loan-=amount;
                System.out.println("Loan repaid successfully.");
            }else{
                System.out.println("amount exceeded outstanding loan");
            }
        }
        
         public double getloan(){
            return loan;
        }
    }

public class LedgerSystem {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final User newuser = new User();
    
    private static User[] users = new User[50];
    private static int userCount = 0;
    
    private static void addUser(User user) {
        users[userCount++] = user;
    }
    
    private static void creditloan(){
        System.out.println("Choose application:");
        System.out.println("1.Apply Loan");
        System.out.println("2.Repay Loan");
        System.out.print(">");
        int choice = scanner.nextInt();
        
        switch (choice){
            case 1:
                applyloan();
                break;
            case 2:
                repayloan();
                break;
            default:
                System.out.println("Invalid input");
        }
           
    }

    private static void applyloan(){
        System.out.print("Enter Principal:");
        double principal = scanner.nextDouble();
        
        System.out.print("Enter Interest Rate:");
        double rate = scanner.nextDouble();
        
        System.out.print("Repayment Period(month):");
        int period = scanner.nextInt();
        
        double repayment_amount = principal +(principal*rate*period / 100);
        System.out.println("Total Repayment amount = "+repayment_amount);
                                                                                          // start here = Schedule periodic or monthly installment 
        newuser.applyloan(principal, rate, period);
        
        System.out.println("loan="+newuser.getloan());
    }
    
    private static void repayloan(){
        System.out.println("Enter amount");
        double amount = scanner.nextDouble();
        
        newuser.repayloan(amount);
        System.out.println("loan ="+newuser.getloan());
        
        
    }
   
    

    public static void main(String[] args) {
        while(true){
       creditloan();
        }
    }
    
    
    
}
