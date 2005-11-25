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
package org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class DiagramUIRenderMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n.DiagramUIRenderMessages";//$NON-NLS-1$

	private DiagramUIRenderMessages() {
		// Do not instantiate
	}

	public static String CopyAction_ErrorDialogTitle;
	public static String CopyAction_UnableToCopyImageMessage;

	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramUIRenderMessages.class);
	}
}