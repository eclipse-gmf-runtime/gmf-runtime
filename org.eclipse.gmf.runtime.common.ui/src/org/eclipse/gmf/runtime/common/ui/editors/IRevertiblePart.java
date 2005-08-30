/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.editors;

import org.eclipse.ui.ISaveablePart;

/**
 * Workbench parts can implement or adapt to this interface to participate
 * in the enablement and execution of the <code>Revert</code> action.
 * 
 * @author ldamus
 */
public interface IRevertiblePart
	extends ISaveablePart {

	/**
	 * Reverts this part by abandoning changes made in the part since the last
	 * save operation.
	 */
	public void doRevertToSaved();
}