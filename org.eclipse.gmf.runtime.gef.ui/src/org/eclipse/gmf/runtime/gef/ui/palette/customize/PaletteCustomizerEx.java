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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.customize.DefaultEntryPage;
import org.eclipse.gef.ui.palette.customize.DrawerEntryPage;
import org.eclipse.gef.ui.palette.customize.EntryPage;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.gef.ui.internal.GefDebugOptions;
import org.eclipse.gmf.runtime.gef.ui.internal.GefPlugin;
import org.eclipse.gmf.runtime.gef.ui.internal.GefStatusCodes;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

/**
 * Extends GEF's <code>PaletteCustomizer</code> to support:
 * <li>Saving the customizations to the workspace preference store.</li>
 * <li>Rolling back the changes made to the palette model if the cancel button
 * is pressed. See bugzilla#211065.</li>
 * 
 * @since 2.1
 * @author crevells
 */
public class PaletteCustomizerEx
    extends PaletteCustomizer {

    /**
     * Customized so that the palette state can be saved before the change is
     * made to the palette model so that:
     * <li>when the cancel button is pressed, the stored state can be restored</li>
     * <li>when the save button is pressed, the customizations made since the
     * state was stored can be written to the workspace preferences</li>
     */
    private class DefaultEntryPageEx
        extends DefaultEntryPage {

        protected void handleDescriptionChanged(String text) {
            storePaletteState(getEntry());
            super.handleDescriptionChanged(text);
        }

        protected void handleHiddenSelected(boolean isChecked) {
            storePaletteState(getEntry());
            super.handleHiddenSelected(isChecked);
        }

        protected void handleNameChanged(String text) {
            storePaletteState(getEntry());
            super.handleNameChanged(text);
        }

    }

    /**
     * Customized so that the palette state can be saved before the change is
     * made to the palette model so that:
     * <li>when the cancel button is pressed, the stored state can be restored</li>
     * <li>when the save button is pressed, the customizations made since the
     * state was stored can be written to the workspace preferences</li>
     */
    private class DrawerEntryPageEx
        extends DrawerEntryPage {

        protected void handleOpenSelected(boolean selection) {
            storePaletteState(getEntry());
            super.handleOpenSelected(selection);
        }

        protected void handlePinSelected(boolean selection) {
            storePaletteState(getEntry());
            super.handlePinSelected(selection);
        }

        protected void handleDescriptionChanged(String text) {
            storePaletteState(getEntry());
            super.handleDescriptionChanged(text);
        }

        protected void handleHiddenSelected(boolean isChecked) {
            storePaletteState(getEntry());
            super.handleHiddenSelected(isChecked);
        }

        protected void handleNameChanged(String text) {
            storePaletteState(getEntry());
            super.handleNameChanged(text);
        }
    }

    /**
     * id used in the workspace preference store for palette customizations
     */
    private static final String PALETTE_CUSTOMIZATIONS_ID = "org.eclipse.gmf.runtime.diagram.ui.paletteCustomizations"; //$NON-NLS-1$

    /**
     * Matches each palette entry with a palette state.
     */
    private HashMap<PaletteEntry, IPaletteState> paletteStates = new HashMap<PaletteEntry, IPaletteState>();

    /**
     * Creates a new instance.
     */
    public PaletteCustomizerEx() {
        super();
    }

    public EntryPage getPropertiesPage(PaletteEntry entry) {
        if (entry.getType().equals(PaletteDrawer.PALETTE_TYPE_DRAWER)) {
            return new DrawerEntryPageEx();
        }
        return new DefaultEntryPageEx();
    }

    /**
     * Creation factory method for the <code>IPaletteState</code>. Clients
     * may override to provide custom <code>IPaletteStates</code>.
     * 
     * @param entry
     *            the palette entry
     * @return a new <code>IPaletteState</code> instance.
     */
    protected IPaletteState createPaletteState(PaletteEntry entry) {
        if (entry instanceof PaletteDrawer) {
            return new PaletteDrawerState((PaletteDrawer) entry);
        } else {
            return new PaletteEntryState(entry);
        }
    }

    /**
     * Stores the palette state for a given palette entry if the state has not
     * yet been stored.
     * 
     * @param entry
     *            the palette entry
     */
    private void storePaletteState(PaletteEntry entry) {
        if (paletteStates.get(entry) != null) {
            // the palette state has already been stored
            return;
        }

        IPaletteState paletteState = createPaletteState(entry);
        paletteState.storeState();

        paletteStates.put(entry, paletteState);
    }

    public void revertToSaved() {
        for (Iterator<Entry<PaletteEntry, IPaletteState>> iterator = paletteStates
            .entrySet().iterator(); iterator.hasNext();) {
            Entry<PaletteEntry, IPaletteState> entry = iterator.next();
            entry.getValue().rollback();
        }
        paletteStates.clear();
    }

    public void save() {
        if (paletteStates.isEmpty()) {
            return;
        }

        // If there are already existing palette customizations we will add to
        // them, otherwise, create a new XML memento which makes it easy to save
        // the customizations in a tree format.
        XMLMemento rootMemento = getExistingCustomizations();
        if (rootMemento == null) {
            rootMemento = XMLMemento.createWriteRoot(PALETTE_CUSTOMIZATIONS_ID);
        }
        for (Iterator<Entry<PaletteEntry, IPaletteState>> iterator = paletteStates
            .entrySet().iterator(); iterator.hasNext();) {
            Entry<PaletteEntry, IPaletteState> entry = iterator.next();

            IMemento memento = getMementoForEntry(rootMemento, entry.getKey());
            if (memento != null) {
                entry.getValue().storeChangesInMemento(memento);
            }
        }

        StringWriter writer = new StringWriter();
        try {
            rootMemento.save(writer);

            IPreferenceStore preferences = getPreferences();
            if (preferences != null) {
                preferences.setValue(PALETTE_CUSTOMIZATIONS_ID, writer
                    .toString());
            }
        } catch (IOException e) {
            Trace
                .catching(
                    GefPlugin.getInstance(),
                    GefDebugOptions.EXCEPTIONS_CATCHING,
                    getClass(),
                    "Problem saving the XML memento when saving the palette customizations.", //$NON-NLS-1$
                    e);
            Log
                .warning(
                    GefPlugin.getInstance(),
                    GefStatusCodes.IGNORED_EXCEPTION_WARNING,
                    "Problem saving the XML memento when saving the palette customizations.", //$NON-NLS-1$
                    e);
        }

        paletteStates.clear();
    }

    /**
     * Given the rootMemento, gets the memento that already exists for the
     * palette entry or creates a new one in the rootMemento (and the necessary
     * palette container mementos) if one does not exist yet. The root memento's
     * tree structure matches that of the palette root. If a palette entry in
     * stack A, in drawer B is customized, the root memento will have a child
     * memento for drawer B which has a child memento for stack A which has a
     * child memento for the entry. The memento's use the palette entry's id.
     * 
     * @param rootMemento
     *            the root memento representing the palette root
     * @param paletteEntry
     *            the palette entry for which a memento should be retrieved or
     *            created
     * @return returns the memento that already exists for the palette entry or
     *         creates a new one in the rootMemento if one does not exist yet or
     *         null if the memento could not be created (most likely because the
     *         palete id is not acceptable).
     */
    private IMemento getMementoForEntry(IMemento rootMemento,
            PaletteEntry paletteEntry) {

        ArrayList<String> idList = new ArrayList<String>();
        idList.add(paletteEntry.getId());

        PaletteContainer parent = paletteEntry.getParent();
        while (parent != null
            && !PaletteRoot.PALETTE_TYPE_ROOT.equals(parent.getType())) {
            idList.add(parent.getId());
            parent = parent.getParent();
        }

        // go through ids in reverse order and create the mementos as necessary
        IMemento containerMemento = rootMemento;
        for (int i = idList.size() - 1; i >= 0; i--) {
            String id = idList.get(i);
            IMemento memento = containerMemento.getChild(id);
            if (memento == null) {
                try {
                    memento = containerMemento.createChild(id);
                } catch (Exception e) {
                    Trace
                        .catching(
                            GefPlugin.getInstance(),
                            GefDebugOptions.EXCEPTIONS_CATCHING,
                            getClass(),
                            "Invalid palette id encountered when saving the palette customizations.", //$NON-NLS-1$
                            e);
                    Log
                        .warning(
                            GefPlugin.getInstance(),
                            GefStatusCodes.IGNORED_EXCEPTION_WARNING,
                            "Invalid palette id encountered when saving the palette customizations.", //$NON-NLS-1$
                            e);
                    return null;
                }
            }
            containerMemento = memento;
        }

        return containerMemento;
    }

    /**
     * Retrieves the palette customizations from the workspace preferences and
     * applies these customizations to the palette root.
     * 
     * @param paletteRoot
     *            the palette root on which to apply the customizations
     */
    public void applyCustomizationsToPalette(PaletteRoot paletteRoot) {
        XMLMemento rootMemento = getExistingCustomizations();
        if (rootMemento != null) {
            applyCustomizations(rootMemento, paletteRoot);
        }
    }

    /**
     * Recursive helper method to apply the palette customizations in a memento
     * to a palette container.
     * 
     * @param containerMemento
     *            the mememto where the container's customizations are stored
     * @param paletteContainer
     *            the palette container on which to apply the customizations
     */
    private void applyCustomizations(IMemento containerMemento,
            PaletteContainer paletteContainer) {

        for (Iterator iterator = paletteContainer.getChildren().iterator(); iterator
            .hasNext();) {
            PaletteEntry entry = (PaletteEntry) iterator.next();
            IMemento childMemento = containerMemento.getChild(entry.getId());
            if (childMemento != null) {
                IPaletteState state = createPaletteState(entry);
                state.applyChangesFromMemento(childMemento);
                if (entry instanceof PaletteContainer) {
                    applyCustomizations(childMemento, (PaletteContainer) entry);
                }
            }

        }
    }

    /**
     * Retrieves the root memento from the workspace preferences if there were
     * existing palette customizations.
     * 
     * @return the root memento if there were existing customizations; null
     *         otherwise
     */
    private XMLMemento getExistingCustomizations() {
        IPreferenceStore preferences = getPreferences();
        if (preferences != null) {
            String sValue = preferences.getString(PALETTE_CUSTOMIZATIONS_ID);
            if (sValue != null && !sValue.equals("")) { //$NON-NLS-1$
                try {
                    XMLMemento rootMemento = XMLMemento
                        .createReadRoot(new StringReader(sValue));
                    return rootMemento;
                } catch (WorkbenchException e) {
                    Trace
                        .catching(
                            GefPlugin.getInstance(),
                            GefDebugOptions.EXCEPTIONS_CATCHING,
                            getClass(),
                            "Problem creating the XML memento when saving the palette customizations.", //$NON-NLS-1$
                            e);
                    Log
                        .warning(
                            GefPlugin.getInstance(),
                            GefStatusCodes.IGNORED_EXCEPTION_WARNING,
                            "Problem creating the XML memento when saving the palette customizations.", //$NON-NLS-1$
                            e);
                }
            }
        }
        return null;
    }

    /**
     * Gets the preferences in which the palette customizations are saved.
     * 
     * @return the preference store or null if it could not be retrieved for
     *         whatever reason
     */
    private IPreferenceStore getPreferences() {
        // Save the preferences in the GefPlugin so they are shared
        // amoungst all GMF editors in case palette entries are repeated on
        // multiple editors.
        return GefPlugin.getInstance().getPreferenceStore();
    }

}
