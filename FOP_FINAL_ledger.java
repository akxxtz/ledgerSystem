package fop_final_ledger;

import java.util.Scanner;
import java.time.LocalDateTime; 
import java.time.LocalDate;  
import java.time.Duration;                                  //duedate
import java.time.temporal.ChronoUnit; //months difference

import java.io.File;
import java.io.FileWriter;                          //csv
import java.io.FileReader;
import java.io.FileNotFoundException;

import com.opencsv.exceptions.CsvException;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;



 

public class FOP_FINAL_ledger {
    
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    final static  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final Scanner scanner = new Scanner(System.in);
    private static final User user = new User();
    

    
    //private static User[] users = new User[50];   
    private static final ArrayList<User> users = new ArrayList<>();
    private static final ArrayList<Loan> Loans = new ArrayList<>();
    private static final ArrayList<Saving>Savings = new ArrayList<>();
    private static final ArrayList<Transaction>Transactions = new ArrayList<>();
    //private static int userCount = 0;
    
    
  
     
public static short ValidName(String name) {

    if(name == null || name.isEmpty()) {
        return 1;
    }
    for(char LetterinName : name.toCharArray()) {
        if(!(Character.isLetter(LetterinName) || LetterinName == ' ')) {
            return 2;
        }
    }
    if(name.trim().length() < 2) {
        return 3;
    }
    
    return 0;
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////User funtion ///////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static void addUser(User user) { //static void addUser(User user) {
    //if(userCount == users.length) {
        //User[] newUsers = new User[users.length * 2];
        //System.arraycopy(users, 0, userCount, 0, users.length);
        
      //  users = newUsers;
    //}
    //users[userCount++] = user;
    users.add(user);
    
    try (CSVWriter writer = new CSVWriter(new FileWriter("example.csv",true))) {
        // Writing header
        //writer.writeNext(header);

        // Writing data rows
        writer.writeNext(new String[] {String.valueOf(users.size()),user.getname(),user.getemail(),user.getpassword()});

    } catch (IOException e) {
      //  e.printStackTrace();
    }
    
}

private static User findUserByEmail(String email) {        //                  // 2
    for(int i = 0; i < users.size(); i++) {
        if(users.get(i).getemail().equals(email)) {
            return users.get(i);
        }
    }
    return null;
}

private static void readallUserData() {       
    try (CSVReader reader = new CSVReader(new FileReader("example.csv"))) {
       List<String[]>  table = reader.readAll();
       int index = 0;
       for (String[] row : table) {
           index++;
            if (row.length >= 4) {
                users.add(new User(Integer.parseInt(row[0]),row[1], row[2], row[3]));
            } else {
               System.err.println("Skipping incomplete row: " + index);
           }
              
       }
    }
     catch (NumberFormatException | IOException | CsvException e) {
            e.printStackTrace();
     }
}

private static void printUserDetail() {
   for(int i=0;i<users.size();i++){
                 System.out.println("/nUser"+(i+1));
                 System.out.println(users.get(i).getname());
                 System.out.println(users.get(i).getemail());
                 System.out.println(users.get(i).getpassword());
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 //////////////////////////////////////////////Saving function///////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public static void AddSavingtoCSV(User user, Saving saving){   
    String Transaction = "Saving.csv";
    File  csvfile = new File(Transaction);
    try (CSVWriter writer = new CSVWriter(new FileWriter(csvfile,true)))    {
            if (!csvfile.exists() || csvfile.length() == 0){
                 writer.writeNext(new String[] {"Savings_ID", "User_ID" ,   "Percentage" ,"Amount to Transfer" ,"Status"});
         }
        
     
    writer.writeNext(new String[] {String.valueOf(Savings.size()),String.valueOf(user.getuserid()),Double.toString(saving.getpercentage()),Double.toString(saving.getamountToCredit()),saving.getstatus()});
        
        
    } catch (IOException e) {
      //  e.printStackTrace();
    }

}

public static void readallSavingData(){
    try(CSVReader reader = new CSVReader (new FileReader("Saving.csv"))) {
        
        List<String[]> table = reader.readAll();
        
//        int check = 0;
//        int counter = 1;
        
//        for (String[] row :table){
//            check++;
//            System.out.print("Loading Saving Csv!!");
//            if (row.length>=4 ){
//                
//                Savings.add (new Saving(Integer.parseInt(row[0]) , Integer.parseInt(row[1]), Double.parseDouble(row[2]),Double.parseDouble(row[3]),row[4] ));                   //////////////????????????????????
//                
//                System.out.printf ("\nUser%d: SavingID:%s  USERID:%s   Percentage:%s    Status:%s \n",counter++,row[0] , row[1], row[2] , row[3]);                    //test all saving data
//                
//            }else{
//                System.out.println("Skipping incomplete Row : "+check);
//            }
//           
//        }

    for (int index =1;index<table.size();index++){
        String[] row = table.get(index);
        Savings.add(new Saving(Integer.parseInt(row[0]) , Integer.parseInt(row[1]) , Double.parseDouble(row[2])  , Double.parseDouble(row[3]) , row[4]) );
    }
               
    }catch (FileNotFoundException  e) {
         System.err.println("File not found");
    }catch ( CsvException |IOException e  ) {
            e.getMessage();
     }
}



public static void updateSavingInCSV(int index, Saving saving) {
    String Transaction = "Saving.csv";
    File csvfile = new File(Transaction);
    List<String[]> table;

    try (CSVReader reader = new CSVReader(new FileReader(csvfile))) {
        // Read the entire CSV into memory
        table = reader.readAll();

        // Update the specific row (index + 1 because index 0 is the header row)
        if (index + 1 < table.size()) {
            table.set(index + 1, new String[]{
                String.valueOf(saving.getsaving_id()),
                String.valueOf(saving.getuser_id()),
                Double.toString(saving.getpercentage()),
                Double.toString(saving.getamountToCredit()),
                saving.getstatus()
            });
        }
    } catch (IOException | CsvException e) {
        System.err.println("Error reading CSV: " + e.getMessage());
        return;
    }

    // Write the updated table back to the CSV
    try (CSVWriter writer = new CSVWriter(new FileWriter(csvfile))) {
        writer.writeAll(table);
    } catch (IOException e) {
        System.err.println("Error writing CSV: " + e.getMessage());
    }
}

private static int getAllSavingIndexByUserid(User user){ //return array of saving index by the current user
        System.out.println("Searching for Saving index");
        
        for( int idx = 0; idx< Savings.size();idx++){
            if (user.getuserid() == Savings.get(idx).getuser_id())
            {
              System.out.println("Saving index of users: "+ idx);
              return idx;
              
            }
        }
        return -1;
    }

    public static void EditSavingCsvRow(User user,Saving saving) { //if return true means edit , if return false means create new file

        System.out.println("Saving id." + saving.getsaving_id() +"user id."  + user.getuserid());
        String[] SavingToCsvString = {String.valueOf(saving.getsaving_id()),String.valueOf(user.getuserid()),Double.toString(saving.getpercentage()),Double.toString(saving.getamountToCredit()),saving.getstatus()} ;                         //???????????
        int rowToEdit;
        List<String[]> rows = null;
        
        if ((rowToEdit = getAllSavingIndexByUserid(user)) != -1 ){                             //if user id was found from all savings .it returns the index and assign into rowToEdit.
          try (CSVReader reader = new CSVReader(new FileReader("Saving.csv"))){
                // Read the CSV file into memory
                rows = new ArrayList<>(reader.readAll());
                //reader.close();

                // Modify the specific row
                if (rowToEdit >= 0 && rowToEdit < rows.size()) {                  //if there is userid found "index of savings" and index of savings is lesser than the savings list size.
                    rows.set(rowToEdit, SavingToCsvString);
                } else {
                    System.err.println("Row index out of bounds.");
                    return;
                }
                
                

            }catch (FileNotFoundException  e) {
                    System.err.println("File not found");
            } catch (IOException| CsvException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
        System.out.println("Saving row To edit is " + rowToEdit);  

  
        try {
            if (rows != null) {
                // Overwrite the CSV file with writeAll
                try (CSVWriter writer = new CSVWriter(new FileWriter("Saving.csv", false))) { // false means overwrite
                    writer.writeAll(rows);
                    System.out.println("Editing Saving Row");
                }
            } else {
                // Append to the CSV file with writeNext
                try (CSVWriter writer = new CSVWriter(new FileWriter("Saving.csv", true))) { // true means append
                    writer.writeNext(SavingToCsvString);
                    System.out.println("Added one new saving related to user below");
                }
            }

            System.out.println("Row updated successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////Transaction function/////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void AddTransactionToCsv(User user,Transaction transaction){                               
        String Transaction = "Transaction.csv";
        File  csvfile = new File(Transaction);
        try (CSVWriter writer = new CSVWriter (new FileWriter(csvfile,true))){
            if (!csvfile.exists() || csvfile.length() == 0){
                 writer.writeNext(new String[] {                                   //Header
                "Date", "Transaction ID" , "User ID" , "Transaction Type" , "Amount" ,"Balance" ,"Description"
            });
            
                 
            }
           
            writer.writeNext(new String[] {transaction.getdate().format(formatter), String.valueOf(Transactions.size()) , String.valueOf(user.getuserid()) ,
                                            transaction.getTransaction_type() , Double.toString(transaction.getAmountofTransaction()), 
                                            Double.toString(user.getbalance()),transaction.getdescription()});

            
            
            System.out.println("Transaction added successfully to CSV.");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void readallTransactionData(){
        try (CSVReader reader = new CSVReader(new FileReader("transaction.csv"))){
            
            List<String[]> table =reader.readAll();
            

            
            for (int index=1;index<table.size();index++){        
                String[] row =table.get(index);     
               Transactions.add (new Transaction(Double.parseDouble(row[4]) , row[6] , LocalDate.parse(row[0], formatter) ,
                                Integer.parseInt(row[2]) , Double.parseDouble(row[5]) , row[3]));
            }
            
        }catch (FileNotFoundException  e) {
             System.err.println("File not found");
        }
         catch (NumberFormatException | IOException | CsvException e) {
                System.err.println(e.getMessage());
         }
    }
    
    public static void addTransactionToList(double amount,String description ,Transaction transaction,User user){               //use after user debit and credit .
  
            Transactions.add(new Transaction(amount,description,LocalDate.now(),user.getuserid(),user.getbalance(),transaction.getTransaction_type()));
            
    }
    
    private static ArrayList<Integer> getAllTransactionIndexbyUserid(User user){             //  if the current userid match the Userid in every transaction from transactions  &&   check date as well if it is within a month  then add index to 
        ArrayList<Integer> AllTransactionIndexbyUseridList = new ArrayList<>();
       
        for(int index= 0; index<Transactions.size();index++){
        LocalDate TransactionCreatedDate = Transactions.get(index).getdate();
        LocalDate oneMonthLater = TransactionCreatedDate.plus(1, ChronoUnit.MONTHS);
            if(user.getuserid()==Transactions.get(index).getUser_id()&& TransactionCreatedDate.isBefore(oneMonthLater) ){
                AllTransactionIndexbyUseridList.add(index);
            }
            
        }
        return AllTransactionIndexbyUseridList;
    }
    
    
 //////////////////////////////////////////////////////////////////////////////////////////////////////////  
//////////////////////////////////////////////Loan function////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static void readallLoanData() {       
        try (CSVReader reader = new CSVReader(new FileReader("loan.csv"))) {
           List<String[]>  table = reader.readAll();
           int index = 0;
           for (String[] row : table) {
               index++;
               System.out.print("Loading Loan Csv!!");
                if (row.length >= 4) {

                    LocalDateTime dateTime = LocalDateTime.parse("2024-12-26T15:45:30", ISO_FORMATTER);
                    System.out.println("row[0]" +row[0] + "row[1]" +row[1] + "row[2]" +row[2] + "row[3]" +row[3] + "row[4]" +row[4] );
                    Loans.add(new Loan(Integer.parseInt(row[0]),Integer.parseInt(row[1]),
                                          Double.parseDouble(row[2]),Double.parseDouble(row[3]),
                                          Integer.parseInt(row[4]),Double.parseDouble(row[5]),
                                          row[6],dateTime));
                    //Loans.add(new Loan(Loans.size()+1,user.getuserid(),principal,rate,period));
                } else {
                   System.err.println("Skipping incomplete row: " + index);
               }

           }
        }
        catch (FileNotFoundException  e) {
             System.err.println("File not found");
        }
         catch (NumberFormatException | IOException | CsvException e) {
                e.getMessage();
         }
    }


    private static void AddLoanToCsv(User user,Loan loan){


        try (CSVWriter writer = new CSVWriter(new FileWriter("Loan.csv",true))) {
            // Writing header
            //writer.writeNext(header);

            // Writing data rows
            writer.writeNext(new String[] {String.valueOf(Loans.size()),Integer.toString(user.getuserid()),Double.toString(loan.getprincipal_amount()),
                                                          Double.toString(loan.getinterest_rate()), Integer.toString(loan.getrepayment_period())  , 
                                                          Double.toString(loan.getoutstading_balance()) ,loan.getStatus(), loan.getCreatedAt().format(ISO_FORMATTER) });

        } catch (IOException e) {
          //  e.printStackTrace();
        }
    }
    public static void  EditLoanCsvRow(User user,Loan loan,int rowToEdit) {


            String[] LoanToCsvString = {String.valueOf(Loans.size()),Integer.toString(user.getuserid()),Double.toString(loan.getprincipal_amount()),
                                                          Double.toString(loan.getinterest_rate()), Integer.toString(loan.getrepayment_period())  , 
                                                          Double.toString(loan.getoutstading_balance()) ,loan.getStatus(), loan.getCreatedAt().format(ISO_FORMATTER) };
            try {
                // Read the CSV file into memory
                CSVReader reader = new CSVReader(new FileReader("Loan.csv"));
                List<String[]> rows = new ArrayList<>(reader.readAll());
                reader.close();

                // Modify the specific row
                if (rowToEdit >= 0 && rowToEdit < rows.size()) {
                    rows.set(rowToEdit, LoanToCsvString);
                } else {
                    System.err.println("Row index out of bounds.");
                    return;
                }

                // Write the updated rows back to the CSV file
                CSVWriter writer = new CSVWriter(new FileWriter("Loan.csv"));
                writer.writeAll(rows);
                writer.close();

                System.out.println("Row updated successfully.");
            } catch (IOException| CsvException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }

    }
    private static void creditloan(User user){
        System.out.println("Choose application:");
        System.out.println("1.Apply Loan");
        System.out.println("2.Repay Loan");
        System.out.print(">");
        int choice = scanner.nextInt();
        
        switch (choice){
            case 1:
                applyloan(user);
                break;
            case 2:
                repayloan(user);
                break;
            default:
                System.out.println("Invalid input");
        }
           
    }

    private static void applyloan(User user){
        System.out.print("Enter Principal:");
        double principal = scanner.nextDouble();
        
        System.out.print("Enter Interest Rate per month:");
        double rate = scanner.nextDouble();
        
        System.out.print("Repayment Period(month):");
        int period = scanner.nextInt();
        
        double repayment_amount = principal +(principal*rate*period / 100);
        System.out.println("Total Repayment amount = "+repayment_amount);
                                                                                          // start here = Schedule periodic or monthly installment 
        //user.applyloan(principal, rate, period);
       
        Loan loan = new Loan(Loans.size()+1,user.getuserid(),principal,rate,period); //need +1 because the Array is added only after the new class is created
        loan.applyLoan();
        Loans.add(loan);
        

        AddLoanToCsv(user,loan);
        
        System.out.println("loan="+loan.getoutstading_balance());
    }
    

    
    private static ArrayList<Integer> getAllLoanIndexbyUserid(User user){ //return array of loan index by the current user
        System.out.println("Searching for Loan");
        ArrayList<Integer> UserLoansID = new ArrayList<>();
        for( int idx = 0; idx< Loans.size();idx++){
            if (user.getuserid() == Loans.get(idx).getuser_id())
            {
              UserLoansID.add(idx);
              System.out.println("Added Loan to an userid"+Loans.get(idx).getloan_id());
            }
        }
        return UserLoansID;
    }
    
    private static ArrayList<Integer> ListofUserDebt(ArrayList<Integer> UserLoansID){
        
        ArrayList<Integer> UserLoanWithDebt = new ArrayList<>();
        for(int idx = 0 ;idx < UserLoansID.size(); idx++ ){
            
            double principle = Loans.get(UserLoansID.get(idx)).getprincipal_amount();
            double debt = principle + principle* (Loans.get(UserLoansID.get(idx)).getinterest_rate())*(Loans.get(UserLoansID.get(idx)).getrepayment_period()/100.0);
            
            double installment = principle + principle* (Loans.get(UserLoansID.get(idx)).getinterest_rate())*(Loans.get(UserLoansID.get(idx)).getrepayment_period()/100.0)/Loans.get(UserLoansID.get(idx)).getrepayment_period();
            double outstanding_balance  = Loans.get(UserLoansID.get(idx)).getoutstading_balance();
            
           long monthsDifference = ChronoUnit.MONTHS.between(
                Loans.get(UserLoansID.get(idx)).getCreatedAt().plusMonths(1).withDayOfMonth(1),
                LocalDateTime.now().withDayOfMonth(1)
                );
            //Duration duration = Duration.between(LocalDateTime.now(), (Loans.get(UserLoansID.get(idx)).getCreatedAt()) );
            //duration.
            if(monthsDifference <=0){
                break;
            }
            if ( debt - outstanding_balance  < installment*monthsDifference) {
                UserLoanWithDebt.add(idx);
            }
        }   
        
        return UserLoanWithDebt;
        
    }

    private static Boolean CheckUserHasDebt(User user){
        ArrayList<Integer> UserLoanIdxs = getAllLoanIndexbyUserid(user);
        ArrayList<Integer> UserLoanWithDebt = ListofUserDebt(UserLoanIdxs);
        int count =0;
        if (UserLoanWithDebt.isEmpty()){
            return false;
        }
        
        System.out.println("Loan related to user owed:" + user.getuserid());
        for( int loanidx : UserLoanWithDebt){
             System.out.println(++count +":  Loan_id:" + Loans.get(loanidx).getloan_id()+"   Due Date:"+Loans.get(loanidx).getDueDate()+"   oustanding balance:"+ 
                                        Loans.get(loanidx).getoutstading_balance()+ "  status:" + Loans.get(loanidx).getStatus());
        }
        return true;
    }
    private static double getloan(User user){ //sum of all loan owed by user
        ArrayList<Integer> UserLoanIdxs = getAllLoanIndexbyUserid(user);
        double TotalLoanForCurrentUser = 0;
        for( int loanidx : UserLoanIdxs){
             TotalLoanForCurrentUser +=  Loans.get(loanidx).getoutstading_balance();   
             System.out.println("Calculating Total Loan for an userid");
        }
        return TotalLoanForCurrentUser;
    }
    
    private static void repayloan(User user){ //repay a specific loan selected by user
        
   
        ArrayList<Integer> UserLoanIdxs = getAllLoanIndexbyUserid(user);
        int count =0;
        if (UserLoanIdxs.isEmpty()){
            System.out.println("No Loan found related current user ");
            return;
        }
        System.out.println("Loan related to user is shown below:" + user.getuserid());
        for( int loanidx : UserLoanIdxs){
                System.out.println(++count +":  Loan_id:" + Loans.get(loanidx).getloan_id()+"   Due Date:"+Loans.get(loanidx).getDueDate()+"   oustanding balance:"+ 
                                        Loans.get(loanidx).getoutstading_balance()+ "  status:" + Loans.get(loanidx).getStatus());
        }
 
   
        System.out.println("Enter which Loan to repay");
        int loanToRepay = scanner.nextInt();
        while(loanToRepay > count || loanToRepay <= 0) {
           System.out.println("Invalid input,Enter which Loan to repay");
           loanToRepay = scanner.nextInt();
        }
        
        
        System.out.println("Enter amount");
        double amount = scanner.nextDouble();
        
        Loans.get(UserLoanIdxs.get(loanToRepay-1)).repayloan(amount); 
    
        //Write to CSV below
        EditLoanCsvRow( user, Loans.get(UserLoanIdxs.get(loanToRepay-1)), UserLoanIdxs.get(loanToRepay-1));
    }
    
    private static void debit_menu(User user ){                                  
        if (CheckUserHasDebt(user)){
             System.out.println("You have Unpaid loan.");
             return;
        }
        
        System.out.print("Enter amount");
        double amount = scanner.nextDouble();
        
        Saving saving = null;
        int Savingsindex =0;
        for(int index =0; index< Savings.size();index++){
            Saving tempsaving = Savings.get(index);
            if (user.getuserid() == tempsaving.getuser_id()  && tempsaving.getstatus().equalsIgnoreCase("active")){
            saving = tempsaving;
            Savingsindex=index;
            break;
            }
        }               
        if (saving ==null){
         user.debit(amount);
        }else{
         saving.set_amountToCredit(amount);
         user.debitForSaving(amount, saving);
         
         System.out.println("user_id"+saving.getuser_id()+"Saving"+saving.getamountToCredit()+"transaction type"+saving.getstatus())   ;    
         
         //set status to inactive
         saving.setInactiveTransactionType();           
         Savings.set(Savingsindex, saving); 
         updateSavingInCSV(Savingsindex , saving);
         
        }
        
        
       
        //check if user is active
        
//        user.debit(amount,saving);                                           //error 1
//        EditSavingCsvRow(user, saving);
        scanner.nextLine();
        System.out.print("Enter Description:");
        String description = scanner.nextLine();
        while (description.length()>=100){
            System.out.println("It's exceed the maximum length of description. Please Enter description within 100 Character.");
            description = scanner.nextLine();
        }
        
        Transaction transaction = new Transaction(amount,description,LocalDate.now(),user.getuserid() , user.getbalance() ,"Debit");
        addTransactionToList(amount,description,transaction,user);     
        AddTransactionToCsv(user,transaction);                                    //user , transaction       //write to csv . but first need to know all the transaction from same user.
         
     }
    
    private static void credit_menu(User user){
         if (CheckUserHasDebt(user)){
             System.out.println("You have Unpaid loan.");
             return;
        }


       System.out.println("Enter amount");
       double amount = scanner.nextDouble();
       scanner.nextLine();
       
        System.out.print("Enter Description:");
        String description = scanner.nextLine();
        while (description.length()>=100){
            System.out.println("It's exceed the maximum length of description. Please Enter description within 100 Character.");
            description = scanner.nextLine();
        }
        
        Transaction transaction = new Transaction(amount,description,LocalDate.now(),user.getuserid() , user.getbalance() ,"Credit");
        addTransactionToList(amount,description,transaction,user);     
        AddTransactionToCsv(user,transaction);  
       
       user.credit(amount);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////deposit interest Predictor Function/////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static void bankMenu(){    
        
    System.out.println(" ----------------------------------------------------------- ");
    System.out.printf("| %-3s | %-25s | %-20s |\n", "No.", "Bank", "Interest Rate (%)");
    System.out.println(" ----------------------------------------------------------- ");
    
    // Table rows with numbers
    System.out.printf("| %-3d | %-25s | %-20s |\n", 1, "Rhb Bank", "2.6");
    System.out.printf("| %-3d | %-25s | %-20s |\n", 2, "Maybank", "2.5");
    System.out.printf("| %-3d | %-25s | %-20s |\n", 3, "Hong Leong", "2.3");
    System.out.printf("| %-3d | %-25s | %-20s |\n", 4, "Alliance", "2.85");
    System.out.printf("| %-3d | %-25s | %-20s |\n", 5, "AmBank", "2.55");
    System.out.printf("| %-3d | %-25s | %-20s |\n", 6, "Standard Chartered", "2.65");
    System.out.println(" ----------------------------------------------------------- ");
    }
    
  
    

    public static void main(String[] args) { 
        
         enum Page {
         main_page,
         register_page,
         login_page,
         transaction_page,
         
    }
        Page curretpage = Page.main_page;
        String name = null;
        String email = null;
        String password = null;
        User user = null;
        Saving saving = null;
        Transaction transaction = null;
        readallUserData();
        readallLoanData();
        readallSavingData();
        readallTransactionData();
        
    
//        for(int index =0 ;index < Savings.size();index++){                              //testin  on reading transaction data .
//            Saving Saving = Savings.get(index);
//            System.out.println(Saving.getpercentage());
//            
//        }    
            
            
            
        
        boolean run = true;
         while(run)       
        switch (curretpage) {
            case main_page:
                System.out.println("== Ledger System (name can created yourself) ==");
                System.out.println("Login or Register:");
                System.out.println("1.Register");
                System.out.println("2.Login");

                System.out.print("\n>");
                Integer LoginRegister = scanner.nextInt();
                scanner.nextLine();
                if(LoginRegister == 1)
                    curretpage = Page.register_page;
                else if (LoginRegister == 2)
                    curretpage = Page.login_page;
            break;
                
            case register_page:
 
             name = null;
             email = null  ; 
             password = null;
             short errcode ;
             do {
             
            if (name == null){
                   System.out.print("Name: ");
                   name = scanner.nextLine();
                
                 if ((errcode =ValidName(name)) != 0){
                     System.out.println("Invalid name!");
                        name = null;
                 }
                if (errcode == 1) {
                     System.out.println("Name can not be empty");
                 }
                if (errcode == 2) {
                     System.out.println("Not space allowed in name");
                 }
                 
             }
             else if (email == null){
                 System.out.print("Email: ");
                 email = scanner.nextLine();
                 
                 if (!ValidEmail(email)){
                    System.out.println("Invalid email!");
                    email = null;
                }
             }
             else if (password == null){
                 System.out.print("Password: ");
                 password = scanner.nextLine();
                 
                 if (!ValidPassword(password)){
                    System.out.println("Invalid password!");
                    password = null;
                 }else{
                 System.out.print("Password Confirmation:");
                 String confirm_pass = scanner.nextLine();
                 
                 if (!(password.equals(confirm_pass))){
                     System.out.println("Password Confirmation not match");
                     password=null;
                 }
                 }
                 
             }
            
             
             } while (name == null || email == null || password == null);

             user = new User(users.size(),name, email, password);
             addUser(user);

             System.out.println("\nRegister Successful!!! \n");
             curretpage = Page.transaction_page;
         break;
         
         
         
         case login_page:
             System.out.println("== Please enter your email and password ==");

             System.out.print("Email: ");
              email = scanner.nextLine();

             System.out.print("Password: ");
              password = scanner.nextLine();

             user = findUserByEmail(email);                              

             if(user != null && user.getpassword().equals(password)) {                     //1
                 System.out.println("Login Successful!!!");
                 curretpage = Page.transaction_page;

             }else {
                 System.out.println("Invalid email or password, Please try again");
             }
              break;
  
                
         case transaction_page:
             
             //loading saving 
             
             int savingindex = -1;
             if ((savingindex = getAllSavingIndexByUserid(user)) != -1 ){                                        //if user id was not found in any Savings return -1, otherwise return the position from all saving
                saving  = Savings.get(savingindex);
                LocalDateTime now = LocalDateTime.now();                                                        // add saving to balance at the end of the month??????????????????????????????????????????????????????
                if (now.getDayOfMonth() == now.toLocalDate().lengthOfMonth()){
                 user.TransferSavingToBalance(saving);
                }
             }
           
             
             
             

             
             
         
             System.out.println("== Welcome, " + user.getname() + " ==");
             System.out.println("Balance: "+user.getbalance());                                            //add value
             if (saving == null)
                System.out.println("Savings: 0.0");
             else
                 System.out.println("Savings: "+saving.getamountToCredit());
             System.out.println("Loan: "+ getloan(user));

             System.out.println("\n== Transaction ==");
             System.out.println("1.Debit");
             System.out.println("2.Credit");
             System.out.println("3.History");
             System.out.println("4.Saving");
             System.out.println("5.Credit Loan");
             System.out.println("6.Deposit Interest Predictor");
             System.out.println("7.Logout");
             System.out.println("8.Debug Only");
             System.out.print("choice:");
             int choice = scanner.nextInt();
             if (choice == 8)                                             //debug
                 printUserDetail();


             while (choice<=0 || choice>7){
                 System.out.println("Invalid choice");
                 System.out.print("choice:");
                 choice = scanner.nextInt();
             }
           
           
                
             if (choice == 1) {                                                      //问题no single transaction for user . so cant read as argument.
               
                 debit_menu(user);                                       //error 1:saving 拿出来了   
                          
             }

             if (choice == 2) {
                 credit_menu(user);
             }
             if (choice ==3){
                // readalltransaction index by userid 
                System.out.println("=====History=====");
                if (getAllTransactionIndexbyUserid(user).isEmpty()){
                    System.out.println("No available Transaction");                   
                }else{             
                    for (int index=0 ;index < getAllTransactionIndexbyUserid(user).size();index++){
                         transaction = Transactions.get(getAllTransactionIndexbyUserid(user).get(index));
                        System.out.printf("\nDate : "+ transaction.getdate()+"| Description : "+ transaction.getdescription()+ "| Transaction Type : "+ transaction.getTransaction_type()+"| Balance : "+transaction.getbalance()+"\n");
                        
                    }
                }
             }

             if (choice == 4) {
                 boolean SavingFound = false;
                 for(int index = 0;index < Savings.size();index++) {                                //check Saving status
                     if (user.getuserid()==Savings.get(index).getuser_id() ){
                         SavingFound = true;
                     
                         if (Savings.get(index).getstatus().equalsIgnoreCase("active")){
                            System.out.println("Saving status Exist");
                            
                         break;
                        }
                     }                   
                }
                 
                 
                 if(!SavingFound){
                 System.out.println("== Saving ==");
                 System.out.print("Are you sure you want to activate it?(Y/N):");
                 char decision = scanner.next().charAt(0);
                 decision = Character.toLowerCase(decision);
                 
                 // check if already set saving exist or not if exist ask for reset?
                 
                    if (decision == 'y'||decision =='n'){
                     if(decision== 'y'){
                         System.out.print("Please enter the percentage you wish to deduct from the next debit:");
                         double percentage = scanner.nextDouble();
                         
                           
                          
                           saving = new Saving(user.getuserid() , percentage , "active" );
                           Savings.add(saving);
                           AddSavingtoCSV(user,saving);
       
                     }
                    }else{
                     System.out.println("invalid input. Enter only [Y/N]");
                    }
                
                 }
             }
             
              if (choice == 5){
                  creditloan(user);
              }
              
             if (choice ==6){
                 bankMenu();
                 System.out.print("Choose Bank :");
                 int bank = scanner.nextInt();
                    while (bank <=0 || bank>6){
                    System.out.println("Invalid output (Enter [1-6] only)");
                     bank = scanner.nextInt();
                     }
                double interest_rate=0;
                switch (bank){
                    case 1 :
                         interest_rate = 2.6;
                        break;
                    case 2 :
                         interest_rate = 2.5;
                        break;
                    case 3 :
                         interest_rate = 2.3;
                        break;
                    case 4 :
                         interest_rate = 2.85;
                        break;
                    case 5 :
                         interest_rate = 2.55;
                        break;
                    case 6 :
                         interest_rate= 2.65;
                        break;
                }
                
                 System.out.print("Enter Deposit :");
                 double deposit = scanner.nextDouble();
                 scanner.nextLine();
                                 
                  System.out.print("Time Period [monthly , daily, annually] : ");
                  String TimePeriod = scanner.next();
                  TimePeriod.toLowerCase();
                  
                  while (!(TimePeriod.equals("monthly")||TimePeriod.equals("daily")||TimePeriod.equals("annually"))){
                      System.out.print("Invalid Time Period .Enter only [monthly , daily, annually] : ");
                      TimePeriod = scanner.next();
                      TimePeriod.toLowerCase();
                  }
                  
                  double Interest =0;
                  switch (TimePeriod){
                      case "monthly":
                          Interest = (deposit*interest_rate)/12;
                          break;
                      case "daily":
                          Interest = (deposit*interest_rate)/365;
                          break;
                      case "annually":
                          Interest = (deposit*interest_rate);
                          break;
                  }
                  
                  System.out.println(TimePeriod + "Interest : "+Interest);
                 
                 
                 
             }

             if (choice == 7) {
                 curretpage = Page.main_page;
                 System.out.println("Thank you for Using Ledger System ");
             }
         break;
         
        
       
          
           
           
           
           
            
           
           
           
           
           
           
           
           
        }
    }
    
    
    
}
 

