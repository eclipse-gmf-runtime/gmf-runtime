/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;
/**
 * This is an abstract class that contains common behaviour for all
 * the ZOrder Commands.
 * 
 * @author jschofie
 */
public abstract class ZOrderCommand extends AbstractTransactionalCommand {
	
	protected View toMove;
	protected View containerView;

    /**
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param label
     * @param view
     */
	public ZOrderCommand(TransactionalEditingDomain editingDomain, String label,View view ) {
		super(editingDomain, label, getWorkspaceFiles(view));

		this.toMove = view;
		containerView = ViewUtil.getContainerView(toMove);
	}

}
