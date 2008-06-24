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

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;

/**
 * Drop target listener to support dropping of image files onto the diagram
 * surface.
 *
 * @author sshaw
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
        List<String> filesList = new ArrayList<String>();
        if (getCurrentEvent().data instanceof String[]) {
            insertFileNamesFromStringArray(filesList,
                    (String[]) getCurrentEvent().data);
        } else if (getCurrentEvent().data instanceof IStructuredSelection) {
			Object[] array = ((IStructuredSelection)getCurrentEvent().data).toArray();
			for (int j = 0; j < array.length; j++) {
				if (array[j] instanceof IFile) {
					IFile dropFile = (IFile)array[j];
					filesList.add(dropFile.getLocation().toOSString());
				}
			}
        	
        } else {
        	/*
        	 * No needs to check if transfered data we're looking at is FileTransfer type data.
        	 * This drop target listener is invoked as <code>DelegatingDropAdapter</code>, hence
        	 * if we get here the transfer is supported for the DropTargetEvent#currentDataType 
        	 */
            try {
                Object files = FileTransfer.getInstance().nativeToJava(
                        getCurrentEvent().currentDataType);
                if (files instanceof String[]) {
                    insertFileNamesFromStringArray(filesList, (String[]) files);
                }
            } catch (SWTException e) {
                return null;
            }
        }

        if (filesList.size() > 0) {
            return filesList;
        }

        return null;
    }
        
    private void insertFileNamesFromStringArray(List<String> filesList, String[] fileNames) {
        for (int i = 0; i < fileNames.length; i++) {
            filesList.add(fileNames[i]);
        }
    }
    
    /**
     * This implementation assumes that elements being dropped are instances of
     * IElement.
     *
     */
    public boolean isEnabled(DropTargetEvent event) {

        if (super.isEnabled(event)) {
            Object modelObj = getViewer().getContents().getModel();
            
            if (modelObj instanceof EObject) {
                return true;
            } else if (modelObj instanceof IAdaptable) {
                final EObject target = (EObject) ((IAdaptable) modelObj)
                    .getAdapter(EObject.class);

                // additional check
                if (target == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener#isDataTransfered()
	 */
	protected boolean isDataTransfered() {
		/*
		 * The data transfer occurs at the drop time on Linux, hence data is transfered when the request
		 * has some objects that are being dropped.
		 */
		return super.isDataTransfered() && !getDropObjectsRequest().getObjects().isEmpty();
	}

}