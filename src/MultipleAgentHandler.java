import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultipleAgentHandler implements Runnable{
    private final Socket agent;
    private final String name;
    private final BufferedReader in;
    private final PrintWriter out;

    public MultipleAgentHandler(Socket agent, String name) throws IOException {
        this.agent=agent;
        this.name=name;
        in=new BufferedReader(new InputStreamReader(agent.getInputStream()));
        out=new PrintWriter(agent.getOutputStream(),true);
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
                    if (input.equals("Exit")) break;
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
        }
    }
}
