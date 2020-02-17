package net.jacobpeterson;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MP3me {

    static Boolean blnMouseDown = false;
    static int xPos = 0;
    static int yPos = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(new Gson().toJson(new JsonPrimitive(123)));

        // CIRCULAR BLUR METHOD: https://www.codota.com/web/assistant/code/rs/5c65fb401095a50001950eb2#L782

        final Display display = new Display();
        final Shell shell = new Shell(display, SWT.NO_TRIM);

        shell.setRegion(createRoundedRectangle(0, 0, 1000, 563, 20));
        shell.setBackground(new Color(display, 0, 0, 0));

        // Position Shell in center of Display
        Rectangle screenSize = shell.getDisplay().getPrimaryMonitor().getBounds();
        Rectangle shellBounds = shell.getBounds();
        shell.setLocation(screenSize.x + (screenSize.width - shellBounds.width) / 2,
                          screenSize.y + (screenSize.height - shellBounds.height) / 2);

        shell.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent arg0) {
                blnMouseDown = false;
            }

            @Override
            public void mouseDown(MouseEvent e) {
                blnMouseDown = true;
                xPos = e.x;
                yPos = e.y;
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {

            }
        });
        shell.addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent e) {
                if (blnMouseDown) {
                    shell.setLocation(shell.getLocation().x + (e.x - xPos), shell.getLocation().y + (e.y - yPos));
                }
            }
        });

        // shell.setAlpha(12); // 13 is the min for touch input apparently

        shell.setLayout(new FillLayout());
        shell.layout(); // Layout everything
        shell.open(); // Shows the Window

        // GC gc = new GC(shell);
        // final Image image = new Image(display, shell.getSize().x, shell.getSize().y);
        // gc.copyArea(image, 0, 0);
        // gc.dispose();
        // ImageLoader imageLoader = new ImageLoader();
        // imageLoader.data = new ImageData[]{image.getImageData()};
        // imageLoader.save("/Users/Jacob/Desktop/test.png", SWT.IMAGE_PNG);

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public static Region createRoundedRectangle(int x, int y, int W, int H, int r) {
        Region region = new Region();
        int d = (2 * r); // diameter

        region.add(circle(r, (x + r), (y + r)));
        region.add(circle(r, (x + W - r), (y + r)));
        region.add(circle(r, (x + W - r), (y + H - r)));
        region.add(circle(r, (x + r), (y + H - r)));

        region.add((x + r), y, (W - d), H);
        region.add(x, (y + r), W, (H - d));

        return region;
    }

    /**
     * Defines the coordinates of a circle.
     *
     * @param r       radius
     * @param offsetX x offset of the centre
     * @param offsetY y offset of the centre
     *
     * @return the set of coordinates that approximates the circle.
     */
    public static int[] circle(int r, int offsetX, int offsetY) {
        int[] polygon = new int[8 * r + 4];
        // x^2 + y^2 = r^2
        for (int i = 0; i < 2 * r + 1; i++) {
            int x = i - r;
            int y = (int) Math.sqrt(r * r - x * x);
            polygon[2 * i] = offsetX + x;
            polygon[2 * i + 1] = offsetY + y;
            polygon[8 * r - 2 * i - 2] = offsetX + x;
            polygon[8 * r - 2 * i - 1] = offsetY - y;
        }
        return polygon;
    }
}
