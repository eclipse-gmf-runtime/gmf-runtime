/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

/**
 * This class is responsible for getting the contents from the clipboard.
 * 
 * @author cmahoney
 */
public class ClipboardContentsHelper {

	/**
	 * The singleton instance of <code>ClipboardContentsHelper</code>.
	 */
	private static ClipboardContentsHelper instance;

	/**
	 * Retrieves the singleton instance of <code>ClipboardContentsHelper</code>.
	 * 
	 * @return the singleton instance of <code>ClipboardContentsHelper</code>
	 */
	public static ClipboardContentsHelper getInstance() {
		if (instance == null) {
			instance = new ClipboardContentsHelper();
		}
		return instance;
	}

	/**
	 * Creates a new instance.
	 */
	protected ClipboardContentsHelper() {
		// nothing to initialize
	}

	/**
	 * Returns the system clipboard contents
	 * 
	 * @param dataType
	 *            The transfer agent
	 * @return Object Data associated with the transfer agent
	 */
	public Object getClipboardContents(Transfer dataType) {
		/* check the data type */
		assert null != dataType;

		/* create the clipboard instance */
		Clipboard clipboard = new Clipboard(Display.getCurrent());

		/* get the data from the clipboard */
		Object data = clipboard.getContents(dataType);

		/* free the system resources associated with the clipboard */
		clipboard.dispose();

		return data;
	}
}