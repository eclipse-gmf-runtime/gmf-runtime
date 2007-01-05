/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * 
 * Drop target listener to support dropping of image files onto the diagram
 * surface.
 */
public class ImageFileDropTargetListener
	extends DiagramDropTargetListener {

	/**
	 * @param viewer
	 * @param xfer
	 */
	public ImageFileDropTargetListener(EditPartViewer viewer) {
		super(viewer, FileTransfer.getInstance());
	}

	/**
	 * This implementation includes in the list only elements that are instances
	 * of IElement.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener#getElementsBeingDropped()
	 */
	protected List getObjectsBeingDropped() {
		/*  Get the selection from the transfer agent */
		TransferData[] data = getCurrentEvent().dataTypes;
		List fileList = new ArrayList();
		
		for (int i=0; i < data.length; i++) {
			
			if (FileTransfer.getInstance().isSupportedType(data[i])) {
				// FileTransfers from the PE are supported, but an 
				// SWT exception is thrown when using nativeToJava call.
                System.out.println("here");
				try {
					Object files = FileTransfer.getInstance().nativeToJava(data[i]);
					if (files instanceof String[]) {
						String[] fileStrings = (String[])files;
						for	(int j=0; j<fileStrings.length; j++)
							fileList.add(fileStrings[j]);
					}				
				} catch (SWTException e) {
					return null;
				}

			}
		}
		
		if (fileList.size() > 0)
			return fileList;
		
		return null;
	}

	/**
	 * This implementation assumes that elements being dropped are instances of
	 * IElement.
	 * 
	 */
	public boolean isEnabled(DropTargetEvent event) {

		if (super.isEnabled(event)) {
			Object modelObj = getViewer().getContents().getModel();
			List dropObjects = getDropObjectsRequest().getObjects();
			
			if (modelObj instanceof EObject) {
				return dropObjects != null && !dropObjects.isEmpty();
				
			} else if (modelObj instanceof IAdaptable) {
				final EObject target = (EObject) ((IAdaptable) modelObj)
					.getAdapter(EObject.class);

				// additional check
				if (dropObjects == null || dropObjects.isEmpty()
						|| target == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}