/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.View;
/**
 * This is an abstract class that contains common behaviour for all
 * the ZOrder Commands.
 * 
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 */
public abstract class ZOrderCommand extends AbstractModelCommand {
	
	protected View toMove;
	protected View containerView;

	public ZOrderCommand( String label,View view ) {
		super( label, view);

		this.toMove = view;
		containerView = ViewUtil.getContainerView(toMove);
	}

}
