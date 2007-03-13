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

package org.eclipse.gmf.runtime.diagram.ui.render.util;

import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.widgets.Shell;

/**
 * Provides miscellaneous utilities for this plug-in.
 * 
 * @author cmahoney
 */
public class DiagramRenderUtil {

	/**
	 * Utility api to generate an AWT based image representation of the diagram
	 * contents.
	 * 
	 * @param diagram
	 *            The Diagram to render the image from.
	 * @return java.awt.Image that is the bitmap representation of the diagram
	 *         contents.
	 * 
	 * @throws NullPointerException
	 *             <code>diagram</code> is <code>null</code>
	 */
	public static java.awt.Image renderToAWTImage(Diagram diagram) {
		if (null == diagram) {
			throw new NullPointerException("Argument 'diagram' is null"); //$NON-NLS-1$
		}

        Shell shell = new Shell();
        try {
            DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
                .createDiagramEditPart(diagram, shell);
            DiagramImageGenerator imageGenerator = new DiagramImageGenerator(
                diagramEP);

            return imageGenerator.createAWTImageForDiagram();
        } finally {
            shell.dispose();
        }
	}

	/**
	 * Utility api to generate an SWT based image representation of the diagram
	 * contents.
	 * 
	 * @param diagram
	 *            The Diagram to render the image from.
	 * @return org.eclipse.swt.graphics.Image that is the bitmap representation
	 *         of the diagram contents. Caller is responsible for calling
	 *         dispose() on the return image.
	 * 
	 * @throws NullPointerException
	 *             <code>diagram</code> is <code>null</code>
	 */
	public static org.eclipse.swt.graphics.Image renderToSWTImage(
			Diagram diagram) {
		if (null == diagram) {
			throw new NullPointerException("Argument 'diagram' is null"); //$NON-NLS-1$
		}

        Shell shell = new Shell();
        try {
    		DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
    			.createDiagramEditPart(diagram, shell);
    		DiagramImageGenerator imageGenerator = new DiagramImageGenerator(
    			diagramEP);
    
    		return imageGenerator.createSWTImageDescriptorForDiagram()
    			.createImage();
        } finally {
            shell.dispose();
        }
            
	}
}