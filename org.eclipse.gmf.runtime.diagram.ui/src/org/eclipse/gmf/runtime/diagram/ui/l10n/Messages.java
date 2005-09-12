/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.l10n;




/**
 * 
 * @deprecated Use the proper resourceManager directly
 */
/*
 * @canBeSeenBy %partners
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
		return PresentationResourceManager.getI18NString(id);
	}
} 
