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

package org.eclipse.gmf.runtime.diagram.core.edithelpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.CrossReferenceAdapter;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Edit helper advice for the {@link DestroyDependentsRequest} that destroys
 * notations views under the following circumstances:
 * <ul>
 *   <li>element being destroyed is the view's semantic referent</li>
 *   <li>element being destroyed is a Node or Edge to which an Edge is connected</li>
 * </ul>
 *
 * @author Christian W. Damus (cdamus)
 */
public class NotationViewDependentsAdvice extends AbstractEditHelperAdvice {

	public ICommand getBeforeEditCommand(IEditCommandRequest request) {
		if (request instanceof DestroyDependentsRequest) {
			return getBeforeDestroyDependentsCommand((DestroyDependentsRequest) request);
		}
		return null;
	}
	
	public ICommand getAfterEditCommand(IEditCommandRequest request) {		
		return null;
	}
	
	protected ICommand getBeforeDestroyDependentsCommand(
			DestroyDependentsRequest request) {
		
		EObject destructee = request.getElementToDestroy();
		CrossReferenceAdapter crossReferenceAdapter = getCrossReferenceAdapter(request, destructee);
		ICommand result = getDestroyDependentsCommand(destructee, request,
				NotationPackage.Literals.VIEW__ELEMENT, crossReferenceAdapter);
		// handle the node entries for views
		if (destructee instanceof View) {
			result = CompositeCommand.compose(result, getDestroyDependentsCommand(destructee, request,
				NotationPackage.Literals.NODE_ENTRY__KEY, crossReferenceAdapter));

			//  handle the edges connected to nodes or other edges        
			if (destructee instanceof Node || destructee instanceof Edge) {
				View view = (View) destructee;

				if (view.eIsSet(NotationPackage.Literals.VIEW__SOURCE_EDGES)) {
					result = CompositeCommand.compose(result, request
						.getDestroyDependentsCommand(view.getSourceEdges()));
				}
				if (view.eIsSet(NotationPackage.Literals.VIEW__TARGET_EDGES)) {
					result = CompositeCommand.compose(result, request
						.getDestroyDependentsCommand(view.getTargetEdges()));
				}
			}			
		}
		return result;
	}
	
	private CrossReferenceAdapter getCrossReferenceAdapter(
			DestroyDependentsRequest request, EObject destructee) {
		
		CrossReferenceAdapter crossReferenceAdapter = null;
		Map cacheMaps = (Map) request.getParameter("Cache_Maps");//$NON-NLS-1$ RequestCacheEntries.Cache_Maps
		if (cacheMaps != null) {
			crossReferenceAdapter = (CrossReferenceAdapter) cacheMaps
					.get("CrossRefAdapter");//$NON-NLS-1$ RequestCacheEntries.CrossRefAdapter
		}

		if (crossReferenceAdapter == null) {
			crossReferenceAdapter = CrossReferenceAdapter
					.getExistingCrossReferenceAdapter(destructee);
			if (crossReferenceAdapter == null) {
				TransactionalEditingDomain domain = TransactionUtil
						.getEditingDomain(destructee);
				if (domain != null) {
					crossReferenceAdapter = CrossReferenceAdapter
							.getCrossReferenceAdapter(domain.getResourceSet());
				}
			}
		}
		return crossReferenceAdapter;
	}
	
	
	private ICommand getDestroyDependentsCommand(EObject destructee,
			DestroyDependentsRequest request, EReference eRef, CrossReferenceAdapter crossReferenceAdapter) {
		
		if (crossReferenceAdapter != null) {
			Collection revRefs = crossReferenceAdapter
				.getNonNavigableInverseReferences(destructee);
			if (revRefs.isEmpty() == false) {
				Set set = null;
				Iterator it = revRefs.iterator();
				while (it.hasNext()) {
					Setting setting = (Setting) it.next();
					if (setting.getEStructuralFeature() == eRef) {
						if (set == null) {
							set = new HashSet();
						}
						set.add(setting.getEObject());
					}
				}

				if (set != null) {
					return request.getDestroyDependentsCommand(set);
				}
			}
		}

		return null;
	}
}
