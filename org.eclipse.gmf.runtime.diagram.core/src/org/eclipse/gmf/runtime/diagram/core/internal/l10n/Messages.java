/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.l10n;




/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 */
public class Messages {
	
	/**
	 * Method getString.
	 * Redirects to the PresentationResourceManager to get the string.
	 * 
	 * @param id String key found in message.properties
	 * @return String translatable string value that is associated with the key.
	 */
	public static String getString(String id) {
		return DiagramResourceManager.getI18NString(id);
	}
} 
