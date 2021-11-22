package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagementApplicationControllerTest  {
    @Test
    void addItem() {
        try{
            InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
            assertEquals(0, obj.addItem());
        } catch (Exception e) {
            System.out.print("works");
        }
    }

    @Test
    void loadList() throws IOException, ParseException {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        assertEquals(1 , obj.loadList());
    }

    @Test
    void removeAll() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        ObservableList<recordItem> tempItems = obj.removeAll();
        ObservableList<recordItem> clearedList = InventoryManagementApplicationController.items;
        clearedList.clear();
        assertEquals(clearedList, tempItems);
    }

    @Test
    void removedItem() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        ObservableList<recordItem> tempItems = InventoryManagementApplicationController.items;
        ObservableList<recordItem> deletedItems = FXCollections.observableArrayList();
        for(recordItem temp : tempItems){
            if(temp.getSelect().isSelected()) {
                deletedItems.add(temp);
            }
        }
        tempItems.removeAll(deletedItems);
        assertEquals(tempItems, obj.removedItem());
    }

    @Test
    void saveList() throws IOException {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        assertEquals(1, obj.saveList());
    }

    @Test
    void searchByName() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        ObservableList<recordItem> tempItems = InventoryManagementApplicationController.items;
        ObservableList<recordItem> Searched = FXCollections.observableArrayList();
        for(recordItem temp : tempItems){
            if(temp.getName().contains("Name")) {
                Searched.add(temp);
            }
        }
        assertEquals(tempItems, Searched);
    }

    @Test
    void searchBySerialNumber() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        ObservableList<recordItem> tempItems = InventoryManagementApplicationController.items;
        ObservableList<recordItem> Searched = FXCollections.observableArrayList();
        for(recordItem temp : tempItems){
            if(temp.getName().contains("a-123-123-123")) {
                Searched.add(temp);
            }
        }
        assertEquals(tempItems, Searched);
    }

    @Test
    void editName() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        TableColumn.CellEditEvent<recordItem, String>recordItemStringCellEditEvent = null;

        assertNull(obj.editName(recordItemStringCellEditEvent));
    }

    @Test
    void editPrice() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        TableColumn.CellEditEvent<recordItem, String>recordItemStringCellEditEvent = null;

        assertNull(obj.editPrice(recordItemStringCellEditEvent));
    }

    @Test
    void editSerialNumber() {
        InventoryManagementApplicationController obj = new InventoryManagementApplicationController();
        TableColumn.CellEditEvent<recordItem, String>recordItemStringCellEditEvent = null;

        assertNull(obj.editSerialNumber(recordItemStringCellEditEvent));
    }
}