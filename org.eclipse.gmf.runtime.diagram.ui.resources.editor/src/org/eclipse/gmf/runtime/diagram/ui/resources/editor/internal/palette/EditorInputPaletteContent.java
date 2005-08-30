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


import org.eclipse.ui.IEditorInput;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;


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
public class EditorInputPaletteContent extends DiagramPaletteContent {
    /**
     * Attribute to hold the project information 
     * in which the diagram file is located.
     */
    private IEditorInput input;
    
    /**
     * Constructor to create palette content based on project
     * containing the diagram file.
     * 
     * @param project Project containing the diagram file.
     * @param diagramView The diagram view opened in the editor.
     */
    public EditorInputPaletteContent(IEditorInput pInput, IDiagramDocument diagramDocument) {
    	super(diagramDocument);
        this.input = pInput;
    }
    
    public IEditorInput getEditorInput() {
    	return input;
    }
}

