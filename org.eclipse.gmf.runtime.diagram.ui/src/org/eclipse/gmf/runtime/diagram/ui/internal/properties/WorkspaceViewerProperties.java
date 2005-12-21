/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.properties;

/**
 * Contains the property identifiers for the workspace preferences.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public interface WorkspaceViewerProperties {

	/**
	 * Viewer property identifiers.  This values are used to access the
	 * workspace viewer preference store.  Do not localize these strings.
	 */
	public final String VIEWPORTX = "viewport.x";//$NON-NLS-1$
	public final String VIEWPORTY = "viewport.y";//$NON-NLS-1$
	
	public final String ZOOM = "zoom";//$NON-NLS-1$
	
	public final String VIEWPAGEBREAKS ="viewpagebreaks";//$NON-NLS-1$
	
	public final String PAGEBREAK_X ="pagebreak.x";//$NON-NLS-1$	
	public final String PAGEBREAK_Y ="pagebreak.y";//$NON-NLS-1$
	
	// Ruler and grid properties
	public final String VIEWGRID = "rulergrid.viewgrid"; //$NON-NLS-1$
	public final String GRIDLINESTYLE = "rulergrid.gridlinestyle"; //$NON-NLS-1$
	public final String GRIDLINECOLOR = "rulergrid.gridlinecolor"; //$NON-NLS-1$
	public final String SNAPTOGRID = "rulergrid.snaptogrid"; //$NON-NLS-1$
	public final String GRIDORDER = "rulergrid.gridlevel"; //$NON-NLS-1$
	public final String GRIDSPACING = "rulergrid.gridspacing"; //$NON-NLS-1$

	public final String RULERUNIT = "rulergrid.rulerunit"; //$NON-NLS-1$
	public final String VIEWRULERS = "rulergrid.viewrulers"; //$NON-NLS-1$
	
	// PRINTING PREFERENCES
	public static String PREF_USE_WORKSPACE_SETTINGS = "print.useWorkspaceSettings"; //$NON-NLS-1$
	public static String PREF_USE_DIAGRAM_SETTINGS = "print.useDiagramSettings"; //$NON-NLS-1$
	
	public static String PREF_USE_INCHES = "print.useInches"; //$NON-NLS-1$
	public static String PREF_USE_MILLIM = "print.useMillim"; //$NON-NLS-1$
	
	public static String PREF_USE_PORTRAIT = "print.usePortrait"; //$NON-NLS-1$
	public static String PREF_USE_LANDSCAPE = "print.useLandscape"; //$NON-NLS-1$
	
	public static String PREF_PAGE_SIZE = "print.page.size"; //$NON-NLS-1$
	public static String PREF_PAGE_HEIGHT = "print.page.height"; //$NON-NLS-1$
	public static String PREF_PAGE_WIDTH = "print.page.width"; //$NON-NLS-1$
	
	public static String PREF_MARGIN_TOP = "print.margin.top"; //$NON-NLS-1$
	public static String PREF_MARGIN_BOTTOM = "print.margin.bottom"; //$NON-NLS-1$
	public static String PREF_MARGIN_LEFT = "print.margin.left"; //$NON-NLS-1$
	public static String PREF_MARGIN_RIGHT = "print.margin.right"; //$NON-NLS-1$
	// END PRINTING PREFERENCES
	
	public static final String HEADER_PREFIX = "header"; //$NON-NLS-1$
	public static final String FOOTER_PREFIX = "footer"; //$NON-NLS-1$
	public static final String PRINT_TEXT_SUFFIX = "PrintText"; //$NON-NLS-1$
	public static final String PRINT_TITLE_SUFFIX = "PrintTitle"; //$NON-NLS-1$
	public static final String PRINT_DATE_SUFFIX = "PrintDate"; //$NON-NLS-1$
	public static final String PRINT_PAGE_SUFFIX = "PrintPage"; //$NON-NLS-1$
}

