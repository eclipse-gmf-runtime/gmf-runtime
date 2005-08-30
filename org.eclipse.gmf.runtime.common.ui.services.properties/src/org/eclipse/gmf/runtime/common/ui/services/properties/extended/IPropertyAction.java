/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.swt.widgets.Control;

/**
 * Property related action
 *
 * @author dmisic
 */
public interface IPropertyAction {

	/**
	 * Executes the action
	 * 
	 * @param owner The logical owner control; does not have to be control that
	 *              initiated the action
	 * @return The result of the action; may be null
	 */
	public Object execute(Control owner);
}
