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

package org.eclipse.gmf.runtime.emf.core.internal.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * A constraint that enforces universally unique IDs (UUIDs), that is, it fails
 * if multiple eObjects have the same UUID.
 * 
 * @author Scott Cowan
 */
public class UUIDConstraint
	extends AbstractModelConstraint {

	/** The rule ID for ambiguous ID errors */
	public static final String RULE_ID = "org.eclipse.gmf.runtime.emf.core.UUIDConstraint"; //$NON-NLS-1$

	public UUIDConstraint() {
		super();
	}

	/**
	 * Return a label for an eObject.
	 * 
	 * @param eObject
	 *            an eObject
	 * @return a label
	 */
	private String getEObjectLabel(EObject eObject) {
		return EObjectUtil.getQName(eObject, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	public IStatus validate(IValidationContext ctx) {

		EObject target = ctx.getTarget();
		Resource resource = target.eResource();

		if (!(resource instanceof XMLResource))
			return ctx.createSuccessStatus();

		// Fail constraint if target is the key of an eObject to ID map entry,
		// but not the value of an ID to eObject map entry.
		XMLResource xmlResource = (XMLResource) resource;
		String id = xmlResource.getID(target);

		EObject eObject = xmlResource.getEObject(id);
		if (eObject != target) {
			return ctx
				.createFailureStatus(new Object[] {getEObjectLabel(target)});
		}

		return ctx.createSuccessStatus();
	}
}
