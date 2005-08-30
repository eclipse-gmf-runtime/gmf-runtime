/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.notation.providers.internal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;
import org.eclipse.gmf.runtime.notation.providers.internal.semproc.NotationSemProc;

/**
 * Implementation of Notation meta-model extensions.
 * 
 * @author rafikj
 */
public class NotationMetaModel
	implements IMetamodelSupport {

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.services.metamodel.IMetamodelSupport#canDestroy(org.eclipse.emf.ecore.EObject)
	 */
	public boolean canDestroy(EObject eObject) {

		if (eObject == null)
			return false;

		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.services.metamodel.IMetamodelSupport#canContain(org.eclipse.emf.ecore.EClass,
	 *      org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EClass)
	 */
	public boolean canContain(EClass eContainer, EReference eReference,
			EClass eClass) {
		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.services.metamodel.IMetamodelSupport#handleEvent(org.eclipse.emf.common.notify.Notification)
	 */
	public void handleEvent(Notification event) {
		NotationSemProc.handleEvent(event);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.services.metamodel.IMetamodelSupport#postProcess(org.eclipse.emf.ecore.EObject)
	 */
	public void postProcess(final EObject root) {
		// do nothing.
	}
}