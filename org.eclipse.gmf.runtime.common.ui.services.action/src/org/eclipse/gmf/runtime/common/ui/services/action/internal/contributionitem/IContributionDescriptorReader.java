/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem;


/**
 * A package-protected interface for providers wishing to read the contribution
 * descriptor built by parsing the contribution information in the provider's extension.
 * 
 * @author melaasar
 */
public interface IContributionDescriptorReader {

	/**
	 * Sets the contribution descriptor of the provider.
	 * 
	 * @param descriptor the contribution descriptor of the provider
	 */
	public void setContributionDescriptor(ProviderContributionDescriptor descriptor);

}
