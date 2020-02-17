/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.filters;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.jface.viewers.IFilter;

/**
 * Filter to display a property section if the selection is a connection edit part.
 * 
 * @author Anthony Hunter
 */
public class ConnectionEditPartPropertySectionFilter
	implements IFilter {

    public boolean select(Object object) {
        if (object instanceof ConnectionEditPart)
            return true;

        return false;
    }
}