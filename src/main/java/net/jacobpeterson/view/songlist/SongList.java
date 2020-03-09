package net.jacobpeterson.view.songlist;

import net.jacobpeterson.util.GraphicsUtil;
import net.jacobpeterson.view.MainShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class SongList extends Composite {

    private final MainShell mainShell;
    private final int roundRectangleArcSize;

    /**
     * Instantiates a new Song list composite.
     *
     * @param mainShell the main shell
     */
    public SongList(MainShell mainShell) {
        super(mainShell.getShell(), SWT.TRANSPARENT);

        this.mainShell = mainShell;
        this.roundRectangleArcSize = 30;

        this.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle bounds = SongList.this.getBounds();
                if (bounds.width > 0 && bounds.height > 0) {
                    SongList.this.setRegion(GraphicsUtil.getRoundRectangleRegion(0, 0, bounds.width, bounds.height,
                            roundRectangleArcSize / 2));
                }
            }
        });
    }

    /**
     * The Song list background for allowing anti-aliasing on a painted background.
     */
    public class SongListBackground extends Canvas implements PaintListener {

        private final Color blackColor;

        /**
         * Instantiates a new Song list background.
         */
        public SongListBackground() {
            super(mainShell.getShell(), SWT.NONE);

            this.blackColor = mainShell.getDisplay().getSystemColor(SWT.COLOR_BLACK);

            this.addPaintListener(this);
        }

        @Override
        public void paintControl(PaintEvent paintEvent) {
            GC gc = paintEvent.gc;
            gc.setAdvanced(true);
            gc.setAntialias(SWT.ON);
            gc.setInterpolation(SWT.ON);

            Rectangle bounds = SongList.this.getBounds();

            gc.setAlpha(50);
            gc.setBackground(blackColor);
            gc.fillRoundRectangle(0, 0, bounds.width, bounds.height, roundRectangleArcSize, roundRectangleArcSize);
        }
    }
}
