/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INotableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TextEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestCommandCallback;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestFixture;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

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
			.createShapeUsingTool(DiagramNotationType.NOTE,
				new Point(10, 10));
		NoteEditPart note2EP = (NoteEditPart) getFixture()
			.createShapeUsingTool(DiagramNotationType.NOTE,
				new Point(100, 100));

		// Create note attachment between the two notes.
		NoteAttachmentEditPart noteAttachment1EP = (NoteAttachmentEditPart) getFixture()
			.createConnectorUsingTool(note1EP, note2EP,
				DiagramNotationType.NOTE_ATTACHMENT);

		// Create a note attachment between a note and a connector.
		getFixture().createConnectorUsingTool(note1EP, noteAttachment1EP,
			DiagramNotationType.NOTE_ATTACHMENT);

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
			DiagramNotationType.TEXT, new Point(10, 10));

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
	public void testConnectionHandleForNoteAttachment()
		throws Exception {

		NoteEditPart noteEP = (NoteEditPart) getFixture().createShapeUsingTool(
			DiagramNotationType.NOTE, new Point(10, 10));

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
			DiagramNotationType.NOTE_ATTACHMENT));
		assertTrue(service.getRelTypesOnSource(attachableNoteableEP).contains(
			DiagramNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnSource(nonAttachableNoteableEP)
			.contains(DiagramNotationType.NOTE_ATTACHMENT));

		assertTrue(service.getRelTypesOnTarget(noteEP).contains(
			DiagramNotationType.NOTE_ATTACHMENT));
		assertTrue(service.getRelTypesOnTarget(attachableNoteableEP).contains(
			DiagramNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnTarget(nonAttachableNoteableEP)
			.contains(DiagramNotationType.NOTE_ATTACHMENT));

		assertTrue(service.getRelTypesOnSourceAndTarget(noteEP,
			attachableNoteableEP).contains(
			DiagramNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnSourceAndTarget(noteEP,
			nonAttachableNoteableEP).contains(
			DiagramNotationType.NOTE_ATTACHMENT));

		assertTrue(service.getRelTypesOnSourceAndTarget(attachableNoteableEP,
			noteEP).contains(DiagramNotationType.NOTE_ATTACHMENT));
		assertFalse(service.getRelTypesOnSourceAndTarget(
			nonAttachableNoteableEP, noteEP).contains(
			DiagramNotationType.NOTE_ATTACHMENT));
	}
}