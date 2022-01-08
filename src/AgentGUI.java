import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
/*
Group: Saphal karki, Safal Poudel, Karan Aryal
Project: 5
CS 351
5/11/2021
It is the agent GUI class. Here all the required GUI are created.
And its socket sends the given command by the instruction of users.
 */
public class AgentGUI extends Application {
    private static final Integer STARTTIME = 30;
    static BufferedReader fromAuctionHouse;
    static PrintWriter sendAuctionHouse;
    static ArrayList<IntegerProperty> timeSeconds = new ArrayList<>();
    static ArrayList<Timeline> timelines = new ArrayList<>();
    ArrayList<String> auctionHouse = new ArrayList<>();
    ArrayList<Integer> ports = new ArrayList<>();
    Socket socket2;
    boolean myBid;
    BufferedReader fromBank;
    PrintWriter sendBank;
    String item1;
    String item2;
    String item3;
    int portNumber;
    boolean error = false;
    int indexOfCombo;
    boolean first = true;
    boolean endCon = true;
    ArrayList<String> temItem = new ArrayList<>();
    ArrayList<String> wonItems = new ArrayList<>();
    VBox vBox2 = new VBox(10);
    Label winLabel = new Label("List of won items: ");
    BorderPane pane = new BorderPane();
    ComboBox<String> comboBox;
    VBox comboVbox;
    Label balance = new Label();
    Label outbidLabel = new Label();
    Label winnerLabel = new Label();

    FlowPane flowPaneItem = new FlowPane();


    static String host;
    public static void main(String[] args) {
        host = args[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        int HEIGHT = 800;
        int WIDTH = 800;
        Label accountNumber = new Label();

        IntegerProperty timeSecond1 = new SimpleIntegerProperty(STARTTIME);
        IntegerProperty timeSecond2 = new SimpleIntegerProperty(STARTTIME);
        IntegerProperty timeSecond3 = new SimpleIntegerProperty(STARTTIME);
        timeSeconds.add(timeSecond1);
        timeSeconds.add(timeSecond2);
        timeSeconds.add(timeSecond3);

        Timeline timeline1 = new Timeline();
        Timeline timeline2 = new Timeline();
        Timeline timeline3 = new Timeline();
        timelines.add(timeline1);
        timelines.add(timeline2);
        timelines.add(timeline3);

        FlowPane flowPane = new FlowPane();
        flowPane.setVgap(100);

        Socket socket1 = new Socket(host, 9090);
        fromBank = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        sendBank = new PrintWriter(socket1.getOutputStream(), true);

        sendBank.println("#");
        String tem = fromBank.readLine();
        String[] tokens = tem.split(" ");

        int count = 1;
        while (count < tokens.length) {
            auctionHouse.add(tokens[count]);
            count++;
            ports.add(Integer.parseInt(tokens[count]));
            count++;
        }

        VBox comboVbox = new VBox(10);
        HBox hBox2 = new HBox();
        Label title = new Label("Welcome to Online Auction House.");
        title.setFont(Font.font(40));
        hBox2.getChildren().add(title);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.setStyle("-fx-background-color: #B3DDF9;");
        pane.setTop(hBox2);

        Button exit = new Button("EXIT");
        exit.setAlignment(Pos.BOTTOM_LEFT);
        exit.setOnMousePressed(event -> {
            if (endCon) {
                try {
                    socket2.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button end = new Button("End this auction");
        end.setAlignment(Pos.BOTTOM_LEFT);
        end.setOnMousePressed(event -> {
            if (endCon) {
                flowPaneItem.getChildren().clear();
                comboBox.setDisable(false);
                end.setDisable(true);
            }
        });

        comboBox = new ComboBox<>(FXCollections.observableArrayList(auctionHouse));
        comboBox.setPromptText("List of open Auction House.");

        winLabel.setFont(Font.font(25));
        winLabel.setUnderline(true);

        sendBank.println("Balance");
        String bal = fromBank.readLine();

        balance.setText("Balance: " + bal);
        balance.setFont(new Font(20));
        balance.setAlignment(Pos.TOP_LEFT);

        accountNumber.setText("Account Number: " + tokens[0]);
        accountNumber.setFont(new Font(20));
        accountNumber.setAlignment(Pos.TOP_LEFT);


        Label auctionUpdate = new Label("Auction Update");
        auctionUpdate.setFont(Font.font(25));
        auctionUpdate.setUnderline(true);
        outbidLabel.setText("Item outbid status:");
        winnerLabel.setText("Winner:                     " +
                "                                    ");
        VBox update_vbox = new VBox(10);


        update_vbox.getChildren().addAll(auctionUpdate, outbidLabel, winnerLabel);
        comboVbox.getChildren().addAll(accountNumber, balance, comboBox, end, exit);
        vBox2.getChildren().addAll(winLabel, wonItems());

        comboVbox.setAlignment(Pos.CENTER_LEFT);
        vBox2.setAlignment(Pos.CENTER_LEFT);
        flowPane.getChildren().addAll(comboVbox, update_vbox, vBox2);
        flowPane.setStyle("-fx-background-color: #03FCE8;");
        pane.setLeft(flowPane);

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (auctionHouse.contains(newValue)) {
                indexOfCombo = auctionHouse.indexOf(newValue);
                if (!first) {
                    flowPaneItem.getChildren().clear();
                }
                portNumber = ports.get(auctionHouse.indexOf(newValue));
                try {
                    error = false;
                    end.setDisable(false);
                    socket2 = new Socket(host, portNumber);
                    fromAuctionHouse = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
                    sendAuctionHouse = new PrintWriter(socket2.getOutputStream(), true);
                    sendAuctionHouse.println(tokens[0]);
                    String items = fromAuctionHouse.readLine();
                    String[] tok = items.split("-");
                    item1 = tok[0];
                    item2 = tok[1];
                    item3 = tok[2];
                    temItem.add(item1);
                    temItem.add(item2);
                    temItem.add(item3);
                    comboBox.setDisable(true);
                } catch (IOException e) {
                    flowPaneItem.getChildren().clear();
                    error = true;
                    alert("Reconnect");
                    try {
                        updateConnections();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (!error) {
                    try {
                        displayItems();
                        AgentListener listener = new AgentListener(socket2, this);
                        new Thread(listener).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            first = false;
        });
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Agent");
        primaryStage.show();
        // socket1.close();
    }

    public void updateConnections() throws IOException {
        auctionHouse.remove(indexOfCombo);
        ports.remove(indexOfCombo);
        comboBox.getItems().remove(indexOfCombo);
        flowPaneItem.getChildren().clear();
    }

    public void alert(String status) {
        Alert alert;
        if (status.equals("Success")) {
            alert = new Alert(Alert.AlertType.NONE, "Congratulations!! Your bid has been accepted.", ButtonType.OK);
        } else if (status.equals("Fail")) {
            alert = new Alert(Alert.AlertType.NONE, "Sorry!! Your bid is rejected", ButtonType.OK);

        } else if (status.equals("Reconnect")) {
            alert = new Alert(Alert.AlertType.NONE, "Sorry!! Auction House you are trying to connect is closed...", ButtonType.OK);
        } else if (status.equals("Invalid")) {
            alert = new Alert(Alert.AlertType.NONE, "Invalid Input! Please enter valid Input. ", ButtonType.OK);
        } else {
            alert = new Alert(Alert.AlertType.NONE, "Sorry!! Your current Auction House is closed." +
                    " Please try to connect to other available houses.", ButtonType.OK);
        }
        alert.showAndWait();
    }

    public void Timed(String item, int index, int userPrice) {
        endCon = false;
        String token[] = item.split(" ");
        IntegerProperty timeSeconds = AgentGUI.timeSeconds.get(index);
        Timeline timeline = AgentGUI.timelines.get(index);
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(STARTTIME);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME + 1),
                        new KeyValue(timeSeconds, 0)));
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendAuctionHouse.println("Over-" + token[1] + "-" + index + "-" + userPrice + "-" + item);
                endCon = true;
            }
        });
        timeline.playFromStart();
    }

    public VBox updateItems(String item) {
        VBox vBox = new VBox(10);
        String[] token = item.split(" ");

        HBox hBox1 = new HBox(10);
        HBox hBox2 = new HBox(10);
        HBox hBox3 = new HBox(10);

        Label timerLabel = new Label();
        int index = Integer.parseInt(token[4]);
        IntegerProperty timeSeconds = AgentGUI.timeSeconds.get(index);
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.RED);
        timerLabel.setFont(new Font(20));

        Label itemInfo = new Label("House Id: " + portNumber +"    Item Name: " + token[0] + "     Item ID: " + token[1]);
        itemInfo.setFont(Font.font(15));

        Label itemPrice = new Label("Base Price:$ " + token[2] + "     Current Price:$ " + token[3]);
        itemPrice.setFont(Font.font("Verdana", FontPosture.ITALIC, 15));

        Label bidAmount = new Label("new Bid:$       ");
        bidAmount.setFont(Font.font("Verdana", FontPosture.ITALIC, 15));

        TextField bidPrice = new TextField();
        Button bid = new Button("Bid");

        bid.setOnMousePressed(event -> {
            String userPrice = bidPrice.getText();
            boolean temp = false;
            for (int i = 0; i < userPrice.length(); i++) {
                if (userPrice.charAt(i) >= 48 && userPrice.charAt(i) <= 57) {
                    temp = true;
                } else {
                    temp = false;
                    break;
                }
            }
            if (temp) {
                sendAuctionHouse.println("Bid-" + userPrice + "-" + token[1] + "-" + index);
                myBid = true;
            } else {
                alert("Invalid");
            }
        });

        hBox1.getChildren().add(itemInfo);
        hBox2.getChildren().add(itemPrice);
        hBox3.getChildren().addAll(bidAmount, bidPrice, bid, timerLabel);
        vBox.getChildren().addAll(hBox1, hBox2, hBox3);
        return vBox;
    }

    public void displayItems() throws IOException {
        flowPaneItem.getChildren().add(updateItems(item1 + " 0"));
        flowPaneItem.getChildren().add(updateItems(item2 + " 1"));
        flowPaneItem.getChildren().add(updateItems(item3 + " 2"));

        flowPaneItem.setStyle("-fx-background-color: #A2D9CE;");
        flowPaneItem.setAlignment(Pos.CENTER_LEFT);
        flowPaneItem.setVgap(100);
        pane.setCenter(flowPaneItem);
    }

    public VBox wonItems() {
        VBox vBox = new VBox(10);
        if (wonItems.size() != 0) {
            for (int i = 0; i < wonItems.size(); i++) {
                String item = wonItems.get(i);
                String[] tok = item.split(" ");
                String itemInfo = "Name: " + tok[0] + " Base price: $" + tok[2] + " Your price: $" + tok[3];
                Label label = new Label(itemInfo);
                vBox.getChildren().add(i, label);
            }
        } else {
            Label label = new Label("You haven't won anything yet.");
            vBox.getChildren().add(label);
        }
        return vBox;
    }
}

