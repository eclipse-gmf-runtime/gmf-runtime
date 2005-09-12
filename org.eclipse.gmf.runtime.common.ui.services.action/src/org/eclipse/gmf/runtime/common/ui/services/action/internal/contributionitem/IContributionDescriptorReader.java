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
