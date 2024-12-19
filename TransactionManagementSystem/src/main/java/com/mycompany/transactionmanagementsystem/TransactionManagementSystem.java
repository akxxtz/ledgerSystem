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
        
        double balance = 0.00; 
        ArrayList<String> transactionsIn = new ArrayList<>(); 
        ArrayList<String> transactionsOut = new ArrayList<>(); 
        Scanner scanner = new Scanner(System.in);
        String continueTransaction;

        do {
            System.out.println("What type of transcation you want to do?");
            System.out.println("1. CASH IN\n2. CASH OUT");
            System.out.print("Your choice: ");
            int type = scanner.nextInt(); 

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
                switch (type) {
                    case 1: 
                        balance += amount;
                        transactionsIn.add("RM " + amount + " cashed in on " + date + " - " + description);
                        break;
                    case 2:
                        balance -= amount;
                        transactionsOut.add("RM " + amount + " cashed out on " + date + " - " + description);
                        break;
                    default:
                        System.out.println("Please enter the correct number");
                }
            } else {
                System.out.println("Transaction invalid.");
            }
            
            System.out.println("Your current balance is: " + balance);

            System.out.print("Would you like do another transaction? (yes/no): ");
            continueTransaction = scanner.nextLine();
        } while (continueTransaction.equalsIgnoreCase("yes"));
        
        System.out.println("Transaction History:");
        for (String transactionIn : transactionsIn) {
            System.out.println(transactionIn);
        }
        for (String transactionOut : transactionsOut) {
            System.out.println(transactionOut);
        }
        scanner.close();
    }
}