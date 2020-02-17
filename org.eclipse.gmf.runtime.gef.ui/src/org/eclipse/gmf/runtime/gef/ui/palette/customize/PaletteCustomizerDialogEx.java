/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.palette.customize;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * GMF's version of the <code>PaletteCustomizerDialog</code> overridden to:
 * <li>Remove the toolbar items. We do not support creating new entries for the
 * palette, deleting entries (because there is no way the user can get them
 * back), and reordering entries (because how would we determine the order after
 * the entries changed based on capability enablement?).</li>
 * 
 * @author crevells
 */
public class PaletteCustomizerDialogEx
    extends PaletteCustomizerDialog {

    public PaletteCustomizerDialogEx(Shell shell, PaletteCustomizer customizer,
            PaletteRoot root) {
        super(shell, customizer, root);
    }

    protected List createOutlineActions() {
        return Collections.EMPTY_LIST;
    }

    protected Control createOutlineToolBar(Composite parent) {
        if (createOutlineActions().isEmpty()) {
            return null;
        }
        return super.createOutlineToolBar(parent);
    }

}
