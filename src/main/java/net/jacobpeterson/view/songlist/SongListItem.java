package net.jacobpeterson.view.songlist;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SongListItem extends GridPane {

    private final SongList songList;
    private final Background hoverBackground;

    private DoubleProperty progressBarPercentage;

    private Rectangle progressBar;
    private Region albumArt;
    private BackgroundImage albumArtBackground;
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

        hoverBackground = new Background(new BackgroundFill(Color.grayRgb(0, 0.4), null, null));
        progressBarPercentage = new SimpleDoubleProperty(0);

        this.setupPane();
        this.setupProgressBar();
        this.setupAlbumArt();
        this.setupTitle();
        this.setupArtist();
    }

    /**
     * Sets up this pane.
     */
    private void setupPane() {
        this.setBackground(Background.EMPTY);
        this.setOnMouseEntered(event -> this.setBackground(hoverBackground));
        this.setOnMouseExited(event -> this.setBackground(Background.EMPTY));

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

        RowConstraints artistRow = new RowConstraints();
        artistRow.setVgrow(Priority.NEVER);
        artistRow.setPercentHeight(20);

        RowConstraints statusRow = new RowConstraints();
        statusRow.setVgrow(Priority.ALWAYS);
        statusRow.setValignment(VPos.BOTTOM);

        this.getRowConstraints().addAll(marginRow, titleRow, artistRow, statusRow, marginRow);
    }

    /**
     * Sets up the progress bar.
     */
    private void setupProgressBar() {

    }

    /**
     * Sets up the album art.
     */
    private void setupAlbumArt() {
        albumArt = new Region();
        // albumArtBackground = new BackgroundImage()
        // albumArt.setBackground(new Background(albumArtBackground));
    }

    /**
     * Sets up the title.
     */
    private void setupTitle() {

    }

    /**
     * Sets up the artist.
     */
    private void setupArtist() {

    }

    /**
     * Sets progress bar percentage (0% - 100%).
     *
     * @param percentage the percentage
     */
    public void setProgressBarPercentage(int percentage) {

    }
}
