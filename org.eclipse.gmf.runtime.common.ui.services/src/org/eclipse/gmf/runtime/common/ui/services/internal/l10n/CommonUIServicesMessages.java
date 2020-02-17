/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonUIServicesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.ui.services.internal.l10n.CommonUIServicesMessages";//$NON-NLS-1$

	private CommonUIServicesMessages() {
		// Do not instantiate
	}

	public static String ElementSelectionService_ProgressName;
    public static String ElementSelectionService_JobName;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonUIServicesMessages.class);
	}
}
