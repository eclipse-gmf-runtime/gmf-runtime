/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
