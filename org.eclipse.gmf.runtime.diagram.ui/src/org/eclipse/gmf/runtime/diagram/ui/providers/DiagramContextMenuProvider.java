/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 * An implementation of a context menu provider for GMF diagrams
 * It adds contributions to the popup menu both programatically and through
 * the contribution item service
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 * @see ContributionItemService
 */
public class DiagramContextMenuProvider extends ContextMenuProvider {

	/** the workbench part */
	private IWorkbenchPart part;

	/**
	 * Constructor for DiagramContextMenuProvider.
	 * @param part
	 * @param viewer
	 */
	public DiagramContextMenuProvider(
		IWorkbenchPart part,
		EditPartViewer viewer) {
		super(viewer);
		this.part = part;
	}

	/**
	 * Returns the actionRegistry.
	 * @return ActionRegistry
	 */
	public ActionRegistry getActionRegistry() {
		return (ActionRegistry) part.getAdapter(ActionRegistry.class);
	}

	/**
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu) {
		getViewer().flush();

		try {
			TransactionUtil.getEditingDomain((EObject)getViewer().getContents().getModel()).runExclusive( new Runnable() {
				public void run() {
					ContributionItemService.getInstance().contributeToPopupMenu(
						DiagramContextMenuProvider.this,
						part);
				}
			});
		}catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
					DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
					"buildContextMenu()", //$NON-NLS-1$
					e);
		}
	}

}
