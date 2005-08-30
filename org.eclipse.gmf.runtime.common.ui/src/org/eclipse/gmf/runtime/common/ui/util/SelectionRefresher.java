/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;

/**
 * This class serves the purpose of refershing selection in shell.  
 * Refreshing selection has some positive side affects, one which is the 
 * refreshing of properties in the property browser.
 * Note: Properties will not be refreshed if SelectionProvider is null.
 * 
 * @author Tauseef A. Israr
 * Created on: Oct 10, 2002
 * 
 * 
 */
public class SelectionRefresher {

	/**
	 * represents the number of unhandled requests
	 * 0 = no requests
	 * > 0 processing requests
	 */
	private static int count = 0;

	/**
	 * Clients call this function to refresh the selection.
	 * This function is implemented in a way in which it refreshes at the 
	 * instance it is first called and while the refresh is in progress, if 
	 * it receives more messages, it queues them and only refresh once after 
	 * that.
	 * 
	 */
	public synchronized static void refreshSelection() {
		if (0 == count) {
			Thread thread1 = getJavaThread();
			thread1.start();
		}
		count++;
	}

	/**
	 * Method to inform that the thread has finished refreshing the selection.
	 */
	private synchronized static void finishedRefresh() {

		if (count == 1) {
			count = 0;
		}
		if (count > 1) {
			count = 1;
			Thread thread1 = getJavaThread();
			thread1.start();
		}
	}

	/**
	 * Refreshes the current property sheet page so that 
	 * its model is updated from its input source.
	 */
	private static void doRun() {

		PropertySheetUtil.refreshCurrentPage();
	}

	/**
	 * 
	 * @param count the counter is 
	 * @return Thread
	 */
	private static Thread getJavaThread() {

		Thread thread1 = new Thread() {

			public void run() {

				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							doRun();
							///////////////////////////////////////////////////////////////////////////////
							//                            IWorkbench workBench = PlatformUI.getWorkbench();
							//
							//                            IWorkbenchWindow workbenchwindow =
							//                                workBench.getActiveWorkbenchWindow();
							//
							//                            IWorkbenchPage page =
							//                                workbenchwindow.getActivePage();
							//
							//                            IViewReference[] parts = page.getViewReferences();
							//
							//                            for (int i = 0; i < parts.length; i++) {
							//                                // Refresh each part of the active page
							//                                IWorkbenchPart part = parts[i].getPart(false);
							//
							//                                if (part != null) {
							//                                    ISelectionProvider selectionProvider =
							//                                        part.getSite().getSelectionProvider();
							//
							//                                    if (selectionProvider != null) {
							//                                        ISelection selection =
							//                                            selectionProvider.getSelection();
							//                                        selectionProvider.setSelection(
							//                                            StructuredSelection.EMPTY);
							//                                        selectionProvider.setSelection(
							//                                            selection);
							//                                    }
							//                                }
							//                            }
							///////////////////////////////////////////////////////////////////////////////
							SelectionRefresher.finishedRefresh();

						} catch (NullPointerException e) {
							/*try catch is done so that if any of the variable
							  in try clause is null, the program will not crash.
							  This also means that no selection change will 
							  occur. which also means that no properties will be
							  refreshed.*/
							Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getJavaThread", e); //$NON-NLS-1$
							Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.SERVICE_FAILURE, "getJavaThread", e); //$NON-NLS-1$
						}
					}
				});
			}

		};
		return thread1;
	}

	
    /**
     * This method forces a selection refresh to null back again to selection 
     * previously held.
     */
    public synchronized static void forceSelectionRefresh() {
		IWorkbench workBench = PlatformUI.getWorkbench();

		IWorkbenchWindow workbenchwindow = workBench.getActiveWorkbenchWindow();

		IWorkbenchPage page = workbenchwindow.getActivePage();

        if (page != null){
            IWorkbenchPart part = page.getActivePart();
            
            if (part != null){
                ISelectionProvider selectionProvider =
                    part.getSite().getSelectionProvider();

                if (selectionProvider != null) {
                    ISelection selection = selectionProvider.getSelection();
                    selectionProvider.setSelection(StructuredSelection.EMPTY);
                    selectionProvider.setSelection(selection);
                }
            }
        }
    }

        
           
    /**
     * Retrives the current ISelection
     * @return ISelection The current selection.  Null if no selection is set.
     */
    public static ISelection getCurrentSelection(){
        IWorkbench workBench = PlatformUI.getWorkbench();

        IWorkbenchWindow workbenchwindow = workBench.getActiveWorkbenchWindow();

        IWorkbenchPage page = workbenchwindow.getActivePage();

        if (page != null){
            IWorkbenchPart part = page.getActivePart();
            
            if (part != null){
                ISelectionProvider selectionProvider =
                    part.getSite().getSelectionProvider();

                if (selectionProvider != null) {
                    return selectionProvider.getSelection();
                }
            }
        }
        return null;
    }
        

}
