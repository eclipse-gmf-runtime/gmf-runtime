/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.viewers.StructuredViewer;

/**
 * This interface is used to describe a workbench part that supports inline
 * editing.
 * 
 * @author ldamus
 */
public interface IInlineTextEditorPart {

	/**
	 * Start inline editing.
	 */
	public void startInlineEdit();

	/**
	 * sets the selection to the passed object and starts rename
	 * 
	 * @param object
	 *            the object to set selection on and rename
	 */
	void startInlineEdit(Object object);

	/**
	 * sets the selection to the passed object and starts the
	 * runnable to rename
	 * 
	 * @param object
	 *            the object to set selection on and rename
	 * @param editingRunnable a Runnable to invoke after setting selection on object
	 * in order to start renaming
	 */
	void startInlineEdit(Object object, Runnable editingRunnable);

	/**
	 * Gets the viewer for this part, in case the <code>RenameResourceAction</code> is to be used for
	 * inline editing.
	 * 
	 * @return the viewer, or <code>null</code> if there is none
	 */
	public StructuredViewer getViewer();

}