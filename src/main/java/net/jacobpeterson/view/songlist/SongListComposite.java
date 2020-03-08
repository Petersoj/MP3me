package net.jacobpeterson.view.songlist;

import net.jacobpeterson.view.MainShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class SongListComposite extends ScrolledComposite implements PaintListener {

    private final MainShell mainShell;
    private final Color blackColor;

    /**
     * Instantiates a new Song list composite.
     *
     * @param mainShell the main shell
     */
    public SongListComposite(MainShell mainShell) {
        super(mainShell.getShell(), SWT.V_SCROLL);

        this.mainShell = mainShell;
        this.blackColor = mainShell.getDisplay().getSystemColor(SWT.COLOR_BLACK);

        this.addPaintListener(this);
    }

    @Override
    public void paintControl(PaintEvent paintEvent) {
        GC gc = paintEvent.gc;
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.ON);

        gc.setAlpha(50);
        gc.setBackground(blackColor);

        Rectangle bounds = this.getBounds();
        gc.fillRoundRectangle(0, 0, bounds.width, bounds.height, 35, 35);
    }
}
