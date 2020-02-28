package net.jacobpeterson.view;

import net.jacobpeterson.MP3me;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class MainShell {

    private final MP3me mp3me;

    private final Display display;
    private final Shell shell;
    private SongListComposite songListComposite;
    private FormLayout formLayout;

    /**
     * Instantiates a new Main shell.
     *
     * @param mp3me the MP3me
     */
    public MainShell(MP3me mp3me) {
        this.mp3me = mp3me;

        display = new Display();
        shell = new Shell();
    }

    /**
     * Starts the view. This is a blocking method!
     */
    public void start() {
        this.setupShell();
        this.setupWidgets();
        this.setupLayout();

        shell.open();
        shell.forceActive();

        while (!shell.isDisposed()) {
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
        if (!shell.isDisposed()) {
            display.dispose();
        }
    }

    /**
     * Set up shell.
     */
    private void setupShell() {
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
        int shellWidth = (int) (0.65 * monitorBounds.width);
        int shellHeight = (int) (0.65 * monitorBounds.height);
        shell.setSize(shellWidth, shellHeight);
        shell.setLocation((monitorBounds.width / 2) - (shellWidth / 2) + monitorBounds.x,
                          (monitorBounds.height / 2) - (shellHeight / 2) + monitorBounds.y);

        // Set background black
        shell.setBackground(new Color(display, 0, 0, 0));
    }

    /**
     * Set up widgets.
     */
    private void setupWidgets() {

    }

    /**
     * Set up layout.
     */
    private void setupLayout() {
        formLayout = new FormLayout();

        shell.setLayout(formLayout);
        shell.layout();
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
     * Gets the shell.
     *
     * @return the shell
     */
    public Shell getShell() {
        return shell;
    }
}
