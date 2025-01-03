
package fop_final_ledger;


public class Saving {
    
   
    private int saving_id;
    private int user_id;
    private double percentage=0.0;
    private double amountToCredit=0.0;
    private String status;
    
    
    
    public Saving(){
        
    }
    
    /////////////////////////////////used for write////////////////////////////////////////////
//    public Saving(int savingid, int userid, double percentage){
//        this.saving_id      =savingid;
//        this.user_id        =userid;
//        this.percentage     =percentage;
//        
//        
//    }
    
     public Saving(int userid, double percentage , String status){
        
        this.user_id        =userid;
        this.percentage     =percentage;
        this.status =        status;
        
        
    }
    
   
    
    //////////////////////used for read ///////////////////////
     public Saving(int savingid, int userid, double percentage,double amountToCredit ,String status){
        this.saving_id      =savingid;
        this.user_id        =userid;
        this.percentage     =percentage;
        this.amountToCredit = amountToCredit;
        this.status         = status;
        
    }
     
     
      public void applysaving(){
        this.status = "active";
        
    }

public void set_amountToCreditToZero(){     //clear the balance once transfer                        
    amountToCredit = 0;
}

public void set_percentageZero(){
    percentage = 0;
}

public void setInactiveTransactionType(){
     status = "inactive";
}

public void set_amountToCredit(double amount){
    this.amountToCredit = amount * (percentage/100.0);
}
   
public void set_percentage(double percentage){       //when calling debit function                         
    this.percentage  =percentage;
}

public int getsaving_id(){
     return saving_id;
}

public int getuser_id(){
     return user_id;
}

public String getstatus(){
     return status;
}

public double getpercentage(){
     return percentage;
}

public double getamountToCredit(){ //to transfer to balance
    return amountToCredit;
}

public boolean checkSavingStatus() {
    return (percentage !=0);

}





}