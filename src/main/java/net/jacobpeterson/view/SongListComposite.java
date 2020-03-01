package net.jacobpeterson.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

public class SongListComposite extends ScrolledComposite {

    private MainShell mainShell;

    /**
     * Instantiates a new Song list composite.
     *
     * @param mainShell the main shell
     */
    public SongListComposite(MainShell mainShell) {
        super(mainShell.getShell(), SWT.NONE);

        this.mainShell = mainShell;
    }
}
