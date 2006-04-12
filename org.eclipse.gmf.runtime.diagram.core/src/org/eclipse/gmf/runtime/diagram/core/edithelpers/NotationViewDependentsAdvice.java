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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;
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
	protected ICommand getBeforeDestroyDependentsCommand(DestroyDependentsRequest request) {
		EObject destructee = request.getElementToDestroy();
		ICommand result = null;

		// handle views referencing a semantic element being destroyed
		
        Collection semanticReferencers = EMFCoreUtil.getReferencers(
                destructee,
                new EReference[] {NotationPackage.Literals.VIEW__ELEMENT});

        result = CompositeCommand.compose(
        		result,
        		request.getDestroyDependentsCommand(semanticReferencers));

        // handle the node entries for views
        
        if (destructee instanceof View) {
            Collection nodeEntryKeys = EMFCoreUtil.getReferencers(
            		destructee,
            		new EReference[] {NotationPackage.Literals.NODE_ENTRY__KEY});

            result = CompositeCommand.compose(
            		result,
            		request.getDestroyDependentsCommand(nodeEntryKeys));
        }
        
        // handle the edges connected to nodes or other edges
        
        if (destructee instanceof Node || destructee instanceof Edge) {
        	View view = (View) destructee;
        	
            result = CompositeCommand.compose(
            		result,
            		request.getDestroyDependentsCommand(view.getSourceEdges()));
        	
            result = CompositeCommand.compose(
            		result,
            		request.getDestroyDependentsCommand(view.getTargetEdges()));
        }
        
 		return result;
	}
}
