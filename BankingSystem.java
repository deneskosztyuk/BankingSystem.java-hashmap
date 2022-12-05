import java.util.*;
import java.text.*;

interface transactionAccount{
    final double exchangeRate = 0.05, limitLower = 100, limitHigher = 10000;
    void deposit(double n, Date d);
    void withdraw(double n, Date d);
}

class User implements transactionAccount{
    String accountNumber;
    String holderName;
    String holderAddress;
    double balance;
    ArrayList<String> transactions;
    User(String accountNumber, String holderName, String holderAddress, double balance, Date date){
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.holderAddress = holderAddress;
        this.balance = balance;
        this.transactions = new ArrayList<String>(6);
        addTransaction(String.format("Initial deposit - " + NumberFormat.getCurrencyInstance().format(balance)+ " as on " + "%1$tD"+" at "+"%1$tT.", date));
    }

    void update (Date date){
        if (balance >= limitHigher){
            balance += exchangeRate*balance;
        } else {
            balance -= (int)(balance/limitLower);
        }
        addTransaction(String.format("Account updated. New balance: " + NumberFormat.getCurrencyInstance().format(balance)+ " as on " + "%1$tD" + " at " + "%1$tT.", date));
    } 

    @Override
    public void deposit(double amount, Date date){
        balance += amount;
        addTransaction(String.format(NumberFormat.getCurrencyInstance().format(amount) + " credited to your account. New balance is: " + NumberFormat.getCurrencyInstance().format(balance)+ " as on " + "%1$tD" + " at " + "%1$tT.", date));
    }

    @Override
    public void withdraw(double amount, Date date){
        if (amount >(balance-100)){
            System.out.println("Insufficient balance.");
            return;
        }
        balance -= amount;
        addTransaction(String.format(NumberFormat.getCurrencyInstance().format(amount) + " debited from your account. New balance is: " + NumberFormat.getCurrencyInstance().format(balance) + " as on " + "%1$tD" + " at " + "%1$tT.", date));
    }

    private void addTransaction(String message){
        transactions.add(0, message);
        if (transactions.size()>6) {
            transactions.remove(6);
            transactions.trimToSize();
        }
    }
}

class Bank {
    Map<String, User> userMap;

    Bank(){
        userMap = new HashMap<String, User>();

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String accountNumber;
        Bank bank = new Bank();
        int choice;
            outer: while (true){
            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println("-------------------------------------------------");
            System.out.println("> > > > > > > > > BING SHILLING BANK > > > > > > ");
            System.out.println("1.  Register an account.  ");
            System.out.println("2.  Search an account.  ");
            System.out.println("3.  Delete an account.  ");
            System.out.println("4.  Exit application.  ");
            System.out.println("-------------------------------------------------");
            System.out.println("> > > > > > PLEASE CHOOSE AN OPTION > > > > > > >");
            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            choice = scanner.nextInt();
            scanner.nextLine();
            User user;
            switch (choice) {
                case 1:
                    System.out.println("Enter account number: ");
                    accountNumber = scanner.nextLine();

                    System.out.println("Enter account holder name: ");
                    String holderName = scanner.nextLine();

                    System.out.println("Enter account holder address: ");
                    String holderAddress = scanner.nextLine();

                    System.out.println("Please enter new account balance: ");
                    double amount = scanner.nextDouble();

                    user = new User(accountNumber, holderName, holderAddress, amount, new Date());
                    bank.userMap.put(accountNumber, user);
                    break;

                case 2:
                    System.out.println("Please enter account number you wish to search: ");
                    accountNumber = scanner.nextLine();
                    if (bank.userMap.containsKey(accountNumber)){
                        user = bank.userMap.get(accountNumber);
                        System.out.println("--------------");
                        System.out.println("Account found!");
                        System.out.println("--------------");
                        while (true) {
                            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                            System.out.println("-------------------------------------------------");
                            System.out.println("< < < < <    ACCOUNT MANAGEMENT TOOL     > > > > >");
                            System.out.println("1. Deposit NEW amount");
                            System.out.println("2. Withdraw an amount");
                            System.out.println("3. View your previous transactions");
                            System.out.println("4. View account details");
                            System.out.println("5. Return to main menu");
                            System.out.println("-------------------------------------------------");
                            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            switch (choice) {
                                case 1:
                                    System.out.println("Enter an amount you'd like to deposit: ");
                                    while (!scanner.hasNextDouble()){
                                        System.out.println("Invalid amount, try again: ");
                                        scanner.nextLine();
                                    }
                                    amount = scanner.nextDouble();
                                    scanner.nextLine();
                                    user.deposit(amount, new Date());
                                    break;
                                case 2:
                                    System.out.println("Enter an amount you'd like to withdraw: ");
                                    while (!scanner.hasNextDouble()){
                                        System.out.println("Invalid amount, try again: ");
                                        scanner.nextLine();
                                    }
                                    amount = scanner.nextDouble();
                                    scanner.nextLine();
                                    user.withdraw(amount, new Date());
                                    break;
                                case 3:
                                    for(String transactions : user.transactions){
                                        System.out.println(transactions);
                                    }
                                    break;
                                case 4: 
                                    System.out.println("Account holder name: " + user.holderName);
                                    System.out.println("Account holder address: " + user.holderAddress);
                                    System.out.println("Account balance: " + "£" + user.balance);
                                    break;
                                case 5:
                                    continue outer;
                                default:
                                    System.out.println("> > > > > > > > > > >");
                                    System.out.println("Wrong choice entered!");
                                    System.out.println("< < < < < < < < < < <");
                        }
                    } 
                } 
                else {
                    System.out.println("Wrong account number entered.");
                }
                case 3:
                    System.out.println("Enter an account number to delete it: ");
                    accountNumber = scanner.nextLine();
                    if (bank.userMap.containsKey(accountNumber)){
                        bank.userMap.remove(accountNumber).update(new Date());
                        System.out.println("-------------------------------------------------");
                        System.out.println("Account: " + accountNumber + " successfully deleted!");
                        System.out.println("-------------------------------------------------");
                    } else {
                        System.out.println("Account number invalid or doesn't exist!");
                    }
                    break;
                case 4:
                    System.out.println("Thank you for choosing BING SHILLING BANK.");
                    System.exit(1);
                    break;
                default:
                    System.out.println("Wrong choice entered!");
            } 
        }
    }
}

