/******************************************************************************
 * Copyright (c) 2004-2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * The MResource interface can be implemented by custom Resource
 * implementations. This will allow clients to change the behavior of certain
 * MSL functions.
 * 
 * @author rafikj
 */
public interface MResource {

	/**
	 * Override and return a custom implementation of Helper.
	 */
	public Helper getHelper();

	public class Helper {

		// Override to alter the behavior of object creation.
		public EObject create(MEditingDomain domain, EClass eClass) {
			return MSLUtil.create((MSLEditingDomain) domain, eClass, false);
		}

		// Override to alter the behavior of object destruction.
		public void destroy(MEditingDomain domain, EObject eObject) {
			MSLUtil.destroy((MSLEditingDomain) domain, eObject, 0);
		}

		// Override to alter the behavior of object identification.
		public String getID(EObject eObject) {
			return MSLUtil.getID(eObject);
		}

		// Override to alter the behavior of object identification.
		public void setID(EObject eObject, String id) {
			MSLUtil.setID(eObject, id);
		}

		// Override to alter the behavior of assigning types to objects.
		public MObjectType getType() {
			return MObjectType.MODELING;
		}

		// Override to alter the behavior of getting object contents.
		public Collection getContents(EObject eObject) {
			return eObject.eContents();
		}

		// Override to alter the behavior of object modifyability.
		public boolean isModifiable(Resource resource) {
			return MSLUtil.isModifiable(resource);
		}

		// Override to alter the behavior of reference registration.
		/**
		 * @deprecated No longer necessary to register references. The cross
		 * reference adapter takes care of this. 
		 */
		public void registerReferences(MEditingDomain domain, EObject eObject,
				EReference reference, List newObjects, List oldObjects) {
			// empty
		}
	}
}