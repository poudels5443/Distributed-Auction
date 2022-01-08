/*
Group: Saphal karki, Safal Poudel, Karan Aryal
Project: 5
CS 351
5/11/2021
It is a item class for the each items created by reading the list on auction house.
 */
public class Item {
    private final String itemName;
    private final String itemID;
    private final double basePrice;
    private double currentPrice;
    public Item(String itemID, String itemName, Double basePrice){
        this.itemName=itemName;
        this.itemID=itemID;
        this.basePrice=basePrice;
        this.currentPrice=basePrice;
    }
    public String getItemName(){return this.itemName;}
    public String getItemID(){return this.itemID;}
    public double getBasePrice(){return this.basePrice;}
    public void setCurrentPrice(Double amount){this.currentPrice=amount;}
    public double getCurrentPrice(){return this.currentPrice;}
}
