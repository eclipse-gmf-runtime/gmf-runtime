/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.palette;


import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * The default palette content. 
 * An instance of this class is passed to 
 * <code>org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteService</code>
 * when creating a palette for an Editor
 * <p>
 * This palette content is based on the kind of project for which this
 * content is created. It allows the Palette items to check on Nature
 * of project containing the diagram and provide an enablement criteria. 
 * 
 * @author mgoyal
 * 
 */
public class DiagramPaletteContent {
    /**
     * Attribute to hold the diagram View that is currently
     * open in the editor
     */
    private IDiagramDocument fDiagramDocument;
    
    /**
     * Constructor to create palette content based on project
     * containing the diagram file.
     * 
     * @param project Project containing the diagram file.
     * @param diagramView The diagram view opened in the editor.
     */
    public DiagramPaletteContent(IDiagramDocument diagramDocument) {
        this.fDiagramDocument = diagramDocument;
    }
    
    /**
     * Accessor method for the diagram view field.
     * @return the diagram field.
     */
    public Diagram getDiagramView() {
    	return getDiagram();
    }

    /**
     * Accessor method for the diagram.
     * @return the diagram for the content.
     */
    public Diagram getDiagram() {
    	return fDiagramDocument != null ? fDiagramDocument.getDiagram() : null;
    }
    
    public IDiagramDocument getDiagramDocument() {
    	return fDiagramDocument;
    }
}

