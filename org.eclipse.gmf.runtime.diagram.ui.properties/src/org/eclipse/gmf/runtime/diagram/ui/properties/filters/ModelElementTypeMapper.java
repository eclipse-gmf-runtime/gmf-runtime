/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import org.eclipse.wst.common.ui.properties.internal.provisional.ITypeMapper;

/**
 * Create a filter that is used assist in input filtering for the
 * propertySection extension point.
 * 
 * @author Anthony Hunter <a
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.properties.*
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public class ModelElementTypeMapper
	implements ITypeMapper {

	/**
	 * Constructor for ModelElementTypeMapper.
	 */
	public ModelElementTypeMapper() {
		super();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITypeMapper#remapType(java.lang.Object, java.lang.Class)
	 */
	public Class remapType(Object input, Class effectiveType) {
		Class type = effectiveType;
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
