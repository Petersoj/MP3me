package net.jacobpeterson.view;

import net.jacobpeterson.MP3me;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class MainView {

    private final MP3me mp3me;

    private final Display display;
    private final Shell mainShell;

    /**
     * Instantiates a new Main view.
     *
     * @param mp3me the MP3me
     */
    public MainView(MP3me mp3me) {
        this.mp3me = mp3me;

        display = new Display();
        mainShell = new Shell();
    }

    /**
     * Starts the view. This is a blocking method!
     */
    public void start() {
        this.setupShell();

        mainShell.open();

        while (!mainShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();

        this.stop();
    }

    /**
     * Stops the view.
     */
    public void stop() {
        if (!mainShell.isDisposed()) {
            display.dispose();
        }
    }

    public void setupShell() {
        // Position Shell in center of active monitor
        Point cursorLocation = display.getCursorLocation();
        Monitor activeMonitor = display.getPrimaryMonitor();
        for (Monitor monitor : display.getMonitors()) {
            if (monitor.getBounds().contains(cursorLocation)) {
                activeMonitor = monitor;
                break;
            }
        }
        Rectangle monitorBounds = activeMonitor.getBounds();
        int shellWidth = (int) (0.6 * monitorBounds.width);
        int shellHeight = (int) (0.7 * monitorBounds.height);
        mainShell.setSize(shellWidth, shellHeight);
        mainShell.setLocation((monitorBounds.width / 2) - (shellWidth / 2) + monitorBounds.x,
                              (monitorBounds.height / 2) - (shellHeight / 2) + monitorBounds.y);

        // Set background black
        mainShell.setBackground(new Color(display, 0, 0, 0));
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
     * Gets display.
     *
     * @return the display
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Gets main shell.
     *
     * @return the main shell
     */
    public Shell getMainShell() {
        return mainShell;
    }
}
