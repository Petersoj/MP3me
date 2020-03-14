package net.jacobpeterson.view;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.jacobpeterson.MP3me;

public class MainView {

    private MP3me mp3me;
    private Stage stage;
    private GridPane gridPane;
    private Scene scene;

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
     * @param stage the stage
     */
    public void start(Stage stage) {
        this.stage = stage;
        this.gridPane = new GridPane();
        this.scene = new Scene(gridPane);

        this.setupStage();
        this.setupScene();
        this.setupNodes();

        stage.show();
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

        final double marginSizePercent = 3.5;

        ColumnConstraints marginColumn = new ColumnConstraints();
        marginColumn.setHgrow(Priority.NEVER);
        marginColumn.setFillWidth(true);
        marginColumn.setPercentWidth(marginSizePercent);

        ColumnConstraints songListColumn = new ColumnConstraints();
        songListColumn.setHgrow(Priority.ALWAYS);
        songListColumn.setFillWidth(true);
        songListColumn.setPercentWidth(25);

        ColumnConstraints songEditorColumn = new ColumnConstraints();
        songEditorColumn.setHgrow(Priority.ALWAYS);
        songEditorColumn.setFillWidth(true);
        songEditorColumn.setPercentWidth(64.5);

        gridPane.getColumnConstraints().addAll(marginColumn, songListColumn, marginColumn,
                songEditorColumn, marginColumn);

        RowConstraints marginRow = new RowConstraints();
        marginRow.setVgrow(Priority.NEVER);
        marginRow.setFillHeight(true);
        gridPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) ->
                marginRow.setPrefHeight(newValue.getWidth() * marginSizePercent / 100));

        RowConstraints contentRow = new RowConstraints();
        contentRow.setVgrow(Priority.ALWAYS);
        contentRow.setFillHeight(true);

        gridPane.getRowConstraints().addAll(marginRow, contentRow, marginRow);
    }

    /**
     * Sets up the nodes.
     */
    private void setupNodes() {
        BlurredBackgroundImage blurredBackgroundImage = new BlurredBackgroundImage();
        gridPane.add(blurredBackgroundImage, 0, 0, gridPane.getColumnCount(), gridPane.getRowCount());
        blurredBackgroundImage.toBack();
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
}
