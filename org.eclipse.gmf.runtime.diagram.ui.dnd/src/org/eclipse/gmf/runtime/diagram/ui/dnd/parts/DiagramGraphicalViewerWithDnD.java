/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.dnd.parts;

import org.eclipse.gef.dnd.DelegatingDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;


/**
 * @author tmacdoug
 *
 * Extension of the diagram graphical viewer to include drag and drop 
 * functionality
 * 
 */
public class DiagramGraphicalViewerWithDnD extends DiagramGraphicalViewer {

    /* 
     * Attribute for the drop adapter
     */
    private final DiagramViewerDropAdapter dropAdapter;	
	
	/**
	 * Constructor
	 * 
	 * @param dropAdapter
	 *            The dropAdapter to set.
	 */
	public DiagramGraphicalViewerWithDnD(DiagramViewerDropAdapter dropAdapter) {
		super();
		this.dropAdapter = dropAdapter;
	}

    /* 
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#getDelegatingDropAdapter()
     */
    protected DelegatingDropAdapter getDelegatingDropAdapter() {
    	if ( this.dropAdapter == null ) {
    		return super.getDelegatingDropAdapter();
    	}
    	
    	return this.dropAdapter;
    }

	/* 
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#refreshDropTargetAdapter()
	 */
	protected void refreshDropTargetAdapter() {
		if (getControl() == null)
			return;
		if (getDelegatingDropAdapter() == null)
			setDropTarget(null);
		else {
			if (getDropTarget() == null)
				setDropTarget(
					new DropTarget(
						getControl(),
						DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK));
			
			if (this.dropAdapter != null && !this.dropAdapter.isInitialized())
				this.dropAdapter.initialize(getControl());		

			getDropTarget().setTransfer(getDelegatingDropAdapter().getTransfers());
		}
	}  
	
	
}
