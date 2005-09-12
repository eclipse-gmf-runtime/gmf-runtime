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


package org.eclipse.gmf.runtime.common.ui.preferences;


/**
 * Defines preference constants for this plug-in.
 * 
 * @author cmahoney
 * 
 */
public interface IPreferenceConstants {

	/**
	 * open even if there are unrecognized versions in the Diagram; Compatibility mode 
	 */
	public static final String OPEN_UNRECOGNIZED_VERSIONS = "Modeling.openUnrecognizedVersions";  //$NON-NLS-1$
	
	/**
	 * save data comming form unrecognized versions on the diagram; Compatibility mode 
	 */
	public static final String SAVE_UNRECOGNIZED_VERSIONS = "Modeling.saveUnrecognizedVersions";  //$NON-NLS-1$

}
