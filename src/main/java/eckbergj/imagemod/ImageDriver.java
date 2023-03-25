/*
 * CS1021 - 011
 * Winter 2021-2022
 * Lab 8: Image Manipulation
 * John Eckberg
 * 5/17/2022
 */
package eckbergj.imagemod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Driver class for the image manipulator application
 */
public class ImageDriver extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        //main window

        FXMLLoader loader = new FXMLLoader((Controller.class.getResource("fxml/Manipulator.fxml")));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Image GUI");
        stage.setScene(scene);

        stage.show();


        //manipulator window
        FXMLLoader secondaryLoader = new FXMLLoader(ConvolveController.class.getResource("fxml/Convolve.fxml"));
        Stage secondaryStage = new Stage();
        //Secondary Stage/Window
        secondaryStage.setScene(new Scene(secondaryLoader.load(), 320, 240));
        secondaryStage.hide();


        //Get Controllers
        ConvolveController secondaryController = secondaryLoader.getController();
        Controller primaryController = loader.getController();

        //external close closer
        secondaryStage.setOnCloseRequest(e -> primaryController.convolveButton());

        //Pass stage references to each of the controllers
        primaryController.setReferenceStage(secondaryStage);
        primaryController.setPrimaryStage(stage);
        secondaryController.setReferenceStage(stage);
        secondaryController.setSecondaryStage(secondaryStage);
        //pass through controller
        secondaryController.setReferenceController(primaryController);
    }

}
