/******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.ui.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditorWithFlyoutPalette;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;


public class LogicNotationEditor
	extends FileDiagramEditorWithFlyoutPalette {

	private static final String EDITING_DOMAIN_ID = "org.eclipse.gmf.examples.runtime.diagram.logicEditingDomain"; //$NON-NLS-1$
	private static final List SUPPORTED_DRAG_DROP_ECLASSES = new ArrayList();
	static {
		SUPPORTED_DRAG_DROP_ECLASSES.add(SemanticPackage.eINSTANCE.getLED());
		SUPPORTED_DRAG_DROP_ECLASSES.add(SemanticPackage.eINSTANCE.getAndGate());
		SUPPORTED_DRAG_DROP_ECLASSES.add(SemanticPackage.eINSTANCE.getOrGate());
		SUPPORTED_DRAG_DROP_ECLASSES.add(SemanticPackage.eINSTANCE.getXORGate());
		SUPPORTED_DRAG_DROP_ECLASSES.add(SemanticPackage.eINSTANCE.getCircuit());
	}
	
	protected String getEditingDomainID() {
		return EDITING_DOMAIN_ID;
	}
	
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		
		getDiagramGraphicalViewer().addDropTargetListener(
			(TransferDropTargetListener) new DiagramDropTargetListener(getDiagramGraphicalViewer(), LocalTransfer.getInstance()) {
			protected List getObjectsBeingDropped() {
				TransferData[] data = getCurrentEvent().dataTypes;
				List eObjects = new ArrayList();
				
				for (int i=0; i<data.length; i++) {
					if (LocalTransfer.getInstance().isSupportedType(data[i])) {
						IStructuredSelection selection = (IStructuredSelection)LocalTransfer.getInstance().nativeToJava(data[i]);
						eObjects.addAll(selection.toList());
					}
				}
				return eObjects;
			}
			
			public boolean isEnabled(DropTargetEvent event) {
				if (super.isEnabled(event)) {
					Object modelObj = getViewer().getContents().getModel();
					if (modelObj instanceof EObject) {
						List eObjects = getDropObjectsRequest().getObjects();
						
						if (eObjects == null)
							return false;
						
						for (Iterator i = eObjects.iterator(); i.hasNext();) {
							Object o = i.next();
							
							if (!(o instanceof EObject))
								return false;
							
							if (!SUPPORTED_DRAG_DROP_ECLASSES.contains(((EObject)o).eClass()))
								return false;
						}
						
						return true;
					}
				}

				return false;
			}
		});
	}
}
