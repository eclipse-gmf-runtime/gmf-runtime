/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Mar 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramStatusCodes;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;


/**
 * @author sshaw
 *
 * Utility class for creating simple diagrams for testing
 */
public class DiagramCreator {

	static public Diagram createEmptyDiagram(final PreferencesHint preferencesHint, TransactionalEditingDomain editingDomain) {
		final Diagram[] dgmContainer = new Diagram[1];
		AbstractEMFOperation operation = new AbstractEMFOperation(
			editingDomain, "") { //$NON-NLS-1$

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				
				Diagram diagram = ViewService.createDiagram("logic", preferencesHint); //$NON-NLS-1$
				Assert.isNotNull(diagram);
				dgmContainer[0] = diagram;

				return Status.OK_STATUS;
			};
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(operation,
				new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return dgmContainer[0];
	}

	static public List createNodes(final Diagram diagram,
			final PreferencesHint preferencesHint,
			TransactionalEditingDomain editingDomain) {

		final List list = new ArrayList(2);
		
        AbstractEMFOperation operation = new AbstractEMFOperation(
			editingDomain, StringStatics.BLANK) {

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {

				Node note1 = ViewService.createNode(diagram,
					ViewType.NOTE, preferencesHint);
				Assert.isNotNull(note1, "Note1 creation failed"); //$NON-NLS-1$
				Assert.isTrue(diagram.getChildren().get(0) == note1,
					"Note1 is not inserted in diagram"); //$NON-NLS-1$

				Node note2 = ViewService.createNode(diagram,
					ViewType.NOTE, preferencesHint);
				Assert.isNotNull(note2, "Note2 creation failed"); //$NON-NLS-1$
				Assert.isTrue(diagram.getChildren().get(1) == note2,
					"Note2 is not inserted in diagram"); //$NON-NLS-1$

				Edge noteAttachment = ViewService.createEdge(
					note1, note2, ViewType.NOTEATTACHMENT, preferencesHint);
				Assert.isNotNull(noteAttachment,
					"NoteAttachment creation failed"); //$NON-NLS-1$
				Assert.isTrue(diagram.getEdges().get(0) == noteAttachment,
					"NoteAttachment is not inserted in diagram"); //$NON-NLS-1$
				Assert.isTrue(note1.getSourceEdges().get(0) == noteAttachment,
					"NoteAttachment is not inserted in note1"); //$NON-NLS-1$
				Assert.isTrue(note2.getTargetEdges().get(0) == noteAttachment, 
					"NoteAttachment is not inserted in note2"); //$NON-NLS-1$
				Assert.isTrue(noteAttachment.getSource() == note1,
					"Note1 is not source for noteattachment"); //$NON-NLS-1$
				Assert.isTrue(noteAttachment.getTarget() == note2, 
					"Note2 is not target for noteattachment"); //$NON-NLS-1$

				list.add(note1);
				list.add(note2);
				
				return new Status(IStatus.OK, LogicDiagramPlugin.getPluginId(),
					DiagramStatusCodes.OK, StringStatics.BLANK, null);
			};
		};

		try {
			operation.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}


		return list;
	}
}
