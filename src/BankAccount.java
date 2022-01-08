import java.util.ArrayList;

public class BankAccount {
    private double balance;
    private final String accountNumber;
    private final String accountID;
    private ArrayList<Double> holds=new ArrayList<>();

    public BankAccount(String accountNumber,String accountID){
        this.accountNumber=accountNumber;
        this.accountID=accountID;
    }

    public String getID(){
        return accountID;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public void setBalance(double amount){balance=amount;}

    public void hold(double amount){
        holds.add(amount);
    }

    public void removeHold(double amount){
        for (int i=0;i<holds.size();i++){
            if(holds.get(i)==amount){
                holds.remove(i);
                break;
            }
        }
    }

    public void deposit(double amount){
        if(amount>0){
            balance+=amount;
        }
    }
    public void withDraw(double amount){
        if(amount>0 && amount<balance){
            balance=balance-amount;
        }
        else {
            System.out.println("Insufficient fund");
        }
    }
    public double getBalance()
    {
        double holdAmounts=0;
        if(!holds.isEmpty()){
            for (Double hold : holds) {
                holdAmounts += hold;
            }
        }
        return balance-holdAmounts;
    }
}
