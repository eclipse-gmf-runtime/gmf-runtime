/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class for operations around using a <code>GC</code>
 * 
 * @author sshaw, crevells
 */
public class GCUtilities {

    static private boolean checkedAdvancedGraphicsSupport = false;

    static private boolean platformSupportsAdvancedGraphics = false;


    /**
     * Returns true is advanced graphics is supported in this environment. This
     * includes the OS supporting an advanced graphics library (usually an
     * externally installed library such as Cairo or GDI+)
     * 
     * @return true if advanced graphics is supported; false otherwise
     */
    public static boolean supportsAdvancedGraphics() {
        if (!checkedAdvancedGraphicsSupport) {
            Shell shell = new Shell();
            GC gc = new GC(shell);
            gc.setAdvanced(true);
            platformSupportsAdvancedGraphics = (gc.getAdvanced() == true);
            checkedAdvancedGraphicsSupport = true;
            gc.dispose();
            shell.dispose();
        }

        return platformSupportsAdvancedGraphics;
    }
}
