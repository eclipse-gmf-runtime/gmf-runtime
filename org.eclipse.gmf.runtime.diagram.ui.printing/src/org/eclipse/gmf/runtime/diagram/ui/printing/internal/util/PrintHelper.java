/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.internal.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.PageBreakEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.PageBreaksFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Helper to assist in making an offscreen diagram suitable for printing or
 * previewing.
 * 
 * @author wdiu, Wayne Diu
 */
public class PrintHelper {
	/**
	 * Creates a <code>DiagramEditPart</code> given the <code>Diagram</code>
	 * without opening an editor.
	 * 
	 * @param diagram the <code>Diagram</code>
	 * @param preferencesHint the preferences hint to use for intiializing the
	 * preferences of the root edit part
	 * @return the new populated <code>DiagramEditPart</code>
	 */
	public static DiagramEditPart createDiagramEditPart(Diagram diagram,
            PreferencesHint preferencesHint) {
        DiagramEditPart diagramEditPart =  OffscreenEditPartFactory.getInstance().createDiagramEditPart(
            diagram, new Shell(), preferencesHint);
        // since some of the diagram updates are ASync we need to give the 
        // inter-thread messages a chance to get processed processed before we
        // continue; check bugzilla 170332
        while (Display.getDefault().readAndDispatch ()){
            // nothing special to do 
        }
        return diagramEditPart;
    }
	
	/**
	 * Initialize the preferences for a diagram edit part, specifically
	 * including page breaks and margins.
	 * 
	 * Typically, the diagram edit part is created using
	 * createDiagramEditPart() and the diagram edit part is passed in as the
	 * first parameter of this method.
	 * 
	 * @param diagramEditPart the DiagramEditPart to pass in 
	 * @param preferencesHint the preferences hint to use for intiializing the preferences
	 * 
	 * @return true if the preferences could be loaded, false if they weren't
	 * loaded and defaults had to be used instead
	 */
	public static boolean initializePreferences(DiagramEditPart diagramEditPart, PreferencesHint preferencesHint) {
		assert diagramEditPart.getViewer() instanceof DiagramGraphicalViewer;
		
		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer)diagramEditPart.getViewer();

		boolean loadedPreferences = true;

		IPreferenceStore fPreferences = getPreferenceStoreForDiagram(diagramEditPart);
		
		if (fPreferences == null) {
			loadedPreferences = false;
			//leave at default x and y
			PreferenceStore defaults = new PreferenceStore();
			DiagramEditor.addDefaultPreferences(defaults, preferencesHint);

			fPreferences = getWorkspacePreferenceStore(preferencesHint);
		} else if (!fPreferences
			.getBoolean(WorkspaceViewerProperties.PREF_USE_DIAGRAM_SETTINGS)) {
			//if we aren't supposed to use the diagram settings, switch to the
			//workspace settings

			//we have to use the page break x and y settings from the diagram
			int x = fPreferences.getInt(WorkspaceViewerProperties.PAGEBREAK_X), y = fPreferences
				.getInt(WorkspaceViewerProperties.PAGEBREAK_Y);

			//minor performance optimization, use the existing
			//preferences from the workspace instead of making a new one
			fPreferences = getWorkspacePreferenceStore(preferencesHint);
			fPreferences.setValue(WorkspaceViewerProperties.PAGEBREAK_X, x);
			fPreferences.setValue(WorkspaceViewerProperties.PAGEBREAK_Y, y);
		}

		viewer.hookWorkspacePreferenceStore(fPreferences);

		diagramEditPart.refreshPageBreaks();
		
		return loadedPreferences;
	}

	/**
	 * Returns the workspace viewer <code>PreferenceStore</code> for a given diagram edit part.
	 * 
	 * @param diagramEP the DiagramEditPart to obtain the preference store for
	 * 
	 * @return the <code>PreferenceStore</code> for the given diagram edit part
	 * Could return null if it couldn't be loaded 
	 */
	private static IPreferenceStore getPreferenceStoreForDiagram(DiagramEditPart diagramEP) {
		// Try to load it
		String id = ViewUtil.getIdStr(diagramEP.getDiagramView());

		//try and get preferences from the open diagrams first
		//loadedPreferences will be set to true only if the preferences could
		// be
		//successfully loaded
		IPreferenceStore fPreferences = loadPreferencesFromOpenDiagram(id);
		if (fPreferences != null) {
			//loadPreferencesFromOpenDiagram will have set preferences
			return fPreferences;
		}

		IPath path = DiagramUIPlugin.getInstance().getStateLocation();

		String fileName = path.toString() + "/" + id;//$NON-NLS-1$
		java.io.File file = new File(fileName);
		fPreferences = new PreferenceStore(fileName);
		if (file.exists()) {
			// Load it
			try {
				((PreferenceStore) fPreferences).load();

				return fPreferences;
			} catch (Exception e) {
				return null;
			}
		}
		return null; //fPreferences couldn't be loaded
	}
	
	/**
	 * Load the preferences from an open diagram that has the given guid.
	 * 
	 * @param id guid of the open diagram to load the preferences for 
	 */
	private static IPreferenceStore loadPreferencesFromOpenDiagram(String id) {

		List diagramEditors = EditorService.getInstance()
			.getRegisteredEditorParts();
		Iterator it = diagramEditors.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof DiagramEditor) {
				DiagramEditor diagramEditor = (DiagramEditor) obj;

				//diagram edit part and view should not be null for an open
				// diagram
				if (id.equals(ViewUtil.getIdStr(diagramEditor
					.getDiagramEditPart().getDiagramView()))) {
					IDiagramGraphicalViewer viewer = diagramEditor
						.getDiagramGraphicalViewer();
					if (viewer instanceof DiagramGraphicalViewer) {
						DiagramGraphicalViewer diagramGraphicalViewer = (DiagramGraphicalViewer) viewer;

						//preferences loaded
						return diagramGraphicalViewer
							.getWorkspaceViewerPreferenceStore();
					}

					//id was equal, but we couldn't load it, so don't continue
					return null;
				}
			}
		}

		//no matching guid found
		return null;
	}

	/**
	 * Return the preference store for the given PreferenceHint
	 * @param preferencesHint to return the preference store for.
	 * 
	 * @return preference store for the given PreferenceHint
	 */
	private static IPreferenceStore getWorkspacePreferenceStore(PreferencesHint preferencesHint) {
		return (IPreferenceStore) preferencesHint.getPreferenceStore();
	}
	
	/**
	 * Returns the page break bounds on the diagram. If the bounds cannot be
	 * found the diagram bounds is returned.
	 * 
	 * @param dgrmEP the diagram edit part to return the page break bounds for
	 * @param loadedPreferences true if preferences were previously loaded,
	 * false if they couldn't be.  For consistency when printing, we have to
	 * treat page breaks differently depending on whether preferences were
	 * successfully loaded or not.
	 * 
	 * @return Rectangle with the page break bounds for the given diagram edit
	 * part. If the page break bounds cannot be found the diagram bounds
	 * Rectangle is returned.
	 */
	public static Rectangle getPageBreakBounds(DiagramEditPart dgrmEP, boolean loadedPreferences) {
		
		Rectangle pageBreakBounds = null;
		assert dgrmEP.getViewer() instanceof DiagramGraphicalViewer;
        
        
        //get the preferences in use...
        IPreferenceStore fPreferences = ((DiagramGraphicalViewer)dgrmEP.getViewer()).getWorkspaceViewerPreferenceStore();
        
        if (fPreferences.getBoolean(WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS)) {
            
            //get workspace settings...
            if (dgrmEP.getDiagramPreferencesHint().getPreferenceStore() != null)
                fPreferences = (IPreferenceStore)dgrmEP.getDiagramPreferencesHint().getPreferenceStore(); 
        }
        
		RootEditPart rootEditPart = dgrmEP.getRoot();
		if (rootEditPart instanceof DiagramRootEditPart) {
			DiagramRootEditPart diagramRootEditPart = (DiagramRootEditPart) rootEditPart;
			PageBreakEditPart pageBreakEditPart = diagramRootEditPart
				.getPageBreakEditPart();

			if (pageBreakEditPart != null) {
				//resize must be called. Otherwise you get the 64 x 32 default
				pageBreakEditPart.resize(dgrmEP.getChildrenBounds());

				if (loadedPreferences) {
					//if preferences were loaded, we'll always do this for
					//consistency when printing.
					//this is necessary when printing using workspace
					//preferences, which is "if (getWorkspacePreferenceStore()
					//== fPreferences)"

					//if preferences were not loaded, we do not set the figure
					//location. we'll just leave them at defaults.
					org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
						fPreferences
							.getInt(WorkspaceViewerProperties.PAGEBREAK_X),
						fPreferences
							.getInt(WorkspaceViewerProperties.PAGEBREAK_Y));
					pageBreakEditPart.set(p, PageInfoHelper.getChildrenBounds(
						(DiagramEditPart) diagramRootEditPart.getContents(),
						PageBreaksFigure.class));
				}

				pageBreakBounds = pageBreakEditPart.getFigure().getBounds();
			} else {
				pageBreakBounds = dgrmEP.getFigure().getBounds();
			}
		}

		return pageBreakBounds;
	}	
}