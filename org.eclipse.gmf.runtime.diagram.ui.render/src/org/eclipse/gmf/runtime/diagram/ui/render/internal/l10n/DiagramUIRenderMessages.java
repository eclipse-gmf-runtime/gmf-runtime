/*******************************************************************************
 * Copyright (c) 2000, 2005, 2006 IBM Corporation and others.
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
	public static String CopyToImageAction_Label;
	public static String CopyToImageAction_copyToImageErrorDialogTitle;
	public static String CopyToImageAction_copyToImageErrorDialogMessage;
	public static String CopyToImageAction_copyingDiagramToImageFileMessage;
	public static String CopyToImageAction_copyingSelectedElementsToImageFileMessage;
	public static String CopyToImageAction_outOfMemoryMessage;
	public static String CopyToImageAction_overwriteExistingConfirmDialogTitle;
	public static String CopyToImageAction_overwriteExistingConfirmDialogMessage;


	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramUIRenderMessages.class);
	}
}