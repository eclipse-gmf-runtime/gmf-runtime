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

import org.eclipse.ui.IMemento;

/**
 * An interface that defines the protocol that will allow the
 * <code>PaletteCustomizer</code> store, rollback, and apply customizations to a
 * palette entry.
 * 
 * @since 2.1
 * @author crevells
 */
public interface IPaletteState {

    /**
     * Stores the current state of the palette entry.
     */
    void storeState();

    /**
     * Applies the changes from the stored state to the palette entry.
     */
    void rollback();

    /**
     * Stores changes from the existing palette entry from the stored state into
     * the memento.
     * 
     * @param memento
     *            the memento in which to store the palette entry's
     *            customizations
     */
    void storeChangesInMemento(IMemento memento);

    /**
     * Applies the changes from the memento to the palette entry.
     * 
     * @param memento
     *            the memento from which to retrieve the palette entry's
     *            customizations
     */
    void applyChangesFromMemento(IMemento memento);
}
