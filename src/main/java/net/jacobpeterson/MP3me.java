package net.jacobpeterson;

import net.jacobpeterson.view.MainView;

public class MP3me {

    private MainView mainView;

    /**
     * Instantiates a new MP3me.
     */
    public MP3me() {
        mainView = new MainView(this);
    }

    /**
     * Start MP3me.
     */
    public void start() {
        mainView.start();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        MP3me mp3me = new MP3me();
        mp3me.start();
    }
}
