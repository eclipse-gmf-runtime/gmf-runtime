/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.filters;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

/**
 * Create a filter that is used assist in input filtering for the
 * propertySection extension point. 
 * <p>
 * Elements in this filter are assumed to be Modeling EObjects. 
 * 
 * @author nbalaba
 */
public class GenericModelElementFilter
	extends AbstractTypeMapper {

	/**
	 * Converts the input into an EObject, if it can be adapted. Otherwise
	 * returns null.
	 * 
	 * @param input
	 *            Input object to be converted to an EObject
	 * @return EObject converted from the input object or null if the input
	 *         object cannot be converted
	 */
	protected EObject getEObject(Object input) {

		if (input instanceof IAdaptable) {
			EObject eObj = (EObject) ((IAdaptable) input)
				.getAdapter(EObject.class);
			if (eObj != null && isSupportedMObjectType(eObj)) {
				return eObj;
			} else {
				return null;
			}
		}

		return null;
	}

	
	/**
	 * @inheritDoc
	 */
	public Class mapType(Object input) {
		EObject object = getEObject(input);

		if (isApplicableToEObject(object))
			return object.getClass();

		return super.mapType(object);
	}

	/**
	 * The generic filter already has been applied during 'remap' filtering
	 * phase. If it got to this point - it is fine, always true (except when
	 * null) - the underlaying model element type of the input will be returned.
	 * Subclasses can override to filter out more specific element types
	 * 
	 * @param object object to check
	 * @return <code>true</code> is filter is applicable to specified <code>EObject</code>, <code>false</code> otherwise
	 */
	protected boolean isApplicableToEObject(EObject object) {
		// the object can still be null after the model resolution - it may not
		// be a model element
		return object != null;
	}

	/**
	 * Checks if the MObjectType of the EObject is supported. This
	 * implementation excludes all types except MODELING. Subclasses may
	 * override.
	 * 
	 * @param obj
	 *            EObject whose type is to be tested. Must not be null.
	 * @return <code>true</code> if the type is supported; otherwise <code>false</code>
	 */
	protected boolean isSupportedMObjectType(EObject obj) {
		return EObjectUtil.getType(obj) == MObjectType.MODELING;
	}
}