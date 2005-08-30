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
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;


/**
 * Concrete implementation of the IParserEditStatus suitable for use or extending. This
 * class aides parsing by enabling feedback to help users understand
 * 
 * @see #getMessage()
 * @author jcorchis
 */
public class ParserEditStatus
	extends Status 
	implements IParserEditStatus {
	
	/**
	 * The standard Editable status. Does not contain any message.
	 */
	public static final ParserEditStatus EDITABLE_STATUS = new ParserEditStatus(
		CommonUIServicesPlugin.getPluginId(), EDITABLE, ""); //$NON-NLS-1$

	/**
	 * The standard Uneditable status. Does not contain a message.
	 */
	public static final ParserEditStatus UNEDITABLE_STATUS = new ParserEditStatus(
		CommonUIServicesPlugin.getPluginId(), UNEDITABLE, ""); //$NON-NLS-1$
	
	/**
	 * Creates a new status object.  The created status has no children.
	 *
	 * @param severity the severity; one of <code>OK</code>, <code>ERROR</code>, 
	 * <code>INFO</code>, <code>WARNING</code>,  or <code>CANCEL</code>
	 * @param pluginId the unique identifier of the relevant plug-in
	 * @param code either <code>EDITABLE</code> or <code>UNEDITABLE</code> 
	 * @param message a human-readable message, localized to the
	 *    current locale
	 * @param exception a low-level exception, or <code>null</code> if not
	 *    applicable 
	 */
	public ParserEditStatus(int status, String pluginId, int code, String message, Throwable exception) {
		super(status, pluginId, code, message, exception);
	}
	
	/**
	 * Create a new status object with severity <code>INFO</code>, and null for the 
	 * exception.
	 * @param pluginId the unique identifier of the relevant plug-in
	 * @param code either <code>EDITABLE</code> or <code>UNEDITABLE</code> 
	 * @param message message a human-readable message, localized to the
	 *    current locale
	 */
	public ParserEditStatus(String pluginId, int code, String message) {
		super(IStatus.INFO, pluginId, code, message, null);
	}
}
