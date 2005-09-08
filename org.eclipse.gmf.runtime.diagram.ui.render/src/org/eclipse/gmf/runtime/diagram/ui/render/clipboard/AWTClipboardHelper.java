/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004,2005.  All Rights Reserved.               |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.clipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionPlugin;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.clipboard.AWTViewImageTransferable;

/**
 * Used for image transfer to the clipboard. This is only supported when on
 * Windows systems.
 * <p>
 * SWT does not currently support image transfer to the Clipboard. This utility
 * class is provided in order to transfer an SWT Image to the system clipboard
 * using AWT image transfer APIs.
 * </p>
 */
public class AWTClipboardHelper {

	/**
	 * Are we running on Windows?
	 */
	private static final boolean IMAGE_COPY_SUPPORTED = System.getProperty(
		"os.name").toUpperCase().startsWith("WIN"); //$NON-NLS-1$ //$NON-NLS-2$

	static private AWTClipboardHelper INSTANCE = new AWTClipboardHelper();

	/**
	 * Retrieves the singleton instance of <code>AWTClipboardHelper</code>.
	 * 
	 * @return the singleton instance of <code>AWTClipboardHelper</code>
	 */
	public static AWTClipboardHelper getInstance() {
		return INSTANCE;
	}

	private java.awt.datatransfer.Clipboard awtClipboard;
	
	private AWTClipboardHelper() {
		if (isImageCopySupported())
			awtClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	/**
	 * Return true if image copy is supported, that is, when running under Windows.
	 * 
	 * Workaround for RATLC00526604, ClipboardHelper's hasCustomData invoking
	 * SunClipboard's getContents which waits indefinitely
	 * 
	 * @return true when running under Windows 
	 */
	final public boolean isImageCopySupported() {
		return IMAGE_COPY_SUPPORTED;
	}

	/**
	 * Query method to determine if a <code>CustomData</code> object is on the
	 * clipboard.
	 * 
	 * @return true if CustomData is on the clipboard, false otherwise.
	 */
	public boolean hasCustomData() {
		assert (isImageCopySupported());
		Transferable transferable = getClipboard().getContents(null);
		if (transferable != null) {
			DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
			for (int i = 0; i < dataFlavors.length; i++) {
				if (dataFlavors[i]
					.equals(AWTViewImageTransferable.AWTCUSTOMDATAFLAVOR))
					return true;
			}
		}

		return false;
	}

	/**
	 * Accessor method to retrieve the <code>CustomData</code> object from the clipboard.
	 * 
	 * @return the <code>CustomData</code> if it exists on the clipboard, null otherwise.
	 */
	public CustomData getCustomData() {
		assert (isImageCopySupported());
		Transferable transferable = getClipboard().getContents(null);
		if (transferable == null)
			return null;

		if (!hasCustomData())
			return null;

		CustomData data = null;

		try {
			data = (CustomData) transferable
				.getTransferData(AWTViewImageTransferable.AWTCUSTOMDATAFLAVOR);
		} catch (UnsupportedFlavorException e) {
			Trace.catching(CommonUIActionPlugin.getDefault(),
				CommonUIActionDebugOptions.EXCEPTIONS_CATCHING,
				AWTClipboardHelper.class, "getCustomData", e); //$NON-NLS-1$
		} catch (IOException e) {
			Trace.catching(CommonUIActionPlugin.getDefault(),
				CommonUIActionDebugOptions.EXCEPTIONS_CATCHING,
				AWTClipboardHelper.class, "getCustomData", e); //$NON-NLS-1$
		}

		return data;
	}

	/**
	 * Copies an image to the system clipboard. Creates an AWT BufferedImage
	 * from the SWT Image and transfers the bufferedImage to the system
	 * clipboard.
	 * 
	 * @param data
	 *            the custom data
	 * @param image
	 *            the image to be copied
	 */
	public void copyToClipboard(CustomData data, Image image) {
		assert (isImageCopySupported());
		getClipboard().setContents(new AWTViewImageTransferable(data, image),
			null);
	}

	/**
	 * @return Returns the clipboard.
	 */
	private java.awt.datatransfer.Clipboard getClipboard() {
		return awtClipboard;
	}
}