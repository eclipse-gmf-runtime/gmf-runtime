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
