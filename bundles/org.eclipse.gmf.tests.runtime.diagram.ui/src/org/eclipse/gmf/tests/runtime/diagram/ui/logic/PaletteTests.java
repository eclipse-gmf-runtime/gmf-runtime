/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.PaletteToolTransferDragSourceListener;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.PaletteToolTransferDropTargetListener;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for the palette functionality.
 * 
 * @author cmahoney
 */
public class PaletteTests
	extends AbstractTestBase {

	public static Test suite() {
		TestSuite s = new TestSuite(PaletteTests.class);
		return s;
	}

	public PaletteTests() {
		super("Palette Tests");//$NON-NLS-1$
	}

	/** installs the composite state test fixture. */
	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}

	protected LogicTestFixture getFixture() {
		return (LogicTestFixture) testFixture;
	}

	/**
	 * Test the listeners that are used for drag and drop of palette tools on
	 * the diagram.
	 * 
	 * @throws Exception
	 */
	public void testDNDFromPalette()
		throws Exception {

		class MyPaletteToolTransferDragSourceListener
			extends PaletteToolTransferDragSourceListener {

			public MyPaletteToolTransferDragSourceListener(EditPartViewer viewer) {
				super(viewer);
			}

			/** Make public. */
			public Object getTemplate() {
				return super.getTemplate();
			}

		}

		class MyPaletteToolTransferDropTargetListener
			extends PaletteToolTransferDropTargetListener {

			public MyPaletteToolTransferDropTargetListener(EditPartViewer viewer) {
				super(viewer);
			}

			/** Make public. */
			public Request createTargetRequest() {
				return super.createTargetRequest();
			}

			protected Point getDropLocation() {
				return new Point(10, 10);
			}
		}

		getFixture().openDiagram();
		System.out.println(getDiagramEditPart().getPrimaryEditParts().size());
		assertEquals(2, getDiagramEditPart().getPrimaryEditParts().size());

		PaletteViewer paletteViewer = (PaletteViewer) getFixture()
			.getDiagramWorkbenchPart().getAdapter(PaletteViewer.class);

		MyPaletteToolTransferDragSourceListener dragListener = new MyPaletteToolTransferDragSourceListener(
			paletteViewer);
		MyPaletteToolTransferDropTargetListener dropListener = new MyPaletteToolTransferDropTargetListener(
			paletteViewer);

		PaletteEntry noteEntry = findChildPaletteEntryRecursive(paletteViewer
			.getPaletteRoot(), "noteTool"); //$NON-NLS-1$
		assertNotNull(noteEntry);

		paletteViewer.setActiveTool((ToolEntry) noteEntry);
		paletteViewer.select((EditPart) paletteViewer.getEditPartRegistry()
			.get(noteEntry));

		// Verify that drag is enabled.
		TemplateTransfer.getInstance().setTemplate(noteEntry);
		assertNotNull(dragListener.getTemplate());

		// Verify that the request works.
		CreateRequest request = (CreateRequest) dropListener
			.createTargetRequest();
		request.setLocation(new Point(10, 10));
		getDiagramEditPart().getCommand(request).execute();
		assertEquals(3, getDiagramEditPart().getPrimaryEditParts().size());
		Object noteEP = getDiagramEditPart().getPrimaryEditParts().get(2);
		assertTrue(noteEP instanceof NoteEditPart);

		// I cannot figure out how to test if the selection of the new shape
		// works since the call to
		// PaletteToolTransferDropTargetListener.handleDrop() expects a
		// DropTargetEvent and I cannot instantiate one.

		// Verify that drag is disabled for connection tools.
		PaletteEntry noteAttachmentEntry = findChildPaletteEntryRecursive(
			paletteViewer.getPaletteRoot(), "noteattachmentTool"); //$NON-NLS-1$
		paletteViewer.setActiveTool((ToolEntry) noteAttachmentEntry);
		paletteViewer.select((EditPart) paletteViewer.getEditPartRegistry()
			.get(noteAttachmentEntry));
		TemplateTransfer.getInstance().setTemplate(noteAttachmentEntry);
		assertNull(dragListener.getTemplate());
	}

	private static PaletteEntry findChildPaletteEntryRecursive(
			PaletteContainer container, String childId) {

		Iterator entries = container.getChildren().iterator();
		while (entries.hasNext()) {
			PaletteEntry entry = (PaletteEntry) entries.next();
			if (entry.getId().equals(childId)) {
				return entry;
			} else {
				if (entry instanceof PaletteContainer) {
					PaletteEntry entry2 = findChildPaletteEntryRecursive(
						(PaletteContainer) entry, childId);
					if (entry2 != null) {
						return entry2;
					}
				}
			}
		}
		return null;
	}

}
