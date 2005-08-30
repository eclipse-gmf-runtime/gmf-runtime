/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import org.eclipse.gef.commands.Command;


/**
 * @author sshaw
 * @canBeSeenBy %level1
 *
 * Interface for accessing the wrapped GEF command
 */
public interface IInternalLayoutRunnable extends Runnable {
	
	/**
	 * @return the wrapped GEF command to be executed from the layout service.
	 */
	public Command getCommand();
}
