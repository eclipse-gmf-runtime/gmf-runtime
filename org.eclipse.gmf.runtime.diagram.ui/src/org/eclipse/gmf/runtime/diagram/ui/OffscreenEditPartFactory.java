/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import com.ibm.xtools.notation.Diagram;


/**
 * @author sshaw
 *
 * Utility class to generate editpart containment offscreen without
 * creating an editor.
 */
public class OffscreenEditPartFactory {
	
	private static OffscreenEditPartFactory INSTANCE = new OffscreenEditPartFactory();
	
	/**
	 * gives access to the singleton instance  of <code>OffscreenEditPartFactory</code> 
	 * @return the singleton instance
	 */
	public static OffscreenEditPartFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Creates a <code>DiagramEditPart</code> given the <code>Diagram</code>
	 * without opening an editor.
	 * 
	 * @param diagram the <code>Diagram</code>
	 * @return the new populated <code>DiagramEditPart</code>
	 */
	public DiagramEditPart createDiagramEditPart(
		Diagram diagram) {		

		Shell shell = new Shell();
		GraphicalViewer customViewer = new DiagramGraphicalViewer();
		customViewer.createControl(shell);

		DiagramEditDomain editDomain = new DiagramEditDomain(null);
		editDomain.setCommandStack(
			new DiagramCommandStack(editDomain));

		customViewer.setEditDomain(editDomain);

		customViewer.setRootEditPart(EditPartService.getInstance()
			.createRootEditPart());

		customViewer.setEditPartFactory(EditPartService.getInstance());

		customViewer.setContents(diagram);
		customViewer.flush();

		Assert.isTrue(customViewer.getContents() instanceof DiagramEditPart);
		return (DiagramEditPart) customViewer.getContents();
	}
}
