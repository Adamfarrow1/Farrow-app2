/*
 *  UCF COP3330 Fall 2021 Application Assignment 2 Solution
 *  Copyright 2021 Adam Farrow
 */
package baseline;

import javafx.scene.control.CheckBox;

public class RecordItems {
    private String serialNumber;
    private String name;
    private String price;
    private final CheckBox select;
    //defines instance variables
    public RecordItems(String serialNumber, String name , String price) {
        // sets instance variables to the method arguments
        this.serialNumber = serialNumber;
        this.name = name;
        this.price = price;
        this.select = new CheckBox();
    }

    public CheckBox getSelect(){
        //returns the check
        return select;
    }

    public String getPrice(){
        //returns the price
        return price;
    }
    public void setPrice(String price) {
        //sets the price
        this.price = price;
    }

    public void setSerialNumber(String serialNumber){
        //sets the serial number
        this.serialNumber = serialNumber;
    }
    public void setName(String name){
        //sets the name
        this.name = name;
    }

    public String getSerialNumber() {
        //returns the serial number
        return serialNumber;
    }
    public String getName(){
        //returns the name
        return name;
    }
}

