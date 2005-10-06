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


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts.FileResourceEditorInput;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Diagram Utility Class for Editor.
 *
 * @author qili
 * @canBeSeenBy %level1
 * 
 */
public class EditorUtil {

	/**
	 * @param diagramFileCreator
	 * @param containerPath
	 * @param fileName
	 * @param diagramId
	 * @param initialContents
	 * @param kind
	 * @param dWindow
	 * @param progressMonitor
	 * @param openEditor
	 * @param saveDiagram
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return
	 */
	public static final IFile createAndOpenDiagram(
			EditorFileCreator diagramFileCreator,
			IPath containerPath, String fileName, String diagramId,
			InputStream initialContents,
			String kind, IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor, boolean openEditor,
			boolean saveDiagram, PreferencesHint preferencesHint) {
		IFile newFile = EditorUtil.createNewDiagramFile(diagramFileCreator,
			containerPath, fileName, initialContents, kind, dWindow.getShell(),
			progressMonitor, preferencesHint);

		if (newFile != null && openEditor) {
			//Since the file resource was created fine, open it for editing
			// iff requested by the user
			EditorUtil.openDiagram(newFile, dWindow, saveDiagram,
				progressMonitor, diagramId);
			
		}

		return newFile;
	}
	
	public static final DiagramEditPart openDiagram(IFile file,
			IWorkbenchWindow dWindow, boolean saveDiagram,
			IProgressMonitor progressMonitor, String diagramId) {
		IEditorPart editorPart = null;
		try {
			//TODO which MEditingDomain to use?
			IWorkbenchPage page = dWindow.getActivePage();
			if (page != null) {
				
				editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().openEditor(new FileResourceEditorInput(file),
					diagramId);

				if (saveDiagram)
					editorPart.doSave(progressMonitor);
			}
			file.refreshLocal(IResource.DEPTH_ZERO, null);
			return ((IDiagramWorkbenchPart) editorPart).getDiagramEditPart();
		} catch (Exception e) {
			Trace.catching(EditorPlugin.getInstance(),
				EditorDebugOptions.EXCEPTIONS_CATCHING,
				EditorUtil.class, "openDiagram", e); //$NON-NLS-1$
		}

		return null;
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
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 *
	 * @return the created file resource, or <code>null</code> if the file was
	 *         not created
	 */
	public static IFile createNewDiagramFile(
			EditorFileCreator diagramFileCreator,
			IPath containerFullPath, String fileName,
			InputStream initialContents, final String kind,
			Shell shell, final IProgressMonitor progressMonitor, final PreferencesHint preferencesHint) {
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
				notationModel = ResourceUtil.create(completeFileName, null);//TODO which MEditingDomain to use?
			} finally {
				stream.close();
			}

		} catch (Exception e) {
			Trace.catching(EditorPlugin.getInstance(),
				EditorDebugOptions.EXCEPTIONS_CATCHING,
				EditorUtil.class, "createNewDiagramFile", //$NON-NLS-1$
				e);
		}

		if (notationModel != null) {
			final Resource notationModel_ = notationModel;
			MEditingDomainGetter.getMEditingDomain(notationModel_).runAsUnchecked(new MRunnable() {
				public Object run() {
					View view = DiagramUtil
						.createDiagram(null, kind, preferencesHint);
					
					if (view != null) {
						notationModel_.getContents().add(view.getDiagram());
						view.getDiagram().setName(newDiagramFile.getName());
					}
					return null;
				}
			});
		}

		return newDiagramFile;
	}

	/**
	 * Gets the default diagram path for a new diagram that will contain the
	 * elements in <code>elements</code>. Returns <code>null</code> if
	 * there is no IVizUIHandler to handle the visualization, or there is a
	 * handler, but it has no default diagram path for the elements.
	 * <P>
	 *
	 * @param elements
	 *            the list of elements being visualized on a new class diagram
	 * @param diagramKind
	 *            the kind of diagram we want to create
	 * @return the default diagram path for a new diagram that will contain the
	 *         elements in <code>elements</code>. Returns <code>null</code>
	 *         if there is no IVizUIHandler to handle the visualization, or
	 *         there is a handler, but it has no default diagram path for the
	 *         elements.
	 */
	public static IPath getDefaultDiagramPath(List elements,
			String diagramKind) {

		IPath path = null;

		return path;
	}

	/**
	 * Method getInitialContents. Gets the initial contents of the UML
	 * Visualizer diagram file. Currently it returns an empty byte stream.
	 *
	 * @return InputStream Byte stream that will initially populate the UML
	 *         Visualizer diagram file.
	 */
	public static InputStream getInitialContents() {
		return new ByteArrayInputStream(new byte[0]);
	}
}