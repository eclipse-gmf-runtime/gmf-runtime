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

package org.eclipse.gmf.runtime.common.ui.services.parser;

import org.eclipse.core.runtime.IStatus;



/**
 * Status object for indicating the validity of an edit.
 *  
 * @author jcorchis
 */
public interface IParserEditStatus extends IStatus {
	
	/** Status code constant (value 0) indicating the edit status editable. */
	public static final int EDITABLE = 0;

	/** Status code constant (bit mask, value 1) indicating the edit status uneditable. */
	public static final int UNEDITABLE = 0x01;
	
}
