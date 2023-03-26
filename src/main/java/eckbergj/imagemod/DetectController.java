package eckbergj.imagemod;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class DetectController {

    private Stage primaryStage;
    private Stage secondaryStage;
    private Controller referenceController;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ImageView imageViewTwo;

    @FXML
    private Label labels;


    protected void setReferenceStage(Stage stage){
        primaryStage = stage;
    }
    protected void setTertiaryStage(Stage stage){
        this.secondaryStage = stage;
    }
    protected void setReferenceController(Controller controller){
        referenceController = controller;
    }

    /**
     * sets imageview when button is pushed
     * @param image: the image from the main imageview
     */
    public void setImageView(Image image) {
        imageViewTwo.setImage(image);
    }

    @FXML
    /**
     * Runs detection operation using detector class
     */
    void detect(){
        try {
            Detector detector = new Detector();
            detector.predict().save(new FileOutputStream("boxed.png"), "png");
            //TODO sys message?
            imageViewTwo.setImage(ImageIO.read(Path.of("boxed.png")));
        } catch (ModelNotFoundException | MalformedModelException | IOException | TranslateException e) {
            //TODO real javafx error window
            e.printStackTrace();
        }

    }

    @FXML
    void progressBar(Progress p){
    }

    @FXML
    /**
     * Closes Detection using the detection button manager from the main controller
     */
    void close(){
        referenceController.detectButton();
    }
}
