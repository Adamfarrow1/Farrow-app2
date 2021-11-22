/*
 *  UCF COP3330 Fall 2021 Application Assignment 2 Solution
 *  Copyright 2021 Adam Farrow
 */
package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class InventoryManagementApplicationController {
    //define the fxml variables used
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
    private TextField loadName;

    @FXML
    private TextField loadPath;

    @FXML
    private TextField saveName;

    @FXML
    private TextField savePath;

    @FXML
    private TextField searchName;

    @FXML
    private TextField searchSerialNumber;

    @FXML
    private TableView<recordItem> table;

    @FXML
    private TableColumn<recordItem, String> tableName;

    @FXML
    private TableColumn<recordItem, String> tablePrice;

    @FXML
    private TableColumn<?, ?> tableSelect;

    @FXML
    private TableColumn<recordItem, String> tableSerialNumber;

    @FXML
    private CheckBox tsvCheckBox;

    @FXML
    private Text serialNumberText;

    @FXML
    private Text nameText;

    @FXML
    private Text priceText;

    @FXML
    private Text serialNumberEditText;

    @FXML
    private Text nameEditText;

    @FXML
    private Text priceEditText;

    //define observable list of items
    static ObservableList<recordItem> items = FXCollections.observableArrayList();
    @FXML
     int addItem() {
        int flag = 0;
        if(table == null)
            return 0;
        //flag variable to check if the information inputted if valid
        //checks to see if the serial number is a duplicate if so give error message
        for (recordItem item : items) {
            if (addSerialNumber.getText().equals(item.getSerialNumber())) {
                flag = 1;
                serialNumberText.setText("Duplicate serial number");
            }
        }
        //checks to see if the serial number is in the right format
        if(!addSerialNumber.getText().matches("[a-zA-Z]-[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]-[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]-[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]")) {
            flag = 1;
            serialNumberText.setText("Invalid. A-XXX-XXX-XXX required");
        }
        //checks to see if the name is the valid length and isn't a number
        if(addName.getText().length() < 2 || addName.getText().length() > 256  || !addName.getText().matches("[a-zA-Z]*")) {
            nameText.setText("Input 2-256 characters");
            flag = 1;
        }
        else{
            nameText.setText("");
        }
        //checks to see if the price is inputted in the correct format and that it is greater than or equal to 0
        if(!addPrice.getText().matches("(\\d*.\\d\\d)") || Double.parseDouble(addPrice.getText()) < 0){
            priceText.setText("Enter valid price in x.xx format");
            flag = 1;
        }
        else {
            nameText.setText("");
        }
        //if there was no flag then add the item to the list and remove all the error messages
        if(flag == 0) {
            String p = "$" + addPrice.getText();
            items.add(new recordItem(addSerialNumber.getText(), addName.getText(), p));
            table.setItems(items);
            table.getColumns().get(0).setVisible(false);
            table.getColumns().get(0).setVisible(true);
            nameText.setText("");
            serialNumberText.setText("");
            priceText.setText("");
            addName.clear();
            addSerialNumber.clear();
            addPrice.clear();
        }
        return 1;
    }

    @FXML
    int loadList() throws IOException, ParseException{
        if(table == null)
            return 1;
        //if .txt is found in the name then call the loadTSV function
        if(loadName.getText().contains(".txt"))
            loadTSV();
        //html is found within the name call load html function
        if(loadName.getText().contains(".html"))
            loadHtml();
        //if .json is found inside the name then call the load json function
        if(loadName.getText().contains(".json"))
            loadJson();
        return 0;
    }

    void loadJson() throws IOException, ParseException {
        //clears the items inside the list and refreshes the table
        items.clear();
        table.setItems(items);
        //parse the data of the file to a JSONObject
        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(loadPath.getText() + "\\" + loadName.getText()));
        //creates a JSONArray of items using the parsed object
        JSONArray address = (JSONArray) obj.get("items");
        //iterates through the array and adds each item to the items observable list
        for (Object subject : address) {
            //creates each index of array of the JSONArray into an object and adds it to the list
            JSONObject json = (JSONObject) subject;
            items.add(new recordItem(json.get("Serial Number").toString(), json.get("Name").toString(), json.get("Price").toString()));
        }

    }

    void loadTSV() throws IOException {
        items.clear();
        table.setItems(items);
        //tries to open the files with buffered reader
        try(BufferedReader br = new BufferedReader(new FileReader(loadPath.getText() + "\\" + loadName.getText()))) {
            String currentLine;
            String[] data;
            //reads current line and checks if its null
            currentLine = br.readLine();
            if(currentLine != null) {
                //while data isn't null read the current line and parse it by tab and then add each index of the array to items
                while ((currentLine = br.readLine()) != null) {
                    data = currentLine.split("\t");
                    items.add(new recordItem(data[0], data[1], data[2]));
                }
            }
            table.setItems(items);
            table.getColumns().get(0).setVisible(false);
            table.getColumns().get(0).setVisible(true);
        }
    }

    void loadHtml() throws IOException {
        items.clear();
        table.setItems(items);
        String pathDelimiter = "\\";
        //creates a path to the file needed
        File path = new File(loadPath.getText() + pathDelimiter + loadName.getText());
        //parses the data to a Document variable named doc
        Document doc = Jsoup.parse(path, null);
        //Finds the first table within the document
        Element tb = doc.select("table").first();
        assert tb != null;
        //adds all the elements within the table to tdTags
        Elements tdTags = tb.select("td");
        for(int i = 0; i < tdTags.size(); i = i + 3) {
            //iterates through all the table tags with td and adds them to the items list
            items.add(new recordItem(tdTags.get(i).text(), tdTags.get(i+1).text(), tdTags.get(i+2).text()));
        }
        table.setItems(items);
        table.getColumns().get(0).setVisible(false);
        table.getColumns().get(0).setVisible(true);
    }

    @FXML
    ObservableList<recordItem> removeAll() {
        //deletes all the items inside the list
        items.clear();
        //refreshes the table
        if(table != null) {
            table.getColumns().get(0).setVisible(false);
            table.getColumns().get(0).setVisible(true);
        }
        return items;
    }

    @FXML
    ObservableList<recordItem> removedItem() {
        //defines observable list called deleted items
        ObservableList<recordItem> deletedItems = FXCollections.observableArrayList();
        for(recordItem temp : items){
            //loops through the items list and if the item checkbox is selected add it to the deleted items list
            if(temp.getSelect().isSelected()) {
                deletedItems.add(temp);
            }
        }
        //remove the deleted items from the items list
        items.removeAll(deletedItems);
        return items;
    }

    @FXML
    int saveList() throws IOException{
        int flag = 0;
        if(table == null)
            return 1;
        //if the html checkbox is chosen then call saveHTML method
        if(htmlCheckBox.isSelected()) {
            saveHTML();
            flag = 1;
        }
        //if the tsv check box is selected and flag is 0 call saveTSV
        if(tsvCheckBox.isSelected() && flag == 0){
            saveTSV();
            flag = 1;
        }
        //if jsonCheckBox is selected and flag = 0 call saveJson
        if(jsonCheckBox.isSelected() && flag == 0){
            saveJson();
        }
        return 0;
    }

    void saveJson() throws IOException {
        String path = savePath.getText();
        String name = "\\" + saveName.getText() + ".json";
        //creates file with the given path and name
        Files.createFile(Path.of(path + name));
        //tries to open fileWriter with the path
        try(FileWriter fw = new FileWriter(path + name)){
            int i = 0;
            //prints the beginning of the json file
            fw.write("""
                    {
                      "items" : [
                    """);
            //prints the items in the list to the json file
            for(recordItem item : items){
                fw.write("\t\t{\"Serial Number\": \""+ item.getSerialNumber() +"\", \"Name\": \""+  item.getName()  +"\", \"Price\": \""+ item.getPrice() +"\" }");
                if(i != items.size() - 1)
                    fw.write(",");
                fw.write("\n");
                i++;
            }
            //ends the json file
            fw.write("\t]\n" +
                    "}");
            //clears the saveName and savePath text box
            saveName.clear();
            savePath.clear();
        }
    }

    void saveTSV() throws IOException {
        String path = savePath.getText();
        String name = "\\" + saveName.getText() + ".txt";
        //creates file in the path given
        Files.createFile(Path.of(path + name));
        //tries to open file writer to the created file path
        try(FileWriter fw = new FileWriter(path + name)){
            //prints the column titles of the table
            fw.write("Serial Number\tName\tPrice\n");
            for (recordItem item : items) {
                //prints each item with tables separating the values
                fw.write(item.getSerialNumber() + "\t" + item.getName() + "\t" + item.getPrice() + "\n");
            }
            saveName.clear();
            savePath.clear();
        }
    }

    void saveHTML() throws IOException {
        String path = savePath.getText();
        String name = "\\" + saveName.getText() + ".html";
        //creates file in the given path
        Files.createFile(Path.of(path + name));
        try(FileWriter fw = new FileWriter(path + name)){
            //prints the top part of the html file including a table at the bottom
            fw.write("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <style>
                    table, th, td {
                      border: 1px solid black;
                    }
                    </style>
                    </head>
                    <body>
                    
                    <h1>Inventory Management</h1>
                 
                    <table>
                      <tr>
                        <th>Serial Number</th>
                        <th>Name</th>
                        <th>Price</th>
                   </tr>
                    """);
            for (recordItem item : items) {
                //prints all the items inside of items to the table
                fw.write("<tr> \n<td>" + item.getSerialNumber() + "</td>\n");
                fw.write("<td>" + item.getName() + "</td>\n");
                fw.write("<td>" + item.getPrice() + "</td>\n</tr>\n");
            }
            //closes the html file
            fw.write("""
                    </table>

                    </body>
                    </html>""");
            saveName.clear();
            savePath.clear();
        }

    }

    @FXML
    ObservableList<recordItem> searchByName() {
        ObservableList<recordItem> searchedItems = FXCollections.observableArrayList();
        //creates items searched list
        for (recordItem item : items) {
            //if the items searched is found then add it to the searched items list
            if (item.getName().contains(searchName.getText()))
                searchedItems.add(item);
        }
        //show the searched items list
        if(table != null)
        table.setItems(searchedItems);
        return searchedItems;
    }

    @FXML
    void searchBySerialNumber() {
        ObservableList<recordItem> searchedItems = FXCollections.observableArrayList();
        //creates searched items list
        for (recordItem item : items) {
            //if the item contains the search then add it to the searched item list
            if (item.getSerialNumber().contains(searchSerialNumber.getText()))
                searchedItems.add(item);
        }
        //show the list created
        table.setItems(searchedItems);
    }

    @FXML
    ObservableList<recordItem> editName(TableColumn.CellEditEvent<recordItem, String>recordItemStringCellEditEvent) {
        int flag = 0;
        if(table == null)
            return null;
        //if the name is invalid show error message and set flag equal to 1
        if(recordItemStringCellEditEvent.getNewValue().length() < 2 || recordItemStringCellEditEvent.getNewValue().length() > 256 ||  !recordItemStringCellEditEvent.getNewValue().matches("[a-zA-Z]*")) {
            flag = 1;
            nameEditText.setText("Input 2-256 characters");
        }
        //if flag is 0 then set the name of the set the name to the edited value
        if(flag == 0) {
            recordItem item = table.getSelectionModel().getSelectedItem();
            item.setName(recordItemStringCellEditEvent.getNewValue());
            nameEditText.setText("");
        }
        else{
            table.getColumns().get(0).setVisible(false);
            table.getColumns().get(0).setVisible(true);
        }
        return items;
    }

    @FXML
    ObservableList<recordItem> editPrice(TableColumn.CellEditEvent<recordItem, String>recordItemStringCellEditEvent) {
        int flag = 0;
        if(table == null)
            return null;
        //check to see if the price is in the right format if it isn't set flag to 1
        if(!recordItemStringCellEditEvent.getNewValue().matches("(\\d*.\\d\\d)") || Double.parseDouble(recordItemStringCellEditEvent.getNewValue()) <= 0){
            flag = 1;
            priceEditText.setText("Enter valid price in x.xx format");
        }
        //if flag is 0 then set the price to the new value
        if(flag == 0){
            priceEditText.setText("");
            String price = "$" + recordItemStringCellEditEvent.getNewValue();
            recordItem item = table.getSelectionModel().getSelectedItem();
            item.setPrice(price);
        }
        //refresh the table contents
        table.getColumns().get(0).setVisible(false);
        table.getColumns().get(0).setVisible(true);
        return items;
    }

    @FXML
    ObservableList<recordItem> editSerialNumber(TableColumn.CellEditEvent<recordItem, String>recordItemStringCellEditEvent) {
        int flag = 0;
        if(table == null)
            return null;
        //checks to see if the value is in the correct format if it isn't set flag to 1
        if(!recordItemStringCellEditEvent.getNewValue().matches("[a-zA-Z]-[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]-[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]-[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]")){
            serialNumberEditText.setText("Invalid. A-XXX-XXX-XXX required");
            flag = 1;
        }
        //checks to see if the serial number isn't already inside the list if it is set flag to 1
        for (recordItem item : items) {
            if (recordItemStringCellEditEvent.getNewValue().equals(item.getSerialNumber())) {
                flag = 1;
                serialNumberEditText.setText("Duplicate serial number");
            }
        }
        //if flag is 0 then set the new serial number to the selected items serial number
        if(flag == 0) {
            recordItem item = table.getSelectionModel().getSelectedItem();
            item.setSerialNumber(recordItemStringCellEditEvent.getNewValue());
            serialNumberEditText.setText("");
        }
        else {
            table.getColumns().get(0).setVisible(false);
            table.getColumns().get(0).setVisible(true);
        }
        return items;
    }

    public void initialize(){
        //initialize the columns to be added
        tableSerialNumber.setCellValueFactory(new PropertyValueFactory<>("SerialNumber"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        tableSelect.setCellValueFactory(new PropertyValueFactory<>("Select"));

        //initialize the cells to be edited
        tableSerialNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        tableName.setCellFactory(TextFieldTableCell.forTableColumn());
        tablePrice.setCellFactory(TextFieldTableCell.forTableColumn());

    }

}
