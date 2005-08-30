/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.       		       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.tests.runtime.diagram.ui;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DefaultNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.NoteFigure;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.NotationFactory;
import com.ibm.xtools.notation.View;


/**
 * @author mmostafa
 */
public class DiagramLinkTest
	extends TestCase {
	
	/**
	 * 
	 * @param TestName name for the test
	 */
	public DiagramLinkTest(String arg0) {
		super(arg0);
	}

	
	/**
	 * Sets up the fixture.  The default setup includes:
	 * - creating the project
	 * - creating a diagram
	 * - opening the diagram
	 * - adding shapes
	 * - adding connectors
	 * 
	 * This method is called before each test method is executed.
	 */
	protected void setup() throws Exception {
		super.setUp();
	}
	
	public static Test suite() {
		return new TestSuite(DiagramLinkTest.class);
	}
	
	public void testBrokenDiagramLink(){
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource("uml2.diagrams"); //$NON-NLS-1$
		Diagram diagram1 = NotationFactory.eINSTANCE.createDiagram();
		diagram1.setType("Class"); //$NON-NLS-1$
		diagram1.setName("Diagram1"); //$NON-NLS-1$
		diagram1.setVisible(true);
		
		// temp diagram 
		Diagram diagram2 = NotationFactory.eINSTANCE.createDiagram();
		diagram2.setType("Class"); //$NON-NLS-1$
		diagram2.setName("Diagram2"); //$NON-NLS-1$
		diagram1.setVisible(true);
		
		// add the diagrams to the model (so they will have guids)
		annotation.getContents().add(diagram1);
		annotation.getContents().add(diagram2);
		
		// simulate creating a valid Diagram Link	using the new format
		View link1 = ViewService.getInstance().createNodeView(new EObjectAdapter(diagram1),diagram1,Properties.NOTE,0,true, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
		assertValidDiagramLinkView(link1);
		
		
		//simulate loading  broken link using the new format	
		View link2 = ViewService.getInstance().createNodeView(new EObjectAdapter(diagram2),diagram1,Properties.NOTE,0,true, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
		assertValidDiagramLinkView(link2);
		link2.setElement(null);
		View link2NotationView = (View)EcoreUtil.copy(link2);
		diagram1.insertChild(link2NotationView);
		assertValidDiagramLinkView(link2NotationView);
		
		//simulate loading  valid link using the old format	
		View diagramLink = NotationFactory.eINSTANCE.createNode();
		diagramLink.setElement(diagram1);
		diagramLink.setVisible(true);
		diagram1.insertChild(diagramLink);
		assertValidDiagramLinkView(diagramLink);
		
		//simulate loading broken link using the old format	
		diagramLink = NotationFactory.eINSTANCE.createNode();
		diagramLink.setElement(null);
		diagramLink.setVisible(true);
		diagram1.insertChild(diagramLink);
		assertCorruptView(diagramLink);
		
		// last check will be making sure that normal Notes still working fine
		diagramLink = NotationFactory.eINSTANCE.createNode();
		diagramLink.setElement(null);
		diagramLink.setType(Properties.NOTE);
		diagramLink.setVisible(true);
		diagram1.insertChild(diagramLink);
		assertValidNoteView(diagramLink);
		
	}


	/**
	 * @param link1
	 * @return
	 */
	private IGraphicalEditPart assertValidDiagramLinkView(View link1) {
		assertNotNull(link1);
		// try to get the edit part
		IGraphicalEditPart gEditPart1 = EditPartService.getInstance().createGraphicEditPart(link1);
		assertNotNull(gEditPart1);
		assertFalse(gEditPart1 instanceof DefaultNodeEditPart);
		IFigure figure = gEditPart1.getFigure();
		assertTrue(figure instanceof NoteFigure);
		NoteFigure noteFigure = (NoteFigure)figure;
		assertTrue(noteFigure.isDiagramLinkMode());
		return gEditPart1;
	}
	
	/**
	 * @param link1
	 * @return
	 */
	private IGraphicalEditPart assertValidNoteView(View link1) {
		assertNotNull(link1);
		// try to get the edit part
		IGraphicalEditPart gEditPart1 = EditPartService.getInstance().createGraphicEditPart(link1);
		assertNotNull(gEditPart1);
		assertFalse(gEditPart1 instanceof DefaultNodeEditPart);
		IFigure figure = gEditPart1.getFigure();
		assertTrue(figure instanceof NoteFigure);
		NoteFigure noteFigure = (NoteFigure)figure;
		assertFalse(noteFigure.isDiagramLinkMode());
		return gEditPart1;
	}
	
	/**
	 * @param link1
	 * @return
	 */
	private IGraphicalEditPart assertCorruptView(View link1) {
		if (link1==null)
			return null;
		assertNotNull(link1);
		// try to get the edit part
		IGraphicalEditPart gEditPart1 = EditPartService.getInstance().createGraphicEditPart(link1);
		assertNotNull(gEditPart1);
		assertFalse(gEditPart1 instanceof DefaultNodeEditPart);
		return null;
	}

}
