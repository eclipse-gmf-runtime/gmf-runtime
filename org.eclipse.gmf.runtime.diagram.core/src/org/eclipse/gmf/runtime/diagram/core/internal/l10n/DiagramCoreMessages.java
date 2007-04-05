/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.diagram.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class DiagramCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages";//$NON-NLS-1$

	private DiagramCoreMessages() {
		// Do not instantiate
	}

	public static String AddCommand_Label;
	public static String DeleteCommand_Label;
	public static String Command_ChangeViewProperty_ChangePropertyPattern;
	public static String AbstractViewProvider_create_view_failed_ERROR_;
    
    public static String BringForwardCommand_Label;
    public static String BringToFrontCommand_Label;
    public static String SendBackwardCommand_Label;
    public static String SendToBackCommand_Label;
    
    public static String GroupCommand_Label;
    public static String UngroupCommand_Label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramCoreMessages.class);
	}
}