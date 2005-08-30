/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.palette;


import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * The default palette content. 
 * An instance of this class is passed to 
 * <code>org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteService</code>
 * when creating a palette for <code>com.ibm.xtools.uml.ui.diagram.internal.parts.UMLDiagramEditor</code>.
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

