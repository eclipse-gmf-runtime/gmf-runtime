/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.contributionitem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionGroup;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.IDisposableAction;
import org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ContributionItemConstants;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.DisposeContributionsOperation;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionDescriptorReader;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ProviderContributionDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.ActionGroupCache;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartDescirptor;

/**
 * An abstract implementation of the IContributionItemProvider interface
 * This provider performs the contribution by reading its XML contribution
 * descriptor and parsing it in the correct format.
 * <P>
 * Contribution item providers wishing "not" to read an XML-based descriptor
 * but rather performs the contribution totally programatically should not
 * subclass this class but rather implement the <code>IContributionItemProvider</code>
 * interface directly. 
 * 
 * @author melaasar
 */
public abstract class AbstractContributionItemProvider
	extends AbstractProvider
	implements IContributionItemProvider, IContributionDescriptorReader {

	/** the provider's contribution descriptor */
	private ProviderContributionDescriptor contributionDescriptor;
	/** the action registry cache by workbench part descriptor*/
	private Map actionCache = new HashMap();
	/** the Action Group registry cache by workbench part descriptor */
	private ActionGroupCache actionGroupCache = new ActionGroupCache();

	/**
	 * Gets the structured selection from the workbench part described by
	 * <code>partDescriptor</code>.
	 * 
	 * @param partDescriptor
	 *            the part descriptor
	 * @return the structured selection
	 */
	protected IStructuredSelection getStructuredSelection(IWorkbenchPartDescriptor partDescriptor) {
		
		IStructuredSelection selection = null;
		IWorkbenchPart activePart = partDescriptor.getPartPage().getActivePart();
		if(activePart != null) {
			ISelectionProvider selectionProvider = activePart.getSite().getSelectionProvider();
				
			if (selectionProvider != null
				&& selectionProvider.getSelection() instanceof IStructuredSelection) {
				selection = (IStructuredSelection) selectionProvider.getSelection();
			}
		}
		return (selection != null) ? selection : StructuredSelection.EMPTY;
	}
	
	/**
	 * Gets the first object in the selection in the workbench part described by
	 * <code>partDescriptor</code>.
	 * 
	 * @param partDescriptor
	 *            the part descriptor
	 * @return the first object in the selection
	 */
	protected Object getSelectedObject(IWorkbenchPartDescriptor partDescriptor)
	{
		IStructuredSelection ss = getStructuredSelection(partDescriptor);
		if(!ss.isEmpty())
			return ss.getFirstElement();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionDescriptorReader#setContributionDescriptor(org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ProviderContributionDescriptor)
	 */
	public final void setContributionDescriptor(ProviderContributionDescriptor descriptor) {
		contributionDescriptor = descriptor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionItemProvider#contributeToActionBars(org.eclipse.ui.IActionBars, org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	public final void contributeToActionBars(
		IActionBars actionBars,
		IWorkbenchPartDescriptor partDescriptor) {

		Iterator contributions =
			contributionDescriptor
				.getContributionsFor(
					partDescriptor.getPartId(),
					partDescriptor.getPartClass())
				.iterator();

		while (contributions.hasNext()) {
			Object c = contributions.next();

			if (c
				instanceof ProviderContributionDescriptor.PartMenuDescriptor) {
				ProviderContributionDescriptor.PartMenuDescriptor item =
					(ProviderContributionDescriptor.PartMenuDescriptor) c;

				contributeItem(
					new MenuContributionItemAdapter(
						item.getId(),
						partDescriptor),
					actionBars.getMenuManager(),
					item.getMenubarPath(),
					item.getMenubarGroup());

				contributeItem(
					new MenuContributionItemAdapter(
						item.getId(),
						partDescriptor),
					actionBars.getToolBarManager(),
					item.getToolbarPath(),
					item.getToolbarGroup());
			} else if (
				c
					instanceof ProviderContributionDescriptor.PartMenuGroupDescriptor) {
				ProviderContributionDescriptor.PartMenuGroupDescriptor item =
					(ProviderContributionDescriptor.PartMenuGroupDescriptor) c;

				contributeItem(
					new MenuGroupContributionItemAdapter(
						item.getId(),
						item.isSeparator()),
					actionBars.getMenuManager(),
					item.getMenubarPath(),
					item.getMenubarGroup());

				contributeItem(
					new MenuGroupContributionItemAdapter(
						item.getId(),
						item.isSeparator()),
					actionBars.getToolBarManager(),
					item.getToolbarPath(),
					item.getToolbarGroup());
			} else if (
				c
					instanceof ProviderContributionDescriptor.PartActionDescriptor) {
				ProviderContributionDescriptor.PartActionDescriptor item =
					(ProviderContributionDescriptor.PartActionDescriptor) c;

				contributeItem(
					new ActionContributionItemAdapter(
						item.getId(),
						partDescriptor),
					actionBars.getMenuManager(),
					item.getMenubarPath(),
					item.getMenubarGroup());

				contributeItem(
					new ActionContributionItemAdapter(
						item.getId(),
						partDescriptor),
					actionBars.getToolBarManager(),
					item.getToolbarPath(),
					item.getToolbarGroup());

				if (item.isGlobal())
					actionBars.setGlobalActionHandler(
						item.getId(),
						getAction(item.getId(), partDescriptor));
			} else if (
				c
					instanceof ProviderContributionDescriptor.PartCustomDescriptor) {
				ProviderContributionDescriptor.PartCustomDescriptor item =
					(ProviderContributionDescriptor.PartCustomDescriptor) c;

				contributeItem(
					new CustomContributionItemAdapter(
						item.getId(),
						partDescriptor),
					actionBars.getMenuManager(),
					item.getMenubarPath(),
					item.getMenubarGroup());

				contributeItem(
					new CustomContributionItemAdapter(
						item.getId(),
						partDescriptor),
					actionBars.getToolBarManager(),
					item.getToolbarPath(),
					item.getToolbarGroup());
			} else if (
					c
						instanceof ProviderContributionDescriptor.PartActionGroupDescriptor) {
					ProviderContributionDescriptor.PartActionGroupDescriptor item =
						(ProviderContributionDescriptor.PartActionGroupDescriptor) c;
					
					contributeItem(
						new ActionGroupContributionItemAdapter(
							item.getId(),
							partDescriptor),
							actionBars);
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionItemProvider#contributeToPopupMenu(org.eclipse.jface.action.IMenuManager, org.eclipse.ui.IWorkbenchPart)
	 */
	public final void contributeToPopupMenu(
		IMenuManager popupMenu,
		IWorkbenchPart workbenchPart) {

		ISelection selection =
			workbenchPart.getSite().getSelectionProvider().getSelection();

		IWorkbenchPartDescriptor partDescriptor =
			new WorkbenchPartDescirptor(
				workbenchPart.getSite().getId(),
				workbenchPart.getClass(),
				workbenchPart.getSite().getPage());

		Iterator contributions =
			contributionDescriptor
				.getContributionsFor(popupMenu, selection)
				.iterator();

		while (contributions.hasNext()) {
			Object c = contributions.next();

			if (c
				instanceof ProviderContributionDescriptor.PopupMenuDescriptor) {
				ProviderContributionDescriptor.PopupMenuDescriptor item =
					(ProviderContributionDescriptor.PopupMenuDescriptor) c;

				contributeItem(
					new MenuContributionItemAdapter(
						item.getId(),
						partDescriptor),
					popupMenu,
					item.getPath(),
					item.getGroup());

			} else if (
				c
					instanceof ProviderContributionDescriptor.PopupMenuGroupDescriptor) {
				ProviderContributionDescriptor.PopupMenuGroupDescriptor item =
					(ProviderContributionDescriptor.PopupMenuGroupDescriptor) c;

				contributeItem(
					new MenuGroupContributionItemAdapter(
						item.getId(),
						item.isSeparator()),
					popupMenu,
					item.getPath(),
					item.getGroup());
			} else if (
				c
					instanceof ProviderContributionDescriptor.PopupActionDescriptor) {
				ProviderContributionDescriptor.PopupActionDescriptor item =
					(ProviderContributionDescriptor.PopupActionDescriptor) c;

				contributeItem(
					new ActionContributionItemAdapter(
						item.getId(),
						partDescriptor),
					popupMenu,
					item.getPath(),
					item.getGroup());
			} else if (
				c
					instanceof ProviderContributionDescriptor.PopupCustomDescriptor) {
				ProviderContributionDescriptor.PopupCustomDescriptor item =
					(ProviderContributionDescriptor.PopupCustomDescriptor) c;

				contributeItem(
					new CustomContributionItemAdapter(
						item.getId(),
						partDescriptor),
					popupMenu,
					item.getPath(),
					item.getGroup());
			} else if (
					c
						instanceof ProviderContributionDescriptor.PopupActionGroupDescriptor) {
					ProviderContributionDescriptor.PopupActionGroupDescriptor item =
						(ProviderContributionDescriptor.PopupActionGroupDescriptor) c;

					contributeItem(
						new ActionGroupContributionItemAdapter(
							item.getId(),
							partDescriptor),
						popupMenu,
						item.getPath(),
						item.getGroup());

			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionItemProvider#disposeContributions(org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	public final void disposeContributions(IWorkbenchPartDescriptor partDescriptor) {
		ActionRegistry registry = (ActionRegistry) actionCache.get(partDescriptor);
		if (registry != null) {
			registry.dispose();
		}
		actionCache.remove(partDescriptor);
		
		// dispose of the action group contributions
		actionGroupCache.dispose(partDescriptor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public final boolean provides(IOperation operation) {
		if (operation instanceof DisposeContributionsOperation) {
			IWorkbenchPartDescriptor partDescriptor =
				((DisposeContributionsOperation) operation)
					.getWorkbenchPartDescriptor();
			return actionCache.containsKey(partDescriptor) || actionGroupCache.contains(partDescriptor);
		}
		return false;
	}

	/**
	 * Returns the action with the given id that is created for the given part id.
	 * An action is created once and cached for each unique part's id.
	 * 
	 * @param actionId The request action id
	 * @param partDescriptor The workbench part descriptor
	 * @return The action with the given id
	 */
	protected final IAction getAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor) {
		ActionRegistry registry = (ActionRegistry) actionCache.get(partDescriptor);
		if (registry == null) {
			registry = new ActionRegistry();
			actionCache.put(partDescriptor, registry);
		}
		
		IAction action = getActionFromRegistry(actionId, partDescriptor, registry);
		if (action == null) {
			action = createAction(actionId, partDescriptor);
			if (action != null) {
				if (action instanceof IDisposableAction) {
					((IDisposableAction) action).init();
				}

				registry.registerAction(actionId, action);
			}
		}
		else {
			if ( action instanceof IRepeatableAction ) {
				((IRepeatableAction)action).refresh();
			}
		}
		return action;
	}
	/**
	 * Returns the ActionGroup with the given id that is created for the given part id
	 * An ActionGroup is created once and cached for each unique part's id
	 * 
	 * @param actionGroupId The request action group id
	 * @param partDescriptor The workbench part descriptor
	 * @return The ActionGroup with the given id
	 */
	protected final ActionGroup getActionGroup(String actionGroupId,
			IWorkbenchPartDescriptor partDescriptor) {
		ActionGroup actionGroup = actionGroupCache.getActionGroup(actionGroupId, partDescriptor);
		
		if (actionGroup == null) {
			actionGroup = createActionGroup(actionGroupId, partDescriptor);
			actionGroupCache.addActionGroup(actionGroupId, actionGroup, partDescriptor);
		}
		return actionGroup;
	}
	
	/**
	 * This method put in order to override the action registry caching. Used to
	 * get around unimplemented features of contribution item service.
	 * 
	 * @param actionId
	 *            the action id
	 * @param partDescriptor
	 *            the part descriptor
	 * @param registry
	 *            the action registry
	 * @return the action with <code>actionId</code> retrieved from the action
	 *         <code>registry</code>.
	 */
	protected IAction getActionFromRegistry(String actionId, IWorkbenchPartDescriptor partDescriptor, ActionRegistry registry){
		return registry.getAction(actionId);
	}

	/**
	 * Creates the action with the given id. By default, this method does
	 * nothing. Subclasses must override to provide their own implementation of
	 * action creation.
	 * 
	 * @param actionId
	 *            The action id
	 * @param partDescriptor
	 *            The workbench part descriptor
	 * @return The action with the given id
	 */
	protected IAction createAction(
		String actionId,
		IWorkbenchPartDescriptor partDescriptor) {
		return null;
	}

	/**
	 * Creates the action group for the given id. By default, this method does
	 * nothing. Subclasses must override to provide their own implementation of
	 * action group creation.
	 * 
	 * @param actionGroupId
	 *            The action group id
	 * @param partDescriptor
	 *            The workbench part descriptor
	 * @return Action Group for the given id
	 */
	protected ActionGroup createActionGroup(
		String actionGroupId,
		IWorkbenchPartDescriptor partDescriptor) {
		return null;
	}

	/**
	 * Creates the menu manager with the given id. By default, this method does
	 * nothing. Subclasses must override to provide their own implementation of
	 * menu manager creation.
	 * 
	 * @param menuId
	 *            The menu manager id
	 * @param partDescriptor
	 *            The workbench part descriptor
	 * @return The menu manager with the given id
	 */
	protected IMenuManager createMenuManager(
		String menuId,
		IWorkbenchPartDescriptor partDescriptor) {
		return null;
	}

	/**
	 * Creates the custom contribution with the given id. By default, this
	 * method does nothing. Subclasses must override to provide their own
	 * implementation of custom contribution creation.
	 * 
	 * @param customId
	 *            The custom contribution id
	 * @param partDescriptor
	 *            The workbench part descriptor
	 * @return The custom contribution with the given id
	 */
	protected IContributionItem createCustomContributionItem(
		String customId,
		IWorkbenchPartDescriptor partDescriptor) {
		return null;
	}

	/**
	 * Finds a menu manager using a '/' separated path.
	 * 
	 * @param parent
	 *            The starting contribution manager
	 * @param path
	 *            The '/' separated path
	 * @return A menu manager described by the given path
	 */
	private IMenuManager findMenuUsingPath(
		IContributionManager parent,
		String path) {

		IContributionItem item = null;
		String id = path;
		String rest = null;
		int separator = path.indexOf('/');
		if (separator != -1) {
			id = path.substring(0, separator);
			rest = path.substring(separator + 1);
		} else {
			item = parent.find(path);
			if (item instanceof IMenuManager)
				return (IMenuManager) item;
		}

		item = parent.find(id);
		if (item instanceof IMenuManager) {
			IMenuManager manager = (IMenuManager) item;
			return manager.findMenuUsingPath(rest);
		}
		return null;
	}

	/**
	 * Contributes the given item to the given manager in the given path/group.
	 * 
	 * @param contributionItem
	 *            The item to be contributed
	 * @param contributionManager
	 *            The manager to be contributed to
	 * @param path
	 *            The path of contribution within the manager
	 * @param group
	 *            The group of contribution within the path
	 */
	private void contributeItem(
		IAdaptable contributionItemAdapter,
		IContributionManager contributionManager,
		String path,
		String group) {

		// Find parent menu.
		if (path == null)
			return;
		IContributionManager parent = contributionManager;

		if (path.length() > 1) { // if path is more than '/'
			parent = findMenuUsingPath(parent, path.substring(1));
			if (parent == null) {
				Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The contribution item path is invalid"); //$NON-NLS-1$
				return;
			}
		}

		//if contributing a menu group
		if (contributionItemAdapter
			instanceof MenuGroupContributionItemAdapter) {
			IContributionItem contributionItem =
				(IContributionItem) contributionItemAdapter.getAdapter(
					IContributionItem.class);
			parent.add(contributionItem);
			return;
		}
		
		//if contributing an action group
		if (contributionItemAdapter
			instanceof ActionGroupContributionItemAdapter) {

			try {
				ActionGroup actionGroup = 
					(ActionGroup) contributionItemAdapter.getAdapter(
						ActionGroup.class);
	
				if (parent instanceof IMenuManager) {
					actionGroup.fillContextMenu((IMenuManager) parent);
				}
			} catch (IllegalArgumentException e) {
				Trace.catching(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING, CommonUIServicesActionPlugin.getDefault().getClass(), "Error adding contribution item", e); //$NON-NLS-1$
				Log.error(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "Error adding contribution item", e); //$NON-NLS-1$
			}

			return;
		}
		
		// Find reference group.
		if (group == null)
			return;
		IContributionItem sep = parent.find(group);
		if (sep == null) {
			if (group.equals(ContributionItemConstants.GROUP_ADDITIONS)) {
				sep = new Separator(group);
				parent.add(sep);
			}
			if (sep == null) {
				Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The contribution item group is invalid"); //$NON-NLS-1$
				return;
			}
		}

		// Add contribution to group
		try {
			IContributionItem contributionItem =
				(IContributionItem) contributionItemAdapter.getAdapter(
					IContributionItem.class);
			if (contributionItem != null) {
				if (sep.isGroupMarker())
					parent.appendToGroup(group, contributionItem);
				else
					parent.insertAfter(group, contributionItem);
			} else
				Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "Failed to create the contribution with id: " + (String) contributionItemAdapter.getAdapter(String.class)); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			Trace.catching(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING, CommonUIServicesActionPlugin.getDefault().getClass(), "Error adding contribution item", e); //$NON-NLS-1$
			Log.error(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "Error adding contribution item", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * Contributes the given item to the given manager in the given path/group.
	 * 
	 * @param contributionItem
	 *            The item to be contributed
	 * @param contributionManager
	 *            The manager to be contributed to
	 * @param path
	 *            The path of contribution within the manager
	 * @param group
	 *            The group of contribution within the path
	 */
	private void contributeItem(
		IAdaptable contributionItemAdapter,
		IActionBars actionBars) {

		//if contributing an action group
		if (contributionItemAdapter
			instanceof ActionGroupContributionItemAdapter) {

			try {
				ActionGroup actionGroup = 
					(ActionGroup) contributionItemAdapter.getAdapter(
						ActionGroup.class);
	
				actionGroup.fillActionBars(actionBars);
				return;
			} catch (IllegalArgumentException e) {
				Trace.catching(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING, CommonUIServicesActionPlugin.getDefault().getClass(), "Error adding contribution item", e); //$NON-NLS-1$
				Log.error(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "Error adding contribution item", e); //$NON-NLS-1$
			}
		}

		Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "Failed to create the contribution with id: " + (String) contributionItemAdapter.getAdapter(String.class)); //$NON-NLS-1$
	}

	/**
	 * An adapter for an action contribution item.
	 */
	private class ActionContributionItemAdapter implements IAdaptable {
		private String actionId;
		private IWorkbenchPartDescriptor partDescriptor;

		/**
		 * Creates an instance of <code>ActionContributionItemAdapter</code>.
		 * 
		 * @param actionId attribute for action ID
		 * @param partDescriptor attribute for partDescriptor
		 */
		public ActionContributionItemAdapter(
			String actionId,
			IWorkbenchPartDescriptor partDescriptor) {
			this.actionId = actionId;
			this.partDescriptor = partDescriptor;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IContributionItem.class) {
				IAction action = getAction(actionId, partDescriptor);
				if (action != null)
					return new ActionContributionItem(action);
			} else if (adapter == String.class) {
				return actionId;
			}
			return null;
		}
	}
	
	/**
	 * An adapter for an action group contribution item.
	 */
	private class ActionGroupContributionItemAdapter implements IAdaptable {
		private String menuId;
		private IWorkbenchPartDescriptor partDescriptor;

		/**
		 * Creates an instance of <code>ActionGroupContributionItemAdapter</code>
		 * @param menuId attribute for menu ID
		 * @param partDescriptor attribute for partDescriptor
		 */
		public ActionGroupContributionItemAdapter(
			String menuId,
			IWorkbenchPartDescriptor partDescriptor) {
			this.menuId = menuId;
			this.partDescriptor = partDescriptor;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == ActionGroup.class) {
				return getActionGroup(menuId, partDescriptor);
			} else if (adapter == String.class) {
				return menuId;
			}
			return null;
		}
	}

	/**
	 * An adapter for an menu contribution item.
	 */
	private class MenuContributionItemAdapter implements IAdaptable {
		private String menuId;
		private IWorkbenchPartDescriptor partDescriptor;

		/**
		 * Creates an instance of <code>MenuContributionItemAdapter</code>.
		 * 
		 * @param menuId attribute for menuID
		 * @param partDescriptor attribute for partDescriptor
		 */
		public MenuContributionItemAdapter(
			String menuId,
			IWorkbenchPartDescriptor partDescriptor) {
			this.menuId = menuId;
			this.partDescriptor = partDescriptor;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IContributionItem.class) {
				return createMenuManager(menuId, partDescriptor);
			} else if (adapter == String.class) {
				return menuId;
			}
			return null;
		}
	}

	/**
	 * An adapter for an menu group contribution item.
	 */
	private class MenuGroupContributionItemAdapter implements IAdaptable {
		private String groupId;
		private boolean isSeparator;

		/**
		 * Creates an instance of <code>MenuGroupContributionItemAdapter</code>.
		 * 
		 * @param groupId attribute for groupID
		 * @param isSeparator attribute for isSeparator
		 */
		public MenuGroupContributionItemAdapter(
			String groupId,
			boolean isSeparator) {
			this.groupId = groupId;
			this.isSeparator = isSeparator;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IContributionItem.class) {
				if (isSeparator)
					return new Separator(groupId);
				return new GroupMarker(groupId);
			} else if (adapter == String.class) {
				return groupId;
			}
			return null;
		}
	}

	/**
	 * An adapter for an custom contribution item.
	 */
	private class CustomContributionItemAdapter implements IAdaptable {
		private String customId;
		private IWorkbenchPartDescriptor partDescriptor;

		/**
		 * Creates an instance of <code>CustomContributionItemAdapter</code>.
		 * 
		 * @param customId attribute for customID
		 * @param partDescriptor attribute for partDescriptor
		 */
		public CustomContributionItemAdapter(
			String customId,
			IWorkbenchPartDescriptor partDescriptor) {
			this.customId = customId;
			this.partDescriptor = partDescriptor;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IContributionItem.class) {
				IContributionItem item =
					createCustomContributionItem(customId, partDescriptor);
				item.update();
				return item;
			} else if (adapter == String.class) {
				return customId;
			}
			return null;
		}
	}

}
