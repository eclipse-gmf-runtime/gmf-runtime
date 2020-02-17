/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.preferences;

/**
 * Defines preference constants for the GMF diagram layer.
 * 
 * @noimplement This interface is not intended to be implemented by clients.  
 * @author schafe
 */
public interface IPreferenceConstants {

    /**
     * the connection line style; shows up in ConnectionsPreferencePage 
     */
    public static final String PREF_LINE_STYLE = "Connectors.lineStyle"; //$NON-NLS-1$

    /**
     * the font; shows up in AppearancePreferencePage 
     */
    public static final String PREF_DEFAULT_FONT = "Appearance.defaultFont"; //$NON-NLS-1$
    /**
     * the font color; shows up in AppearancePreferencePage 
     */
	public static final String PREF_FONT_COLOR = "Appearance.fontColor"; //$NON-NLS-1$
	/**
     * the the fill color; shows up in AppearancePreferencePage 
     */
    public static final String PREF_FILL_COLOR = "Appearance.fillColor"; //$NON-NLS-1$
    /**
     * the line color; shows up in AppearancePreferencePage 
     */
    public static final String PREF_LINE_COLOR = "Appearance.lineColor"; //$NON-NLS-1$
    /**
     * the note fill color; shows up in AppearancePreferencePage 
     */
	public static final String PREF_NOTE_FILL_COLOR = "Appearance.noteFillColor"; //$NON-NLS-1$
	/**
     * the note line color; shows up in AppearancePreferencePage 
     */
	public static final String PREF_NOTE_LINE_COLOR = "Appearance.noteLineColor"; //$NON-NLS-1$
    
    /**
     * show connection handles preference; Global Preference 
     */
	public static final String PREF_SHOW_CONNECTION_HANDLES = "Global.showConnectionHandles"; //$NON-NLS-1$
	
	/**
     * show popup bars preference;; Global Preference 
     */
	public static final String PREF_SHOW_POPUP_BARS = "Global.showPopupBars"; //$NON-NLS-1$

	/**
     * prompt when user choose delete from model; Global Preference 
     */
	public static final String PREF_PROMPT_ON_DEL_FROM_MODEL = "Global.promptOnDelFromModel"; //$NON-NLS-1$
	/**
     * prompt when user choose delete from diagram ; Global Preference
     */
	public static final String PREF_PROMPT_ON_DEL_FROM_DIAGRAM = "Global.promptOnDelFromDiagram"; //$NON-NLS-1$	
	/**
     * enable Layout animation ; Global Preference
     */
	public static final String PREF_ENABLE_ANIMATED_LAYOUT = "Global.enableAnimatedLayout"; //$NON-NLS-1$
	/**
     * enable zoom animation; Global Preference 
     */
	public static final String PREF_ENABLE_ANIMATED_ZOOM = "Global.enableAnimatedZoom"; //$NON-NLS-1$	

	/**
     * enable anti-aliasing; Global Preference 
     */
	public static final String PREF_ENABLE_ANTIALIAS = "Global.enableAntiAlias"; //$NON-NLS-1$	

	/**
     * enable status line content; Global Preference 
	 * @since 1.2
     */
	public static final String PREF_SHOW_STATUS_LINE = "Global.showStatusLine"; //$NON-NLS-1$	
	
	/**
     * show grid preference; Grid/Rulers Preference Page 
     */
	public static final String PREF_SHOW_GRID    = "GridRuler.showGrid";    //$NON-NLS-1$
	/**
     * Show Rulers Preference; Grid/Rulers Preference Page 
     */
	public static final String PREF_SHOW_RULERS  = "GridRuler.showRulers";  //$NON-NLS-1$
	/**
     * Snap to grid preference; Grid/Rulers Preference Page 
     */
	public static final String PREF_SNAP_TO_GRID = "GridRuler.snapToGrid";  //$NON-NLS-1$
	/**
     * ruler units preference; Grid/Rulers Preference Page 
     */
	public static final String PREF_RULER_UNITS  = "GridRuler.rulerUnits";  //$NON-NLS-1$
	/**
     * grid spacing preference; Grid/Rulers Preference Page 
     */
	public static final String PREF_GRID_SPACING = "GridRuler.gridSpacing"; //$NON-NLS-1$
	/**
     * snap to geometry preference; Grid/Rulers Preference Page 
     */
	public static final String PREF_SNAP_TO_GEOMETRY = "GridRuler.snapToGeometry"; //$NON-NLS-1$
	

}

