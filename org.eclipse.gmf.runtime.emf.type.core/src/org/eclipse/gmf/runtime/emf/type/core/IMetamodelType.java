/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;


/**
 * Interface used to define application-layer types that map directly to an
 * <code>EClass</code>.
 * <P>
 * Clients should not implement this interface directly, but should extend the
 * abstract implementation {@link org.eclipse.gmf.runtime.emf.type.core.MetamodelType}.
 * 
 * @author ldamus
 */
public interface IMetamodelType
	extends IElementType {

	// No additional API.
}