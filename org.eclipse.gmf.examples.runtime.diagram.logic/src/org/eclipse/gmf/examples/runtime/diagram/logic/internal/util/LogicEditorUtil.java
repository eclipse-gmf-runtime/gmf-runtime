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

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramDebugOptions;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
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
			DiagramFileCreator diagramFileCreator,
			IPath containerPath, String fileName, InputStream initialContents,
			String kind, IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor, boolean openEditor,
			boolean saveDiagram) {
		IFile newFile = LogicEditorUtil.createNewDiagramFile(diagramFileCreator,
			containerPath, fileName, initialContents, kind, dWindow.getShell(),
			progressMonitor);

		if (newFile != null && openEditor) {
			//Since the file resource was created fine, open it for editing
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
			DiagramFileCreator diagramFileCreator,
			IPath containerFullPath, String fileName,
			InputStream initialContents, final String kind,
			Shell shell, final IProgressMonitor progressMonitor) {
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

		// Fill the contents of the file dynamically
		Resource notationModel = null;
		try {
			newDiagramFile.refreshLocal(IResource.DEPTH_ZERO, null); //RATLC00514368
			InputStream stream = newDiagramFile.getContents();
			String completeFileName = newDiagramFile.getLocation().toOSString();

			try {
				// Empty file....
				notationModel = ResourceUtil.create(completeFileName, null);
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
			final Resource notationModel_ = notationModel;
			OperationUtil.runAsUnchecked(new MRunnable() {
				public Object run() {
					//create model semantic element and hook it up with diagram view
					EObject model = EObjectUtil.create(SemanticPackage.eINSTANCE.getModel());
					Diagram view = DiagramUtil.createDiagram(model, kind, new PreferencesHint(LogicDiagramPlugin.EDITOR_ID));
					if (view != null) {
						notationModel_.getContents().add(view);
						notationModel_.getContents().add(model);
						view.getDiagram().setName(newDiagramFile.getName());
					}
					return null;
				}
			});
		}

		return newDiagramFile;
	}

}
