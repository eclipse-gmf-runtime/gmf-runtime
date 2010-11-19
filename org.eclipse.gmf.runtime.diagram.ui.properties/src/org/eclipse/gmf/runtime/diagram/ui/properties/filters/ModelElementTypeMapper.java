/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.filters;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

/**
 * Create a filter that is used assist in input filtering for the
 * propertySection extension point.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public class ModelElementTypeMapper
	extends AbstractTypeMapper {

	/**
	 * Constructor for ModelElementTypeMapper.
	 */
	public ModelElementTypeMapper() {
		super();
	}


    /**
     * @inheritDoc
     */
    public Class mapType(Object input) {
        Class type = super.mapType(input);
        if (input instanceof EditPart) {
            Object tmpObj = ((EditPart) input).getModel();
            if (tmpObj instanceof EObject) {
                type = getEObjectType((EObject) tmpObj);
            } else {
                type = tmpObj.getClass();
            }
        } else if (input instanceof EObject) {
            return getEObjectType((EObject) input);
        }

        return type;
    }

	/**
	 * Returns the type of the EObject. Subclasses may override.
	 * 
	 * @param eObj
	 *            EObject whose type is being examined
	 * @return Type of the EObject
	 */
	protected Class getEObjectType(EObject eObj) {
		return eObj.getClass();
	}
}
