/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The operation used with the modeling assistant service that gets the type of
 * elements that can be created or used for a specific hint or gesture and optional data.
 * 
 * @author cmahoney
 */
public class GetTypesOperation
	implements IModelingAssistantOperation {
	
	/** identifies the gesture */
	private final String hint;

	/** additional data required */
	private final IAdaptable data;

	/**
	 * Creates a new <code>GetTypesOperation</code>.
	 * 
	 * @param hint
	 *            identifies the hint or gesture
	 * @param data
	 *            <li>provides additional data that is required</li>
	 *            <li>may be null depending on the hint</li>
	 */
	protected GetTypesOperation(String hint, IAdaptable data) {
		this.hint = hint;
		this.data = data;
	}

	/**
	 * The ID for a this operation is the hint itself.
	 */
	public String getId() {
		return getHint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getContext()
	 */
	public IAdaptable getContext() {
		return getData();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider)
			.getTypes(getHint(), getData());
	}
	
	/**
	 * Gets the hint that identifies the gesture.
	 * 
	 * @return the hint
	 */
	public String getHint() {
		return hint;
	}
	
	/**
	 * Gets the additional data required (if applicable). 
	 * @return the data, may be null.
	 */
	public IAdaptable getData() {
		return data;
	}

}