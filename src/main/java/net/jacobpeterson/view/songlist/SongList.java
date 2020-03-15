package net.jacobpeterson.view.songlist;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.jacobpeterson.view.MainView;

public class SongList extends StackPane {

    private final MainView mainView;
    private VBox scrollPaneContent;
    private ScrollPane scrollPane;

    /**
     * Instantiates a new Song list.
     *
     * @param mainView the main view
     */
    public SongList(MainView mainView) {
        this.mainView = mainView;

        this.setupScrollPaneContent();
        this.setupScrollPane();
        this.setupPane();
    }

    /**
     * Sets up this pane.
     */
    private void setupPane() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.3), null, null)));

        Rectangle roundRectangle = new Rectangle();
        roundRectangle.setArcWidth(50);
        roundRectangle.setArcHeight(50);
        this.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            roundRectangle.setWidth(newValue.getWidth());
            roundRectangle.setHeight(newValue.getHeight());
        });
        this.setClip(roundRectangle);

        this.getChildren().add(scrollPane);
    }

    /**
     * Sets up the scroll pane content.
     */
    private void setupScrollPaneContent() {
        scrollPaneContent = new VBox();
        scrollPaneContent.setBackground(Background.EMPTY);
    }

    /**
     * Sets up the scroll pane.
     */
    private void setupScrollPane() {
        scrollPane = new ScrollPane(scrollPaneContent);
        scrollPane.getStylesheets().add(getClass().getResource("/css/CustomScrollPane.css").toExternalForm());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    /**
     * Gets main view.
     *
     * @return the main view
     */
    public MainView getMainView() {
        return mainView;
    }

    /**
     * Gets scroll pane content.
     *
     * @return the scroll pane content
     */
    public VBox getScrollPaneContent() {
        return scrollPaneContent;
    }

    /**
     * Gets scroll pane.
     *
     * @return the scroll pane
     */
    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
