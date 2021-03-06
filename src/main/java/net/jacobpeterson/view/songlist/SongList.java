package net.jacobpeterson.view.songlist;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import net.jacobpeterson.view.MainView;
import net.jacobpeterson.view.songlist.item.SongListItem;

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
        this.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
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
        scrollPaneContent.setFillWidth(true);

        // Testing:
        SongListItem songListItem = new SongListItem(this);
        songListItem.setPrefHeight(115);
        songListItem.setAlbumArtImage(new Image("file:/Users/Jacob/Downloads/stoney.jpg"));
        songListItem.activeProperty().set(true);
        songListItem.getArtist().setText("Artist");
        scrollPaneContent.getChildren().add(songListItem);
    }

    /**
     * Sets up the scroll pane.
     */
    private void setupScrollPane() {
        scrollPane = new ScrollPane(scrollPaneContent);
        scrollPane.getStylesheets().add(getClass().getResource("/css/CustomScrollPane.css").toExternalForm());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) ->
                scrollPaneContent.setPrefWidth(newBounds.getWidth()));

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
