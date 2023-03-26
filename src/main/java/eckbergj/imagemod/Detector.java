package eckbergj.imagemod;

import ai.djl.MalformedModelException;
import ai.djl.modality.cv.*;
import ai.djl.modality.cv.output.*;
import ai.djl.repository.zoo.*;
import ai.djl.training.util.*;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;

import java.io.IOException;

public class Detector {
    private ZooModel<Image, DetectedObjects> model;
    private Image image;
    private Criteria<Image, DetectedObjects> criteria;

    public Detector() throws ModelNotFoundException, MalformedModelException, IOException {
        loadImage();
        //var??
        criteria = Criteria.builder()
                .setTypes(Image.class, DetectedObjects.class)
                .optArtifactId("ssd")
                .optProgress(new ProgressBar())
                .build();
        model = criteria.loadModel();
    }

    public Progress getProgressBar(){
        return criteria.getProgress();
    }

    //This might not be needed. loads last image I'ed assuming it's in the imageview. this is not the case if a transformation is applied
    private void loadImage() throws IOException {
        image = ImageFactory.getInstance().fromFile(ImageIO.getLastRead());

    }


    public javafx.scene.image.Image predict() throws TranslateException, IOException {
        DetectedObjects objects = model.newPredictor().predict(image);
        image.drawBoundingBoxes(objects);
        image.getWrappedImage();

        return (javafx.scene.image.Image) image;
    }

}
