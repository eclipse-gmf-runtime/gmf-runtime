/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonUIServicesDNDMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.ui.services.dnd.internal.l10n.CommonUIServicesDNDMessages";//$NON-NLS-1$

	private CommonUIServicesDNDMessages() {
		// Do not instantiate
	}

	public static String DelegatingDragSourceAdapter_errorMessage;
	public static String DelegatingDragSourceAdapter__ERROR__errorMessage;
	public static String DelegatingDropTargetAdapter_errorMessage;
	public static String DelegatingDropTargetAdapter__ERROR__errorMessage;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonUIServicesDNDMessages.class);
	}
}
