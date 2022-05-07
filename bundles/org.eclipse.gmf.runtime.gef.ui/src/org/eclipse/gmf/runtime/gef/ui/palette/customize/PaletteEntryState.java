/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.palette.customize;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.ui.IMemento;

/**
 * Used to store, rollback, and apply palette customizations of a palette entry.
 * 
 * @since 2.1
 * @author crevells
 */
public class PaletteEntryState
    implements IPaletteState {

    private PaletteEntry paletteEntry;

    private static final String LABEL_KEY = "label"; //$NON-NLS-1$

    private static final String DESCRIPTION_KEY = "description"; //$NON-NLS-1$

    private static final String ISVISIBLE_KEY = "isVisible"; //$NON-NLS-1$

    private String label;

    private String description;

    private boolean isVisible;

    public PaletteEntryState(PaletteEntry entry) {
        paletteEntry = entry;
    }

    /**
     * Gets the palette entry being acted on.
     * 
     * @return
     */
    protected PaletteEntry getPaletteEntry() {
        return paletteEntry;
    }

    public void storeState() {
        PaletteEntry entry = getPaletteEntry();
        label = entry.getLabel();
        description = entry.getDescription();
        isVisible = entry.isVisible();
    }

    public void rollback() {
        PaletteEntry entry = getPaletteEntry();
        entry.setLabel(label);
        entry.setDescription(description);
        entry.setVisible(isVisible);
    }

    public void storeChangesInMemento(IMemento memento) {
        PaletteEntry entry = getPaletteEntry();

        if (label != null && !label.equals(entry.getLabel())
            || (label == null && entry.getLabel() != null)) {
            memento.putString(LABEL_KEY, entry.getLabel());
        }
        if (description != null && !description.equals(entry.getDescription())
            || (description == null && entry.getDescription() != null)) {
            memento.putString(DESCRIPTION_KEY, entry.getDescription());
        }
        if (isVisible != entry.isVisible()) {
            memento.putBoolean(ISVISIBLE_KEY, entry.isVisible());
        }
    }

    public void applyChangesFromMemento(IMemento entryMemento) {
        PaletteEntry entry = getPaletteEntry();

        String sValue = entryMemento.getString(LABEL_KEY);
        if (sValue != null) {
            entry.setLabel(sValue);
        }
        sValue = entryMemento.getString(DESCRIPTION_KEY);
        if (sValue != null) {
            entry.setDescription(sValue);
        }
        Boolean bValue = entryMemento.getBoolean(ISVISIBLE_KEY);
        if (bValue != null) {
            entry.setVisible(bValue);
        }
    }

}
