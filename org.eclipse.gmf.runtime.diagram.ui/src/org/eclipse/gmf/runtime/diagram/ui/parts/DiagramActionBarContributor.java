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

package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramMEditingDomainGetter;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.ActivityManagerEvent;
import org.eclipse.ui.activities.IActivityManagerListener;

/**
 * @author melaasar, cmahoney
 * 
 * An abstract implementation of a diagram editor action bar contributor
 * It contributes generic items that applies to all diagrams 
 */
public abstract class DiagramActionBarContributor
	extends ActionBarContributor {

	/**
	 * Listens for activity/capability events.
	 * 
	 * @author cmahoney
	 */
	class ActivityManagerListener
		implements IActivityManagerListener {

		public void activityManagerChanged(
				ActivityManagerEvent activityManagerEvent) {
			if (activityManagerEvent.haveEnabledActivityIdsChanged()) {
				updateActionBars();
			}
		}
	}
	
	/**
	 * The activity listener.
	 */
	private ActivityManagerListener activityManagerListener;
	
	private IWorkbenchPartDescriptor descriptor;

	/**
	 * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars)
	 */
	public void init(final IActionBars bars) {
		super.init(bars);

		descriptor =
			new WorkbenchPartDescriptor(
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
		
		activityManagerListener = new ActivityManagerListener();
		PlatformUI.getWorkbench().getActivitySupport().getActivityManager()
			.addActivityManagerListener(activityManagerListener);
	}

	/**
	 * @see org.eclipse.ui.IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		ContributionItemService.getInstance().disposeContributions(descriptor);
		descriptor = null;
		
		if (activityManagerListener != null) {
			PlatformUI.getWorkbench().getActivitySupport().getActivityManager()
				.removeActivityManagerListener(activityManagerListener);
		}
		activityManagerListener = null;
		
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

	/**
	 * Updates the actionbars to show/hide contribution items as applicable.
	 */
	private void updateActionBars() {

		// get the new contributions
		ContributionItemService.getInstance().updateActionBars(getActionBars(),
			descriptor);
		
		// refresh the UI
		getActionBars().updateActionBars();
	}

}
