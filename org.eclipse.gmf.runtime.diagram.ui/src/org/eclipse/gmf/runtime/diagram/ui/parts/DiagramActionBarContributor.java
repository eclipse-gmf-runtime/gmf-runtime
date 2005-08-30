/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.ui.IActionBars;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartDescirptor;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramMEditingDomainGetter;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;

/**
 * @author melaasar
 * 
 * An abstract implementation of a diagram editor action bar contributor
 * It contributes generic items that applies to all presentation diagrams 
 */
public abstract class DiagramActionBarContributor
	extends ActionBarContributor {

	private IWorkbenchPartDescriptor descriptor;

	/**
	 * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars)
	 */
	public void init(final IActionBars bars) {
		super.init(bars);

		descriptor =
			new WorkbenchPartDescirptor(
				getEditorId(),
				getEditorClass(),
				getPage());

		try {			
			DiagramMEditingDomainGetter.getMEditingDomain(getPage().getActiveEditor()).runAsRead( new MRunnable() {
				public Object run() {
					ContributionItemService.getInstance().contributeToActionBars(
							bars,
							descriptor);
					return null;
				}
			});
		}catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
					DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
					"init()", //$NON-NLS-1$
					e);
		}
		
		bars.updateActionBars();
	}

	/**
	 * @see org.eclipse.ui.IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		ContributionItemService.getInstance().disposeContributions(descriptor);
		descriptor = null;
		super.dispose();
	}

	/**
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		// empty impl
	}

	/**
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		// empty impl
	}

	/**
	 * Returns the id of the editor configured with this contributor
	 * 
	 * @return The editor's id configured with this contribution
	 */
	protected abstract String getEditorId();

	/**
	 * Returns the class of the editor configured with this contributor
	 * 
	 * @return The editor's class configured with this contribution
	 */
	protected abstract Class getEditorClass();

}
