/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fop_final_ledger;

import java.time.LocalDateTime;

/**
 *
 * @author weili
 */
class User{
       
        
    
        private int user_id;
        private String name;
        private String email;
        private String password;
                                                                           
        private double balance = 0.0;
        //loan
        //private boolean debt = false;                //loan
        private LocalDateTime duedate  ;             
        private double loan = 0.0;
        

        
        
        
        public User() {
        this.user_id = 0;
        this.name = "";
        this.email = "";
        this.password = "";
    }
    
        public User(int user_id, String name, String email, String password) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
         }

         
         
       public void debit(double amount){
           if (amount <= 0){
                System.out.println("Enter positive value only:");
                return;
            }

             balance +=amount;
             
   
       }
       
       public void debitForSaving(double amount, Saving saving){
           if (amount <= 0){
                System.out.println("Enter positive value only:");
                return;
            }

             balance +=amount*(1-(saving.getpercentage()/100));
             
   
       }
       
         public void credit(double amount){
             if ((amount >0)&&(balance>amount)){
                    balance -=amount;
                    System.out.println("credit successfully");
                }else{
                 System.out.println("Enter positive value or not enough balance:");
             }
         }
         
          public void TransferSavingToBalance(Saving saving){
          balance += saving.getamountToCredit();
          saving.set_amountToCreditToZero();
        }
         
         

         
         public int getuserid(){
             return user_id;
         }
         public String getname(){
             return name;
         }
         
         public String getemail(){
             return email;
         }
         
         public String getpassword(){
             return password;
         }
        
         public double getbalance(){
             return balance;
         }
         

         

 }    
       
