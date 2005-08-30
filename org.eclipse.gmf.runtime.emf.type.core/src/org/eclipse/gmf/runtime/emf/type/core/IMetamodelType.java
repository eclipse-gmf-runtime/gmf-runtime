/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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