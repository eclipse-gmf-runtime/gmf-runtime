/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.providers;

import java.util.Hashtable;

import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandlerProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Concrete class that implements the <code>IGlobalActionHandlerProvider</code>
 * providing <code>IGlobalActionHandler</code> for all diagram ui based
 * diagrams. This provider supports printing of images.
 * 
 * @author cmahoney
 */
public class DiagramWithPrintGlobalActionHandlerProvider
	extends AbstractGlobalActionHandlerProvider {

	private Hashtable handlerList = new Hashtable();

	/**
	 * Constructor for DiagramWithPrintGlobalActionHandlerProvider.
	 */
	public DiagramWithPrintGlobalActionHandlerProvider() {
		super();
	}

	public IGlobalActionHandler getGlobalActionHandler(
			final IGlobalActionHandlerContext context) {
		/* Create the handler */
		if (!getHandlerList().containsKey(context.getActivePart())) {
			getHandlerList().put(context.getActivePart(),
				new DiagramWithPrintGlobalActionHandler());

			/*
			 * Register as a part listener so that the cache can be cleared when
			 * the part is disposed
			 */
			context.getActivePart().getSite().getPage().addPartListener(
				new IPartListener() {

					private IWorkbenchPart localPart = context.getActivePart();

					public void partActivated(IWorkbenchPart part) {
						// Do nothing
					}

					public void partBroughtToTop(IWorkbenchPart part) {
						// Do nothing
					}

					public void partClosed(IWorkbenchPart part) {
						/* Remove the cache associated with the part */
						if (part != null && part == localPart
							&& getHandlerList().containsKey(part)) {
							getHandlerList().remove(part);
							localPart.getSite().getPage().removePartListener(
								this);
							localPart = null;
						}
					}

					public void partDeactivated(IWorkbenchPart part) {
						// Do nothing
					}

					public void partOpened(IWorkbenchPart part) {
						// Do nothing
					}
				});
		}

		return (DiagramWithPrintGlobalActionHandler) getHandlerList().get(
			context.getActivePart());
	}

	/**
	 * Returns the handlerList.
	 * 
	 * @return Hashtable
	 */
	private Hashtable getHandlerList() {
		return handlerList;
	}
}
