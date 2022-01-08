import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MultipleClientHandler implements Runnable{
    private final Bank bank;
    //private final Socket client;
    private final String name;
    BankAccount account;
    private final BufferedReader in;
    private final PrintWriter out;


    public MultipleClientHandler(Socket client,String name,BankAccount account,Bank bank) throws IOException {
        //this.client=client;
        this.name=name;
        this.account=account;
        this.bank=bank;
        in=new BufferedReader(new InputStreamReader(client.getInputStream()));
        out=new PrintWriter(client.getOutputStream(),true);
    }

    public String getName(){
        return name;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String input = in.readLine();
                if (input != null) {
                    String[] tokens = input.split(" ");
                    String command = tokens[0];
                    Double amount;
                    BankAccount bankaccount;
                    if (name.charAt(0) == '$') {
                        switch (command) {
                            case "Block":
                                amount=Double.parseDouble(tokens[1]);
                                bankaccount=bank.getAccount(tokens[2]);
                                if(bankaccount.getBalance()>=amount){
                                    bank.putHold(bankaccount,amount);
                                    out.println("Success");
                                }else out.println("Fail");
                                break;
                            case "Unblock":
                                amount=Double.parseDouble(tokens[1]);
                                bankaccount=bank.getAccount(tokens[2]);
                                bank.removeHold(bankaccount,amount);
                                break;
                            case "Balance":
                                out.println(account.getBalance());
                                break;
                            default:
                                System.out.println("Invalid operation");
                        }
                    } else {
                        switch (command) {
                            case "Transfer":
                                amount=Double.parseDouble(tokens[1]);
                                System.out.println(amount);
                                bankaccount=bank.getAccount(tokens[2]);
                                if(account.getBalance()>=amount){
                                    bank.transfer(account,bankaccount,amount);
                                    out.println("Success");
                                }else out.println("Fail");
                                break;
                            case "Balance":
                                out.println(account.getBalance());
                                break;
                            default:
                                System.out.println("Invalid operation");
                        }
                    }
                   // System.out.println(command);
                    if (command.equals("Exit")) break;
                }
                else out.println("Invalid input");
            }
        }catch (IOException e){
            System.err.println("IO Exception found");
        }
        finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
         //   System.exit(0);
        }
    }
//    private void cast(String message){
//        for(MultipleClientHandler c: bank.clients){
//            if(c.client!=client) c.out.println(message);
//        }
//    }
}
