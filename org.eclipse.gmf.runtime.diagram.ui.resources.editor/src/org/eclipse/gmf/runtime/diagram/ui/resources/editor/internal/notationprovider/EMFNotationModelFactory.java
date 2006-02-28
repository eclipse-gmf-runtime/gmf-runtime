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


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.notationprovider;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author qili
 * 
 */
public class EMFNotationModelFactory {
	
	/**
	 * load an existing diagram file.
	 * 
	 * @param file
	 * @return
	 * @throws EmfNotationException
	 */	
	static public Diagram load(final IFile file, TransactionalEditingDomain editingDomain) throws EmfNotationException {
		Resource notationModel = null;
		try {
			file.refreshLocal(IResource.DEPTH_ZERO, null); //RATLC00514368
			String fileName = file.getLocation().toOSString();
			
	        // File exists with contents..
	        notationModel = editingDomain.loadResource(fileName);//TODO which MEditingDomain to use?
		     
		} catch (Exception e) {
            Trace.catching(
                EditorPlugin.getInstance(),
                EditorDebugOptions.EXCEPTIONS_CATCHING,
                EMFNotationModelFactory.class,
                "load", //$NON-NLS-1$
                e);
            EmfNotationException t = new EmfNotationException(e);
            Trace.throwing(
                EditorPlugin.getInstance(),
                EditorDebugOptions.EXCEPTIONS_CATCHING,
                EMFNotationModelFactory.class,
                "load", //$NON-NLS-1$
                e);
            throw t;
		}

		EList contents = notationModel.getContents();
		if (!contents.isEmpty()) {
			Object element = contents.get(0);
			return (element instanceof Diagram) ? (Diagram) element
				: null;
		}
		return null;
	}
	
	static public void save(IFile file, Diagram diagram, boolean clone, IProgressMonitor progressMonitor) throws Exception {
        Resource notationModel = ((EObject)diagram).eResource();
        String fileName = file.getLocation().toOSString();
        
        if (clone) {
			// save as option..
//            URI uri = URI.createPlatformResourceURI(fFile.getFullPath().toString(), true);
			notationModel.setURI(URI.createURI((fileName)));
			notationModel.save(null);
		} else {
			notationModel.save(null);
		}

		if (progressMonitor != null)		
			progressMonitor.done();
	}
}

