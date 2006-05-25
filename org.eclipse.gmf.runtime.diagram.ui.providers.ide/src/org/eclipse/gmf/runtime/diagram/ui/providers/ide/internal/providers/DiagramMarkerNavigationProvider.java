/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.providers.ide.internal.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.providers.ide.providers.AbstractDiagramMarkerNavigationProvider;

import com.ibm.icu.util.StringTokenizer;

/**
 * Provider for marker navigation in diagrams
 * 
 * @author Michael Yee, Kevin Cornell
 */
public class DiagramMarkerNavigationProvider
    extends AbstractDiagramMarkerNavigationProvider {

    public static final String A_ELEMENT_ID = "elementId"; //$NON-NLS-1$

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.marker.AbstractMarkerNavigationProvider#doGotoMarker(org.eclipse.core.resources.IMarker)
     */
    protected void doGotoMarker(IMarker marker) {
        if (getEditor() instanceof IDiagramWorkbenchPart) {
            List elements = new ArrayList();
            String elementIds =
                marker.getAttribute(A_ELEMENT_ID, StringStatics.BLANK);
            StringTokenizer ids = new StringTokenizer(elementIds);
            while (ids.hasMoreTokens()) {
                elements.add(ids.nextToken());
            }
            selectInDiagram(elements);
            return;
        }
    }

    /**
     * This method takes a list of notation element IDs, converts then into
     * views (IView) defined within the diagram and then selects the
     * corresponding edit parts. 
     * <p>
     * This method assumes the notation elements all reside in the same
     * diagram and that the editor for that diagram is already open 
     * (and set via setEditor()).
     * 
     * @param notationElementIds - list of notation element IDs in same diagram
     */
    private void selectInDiagram(List notationElementIds) {
        List editParts = findEditParts(convertIdsToViews(notationElementIds));
        GraphicalViewer viewer = getDiagramEditor().getDiagramGraphicalViewer();
        viewer.deselectAll();
        Iterator iter = editParts.iterator();
        while (iter.hasNext()) {
            EditPart editPart = (EditPart) iter.next();
            viewer.appendSelection(editPart);
            viewer.reveal(editPart);
        }
    }

}
