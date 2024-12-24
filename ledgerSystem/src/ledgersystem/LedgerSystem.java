package ledgersystem;

import java.util.Scanner;
import java.time.LocalDateTime;

 class User{
       
        
        private String name;
        private String email;
        private String password;
        private boolean debt = false;                                                                      // change this to private and return boolean value
        private double balance = 0.0;
        private LocalDateTime duedate ;
        
        public User() {
        this.name = "";
        this.email = "";
        this.password = "";
    }
    
        public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
         }
         
         
        double loan = 0.0;
  
        
        public void applyloan(double principal, double rate, int period){
            loan+= principal +(principal*rate*period / 100);
            duedate = LocalDateTime.now().plusMonths(period);
            System.out.println("please pay the loan within the time given:"+duedate);
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
         
         public void debit(double amount){
             if (amount >0){
                    balance +=amount;
                    System.out.println("debit successfully");
                }else{
                 System.out.println("Enter positive value only:");
             }
         }
         
         public void credit(double amount){
             if ((amount >0)&&(balance>amount)){
                    balance -=amount;
                    System.out.println("credit successfully");
                }else{
                 System.out.println("Enter positive value or not enough balance:");
             }
         }
         
         
         public void updatedebtstatus(){
             if (duedate != null && LocalDateTime.now().isAfter(duedate) && loan != 0) {
             debt = true;
         }
    }
         public boolean hasdebt(){
             return debt;
         }
 }    
       

public class LedgerSystem {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final User user = new User();
    
    /*private static User[] users = new User[50];                             different user
    private static int userCount = 0;
    
    private static void addUser(User user) {
        users[userCount++] = user;
    }*/                                                    
    
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
        
        System.out.print("Enter Interest Rate per month:");
        double rate = scanner.nextDouble();
        
        System.out.print("Repayment Period(month):");
        int period = scanner.nextInt();
        
        double repayment_amount = principal +(principal*rate*period / 100);
        System.out.println("Total Repayment amount = "+repayment_amount);
                                                                                          // start here = Schedule periodic or monthly installment 
        user.applyloan(principal, rate, period);
        
        System.out.println("loan="+user.getloan());
    }
    
    private static void repayloan(){
        System.out.println("Enter amount");
        double amount = scanner.nextDouble();
        
        user.repayloan(amount);
        System.out.println("loan ="+user.getloan());
        
        
    }
    
   
    

    public static void main(String[] args) {
        
        boolean run = true;
        
        while(run){
            System.out.println("1.debit:");
            System.out.println("2.credit:");
            System.out.println("3.manageloan:");
            System.out.println("4.Exit:");
            System.out.print("Enter choice:");
            int choice = scanner.nextInt();
            
           user.updatedebtstatus();
            
            switch (choice){
                case 1:
                    if(!(user.hasdebt())){
                    System.out.println("Enter amount");
                    double amount = scanner.nextDouble();
                    user.debit(amount);
                
                    }else{
                        System.out.println("You have Unpaid loan.");
                    }
                    break;
                
                case 2:
                    if(!(user.hasdebt())){
                   System.out.println("Enter amount");
                   double amount = scanner.nextDouble();
                   user.credit(amount);

                   }else{
                       System.out.println("You have Unpaid loan.");
                   }
                    break;
                
                case 3:
                    creditloan();
                    break;
                case 4:
                    run=false;
                    break;
                default :
                    System.out.println("Invalid input");
            }
        }
    }
    
    
    
}
 
