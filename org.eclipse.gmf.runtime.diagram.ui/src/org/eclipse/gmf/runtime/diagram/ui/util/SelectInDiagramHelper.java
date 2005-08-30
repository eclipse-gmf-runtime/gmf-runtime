/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.View;

/**
 * Helper for selecting an element on a diagram.  Works for closed diagrams
 * when you open the diagram first.  You can use View's getDiagram()
 * to determine the diagram to open.  getDiagram() returns itself if the
 * View is a Diagram.
 *
 * @author wdiu, Wayne Diu
 */
public class SelectInDiagramHelper {

	/**
	 * Do not instantiate this helper class
	 */
	private SelectInDiagramHelper() {
		//do not instantiate
	}

	/**
	 * Activate the diagram if it's already open.
	 * If not, return null.
	 * 
	 * @param diagram Diagram to activate
	 * @return DiagramEditor of the activated diagram.  null if it was not open
	 * and could not be activated.
	 */
	public static DiagramEditor activateDiagram(Diagram diagram) {

		List editors = EditorService.getInstance().getRegisteredEditorParts();
		Iterator it = editors.iterator();
		while (it.hasNext()) {
			Object obj = it.next();

			if (obj instanceof DiagramEditor) {
				DiagramEditor diagramEditor = ((DiagramEditor) obj);
				if (diagramEditor.getDiagram().equals(diagram)) {
					//it's already open, just activate
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().activate(diagramEditor);
					return diagramEditor;
				}
			}
		}

		return null;
	}

	/**
	 * Select the View element from the DiagramEditor.
	 * 
	 * The DiagramEditor for the view must be the active editor for this
	 * workbench, otherwise it does not make sense to call this method.
	 * 
	 * Call activateDiagram to set the active DiagramEditor or open it
	 * manually.
	 * 
	 * @param view View to select
	 */
	public static void selectElement(View view) {
		IEditorPart editorPart = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		//DiagramEditor should be activated
		Assert.isTrue(editorPart instanceof DiagramEditor);

		DiagramEditor diagramEditor = (DiagramEditor) editorPart;

		//activated DiagramEditor must be the one that corresponds to
		//this view's diagram
		Assert.isTrue(diagramEditor.getDiagram().equals(view.getDiagram()));

		//diagramEditor instanceof IDiagramWorkbenchPart
		IDiagramGraphicalViewer viewer = diagramEditor
			.getDiagramGraphicalViewer();
		Assert.isNotNull(viewer);

		//find the edit part
		Object obj = viewer.getEditPartRegistry().get(view);

		if (obj instanceof EditPart) {
			viewer.select((EditPart) obj);
		} else {
			//could not activate the edit part from the registry
			Assert.isTrue(false);
		}

	}

	/**
	 * This api will expose the diagram at the location given in absolute co-ordinates.
	 * @param canvas
	 * @param location
	 */
	public static void exposeLocation(FigureCanvas canvas,Point location){
		location = location.getCopy();
		int padding = 50;
		if (location.x >= 0){
			location.x += padding;  
		}else{
			location.x -= padding;
		}
		
		if (location.y >= 0){
			location.y += padding;  
		}else{
			location.y -= padding;
		}
		
		int viewPortXLocation = canvas.getBounds().x;//getViewport().getHorizontalRangeModel().getValue();
		int viewPortXExtent = canvas.getBounds().x+canvas.getBounds().width;//getViewport().getHorizontalRangeModel().getExtent();
		
		int viewPortYLocation = canvas.getBounds().y;//getViewport().getVerticalRangeModel().getValue();
		int viewPortYExtent = canvas.getBounds().y+canvas.getBounds().height;//Viewport().getVerticalRangeModel().getExtent();
		
		int deltaX = 0;
		int deltaY = 0;
		
		if (location.x < viewPortXLocation){
			deltaX = location.x - viewPortXLocation;
		}else if (location.x > viewPortXExtent){
			deltaX = location.x - viewPortXExtent;
		}
		
		if (location.y < viewPortYLocation){
			deltaY = location.y - viewPortYLocation;
		}else if (location.y > viewPortYExtent){
			deltaY = location.y - viewPortYExtent;
		}
		
		RangeModel hRange = canvas.getViewport().getHorizontalRangeModel();
		RangeModel vRange = canvas.getViewport().getVerticalRangeModel();
		if ((deltaX != 0)|| (deltaY != 0)){
			canvas.getViewport().setIgnoreScroll(true);
			int x = hRange.getValue() + deltaX;
			int y = vRange.getValue() + deltaY;
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			canvas.scrollSmoothTo(x,y);
			canvas.getViewport().setIgnoreScroll(false);
		}
	}
}