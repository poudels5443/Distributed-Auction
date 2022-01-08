Author : Saphal Karki, Safal Poudel, Karan Aryal

Distributed Auction:

Class : BankAccount
This class creates bank account for both auction house and agent.
Method:
getID(): returns the account Id for every account created
getAccountNumber(): returns account number of an account
setBalance(): sets the balance of each account
hold(): holds balance for a bid that an agent has made for an item
removeHold(): removes the hold after another agent has outbidden 
deposit(): deposits the money to the bank
withDraw(): withdraws the money from the bank
getBalance(): gets the balance from an account

Class: Item
This class reads the list of item from item file
Method:
getItemName(): returns the name of the item
getItemID(): returns the id of the item
getBasePrice(): returns the base price of the item
setCurrentPrice(): sets the current price of the item
getCurrentPrice(): gets the current price of the item

Class AuctionHouse
This class creates an auction house, handles multiple auction house and 
does all the function required by an auction house.
Method:
readItem(): reads the list of item from item text and stores it in an arraylist
getItem(): gets three the item from the list for each auction house.
createAgentName(): method which creates a name for each agent connected to the auction house
generateRandom(): generates random number
main(): After reading items this method generates port number for the bank. It dynamically 
        creates a server and port for each auction house. Each server then has its own thread which handles multiple clients
findItem(): method which finds item on the item list.
updateList(): updates the item list, after the item gets sold
displayItem(): displays the item

Class Bank:
This class creates a bank which gets account for auction house and agent. It transfers the money, holds and removes
the hold of money for any bidding that is going on.
Method:
createAccount: Creates account of agent or auction house
getAccount: gets the account of agent or auction house
createAuctionHouse: creates a list of auction house names
createAgentHouse: creates a list of agent names
availablePORTS(): method that returns the available ports
transfer(): method which transfers the money from bank to agent
generateAccountNumber(): method which generates random account number
putHold(): method that keeps hold on a certain account
removeHold(): method which removes the hold of a certain account
main(): This method creates server and accepts connections. It then identifies
    if the connection is from auction house or agent, then it creates respective account for
    each of the account. Then it handles a multiple agent and auction house and creates respective thread

Class MultipleAgentHandler:
This class handles multiple agent on their respective thread
Method:
cast(): This method cast message to every connection
unHoldAll(): This method removes hold of the account once its won
disconnect(): This method disconnect and removes the agent 
makeBid(): This method makes bid for each item
highestBid(): This method returns the highest bid of the item
run(): This method runs on its own thread where the winner, reject and outbid is
    determined

Class MultipleClientHandler:
This class handles multiple client on their respective thread
Method:
disconnected(): This method updates the list after the disconnection of an auctionhouse
getName(): returns the name
run(): THis method runs on its own thread

Class AgentListener:
This class runs on its thread and listen to the message from auction house
Method:
run(): This method runs on its own thread, prints success when an auction house is connected.
    gets and sets the balance, and acts accordingly when the item is won, reject or outbidden.

Class AgentGUI:
This class runs the gui version of the program and an agent interacts with it.
Method:
updateConnection: Updates the connection of auction houses on the gui.
alert(): Shows the alert box it the bid is accepted or rejected
Timed(): Method to the run timer on the gui
updateItems(): Method that updates the list of items won in the label in GUI
displayItems(): Method that displays item 
wonItems(): Method that display the won item 
start(): Method that sets the scene and shows the items on the gui 
main(): runs the class

Bugs and error:
Unsuccessfull to connect on different machines
Random bugs while getting the item on te auction house



