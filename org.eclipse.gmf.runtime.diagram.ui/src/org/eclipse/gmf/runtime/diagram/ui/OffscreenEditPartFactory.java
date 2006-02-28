/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui;

import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Shell;


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
		DiagramGraphicalViewer customViewer = new DiagramGraphicalViewer();
		customViewer.createControl(shell);

		DiagramEditDomain editDomain = new DiagramEditDomain(null);
		editDomain.setCommandStack(
			new DiagramCommandStack(editDomain));

		customViewer.setEditDomain(editDomain);

		// hook in preferences
		RootEditPart rep = EditPartService.getInstance()
		.createRootEditPart(diagram);
		if (rep instanceof DiagramRootEditPart) {
			DiagramRootEditPart drep = (DiagramRootEditPart)rep;
			IPreferenceStore fPreferences = (IPreferenceStore) drep.getPreferencesHint().getPreferenceStore();

			customViewer.hookWorkspacePreferenceStore(fPreferences);
		}
		
		customViewer.setRootEditPart(rep);

		customViewer.setEditPartFactory(EditPartService.getInstance());

		DiagramEventBroker.startListening(TransactionUtil.getEditingDomain(diagram));
		
		customViewer.setContents(diagram);
		customViewer.flush();

		
		Assert.isTrue(customViewer.getContents() instanceof DiagramEditPart);
		
		return (DiagramEditPart) customViewer.getContents();
	}
}
