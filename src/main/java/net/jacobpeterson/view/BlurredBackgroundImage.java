package net.jacobpeterson.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlurredBackgroundImage extends ImageView implements ChangeListener<Bounds> {

    private MainView mainView;

    /**
     * Instantiates a new Blurred background image.
     *
     * @param mainView the main view
     */
    public BlurredBackgroundImage(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void changed(ObservableValue<? extends Bounds> observableBounds, Bounds oldBounds, Bounds newBounds) {
        if (this.getImage() == null) {
            return;
        }

        double imageWidth = this.getImage().getWidth();
        double imageHeight = this.getImage().getHeight();
        double thisAspectRatio = newBounds.getWidth() / newBounds.getHeight();
        double imageAspectRatio = imageWidth / imageHeight;
        if (imageAspectRatio < thisAspectRatio) { // Cut off tops of image
            imageHeight = (int) (newBounds.getWidth() / imageAspectRatio);
        } else { // Cut off sides of image
            imageWidth = (int) (newBounds.getHeight() * imageAspectRatio);
        }
        double imageX = (newBounds.getWidth() / 2) - (imageWidth / 2);
        double imageY = (newBounds.getHeight() / 2) - (imageHeight / 2);
        this.setViewport(new Rectangle2D(Math.abs(imageX), Math.abs(imageY), imageWidth, imageHeight));
    }

    /**
     * Sets the blurred image.
     *
     * @param image      the image
     * @param blurRadius the blur radius (null or -1 for no blur)
     */
    public void setBlurredImage(Image image, Integer blurRadius) {
        this.setImage(image);
        this.setCache(true);

        if (blurRadius > 0) {
            GaussianBlur gaussianBlur = new GaussianBlur(blurRadius);
            this.setEffect(gaussianBlur);
        }
    }
}
