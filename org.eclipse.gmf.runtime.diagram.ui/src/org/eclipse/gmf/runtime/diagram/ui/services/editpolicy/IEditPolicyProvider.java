/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.editpolicy;

import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for providers of the EditPolicy Service.
 * 
 * @author chmahone
 */
public interface IEditPolicyProvider extends IProvider {

	/**
	 * Creates and installs the applicable editpolicies on the given editpart.
	 * @param editPart the <code>EditPart</code>
	 */
	public void createEditPolicies(EditPart editPart);

}
