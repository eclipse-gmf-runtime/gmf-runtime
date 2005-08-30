/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;

/**
 * 
 * An implementation of a context menu provider for presentation diagrams
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
			MEditingDomainGetter.getMEditingDomain((EObject)getViewer().getContents().getModel()).runAsRead( new MRunnable() {
				public Object run() {
					ContributionItemService.getInstance().contributeToPopupMenu(
						DiagramContextMenuProvider.this,
						part);
					return null;
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
