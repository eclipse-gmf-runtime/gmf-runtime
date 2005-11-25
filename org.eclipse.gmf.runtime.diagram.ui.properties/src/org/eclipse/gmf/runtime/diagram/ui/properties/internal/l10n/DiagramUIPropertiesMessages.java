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
package org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class DiagramUIPropertiesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesMessages";//$NON-NLS-1$

	private DiagramUIPropertiesMessages() {
		// Do not instantiate
	}

	public static String DiagramGeneralDetails_nameLabel_text;
	public static String DiagramGeneralDetails_diagramTypeLabel_text;
	public static String DiagramGeneralDetails_diagramDescriptionLabel_text;
	public static String DiagramGeneralDetails_nameChangeCommand_text;
	public static String DiagramGeneralDetails_diagramDescriptionChangeCommand_text;
	public static String FontAndColor_nameLabel;
	public static String FontColor_commandText;
	public static String FillColor_commandText;
	public static String LineColor_commandText;
	public static String Font_commandText;
	public static String ConnectionAppearanceDetails_AvoidObstaclesLabel_Text;
	public static String AppearanceDetails_AvoidObstaclesCommand_Text;
	public static String ConnectionAppearanceDetails_ClosestDistanceLabel_Text;
	public static String AppearanceDetails_ClosestDistanceCommand_Text;
	public static String ConnectionAppearanceDetails_RouterOptionsLabel_Text;
	public static String ConnectionAppearanceDetails_JumpLinksLabel_Text;
	public static String AppearanceDetails_JumpLinksCommand_Text;
	public static String ConnectionAppearanceDetails_JumpLinkTypeLabel_Text;
	public static String ConnectionAppearanceDetails_JumpLinkGroupLabel_Text;
	public static String AppearanceDetails_JumpLinkTypeCommand_Text;
	public static String ConnectionAppearanceDetails_LineRouterLabel_Text;
	public static String AppearanceDetails_LineRouterCommand_Text;
	public static String ConnectionAppearanceDetails_ReverseJumpLinksLabel_Text;
	public static String AppearanceDetails_ReverseJumpLinksCommand_Text;
	public static String ConnectionAppearanceDetails_SmoothnessLabel_Text;
	public static String AppearanceDetails_SmoothnessCommand_Text;
	public static String ColorPalettePopup_default;
	public static String ColorPalettePopup_custom;

	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramUIPropertiesMessages.class);
	}
}