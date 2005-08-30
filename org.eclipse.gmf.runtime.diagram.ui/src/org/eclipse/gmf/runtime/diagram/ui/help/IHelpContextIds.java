/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.help;

/**
 * Context sensitive help ids. Currently, the help ids are defined in the 
 * com.ibm.xtools.umlvisualizer.infopop plug-in.
 *
 * @author Anthony Hunter 
 * <a href="mailto:ahunter@rational.com">ahunter@rational.com</a>
 */
public interface IHelpContextIds {

	/**
	 * The context sensitive help id prefix for this plug-in
	 */
	public static final String PREFIX = "org.eclipse.gmf.runtime.diagram.ui" + "."; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * The context sensitive help id for the Copy Diagram to Image File dialog
	 */
	public static final String VZ_U_COPY_DGM_TO_IMAGE_DB = PREFIX +  "vz_u_copy_dgm_image_db"; //$NON-NLS-1$
	
}
