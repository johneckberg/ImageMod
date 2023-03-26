/*
 * CS1021 - 011
 * Winter 2021-2022
 * Lab 8: Image Manipulation
 * John Eckberg
 * 5/17/2022
 */
package eckbergj.imagemod;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Controller class
 */
public class Controller {

    FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG", "*.png");
    FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPEG", "*.jpeg");
    FileChooser.ExtensionFilter msoe = new FileChooser.ExtensionFilter("MSOE", "*.msoe");
    FileChooser.ExtensionFilter bmsoe = new FileChooser.ExtensionFilter("BMSOE", "*.bmsoe");

    FileChooser fileChooser = new FileChooser();

    @FXML
    private Button filter;

    @FXML
    private Button detect;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane window;

    DetectController detectController;

    private Stage primaryStage;
    private Stage secondaryStage;
    private Stage tertiaryStage;

    private Image preOp;
    private Image postOp;

    @FXML
    /**
     * loads user specified image
     */
    private void load() {

        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(png, msoe, jpg, bmsoe);

        // I could just find imageview's scene?
        File file = fileChooser.showOpenDialog(window.getScene().getWindow());

        //tries to load image
        if(file != null) {
            Path filePath = file.toPath();
            System.out.println(filePath);


            try {
                System.out.println("Trying");
                Image loading = ImageIO.read(filePath);
                System.out.println("Complete");
                if (loading != null) {
                    preOp = loading;
                    postOp = preOp;
                    imageView.setImage(preOp);
                } else {
                    throw new IllegalArgumentException("Image not Read");
                }

            } catch (IOException | IllegalArgumentException fileFailure) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Format Notice");
                alert.setHeaderText("File Type Not Supported");
                alert.setContentText("The file type you tried to load is either incorrect or " +
                        "unsupported");

                alert.showAndWait();
            }
        }
    }


    @FXML
    /**
     * reloads image to orginal state
     */
    private void reLoad(){
        imageView.setImage(preOp);
        postOp = preOp;
        /*
        reset image selected in imageviewer to preOp
         */
    }

    @FXML
    /**
     * Saves the Image
     */
    private void save(){

        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(png, jpg, msoe, bmsoe);

        File dest = fileChooser.showSaveDialog(window.getScene().getWindow());

        if(imageView.getImage() != null && dest != null) {
            Path filePath = dest.toPath();
            try {
                ImageIO.write(imageView.getImage(), filePath);

                //File saved alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("I/O Notice");
                alert.setHeaderText("File  Saved");
                alert.setContentText("The header content" +
                        " in the specified file can be read correctly");

                alert.showAndWait();
            } catch (IOException | IllegalArgumentException fileFailure) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Format Notice");
                alert.setHeaderText("File Type Not Supported");
                alert.setContentText("The file type you specified is either incorrect or " +
                        "unsupported");

                alert.showAndWait();
            }
        }

    }

    protected void setSecondaryStage(Stage stage){
        this.secondaryStage = stage;
    }

    protected void setTertiaryStage(Stage stage){
        this.tertiaryStage = stage;
    }

    protected void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    protected void setRefrenceController(DetectController controller){
        this.detectController = controller;
    }

    @FXML
    private void grayScale(){

        if (imageView.getImage() != null) {
            postOp = transformImage(preOp, ImageIO.greyScale);
            imageView.setImage(postOp);
        }
    }

    @FXML
    private void negative(){
        if(imageView.getImage() != null) {
            postOp = transformImage(preOp, ImageIO.invert);
            imageView.setImage(postOp);
        }
    }

    @FXML
    private void red(){
        if(imageView.getImage() != null) {
            postOp = transformImage(preOp, ImageIO.red);
            imageView.setImage(postOp);
        }
    }

    @FXML
    private void redGray(){
        if(imageView.getImage() != null) {
            postOp = transformImage(preOp, ImageIO.redGray);
            imageView.setImage(postOp);
        }
    }


    /*
    implement scroll to brighten & scroll down to dim using ColorAdjust
     */


    private static Image transformImage(Image image, Transformable transform) {

        //Reading the pixels
        PixelReader reader = image.getPixelReader();
        //Writing the read pixels
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage wImage = new WritableImage(width, height);
        PixelWriter writer = wImage.getPixelWriter();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {

                //getting the color of the og pixel
                Color color = reader.getColor(x, y);
                // changing the writable image pixel
                writer.setColor(x, y, transform.apply(y, color));
            }
        }
        return wImage;
    }


    /**
     *Gets pre operated on Image
     */
    public Image getImageview(){
        return imageView.getImage();
    }

    /**
     * sets post op image after operation
     */
    public void setPostOp(Image image){
        postOp = image;
    }

    /**
     * sets the application Imageview
     */
    public void setImageView(Image image){
        imageView.setImage(image);
    }

    /**
     *Opens and closes the convolution window
     */
    @FXML
    public void convolveButton(){
        stageManager(secondaryStage, filter, "Convolve");
    }

    /**
     * Opens and closes detection window
     */
    @FXML
    public void detectButton(){
        stageManager(tertiaryStage, detect, "Detect");
        detectController.setImageView(imageView.getImage()); // as to not pass a shallow copy?
    }
    /**
     * Helper method for managing windows
     */
    private void stageManager(Stage stage, Button button, String name) {
        if(imageView.getImage() != null) {
            if (stage.isShowing()) {
                stage.hide();
                button.setText(name);
            } else if (!stage.isShowing()) {
                stage.show();
                button.setText("Close");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Image");
            alert.setContentText("No operation can be applied as no image has been loaded ");

            alert.showAndWait();
        }
    }

}
