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

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.jface.viewers.IFilter;

/**
 * Filter to display a property section if the selection is a shape editpart
 * that has text compartment editpart children.
 * 
 * @author crevells
 */
public class TextLabelPropertySectionFilter
    implements IFilter {

    public boolean select(Object object) {
        if (object instanceof ShapeEditPart) {
            ShapeEditPart shapeEP = (ShapeEditPart) object;
            return !getNestedTextCompartmentEditParts(shapeEP).isEmpty();
        }
        return false;
    }

    private Collection getNestedTextCompartmentEditParts(
            IGraphicalEditPart containerEP) {
        HashSet textCompartmentEPs = new HashSet();
        for (Iterator iterator = containerEP.getChildren().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart childEP = (IGraphicalEditPart) iterator.next();
            if (childEP instanceof TextCompartmentEditPart) {
                textCompartmentEPs.add(childEP);
            }
            textCompartmentEPs
                .addAll(getNestedTextCompartmentEditParts(childEP));
        }
        return textCompartmentEPs;
    }
}
