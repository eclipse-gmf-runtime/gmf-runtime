/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

/**
 * Advice that includes the client's dependent annotations in the destruction
 * of a client.
 * 
 * @author Christian W. Damus (cdamus)
 */
public class ClientDependentsAdvice
	extends AbstractEditHelperAdvice {

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
			
		return result;
	}
}
