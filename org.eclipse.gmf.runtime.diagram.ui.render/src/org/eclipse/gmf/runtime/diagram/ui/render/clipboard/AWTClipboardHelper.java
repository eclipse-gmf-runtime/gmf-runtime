/******************************************************************************
 * Copyright (c) 2004, 2021 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.clipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.clipboard.AWTViewImageTransferable;

/**
 * Used for image transfer to the clipboard. This is only supported when on Windows systems and running on Java before
 * Java 11, but can be overridden (forced enabled or disabled) by explicitly setting the
 * <code>org.eclipse.gmf.runtime.image.copy.supported</code> system property (see
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=566315).
 * <p>
 * SWT does not currently support image transfer to the Clipboard. This utility class is provided in order to transfer
 * an SWT Image to the system clipboard using AWT image transfer APIs.
 * </p>
 */
public class AWTClipboardHelper {
    
	private static final String OVERRIDE_PROPERTY = "org.eclipse.gmf.runtime.image.copy.supported"; //$NON-NLS-1$
    
	/**
	 * Are we running on Windows and system property 
	 * org.eclipse.gmf.runtime.image.copy.support is set to <code>true</code>?
	 */
	private static final boolean IMAGE_COPY_SUPPORTED = computeImageCopySupported();

	private static final AWTClipboardHelper INSTANCE = new AWTClipboardHelper();

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
     * Return true if image copy is supported.
     * 
     * By default this is only supported when on Windows systems and running on Java before Java 11, but can be
     * overridden (forced enabled or disabled) by explicitly setting the
     * <code>org.eclipse.gmf.runtime.image.copy.supported</code> system property. See
     * https://bugs.eclipse.org/bugs/show_bug.cgi?id=566315 for details.
     * 
     * Workaround for RATLC00526604, ClipboardHelper's hasCustomData invoking SunClipboard's getContents which waits
     * indefinitely
     * 
     * @return true if copying images to the clipboard is supported.
     */
	public final boolean isImageCopySupported() {
		return IMAGE_COPY_SUPPORTED;
	}

	private static final boolean computeImageCopySupported() {
		String imageCopySupported = System.getProperty(OVERRIDE_PROPERTY);
		if (imageCopySupported == null) {
		    boolean onWindows = System.getProperty("os.name").toUpperCase().startsWith("WIN"); //$NON-NLS-1$ //$NON-NLS-2$
		    String javaVersion = System.getProperty("java.specification.version"); //$NON-NLS-1$
		    boolean beforeJava11 = Arrays.asList("1.8", "9", "10").stream().anyMatch(javaVersion::equals); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		    return onWindows && beforeJava11; 
		} else {
		    // If the property is explicitly set, trust it.
		    return Boolean.parseBoolean(imageCopySupported);
		}
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
			Trace.catching(DiagramUIRenderPlugin.getInstance(),
				DiagramUIRenderDebugOptions.EXCEPTIONS_CATCHING,
				AWTClipboardHelper.class, "getCustomData", e); //$NON-NLS-1$
		} catch (IOException e) {
			Trace.catching(DiagramUIRenderPlugin.getInstance(),
                DiagramUIRenderDebugOptions.EXCEPTIONS_CATCHING,
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
