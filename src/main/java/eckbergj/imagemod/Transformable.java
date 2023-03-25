package eckbergj.imagemod;

import javafx.scene.paint.Color;

/**
 *  The method, named apply(), must accept two arguments: the y location of the pixel and its color.
 *  The method must return the color for the pixel after the applying the transformation. You must specify implementations
 *  of the Transformable interface for the grayscale, red, redGray, and negative image transformations as lambda expressions.
 */
public interface Transformable {
    /**
     *
     * @return color of the pixel after a transform is applied
     */
    Color apply(int y, Color color);
}
