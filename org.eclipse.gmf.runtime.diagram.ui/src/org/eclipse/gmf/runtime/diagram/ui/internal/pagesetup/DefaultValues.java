/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import java.util.Locale;

/**
 * Contains default values that will be used to initialize printing
 * preferences declared in WorkspaceViewerProperties.PREF.  Two sets of preferences 
 * exist: one set in workspace and one set in diagram preference store.
 * 
 * @author etworkow
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class DefaultValues {

	/** Represents default value for WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS. */
	public static boolean DEFAULT_USE_WORKSPACE_SETTINGS = true;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_USE_DIAGRAM_SETTINGS. */
	public static boolean DEFAULT_USE_DIAGRAM_SETTINGS = false;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_USE_INCHES. */
	public static boolean DEFAULT_INCHES = true;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_USE_MILLIM. */
	public static boolean DEFAULT_MILLIM = false;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_USE_PORTRAIT. */
	public static boolean DEFAULT_PORTRAIT = true;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_USE_LANDSCAPE. */
	public static boolean DEFAULT_LANDSCAPE = false;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_PAGE_SIZE. */
	public static String DEFAULT_PAGE_SIZE = getLocaleSpecificPageType().getName();
	
	/** Represents default value for WorkspaceViewerProperties.PREF_PAGE_HEIGHT. */
	public static double DEFAULT_PAGE_WIDTH = getLocaleSpecificPageType().getWidth();
	
	/** Represents default value for WorkspaceViewerProperties.PREF_PAGE_WIDTH. */
	public static double DEFAULT_PAGE_HEIGHT = getLocaleSpecificPageType().getHeight();
	
	/** Represents default value for WorkspaceViewerProperties.PREF_MARGIN_TOP. */
	public static double DEFAULT_MARGIN_TOP = 0.5;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_MARGIN_BOTTOM. */
	public static double DEFAULT_MARGIN_BOTTOM = 0.5;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_MARGIN_LEFT. */
	public static double DEFAULT_MARGIN_LEFT = 0.5;
	
	/** Represents default value for WorkspaceViewerProperties.PREF_MARGIN_RIGHT. */
	public static double DEFAULT_MARGIN_RIGHT = 0.5;
			
	/** Enforce a minimum margin to take printer limitations into account */
	public static double MINIMUM_MARGIN_TOP = 0.25;
	
	/** Enforce a minimum margin to take printer limitations into account */
	public static double MINIMUM_MARGIN_BOTTOM = 0.25;
	
	/** Enforce a minimum margin to take printer limitations into account */
	public static double MINIMUM_MARGIN_LEFT = 0.25;
	
	/** Enforce a minimum margin to take printer limitations into account */
	public static double MINIMUM_MARGIN_RIGHT = 0.25;
	
	/**
	 * Returns locale specific page size.
	 * 
	 * @return PSPageType PSPageType.LETTER on US/Canada locale, PSPageType.A4 otherwise.
	 */
	static public PageSetupPageType getLocaleSpecificPageType() {
    	String defaultCountry = Locale.getDefault().getCountry(); 
        if (defaultCountry != null && 
            (defaultCountry.equals(Locale.US.getCountry()) || 
             defaultCountry.equals(Locale.CANADA.getCountry()))) { 
            return PageSetupPageType.LETTER;
        } else { 
            return PageSetupPageType.A4;
        } 
    }
}
