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

package org.eclipse.gmf.runtime.emf.type.core.internal;

import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;

/**
 * Request parameter names for parameters used internally by the EMF Type Core
 * plug-in.
 *
 * @author Christian W. Damus (cdamus)
 */
public class InternalRequestParameters {
	/**
	 * Request parameter specifying the associated destroy-dependents request
	 * that is propagated recursively through the processing of destroy
	 * requests for an element, its contents, and its dependents.
	 * <p>
	 * The expected parameter value is an instance of type {@link DestroyDependentsRequest}. 
	 * </p>
	 */
	public static final String DESTROY_DEPENDENTS_REQUEST_PARAMETER = "DestroyElementRequest.destroyDependentsRequest"; //$NON-NLS-1$
	
}
