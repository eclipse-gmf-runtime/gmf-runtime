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