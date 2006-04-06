/******************************************************************************
 * Copyright (c) 2004,2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal.clipboard;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;

/**
 * Used for image transfer to the clipboard. 
 * <p>SWT does not currently support image transfer to 
 * the Clipboard.  This utility class is provided in 
 * order to transfer an SWT Image to the system 
 * clipboard using AWT image transfer APIs.</p>
 * <p>A Transferable which implements the capability required to transfer a View.
 * This Transferable properly supports CUSTOMDATAFLAVOR.</p>
 * @author sshaw
 */
public class AWTViewImageTransferable
	implements Transferable {

	private ICustomData data = null;

	private Image image = null;

	/** DataFlavor. */
	static public DataFlavor AWTCUSTOMDATAFLAVOR = new DataFlavor(
		ICustomData.class, null);

	/**
	 * Constructor.
	 * 
	 * @param data
	 * @param image
	 */
	public AWTViewImageTransferable(ICustomData data, Image image) {
		this.data = data;
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {AWTCUSTOMDATAFLAVOR, DataFlavor.imageFlavor };
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.equals(AWTCUSTOMDATAFLAVOR) && data != null)
			|| (flavor.equals(DataFlavor.imageFlavor) && image != null);
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor)) {
			UnsupportedFlavorException ufe = new UnsupportedFlavorException(
				flavor);
			Trace.throwing(DiagramUIRenderPlugin.getInstance(),
				DiagramUIRenderDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"getTransferData()", //$NON-NLS-1$
				ufe);
			throw ufe;
		}

		if (flavor.equals(AWTCUSTOMDATAFLAVOR))
			return data;
		else if (flavor.equals(DataFlavor.imageFlavor))
			return image;

		return null;
	}
}