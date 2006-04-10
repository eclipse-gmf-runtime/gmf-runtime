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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramDebugOptions;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramStatusCodes;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.ExampleDiagramLogicMessages;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Model;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticFactory;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * @author qili
 *
 * Diagram Utility Class for Logic Editor.
 */
public class LogicEditorUtil extends IDEEditorUtil {
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorUtil#createAndOpenDiagram(org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator, org.eclipse.core.runtime.IPath, java.lang.String, java.io.InputStream, java.lang.String, org.eclipse.ui.IWorkbenchWindow, org.eclipse.core.runtime.IProgressMonitor, boolean, boolean, org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint)
	 */
	public static final IFile createAndOpenDiagram(
			DiagramFileCreator diagramFileCreator, IPath containerPath,
			String fileName, InputStream initialContents, String kind,
			IWorkbenchWindow dWindow, IProgressMonitor progressMonitor,
			boolean openEditor, boolean saveDiagram, String semanticResourcePath) {

		IFile newFile = createNewDiagramFile(
            diagramFileCreator, containerPath, fileName, initialContents, kind,
            dWindow.getShell(), progressMonitor, semanticResourcePath);

		if (newFile != null && openEditor) {
			// Since the file resource was created fine, open it for editing
			// iff requested by the user
			IDEEditorUtil.openDiagram(newFile, dWindow, saveDiagram,
                progressMonitor);
		}

		return newFile;
	}
	
	/**
	 * Creates a new diagram file resource in the selected container and with
	 * the selected name. Creates any missing resource containers along the
	 * path; does nothing if the container resources already exist.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on on this page currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this page caches the new file once it has been successfully
	 * created; subsequent invocations of this method will answer the same file
	 * resource without attempting to create it again.
	 * </p>
	 * <p>
	 * This method should be called within a workspace modify operation since it
	 * creates resources.
	 * </p>
	 *
	 * @return the created file resource, or <code>null</code> if the file was
	 *         not created
	 */
	public static final IFile createNewDiagramFile(
			DiagramFileCreator diagramFileCreator, IPath containerFullPath,
			String fileName, InputStream initialContents, final String kind,
			Shell shell, final IProgressMonitor progressMonitor, final String semanticResourcePath) {
		
		/** cache of newly-created file */
		final IFile newDiagramFile = diagramFileCreator.createNewFile(
			containerFullPath, fileName, initialContents, shell,
			new IRunnableContext() {

				public void run(boolean fork, boolean cancelable,
						IRunnableWithProgress runnable)
					throws InvocationTargetException, InterruptedException {
					runnable.run(progressMonitor);
				}
			});
		
		TransactionalEditingDomain domain = GMFEditingDomainFactory.getInstance().createEditingDomain();
		final ResourceSet resourceSet =  domain.getResourceSet();
		
		AbstractEMFOperation op = new AbstractEMFOperation(domain,
				ExampleDiagramLogicMessages.LogicWizardPage_Title) {
			
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {
				
				IFile semanticFile = null;
				boolean semanticFileIsNew = false;
				if (semanticResourcePath != null && semanticResourcePath.length() > 0) {
					semanticFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(semanticResourcePath));
					if (!semanticFile.exists()) {
						semanticFileIsNew = true;
						try {
							semanticFile.create(new ByteArrayInputStream(new byte[0]), false, progressMonitor);
						} catch (CoreException e) {
							Log.error(LogicDiagramPlugin.getInstance(), IStatus.ERROR, e.getMessage(), e);
							return null;
						}
					}
				}
				
				// Fill the contents of the file dynamically
				Resource notationModel = null;
				Model semanticModel = null;

				try {
					newDiagramFile.refreshLocal(IResource.DEPTH_ZERO, null); //RATLC00514368
					if (semanticFile != null) {
						semanticFile.refreshLocal(IResource.DEPTH_ZERO, null);
					}
					
					InputStream stream = newDiagramFile.getContents();
					final String completeFileName = newDiagramFile.getLocation().toOSString();

					try {
						//Empty file....
		                notationModel = resourceSet.createResource(URI
		                    .createFileURI(completeFileName));
		                
		                if (semanticFileIsNew) {
		                	semanticModel = SemanticFactory.eINSTANCE.createModel();
		                	Resource semanticResource = resourceSet.createResource(URI.createPlatformResourceURI(semanticResourcePath,true));
		                	
							semanticResource.getContents().add(semanticModel);
		                } else if (semanticFile != null){
		                	semanticModel = (Model) resourceSet.getResource(URI.createPlatformResourceURI(semanticResourcePath,true),true).getContents().get(0);
		                }

					} finally {
						stream.close();
					}

				} catch (Exception e) {
					Trace.catching(LogicDiagramPlugin.getInstance(),
						LogicDiagramDebugOptions.EXCEPTIONS_CATCHING,
						LogicEditorUtil.class, "createNewDiagramFile", //$NON-NLS-1$
						e);
				}


				if (notationModel != null) {
					if (semanticModel == null) {
						semanticModel = SemanticFactory.eINSTANCE.createModel();
						notationModel.getContents().add(semanticModel);
					}

		            Diagram view = ViewService.createDiagram(semanticModel, kind,
		                new PreferencesHint(LogicDiagramPlugin.EDITOR_ID));
		            
		            if (view != null) {
		                notationModel.getContents().add(0,view);
		                view.getDiagram().setName(newDiagramFile.getName());
		            }

		            try {
		                notationModel.save(Collections.EMPTY_MAP);
		                semanticModel.eResource().save(Collections.EMPTY_MAP);
		            } catch (IOException e) {
		                Trace.catching(LogicDiagramPlugin.getInstance(),
		                    LogicDiagramDebugOptions.EXCEPTIONS_CATCHING,
		                    LogicEditorUtil.class, "createNewDiagramFile", e); //$NON-NLS-1$
		                Log.error(LogicDiagramPlugin.getInstance(),
		                    LogicDiagramStatusCodes.IGNORED_EXCEPTION_WARNING, e
		                        .getLocalizedMessage());
		            }
				}
				
				return Status.OK_STATUS;
			}
		};

		
		try {
			op.execute(new NullProgressMonitor(), null);
			
		} catch (ExecutionException e) {
			Trace.catching(LogicDiagramPlugin.getInstance(),
                    LogicDiagramDebugOptions.EXCEPTIONS_CATCHING,
                    LogicEditorUtil.class, "createNewDiagramFile", e); //$NON-NLS-1$
                Log.error(LogicDiagramPlugin.getInstance(),
                    LogicDiagramStatusCodes.IGNORED_EXCEPTION_WARNING, e
                        .getLocalizedMessage());
		}
		
		return newDiagramFile;
	}

}
