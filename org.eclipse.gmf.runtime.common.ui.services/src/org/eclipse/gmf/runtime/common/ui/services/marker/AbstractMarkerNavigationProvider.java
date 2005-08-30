/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.internal.marker.IMarkerNavigationProvider;

/**
 * This class saves the editor associated with the goto operation so that
 * utility methods in derived classes can access the editor instance before the
 * doGotoMarker() method is called.
 * 
 * @author Kevin Cornell
 */
public abstract class AbstractMarkerNavigationProvider
	extends AbstractProvider
	implements IMarkerNavigationProvider {

	/** Remember the editor associated with this goto operation. */
	private IEditorPart editor = null;

	/**
	 * Save the editor instance associated with the marker.
	 * 
	 * @param anEditor
	 *            the editor instance to be saved
	 */
	protected void setEditor(IEditorPart anEditor) {
		this.editor = anEditor;
	}

	/**
	 * Retrieve the saved editor instance.
	 * 
	 * @return the saved editor instance
	 */
	protected IEditorPart getEditor() {
		return editor;
	}

	/**
	 * Perform the marker navigation in a derived class.
	 * 
	 * @param marker
	 *            the IMarker to go to
	 */
	abstract protected void doGotoMarker(IMarker marker);

	/**
	 * Perform the feedback for navigating to the given marker.
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.internal.marker.IMarkerNavigationProvider#gotoMarker(org.eclipse.ui.IEditorPart,
	 *      org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IEditorPart edit, IMarker marker) {
		// Save the editor instance and call
		setEditor(edit);

		// Perform the marker navigation (feedback).
		doGotoMarker(marker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		return (operation instanceof GotoMarkerOperation);
	}

}