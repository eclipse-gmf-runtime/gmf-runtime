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

package org.eclipse.gmf.runtime.common.ui.services.icon;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;
import org.eclipse.gmf.runtime.common.ui.services.internal.icon.IconServiceProviderConfiguration;
import org.eclipse.swt.graphics.Image;

/**
 * Service which handles icon operations.
 * 
 * @author Michael Yee
 */
public class IconService extends Service implements IIconProvider {

	/** The singleton instance of the icon service. */
    private final static IconService _instance = new IconService();

    /**
	 * A descriptor for <code>IIconProvider</code> defined
	 * by a configuration element.
	 * 
	 * @author chmahone
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private IconServiceProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>IIconProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration =
				IconServiceProviderConfiguration.parse(element);
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (!policyInitialized){
				policy = getPolicy();
				policyInitialized = true;
			}
			if (policy != null)
				return policy.provides(operation);
			if (provider == null) {
				if (isSupportedInExtention(operation)) {
					providerConfiguration = null;
					return getProvider().provides(operation);
				}
				return false;
			}
			return getProvider().provides(operation);
		}

		/**
		 * Checks if the operation is supported by the XML extension.
		 * 
		 * @param operation
		 * @return <code>true</code> if the operation is supported, <code>false</code> otherwise
		 */
		private boolean isSupportedInExtention(IOperation operation) {
			if (operation instanceof GetIconOperation) {
				GetIconOperation o = (GetIconOperation) operation;
				return providerConfiguration.supports(o.getHint());
			}
			return false;
		}
	}
	
    /**
     * The IconService constructor
     */
    private IconService() {
		super();
		configureProviders(CommonUIServicesPlugin.getPluginId(), "iconProviders"); //$NON-NLS-1$
    }

    /**
     * Retrieves the singleton instance of the IconService.
     * 
     * @return IconService the IconService singleton instance 
     */
    public static IconService getInstance() {
        return _instance;
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider#getIcon(IAdaptable, int)
     */
    public Image getIcon(IAdaptable hint, int flags) {
        return (Image)executeUnique(ExecutionStrategy.FIRST, new GetIconOperation(hint, flags));
    }

    /**
     * Convenience method for getting an icon for an element.
     * 
     * @param hint argument adaptable to IElement or IElementTypeInfo
     * @return Image the image
     */
    public Image getIcon(IAdaptable hint) {
        return getIcon(hint, IconOptions.NONE.intValue());
    }

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}
}
