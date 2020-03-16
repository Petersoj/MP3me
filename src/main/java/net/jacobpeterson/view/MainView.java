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
     * Starts the MainScene.
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
        this.setupControls();

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
    public void setupScene() {
        scene.setFill(Color.BLACK);

        gridPane.setBackground(Background.EMPTY);

        ColumnConstraints marginColumn = new ColumnConstraints();
        marginColumn.setHgrow(Priority.NEVER);
        marginColumn.setFillWidth(true);
        marginColumn.setPercentWidth(3.5);

        ColumnConstraints songListColumn = new ColumnConstraints();
        songListColumn.setHgrow(Priority.NEVER);
        songListColumn.setFillWidth(true);
        songListColumn.setPercentWidth(25);

        ColumnConstraints songEditorColumn = new ColumnConstraints();
        songEditorColumn.setHgrow(Priority.ALWAYS);
        songEditorColumn.setFillWidth(true);

        gridPane.getColumnConstraints().addAll(marginColumn, songListColumn, marginColumn,
                songEditorColumn, marginColumn);

        RowConstraints marginRow = new RowConstraints();
        marginRow.setVgrow(Priority.NEVER);
        marginRow.setFillHeight(true);
        gridPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) ->
                marginRow.setPrefHeight(newValue.getWidth() * marginColumn.getPercentWidth() / 100));

        RowConstraints contentRow = new RowConstraints();
        contentRow.setVgrow(Priority.ALWAYS);
        contentRow.setFillHeight(true);

        gridPane.getRowConstraints().addAll(marginRow, contentRow, marginRow);
    }

    /**
     * Sets up the controls.
     */
    private void setupControls() {
        blurredBackgroundImage = new BlurredBackgroundImage();
        gridPane.add(blurredBackgroundImage, 0, 0, gridPane.getColumnCount(), gridPane.getRowCount());

        songList = new SongList(this);
        gridPane.add(songList, 1, 1);

        // Testing:
        blurredBackgroundImage.setBlurredImage(new Image("file:/Users/Jacob/Downloads/hb.png"), 30);
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
