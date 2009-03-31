/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.preference.IPreferenceStore;
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
     * @param diagram
     *            the <code>Diagram</code>
     * @return the new populated <code>DiagramEditPart</code>
     * @deprecated Please use {@link #createDiagramEditPart(Diagram, Shell)}
     *             instead as this method does not dispose the new Shell that it
     *             creates.
     */
	public DiagramEditPart createDiagramEditPart(
		Diagram diagram) {	
        
        return createDiagramEditPart(diagram, new Shell(), null);
	}
    
    /**
     * Creates a <code>DiagramEditPart</code> given the <code>Diagram</code>
     * without opening an editor.
     * 
     * @param diagram the <code>Diagram</code>
     * @param shell
     *            the shell
     * @return the new populated <code>DiagramEditPart</code>
     */
    public DiagramEditPart createDiagramEditPart(
        Diagram diagram, Shell shell) {  
        
        return createDiagramEditPart(diagram, shell, null);
    }
    
    /**
     * Creates a <code>DiagramEditPart</code> given the <code>Diagram</code>
     * without opening an editor.
     * 
     * @param diagram
     *            the <code>Diagram</code>
     * @param shell
     *            the shell
     * @param preferencesHint
     *            the preferences hint to be used when creating the diagram; if
     *            null, the preferences hint from the root editpart will be
     *            used.
     * @return the new populated <code>DiagramEditPart</code>
     */
     public DiagramEditPart createDiagramEditPart(
        Diagram diagram, Shell shell, PreferencesHint preferencesHint) {     
        
        DiagramGraphicalViewer customViewer = new DiagramGraphicalViewer();
        customViewer.createControl(shell);

        DiagramEditDomain editDomain = new DiagramEditDomain(null);
        editDomain.setCommandStack(
            new DiagramCommandStack(editDomain));

        customViewer.setEditDomain(editDomain);

        // hook in preferences
        RootEditPart rootEP = EditPartService.getInstance().createRootEditPart(
            diagram);
        if (rootEP instanceof IDiagramPreferenceSupport) {
            if (preferencesHint == null) {
                preferencesHint = ((IDiagramPreferenceSupport) rootEP)
                    .getPreferencesHint();
            } else {
                ((IDiagramPreferenceSupport) rootEP)
                    .setPreferencesHint(preferencesHint);
            }
            customViewer
                .hookWorkspacePreferenceStore((IPreferenceStore) preferencesHint
                    .getPreferenceStore());
        }
        
        customViewer.setRootEditPart(rootEP);

        customViewer.setEditPartFactory(EditPartService.getInstance());

        DiagramEventBroker.startListening(TransactionUtil.getEditingDomain(diagram));
        
        customViewer.setContents(diagram);
        customViewer.flush();
        
        Assert.isTrue(customViewer.getContents() instanceof DiagramEditPart);
        
    	/*
    	 * We need to flush all the deferred updates. 
    	 */
   		while (!shell.getDisplay().readAndDispatch());
        
        return (DiagramEditPart) customViewer.getContents();

    }

}
