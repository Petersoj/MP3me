package net.jacobpeterson.view.songlist;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class AddButton extends StackPane implements ChangeListener<Bounds> {

    private Circle clip;

    private Background background;
    private Background hoverBackground;

    private double plusSignSizePercentage;
    private double plusSignMarginPercentage;
    private Rectangle plusSignVertical;
    private Rectangle plusSignHorizontal;

    /**
     * Instantiates a new Add button.
     */
    public AddButton() {
        clip = new Circle();

        background = new Background(new BackgroundFill(Color.grayRgb(0, 0.2), null, null));
        hoverBackground = new Background(new BackgroundFill(Color.grayRgb(0, 0.4), null, null));

        plusSignSizePercentage = 5;
        plusSignMarginPercentage = 20;
        plusSignVertical = new Rectangle();
        plusSignHorizontal = new Rectangle();
        plusSignVertical.setFill(Color.WHITE);
        plusSignHorizontal.setFill(Color.WHITE);

        this.setupPane();
    }

    /**
     * Sets up this pane.
     */
    private void setupPane() {
        this.setBackground(background);
        this.setOnMouseEntered(event -> this.setBackground(hoverBackground));
        this.setOnMouseExited(event -> this.setBackground(background));

        this.layoutBoundsProperty().addListener(this);

        this.getChildren().add(plusSignVertical);
        this.getChildren().add(plusSignHorizontal);
    }

    @Override
    public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds newBounds) {
        double shortSide = Math.min(newBounds.getWidth(), newBounds.getHeight());

        clip.setCenterX(newBounds.getCenterX());
        clip.setCenterY(newBounds.getCenterY());
        clip.setRadius(shortSide / 2);
        this.setClip(clip);

        double plusSignMargin = plusSignMarginPercentage / 100 * shortSide;
        double plusSignSize = plusSignSizePercentage / 100 * shortSide;

        plusSignVertical.setWidth(plusSignSize);
        plusSignVertical.setHeight(shortSide - plusSignMargin * 2);
        plusSignVertical.setX(newBounds.getCenterX() - plusSignVertical.getWidth() / 2);
        plusSignVertical.setY(clip.getCenterY() - plusSignVertical.getHeight() / 2);

        plusSignHorizontal.setWidth(shortSide - plusSignMargin * 2);
        plusSignHorizontal.setHeight(plusSignSize);
        plusSignHorizontal.setX(newBounds.getCenterX() - plusSignHorizontal.getWidth() / 2);
        plusSignHorizontal.setY(clip.getCenterY() - plusSignHorizontal.getHeight() / 2);
    }
}
