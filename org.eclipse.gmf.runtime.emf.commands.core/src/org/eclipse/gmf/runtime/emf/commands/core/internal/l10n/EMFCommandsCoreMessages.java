/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.emf.commands.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class EMFCommandsCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.emf.commands.core.internal.l10n.EMFCommandsCoreMessages";//$NON-NLS-1$

	private EMFCommandsCoreMessages() {
		// Do not instantiate
	}

	public static String AbstractModelCommand__ERROR__abandonedActionErrorMessage;
	public static String editingDomainContext;
    
	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFCommandsCoreMessages.class);
	}
}
