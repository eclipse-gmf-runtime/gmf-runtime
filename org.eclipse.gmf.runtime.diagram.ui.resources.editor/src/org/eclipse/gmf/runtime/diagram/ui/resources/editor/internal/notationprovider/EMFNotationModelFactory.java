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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
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
	static public Diagram load(final IFile file) throws EmfNotationException {
		return load(file, MEditingDomain.INSTANCE);
	}
	
	static public Diagram load(final IFile file, MEditingDomain editingDomain) throws EmfNotationException {
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

		EObject element = ResourceUtil.getFirstRoot(notationModel);
		return (element instanceof Diagram) ? (Diagram)element : null;
	}
	
	static public void save(IFile file, Diagram diagram, boolean clone, IProgressMonitor progressMonitor) throws Exception {
        Resource notationModel = ((EObject)diagram).eResource();
        String fileName = file.getLocation().toOSString();
        
        if(clone) {
            //save as option..
            MEditingDomainGetter.getMEditingDomain(notationModel).saveResourceAs(notationModel, fileName);
        } else {
        	MEditingDomainGetter.getMEditingDomain(notationModel).saveResource(notationModel);
        }

		if (progressMonitor != null)		
			progressMonitor.done();
	}
}

