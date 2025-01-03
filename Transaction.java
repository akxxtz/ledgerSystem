
package fop_final_ledger;

import java.time.LocalDate; 

public class Transaction {
     private double balance;
    private String Transaction_type;
    private double AmountofTransaction;
    private String description;
    private LocalDate date =LocalDate.now();
    private int User_id;
   
    
    
    public Transaction (double AmountofTransaction , String description , LocalDate date , int User_id , double balance ,String Transaction_type){                      //for writer
        this.AmountofTransaction = AmountofTransaction;
        this.description = description;
        this.date = date;
        this.User_id = User_id;
        this.balance = balance;
        this.Transaction_type = Transaction_type;
        
    }
    
          
    
    public Transaction(){
        
    }
    
    
    
    
    
    
  
    
    
    ///////getters
    
public String getTransaction_type(){
    return Transaction_type;
}

public double getAmountofTransaction(){
    return AmountofTransaction;
}

public String getdescription(){
    return description;
}

public LocalDate getdate(){
    return date;
}

public int getUser_id(){
    return User_id;
}

public double getbalance(){
    return balance;
}


}
