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
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import java.util.List;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Interface describing a element selection provider for the element selection
 * service.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public interface IElementSelectionProvider
	extends IProvider {

	/**
	 * Retrieve a list of matching objects from the provider.
	 * @param input the element selection input.
	 * @return A list of IMatchingObject.
	 */
	public List getMatchingObjects(IElementSelectionInput input);

	/**
	 * Resolve the matching object to a modeling object.
	 * @param object the matching object.
	 * @return a modeling object.
	 */
	public Object resolve(IMatchingObject object);
}
