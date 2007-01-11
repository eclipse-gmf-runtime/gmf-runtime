/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IPopupMenuContributionPolicy;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionStatusCodes;
import org.eclipse.gmf.runtime.common.ui.util.IPartSelector;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * A descriptor for an XML-based contribution made by a provider of
 * contribution items. This class parses an <code>IConfigurationElement</code> 
 * that is associated with a given extension of a contribution item provider 
 * and builds the contribution descriptor in memeory for a more convenient access
 * 
 * @author melaasar
 */
public class ProviderContributionDescriptor extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String PART_CONTRIBUTION = "partContribution"; //$NON-NLS-1$
	private static final String PART_MENU_CONTRIBUTION = "partMenu"; //$NON-NLS-1$
	private static final String PART_MENUGROUP_CONTRIBUTION = "partMenuGroup"; //$NON-NLS-1$
	private static final String PART_ACTION_CONTRIBUTION = "partAction"; //$NON-NLS-1$
	private static final String PART_ACTIONGROUP_CONTRIBUTION = "partActionGroup"; //$NON-NLS-1$
	private static final String PART_CUSTOM_CONTRIBUTION = "partCustom"; //$NON-NLS-1$
	private static final String POPUP_CONTRIBUTION = "popupContribution"; //$NON-NLS-1$
	private static final String POPUP_MENU_CONTRIBUTION = "popupMenu"; //$NON-NLS-1$
	private static final String POPUP_MENUGROUP_CONTRIBUTION = "popupMenuGroup"; //$NON-NLS-1$
	private static final String POPUP_ACTION_CONTRIBUTION = "popupAction"; //$NON-NLS-1$
	private static final String POPUP_ACTIONGROUP_CONTRIBUTION = "popupActionGroup"; //$NON-NLS-1$
	private static final String POPUP_CUSTOM_CONTRIBUTION = "popupCustom"; //$NON-NLS-1$
    private static final String POPUP_PREDEFINED_ITEM = "popupPredefinedItem"; //$NON-NLS-1$
	private static final String STRUCTURED_CRITERIA = "popupStructuredContributionCriteria"; //$NON-NLS-1$
	private static final String TEXT_CRITERIA = "popupTextContributionCriteria"; //$NON-NLS-1$
	private static final String MARK_CRITERIA = "popupMarkContributionCriteria"; //$NON-NLS-1$
	private static final String CONTRIBUTION_ID = "id"; //$NON-NLS-1$
	private static final String CONTRIBUTION_TOOLBAR_PATH = "toolbarPath"; //$NON-NLS-1$
	private static final String CONTRIBUTION_MENUBAR_PATH = "menubarPath"; //$NON-NLS-1$
	private static final String CONTRIBUTION_PATH = "path"; //$NON-NLS-1$
	private static final String MENUGROUP_SEPARATOR = "separator"; //$NON-NLS-1$
	private static final String OBJECT_CLASS = "objectClass"; //$NON-NLS-1$
	private static final String OBJECT_COUNT = "objectCount"; //$NON-NLS-1$
	private static final String POLICY_CLASS = "policyClass"; //$NON-NLS-1$
	private static final String GLOBAL = "global"; //$NON-NLS-1$
	private static final String TEXT = "text"; //$NON-NLS-1$
	private static final String DOCUMENT_CLASS = "documentClass"; //$NON-NLS-1$
    private static final String REMOVE = "remove"; //$NON-NLS-1$

	/** the list of all part contributions made by a provider */
	private List partContributions = new ArrayList();

	/** the list of all popup menu contributions made by a provider */
	private List popupContributions = new ArrayList();

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private ProviderContributionDescriptor(IConfigurationElement configElement) {
		IConfigurationElement configChildren[] = configElement.getChildren();
		if (configChildren.length <= 1)
			Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The contribution item provider has zero contributions"); //$NON-NLS-1$

		// sort out the contributions into part and popup related
		for (int i = 1; i < configChildren.length; i++) {
			if (configChildren[i].getName().equals(PART_CONTRIBUTION)) {
				partContributions.add(
					new PartContributionDescriptor(configChildren[i]));
			} else if (
				configChildren[i].getName().equals(POPUP_CONTRIBUTION)) {
				popupContributions.add(
					new PopupContributionDescriptor(configChildren[i]));
			}
		}
	}

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element.
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static ProviderContributionDescriptor parse(IConfigurationElement configElement) {
		assert null != configElement : "null provider configuration element"; //$NON-NLS-1$
		return new ProviderContributionDescriptor(configElement);
	}

	/**
	 * Determines if the descriptor has any XML-based contributions.
	 * 
	 * @return Whether the descriptor contains contribution descriptors or not
	 */
	public boolean hasContributions() {
		return !partContributions.isEmpty() || !popupContributions.isEmpty();
	}

	/**
	 * Determines if the provider contribution descriptor has contributions
	 * for a part with the given id and class.
	 * 
	 * @param partId The target part's id
	 * @param partClass The target part's class
	 * @return whether contribution are available or not
	 */
	public boolean hasContributionsFor(String partId, Class partClass) {
		assert null != partId : "null part id"; //$NON-NLS-1$
		assert null != partClass : "null part class"; //$NON-NLS-1$

		Iterator iter = partContributions.iterator();
		while (iter.hasNext()) {
			PartContributionDescriptor contribution =
				(PartContributionDescriptor) iter.next();
			if (contribution.appliesTo(partId, partClass))
				return true;
		}
		return false;
	}

	/**
	 * Determines if the provider contribution descriptor has contributions
	 * for a given popup menu with a given selection as a context.
	 * 
	 * @param popupMenu The target popup menu manager
	 * @param selection The menu context (selection)
	 * @return whether contribution are available or not
	 */
	public boolean hasContributionsFor(
		IMenuManager popupMenu,
		ISelection selection) {
		assert null != popupMenu : "null popupMenu"; //$NON-NLS-1$
		assert null != selection : "null selection"; //$NON-NLS-1$

		String popupId = popupMenu.getId();
		Class popupClass = popupMenu.getClass();

		Iterator iter = popupContributions.iterator();
		while (iter.hasNext()) {
			PopupContributionDescriptor contribution =
				(PopupContributionDescriptor) iter.next();
			if (contribution.appliesTo(popupId, popupClass, selection))
				return true;
		}
		return false;
	}

	/**
	 * Gets a list of contributions available in the descriptor for a given 
	 * part with the given id and class.
	 * 
	 * @param partId The target part's id
	 * @param partClass The target part's class
	 * @return a list of contributions
	 */
	public List getContributionsFor(String partId, Class partClass) {
		assert null != partId : "null part id"; //$NON-NLS-1$
		assert null != partClass : "null part class"; //$NON-NLS-1$

		List contributions = new ArrayList();
		Iterator iter = partContributions.iterator();
		while (iter.hasNext()) {
			PartContributionDescriptor contribution =
				(PartContributionDescriptor) iter.next();
			if (contribution.appliesTo(partId, partClass)) {
				contributions.addAll(contribution.getContributionItems());
			}
		}
		return contributions;
	}

	/**
	 * Gets a list of contributions available in the descriptor for a given 
	 * popup menu with a given selection as a context.
	 * 
	 * @param popupMenu The target popup menu manager
	 * @param selection The menu context (selection)
	 * @return a list of contributions
	 */
	public List getContributionsFor(
		IMenuManager popupMenu,
		ISelection selection) {
		assert null != popupMenu : "null popupMenu"; //$NON-NLS-1$
		assert null != selection : "null selection"; //$NON-NLS-1$

		List contributions = new ArrayList();
		Iterator iter = popupContributions.iterator();
		while (iter.hasNext()) {
			PopupContributionDescriptor contribution =
				(PopupContributionDescriptor) iter.next();
			if (contribution
				.appliesTo(
					popupMenu.getId(),
					popupMenu.getClass(),
					selection)) {
				contributions.addAll(contribution.getContributionItems());
			}
		}
		return contributions;
	}

	/**
	 * An abstract descriptor for a contribution made in XML by 
	 * a contribution item provider. 
	 */
	private static abstract class AbstractContributionDescriptor {
		/** the target id */
		private final String targetId;
		/** the target class name */
		private final String targetClassName;
		/** the list of items contributed by this descriptor */
		private List contributionItems = new ArrayList();

		/**
		 * Initializes a new contribution descriptor by reading the target
		 * id and class from the contribution configuration element.
		 * 
		 * @param configElement The contribution configuration element
		 */
		public AbstractContributionDescriptor(IConfigurationElement configElement) {
			targetId = configElement.getAttribute(ID);
			targetClassName = configElement.getAttribute(CLASS);
			if (targetId == null && targetClassName == null)
				Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "Both the target id and class are missing for the contribution"); //$NON-NLS-1$
		}

		/**
		 * Determines whether this contribution is applicable to the given source id & class.
		 * 
		 * @param sourceId The source id
		 * @param sourceClass The source class
		 * @return <code>true</code> if applicable <code>false</code> if not
		 */
		protected boolean appliesTo(String sourceId, Class sourceClass) {
			if (targetId != null && sourceId != null) {
				return targetId.equals(sourceId);
			}
			if (targetClassName != null && sourceClass != null) {
				return isAssignableTo(sourceClass, targetClassName);
			}
			return false;
		}

		/**
		 * Returns the list of contribution items provided in this descriptor.
		 * 
		 * @return a List of contributions items provided by this descriptor
		 */
		public List getContributionItems() {
			return contributionItems;
		}

	}

	/**
	 * A descriptor for a part contribution made by a contribution item provider.
	 */
	private static class PartContributionDescriptor
		extends AbstractContributionDescriptor {

		/**
		 * Constructs a new descriptor for a part contribution 
		 * by parsing all the contirbution items from a configuration element.
		 * 
		 * @param configElement The contribution configuration element
		 */
		public PartContributionDescriptor(IConfigurationElement configElement) {
			super(configElement);

			IConfigurationElement configChildren[] =
				configElement.getChildren();
			if (configChildren.length <= 0)
				Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The part contribution has zero contribution items"); //$NON-NLS-1$

			for (int i = 0; i < configChildren.length; i++) {
				String contributionType = configChildren[i].getName();
				if (contributionType.equals(PART_MENU_CONTRIBUTION))
					getContributionItems().add(
						new PartMenuDescriptor(configChildren[i]));
				else if (contributionType.equals(PART_MENUGROUP_CONTRIBUTION))
					getContributionItems().add(
						new PartMenuGroupDescriptor(configChildren[i]));
				else if (contributionType.equals(PART_ACTION_CONTRIBUTION))
					getContributionItems().add(
						new PartActionDescriptor(configChildren[i]));
				else if (contributionType.equals(PART_CUSTOM_CONTRIBUTION))
					getContributionItems().add(
						new PartCustomDescriptor(configChildren[i]));
				else if (contributionType.equals(PART_ACTIONGROUP_CONTRIBUTION))
					getContributionItems().add(
						new PartActionGroupDescriptor(configChildren[i]));
                  
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ProviderContributionDescriptor.AbstractContributionDescriptor#appliesTo(java.lang.String, java.lang.Class)
		 */
		public boolean appliesTo(String sourceId, Class sourceClass) {
			return super.appliesTo(sourceId, sourceClass);
		}
	}

	/**
	 * A descriptor for a popup menu contribution made by a contribution item provider.
	 */
	private static class PopupContributionDescriptor
		extends AbstractContributionDescriptor {
		/** an optional popup menu contribution criteria */
		private PopupContributionCriteria[] criteria;

		/**
		 * Constructs a new descriptor for a popup menu contribution 
		 * by parsing all the contirbution items from a configuration element.
		 * 
		 * @param configElement The contribution configuration element
		 */
		public PopupContributionDescriptor(IConfigurationElement configElement) {
			super(configElement);

			criteria = readCriteria(configElement);

			IConfigurationElement configChildren[] =
				configElement.getChildren();
			if (configChildren.length <= 0)
				Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The part contribution has zero contribution items"); //$NON-NLS-1$

			for (int i = 0; i < configChildren.length; i++) {
				String contributionType = configChildren[i].getName();
				if (contributionType.equals(POPUP_MENU_CONTRIBUTION))
					getContributionItems().add(
						new PopupMenuDescriptor(configChildren[i]));
				else if (contributionType.equals(POPUP_MENUGROUP_CONTRIBUTION))
					getContributionItems().add(
						new PopupMenuGroupDescriptor(configChildren[i]));
				else if (contributionType.equals(POPUP_ACTION_CONTRIBUTION))
					getContributionItems().add(
						new PopupActionDescriptor(configChildren[i]));
				else if (contributionType.equals(POPUP_CUSTOM_CONTRIBUTION))
					getContributionItems().add(
						new PopupCustomDescriptor(configChildren[i]));
				else if (contributionType.equals(POPUP_ACTIONGROUP_CONTRIBUTION))
					getContributionItems().add(
						new PopupActionGroupDescriptor(configChildren[i]));
               else if (contributionType.equals(POPUP_PREDEFINED_ITEM))
                    getContributionItems().add(
                        new PopupPredefinedItemDescriptor(configChildren[i]));

			}
		}

		/**
		 * Reads the contribution criteria if any. Depending on the type of
		 * criteria, the correct descriptor will be instantiated.
		 * @param configElement the configuration element
		 * @return the popup contribution criteria
		 */
		protected PopupContributionCriteria[] readCriteria(IConfigurationElement configElement) {
			IConfigurationElement[] criteriaEl;

			criteriaEl = configElement.getChildren(STRUCTURED_CRITERIA);
			if (criteriaEl.length > 0) {
				PopupContributionCriteria[] pcc =
					new PopupContributionCriteria[criteriaEl.length];
				for (int i = 0; i < criteriaEl.length; i++) {
					pcc[i] =
						new PopupStructuredContributionCriteria(criteriaEl[i]);
				}
				return pcc;
			}
			criteriaEl = configElement.getChildren(TEXT_CRITERIA);
			if (criteriaEl.length > 0) {
				PopupContributionCriteria[] pcc =
					new PopupContributionCriteria[criteriaEl.length];
				for (int i = 0; i < criteriaEl.length; i++) {
					pcc[i] = new PopupTextContributionCriteria(criteriaEl[i]);
				}
				return pcc;
			}
			criteriaEl = configElement.getChildren(MARK_CRITERIA);
			if (criteriaEl.length > 0) {
				PopupContributionCriteria[] pcc =
					new PopupContributionCriteria[criteriaEl.length];
				for (int i = 0; i < criteriaEl.length; i++) {
					pcc[i] = new PopupMarkContributionCriteria(criteriaEl[i]);
				}
				return pcc;
			}
			return null;
		}

		/**
		 * Determines whether this contribution is applicable to the given source 
		 * id & class and for the given selection.
		 * 
		 * @param sourceId The source id
		 * @param sourceClass The source class
		 * @param selection The selection (context)
		 * @return <code>true</code> if it applies and <code>false</code> if not
		 */
		public boolean appliesTo(
			String sourceId,
			Class sourceClass,
			ISelection selection) {
			if (!appliesTo(sourceId, sourceClass))
				return false;
			if (criteria != null) {
				if (!isCriteriaMet(selection))
					return false;
			}
			return true;
		}

		/**
		 * Determines if at least one of the criteria is met in the given
		 * <code>selection</code>.
		 * 
		 * @param selection
		 *            the selection
		 * @return <code>true</code> if at least one criteria is met,
		 *         <code>false/code> otherwise
		 */
		protected boolean isCriteriaMet(ISelection selection) {
			for (int i = 0; i < criteria.length; i++) {
				if (criteria[i].appliesTo(selection))
					return true;
			}
			return false;
		}
	}

	/**
	 * An abstract descriptor for a contribution item by a contribution 
	 * item provider through XML.
	 */
	private static abstract class AbstractContributionItemDescriptor {
		/** the contribution item id */
		private String id;

		/**
		 * Contructs a new contribution item descriptor by extracting the item's id
		 * from the configuration element.
		 * 
		 * @param configElement The configuration element
		 */
		public AbstractContributionItemDescriptor(IConfigurationElement configElement) {
			this.id = configElement.getAttribute(CONTRIBUTION_ID);
			assert null != id : "The contribution item's id is missing"; //$NON-NLS-1$
		}

		/**
		 * Returns the contribution item id.
		 * 
		 * @return The id of the contribution item
		 */
		public String getId() {
			return id;
		}

		/**
		 * A utility method to extract the contribution item's menu path within
		 * its target manager from a location in the configuration.
		 * 
		 * @param location The supplied location in the configuration
		 * @return The contribution item's menu path in its target manager
		 */
		protected static String extractMenuPath(String location) {
			if (location != null) {
				int loc = location.lastIndexOf('/');
				if (loc != -1) {
					return location.substring(0, loc == 0 ? 1 : loc);
				}
			}
			return null;
		}

		/**
		 * A utility method to extract the contribution item's group within
		 * its target manager from a location in the configuration.
		 * 
		 * @param location The supplied location in the configuration
		 * @return The contribution item's group in its target manager
		 */
		protected static String extractGroup(String location) {
			if (location != null) {
				int loc = location.lastIndexOf('/');
				if (loc != -1) {
					return location.substring(loc + 1);
				}
			}
			return null;
		}
	}

	/**
	 * A descriptor for a part contribution item.
	 */
	private static abstract class AbstractPartContributionItemDescriptor
		extends AbstractContributionItemDescriptor implements IPartSelector {
		/** the contribution item's menubar path */
		private String menubarPath;
		/** the contribution item's menubar group */
		private String menubarGroup;
		/** the contribution item's toolbar path */
		private String toolbarPath;
		/** the contribution item's toolbar group */
		private String toolbarGroup;
		/** the contribution item's part ID, if specified */
		private String targetId;
		/** the contribution item's part class name or interface name, if specified */
		private String targetClassName;

		/**
		 * Constructs a new part contribution item from its configuration element.
		 * 
		 * @param configElement The item's configuration element
		 */
		public AbstractPartContributionItemDescriptor(IConfigurationElement configElement) {
			super(configElement);

			String location;

			location = configElement.getAttribute(CONTRIBUTION_MENUBAR_PATH);
			if (location != null) {
				menubarPath = extractMenuPath(location);
				menubarGroup = extractGroup(location);
			}

			location = configElement.getAttribute(CONTRIBUTION_TOOLBAR_PATH);
			if (location != null) {
				toolbarPath = extractMenuPath(location);
				toolbarGroup = extractGroup(location);
			}	

			// extract the part ID or class name from the parent element
			Object parent = configElement.getParent();
			if (parent instanceof IConfigurationElement) {
				IConfigurationElement parentElement = ((IConfigurationElement) parent);
				targetId = parentElement.getAttribute(ID);
				targetClassName = parentElement.getAttribute(CLASS);
			}
		}

		/**
		 * Returns the menubar path if any.
		 * 
		 * @return The menubar path if any
		 */
		public String getMenubarPath() {
			return menubarPath;
		}

		/**
		 * Returs the menubar group if any.
		 * 
		 * @return The menubar group if any
		 */
		public String getMenubarGroup() {
			return menubarGroup;
		}

		/**
		 * Returns the toolbar path if any.
		 * 
		 * @return The toolbar path if any
		 */
		public String getToolbarPath() {
			return toolbarPath;
		}

		/**
		 * Returs the toolbar group if any.
		 * 
		 * @return The toolbar group if any
		 */
		public String getToolbarGroup() {
			return toolbarGroup;
		}		
		
		/**
		 * Determines whether or not this contribution is applicable to the
		 * given workbench <code>part</code>.
		 * 
		 * @param part
		 *            the workbench part to be tested
		 * @return <code>true</code> if applicable, <code>false</code> if
		 *         not
		 */
		public boolean selects(IWorkbenchPart part) {

			IWorkbenchPartSite site = part.getSite();
			if (site != null) {
				String partId = site.getId();
				if (targetId != null && partId != null) {
					return targetId.equals(partId);
				}
			}

			Class partClass = part.getClass();
			if (targetClassName != null && partClass != null) {
				return isAssignableTo(partClass, targetClassName);
			}
			return false;
		}
	}

	/**
	 * A descriptor for a popup menu contribution item.
	 */
	public static abstract class AbstractPopupContributionItemDescriptor
		extends AbstractContributionItemDescriptor implements IPartSelector {
		/** the contribution item's path */
		private String path;
		/** the contribution item's group */
		private String group;

		/**
		 * Constructs a new part contribution item from its configuration element.
		 * 
		 * @param configElement The item's configuration element
		 */
		public AbstractPopupContributionItemDescriptor(IConfigurationElement configElement) {
			super(configElement);

			String location = configElement.getAttribute(CONTRIBUTION_PATH);
			if (location != null) {
				path = extractMenuPath(location);
				group = extractGroup(location);
			} else {
				path = "/"; //$NON-NLS-1$
				group = ContributionItemConstants.GROUP_ADDITIONS;
			}
		}

		/**
		 * Returns the contribution item's path if any.
		 * 
		 * @return The contribution item's path if any
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Returs the contribution item's group if any.
		 * 
		 * @return The contribution item's group if any
		 */
		public String getGroup() {
			return group;
		}
		
		/**
		 * Always returns <code>false</code>.
		 * <P>
		 * Popup contributions are always re-contributed when the menu is about
		 * to be shown, so there is no need for them to listen for selection
		 * change on the workbench part.
		 */
		public boolean selects(IWorkbenchPart part) {
			return false;
		}
	}

	/**
	 * A descriptor for a part menu contribution item.
	 */
	public static class PartMenuDescriptor
		extends AbstractPartContributionItemDescriptor {

		/**
		 * Constructs a new part menu descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PartMenuDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}

	/**
	 * A descriptor for a part menu group contribution item.
	 */
	public static class PartMenuGroupDescriptor
		extends AbstractPartContributionItemDescriptor {
		/** whether this menu group is a separator */
		private Boolean separator;

		/**
		 * Constructs a new part menu group descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PartMenuGroupDescriptor(IConfigurationElement configElement) {
			super(configElement);
			String sep = configElement.getAttribute(MENUGROUP_SEPARATOR);
			separator = sep == null ? Boolean.TRUE : Boolean.valueOf(sep);
		}

		/**
		 * Returns whether this menu group descriptor is also a separator.
		 * 
		 * @return <code>true</code> if separator and <code>false</code> if not
		 */
		public boolean isSeparator() {
			return separator.booleanValue();
		}
	}

	/**
	 * A descriptor for a part action contribution item.
	 */
	public static class PartActionDescriptor
		extends AbstractPartContributionItemDescriptor {

		/** whether this action is a global one */
		private Boolean isGlobal;

		/**
		 * Constructs a new part action descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PartActionDescriptor(IConfigurationElement configElement) {
			super(configElement);
			String global = configElement.getAttribute(GLOBAL);
			isGlobal = global == null ? Boolean.FALSE : Boolean.valueOf(global);
		}

		/**
		 * Whether this is a global action
		 * 
		 * @return <code>true</code> if global, <code>false</code> otherwise
		 */
		public boolean isGlobal() {
			return isGlobal.booleanValue();
		}

	}
	
	/**
	 * A descriptor for a part action group contribution item.
	 */
	public static class PartActionGroupDescriptor
		extends AbstractPartContributionItemDescriptor {

		/**
		 * Constructs a new popup action group descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PartActionGroupDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}

	/**
	 * A descriptor for a part custom contribution item.
	 */
	public static class PartCustomDescriptor
		extends AbstractPartContributionItemDescriptor {
		/**
		 * Constructs a new part custom descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PartCustomDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}

	/**
	 * A descriptor for a popup menu contribution item.
	 */
	public static class PopupMenuDescriptor
		extends AbstractPopupContributionItemDescriptor {

		/**
		 * Constructs a new popup menu descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PopupMenuDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}

	/**
	 * A descriptor for a popup menu group contribution item.
	 */
	public static class PopupMenuGroupDescriptor
		extends AbstractPopupContributionItemDescriptor {
		/** whether this menu group is a separator */
		private Boolean separator;

		/**
		 * Constructs a new popup menu group descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PopupMenuGroupDescriptor(IConfigurationElement configElement) {
			super(configElement);
			String sep = configElement.getAttribute(MENUGROUP_SEPARATOR);
			separator = sep == null ? Boolean.TRUE : Boolean.valueOf(sep);
		}

		/**
		 * Returns whether this menu group descriptor is also a separator.
		 * 
		 * @return <code>true</code> if separator and <code>false</code> if not
		 */
		public boolean isSeparator() {
			return separator.booleanValue();
		}

	}

	/**
	 * A descriptor for a popup action contribution item.
	 */
	public static class PopupActionDescriptor
		extends AbstractPopupContributionItemDescriptor {

		/**
		 * Constructs a new popup action descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PopupActionDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}

	/**
	 * A descriptor for a popup action group contribution item.
	 */
	public static class PopupActionGroupDescriptor
		extends AbstractPopupContributionItemDescriptor {

		/**
		 * Constructs a new popup action group descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PopupActionGroupDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}

	/**
	 * A descriptor for a popup custom contribution item.
	 */
	public static class PopupCustomDescriptor
		extends AbstractPopupContributionItemDescriptor {
		/**
		 * Constructs a new popup custom descriptor from its configration element.
		 * 
		 * @param configElement The contribution's configuration element
		 */
		public PopupCustomDescriptor(IConfigurationElement configElement) {
			super(configElement);
		}
	}
    
    /**
     * A descriptor for a predefined contribution item.
     */
    public static class PopupPredefinedItemDescriptor
        extends AbstractContributionItemDescriptor {
        
        /** the contribution item's path */
        private String path;
        
        /** flag to remove the predefined contribution item */
        private boolean remove;

        /**
         * Constructs a new popup custom descriptor from its configration element.
         * 
         * @param configElement The contribution's configuration element
         */
        public PopupPredefinedItemDescriptor(IConfigurationElement configElement) {
            super(configElement);
            
            String location = configElement.getAttribute(CONTRIBUTION_PATH);
            path = (location == null) ? "/" //$NON-NLS-1$
                : extractMenuPath(location);
            remove = Boolean.valueOf(configElement.getAttribute(REMOVE))
                .booleanValue();
        }
        
        /**
         * Returns the contribution item's path if any.
         * 
         * @return The contribution item's path if any
         */
        public String getPath() {
            return path;
        }
        
        public boolean isToBeRemoved() {
            return remove;
        }
        
    }
	/**
	 * The popup menu contribution criteria.  Currently the following criteria are supported:
	 * 1) Whether a given contribution policy applies to the selection.
	 */
	private static class PopupContributionCriteria {
		/** the criteria configuration element */
		private IConfigurationElement configElement;
		/** the criteria's policy class name */
		private String policyClassName;
		/** the 'loaded' policy class */
		private IPopupMenuContributionPolicy policy = null;

		/**
		 * Constructs a new popup menu contribution criteria from a configuration element.
		 * 
		 * @param configElement The criteria's configuration element
		 */
		public PopupContributionCriteria(IConfigurationElement configElement) {
			this.configElement = configElement;
			this.policyClassName = configElement.getAttribute(POLICY_CLASS);
		}

		/**
		 * Determines if the contribution criteria applies to the given selection.
		 * 
		 * @param selection The selection in question
		 * @return whether it applies to it or not
		 */
		public boolean appliesTo(ISelection selection) {
			if (policyClassName != null) {
				IPopupMenuContributionPolicy thePolicy = getPolicy();
				if (thePolicy == null
					|| !thePolicy.appliesTo(selection, configElement))
					return false;
			}
			return true;
		}

		/**
		 * Loads up the policy class (if any) from the configuration element.
		 * 
		 * @return The policy class (if any) from the contribution element
		 */
		protected IPopupMenuContributionPolicy getPolicy() {
			if (null == policy) {
				try {
					Object extension =
						configElement.createExecutableExtension(
                            POLICY_CLASS);
					if (extension instanceof IPopupMenuContributionPolicy)
						policy = (IPopupMenuContributionPolicy) extension;
					else
						Log.info(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionStatusCodes.SERVICE_FAILURE, "The supplied policy class name does not implement IPopupMenuContributionPolicy"); //$NON-NLS-1$
				} catch (CoreException ce) {
					Trace.catching(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getPolicy", ce); //$NON-NLS-1$
					Log.log(
						CommonUIServicesActionPlugin.getDefault(),
						ce.getStatus().getSeverity(),
						CommonUIServicesActionStatusCodes.SERVICE_FAILURE,
						ce.getStatus().getMessage(),
						ce.getStatus().getException());
				}
			}
			return policy;
		}
	}

	/**
	 * The popup menu contribution criteria. Currently the following criteria are supported:
	 * <OL>
	 * <LI>Whether all objects in the selection are assignable from a certain class/interface or adapt to it</LI>
	 * <LI>Whether the number of objects in the selection matches a given number</LI>
	 * </OL>
	 * 
	 * All criteria are optional and more could be added in the future.
	 */
	private static class PopupStructuredContributionCriteria
		extends PopupContributionCriteria {
		/** the object descriptor */
		private ObjectDescriptor object;
		/** the criteria's target count */
		private Integer targetCount;
		/** 'true' if numbers > the provided one should work */
		private boolean orHigher;

		/**
		 * Constructs a new popup menu contribution criteria from a configuration element.
		 * 
		 * @param configElement The criteria's configuration element
		 */
		public PopupStructuredContributionCriteria(IConfigurationElement configElement) {
			super(configElement);

			object =
				new ObjectDescriptor(
					configElement,
					OBJECT_CLASS);

			String countStr = configElement.getAttribute(OBJECT_COUNT);
			
			this.orHigher = false;
			
			if (countStr != null) {
				if (countStr.equals("*")) { //$NON-NLS-1$
					this.targetCount = Integer.valueOf("0"); //$NON-NLS-1$
					this.orHigher = true;
				}
				else if (countStr.equals("+")) { //$NON-NLS-1$
					this.targetCount = Integer.valueOf("1"); //$NON-NLS-1$
					this.orHigher = true;
				}
				else {
					int plusIndex = countStr.lastIndexOf("+"); //$NON-NLS-1$
					if (plusIndex > 0) {
						this.orHigher = true;
						countStr = countStr.substring(0, plusIndex);
					}
					
					// Since the string is 'unsafe' we'll wrap the conversion in a try/catch block
					try {
						this.targetCount = Integer.valueOf(countStr);
					} catch (NumberFormatException e) {
						// TODO Log the exception
						this.orHigher = true;
						this.targetCount = Integer.valueOf("1"); //$NON-NLS-1$
					}
				}
			}
		}

		/**
		 * Determines if the contribution criteria applies to the given selection.
		 * 
		 * @param selection The selection in question
		 * @return whether it applies to it or not
		 */
		public boolean appliesTo(ISelection selection) {
			if (!(selection instanceof IStructuredSelection))
				return false;

			IStructuredSelection structuredSelection =
				(IStructuredSelection) selection;

			Iterator objects = structuredSelection.iterator();
			while (objects.hasNext()) {
				if (!object.sameAs(objects.next()))
					return false;
			}
			if (targetCount != null) {
				// If 'orHigher' is set then hide if the selection count is < the targetCount
				if (orHigher) {
					if (structuredSelection.size() < targetCount.intValue())
						return false;
				}
				else {
					if (structuredSelection.size() != targetCount.intValue())
						return false;
				}
			}
			return super.appliesTo(selection);
		}
	}

	/**
	 * The popup menu contribution criteria. Currently the following criteria are supported:
	 * <OL>
	 * <LI>Whether the selection has to have a given text</LI>
	 * </OL>
	 * 
	 * All criteria are optional and more could be added in the future.
	 */
	private static class PopupTextContributionCriteria
		extends PopupContributionCriteria {
		/** the text descriptor */
		private String text;

		/**
		 * Constructs a new popup menu contribution criteria from a configuration element.
		 * 
		 * @param configElement The criteria's configuration element
		 */
		public PopupTextContributionCriteria(IConfigurationElement configElement) {
			super(configElement);

			text = configElement.getAttribute(TEXT);
		}

		/**
		 * Determines if the contribution criteria applies to the given selection.
		 * 
		 * @param selection The selection in question
		 * @return whether it applies to it or not
		 */
		public boolean appliesTo(ISelection selection) {
			if (!(selection instanceof ITextSelection))
				return false;

			ITextSelection textSelection = (ITextSelection) selection;

			if (text != null) {
				if (!text.equals(textSelection.getText()))
					return false;
			}
			return super.appliesTo(selection);
		}
	}

	/**
	 * The popup menu contribution criteria. Currently the following criteria are supported:
	 * <OL>
	 * <LI>Whether the mark document conforms to a given document descriptor</LI>
	 * </OL>
	 * 
	 * All criteria are optional and more could be added in the future.
	 */
	private static class PopupMarkContributionCriteria
		extends PopupContributionCriteria {
		/** the document descriptor */
		private ObjectDescriptor document;

		/**
		 * Constructs a new popup menu contribution criteria from a configuration element.
		 * 
		 * @param configElement The criteria's configuration element
		 */
		public PopupMarkContributionCriteria(IConfigurationElement configElement) {
			super(configElement);

			document =
				new ObjectDescriptor(
					configElement,
					DOCUMENT_CLASS);
		}

		/**
		 * Determines if the contribution criteria applies to the given selection.
		 * 
		 * @param selection The selection in question
		 * @return whether it applies to it or not
		 */
		public boolean appliesTo(ISelection selection) {
			if (!(selection instanceof IMarkSelection))
				return false;

			IMarkSelection markSelection = (IMarkSelection) selection;

			if (!document.sameAs(markSelection.getDocument()))
				return false;

			return super.appliesTo(selection);
		}
	}
}
