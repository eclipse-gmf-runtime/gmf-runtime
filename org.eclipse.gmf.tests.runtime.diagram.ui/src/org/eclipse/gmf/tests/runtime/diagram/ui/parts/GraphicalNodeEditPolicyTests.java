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

package org.eclipse.gmf.tests.runtime.diagram.ui.parts;

import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestFixture;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

/**
 * GraphicalNodeEditPolicy class tests.
 * 
 * @author cmahoney
 */
public class GraphicalNodeEditPolicyTests
	extends AbstractTestBase {

	/**
	 * Extend the <code>GraphicalNodeEditPolicy</code> class to support a menu
	 * popup that not only creates a note attachment, but also allows the user
	 * to choose what color to make the note attachment.
	 * 
	 * @author cmahoney
	 */
	static class NoteGraphicalNodeEditPolicy
		extends GraphicalNodeEditPolicy {

		/**
		 * Provides a description of the type of note attachment to be created.
		 */
		static class NoteAttachmentDescriptor {

			Color color;

			NoteAttachmentDescriptor(Color color) {
				super();
				this.color = color;
			}
		}

		static NoteAttachmentDescriptor USER_CHOICE = new NoteAttachmentDescriptor(
			ColorConstants.red);

		protected List getConnectionMenuContent(CreateConnectionRequest request) {
			List content = super.getConnectionMenuContent(request);
			if (content.contains(DiagramNotationType.NOTE_ATTACHMENT)) {
				content.add(new NoteAttachmentDescriptor(ColorConstants.blue));
				content.add(USER_CHOICE);
				content
					.add(new NoteAttachmentDescriptor(ColorConstants.yellow));
			}
			return content;
		}

		protected Command getConnectionCompleteCommand(Object connectionType,
				CreateConnectionRequest request) {
			if (connectionType instanceof NoteAttachmentDescriptor) {
				CompoundCommand cc = new CompoundCommand(
					"Create Note Attachment"); //$NON-NLS-1$
				cc.add(super.getConnectionCompleteCommand(
					DiagramNotationType.NOTE_ATTACHMENT, request));

				if (request instanceof CreateUnspecifiedTypeConnectionRequest)
					cc
						.add(new EtoolsProxyCommand(
							new SetPropertyCommand(
                                ((IGraphicalEditPart) getHost()).getEditingDomain(),
								((CreateConnectionViewRequest) ((CreateUnspecifiedTypeConnectionRequest) request)
									.getRequestForType(DiagramNotationType.NOTE_ATTACHMENT))
									.getConnectionViewDescriptor(),
								Properties.ID_LINECOLOR,
								Properties.ID_LINECOLOR,
								FigureUtilities
									.colorToInteger(((NoteAttachmentDescriptor) connectionType).color))));
				return cc.unwrap();
			}
			return super.getConnectionCompleteCommand(connectionType, request);
		}

		protected ICommand getPromptAndCreateConnectionCommand(List content,
				CreateConnectionRequest request) {

			class TestablePromptAndCreateConnectionCommand
				extends PromptAndCreateConnectionCommand {

				class TestablePopupMenu
					extends PopupMenu {

					TestablePopupMenu(List theContent,
							ILabelProvider theLabelProvider) {
						super(theContent, theLabelProvider);
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu#show(org.eclipse.swt.widgets.Control)
					 */
					public boolean show(Control parent) {
						assertTrue(getContent().contains(USER_CHOICE));
						setResult(Collections.singletonList(USER_CHOICE));
						return true;
					}

				}

				/**
				 * Creates a new instance.
				 * 
				 * @param theContent
				 * @param theRequest
				 */
				TestablePromptAndCreateConnectionCommand(List theContent,
						CreateConnectionRequest theRequest) {
					super(theContent, theRequest);
					setPopupMenu(new TestablePopupMenu(theContent,
						getLabelProvider()));

				}

				protected ILabelProvider getLabelProvider() {
					return new CreateOrSelectElementCommand.LabelProvider() {

						public String getText(Object object) {
							if (object instanceof NoteAttachmentDescriptor) {
								return "Create a note attachment colored " //$NON-NLS-1$
									+ ((NoteAttachmentDescriptor) object).color;
							}
							return super.getText(object);
						}
					};
				}

			}
			return new TestablePromptAndCreateConnectionCommand(content,
				request);
		}

	}

	/**
	 * Subclass the Connection handle tool to make it usable in the test
	 * environment.
	 * 
	 * @author cmahoney
	 */
	class ConnectionHandleTool
		extends
		org.eclipse.gmf.runtime.diagram.ui.internal.tools.ConnectionHandleTool {

		public ConnectionHandleTool(ConnectionHandle connectorHandle) {
			super(connectorHandle);
		}

		/** Make public. */
		public Request createTargetRequest() {
			return super.createTargetRequest();
		}

		protected PreferencesHint getPreferencesHint() {
			return PreferencesHint.USE_DEFAULTS;
		}
	}

	public static Test suite() {
		TestSuite s = new TestSuite(GraphicalNodeEditPolicyTests.class);
		return s;
	}

	public GraphicalNodeEditPolicyTests() {
		super("GraphicalNodeEditPolicy Test Suite");//$NON-NLS-1$
	}

	protected void setTestFixture() {
		testFixture = new PresentationTestFixture();
	}

	protected PresentationTestFixture getFixture() {
		return (PresentationTestFixture) testFixture;
	}

	/**
	 * Tests the ability to use a custom prompt and add non-element types to the
	 * prompt.
	 * 
	 * @throws Exception
	 */
	public void testCustomPrompt()
		throws Exception {

		NoteEditPart sourceEP = getFixture().createNote();
		NoteEditPart targetEP = getFixture().createNote();

		sourceEP.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new NoteGraphicalNodeEditPolicy());
		targetEP.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new NoteGraphicalNodeEditPolicy());

		ConnectionHandle handle = new ConnectionHandle(sourceEP,
			ConnectionHandle.HandleDirection.OUTGOING, "the tooltip"); //$NON-NLS-1$
		ConnectionHandleTool tool = new ConnectionHandleTool(handle);
		CreateConnectionRequest request = (CreateConnectionRequest) tool
			.createTargetRequest();
		request.setTargetEditPart(sourceEP);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEP.getCommand(request);
		request.setSourceEditPart(sourceEP);
		request.setTargetEditPart(targetEP);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		targetEP.getCommand(request).execute();

		assertEquals(1, getDiagramEditPart().getConnections().size());
		NoteAttachmentEditPart noteAttachmentEP = (NoteAttachmentEditPart) getDiagramEditPart()
			.getConnections().get(0);
		assertEquals(sourceEP, noteAttachmentEP.getSource());
		assertEquals(targetEP, noteAttachmentEP.getTarget());
		assertTrue(FigureUtilities.integerToColor(
			(Integer) ViewUtil.getStructuralFeatureValue(noteAttachmentEP
				.getNotationView(), NotationPackage.eINSTANCE
				.getLineStyle_LineColor())).equals(
			NoteGraphicalNodeEditPolicy.USER_CHOICE.color));

		noteAttachmentEP.getCommand(
			new GroupRequest(RequestConstants.REQ_DELETE)).execute();
		assertEquals(0, getDiagramEditPart().getConnections().size());

		handle = new ConnectionHandle(targetEP,
			ConnectionHandle.HandleDirection.INCOMING, "the tooltip"); //$NON-NLS-1$
		tool = new ConnectionHandleTool(handle);
		request = (CreateConnectionRequest) tool.createTargetRequest();
		request.setTargetEditPart(targetEP);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		targetEP.getCommand(request);
		request.setSourceEditPart(targetEP);
		request.setTargetEditPart(sourceEP);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		sourceEP.getCommand(request).execute();

		assertEquals(1, getDiagramEditPart().getConnections().size());
		noteAttachmentEP = (NoteAttachmentEditPart) getDiagramEditPart()
			.getConnections().get(0);
		assertEquals(sourceEP, noteAttachmentEP.getSource());
		assertEquals(targetEP, noteAttachmentEP.getTarget());
		assertTrue(FigureUtilities.integerToColor(
			(Integer) ViewUtil.getStructuralFeatureValue(noteAttachmentEP
				.getNotationView(), NotationPackage.eINSTANCE
				.getLineStyle_LineColor())).equals(
			NoteGraphicalNodeEditPolicy.USER_CHOICE.color));
	}

}
