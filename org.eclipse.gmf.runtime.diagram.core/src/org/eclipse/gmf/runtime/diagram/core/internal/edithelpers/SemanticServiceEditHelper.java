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

package org.eclipse.gmf.runtime.diagram.core.internal.edithelpers;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.SemanticRequestTranslator;
import org.eclipse.gmf.runtime.emf.commands.core.edithelpers.MSLEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Edit helper that delegates to the semantic service to get its commands.
 * <P>
 * Note that
 * {@link org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.ElementTypeSemanticProvider}
 * is registered as the lowest-priority semantic provider. It will call back
 * into the Element Type framework to get its commands. If this edit helper is
 * consulted while it is waiting for a command from the semantic service, it
 * will call the superclass to get the command. This prevents the infinite loop
 * that would otherwise result from the interaction between this edit helper and
 * the <code>elementTypeSemanticProvider</code>.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 * 
 * @deprecated this is a temporary class that exists to support new clients of
 *             the Element Type API {@link org.eclipse.gmf.runtime.emf.type.core}while the
 *             semantic providers have not yet been migrated to the new
 *             ElementType API.
 */
public class SemanticServiceEditHelper
	extends MSLEditHelper {

	private boolean requestInProgress;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edit.AbstractEditHelper#getEditCommand(org.eclipse.gmf.runtime.emf.type.core.edit.IEditCommandRequest)
	 */
	public ICommand getEditCommand(IEditCommandRequest req) {

		ICommand result = null;

		// Try to translate the request.
		SemanticRequest semanticRequest = SemanticRequestTranslator
			.translate(req);

		if (semanticRequest == null) {
			// If we can't get a matching semantic request, consult
			// the superclass for the command.
			return super.getEditCommand(req);

		} else {
			if (requestInProgress) {
				if (req instanceof MoveRequest || req instanceof DestroyRequest) {
					// We've been called while we were asking for a command
					// from the semantic service. Only consult the superclass for
					// move and destroy requests (these were formerly handled by the
					// PresentationSemanticProvider). Otherwise, the request was
					// formerly not supported so we shouldn't return a command to
					// honour the request.
					result = super.getEditCommand(req);
				}

			} else {
				try {
					requestInProgress = true;
					// Consult the semantic service
					result = SemanticService.getInstance().getCommand(
						semanticRequest);

				} finally {
					requestInProgress = false;
				}
			}
		}

		return result;
	}

	/**
	 * Figures out what container to use when creating a new element using the
	 * <code>SemanticService</code>.
	 * <P>
	 * If we're creating a relationship, arbitrarily use the source as the
	 * container. The semantic service does not require the correct container,
	 * but the container is used to find the edit helper from which to get the
	 * command.
	 * <P>
	 * If we're creating an element and the container is a <code>Diagram</code>,
	 * use the diagram's element as the new container.
	 */
	protected ICommand getEditContextCommand(GetEditContextRequest req) {
		GetEditContextCommand result = new GetEditContextCommand(req);

		IEditCommandRequest editRequest = req.getEditCommandRequest();
		
		if (editRequest instanceof CreateRelationshipRequest) {
			// Need an arbitrary container from which to get the edit helper
			// with which to create the new relationship.
			CreateRelationshipRequest createRequest = (CreateRelationshipRequest) editRequest;
			EObject container = createRequest.getContainer();

			if (container == null) {
				container = createRequest.getSource();
			}
			result.setEditContext(container);

		} else if (editRequest instanceof CreateElementRequest){
			// Set the container to be the diagram's element, if the container
			// is a diagram
			CreateElementRequest createRequest = (CreateElementRequest) editRequest;
			EObject container = createRequest.getContainer();

			if (container instanceof Diagram) {
				EObject element = ((Diagram) container).getElement();
				
				if (element == null) {
					// Element is null if the diagram was created using the new model wizard
					EObject annotation = ((Diagram) container).eContainer();
					
					if (annotation != null) {
						element = annotation.eContainer();
					}
				}
				if (element != null) {
					container = element;
				}
			}
			result.setEditContext(container);
		}

		return result;
	}
}