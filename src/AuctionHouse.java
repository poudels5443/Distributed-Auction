import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class AuctionHouse {
    private static String houseID = null;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<Double> bid=new ArrayList<>();
    static Random random = new Random();


    public AuctionHouse(String Id){
        this.houseID=Id;
    }
    public String getHouseID(){
        return houseID;
    }
    public String generateItemID(){
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000);
        String itemID = String.format("%03d", num);
        return itemID;
    }

    public static void main(String[] args) throws IOException {
        //AuctionHouse auctionHouse=new AuctionHouse("AuctionHouse1");

        Socket socket = new Socket("127.0.0.1",9090);

        BufferedReader keyboardRead=new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter send = new PrintWriter(socket.getOutputStream(),true);

        int rand=random.nextInt(1000);

        String sendName="$ "+rand;
        send.println(sendName);

        AgentServer server=new AgentServer(rand);
        new Thread(server).start();

        while (true){

            String sendMessage=keyboardRead.readLine();
            send.println(sendMessage);

            if(sendMessage.equals("Exit")) break;

            String message=in.readLine();
            System.out.println(message);
        }
        socket.close();
        System.exit(0);
    }
}
