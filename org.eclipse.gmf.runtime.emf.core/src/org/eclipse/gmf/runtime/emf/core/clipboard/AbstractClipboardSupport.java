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


package org.eclipse.gmf.runtime.emf.core.clipboard;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;


/**
 * A partial implementation of the {@link IClipboardSupport} interface that
 * uses MSL facilities to implement some of the metamodel-specific hooks that
 * MSL, itself, provides support for.
 *
 * @author Christian W. Damus (cdamus)
 */
public abstract class AbstractClipboardSupport implements IClipboardSupport {

	/**
	 * Initializes me.
	 */
	public AbstractClipboardSupport() {
		super();
	}

	/**
	 * MSL has extensions for metamodels to indicate which objects can have
	 * names.
	 */
	public boolean isNameable(EObject eObject) {
		return PackageUtil.getNameAttribute(eObject.eClass()) != null;
	}
	
	/**
	 * MSL has extensions for metamodels to get object names.
	 */
	public String getName(EObject eObject) {
		return EMFCoreUtil.getName(eObject);
	}
	
	/**
	 * MSL has extensions for metamodels to set object names.
	 */
	public void setName(EObject eObject, String name) {
		if (!isNameable(eObject)) {
			throw new IllegalArgumentException("eObject not nameable"); //$NON-NLS-1$
		}
		
		EMFCoreUtil.setName(eObject, name);
	}

	public XMLResource getResource(EObject eObject) {
		return (XMLResource) eObject.eResource();
	}
	
	/**
	 * MSL implements creation notifications.
	 */
	public void sendCreateNotification(EObject eObject) {
		Resource res = eObject.eResource();
		
		if (res != null) {
			Notification createNotification = new ENotificationImpl(
				(InternalEObject) eObject, 0, // classical CREATE event type
				(EStructuralFeature) null, (Object) null, (Object) null, -1);
			
			eObject.eNotify(createNotification);
		}
	}
	
	/**
	 * MSL has extensions for metamodels to implement containment conditions.
	 */
	public boolean canContain(EObject container, EReference reference,
			EClass containedType) {
		return PackageUtil.canContain(
			container.eClass(),
			reference,
			containedType,
			false);  // not recursive
	}
}
