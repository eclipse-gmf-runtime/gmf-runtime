/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.providers.ide.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.marker.GotoMarkerOperation;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.ui.providers.marker.AbstractModelMarkerNavigationProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Abstract Diagram Marker Navigation Provider
 * <p>
 * This class provides utility methods for converting element ID strings
 * into their corresponding edit parts within the diagram.
 * <p>
 * @author Kevin Cornell
 */
public abstract class AbstractDiagramMarkerNavigationProvider
    extends AbstractModelMarkerNavigationProvider {

    /**
     * Get the associated diagram editor (implements IDiagramWorkbenchPart).
     *
     * @return the diagram editor
     */
    protected final IDiagramWorkbenchPart getDiagramEditor() {
        if (getEditor() instanceof IDiagramWorkbenchPart) {
            return (IDiagramWorkbenchPart)getEditor();
        }
        return null; // Should not happen because provides() tests the editor.
    }

    
    /**
     * Converts a list of element Id strings into a list of IViews.
     *
     * @param  elementIds - a list of element ID strings
     * @return a list of views corresponding to the ID strings
     */
    protected final List convertIdsToViews(List elementIds) {
    	List result = new ArrayList();
    	Iterator iter = elementIds.iterator();
    	Diagram diagramView = getDiagramEditor().getDiagram();
    	while (iter.hasNext()) {
    		Object elementId = iter.next();
    		if (elementId instanceof String && diagramView !=null) {    	
    			View view; 			    		
    			
    			// the Primary views could be nested 
    			// too deep so we need recursion to get to them    			
    			List viewList = new ArrayList();
    			viewList.add(diagramView );
    			view = recursiveConvertIdToView((String)elementId, viewList );      			    			
    			
    			if (view != null) {
    				result.add(view);
    			}       			
    		}    		
    	}
    	
    	return result;
    }
    
    /**
     * Converts an element Id string into the associated IView.
     * @param elementId - search the view with the given elemenID
     * @param viewList - List of views in which to search the view with the given elemenID
     */
    private View recursiveConvertIdToView(String elementId, List  viewList) {
    	
    	View view = null;
    	List tmpList = new ArrayList();
    	Iterator i = viewList.iterator();
    	while(i.hasNext()){
    		View tmpView = (View)i.next(); 
    		view = ViewUtil.getChildByIdStr(tmpView,elementId);				
    		if(view !=null) return view;	
    			view = getViewByIdStr(elementId,ViewUtil.getSourceConnections(tmpView)); 
    		if(view !=null) return view;
    			view = getViewByIdStr(elementId,ViewUtil.getTargetConnections(tmpView));
    		if(view !=null) return view;
    	    tmpList.addAll(tmpView.getChildren());
    		if(view !=null) return view;	
    	}		
    	
    	if(tmpList.size() == 0) return null;
    	
    	return recursiveConvertIdToView(elementId, tmpList);
    }
    
    /**
     * @param elementId - search the view with the given elemenID
     * @param edges - the list of notational edges to search in
     */
    private View getViewByIdStr(String elementId, List edges) {		
    	Iterator iter = edges.iterator();
    	while( iter.hasNext() ) {
    		Edge connection = (Edge)iter.next();
            String id = ((XMLResource)(connection.eResource())).getID(connection); //previously EObjectUtil.getID(connection)
    		if ( elementId.equals(id)) {
    			return connection;
    		}
    	}
    	return null;
    	
    }
    
    
    
    /** 
     * Convert a list of views into a list of associated edit parts.
     * 
     * @param elements - list of views (IView) 
     * @return - list of corresponding edit parts
     */
    protected final List findEditParts(List views) {
    	
        // Obtain the edit part registry for this diagram.
        Map editPartRegistry =
            getDiagramEditor()
                .getDiagramGraphicalViewer()
                .getEditPartRegistry();

        // Convert the list of views (IView) into a list of edit parts.
        List result = new ArrayList();
        Iterator iterator = views.listIterator();
        while (iterator.hasNext()) {
            EditPart part = (EditPart)editPartRegistry.get(iterator.next());
            if (part != null) {
                result.add(part);
            }
        }

        return result;
    }

	/** 
	 * Only accept marker navigation operations for diagram editors.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GotoMarkerOperation
			&& ((GotoMarkerOperation)operation).getEditor()
				instanceof IDiagramWorkbenchPart) {
			return true;
		}
		return false;
	}

}
