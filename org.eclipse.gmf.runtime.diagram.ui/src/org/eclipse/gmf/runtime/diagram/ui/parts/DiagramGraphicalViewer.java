/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.parts;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.ui.internal.parts.ElementToEditPartsMap;


/**
 * @author melaasar
 *
 * Implementation of a diagram graphical viewer
 */
public class DiagramGraphicalViewer
    extends ScrollingGraphicalViewer
    implements IDiagramGraphicalViewer {
		
	/**
	 * Constructor
	 */
	public DiagramGraphicalViewer() {
		super();
	}
	

    /**
     * A selection event pending flag (for asynchronous firing)
     */
    private boolean selectionEventPending = false;

    /**
     * A registry of editparts on the diagram, mapping an element's id string
     * to a list of <code>EditParts</code>.  
     */
    private ElementToEditPartsMap elementToEditPartsMap =
        new ElementToEditPartsMap();

    /**
     * Hook a zoom enabled graphics source
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#hookControl()
     */
    protected void hookControl() {
        super.hookControl();
    }

    /**
     * Refresh drag source adapters regardless if the adapter list is empty
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#removeDragSourceListener(TransferDragSourceListener)
     */
    public void removeDragSourceListener(TransferDragSourceListener listener) {
        getDelegatingDragAdapter().removeDragSourceListener(listener);
        refreshDragSourceAdapter();
    }

    /**
     * Refresh drag target adapters regardless if the adapter list is empty
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#removeDropTargetListener(TransferDropTargetListener)
     */
    public void removeDropTargetListener(TransferDropTargetListener listener) {
        getDelegatingDropAdapter().removeDropTargetListener(listener);
        refreshDropTargetAdapter();
    }

    /**
     * Overriden to also flush pending selection events to account for 
     * OS diffences, since we are firing selection change events asynchronously.
     */
    public void flush() {
        super.flush();
        if (selectionEventPending) {
            flushSelectionEvents(getSelection());
        }

    }

    /**
     * For performance reasons, we fire the event asynchronously
     */
    protected void fireSelectionChanged() {
        if (selectionEventPending)
            return;
        selectionEventPending = true;
        Display display = Display.getCurrent();
        if (display != null) {
            display.asyncExec(new Runnable() {
                public void run() {
                    flushSelectionEvents(getSelection());
                }
            });
        }
    }

    /**
     * flush the selection events
     * @param sel
     */
    protected void flushSelectionEvents(ISelection sel) {
        selectionEventPending = false;
        SelectionChangedEvent event =
            new SelectionChangedEvent(this, sel);

        // avoid exceptions caused by selectionChanged 
        // modifiying selectionListeners
        Object[] array = selectionListeners.toArray();

        for (int i = 0; i < array.length; i++) {
            ISelectionChangedListener l = (ISelectionChangedListener)array[i];
            if (selectionListeners.contains(l))
                l.selectionChanged(event);
        }
    }
    
    private void fireEmptySelection() {
        if (selectionEventPending)
            return;
        selectionEventPending = true;
        Display display = Display.getCurrent();
        if (display != null) {
            display.asyncExec(new Runnable() {
                public void run() {
                    flushSelectionEvents(getSelection());
                    flushSelectionEvents(StructuredSelection.EMPTY);
                }
            });
        }
    }
    
    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#getDiagramEditDomain()
     */
    public IDiagramEditDomain getDiagramEditDomain() {
        return (IDiagramEditDomain)getEditDomain();
    }

    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#findEditPartsForElement(java.lang.String, java.lang.Class)
     */
    public List findEditPartsForElement(
        String elementIdStr,
        Class editPartClass) {
        return elementToEditPartsMap.findEditPartsForElement(
            elementIdStr,
            editPartClass);
    }

    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#registerEditPartForElement(java.lang.String, org.eclipse.gef.EditPart)
     */
    public void registerEditPartForElement(String elementIdStr, EditPart ep) {
        elementToEditPartsMap.registerEditPartForElement(elementIdStr, ep);
    }

    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#unregisterEditPartForElement(java.lang.String, org.eclipse.gef.EditPart)
     */
    public void unregisterEditPartForElement(
        String elementIdStr,
        EditPart ep) {
        elementToEditPartsMap.unregisterEditPartForElement(elementIdStr, ep);
    }

	
	/** The work space preference store */
	private IPreferenceStore workspacePreferenceStore;
	
	
	/**
	 * The editor manages the workspaces preferences store. So viewers not using a editor
	 * do not need to create a preference store.  This method provides a hook for clients
	 * requiring access to the preference store.
	 * 
	 * @param store
	 */
	public void hookWorkspacePreferenceStore(IPreferenceStore store) {
		this.workspacePreferenceStore = store;
	}
	
	/**
	 * Returns the workspace preference store managed by the <code>DiagramEditor</code>,
	 * if one is being used. May return null.
	 * 
	 * @return the work space preference store
	 */
	public IPreferenceStore getWorkspaceViewerPreferenceStore() {
		return workspacePreferenceStore;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
	 */
	protected void unhookControl() {
		fireEmptySelection();
		super.unhookControl();
	}
}