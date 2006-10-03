/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class for operations around using a <code>GC</code>
 * @author sshaw
 */
public class GCUtilities {
	static private boolean checkedAdvancedGraphicsSupport = false;
	static private boolean platformSupportsAdvancedGraphics = false;
	
	/**
	 * @return <code>true</code> if the OS platform can support advanced graphics
	 * libraries (usually an externally installed library such as Cairo), or <code>false</code>
	 * otherwise.
	 */
	public static boolean supportsAdvancedGraphics() {
		if (!checkedAdvancedGraphicsSupport) {
			GC gc = new GC(new Shell());
			gc.setAdvanced(true);
			platformSupportsAdvancedGraphics = (gc.getAdvanced() == true);
			checkedAdvancedGraphicsSupport = true;
		}
		
		// Advanced graphics is disabled if the RTL orintation is ON
        // this is to avoid an SWT bug, on the GC object, where if the Advanced
        // graphics Mask is on it does not consider mirroring
		return platformSupportsAdvancedGraphics && Window.getDefaultOrientation()!=SWT.RIGHT_TO_LEFT;
	}
}
