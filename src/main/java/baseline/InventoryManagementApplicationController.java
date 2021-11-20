/*
 *  UCF COP3330 Fall 2021 Application Assignment 2 Solution
 *  Copyright 2021 Adam Farrow
 */
package baseline;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class InventoryManagementApplicationController {
    //define the fxml variables used
    @FXML
    private Button addButton;

    @FXML
    private TextField addName;

    @FXML
    private TextField addPrice;

    @FXML
    private TextField addSerialNumber;

    @FXML
    private CheckBox htmlCheckBox;

    @FXML
    private CheckBox jsonCheckBox;

    @FXML
    private Button loadButton;

    @FXML
    private TextField loadName;

    @FXML
    private TextField loadPath;

    @FXML
    private Button removeItem;

    @FXML
    private Button saveButton;

    @FXML
    private TextField saveName;

    @FXML
    private TextField savePath;

    @FXML
    private TextField searchName;

    @FXML
    private Button searchNameButton;

    @FXML
    private TextField searchSerialNumber;

    @FXML
    private Button searchSerialNumberButton;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> tableName;

    @FXML
    private TableColumn<?, ?> tablePrice;

    @FXML
    private TableColumn<?, ?> tableSelect;

    @FXML
    private TableColumn<?, ?> tableSerialNumber;

    @FXML
    private CheckBox tsvCheckBox;
    //define observable list of items
    @FXML
    void addItem(MouseEvent event) {
        //gets the data inputted into the text fields
        //makes sure they are valid
        //adds them to an observable list
    }

    @FXML
    void loadList(MouseEvent event) {
        //gets the name of the file and the path
        // and parses the data and adds it the table
    }

    @FXML
    void removeAll(MouseEvent event) {
        //deletes all the items inside the list
        //refreshes the table
    }

    @FXML
    void removedItem(MouseEvent event) {
        //deletes the current selected item from the list
        //refreshes the list
    }

    @FXML
    void saveList(MouseEvent event) {
        //checks to see which check box is chosen
        //parses data accordingly
        //saves it to the correct path
    }

    @FXML
    void searchByName(MouseEvent event) {
        //compares word searched for with the words in the list
        // shows only the words in the list
    }

    @FXML
    void searchBySerialNumber(MouseEvent event) {
        //compares the serial number searched for with a the items in the list
        //adds items with the serial number inside to a new list
        //shows the new list
    }

}
