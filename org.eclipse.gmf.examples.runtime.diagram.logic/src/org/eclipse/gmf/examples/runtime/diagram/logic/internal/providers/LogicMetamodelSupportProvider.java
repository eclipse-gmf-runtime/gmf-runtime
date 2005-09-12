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
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.GetMetamodelSupportOperation;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupportProvider;
import org.eclipse.gmf.runtime.notation.providers.internal.semproc.NotationSemProc;

/**
 * This metamodel support provider exists so that notation semantic procedures will
 *  be fired when events occur on logic semantic elements.
 *  
 *  @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.DeleteSemanticAction
 */
public class LogicMetamodelSupportProvider extends AbstractProvider
	implements IMetamodelSupportProvider {

	private IMetamodelSupport metaModel = null;
	
	public IMetamodelSupport getMetamodelSupport(EPackage ePackage) {
		if (metaModel == null) 
			metaModel = new LogicMetamodelSupport();
		
		return metaModel;
	}

	public boolean provides(IOperation operation) {
		if (operation instanceof GetMetamodelSupportOperation) {

			GetMetamodelSupportOperation getMetaModelOperation = (GetMetamodelSupportOperation) operation;

			EPackage ePackage = getMetaModelOperation.getEPackage();

			return (ePackage == SemanticPackage.eINSTANCE);
		}

		return false;
	}

	public class LogicMetamodelSupport implements IMetamodelSupport {
		public boolean canDestroy(EObject eObject) {
			// No restrictions on destroy
			return true;
		}

		public boolean canContain(EClass eContainer, EReference eReference, EClass eClass) {
			// No restrictions on containment
			return true;
		}

		public void handleEvent(Notification event) {
			// Call the notation semantic procedures in case there are procedures
			//  for changes in our metamodel.
			NotationSemProc.handleEvent(event);
		}

		public void postProcess(EObject root) {
			// No post processing required
		}
	}
}
