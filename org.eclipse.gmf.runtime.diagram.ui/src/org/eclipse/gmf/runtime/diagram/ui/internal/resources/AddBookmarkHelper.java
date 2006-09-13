/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.resources;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.resources.IBookmark;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Helper class for creating a bookmark for the selected element
 * 
 * @author Kevin Cornell
 * @canBeSeenBy %level1
 * @author Michael Yee
 */
public class AddBookmarkHelper {
	/**
	 * Add bookmark to given selection.  Note that the assertion statements
	 * are taken from the canBookmark() constraints
	 * 
	 * @param editorPart the editorPart 
	 */
	static public void addBookmark(final IDiagramWorkbenchPart editorPart) {
		final IStructuredSelection selection =
			(IStructuredSelection) editorPart
				.getSite()
				.getWorkbenchWindow()
				.getSelectionService()
				.getSelection();

		try {
			TransactionUtil.getEditingDomain(editorPart.getDiagram())
				.runExclusive(new Runnable() {

					public void run() {
					for (Iterator i = selection.toList().iterator();
						i.hasNext();
						) {
						// Get the selected object as an element.
						Object selectedObject = i.next();
						Assert.isTrue(selectedObject instanceof EditPart);
	
						View view =
							(View)((EditPart) selectedObject).getAdapter(
								View.class);
						Assert.isTrue(selectedObject instanceof IPrimaryEditPart);
						Assert.isNotNull(view);
						Assert.isTrue(view.eResource()!=null);
	
						// Create the "add bookmark" command if the view is defined.
						// By default, name the bookmark with the element's fully qualified name
						String elementName = StringStatics.BLANK;
						EObject semanticElement = ViewUtil.resolveSemanticElement(view);
	
						if (semanticElement != null) {
							elementName = EMFCoreUtil.getQualifiedName(semanticElement,true);
						}
	
						// Obtain the marker description from the user.  
						String description =
							getDescription(elementName, editorPart);
	
						// If the user did not cancel the dialog (the prompt for the description)
						if (description != null) {
							// Create the marker
							HashMap attribMap = new HashMap();
							attribMap.put(IMarker.MESSAGE, description);
							attribMap.put(IMarker.LOCATION, elementName);
							attribMap.put(IBookmark.ELEMENT_ID,
									((XMLResource) view.eResource())
										.getID(view));
	
							IResource resource = getResource(editorPart);
							Assert.isNotNull(resource);
	
							try {
								IMarker marker =
									resource.createMarker(IBookmark.TYPE);
								marker.setAttributes(attribMap);
							} catch (CoreException e) {
								Trace.catching(DiagramUIPlugin.getInstance(), DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), "addBookmark", e); //$NON-NLS-1$
								Log.error(DiagramUIPlugin.getInstance(), IStatus.ERROR, "addBookmark"); //$NON-NLS-1$
							}
						}
					}
				}
			});
		}catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
					DiagramUIDebugOptions.EXCEPTIONS_CATCHING, AddBookmarkHelper.class,
					"addBookmark()", //$NON-NLS-1$
					e);
		}
	}

	/**
	 * Gets the resource for the given workbench part
	 * @param part the given workbench part
	 * @return the resource for the given workbench part, otherwise <code>null</code>
	 */
	static private IResource getResource(IWorkbenchPart part) {
		// Attempt a generic way to get the resource from the editor input.
		if (part instanceof IEditorPart) {
			IEditorInput input = ((IEditorPart) part).getEditorInput();
			return (IFile) input.getAdapter(IFile.class);
		}
		return null;
	}

	/**
//	 * Gets the root resource of the workspace
//	 * @return the root resource of the workspace
//	 */
//	static private IWorkspaceRoot getWorkspaceRoot() {
//		return ResourcesPlugin.getWorkspace().getRoot();
//	}

	/**
	 * Get the bookmark description.
	 * @param defaultDescription the suggested bookmark name
	 * @return the bookmark name or <code>null</code> if cancelled.
	 */
	static private String getDescription(
		String defaultDescription,
		IWorkbenchPart part) {
		String title = DiagramUIMessages.AddBookmarkAction_dialog_title;
		String message = DiagramUIMessages.AddBookmarkAction_dialog_message;

		IInputValidator inputValidator = new IInputValidator() {
			public String isValid(String newText) {
				return (newText == null || newText.length() == 0)
					? StringStatics.SPACE
					: null;
			}
		};
		InputDialog dialog =
			new InputDialog(
				part.getSite().getShell(),
				title,
				message,
				defaultDescription,
				inputValidator);

		// If the user cancelled the dialog, do not create the bookmark.
		if (dialog.open() != Window.CANCEL) {
			String name = dialog.getValue();
			if (name == null)
				return null;
			name = name.trim();
			return (name.length() == 0) ? null : name;
		} else {
			return null;
		}
	}

}
