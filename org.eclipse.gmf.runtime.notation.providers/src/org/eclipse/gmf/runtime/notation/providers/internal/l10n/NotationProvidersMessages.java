/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.notation.providers.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class NotationProvidersMessages
	extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.notation.providers.internal.l10n.NotationProvidersMessages";//$NON-NLS-1$

	private NotationProvidersMessages() {
		// Do not instantiate
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, NotationProvidersMessages.class);
	}
}
