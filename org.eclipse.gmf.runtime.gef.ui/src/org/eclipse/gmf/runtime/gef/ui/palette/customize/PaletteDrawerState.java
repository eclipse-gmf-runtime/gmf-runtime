/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.palette.customize;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.ui.IMemento;

/**
 * Used to store, rollback, and apply palette customizations of a palette
 * drawer.
 * 
 * @since 2.1
 * @author crevells
 */
public class PaletteDrawerState
    extends PaletteEntryState {

    private static final String INITIAL_STATE_KEY = "initialState"; //$NON-NLS-1$

    int initialState;

    public PaletteDrawerState(PaletteDrawer drawer) {
        super(drawer);
    }

    public void storeState() {
        super.storeState();
        initialState = ((PaletteDrawer) getPaletteEntry()).getInitialState();
    }

    public void rollback() {
        super.rollback();
        ((PaletteDrawer) getPaletteEntry()).setInitialState(initialState);
    }

    public void storeChangesInMemento(IMemento memento) {
        super.storeChangesInMemento(memento);

        PaletteDrawer drawer = ((PaletteDrawer) getPaletteEntry());
        if (initialState != drawer.getInitialState()) {
            memento.putInteger(INITIAL_STATE_KEY, drawer.getInitialState());
        }
    }

    public void applyChangesFromMemento(IMemento entryMemento) {
        super.applyChangesFromMemento(entryMemento);

        Integer iValue = entryMemento.getInteger(INITIAL_STATE_KEY);
        if (iValue != null) {
            ((PaletteDrawer) getPaletteEntry()).setInitialState(iValue);
        }
    }
}
