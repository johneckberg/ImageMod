/*
 * CS1021 - 011
 * Winter 2021-2022
 * Lab 8: Image Manipulation
 * John Eckberg
 * 5/17/2022
 */
package eckbergj.imagemod;

import edu.msoe.cs1021.ImageUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for the Convolution manager window
 */
public class ConvolveController {

    private Stage primaryStage;
    private Stage secondaryStage;
    private Controller referenceController;

    protected void setReferenceStage(Stage stage){
        primaryStage = stage;
    }

    protected void setSecondaryStage(Stage stage){
        this.secondaryStage = stage;
    }
    protected void setReferenceController(Controller controller){
        referenceController = controller;
    }
    @FXML
    private TextField oneone;

    @FXML
    private TextField onethree;

    @FXML
    private TextField onetwo;

    @FXML
    private TextField threeOne;

    @FXML
    private TextField threeThree;

    @FXML
    private TextField threeTwo;

    @FXML
    private TextField twoOne;

    @FXML
    private TextField twoThree;

    @FXML
    private TextField twoTwo;

    private double[] kernelify() {
        //create kernel array
        List<TextField> kernel = Arrays.asList(oneone, onetwo, onethree,
                twoOne, twoTwo, twoThree, threeOne, threeTwo, threeThree);
        double[] primitive = new double[9];

        //if nullcheck = false and && instream sum == 1, do this else divide
        if(nullCheck(kernel)){
            //convert to array
            for(int i = 0; i < 9; i++){
                primitive[i] = Double.parseDouble(kernel.get(i).getText());
            }
            if(Arrays.stream(primitive).sum() > 0){
                if (Arrays.stream(primitive).sum() == 1){
                    return primitive;
                }
                return Arrays.stream(primitive).map(d -> d/(Arrays.stream(primitive).sum())).toArray();
            }
        }


        return null;
    }
    private boolean nullCheck(List<TextField> kernel){
        for(TextField t : kernel){
            if(t.getText().isEmpty()){
                return false;
            }
        }
        return true;
    }

    @FXML
    /**
     * Applies Convolution to Image
     */
    void ApplyConvolve() {

        if(kernelify() != null) {
            Image convolve = ImageUtil.convolve(referenceController.getImageview(), kernelify());
            referenceController.setPostOp(convolve);
            referenceController.setImageView(convolve);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kernel Notice");
            alert.setContentText("The kernel must contain valid values");

            alert.showAndWait();
        }
    }

    @FXML
    /**
     * Applies blur transform to Image
     */
    void Blur() {
        oneone.setText("0"); onetwo.setText("1"); onethree.setText("0");
        twoOne.setText("1"); twoTwo.setText("5"); twoThree.setText("1");
        threeOne.setText("0"); threeTwo.setText("1"); threeThree.setText("0");
        ApplyConvolve();
    }

    @FXML
    /**
     * Applies sharpen transformation to Image
     */
    void Sharpen() {
        oneone.setText("0"); onetwo.setText("-1"); onethree.setText("0");
        twoOne.setText("-1"); twoTwo.setText("5"); twoThree.setText("-1");
        threeOne.setText("0"); threeTwo.setText("-1"); threeThree.setText("0");
        ApplyConvolve();
    }

    @FXML
    /**
     * Closes Convolution Image
     */
    void close(){
        referenceController.convolveButton();
    }


}
