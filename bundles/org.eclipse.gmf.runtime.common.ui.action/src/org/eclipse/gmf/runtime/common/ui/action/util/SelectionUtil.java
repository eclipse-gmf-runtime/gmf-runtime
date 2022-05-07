/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.util.IInlineTextEditorPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ISetSelectionTarget;

/**
 * This class provides selection utility methods.
 * 
 * @author dmisic
 */
public class SelectionUtil {

	/**
	 * This class should not be instantiated since it provides static utility
	 * methods.
	 */
	private SelectionUtil() {
		super();
	}

	/**
	 * Attempts to select and reveal the specified object in all parts within
	 * the supplied workbench window's active page. Checks all parts in the
	 * active page to see if they implement <code>ISetSelectionTarget</code>,
	 * either directly or as an adapter. If so, tells the part to select and
	 * reveal the specified resource.
	 * 
	 * @param obj
	 *            The object to be selected and revealed
	 * @param window
	 *            The workbench window to select and reveal the resource
	 * 
	 * @see ISetSelectionTarget
	 */
	public static void selectAndReveal(Object obj, IWorkbenchWindow window) {

		// validate the input
		if (window == null || obj == null)
			return;
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;

		// get all the view and editor parts
		List parts = new ArrayList();
		IWorkbenchPartReference refs[] = page.getViewReferences();
		for (int i = 0; i < refs.length; i++) {
			IWorkbenchPart part = refs[i].getPart(false);
			if (part != null)
				parts.add(part);
		}
		refs = page.getEditorReferences();
		for (int i = 0; i < refs.length; i++) {
			IWorkbenchPart part = refs[i].getPart(false);
			if (part != null)
				parts.add(part);
		}

		final ISelection selection = new StructuredSelection(obj);
		Iterator it = parts.iterator();
		while (it.hasNext()) {
			IWorkbenchPart part = (IWorkbenchPart) it.next();

			// get the part's ISetSelectionTarget implementation
			ISetSelectionTarget target = null;
			if (part instanceof ISetSelectionTarget)
				target = (ISetSelectionTarget) part;
			else
				target = (ISetSelectionTarget) part
					.getAdapter(ISetSelectionTarget.class);

			if (target != null) {
				// select and reveal resource
				final ISetSelectionTarget finalTarget = target;
				window.getShell().getDisplay().asyncExec(new Runnable() {

					public void run() {
						finalTarget.selectReveal(selection);
					}
				});
			}
		}
	}
	
	/**
	 * Tries to get an IInlineTextEditorPart from the part parameter.
	 * It checks to see if the part implements the IInlineTextEditorPart
	 * interface, and if not, it will try to use the IAdaptable mechanism 
	 * to ask for an IInlineTextEditorPart
	 *  
	 * @param part the part to extract IInlineTextEditorPart from
	 * @return IInlineTextEditorPart if possible or null
	 */
	private static IInlineTextEditorPart getInlineTextEditorPart(
			IWorkbenchPart part) {
		if (part instanceof IInlineTextEditorPart) {
			return (IInlineTextEditorPart) part;
		} else {
			return (IInlineTextEditorPart) ((IAdaptable) part)
				.getAdapter(IInlineTextEditorPart.class);
		}
	}

	/**
	 * Select, reveal and start inline editing on the new element, if
	 * appropriate.
	 * 
	 * @param part
	 *            the workbench part in which to start the inline editing
	 * @param newElement
	 *            the new element
	 */
	public static void startInlineEdit(final IWorkbenchPart part,
			final Object newElement) {
		//Run the select and reveal and inline-editor bits asynchronously
		// to ensure that the content provider has had a chance to receive
		// the event notification that a new element was added to the model.
		// Otherwise, the element will not be found in the select/reveal and
		// the inline editor will not be able to start on the new element.		
		part.getSite().getShell().getDisplay().asyncExec(new Runnable() {

			public void run() {
				IInlineTextEditorPart inlineTextEditorPart = getInlineTextEditorPart(part);
				if (inlineTextEditorPart != null) {
					inlineTextEditorPart.startInlineEdit(newElement);
				} else {
					//original impl.
					if (part instanceof ISetSelectionTarget) {
						((ISetSelectionTarget) part)
							.selectReveal(new StructuredSelection(newElement));
					}
					if (part instanceof IInlineTextEditorPart) {
						((IInlineTextEditorPart) part).startInlineEdit();
					}
				}
			}
		});
	}

	/**
	 * Select, reveal and start renaming the new element, if appropriate.
	 * 
	 * @param part
	 *            the workbench part in which to start the global rename action
	 * @param newElement
	 *            the new element
	 */
	public static void startRename(final IWorkbenchPart part,
			final Object newElement) {
		// Run the select and rename asynchronously
		// to ensure that the content provider has had a chance to receive
		// the event notification that a new element was added to the model.
		// Otherwise, the element will not be found in the select/reveal.
		part.getSite().getShell().getDisplay().asyncExec(new Runnable() {

			public void run() {
				IInlineTextEditorPart inlineTextEditorPart = getInlineTextEditorPart(part);
				if (inlineTextEditorPart != null) {
					inlineTextEditorPart.startInlineEdit(newElement,
						new Runnable() {

							public void run() {
								GlobalAction renameAction = GlobalActionManager
									.getInstance().getGlobalAction(part,
										GlobalActionId.RENAME);
								if ((renameAction != null)
									&& (renameAction.isRunnable())) {
									renameAction.run();
								}
							}
						});
				} else {
					//original impl.
					if (part instanceof ISetSelectionTarget) {
						((ISetSelectionTarget) part)
							.selectReveal(new StructuredSelection(newElement));
					}
					GlobalAction renameAction = GlobalActionManager
						.getInstance().getGlobalAction(part,
							GlobalActionId.RENAME);
					if ((renameAction != null) && (renameAction.isRunnable())) {
						renameAction.run();
					}
				}
			}
		});
	}

}