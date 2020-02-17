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

package org.eclipse.gmf.runtime.diagram.ui.render.providers;

import java.util.Hashtable;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandlerProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.providers.ImageSupportGlobalActionHandler;

/**
 * Concrete class that implements the <code>IGlobalActionHandlerProvider</code>
 * providing <code>IGlobalActionHandler</code> for all diagrams supporting
 * render capabilities.
 * 
 * @author cmahoney
 */
public final class DiagramUIRenderGlobalActionHandlerProvider
	extends AbstractGlobalActionHandlerProvider {

	/**
	 * List for handlers.
	 */
	private Hashtable handlerList = new Hashtable();

	/**
	 * Creates a new instance.
	 */
	public DiagramUIRenderGlobalActionHandlerProvider() {
		super();
	}

	/**
	 * Returns a global action handler that supports global image operations
	 * (cut, copy, and paste).
	 */
	public IGlobalActionHandler getGlobalActionHandler(
			final IGlobalActionHandlerContext context) {
		/* Create the handler */
		if (!getHandlerList().containsKey(context.getActivePart())) {
			getHandlerList().put(context.getActivePart(),
				new ImageSupportGlobalActionHandler());

			/*
			 * Register as a part listener so that the cache can be cleared when
			 * the part is disposed
			 */
			context.getActivePart().getSite().getPage().addPartListener(
				new IPartListener() {

					private IWorkbenchPart localPart = context.getActivePart();

					/**
					 * @see org.eclipse.ui.IPartListener#partActivated(IWorkbenchPart)
					 */
					public void partActivated(IWorkbenchPart part) {
						// Do nothing
					}

					/**
					 * @see org.eclipse.ui.IPartListener#partBroughtToTop(IWorkbenchPart)
					 */
					public void partBroughtToTop(IWorkbenchPart part) {
						// Do nothing
					}

					/**
					 * @see org.eclipse.ui.IPartListener#partClosed(IWorkbenchPart)
					 */
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

					/**
					 * @see org.eclipse.ui.IPartListener#partDeactivated(IWorkbenchPart)
					 */
					public void partDeactivated(IWorkbenchPart part) {
						// Do nothing
					}

					/**
					 * @see org.eclipse.ui.IPartListener#partOpened(IWorkbenchPart)
					 */
					public void partOpened(IWorkbenchPart part) {
						// Do nothing
					}
				});
		}

		return (ImageSupportGlobalActionHandler) getHandlerList().get(
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
