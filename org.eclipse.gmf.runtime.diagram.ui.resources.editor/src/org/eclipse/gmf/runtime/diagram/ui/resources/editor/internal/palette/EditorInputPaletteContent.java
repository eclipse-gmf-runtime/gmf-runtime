/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.palette;


import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.ui.IEditorInput;


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

