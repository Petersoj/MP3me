package net.jacobpeterson.view.songlist;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import net.jacobpeterson.view.MainView;

public class SongList extends StackPane {

    private final MainView mainView;
    private VBox scrollPaneContent;
    private ScrollPane scrollPane;
    private AddButton addButton;

    /**
     * Instantiates a new Song list.
     *
     * @param mainView the main view
     */
    public SongList(MainView mainView) {
        this.mainView = mainView;

        this.setupPane();
        this.setupScrollPaneContent();
        this.setupScrollPane();
        this.setupAddButton();
    }

    /**
     * Sets up this pane.
     */
    private void setupPane() {
        this.setBackground(mainView.getGlassBackground());

        Rectangle clip = new Rectangle();
        clip.setArcWidth(mainView.getRoundRectangleArcSize());
        clip.setArcHeight(mainView.getRoundRectangleArcSize());
        this.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            clip.setWidth(newValue.getWidth());
            clip.setHeight(newValue.getHeight());
        });
        this.setClip(clip);

        this.setAlignment(Pos.BOTTOM_RIGHT);
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

        this.getChildren().add(scrollPane);
    }

    private void setupAddButton() {
        addButton = new AddButton();

        double size = 30;
        double translate = mainView.getRoundRectangleArcSize() - size;

        addButton.setMaxSize(size, size);
        addButton.setTranslateX(-translate);
        addButton.setTranslateY(-translate);

        this.getChildren().add(addButton);
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
