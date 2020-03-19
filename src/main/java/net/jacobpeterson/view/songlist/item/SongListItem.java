package net.jacobpeterson.view.songlist.item;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import net.jacobpeterson.view.songlist.SongList;

public class SongListItem extends GridPane {

    private final SongList songList;
    private final Background hoverBackground;
    private final Background activeBackground;
    private final Background emptyAlbumArtBackground;
    private final IntegerProperty progressBarPercentageProperty;
    private final BooleanProperty activeProperty;

    private Rectangle progressBar;
    private Region albumArt;
    private Label title;
    private Label artist;
    private Label status;

    /**
     * Instantiates a new Song list item.
     *
     * @param songList the song list
     */
    public SongListItem(SongList songList) {
        this.songList = songList;

        hoverBackground = new Background(new BackgroundFill(Color.grayRgb(0, 0.3), null, null));
        activeBackground = new Background(new BackgroundFill(Color.grayRgb(0, 0.6), null, null));
        emptyAlbumArtBackground = new Background(new BackgroundImage(new Image(this.getClass()
                .getResourceAsStream("/logo/logo_empty.png")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)));
        progressBarPercentageProperty = new SimpleIntegerProperty(0);
        activeProperty = new SimpleBooleanProperty(false);

        this.setupPane();
        this.setupProgressBar();
        this.setupAlbumArt();
        this.setupTitle();
        this.setupArtist();
        this.setupStatus();
    }

    /**
     * Sets up this pane.
     */
    private void setupPane() {
        this.setBackground(Background.EMPTY);
        this.setOnMouseEntered(event -> this.setBackground(hoverBackground));
        this.setOnMouseExited(event -> {
            if (activeProperty.get()) {
                this.setBackground(activeBackground);
            } else {
                this.setBackground(Background.EMPTY);
            }
        });
        this.activeProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.setBackground(activeBackground);
            } else {
                this.setBackground(Background.EMPTY);
            }
        });

        this.setPrefHeight(115);

        // Column constraints
        ColumnConstraints marginColumn = new ColumnConstraints();
        marginColumn.setHgrow(Priority.NEVER);
        marginColumn.setPercentWidth(5);

        ColumnConstraints albumArtColumn = new ColumnConstraints();
        albumArtColumn.setHgrow(Priority.NEVER);
        albumArtColumn.setPercentWidth(20);

        ColumnConstraints songInfoColumn = new ColumnConstraints();
        songInfoColumn.setHgrow(Priority.ALWAYS);
        songInfoColumn.setHalignment(HPos.LEFT);

        this.getColumnConstraints().addAll(marginColumn, albumArtColumn, marginColumn, songInfoColumn);

        // Row constraints
        RowConstraints marginRow = new RowConstraints();
        marginRow.setVgrow(Priority.NEVER);
        this.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) ->
                marginRow.setPrefHeight(newBounds.getWidth() * marginColumn.getPercentWidth() / 100));

        RowConstraints titleRow = new RowConstraints();
        titleRow.setVgrow(Priority.NEVER);
        titleRow.setPercentHeight(30);
        titleRow.setValignment(VPos.BOTTOM);

        RowConstraints artistRow = new RowConstraints();
        artistRow.setVgrow(Priority.NEVER);
        artistRow.setPercentHeight(20);
        artistRow.setValignment(VPos.TOP);

        RowConstraints statusRow = new RowConstraints();
        statusRow.setVgrow(Priority.ALWAYS);
        statusRow.setValignment(VPos.BOTTOM);

        this.getRowConstraints().addAll(marginRow, titleRow, artistRow, statusRow, marginRow);
    }

    /**
     * Sets up the progress bar.
     */
    private void setupProgressBar() {
        progressBar = new Rectangle();
        progressBar.setFill(Color.grayRgb(255, 0.15));
        progressBar.setX(0);
        progressBar.setY(0);
        this.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            progressBar.setWidth(newBounds.getWidth() * progressBarPercentageProperty.get() / 100);
            progressBar.setHeight(newBounds.getHeight());
        });

        this.add(progressBar, 0, 0, this.getColumnCount() - 1, this.getRowCount() - 1);
    }

    /**
     * Sets up the album art.
     */
    private void setupAlbumArt() {
        albumArt = new Region();
        albumArt.setBackground(emptyAlbumArtBackground);

        this.add(albumArt, 1, 1, 1, this.getRowCount() - 2);
    }

    /**
     * Sets up the title.
     */
    private void setupTitle() {
        title = new Label("New Song");
        title.setTextFill(Color.WHITESMOKE);
        title.setFont(Font.font(22.5));

        this.add(title, 3, 1);
    }

    /**
     * Sets up the artist.
     */
    private void setupArtist() {
        artist = new Label();
        artist.setTextFill(Color.WHITESMOKE);
        artist.setFont(Font.font(15));

        this.add(artist, 3, 2);
    }

    /**
     * Sets up the status.
     */
    private void setupStatus() {
        status = new Label("New");
        status.setTextFill(Color.WHITESMOKE);
        status.setFont(Font.font(10));

        this.add(status, 3, 3);
    }

    /**
     * Sets album art image.
     *
     * @param image the image
     */
    public void setAlbumArtImage(Image image) {
        albumArt.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
    }

    /**
     * Progress bar percentage property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty progressBarPercentageProperty() {
        return progressBarPercentageProperty;
    }

    /**
     * Active property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty activeProperty() {
        return activeProperty;
    }

    /**
     * Gets progress bar.
     *
     * @return the progress bar
     */
    public Rectangle getProgressBar() {
        return progressBar;
    }

    /**
     * Gets album art.
     *
     * @return the album art
     */
    public Region getAlbumArt() {
        return albumArt;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public Label getTitle() {
        return title;
    }

    /**
     * Gets artist.
     *
     * @return the artist
     */
    public Label getArtist() {
        return artist;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Label getStatus() {
        return status;
    }
}
