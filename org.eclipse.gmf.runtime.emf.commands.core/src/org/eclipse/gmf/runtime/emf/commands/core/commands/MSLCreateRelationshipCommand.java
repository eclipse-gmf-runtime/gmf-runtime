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

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * Command to create a relationship using MSL. Uses the first feature in the container
 * that can contain the new element if the containment feature is not specified.
 *
 * @author ldamus
 */
public class MSLCreateRelationshipCommand
	extends MSLCreateElementCommand {

	/**
	 * The relationship source.
	 */
	private final EObject source;

	/**
	 * The relationship target.
	 */
	private final EObject target;
	
	/**
	 * Constructs a new command to create a relationship using MSL.
	 *
	 * @param request
	 *            the request
	 */
	public MSLCreateRelationshipCommand(CreateRelationshipRequest request) {
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
