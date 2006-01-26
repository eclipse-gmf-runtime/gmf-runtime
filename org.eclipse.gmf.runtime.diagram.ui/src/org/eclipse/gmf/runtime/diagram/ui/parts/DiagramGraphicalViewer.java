/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.parts;

import java.util.List;

import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.ElementToEditPartsMap;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


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
	 * @param enable <code>boolean</code> <code>true</code> if client wishes to disable
	 * updates on the figure canvas, <code>false</code> indicates normal updates are
	 * to take place.
	 */
	public void enableUpdates(boolean enable) {
		if (enable)
			getLightweightSystemWithUpdateToggle().enableUpdates();
		else
			getLightweightSystemWithUpdateToggle().disableUpdates();	
	}
	
	private class ToggleUpdateManager extends DeferredUpdateManager {

		private boolean disableUpdates = false;
		
		/**
		 * @return the disableUpdates
		 */
		public boolean shouldDisableUpdates() {
			return disableUpdates;
		}

		/**
		 * @param disableUpdates the disableUpdates to set
		 */
		public void setDisableUpdates(boolean disableUpdates) {
			this.disableUpdates = disableUpdates;
			if (!disableUpdates) {
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						queueWork();
					}
				});
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.DeferredUpdateManager#performUpdate()
		 */
		public synchronized void performUpdate() {
			if (!shouldDisableUpdates())
				super.performUpdate();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.DeferredUpdateManager#performValidation()
		 */
		public void performValidation() {
			if (!shouldDisableUpdates())
				super.performValidation();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.DeferredUpdateManager#queueWork()
		 */
		public void queueWork() {
			if (!shouldDisableUpdates())
				super.queueWork();
		}
	}

	private class LightweightSystemWithUpdateToggle extends LightweightSystem {
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.draw2d.LightweightSystem#getUpdateManager()
		 */
		public ToggleUpdateManager getToggleUpdateManager() {
			return (ToggleUpdateManager)getUpdateManager();
		}
		
		/**
		 * disable updates on the figure canvas
		 */
		public void disableUpdates() {
			getToggleUpdateManager().setDisableUpdates(true);
		}
		
		/**
		 * allow updates on the figure canvas to occcur
		 */
		public void enableUpdates() {
			getToggleUpdateManager().setDisableUpdates(false);
		}
	}
	
	private LightweightSystemWithUpdateToggle getLightweightSystemWithUpdateToggle() {
		return (LightweightSystemWithUpdateToggle)getLightweightSystem();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#createLightweightSystem()
	 */
	protected LightweightSystem createLightweightSystem() {
		LightweightSystem lws = new LightweightSystemWithUpdateToggle();
		lws.setUpdateManager(new ToggleUpdateManager());
		return lws;
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