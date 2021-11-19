/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Adam Farrow
 */
package baseline;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;


public class InventoryManagementApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        //locates the root as the path to the fxml
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("InventoryApplication.fxml")));
        //creates a new scene
        Scene scene = new Scene(root);
        //sets the title to do List
        stage.setTitle("Inventory Management");
        //sets the scene as scene(root)
        stage.setScene(scene);
        //shows the stage
        stage.show();
    }

    public static void main(String[] args) {
        // create a To DO List object and calls its start method
        launch(args);
    }
}
