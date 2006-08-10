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

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalCloseAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalCopyAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalCutAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalDeleteAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalFindAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalMoveAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalOpenAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalPasteAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalPropertiesAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalRefreshAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalRenameAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalRevertAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalSaveAction;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.global.GlobalSelectAllAction;
import org.eclipse.gmf.runtime.common.ui.util.IPartSelector;

/**
 * This class manages all the global actions. It has methods to create
 * global actions, add these actions to a menu and set these actions as
 * the workbench global actions handlers.
 * 
 * @author Vishy Ramaswamy
 */
public class GlobalActionManager {

	/**
	 * Attribute for the list of actions.
	 */
	private Hashtable listOfActions = new Hashtable();

	/**
	 * Attribute for the list of global action ids.
	 */
	private Vector listOfActionIds = new Vector();

	/**
	 * Create the singleton.
	 */
	private static GlobalActionManager instance = new GlobalActionManager();

	/**
	 * Return the singleton.
	 * 
	 * @return a singleton instance of <code>GlobalActionManager</code>
	 */
	public static GlobalActionManager getInstance() {
		return instance;
	}

	/**
	 * Constructor for GlobalActionManager.
	 */
	protected GlobalActionManager() {
		super();

		addActionId(GlobalActionId.CUT);
		addActionId(GlobalActionId.COPY);
		addActionId(GlobalActionId.PASTE);
		addActionId(GlobalActionId.DELETE);
		addActionId(GlobalActionId.SELECT_ALL);
		addActionId(GlobalActionId.UNDO);
		addActionId(GlobalActionId.REDO);
		addActionId(GlobalActionId.PRINT);
		addActionId(GlobalActionId.OPEN);
		addActionId(GlobalActionId.CLOSE);
		addActionId(GlobalActionId.MOVE);
		addActionId(GlobalActionId.RENAME);
		addActionId(GlobalActionId.FIND);
		addActionId(GlobalActionId.PROPERTIES);
		addActionId(GlobalActionId.SAVE);
		addActionId(GlobalActionId.REFRESH);
		addActionId(GlobalActionId.REVERT);
	}

	/**
	 * Returns the <code>GlobalAction</code> for the given part and action id
	 * 
	 * @param globalActionId An action id
	 * @param part The <code>IWorkbenchPart</code>
	 * 
	 * @return GlobalAction
	 */
	public GlobalAction getGlobalActionHandler(final IWorkbenchPart part,
		String globalActionId) {
		assert null != part;
		assert null != globalActionId;

		/* variable for the action to be returned */
		GlobalAction action = null;

		/* Check if the part exists in the cache */
		Hashtable actionList = (Hashtable) getListOfActions().get(part);
		if (actionList != null) {
			/* Check if the action id exists */
			action = (GlobalAction) actionList.get(globalActionId);
			if (action == null) {
				/* create the action */
				action = createActionHandler(part, globalActionId);
				if (action == null) {
					return action;
				}

				/* update the list of actions for the part */
				actionList.put(globalActionId, action);

				/* update the part with the new action list */
				getListOfActions().put(part, actionList);
			}
		} else {
			/* create an action list for the part */
			actionList = new Hashtable();

			/* create the action */
			action = createActionHandler(part, globalActionId);
			if (action == null) {
				return action;
			}

			/* add the action to the action list */
			actionList.put(globalActionId, action);

			/* update the part with the new action list */
			getListOfActions().put(part, actionList);

			/* register as a part listener so that the cache can be cleared
			 * when the part is disposed */
			part.getSite().getPage().addPartListener(new IPartListener() {
				
				private IWorkbenchPart localPart = part;


				/* (non-Javadoc)
				 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
				 */
				public void partActivated(IWorkbenchPart workbenchPart) {
					/* method not implemented */
				}

				/* (non-Javadoc)
				 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
				 */
				public void partBroughtToTop(IWorkbenchPart workbenchPart) {
					/* method not implemented */
				}

				/* (non-Javadoc)
				 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
				 */
				public void partClosed(IWorkbenchPart workbenchPart) {
					/* remove the cache associated with the part */
					if (workbenchPart != null && localPart == workbenchPart && getListOfActions().containsKey(workbenchPart)) {
						getListOfActions().remove(workbenchPart);
						workbenchPart.getSite().getPage().removePartListener(this);
						localPart = null;
					}
				}


				/* (non-Javadoc)
				 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
				 */
				public void partDeactivated(IWorkbenchPart workbenchPart) {
					/* method not implemented */
				}

				/* (non-Javadoc)
				 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
				 */
				public void partOpened(IWorkbenchPart workbenchPart) {
					/* method not implemented */
				}
			});
		}

		return action;
	}

	/**
	 * Creates an GlobalAction.
	 * 
	 * @param page The workbench page
	 * @param id   The action id
	 * 
	 * @return GlobalAction
	 */
	public GlobalAction createActionHandler(IWorkbenchPage page, String id) {
		GlobalAction action = null;

		if (id.equals(GlobalActionId.CUT))
			action = new GlobalCutAction(page);
		else if (id.equals(GlobalActionId.COPY))
			action = new GlobalCopyAction(page);
		else if (id.equals(GlobalActionId.PASTE))
			action = new GlobalPasteAction(page);
		else if (id.equals(GlobalActionId.UNDO))
			action = new GlobalUndoAction(page);
		else if (id.equals(GlobalActionId.REDO))
			action = new GlobalRedoAction(page);
		else if (id.equals(GlobalActionId.DELETE))
			action = new GlobalDeleteAction(page);
		else if (id.equals(GlobalActionId.SELECT_ALL))
			action = new GlobalSelectAllAction(page);
		else if (id.equals(GlobalActionId.PRINT))
			action = new GlobalPrintAction(page);
		else if (id.equals(GlobalActionId.MOVE))
			action = new GlobalMoveAction(page);
		else if (id.equals(GlobalActionId.OPEN))
			action = new GlobalOpenAction(page);
		else if (id.equals(GlobalActionId.CLOSE))
			action = new GlobalCloseAction(page);
		else if (id.equals(GlobalActionId.RENAME))
			action = new GlobalRenameAction(page);
		else if (id.equals(GlobalActionId.REFRESH))
			action = new GlobalRefreshAction(page);
		else if (id.equals(GlobalActionId.REVERT))
				action = new GlobalRevertAction(page);
		else if (id.equals(GlobalActionId.FIND))
			action = new GlobalFindAction(page);
		else if (id.equals(GlobalActionId.PROPERTIES))
			action = new GlobalPropertiesAction(page);
		else if (id.equals(GlobalActionId.SAVE))
			action = new GlobalSaveAction(page);

		// this initialization should be moved to the client to call
		if (action != null)
			action.init();
		return action;
	}

	/**
	 * Creates an GlobalAction.
	 * 
	 * @param part The workbench part
	 * @param id The action id
	 * 
	 * @return GlobalAction
	 */
	public GlobalAction createActionHandler(final IWorkbenchPart part, String id) {
		GlobalAction action = null;

		if (id.equals(GlobalActionId.CUT))
			action = new GlobalCutAction(part);
		else if (id.equals(GlobalActionId.COPY))
			action = new GlobalCopyAction(part);
		else if (id.equals(GlobalActionId.PASTE))
			action = new GlobalPasteAction(part);
		else if (id.equals(GlobalActionId.UNDO))
			action = new GlobalUndoAction(part);
		else if (id.equals(GlobalActionId.REDO))
			action = new GlobalRedoAction(part);
		else if (id.equals(GlobalActionId.DELETE))
			action = new GlobalDeleteAction(part);
		else if (id.equals(GlobalActionId.SELECT_ALL))
			action = new GlobalSelectAllAction(part);
		else if (id.equals(GlobalActionId.PRINT))
			action = new GlobalPrintAction(part);
		else if (id.equals(GlobalActionId.MOVE))
			action = new GlobalMoveAction(part);
		else if (id.equals(GlobalActionId.OPEN))
			action = new GlobalOpenAction(part);
		else if (id.equals(GlobalActionId.CLOSE))
			action = new GlobalCloseAction(part);
		else if (id.equals(GlobalActionId.RENAME))
			action = new GlobalRenameAction(part);
		else if (id.equals(GlobalActionId.REFRESH))
			action = new GlobalRefreshAction(part);
		else if (id.equals(GlobalActionId.REVERT))
			action = new GlobalRevertAction(part);
		else if (id.equals(GlobalActionId.FIND))
			action = new GlobalFindAction(part);
		else if (id.equals(GlobalActionId.PROPERTIES))
			action = new GlobalPropertiesAction(part);
		else if (id.equals(GlobalActionId.SAVE))
			action = new GlobalSaveAction(part);

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

	/**
	 * Returns the listOfActions.
	 * @return Hashtable
	 */
	private Hashtable getListOfActions() {
		return listOfActions;
	}

	/**
	 * Creates default list of all global actions
	 * @param part The workbench part
	 * @return GlobalAction[] An array of GlobalAction
	 */
	public GlobalAction[] createGlobalActions(IWorkbenchPart part) {
		assert null != part;
		Vector list = new Vector();

		/* Loop through all the actions */
		for (int i = 0; i < getListOfActionIds().size(); i++) {
			GlobalAction action = getGlobalActionHandler(part,
				(String) getListOfActionIds().elementAt(i));
			if (action != null) {
				list.addElement(action);
			}
		}

		GlobalAction[] array = new GlobalAction[list.size()];
		list.copyInto(array);

		return array;
	}

	/**
	 * Creates specified list of global actions
	 * @param part The workbench part
	 * @param actionIds List of actions ids
	 * @return GlobalAction[] An array of GlobalAction
	 */
	public GlobalAction[] createGlobalActions(IWorkbenchPart part,
		String[] actionIds) {
		assert null != part;
		assert null != actionIds;
		assert (actionIds.length > 0);

		/* Go through all the actions */
		Vector list = new Vector();
		GlobalAction action = null;
		for (int i = 0; i < actionIds.length; i++) {
			action = getGlobalActionHandler(part, actionIds[i]);
			if (action != null) {
				list.addElement(action);
			}
		}

		GlobalAction[] array = new GlobalAction[list.size()];
		list.copyInto(array);

		return array;
	}

	/** Get Global Action
	 * 
	 * This method obtains the specific global action for the given part.
	 * If the action does not exist, null is returned.
	 * 
	 * @param part the workbench part associated with the global action
	 * @param actionId the ID of the global action to locate
	 * @return GlobalAction the global action, or null if none is defined for the part
	 */
	public GlobalAction getGlobalAction(IWorkbenchPart part, String actionId) {
		assert null != part;
		assert null != actionId;
		GlobalAction action = null;

		/* Get the action list for this part and return the desired action if defined. */
		Hashtable actionList = (Hashtable) getListOfActions().get(part);
		if (actionList != null) {
			action = (GlobalAction) actionList.get(actionId);
		}

		return action;
	}

	/**
	 * Set the global actions as the workbench global action handlers
	 * 
	 * @param bar The action bars
	 * @param part The workbench part
	 */
	public void setGlobalActionHandlers(IActionBars bar, IWorkbenchPart part) {
		assert null != bar;
		assert null != part;

		/* Check if the part exists in the cache */
		if (!getListOfActions().containsKey(part)) {
			/* Create default list of all global actions */
			createGlobalActions(part);
		}

		/* Get the action list */
		Hashtable actionList = (Hashtable) getListOfActions().get(part);
		if (actionList == null) {
			return;
		}

		/* Enumerate through all the actions */
		Enumeration enumeration = actionList.elements();
		while (enumeration.hasMoreElements()) {
			IAction action = (IAction) enumeration.nextElement();
			if (action.getId() != null) {
				bar.setGlobalActionHandler(action.getId(), action);
			}
		}

		/* Refresh the action bars */
		bar.updateActionBars();
	}

	/**
	 * Unset the global actions as the workbench global action handlers
	 * @param bar The action bars
	 * @param part The workbench part
	 */
	public void unSetGlobalActionHandlers(IActionBars bar, IWorkbenchPart part) {
		assert null != bar;
		assert null != part;

		/* Check if the part exists in the cache */
		if (!getListOfActions().containsKey(part)) {
			/* Create default list of all global actions */
			createGlobalActions(part);
		}

		/* Get the action list */
		Hashtable actionList = (Hashtable) getListOfActions().get(part);
		if (actionList == null) {
			return;
		}

		/* Enumerate through all the actions */
		Enumeration enumeration = actionList.elements();
		while (enumeration.hasMoreElements()) {
			IAction action = (IAction) enumeration.nextElement();
			if (action.getId() != null) {
				bar.setGlobalActionHandler(action.getId(), null);
			}
		}

		/* Refresh the action bars */
		bar.updateActionBars();
	}

	/**
	 * Refreshes the global actions for the given part
	 * 
	 * @param part The workbench part
	 */
	public void refreshGlobalActions(IWorkbenchPart part) {
		assert null != part;

		/* Check if the part exists in the cache */
		if (!getListOfActions().containsKey(part)) {
			return;
		}

		/* Get the action list */
		Hashtable actionList = (Hashtable) getListOfActions().get(part);
		if (actionList == null) {
			return;
		}

		/* Enumerate through all the actions */
		Enumeration enumeration = actionList.elements();
		while (enumeration.hasMoreElements()) {
			((GlobalAction) enumeration.nextElement()).refresh();
		}
	}

	/**
	 * Returns the listOfActionIds.
	 * @return Vector
	 */
	private Vector getListOfActionIds() {
		return listOfActionIds;
	}
	
	/**
	 * Adds an ActionId to the list of action ids.
	 * 
	 * @param actionId String action id to add.
	 */
	protected void addActionId(String actionId) {
		//list is initialized in declaration
		getListOfActionIds().addElement(actionId);		
	}
	
}