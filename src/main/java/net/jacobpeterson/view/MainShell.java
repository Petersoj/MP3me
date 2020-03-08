package net.jacobpeterson.view;

import net.jacobpeterson.MP3me;
import net.jacobpeterson.view.songlist.SongListComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class MainShell {

    private final MP3me mp3me;

    private final Display display;
    private final Shell shell;
    private final FormLayout formLayout;
    private BlurredImageBackground blurredImageBackground;
    private SongListComposite songListComposite;

    /**
     * Instantiates a new Main shell.
     *
     * @param mp3me the MP3me
     */
    public MainShell(MP3me mp3me) {
        this.mp3me = mp3me;

        display = new Display();
        shell = new Shell();
        formLayout = new FormLayout();
    }

    /**
     * Starts the view. This is a blocking method!
     */
    public void start() {
        this.setupShell();
        this.setupWidgets();

        shell.open();
        shell.setActive();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        this.stop();
    }

    /**
     * Stops all the views.
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
        shell.setMinimumSize(500, 400);
        shell.setLayout(formLayout);

        // Set background transparent for all components
        shell.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
        shell.setBackgroundMode(SWT.INHERIT_FORCE);
    }

    /**
     * Set up widgets.
     */
    private void setupWidgets() {
        blurredImageBackground = new BlurredImageBackground(this);
        FormData blurredImageBackgroundFormData = new FormData();
        blurredImageBackgroundFormData.top = new FormAttachment(0);
        blurredImageBackgroundFormData.right = new FormAttachment(100);
        blurredImageBackgroundFormData.bottom = new FormAttachment(100);
        blurredImageBackgroundFormData.left = new FormAttachment(0);
        blurredImageBackground.setLayoutData(blurredImageBackgroundFormData);

        // Testing:
        blurredImageBackground.setBlurredImage(new Image(display, "/Users/Jacob/Downloads/stoney.jpg"), 20);

        songListComposite = new SongListComposite(this);
        final FormData songListCompositeFormData = new FormData();
        songListCompositeFormData.top = new FormAttachment(0);
        songListCompositeFormData.right = new FormAttachment(25);
        songListCompositeFormData.bottom = new FormAttachment(100);
        songListCompositeFormData.left = new FormAttachment(5);
        shell.addControlListener(new ControlAdapter() {
            // This is for setting the top and bottom offset from the shell top and bottom to be exactly the same
            // as the left percent offset so the songListComposite is evenly laid out.
            @Override
            public void controlResized(ControlEvent e) {
                int leftOffset = songListComposite.getBounds().x;
                songListCompositeFormData.top.offset = leftOffset;
                songListCompositeFormData.bottom.offset = -leftOffset;
            }
        });
        songListComposite.setLayoutData(songListCompositeFormData);

        // After all widgets are setup and laid out, layout the shell and z-order.
        songListComposite.moveAbove(blurredImageBackground);

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
