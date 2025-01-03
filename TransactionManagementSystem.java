/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.transactionmanagementsystem;

/**
 *
 * @author 4d3pu
 */
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionManagementSystem {
    public static void main(String[] args) {
        
        double balance = 0.0; 
        ArrayList<String> transactions = new ArrayList<>(); 
        Scanner scanner = new Scanner(System.in);
        String continueTransaction;

        do {
            System.out.print("What type of transaction would you like to record? (cash in/withdraw): ");
            String type = scanner.nextLine(); 

            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 

            System.out.print("Enter date (DD-MM-YYYY): ");
            String date = scanner.nextLine();

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            boolean transactionValid = true;
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero");
                transactionValid = false;
            }
            if (date.isEmpty()) {
                System.out.println("Date cannot be empty");
                transactionValid = false;
            }
            if (description.length() > 100) {
                System.out.println("Description must be less than 100 characters");
                transactionValid = false;
            }

            if (transactionValid) {
                if (type.equalsIgnoreCase("cash in")) { 
                    balance += amount; 
                    transactions.add("Cash In: " + amount + " on " + date + " - " + description);
                } else if (type.equalsIgnoreCase("withdraw")) { 
                    balance -= amount; 
                    transactions.add("Withdraw: " + amount + " on " + date + " - " + description);
                } else {
                    System.out.println("Please enter 'cash in' or 'withdraw' only.");
                }
            } else {
                System.out.println("Transaction invalid.");
            }

            System.out.print("Would you like do another transaction? (yes/no): ");
            continueTransaction = scanner.nextLine();
        } while (continueTransaction.equalsIgnoreCase("yes"));

        System.out.println("Your current balance is: " + balance);
        System.out.println("Transaction History:");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
        scanner.close();
    }
}