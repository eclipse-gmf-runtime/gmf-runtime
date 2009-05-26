/******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import org.eclipse.gef.Tool;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;

/**
 * @author crevells
 */
public class LabelPaletteFactory
    extends PaletteFactory.Adapter {

    public Tool createTool(String toolId) {

        if (toolId.equals(LabelConstants.ID_WRAPPINGLABEL)) {
            return new CreationTool(LabelNotationType.WRAPPINGLABEL_NOTE);
        } else if (toolId.equals(LabelConstants.ID_GEFLABEL)) {
            return new CreationTool(LabelNotationType.GEFLABEL_NOTE);
        } else if (toolId.equals(LabelConstants.ID_WRAPLABEL)) {
            return new CreationTool(LabelNotationType.WRAPLABEL_NOTE);
        } else if (toolId.equals(LabelConstants.ID_OLDWRAPLABEL)) {
            return new CreationTool(LabelNotationType.OLDWRAPLABEL_NOTE);
        }

        return null;
    }

}
