package net.jacobpeterson;

import javafx.application.Application;
import javafx.stage.Stage;
import net.jacobpeterson.view.MainView;

public class MP3me extends Application {

    private MainView mainView;

    @Override
    public void init() {
        mainView = new MainView(this);
    }

    @Override
    public void start(Stage primaryStage) {
        mainView.start(primaryStage);
    }

    @Override
    public void stop() {

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
