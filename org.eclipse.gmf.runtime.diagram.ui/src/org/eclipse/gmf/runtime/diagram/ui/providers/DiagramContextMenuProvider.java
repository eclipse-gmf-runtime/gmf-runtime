/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 * An implementation of a context menu provider for GMF diagrams
 * It adds contributions to the popup menu both programatically and through
 * the contribution item service
 * 
 * @author melaasar
 * @see ContributionItemService
 */
public class DiagramContextMenuProvider extends ContextMenuProvider {

	/** the workbench part */
	private IWorkbenchPart part;
    
    private Set exclusionSet = new HashSet();
    
    /** the following items will be deleted from the context menus by default */
    private String[] defaultExclusionList = {
        "replaceWithMenu", //$NON-NLS-1$
        "compareWithMenu", //$NON-NLS-1$
        "ValidationAction", //$NON-NLS-1$
        "team.main", //$NON-NLS-1$
        "org.eclipse.jst.ws.atk.ui.webservice.category.popupMenu", //$NON-NLS-1$
        "org.eclipse.tptp.platform.analysis.core.ui.internal.actions.MultiAnalysisActionDelegate", //$NON-NLS-1$
        "org.eclipse.debug.ui.contextualLaunch.run.submenu", //$NON-NLS-1$
        "org.eclipse.debug.ui.contextualLaunch.debug.submenu", //$NON-NLS-1$
        "org.eclipse.debug.ui.contextualLaunch.profile.submenu" //$NON-NLS-1$
    };

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
        addDefaultExclusions();
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
    
    /**
     * The exclusion <code>Set</code> allows clients to specify which contributed
     * menu items they do not want to include in their context menus.
     * @return <code>Set</code> of IDs
     */
    public Set getExclusionSet() {
        return exclusionSet;
    }
    
    /**
     * set the exclusion <code>Set</code>. 
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramContextMenuProvider#getExclusionSet
     * @param exclusionSet the <code>Set</code> of IDs of menu items that need to be 
     * excluded from the context menu
     */
    public void setExclusionSet(Set exclusionSet) {
        this.exclusionSet = exclusionSet;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.ContributionManager#allowItem(org.eclipse.jface.action.IContributionItem)
     */
    protected boolean allowItem(IContributionItem itemToAdd) {
        if (itemToAdd.getId() != null && exclusionSet.contains(itemToAdd.getId()))
            //we don't want to return false, as other menu items may depend on it...
            itemToAdd.setVisible(false);

        return super.allowItem(itemToAdd);
    }
    
    /**
     * Transfer the String array <code>defaultExclusionList</code>
     * into the <code>exclusionSet</code>
     *
     */
    protected void addDefaultExclusions() {
        for (int i=0; i < defaultExclusionList.length; i++)
            exclusionSet.add(defaultExclusionList[i]);
    }
}
