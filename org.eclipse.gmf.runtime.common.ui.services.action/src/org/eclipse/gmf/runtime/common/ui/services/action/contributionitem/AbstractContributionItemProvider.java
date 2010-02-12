/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.action.IActionWithProgress;
import org.eclipse.gmf.runtime.common.ui.action.IDisposableAction;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ContributionItemConstants;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.DisposeContributionsOperation;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionDescriptorReader;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ProviderContributionDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.ActionGroupCache;
import org.eclipse.gmf.runtime.common.ui.util.ActivityUtil;
import org.eclipse.gmf.runtime.common.ui.util.IPartSelector;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartDescriptor;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionGroup;

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
 * @author melaasar, cmahoney
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
	 * Identifies the plugin where the contributions were made so that these
	 * contributions can be filtered if an activity/capability associated with
	 * the plugin is disabled.
	 */
	private IPluginContribution pluginContribution;
    
	/**
	 * A list of part descriptors ids for which actionbar contributions have
	 * already been made.
	 */
	private Set partDescriptors = new HashSet();

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
		
		contributeToActionBars(actionBars, partDescriptor, false);
	}
	
	/**
	 * Contributes to the given action bars that belong to a part described with
	 * the given part descriptor.
	 * 
	 * <p>
	 * Note: There are currently issues with updating the main menu manager when
	 * multiple editors are open. If contributing to a menu manager that the
	 * contribution item service contributed (e.g. the diagram menu), when
	 * updating it does not always find the instance of the menu manager
	 * associated with the correct editor.
	 * 
	 * @param actionBars
	 *            The target action bars
	 * @param workbenchPartDescriptor
	 *            The context workbench part descriptor
	 * @param updateOnly
	 *            If true, this is called when only updating the actionbars and
	 *            not when the editor is first opened.
	 */
	private void contributeToActionBars(IActionBars actionBars,
			IWorkbenchPartDescriptor partDescriptor, boolean updateOnly) {

		partDescriptors.add(partDescriptor);

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

				if (!updateOnly) {
					contributeItem(
						new MenuContributionItemAdapter(
							item.getId(),
							partDescriptor),
						actionBars.getMenuManager(),
						item.getMenubarPath(),
						item.getMenubarGroup());
				}

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

				if (!updateOnly) {
					contributeItem(
						new MenuGroupContributionItemAdapter(
							item.getId(),
							item.isSeparator()),
						actionBars.getMenuManager(),
						item.getMenubarPath(),
						item.getMenubarGroup());
				}

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

				if (!updateOnly) {
					contributeItem(
						new ActionContributionItemAdapter(
							item.getId(),
							partDescriptor, 
							item),
						actionBars.getMenuManager(),
						item.getMenubarPath(),
						item.getMenubarGroup());
				}

				contributeItem(
					new ActionContributionItemAdapter(
						item.getId(),
						partDescriptor, 
						item),
					actionBars.getToolBarManager(),
					item.getToolbarPath(),
					item.getToolbarGroup());

				if (item.isGlobal())
					actionBars.setGlobalActionHandler(
						item.getId(),
						getAction(item.getId(), partDescriptor, item));
			} else if (
				c
					instanceof ProviderContributionDescriptor.PartCustomDescriptor) {
				ProviderContributionDescriptor.PartCustomDescriptor item =
					(ProviderContributionDescriptor.PartCustomDescriptor) c;

				if (!updateOnly) {
					contributeItem(
						new CustomContributionItemAdapter(
							item.getId(),
							partDescriptor),
						actionBars.getMenuManager(),
						item.getMenubarPath(),
						item.getMenubarGroup());
				}

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
			} else if (c instanceof ProviderContributionDescriptor.PartPredefinedItemDescriptor) {
                ProviderContributionDescriptor.PartPredefinedItemDescriptor item = (ProviderContributionDescriptor.PartPredefinedItemDescriptor) c;

                if (item.isToBeRemovedFromToolbar()) {
                	removeExistingItem(item.getId(), item.getToolbarPath(), actionBars.getToolBarManager(), false);
                }
                if (item.isToBeRemovedFromMenubar()) {
                    removeExistingItem(item.getId(), item.getMenubarPath(), actionBars.getMenuManager(), true);
                }
			}
		}
	}

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionItemProvider#updateActionBars(org.eclipse.ui.IActionBars,
     *      org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
     */
	public final void updateActionBars(IActionBars actionBars,
			IWorkbenchPartDescriptor partDescriptor) {

		if (!partDescriptors.contains(partDescriptor)) {
			contributeToActionBars(actionBars, partDescriptor, true);
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
			new WorkbenchPartDescriptor(
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
						partDescriptor,
						item),
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
            } else if (c instanceof ProviderContributionDescriptor.PopupPredefinedItemDescriptor) {
                ProviderContributionDescriptor.PopupPredefinedItemDescriptor item = (ProviderContributionDescriptor.PopupPredefinedItemDescriptor) c;

                if (item.isToBeRemoved()) {
                    removeExistingItem(item.getId(), item.getPath(), popupMenu, false);
                }
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
		
		partDescriptors.remove(partDescriptor);
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
			if ( action instanceof IActionWithProgress ) {
				((IActionWithProgress)action).refresh();
			}
		}
		return action;
	}

	/**
	 * Returns the action with the given id that is created for the given part
	 * id. An action is created once and cached for each unique part's id.
	 * <P>
	 * The part selector is used is when the new action is an
	 * <code>AbstractActionHandler</code> to determine whether or not the
	 * action is applicable to a given selected part. If the part is not
	 * applicable, the action will not be refreshed when selection changes in
	 * the part.
	 * 
	 * @param actionId
	 *            The request action id
	 * @param partDescriptor
	 *            The workbench part descriptor
	 * @param partSelector
	 *            The part selector
	 * @return The action with the given id
	 */
	protected final IAction getAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor, IPartSelector partSelector) {

        boolean actionExistsAlready = false;
        ActionRegistry registry = (ActionRegistry) actionCache.get(partDescriptor);
        if (registry != null) {
            if (getActionFromRegistry(actionId, partDescriptor, registry) != null) {
                actionExistsAlready = true;
            }
        }
        
		IAction result = getAction(actionId, partDescriptor);
        
        // If the action already existed in the registry and this is a popup
        // menu contribution, we do not want to override the part selector
        // already set as we could override the part selector for a toolbar
        // action.  See bugzilla#157471.
        if (actionExistsAlready
            && partSelector instanceof ProviderContributionDescriptor.AbstractPopupContributionItemDescriptor) {
            return result;
        }
		
		if (result instanceof AbstractActionHandler && partSelector != null) {
			((AbstractActionHandler) result).setPartSelector(partSelector);
		}
		return result;
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
     * @param id
     * @param path
     * @param contributionManager
     */
    private void removeExistingItem(String id, String path,
            IContributionManager contributionManager, boolean useIdForRemoval) {

        // Find the menu or action or group.
        if (id == null)
            return;
        
        IContributionManager parent = contributionManager;
        if (path.length() > 1) { // if path is more than '/'
            parent = findMenuUsingPath(contributionManager, path.substring(1));
            if (parent == null) {
                Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The contribution item path is invalid"); //$NON-NLS-1$
                return;
            }
        }

        IContributionItem predefinedItem = parent.find(id);
        if (predefinedItem == null) {
            Log.info(CommonUIServicesActionPlugin.getDefault(),
                CommonUIServicesActionStatusCodes.SERVICE_FAILURE,
                "The contribution item path is invalid"); //$NON-NLS-1$
            return;
        }

         if (predefinedItem instanceof AbstractGroupMarker) {
            IContributionItem allItems[] = parent.getItems();
            int groupIndex;
            for (groupIndex = 0; groupIndex < allItems.length; groupIndex++) {
                IContributionItem item = allItems[groupIndex];
                if (item.equals(predefinedItem)) {
                    break;
                }
            }
            for (int j = groupIndex + 1; j < allItems.length; j++) {
                IContributionItem item = allItems[j];
                if (item instanceof AbstractGroupMarker) {
                    break;
                }
                parent.remove(item);
            }

        }
        // parent.remove(item) and parent.remove(item.getId()) yield different results in some cases 
        // parent.remove(item) seems to be working for all cases except for removing a menu from menu bar (item defined as partMenu)
        if (useIdForRemoval) {
        	parent.remove(predefinedItem.getId());
        } else {
        	parent.remove(predefinedItem);
        }
    }
    
	/**
     * An adapter for an action contribution item.
     */
	private class ActionContributionItemAdapter implements IAdaptable {
		private String actionId;
		private IWorkbenchPartDescriptor partDescriptor;
		private final IPartSelector partSelector;
		
		/**
		 * Creates an instance of <code>ActionContributionItemAdapter</code>.
		 * 
		 * @param actionId attribute for action ID
		 * @param partDescriptor attribute for partDescriptor
         * @param partSelector selects parts that match this contribution item
		 */
		public ActionContributionItemAdapter(
			String actionId,
			IWorkbenchPartDescriptor partDescriptor,
			IPartSelector partSelector) {
			
			this.actionId = actionId;
			this.partDescriptor = partDescriptor;
			this.partSelector = partSelector;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IContributionItem.class) {
				IAction action = getAction(actionId, partDescriptor, partSelector);
				if (action != null) {
					return new PluginActionContributionItem(action);
				}
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
                IMenuManager manager = createMenuManager(menuId, partDescriptor);
                if (manager != null) {
                    return new PluginMenuManager(manager);
                }
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
					return new PluginSeparator(groupId);
				return new PluginGroupMarker(groupId);
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
				return item;
			} else if (adapter == String.class) {
				return customId;
			}
			return null;
		}
	}
	
	/**
     * Checks if there are activities that have been matched to the plug-in or
     * id in which the item has been contributed and if at least one of those
     * matching activities are enabled.
     * 
     * @return true if at least one matching activity is enabled
     */
    private boolean areActivitiesEnabled(final String itemID) {
        // check if the provider has been matched to a disabled activity id
        if (!ActivityUtil.isEnabled(getPluginContribution().getLocalId(),
            getPluginContribution().getPluginId())) {
            return false;
        }

        // now check if the item has been matched to a disabled activity id
        if (!ActivityUtil.isEnabled(itemID, getPluginContribution()
            .getPluginId())) {
            return false;
        }

        return true;
    }

	/**
	 * Sets the plugin contribution which identifies the plugin where the
	 * contributions were made so that these contributions can be filtered if an
	 * activity/capability associated with the plugin is disabled.
	 * 
	 * @param pluginContribution
	 *            the plugin contribution
	 */
	void setPluginContribution(IPluginContribution pluginContribution) {
		this.pluginContribution = pluginContribution;
	}

	/**
	 * Gets the plugin contribution which identifies the plugin where the
	 * contributions were made so that these contributions can be filtered if an
	 * activity/capability associated with the plugin is disabled.
	 * 
	 * @return the plugin contribution
	 */
	IPluginContribution getPluginContribution() {
		return pluginContribution;
	}

	/**
	 * An <code>IMenuManager</code> whose visibility is affected by
	 * activites/capabilities.
	 * 
	 * @author cmahoney
	 */
	private class PluginMenuManager
		extends MenuManager {

		private IMenuManager realMenuManager;

		public PluginMenuManager(IMenuManager menuManager) {
			this.realMenuManager = menuManager;
		}

		public void add(IAction action) {
			realMenuManager.add(action);
		}

		public void add(IContributionItem item) {
			realMenuManager.add(item);
		}

		public void addMenuListener(IMenuListener listener) {
			realMenuManager.addMenuListener(listener);
		}

		public void appendToGroup(String groupName, IAction action) {
			realMenuManager.appendToGroup(groupName, action);
		}

		public void appendToGroup(String groupName, IContributionItem item) {
			realMenuManager.appendToGroup(groupName, item);
		}

		public void dispose() {
			realMenuManager.dispose();
		}

		public void fill(Composite parent) {
			realMenuManager.fill(parent);
		}

		public void fill(CoolBar parent, int index) {
			realMenuManager.fill(parent, index);
		}

		public void fill(Menu parent, int index) {
			realMenuManager.fill(parent, index);
		}

		public void fill(ToolBar parent, int index) {
			realMenuManager.fill(parent, index);
		}

		public IContributionItem find(String id) {
			return realMenuManager.find(id);
		}

		public IMenuManager findMenuUsingPath(String path) {
			return realMenuManager.findMenuUsingPath(path);
		}

		public IContributionItem findUsingPath(String path) {
			return realMenuManager.findUsingPath(path);
		}

		public String getId() {
			return realMenuManager.getId();
		}

		public IContributionItem[] getItems() {
			return realMenuManager.getItems();
		}

		public IContributionManagerOverrides getOverrides() {
			return realMenuManager.getOverrides();
		}

		public boolean getRemoveAllWhenShown() {
			return realMenuManager.getRemoveAllWhenShown();
		}

		public void insertAfter(String id, IAction action) {
			realMenuManager.insertAfter(id, action);
		}

		public void insertAfter(String id, IContributionItem item) {
			realMenuManager.insertAfter(id, item);
		}

		public void insertBefore(String id, IAction action) {
			realMenuManager.insertBefore(id, action);
		}

		public void insertBefore(String id, IContributionItem item) {
			realMenuManager.insertBefore(id, item);
		}

		public String getMenuText() {
			String text = null;
			if(realMenuManager instanceof MenuManager) {
				text = ((MenuManager)realMenuManager).getMenuText();
			}
			return text;
		}

		public boolean isDirty() {
			return realMenuManager.isDirty();
		}

		public boolean isDynamic() {
			return realMenuManager.isDynamic();
		}

		public boolean isEmpty() {
			return realMenuManager.isEmpty();
		}

		public boolean isEnabled() {
			return realMenuManager.isEnabled();
		}

		public boolean isGroupMarker() {
			return realMenuManager.isGroupMarker();
		}

		public boolean isSeparator() {
			return realMenuManager.isSeparator();
		}

		public boolean isVisible() {
			if (!areActivitiesEnabled(getId())) {
				return false;
			}
			return realMenuManager.isVisible();
		}

		public void markDirty() {
			realMenuManager.markDirty();
		}

		public void prependToGroup(String groupName, IAction action) {
			realMenuManager.prependToGroup(groupName, action);
		}

		public void prependToGroup(String groupName, IContributionItem item) {
			realMenuManager.prependToGroup(groupName, item);
		}

		public IContributionItem remove(IContributionItem item) {
			return realMenuManager.remove(item);
		}

		public IContributionItem remove(String id) {
			return realMenuManager.remove(id);
		}

		public void removeAll() {
			realMenuManager.removeAll();
		}

		public void removeMenuListener(IMenuListener listener) {
			realMenuManager.removeMenuListener(listener);
		}

		public void saveWidgetState() {
			realMenuManager.saveWidgetState();
		}

		public void setParent(IContributionManager parent) {
			realMenuManager.setParent(parent);
		}

		public void setRemoveAllWhenShown(boolean removeAll) {
			realMenuManager.setRemoveAllWhenShown(removeAll);
		}

		public void setVisible(boolean visible) {
			realMenuManager.setVisible(visible);
		}

		public void update() {
			realMenuManager.update();
		}

		public void update(boolean force) {
			realMenuManager.update(force);
		}

		public void update(String id) {
			realMenuManager.update(id);
		}

		public void updateAll(boolean force) {
			realMenuManager.updateAll(force);
		}
	}

	/**
	 * A <code>Separator</code> whose visibility is affected by
	 * activites/capabilities.
	 * 
	 * @author cmahoney
	 */
	private class PluginSeparator
		extends Separator {

		public PluginSeparator(String groupName) {
			super(groupName);
		}

		public boolean isVisible() {
			if (!areActivitiesEnabled(getId())) {
				return false;
			}
			return super.isVisible();
		}

	}

	/**
	 * A <code>GroupMarker</code> whose visibility is affected by
	 * activites/capabilities.
	 * 
	 * @author cmahoney
	 */
	private class PluginGroupMarker
		extends GroupMarker {

		public PluginGroupMarker(String groupName) {
			super(groupName);
		}

		public boolean isVisible() {
			if (!areActivitiesEnabled(getId())) {
				return false;
			}
			return super.isVisible();
		}

	}

	/**
	 * An <code>ActionContributionItem</code> whose visibility is affected by
	 * activites/capabilities.
	 * 
	 * @author cmahoney
	 */
	private class PluginActionContributionItem
		extends ActionContributionItem {

		public PluginActionContributionItem(IAction action) {
			super(action);
		}

		public boolean isVisible() {
			if (!areActivitiesEnabled(getId())) {
				return false;
			}
			return super.isVisible();
		}
		
	}
    
    
}

