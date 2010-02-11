/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;

/**
 * This is just a helper class that is packages the steps necessary to activate
 * a view's window in convenient methods.
 * 
 * @author Yasser Lulu
 */
public final class WorkbenchPartActivator {

	/**
	 * Inner class to perform the work of showing a workbench view in a 
	 * runnable. Also remembers the view part that has been shown, which
	 * can be accessed through #getViewPart().
	 */
	private static class ShowViewRunnable implements Runnable {

		private IViewPart viewPart = null;
		private final String viewId;

		/**
		 * Constructor.
		 * 
		 * @param viewId id of view to show
		 */
		public ShowViewRunnable(String viewId) {
			this.viewId = viewId;
		}

		public void run() {
			IWorkbenchWindow workbenchWindow =
				PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (workbenchWindow == null) {
				return;
			}
			IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			if (workbenchPage == null) {
				return;
			}
			try {
				viewPart = workbenchPage.showView(viewId);
			} catch (PartInitException pie) {
				Trace.catching(
					CommonUIPlugin.getDefault(),
					CommonUIDebugOptions.EXCEPTIONS_CATCHING,
					getClass(),
					pie.getMessage(),
					pie);
				Log.warning(
					CommonUIPlugin.getDefault(),
					CommonUIStatusCodes.GENERAL_UI_FAILURE,
					pie.getMessage(),
					pie);
				String message =
					MessageFormat.format(
						CommonUIMessages.WorkbenchPartActivator_ErrorMessage,
						new Object[] { viewId });
				ErrorDialog.openError(
					Display.getCurrent().getActiveShell(),
					null,
					message,
					new Status(
						IStatus.ERROR,
						CommonUIPlugin.getPluginId(),
						CommonUIStatusCodes.GENERAL_UI_FAILURE,
						pie.getLocalizedMessage(),
						pie));
			}
		}

		/**
		 * Retrieve a view part.
		 * 
		 * @return the <code>IViewPart</code>
		 */
		public IViewPart getViewPart() {
			return viewPart;
		}
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private WorkbenchPartActivator() {
		 /* private constructor */
	}

	/**
	 * The function that tries to activate the view-window in the context of the
	 * GUI thread -which also means create the view if necessary-. 
	 * It blocks until the view-window is activated.
	 * 
	 * @param viewId	The unique id of the view as registered with eclipse.
	 * @return The view part shown, or null if no part was shown.     
	 */
	public static IViewPart showView(final String viewId) {
		ShowViewRunnable runnable = new ShowViewRunnable(viewId);
		Display.getCurrent().syncExec(runnable);
		return runnable.getViewPart();
	}

	/**
	 * A convenience method, calls the #showView with the proper view Id.
	 * @return The view part shown, or null if no part was shown. 
	 */
	public static IViewPart showTaskList() {
		return showView(IPageLayout.ID_TASK_LIST);
	}

	/**
	 * Ensures that the Eclipse 3.0 Problems view is visible, if it is open.
	 * 
	 * @return The Problems view part, or null if it was not shown. 
	 */
	public static IViewPart showProblemView() {
		return showView(IPageLayout.ID_PROBLEM_VIEW);
	}
	
	/**
	 * A convenience method, calls the #showView with the proper view Id.
	 * @return The view part shown, or null if no part was shown. 
	 */
	public static IViewPart showBookmarkNavigator() {
		return showView(IPageLayout.ID_BOOKMARKS);
	}

	/**
	 * A convenience method, calls the #showView with the proper view Id.
	 * @return The view part shown, or null if no part was shown. 
	 */
	public static IViewPart showEditorArea() {
		return showView(IPageLayout.ID_EDITOR_AREA);
	}

	/**
	 * A convenience method, calls the #showView with the proper view Id.
	 * @return The view part shown, or null if no part was shown. 
	 */
	public static IViewPart showContentOutline() {
		return showView(IPageLayout.ID_OUTLINE);
	}

	/**
	 * A convenience method, calls the #showView with the proper view Id.
	 * @return The view part shown, or null if no part was shown. 
	 */
	public static IViewPart showPropertySheet() {
		return showView(IPageLayout.ID_PROP_SHEET);
	}

	/**
	 * A convenience method, calls the #showView with the proper view Id.
	 * @return The view part shown, or null if no part was shown. 
	 */
	public static IViewPart showResourceNavigator() {
		return showView(IPageLayout.ID_PROJECT_EXPLORER);
	}

}
