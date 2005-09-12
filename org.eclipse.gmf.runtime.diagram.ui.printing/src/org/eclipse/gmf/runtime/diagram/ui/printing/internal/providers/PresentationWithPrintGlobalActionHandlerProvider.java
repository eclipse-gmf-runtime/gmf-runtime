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

package org.eclipse.gmf.runtime.diagram.ui.printing.internal.providers;

import java.util.Hashtable;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandlerProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext;

/**
 * Concrete class that implements the <code>IGlobalActionHandlerProvider</code> providing
 * <code>IGlobalActionHandler</code> for all diagram ui based diagrams. This provider is 
 * installed with a lowest priority.
 * <p>
 * Returns {@link org.eclipse.gmf.runtime.diagram.ui.printing.internal.providers.PresentationWithPrintGlobalActionHandler} when
 * queried for global action handler.
 * 
 * @author tmacdoug
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.printing.*
 */
public final class PresentationWithPrintGlobalActionHandlerProvider
	extends AbstractGlobalActionHandlerProvider {
	/**
	 * List for PresentationWithPrintGlobalActionHandlers.
	 */
	private Hashtable handlerList = new Hashtable();

	/**
	 * Constructor for PresentationWithPrintGlobalActionHandlerProvider.
	 */
	public PresentationWithPrintGlobalActionHandlerProvider() {
		super();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.internal.services.action.global.IGlobalActionHandlerProvider#getGlobalActionHandler(IGlobalActionHandlerContext)
	 */
	public IGlobalActionHandler getGlobalActionHandler(final IGlobalActionHandlerContext context) {
		/* Create the handler */
		if (!getHandlerList().containsKey(context.getActivePart())) {
			getHandlerList().put(
				context.getActivePart(),
				new PresentationWithPrintGlobalActionHandler());

			/* Register as a part listener so that the cache can be cleared
			 * when the part is disposed */
			context
				.getActivePart()
				.getSite()
				.getPage()
				.addPartListener(new IPartListener() {
				private IWorkbenchPart localPart = context.getActivePart();
				/**
				 * @see org.eclipse.ui.IPartListener#partActivated(IWorkbenchPart)
				 */
				public void partActivated(IWorkbenchPart part) {
					//Do nothing
				}

				/**
				 * @see org.eclipse.ui.IPartListener#partBroughtToTop(IWorkbenchPart)
				 */
				public void partBroughtToTop(IWorkbenchPart part) {
					//Do nothing
				}

				/**
				 * @see org.eclipse.ui.IPartListener#partClosed(IWorkbenchPart)
				 */
				public void partClosed(IWorkbenchPart part) {
					/* Remove the cache associated with the part */
					if (part != null && part == localPart && getHandlerList().containsKey(part)) {
						getHandlerList().remove(part);
						localPart.getSite().getPage().removePartListener(this);
						localPart = null;
					}
				}

				/**
				 * @see org.eclipse.ui.IPartListener#partDeactivated(IWorkbenchPart)
				 */
				public void partDeactivated(IWorkbenchPart part) {
					//Do nothing
				}

				/**
				 * @see org.eclipse.ui.IPartListener#partOpened(IWorkbenchPart)
				 */
				public void partOpened(IWorkbenchPart part) {
					//Do nothing
				}
			});
		}

		return (PresentationWithPrintGlobalActionHandler) getHandlerList().get(
			context.getActivePart());
	}

	/**
	 * Returns the handlerList.
	 * @return Hashtable
	 */
	private Hashtable getHandlerList() {
		return handlerList;
	}
}

