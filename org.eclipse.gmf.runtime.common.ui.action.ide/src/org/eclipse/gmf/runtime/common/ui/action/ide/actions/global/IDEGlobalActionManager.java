/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.util.IPartSelector;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;


/**
 * This class subclasses GlobalActionManager and includes support for IDE global
 * actions, which include the bookmark action, the open project action, and the
 * close project action.
 * 
 * @author wdiu, Wayne Diu
 */
public class IDEGlobalActionManager extends GlobalActionManager {

	/**
	 * Static list of action ID strings
	 */
	private static final List actionIdStrings = new ArrayList();
	
	static {
		actionIdStrings.add(IDEGlobalActionId.BOOKMARK);
		actionIdStrings.add(IDEGlobalActionId.OPEN_PROJECT);
		actionIdStrings.add(IDEGlobalActionId.CLOSE_PROJECT);
	}

	/**
	 * Create the singleton.
	 */
	private static IDEGlobalActionManager instance = new IDEGlobalActionManager();
	
	/**
	 * Create the singleton.
	 */
	private static GlobalActionManager globalActionManager = GlobalActionManager.getInstance();
	
	/**
	 * Return the singleton.
	 */
	public static GlobalActionManager getInstance() {
		return instance;
	}
	
	/**
	 * Constructor for IDEGlobalActionManager.  Adds several action ids for
	 * ide actions.
	 */
	private IDEGlobalActionManager() {
		Iterator it = actionIdStrings.iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			addActionId(id);
		}
	}	
	
	/**
	 * Creates a GlobalAction.
	 * 
	 * @param page The workbench page
	 * @param id   The action id
	 * 
	 * @return GlobalAction
	 */
	public GlobalAction createActionHandler(IWorkbenchPage page, String id) {
		GlobalAction action = null;
		
		if (id.equals(IDEGlobalActionId.BOOKMARK))
			action = new GlobalBookmarkAction(page);
		else
			action = globalActionManager.createActionHandler(page, id);

		// this initialization should be moved to the client to call
		if (action != null)
			action.init();
		return action;
	}


	/**
	 * Creates a GlobalAction.
	 * 
	 * @param part The workbench part
	 * @param id The action id
	 * 
	 * @return GlobalAction
	 */
	public GlobalAction createActionHandler(final IWorkbenchPart part, String id) {
		GlobalAction action = null;

		if (id.equals(IDEGlobalActionId.BOOKMARK))
			action = new GlobalBookmarkAction(part);
		else if (id.equals(IDEGlobalActionId.OPEN_PROJECT))
			action = new GlobalOpenProjectAction(part);
		else if (id.equals(IDEGlobalActionId.CLOSE_PROJECT))
			action = new GlobalCloseProjectAction(part);
		else
			action = globalActionManager.createActionHandler(part, id);

		if (action != null) {
			// this initialization should be moved to the client to call
			action.init();
			
			// the action will only refresh on selection changes in the specified part
			action.setPartSelector(new IPartSelector() {
				public boolean selects(IWorkbenchPart p) {
					return part == p;
				}
			});
		}
		return action;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#createGlobalActions(org.eclipse.ui.IWorkbenchPart, java.lang.String[])
	 */
	public GlobalAction[] createGlobalActions(IWorkbenchPart part,
			String[] actionIds) {
		
		//superclass will handle the ids from actionIdStrings
		//rest are delegated to global action manager
		List forSuper = new ArrayList(), forGlobalActionManager = new ArrayList();
		for (int i = 0; i < actionIds.length; i++) {
			if (actionIdStrings.contains(actionIds[i])) {
				forSuper.add(actionIds[i]);
			}
			else {
				forGlobalActionManager.add(actionIds[i]);
			}
		}
		
		String[] superArray = new String[forSuper.size()], globalActionManagerArray = new String[forGlobalActionManager.size()];
		forSuper.toArray(superArray);
		forGlobalActionManager.toArray(globalActionManagerArray);
		
		GlobalAction[] actions1 = globalActionManager.createGlobalActions(part, globalActionManagerArray);
		GlobalAction[] actions2 = new GlobalAction[0];
		if (superArray.length > 0) {
			actions2 = super.createGlobalActions(part, superArray);
		}
		
		GlobalAction[] allActions = new GlobalAction[actions1.length + actions2.length]; 
		System.arraycopy(actions1, 0, allActions, 0, actions1.length);
		System.arraycopy(actions2, 0, allActions, actions1.length, actions2.length);
		
		return allActions;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#createGlobalActions(org.eclipse.ui.IWorkbenchPart)
	 */
	public GlobalAction[] createGlobalActions(IWorkbenchPart part) {
		GlobalAction[] actions1 = globalActionManager.createGlobalActions(part);
		GlobalAction[] actions2 = super.createGlobalActions(part);
		
		GlobalAction[] allActions = new GlobalAction[actions1.length + actions2.length]; 
		System.arraycopy(actions1, 0, allActions, 0, actions1.length);
		System.arraycopy(actions2, 0, allActions, actions1.length, actions2.length);
		
		return allActions;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#getGlobalAction(org.eclipse.ui.IWorkbenchPart, java.lang.String)
	 */
	public GlobalAction getGlobalAction(IWorkbenchPart part, String actionId) {
		GlobalAction action = super.getGlobalAction(part, actionId);
		return action != null ? action  : globalActionManager.getGlobalActionHandler(part, actionId);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#getGlobalActionHandler(org.eclipse.ui.IWorkbenchPart, java.lang.String)
	 */
	public GlobalAction getGlobalActionHandler(IWorkbenchPart part,
			String globalActionId) {
		GlobalAction action = super.getGlobalActionHandler(part, globalActionId);
		return action != null ? action  : globalActionManager.getGlobalActionHandler(part, globalActionId);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#refreshGlobalActions(org.eclipse.ui.IWorkbenchPart)
	 */
	public void refreshGlobalActions(IWorkbenchPart part) {
		globalActionManager.refreshGlobalActions(part);
		super.refreshGlobalActions(part);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#setGlobalActionHandlers(org.eclipse.ui.IActionBars, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setGlobalActionHandlers(IActionBars bar, IWorkbenchPart part) {
		globalActionManager.setGlobalActionHandlers(bar, part);
		super.setGlobalActionHandlers(bar, part);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalActionManager#unSetGlobalActionHandlers(org.eclipse.ui.IActionBars, org.eclipse.ui.IWorkbenchPart)
	 */
	public void unSetGlobalActionHandlers(IActionBars bar, IWorkbenchPart part) {
		globalActionManager.unSetGlobalActionHandlers(bar, part);
		super.unSetGlobalActionHandlers(bar, part);
	}
}
