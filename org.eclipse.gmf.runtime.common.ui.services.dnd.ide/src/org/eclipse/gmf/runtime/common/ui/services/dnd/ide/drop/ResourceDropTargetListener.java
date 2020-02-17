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

package org.eclipse.gmf.runtime.common.ui.services.dnd.ide.drop;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.views.navigator.NavigatorDropAdapter;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferId;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetEvent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.ide.core.IDETransferId;

/**
 * Concrete class that implements the IDropTargetListener. This class handles
 * dropping resource elements.
 * 
 * @author ldamus
 */
public class ResourceDropTargetListener
	extends NavigatorDropAdapter
	implements IDropTargetListener {

	/**
	 * Constructs a new drop listener for the given viewer.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public ResourceDropTargetListener(StructuredViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.IDropTargetListener#getExecutableContext(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public ICommand getExecutableContext(DropTargetEvent event) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.IDropTargetListener#canSupport(org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.IDropTargetContext,
	 *      org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.IDropTargetEvent,
	 *      org.eclipse.gmf.runtime.common.ui.internal.dnd.ITransferAgent)
	 */
	public boolean canSupport(IDropTargetContext context,
			IDropTargetEvent currentEvent, ITransferAgent currentAgent) {

		Object target = context.getCurrentTarget();
		return (target instanceof IResource)
			|| ((target instanceof IAdaptable) && ((IAdaptable) target)
				.getAdapter(IResource.class) != null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.IDropTargetListener#setFeedback(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void setFeedback(DropTargetEvent event) {

		if (FileTransfer.getInstance().isSupportedType(event.currentDataType))
			// if dropping a file, make a copy gesture instead of move
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.IDropTargetListener#getSupportingTransferIds()
	 */
	public String[] getSupportingTransferIds() {
		return new String[] {IDETransferId.NAV_SELECTION_TRANSFER,
			TransferId.FILE_TRANSFER};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		Object target = getCurrentTarget();
		if (!(target instanceof IResource)) {
			if (target instanceof IAdaptable) {
				target = ((IAdaptable) target).getAdapter(IResource.class);
			}
		}
		if (!validateDrop(target, event.detail, event.currentDataType)) {
			event.detail = DND.DROP_NONE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.navigator.NavigatorDropAdapter#validateDrop(java.lang.Object,
	 *      int, org.eclipse.swt.dnd.TransferData)
	 */
	public boolean validateDrop(Object target, int dragOperation,
			TransferData transferType) {
		// adapt the target to IResource if necessary
		Object adaptedTarget = target;
		if (!(adaptedTarget instanceof IResource)) {
			if (adaptedTarget instanceof IAdaptable) {
				adaptedTarget = ((IAdaptable) target)
					.getAdapter(IResource.class);
			}
		}

		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
			// validate that the source is IResource, otherwise superclass's
			// call fails
			ISelection selection = LocalSelectionTransfer.getTransfer()
				.getSelection();
			if (selection instanceof IStructuredSelection) {
				List selectionList = ((IStructuredSelection) selection)
					.toList();
				Iterator i = selectionList.iterator();
				while (i.hasNext()) {
					if (!(i.next() instanceof IResource)) {
						return false;
					}
				}
			}
		}

		return super.validateDrop(adaptedTarget, dragOperation, transferType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#getCurrentTarget()
	 */
	protected Object getCurrentTarget() {
		Object target = super.getCurrentTarget();
		if (!(target instanceof IResource)) {
			if (target instanceof IAdaptable) {
				target = ((IAdaptable) target).getAdapter(IResource.class);
			}
		}
		return target;
	}

}