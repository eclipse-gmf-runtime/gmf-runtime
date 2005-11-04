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

package org.eclipse.gmf.runtime.diagram.ui.internal.util;


/**
 * @author mmostafa
 * this interface defines all label View constants
 */
public interface LabelViewConstants {
	/** 
	 * Percentage location for labels that are located
	 * relative to the source end of a <code>Connection</code>
	 */
	public static final int SOURCE_LOCATION = 15;
	/** 
	 * Percentage location for labels that are located
	 * relative to the target of a <code>Connection</code>
	 */
	public static final int TARGET_LOCATION = 85;
	/** 
	 * Percentage location for labels that are located
	 * relative to the middle of a <code>Connection</code>
	 */
	public static final int MIDDLE_LOCATION = 50;	
}
