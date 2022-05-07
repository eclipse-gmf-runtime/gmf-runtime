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

package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * Advice that includes the client's dependent annotations in the destruction
 * of a client.
 * 
 * @author Christian W. Damus (cdamus)
 */
public class ClientDependentsAdvice
	extends AbstractEditHelperAdvice {
	
	public static String INITIAL = "org.eclipse.gmf.tests.runtime.emf.type.core.initial"; //$NON-NLS-1$

	private static EReference[] ANNOTATION_REFERENCES = new EReference[] {
		EcorePackage.Literals.EANNOTATION__REFERENCES};
	
	protected ICommand getBeforeDestroyDependentsCommand(DestroyDependentsRequest request) {
		ICommand result = null;
		
		Collection referencers = EMFCoreUtil.getReferencers(
				request.getElementToDestroy(),
				ANNOTATION_REFERENCES);
		
		for (Iterator iter = referencers.iterator(); iter.hasNext();) {
			EAnnotation ann = (EAnnotation) iter.next();
			
			// could return a null command if the element is already being destroyed
			ICommand command = request.getDestroyDependentCommand(ann);
			
			if (command != null) {
				if (result == null) {
					result = command;
				} else {
					result = result.compose(command);
				}
			}
		}
		
		// store the initial element to destroy in the INITIAL parameter to verify that
		// the correct initial element was found in the advice
		Object initial = request.getParameter(INITIAL);
		if (initial == null) {
			request.setParameter(INITIAL,
					request.getParameter(DestroyElementRequest.INITIAL_ELEMENT_TO_DESTROY_PARAMETER));
		}
			
		
		return result;
	}
}
