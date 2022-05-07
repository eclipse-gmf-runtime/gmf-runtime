/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;

/**
 * A class that is used to handle all global menu/keyboard generated text actions 
 * associated with the displayed text editor 
 * 
 * @author Yasser Lulu
 * 
 */
public class InlineTextActionHandler
	extends BaseInlineTextActionHandler
	implements IInlineTextActionHandler {
	/**
	 * the action bars for the site that the text editor is displayed within
	 */
	private IActionBars actionBars;

	/**
	 * a map used to maintain the global-textEditing action-pairs
	 */
	private Map actionPairMap;
	/**
	 * global actions ids that are non-Eclipse, which the caller needs to have us
	 * disable while editing is in progress
	 */
	private List disableActionsIds;

	/**
	 * Constructor for InlineTextActionHandler.
	 * 
	 * @param actionBars list of action bars for the site that the text editor is displayed within
	 * @param text widget used for editing
	 * @param disableActionsIds list of actions that must be disabled while editing is in progress
	 */
	public InlineTextActionHandler(
		IActionBars actionBars,
		Text text,
		List disableActionsIds) {
		super(text);
		setActionPairMap(new HashMap());
		setActionBars(actionBars);
		this.disableActionsIds = disableActionsIds;
	}
	/**
	 * a class used to insert a disabled action to be used whenever a given
	 * global action has no corresponding replacement for text (e.g., find)
	 */
	private class DisabledTextActionHandler extends Action {
		/**
		 * Constructor.
		 * 
		 * @param name name label
		 */
		protected DisabledTextActionHandler(String name) {
			super(name);
			setEnabled(false);
		}
		public void setEnabled(boolean enabled) {
			super.setEnabled(false);
		}
	}
	/**
	 * a structure used to hold the global-action and its correponding 
	 * text-action replacement 
	 */
	private class ActionPair {
		/**
		 * the action id
		 */
		String actionId;
		/**
		 * the original action as installed by the app
		 */
		IAction originalAction;
		/**
		 * our own corresponding action replacement for handling text
		 */
		IAction textHandlerAction;
	}

	/**
	 * returns the action-bars for the view-site
	 * @return IActionBars The action-bars
	 */
	private IActionBars getActionBars() {
		return actionBars;
	}
	/**
	 * sets the action bars for the site
	 * @param actionBars the IActionBars
	 */
	private void setActionBars(IActionBars actionBars) {
		this.actionBars = actionBars;
	}

	/**
	 * builds the action map pair
	 */
	private void buildActionPairMap() {
		if (getActionPairMap().isEmpty() == false) {
			return;
		}
		ActionPair actionPair = null;
		IAction action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.CUT.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.CUT.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction = new Action(action.getText()) {
				public void run() {
					handleCut();
				}
			};
			getActionPairMap().put(actionPair.actionId, actionPair);
		}
		//////////////////            
		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.COPY.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.COPY.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction = new Action(action.getText()) {
				public void run() {
					handleCopy();
				}
			};
			getActionPairMap().put(actionPair.actionId, actionPair);
		}
		//////////////////
		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.PASTE.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.PASTE.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction = new Action(action.getText()) {
				public void run() {
					handlePaste();
				}
			};
			getActionPairMap().put(actionPair.actionId, actionPair);
		}
		//////////////////
		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.DELETE.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.DELETE.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction = new Action(action.getText()) {
				public void run() {
					handleDelete();
				}
			};
			getActionPairMap().put(actionPair.actionId, actionPair);
		}
		//////////////////
		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.SELECT_ALL.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.SELECT_ALL.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction = new Action(action.getText()) {
				public void run() {
					handleSelectAll();
				}
			};
			getActionPairMap().put(actionPair.actionId, actionPair);
		}
		//////////////

		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.UNDO.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.UNDO.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction =
				new DisabledTextActionHandler(action.getText());
			getActionPairMap().put(actionPair.actionId, actionPair);
		}

		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.REDO.getId());
		if (action != null) {
			action.setEnabled(false);

			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.REDO.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction =
				new DisabledTextActionHandler(action.getText());
			getActionPairMap().put(actionPair.actionId, actionPair);
		}

		action =
			getActionBars().getGlobalActionHandler(
				ActionFactory.FIND.getId());
		if (action != null) {
			actionPair = new ActionPair();
			actionPair.actionId = ActionFactory.FIND.getId();
			actionPair.originalAction = action;
			actionPair.textHandlerAction =
				new DisabledTextActionHandler(action.getText());
			getActionPairMap().put(actionPair.actionId, actionPair);
		}

		Iterator it = disableActionsIds.iterator();
		String specialCommandId = null;
		while (it.hasNext()) {
			specialCommandId = (String) it.next();
			action = getActionBars().getGlobalActionHandler(specialCommandId);
			if (action != null) {
				actionPair = new ActionPair();
				actionPair.actionId = specialCommandId;
				actionPair.originalAction = action;
				actionPair.textHandlerAction =
					new DisabledTextActionHandler(action.getText());
				getActionPairMap().put(actionPair.actionId, actionPair);
			}
		}

	}
	/**
	 * hooks our text actions by replacing the installed global actions with
	 * one suitable for text editing, in order to retarget them correctly while 
	 * the editing is in progress
	 */
	public void hookHandlers() {
		buildActionPairMap();
		ActionPair actionPair = null;
		Iterator it = getActionPairMap().values().iterator();
		while (it.hasNext()) {
			actionPair = (ActionPair) it.next();
			getActionBars().setGlobalActionHandler(actionPair.actionId, null);
		}
		getActionBars().updateActionBars();
		it = getActionPairMap().values().iterator();
		while (it.hasNext()) {
			actionPair = (ActionPair) it.next();
			getActionBars().setGlobalActionHandler(
				actionPair.actionId,
				actionPair.textHandlerAction);
			actionPair.textHandlerAction.setEnabled(true);
		}
		getActionBars().updateActionBars();
		setHooked(true);
	}
	/**
	 * unhooks and uninstall our text actions and re-install the original global 
	 * actions 
	 */
	public void unHookHandlers() {
		ActionPair actionPair = null;
		Iterator it = getActionPairMap().values().iterator();
		while (it.hasNext()) {
			actionPair = (ActionPair) it.next();
			getActionBars().setGlobalActionHandler(actionPair.actionId, null);
		}
		getActionBars().updateActionBars();
		it = getActionPairMap().values().iterator();
		while (it.hasNext()) {
			actionPair = (ActionPair) it.next();
			getActionBars().setGlobalActionHandler(
				actionPair.actionId,
				actionPair.originalAction);
			actionPair.originalAction.setEnabled(
				actionPair.originalAction.isEnabled());
		}
		getActionBars().updateActionBars();
		setHooked(false);
	}

	/**
	 * diposes this action-handler
	 * 
	 */
	public void dispose() {
		super.dispose();
		setActionPairMap(null);
		setActionBars(null);
		disableActionsIds = null;
	}
	/**
	 * Returns the actionPairMap.
	 * @return Map
	 */
	private Map getActionPairMap() {
		return actionPairMap;
	}

	/**
	 * Sets the actionPairMap.
	 * @param actionPairMap The actionPairMap to set
	 */
	private void setActionPairMap(Map actionPairMap) {
		this.actionPairMap = actionPairMap;
	}
}
