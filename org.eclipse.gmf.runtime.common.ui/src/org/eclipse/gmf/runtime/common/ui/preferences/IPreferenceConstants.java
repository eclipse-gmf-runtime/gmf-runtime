/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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
