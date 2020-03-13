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

        this.setupStageAndScene();
        this.setupNodes();

        stage.show();
    }

    /**
     * Sets up the stage.
     */
    private void setupStageAndScene() {
        // Setup stage

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

        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("");
        stage.setScene(scene);

        // Setup scene
        scene.setFill(Color.BLACK);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().add(columnConstraints);
        gridPane.getRowConstraints().add(rowConstraints);
    }

    /**
     * Sets up the nodes.
     */
    private void setupNodes() {
        BlurredBackgroundImage blurredBackgroundImage = new BlurredBackgroundImage(this);

        gridPane.add(blurredBackgroundImage, 0, 0);
        gridPane.layoutBoundsProperty().addListener(blurredBackgroundImage);
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
