import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/*
Group: Saphal karki, Safal Poudel, Karan Aryal
Project: 5
CS 351
5/11/2021
It is the buffer reader class for the GUI. Since reading from the
multiple auction house blocks the GUI thread, this class is created
to listen for the agent separately.
 */
public class AgentListener implements Runnable{
    AgentGUI agentGUI;
    private final BufferedReader in;
    public AgentListener(Socket s,AgentGUI agentGUI) throws IOException {
        this.agentGUI=agentGUI;
        in=new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
    @Override
    public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    String [] tok=message.split("-");
                    if(tok[0].equals("Success")) {
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                agentGUI.alert("Success");
                                getBalance(tok);
                            }
                        });
                    }else if(tok[0].equals("Fail")){
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                agentGUI.alert("Fail");
                            }
                        });
                    }
                    else if(tok[0].equals("Outbidden")){
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                getBalance(tok);
                            }
                        });
                    }
                    else if(tok[0].equals("Win")){
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                int outbidItem=Integer.parseInt(tok[1])+1;
                                agentGUI.wonItems.add(tok[2]);
                                agentGUI.winnerLabel.setText("The auction is over for item"+outbidItem+", you won this item ");
                                agentGUI.vBox2.getChildren().clear();
                                agentGUI.vBox2.getChildren().addAll(agentGUI.winLabel,agentGUI.wonItems());
                            }
                        });
                    }
                    else {
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                agentGUI.sendBank.println("Balance");
                                try {
                                    String bal=agentGUI.fromBank.readLine();
                                    System.out.println(bal);
                                    agentGUI.balance.setText("Balance: "+bal);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                agentGUI.temItem.set(Integer.parseInt(tok[2]),tok[1]);
                                agentGUI.flowPaneItem.getChildren().set(Integer.parseInt(tok[2]),agentGUI.updateItems
                                        (agentGUI.temItem.get(Integer.parseInt(tok[2]))+" "+tok[2]));
                            }
                        });
                    }
                }
            } catch (IOException e) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        agentGUI.alert("Connection Lost");
                        agentGUI.flowPaneItem.getChildren().clear();
                        try {
                            agentGUI.updateConnections();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
                System.err.println("Connection Lost");
            }
            finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private void getBalance(String[] tok) {
        agentGUI.sendBank.println("Balance");
        try {
            String bal=agentGUI.fromBank.readLine();
            agentGUI.balance.setText("Balance: "+bal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int outbidItem=Integer.parseInt(tok[2])+1;
        agentGUI.outbidLabel.setText("Item "+outbidItem+" has been outbidded.");
        agentGUI.temItem.set(Integer.parseInt(tok[2]),tok[1]);
        agentGUI.flowPaneItem.getChildren().set(Integer.parseInt(tok[2]),agentGUI.updateItems
                (agentGUI.temItem.get(Integer.parseInt(tok[2]))+" "+tok[2]));
        agentGUI.Timed(tok[1],Integer.parseInt(tok[2]),Integer.parseInt(tok[3]));
    }
}
