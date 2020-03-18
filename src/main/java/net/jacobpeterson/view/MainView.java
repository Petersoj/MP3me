package net.jacobpeterson.view;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.jacobpeterson.MP3me;
import net.jacobpeterson.view.songlist.SongList;

public class MainView {

    private final MP3me mp3me;

    private Background glassBackground;
    private double roundRectangleArcSize;

    private Stage stage;
    private GridPane gridPane;
    private Scene scene;

    private BlurredBackgroundImage blurredBackgroundImage;
    private SongList songList;

    /**
     * Instantiates a new Main scene.
     *
     * @param mp3me the MP3me
     */
    public MainView(MP3me mp3me) {
        this.mp3me = mp3me;
    }

    /**
     * Starts the MainScene. JavaFX objects must be created within this method and not in the constructor.
     *
     * @param primaryStage the primaryStage
     */
    public void start(Stage primaryStage) {
        stage = primaryStage;
        gridPane = new GridPane();
        scene = new Scene(gridPane);

        this.setupDefaults();
        this.setupStage();
        this.setupScene();
        this.setupBlurredBackgroundImage();
        this.setupSongList();

        stage.show();
    }

    /**
     * Sets up the defaults.
     */
    private void setupDefaults() {
        glassBackground = new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.3), null, null));
        roundRectangleArcSize = 50;
    }

    /**
     * Sets up the stage.
     */
    private void setupStage() {
        // Open stage centered on moused over monitor
        Point2D mousePosition = new Robot().getMousePosition();
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D visualBounds = screen.getVisualBounds();
            if (visualBounds.contains(mousePosition)) {
                stage.setX(visualBounds.getMinX());
                stage.setY(visualBounds.getMinY());
                stage.setWidth(0.65 * visualBounds.getWidth());
                stage.setHeight(0.65 * visualBounds.getHeight());
                stage.centerOnScreen();
                break;
            }
        }

        stage.setMinWidth(600);
        stage.setMinHeight(450);
        stage.setTitle("");
        stage.setScene(scene);
    }

    /**
     * Sets up the scene.
     */
    private void setupScene() {
        scene.setFill(Color.BLACK);

        gridPane.setBackground(Background.EMPTY);

        // Column constraints
        ColumnConstraints marginColumn = new ColumnConstraints();
        marginColumn.setHgrow(Priority.NEVER);
        marginColumn.setPercentWidth(3.5);

        ColumnConstraints songListColumn = new ColumnConstraints();
        songListColumn.setHgrow(Priority.NEVER);
        songListColumn.setPercentWidth(22.5);

        ColumnConstraints songEditorColumn = new ColumnConstraints();
        songEditorColumn.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(marginColumn, songListColumn, marginColumn,
                songEditorColumn, marginColumn);

        // Row constraints
        RowConstraints marginRow = new RowConstraints();
        marginRow.setVgrow(Priority.NEVER);
        gridPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) ->
                marginRow.setPrefHeight(newBounds.getWidth() * marginColumn.getPercentWidth() / 100));

        RowConstraints contentRow = new RowConstraints();
        contentRow.setVgrow(Priority.ALWAYS);

        gridPane.getRowConstraints().addAll(marginRow, contentRow, marginRow);
    }

    /**
     * Sets up the blurred background image.
     */
    private void setupBlurredBackgroundImage() {
        blurredBackgroundImage = new BlurredBackgroundImage(this);
        blurredBackgroundImage.setBlurredImage(new Image(this.getClass()
                .getResourceAsStream("/assets/logo_full.png")), 30);
        gridPane.add(blurredBackgroundImage, 0, 0, gridPane.getColumnCount(), gridPane.getRowCount());
    }

    /**
     * Sets up the song list.
     */
    private void setupSongList() {
        songList = new SongList(this);
        gridPane.add(songList, 1, 1);
    }

    /**
     * Gets MP3me.
     *
     * @return the MP3me
     */
    public MP3me getMP3me() {
        return mp3me;
    }

    /**
     * Gets glass background.
     *
     * @return the glass background
     */
    public Background getGlassBackground() {
        return glassBackground;
    }

    /**
     * Gets round rectangle arc size.
     *
     * @return the round rectangle arc size
     */
    public double getRoundRectangleArcSize() {
        return roundRectangleArcSize;
    }

    /**
     * Gets stage.
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Gets grid pane.
     *
     * @return the grid pane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Gets scene.
     *
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Gets blurred background image.
     *
     * @return the blurred background image
     */
    public BlurredBackgroundImage getBlurredBackgroundImage() {
        return blurredBackgroundImage;
    }

    /**
     * Gets song list.
     *
     * @return the song list
     */
    public SongList getSongList() {
        return songList;
    }
}
