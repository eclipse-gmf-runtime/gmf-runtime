/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.util;

import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import com.ibm.xtools.notation.Diagram;

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

		DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
			.createDiagramEditPart(diagram);
		DiagramImageGenerator imageGenerator = new DiagramImageGenerator(
			diagramEP);

		return imageGenerator.createAWTImageForDiagram();
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

		DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
			.createDiagramEditPart(diagram);
		DiagramImageGenerator imageGenerator = new DiagramImageGenerator(
			diagramEP);

		return imageGenerator.createSWTImageDescriptorForDiagram()
			.createImage();
	}
}