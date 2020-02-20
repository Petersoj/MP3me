package net.jacobpeterson;

import net.jacobpeterson.view.MainShell;

public class MP3me {

    private MainShell mainShell;

    /**
     * Instantiates a new MP3me.
     */
    public MP3me() {
        mainShell = new MainShell(this);
    }

    /**
     * Start MP3me.
     */
    public void start() {
        mainShell.start();
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
