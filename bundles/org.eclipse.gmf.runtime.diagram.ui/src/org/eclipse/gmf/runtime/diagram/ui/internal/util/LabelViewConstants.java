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
