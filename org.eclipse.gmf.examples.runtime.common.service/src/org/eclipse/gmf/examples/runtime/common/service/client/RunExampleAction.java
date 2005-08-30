/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.common.service.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import org.eclipse.gmf.examples.runtime.common.service.application.WidgetService;

/**
 * Simple action to run the Widget Service example.
 * 
 */
public class RunExampleAction
	implements IWorkbenchWindowActionDelegate {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Uses the WidgetService instance to process a variety of Widget requests.
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		Object widgets = WidgetService.getInstance().createWidget(1000);
		System.out.println(widgets == null ? "No widgets created" : "Created " + unwrapWidgets((List)widgets).size() + " widgets");//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		widgets = WidgetService.getInstance().createWidget(30);
		System.out.println(widgets == null ? "No widgets created" : "Created " + unwrapWidgets((List)widgets).size() + " widgets");//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		widgets = WidgetService.getInstance().createWidget(76);
		System.out.println(widgets == null ? "No widgets created" : "Created " + unwrapWidgets((List)widgets).size() + " widgets");//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Helper method to unwrap a List of Lists
	 * @param widgets
	 * @return
	 */
	private List unwrapWidgets(List widgets) {
		List unwrappedList = new ArrayList();
		Iterator i = widgets.iterator();
		while(i.hasNext()) {
			Object o = i.next();
			if (o instanceof List) {
				unwrappedList.addAll((List)o);
			} else {
				unwrappedList.addAll(widgets);
				break;
			}
		}		
		return unwrappedList;		
	}

}
