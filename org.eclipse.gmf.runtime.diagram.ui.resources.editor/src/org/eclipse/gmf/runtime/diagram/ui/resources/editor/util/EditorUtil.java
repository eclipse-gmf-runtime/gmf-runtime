/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;


/**
 * Diagram Utility Class for Editor.
 *
 * @author qili
 * 
 */
public class EditorUtil {

	/**
	 * Creates a new diagram file resource in the selected container and with
	 * the selected name. Creates any missing resource containers along the
	 * path; does nothing if the container resources already exist. Creates a
	 * new editing domain for this diagram.
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
			DiagramFileCreator diagramFileCreator,
			IPath containerFullPath, String fileName,
			InputStream initialContents, final String kind,
			Shell shell, final IProgressMonitor progressMonitor,
			final PreferencesHint preferencesHint) {
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
			String completeFileName = newDiagramFile.getFullPath().toString();

			try {
				// Empty file....
                ResourceSet resourceSet = new ResourceSetImpl();
                notationModel = resourceSet.createResource(URI
                    .createPlatformResourceURI(completeFileName, true)); 
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
            View view = ViewService.createDiagram(kind, preferencesHint);

            if (view != null) {
                notationModel.getContents().add(view.getDiagram());
                view.getDiagram().setName(newDiagramFile.getName());
            }
		}
		try {
            notationModel.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
            Trace.catching(EditorPlugin.getInstance(),
                EditorDebugOptions.EXCEPTIONS_CATCHING, EditorUtil.class,
                "createNewDiagramFile", e); //$NON-NLS-1$
            Log.error(EditorPlugin.getInstance(),
                EditorStatusCodes.RESOURCE_FAILURE, e.getLocalizedMessage());
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
	 * @return Byte stream that will initially populate the UML
	 *         Visualizer diagram file.
	 */
	public static InputStream getInitialContents() {
		return new ByteArrayInputStream(new byte[0]);
	}
}
