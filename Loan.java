
package fop_final_ledger;

import java.time.LocalDateTime;



public class Loan {
    
    
   private int loan_id;
   private int user_id;
   private double principal_amount;
   private double interest_rate;
   private int repayment_period;
   private double outstandingbalance;
   private String status;
   private LocalDateTime CreatedAt;
    
    
    public   Loan(int loanid, int userid,double principal_amount, double interest_rate, int repayment_period){
            this.loan_id = loanid;
            this.user_id = userid;
            this.principal_amount = principal_amount;
            this.interest_rate = interest_rate;
            this.repayment_period = repayment_period;

     }

    public   Loan(int loanid, int userid,double principal_amount, double interest_rate, int repayment_period,
                  double balance, String status, LocalDateTime createdtime){
            this.loan_id = loanid;
            this.user_id = userid;
            this.principal_amount = principal_amount;
            this.interest_rate = interest_rate;
            this.repayment_period = repayment_period;
            this.CreatedAt = createdtime;//LocalDateTime.now();
            this.status = status;//"active";
            this.outstandingbalance = balance;  

     }
    
    public void applyLoan(){
            this.CreatedAt = LocalDateTime.now();
            this.status   = "active";
            this.outstandingbalance += principal_amount +(principal_amount*interest_rate*repayment_period / 100);
        }
    public void repayloan(double amount){
            if (outstandingbalance>= amount){
                outstandingbalance-=amount;
                System.out.println("Loan repaid successfully.");
            }else{
                System.out.println("amount exceeded outstanding loan");
            }
        }
  
    public boolean hasdebt(){
        return (CreatedAt != null && repayment_period != 0 && LocalDateTime.now().isAfter(CreatedAt.plusMonths(repayment_period) ) && outstandingbalance != 0);
     }
    
    public int getloan_id(){
        return loan_id ;
    }    
    public int getuser_id(){
        return user_id;
    }
    
    public double getoutstading_balance(){
        return outstandingbalance;
    }
    public double getprincipal_amount(){
        return principal_amount;
    }
    
    public double getinterest_rate(){
        return interest_rate;
    }
    
    public int getrepayment_period(){
        return repayment_period;
    }
    
    public String getStatus(){
        return status;
    }
        
    public LocalDateTime getCreatedAt(){
        return CreatedAt;
    }
    
    public LocalDateTime getDueDate(){
        return CreatedAt.plusMonths(repayment_period) ;
    }
    
}
