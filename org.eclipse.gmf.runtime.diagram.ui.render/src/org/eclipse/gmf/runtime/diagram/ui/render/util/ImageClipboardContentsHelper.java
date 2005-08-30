/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.util;

import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardContentsHelper;
import org.eclipse.gmf.runtime.common.ui.util.CustomDataTransfer;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper;

/**
 * A specialized <code>ClipboardContentsHelper</code> that supports images in
 * the clipboard.
 * 
 * <p>
 * This is only supported on Windows
 * {@link org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper}.
 * </p>
 * 
 * @author cmahoney
 */
public class ImageClipboardContentsHelper
	extends ClipboardContentsHelper {

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
			instance = new ImageClipboardContentsHelper();
		}
		return instance;
	}

	/**
	 * Creates a new instance.
	 */
	protected ImageClipboardContentsHelper() {
		// nothing to initialize
	}
	
	/**
	 * Returns the system clipboard contents with image support.
	 */
	public Object getClipboardContents(Transfer dataType) {
		Object data = super.getClipboardContents(dataType);

		if ((data == null)
			&& (CustomDataTransfer.getInstance().equals(dataType))
			&& (AWTClipboardHelper.getInstance().isImageCopySupported())) {
			data = AWTClipboardHelper.getInstance().getCustomData();
			if (data instanceof ICustomData) {
				data = new ICustomData[] {(ICustomData) data};
			}
		}

		return data;
	}
}