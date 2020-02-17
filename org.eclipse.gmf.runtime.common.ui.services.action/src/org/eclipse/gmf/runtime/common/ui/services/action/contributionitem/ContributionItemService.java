/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.contributionitem;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ContributeToActionBarsOperation;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ContributeToPopupMenuOperation;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.DisposeContributionsOperation;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionDescriptorReader;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.ProviderContributionDescriptor;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.UpdateActionBarsOperation;
import org.eclipse.gmf.runtime.common.ui.services.util.ActivityFilterProviderDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartDescriptor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbenchPart;
import org.osgi.framework.Bundle;

/**
 * A service for contributing items into different <code>IWorkbenchPart</code>'s 
 * contribution managers: ActionBars and ContextMenu(s).
 * 
 * @see IContributionItemProvider
 * @see AbstractContributionItemProvider
 * 
 * @author melaasar, cmahoney
 */
public class ContributionItemService
	extends Service
	implements IContributionItemProvider {

	/**
	 * A descriptor for <code>IContributionItemProvider</code> defined
	 * by a configuration element.
	 */
	protected static class ProviderDescriptor
		extends ActivityFilterProviderDescriptor {

		private static final String CHECK_PLUGIN_LOADED = "checkPluginLoaded"; //$NON-NLS-1$

		/** the provider contribution descriptor parsed from XML */
		private ProviderContributionDescriptor contributionDescriptor;
		/** a flag to check if plugin is loaded */
		private boolean checkPluginLoaded = true;

		/**
		 * Constructs a <code>IContributionItemProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			String s = element.getAttribute(CHECK_PLUGIN_LOADED);
			if (s != null)
				this.checkPluginLoaded = Boolean.valueOf(s).booleanValue();

			this.contributionDescriptor =
				ProviderContributionDescriptor.parse(element);
			assert null != contributionDescriptor : "contributionDescriptor cannot be null"; //$NON-NLS-1$
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {

			// If contributions are to be disposed, dispose them regardless whether the plugin is loaded or not 
			if (operation instanceof DisposeContributionsOperation) {
				if (provider != null)
					return provider.provides(operation);
			}
			
			// filter providers based on disabled capabilities
			if (!super.provides(operation)) {
				return false;
			}
			
			// if asked to check that the plugin is loaded and it is not, ignore
			if (checkPluginLoaded && !isPluginLoaded()) {
				return false;
			}
			
			// if no XML contributions, forward to the provider 
			if (!contributionDescriptor.hasContributions()) {
				if (!policyInitialized){
					policyInitialized = true;
					policy = getPolicy();
				}
				if (policy!=null) {
					return getPolicy().provides(operation);
				}
				if (getProvider() != null) {
					return getProvider().provides(operation);
				}
			}

			if (operation instanceof ContributeToActionBarsOperation) {
				ContributeToActionBarsOperation op =
					(ContributeToActionBarsOperation) operation;
				return contributionDescriptor.hasContributionsFor(
					op.getWorkbenchPartDescriptor().getPartId(),
					op.getWorkbenchPartDescriptor().getPartClass());
			} else if (operation instanceof ContributeToPopupMenuOperation) {
				ContributeToPopupMenuOperation op =
					(ContributeToPopupMenuOperation) operation;
				ISelection selection =
					op
						.getWorkbenchPart()
						.getSite()
						.getSelectionProvider()
						.getSelection();
				return contributionDescriptor.hasContributionsFor(
					op.getPopupMenu(),
					selection);
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service.ProviderDescriptor#getProvider()
		 */
		public IProvider getProvider() {
			if (provider == null) {
				IProvider newProvider = super.getProvider();
				if (provider instanceof IContributionDescriptorReader) {
					IContributionDescriptorReader reader =
						(IContributionDescriptorReader) newProvider;
					reader.setContributionDescriptor(contributionDescriptor);
				}
				if (provider instanceof AbstractContributionItemProvider) {
					((AbstractContributionItemProvider) provider)
						.setPluginContribution(new IPluginContribution() {

							public String getLocalId() {
								return getElement().getDeclaringExtension()
									.getSimpleIdentifier();
							}

							public String getPluginId() {
								return getElement().getContributor().getName();
							}
						});
				}
				return newProvider;
			}
			return super.getProvider();
		}

		/**
		 * Verify if the declaring plugin of the provider is loaded; if it is not loaded, add a listener that.
		 * 
		 * @return <code>true</code> if the declaring pluging of the propety
		 *         provider is loaded, <code>false</code> otherwise
		 */
		private boolean isPluginLoaded() {
			if (!getElement().isValid())
				return false;
			String pluginId = getElement().getDeclaringExtension().getContributor().getName();
			Bundle bundle = Platform.getBundle(pluginId);
			return null != bundle
				&& bundle.getState() == org.osgi.framework.Bundle.ACTIVE;

		}
	}

	/**
	 * The single instance of the contribution item service. 
	 */
	private static final ContributionItemService instance =
		new ContributionItemService();

	/**
	 * Returns the single instanceo of the <code>ContributionItemService</code>.
	 * 
	 * @return The single instance of the <code>ContributionItemService</code>
	 */
	public static ContributionItemService getInstance() {
		return instance;
	}

	/**
	 * Creates a new <code>ContributionItemService</code> instance.
	 */
	private ContributionItemService() {
		 super();
		 configureProviders(CommonUIServicesActionPlugin.getPluginId(), "contributionItemProviders"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}

	/**
	 * Makes contributions to the given <code>actionBars</code> that belong to the given part.
	 * @param actionBars the action bars
	 * @param workbenchPart the workbench part
	 * @see #contributeToActionBars(IActionBars, IWorkbenchPartDescriptor)
	 */
	public void contributeToActionBars(
		IActionBars actionBars,
		IWorkbenchPart workbenchPart) {
		contributeToActionBars(
			actionBars,
			new WorkbenchPartDescriptor(
				workbenchPart.getSite().getId(),
				workbenchPart.getClass(),
				workbenchPart.getSite().getPage()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionItemProvider#contributeToActionBars(org.eclipse.ui.IActionBars, org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	public void contributeToActionBars(
		IActionBars actionBars,
		IWorkbenchPartDescriptor workbenchPartDescriptor) {
		execute(new ContributeToActionBarsOperation(actionBars,
				workbenchPartDescriptor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionItemProvider#contributeToPopupMenu(org.eclipse.jface.action.IMenuManager,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void contributeToPopupMenu(
		IMenuManager popupMenu,
		IWorkbenchPart workbenchPart) {
		execute(new ContributeToPopupMenuOperation(popupMenu, workbenchPart));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IContributionItemProvider#disposeContributions(org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	public void disposeContributions(IWorkbenchPartDescriptor workbenchPartDescriptor) {
		execute(new DisposeContributionsOperation(workbenchPartDescriptor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem.IContributionItemProvider#updateActionBars(org.eclipse.ui.IActionBars, org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	public void updateActionBars(IActionBars actionBars,
			IWorkbenchPartDescriptor workbenchPartDescriptor) {
		execute(new UpdateActionBarsOperation(actionBars,
			workbenchPartDescriptor));
	}
	
	/**
	 * Disposes of the contributions that have been made to the <code>workbenchPart</code>.
	 * @param workbenchPart the workbench part
	 */
	public void disposeContributions(IWorkbenchPart workbenchPart) {
		execute(
			new DisposeContributionsOperation(
				new WorkbenchPartDescriptor(
					workbenchPart.getSite().getId(),
					workbenchPart.getClass(),
					workbenchPart.getSite().getPage())));

	}

	/**
	 * Executes the operation with the REVERSE strategy.
	 * 
	 * @param operation the operation to be executed.
	 */
	private void execute(IOperation operation) {
		execute(ExecutionStrategy.REVERSE, operation);
	}

		}
