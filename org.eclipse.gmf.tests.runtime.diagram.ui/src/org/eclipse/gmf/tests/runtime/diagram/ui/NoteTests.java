/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INotableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TextEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.PresentationNotationType;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestCommandCallback;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestFixture;

/**
 * Tests functionality relating to notes, note attachments, and text shapes.
 * 
 * @author cmahoney
 */
public class NoteTests
	extends AbstractTestBase {

	public NoteTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(NoteTests.class);
	}

	protected void setTestFixture() {
		testFixture = new PresentationTestFixture();
	}

	protected PresentationTestFixture getFixture() {
		return (PresentationTestFixture) testFixture;
	}

	/**
	 * Tests notes and note attachments.
	 */
	public void testNotesAndNoteAttachments()
		throws Exception {

		final DiagramEditPart diagramEP = getFixture().getDiagramEditPart();
		assertTrue(diagramEP.getChildren().isEmpty());

		// Add two notes.
		NoteEditPart note1EP = (NoteEditPart) getFixture()
			.createShapeUsingTool(PresentationNotationType.NOTE,
				new Point(10, 10));
		NoteEditPart note2EP = (NoteEditPart) getFixture()
			.createShapeUsingTool(PresentationNotationType.NOTE,
				new Point(100, 100));

		// Create note attachment between the two notes.
		NoteAttachmentEditPart noteAttachment1EP = (NoteAttachmentEditPart) getFixture()
			.createConnectorUsingTool(note1EP, note2EP,
				PresentationNotationType.NOTE_ATTACHMENT);

		// Create a note attachment between a note and a connector.
		getFixture().createConnectorUsingTool(note1EP, noteAttachment1EP,
			PresentationNotationType.NOTE_ATTACHMENT);

		// Test delete views.
		Request request = new GroupRequest(RequestConstants.REQ_DELETE);
		noteAttachment1EP.getCommand(request).execute();
		note1EP.getCommand(request).execute();
		note2EP.getCommand(request).execute();

		assertTrue(diagramEP.getChildren().isEmpty());
	}

	/**
	 * Tests text shape.
	 */
	public void testTextShape()
		throws Exception {

		final DiagramEditPart diagramEP = getFixture().getDiagramEditPart();
		assertTrue(diagramEP.getChildren().isEmpty());

		// Add a text shape.
		TextEditPart textEP = (TextEditPart) getFixture().createShapeUsingTool(
			PresentationNotationType.TEXT, new Point(10, 10));

		testCommand(textEP.getCommand(new GroupRequest(
			RequestConstants.REQ_DELETE)), new ITestCommandCallback() {

			public void onCommandExecution() {
				assertTrue(diagramEP.getChildren().isEmpty());
			}
		});

	}
	
	/**
	 * Test that note attachment type will only show up on connector handles
	 * between notes and <code>INoteableEditParts</code> that support note
	 * attachments.
	 * 
	 * @throws Exception
	 */
	public void testConnectorHandleForNoteAttachment()
		throws Exception {

		NoteEditPart noteEP = (NoteEditPart) getFixture().createShapeUsingTool(
			PresentationNotationType.NOTE, new Point(10, 10));

		class NonAttachableNoteableEP
			extends ShapeNodeEditPart
			implements INotableEditPart {

			public NonAttachableNoteableEP() {
				super(null);
			}

			public boolean canAttachNote() {
				return false;
			}

			protected NodeFigure createNodeFigure() {
				return null;
			}
		}

		class AttachableNoteableEP
			extends ShapeNodeEditPart
			implements INotableEditPart {

			public AttachableNoteableEP() {
				super(null);
			}

			public boolean canAttachNote() {
				return true;
			}

			protected NodeFigure createNodeFigure() {
				return null;
			}
		}

		ShapeNodeEditPart attachableNoteableEP = new AttachableNoteableEP();
		ShapeNodeEditPart nonAttachableNoteableEP = new NonAttachableNoteableEP();

		ModelingAssistantService service = ModelingAssistantService
			.getInstance();

		assertTrue(service.getRelTypesOnSource(noteEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));
		assertTrue(service.getRelTypesOnSource(attachableNoteableEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnSource(nonAttachableNoteableEP)
			.contains(PresentationNotationType.NOTE_ATTACHMENT));

		assertTrue(service.getRelTypesOnTarget(noteEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));
		assertTrue(service.getRelTypesOnTarget(attachableNoteableEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnTarget(nonAttachableNoteableEP)
			.contains(PresentationNotationType.NOTE_ATTACHMENT));

		assertTrue(service.getRelTypesOnSourceAndTarget(noteEP,
			attachableNoteableEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnSourceAndTarget(noteEP,
			nonAttachableNoteableEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));

		assertTrue(service.getRelTypesOnSourceAndTarget(attachableNoteableEP,
			noteEP).contains(PresentationNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnSourceAndTarget(
			nonAttachableNoteableEP, noteEP).contains(
			PresentationNotationType.NOTE_ATTACHMENT));
	}
}