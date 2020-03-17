package net.jacobpeterson.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

public class BlurredBackgroundImage extends Pane implements ChangeListener<Bounds> {

    private final MainView mainView;
    private Canvas canvas;
    private Image blurredImage;

    /**
     * Instantiates a new Blurred background image.
     *
     * @param mainView the main view
     */
    public BlurredBackgroundImage(MainView mainView) {
        this.mainView = mainView;

        this.setupPane();
        this.setupCanvas();
    }

    /**
     * Sets up this pane.
     */
    private void setupPane() {
        this.setBackground(Background.EMPTY);

        this.layoutBoundsProperty().addListener(this);
    }

    /**
     * Sets up the canvas.
     */
    private void setupCanvas() {
        canvas = new Canvas();

        this.getChildren().add(canvas);
    }

    @Override
    public void changed(ObservableValue<? extends Bounds> observableBounds, Bounds oldBounds, Bounds newBounds) {
        canvas.setWidth(newBounds.getWidth());
        canvas.setHeight(newBounds.getHeight());

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        if (blurredImage == null || blurredImage.isError()) {
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            return;
        }

        // The following is basically applying the "background-size: cover" CSS equivalent for this canvas
        double thisAspectRatio = newBounds.getWidth() / newBounds.getHeight();
        double imageAspectRatio = blurredImage.getWidth() / blurredImage.getHeight();
        double destImageHeight = newBounds.getHeight();
        double destImageWidth = newBounds.getWidth();
        if (imageAspectRatio < thisAspectRatio) { // Cut off tops of image
            destImageHeight = newBounds.getWidth() / imageAspectRatio;
        } else { // Cut off sides of image
            destImageWidth = newBounds.getHeight() * imageAspectRatio;
        }
        double imageX = (newBounds.getWidth() / 2) - (destImageWidth / 2);
        double imageY = (newBounds.getHeight() / 2) - (destImageHeight / 2);

        graphicsContext.drawImage(blurredImage, 0, 0, blurredImage.getWidth(), blurredImage.getHeight(),
                imageX, imageY, destImageWidth, destImageHeight);
    }

    /**
     * Sets the blurred image (this is a blocking call)
     *
     * @param image      the image
     * @param blurRadius the blur radius (null or -1 for no blur)
     */
    public void setBlurredImage(Image image, Integer blurRadius) {
        if (image == null || image.isError()) {
            blurredImage = null;
            return;
        }

        if (blurRadius < 1) {
            blurredImage = image;
        } else {
            ImageView imageView = new ImageView(image);
            imageView.setEffect(new GaussianBlur(blurRadius));
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(mainView.getScene().getFill());
            snapshotParameters.setViewport(new Rectangle2D(blurRadius, blurRadius,
                    image.getWidth() - blurRadius * 2, image.getHeight() - blurRadius * 2));
            blurredImage = imageView.snapshot(snapshotParameters, null);
        }
    }
}
