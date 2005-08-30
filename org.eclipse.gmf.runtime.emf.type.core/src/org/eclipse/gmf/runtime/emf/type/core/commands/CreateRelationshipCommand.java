/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.commands;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * Command to create a new relationship element using the EMF action protocol.
 * <P>
 * If the containment feature is not specified in the request, the first feature
 * in the container that can contain the new kind of relationship will be used.
 * 
 * @author ldamus
 */
public class CreateRelationshipCommand
	extends CreateElementCommand {

	/**
	 * The relationship source.
	 */
	private final EObject source;

	/**
	 * The relationship target.
	 */
	private final EObject target;

	/**
	 * Constructs a new element creation command for the <code>request</code>.
	 * 
	 * @param request
	 *            the element creation request
	 */
	public CreateRelationshipCommand(CreateRelationshipRequest request) {

		super(request);

		this.source = request.getSource();
		this.target = request.getTarget();
	}

	/**
	 * Gets the relationship source.
	 * 
	 * @return the relationship source
	 */
	public EObject getSource() {
		return source;
	}

	/**
	 * Gets the relationship target.
	 * 
	 * @return the relationship target
	 */
	public EObject getTarget() {
		return target;
	}

	protected ConfigureRequest createConfigureRequest() {

		ConfigureRequest result = super.createConfigureRequest();
		result.setParameter(CreateRelationshipRequest.SOURCE, getSource());
		result.setParameter(CreateRelationshipRequest.TARGET, getTarget());
		return result;
	}

	public boolean isExecutable() {
		return getSource() != null && getTarget() != null
			&& super.isExecutable();
	}
}