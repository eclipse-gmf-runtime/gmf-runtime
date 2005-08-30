/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.icon;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.services.internal.icon.IconOperation;
import org.eclipse.gmf.runtime.common.ui.services.internal.icon.IconServiceProviderConfiguration;

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
			assert null != element : "NULL configuration element"; //$NON-NLS-1$
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (getPolicy() != null)
				return getPolicy().provides(operation);
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
		/* empty method body */
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
     * Executes the operation using the FIRST strategy.
     * 
     * @param operation the operation
     * @return Object the result
     */
    private Object execute(IconOperation operation) {
        List results = execute(ExecutionStrategy.FIRST, operation);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider#getIcon(IAdaptable, int)
     */
    public Image getIcon(IAdaptable hint, int flags) {
        return (Image) execute(new GetIconOperation(hint, flags));
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
