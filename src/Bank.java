import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank {
    private static final int PORT=9090;

    private static final String [] auctionHouseNames={"AuctionHouse1","AuctionHouse2","AuctionHouse3","AuctionHouse4","AuctionHouse5",
            "AuctionHouse6","AuctionHouse7","AuctionHouse8","AuctionHouse9","AuctionHouse10"};
    private static final String [] agentNames={"Agent1","Agent2","Agent3","Agent4","Agent5","Agent6","Agent7","Agent8","Agent9","Agent10"};

    private static ArrayList<MultipleClientHandler> clients=new ArrayList<>();
    private static ArrayList<MultipleClientHandler> clientsAuctionHouse = new ArrayList<>();
    private static ArrayList<MultipleClientHandler> clientsAgent = new ArrayList<>();

    public static ArrayList<Integer> auctionHousePORT=new ArrayList<>();

    private static final ExecutorService pool= Executors.newFixedThreadPool(10);


    public BankAccount createAccount(String ID, double balance){
        BankAccount account=new BankAccount(generateAccountNumber(),ID);
        account.setBalance(balance);
        return account;
    }
    public BankAccount getAccount(String accountNumber){
        if(!clients.isEmpty()){
            for (int i=0;i<clients.size();i++){
                if(clients.get(i).account.getAccountNumber().equals(accountNumber)){
                    return clients.get(i).account;
                }
            }
        }
        return null;
    }
    public String availablePORTS(ArrayList<Integer> arr) throws IOException {
        String tem = "";
        for (int i=0;i<auctionHousePORT.size();i++){
            tem+=clientsAuctionHouse.get(i).getName()+" PORT: "+arr.get(i)+" ";
        }
        return tem;
    }
    public void transfer(BankAccount current, BankAccount target,double amount){
        current.withDraw(amount);
       // System.out.println(tem);
        target.deposit(amount);
    }
    public String generateAccountNumber(){
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String accountNumber = String.format("%05d", num);
        return accountNumber;
    }

    public void putHold(BankAccount account, double amount){
        account.hold(amount);
    }
    public void removeHold(BankAccount account,double amount){
        account.removeHold(amount);
    }

    public static void main(String[] args) throws IOException {
        Bank bank=new Bank();
        ServerSocket server=new ServerSocket(PORT);
        int count=0;

        while (true){
            System.out.println("Waiting for connection....");
            Socket client=server.accept();
            System.out.println("Connected!!");

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out=new PrintWriter(client.getOutputStream(),true);

            MultipleClientHandler newClient;
            BankAccount tem;

            String signal=in.readLine();

            if(signal.charAt(0)=='$'){
                String[] tok=signal.split(" ");
                System.out.println(tok[1]);
                auctionHousePORT.add(Integer.parseInt(tok[1]));
                tem=bank.createAccount(auctionHouseNames[count],0);
                System.out.println(tem.getAccountNumber());
                newClient = new MultipleClientHandler(client,"$"+auctionHouseNames[count],tem,bank);
                clientsAuctionHouse.add(newClient);
            }else  {
                tem=bank.createAccount(auctionHouseNames[count],100);
                System.out.println(tem.getAccountNumber());
                out.println(bank.availablePORTS(auctionHousePORT));
                newClient = new MultipleClientHandler(client,"#"+agentNames[count],tem,bank);
                clientsAgent.add(newClient);
            }
            clients.add(newClient);
            pool.execute(newClient);
            count++;
        }
    }
}
