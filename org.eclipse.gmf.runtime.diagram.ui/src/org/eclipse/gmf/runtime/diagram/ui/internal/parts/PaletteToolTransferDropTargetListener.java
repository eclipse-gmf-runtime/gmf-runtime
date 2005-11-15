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

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.Tool;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;

/**
 * Performs a native Drop using the {@link TemplateTransfer} where the template
 * is a
 * {@link org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteToolEntry}
 * that uses a {@link org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool}.
 * The request from the creation tool is used to get a command to create the new
 * shape. The creation tool is also used to select the new added shape.
 * 
 * @see org.eclipse.gmf.runtime.diagram.ui.internal.parts.PaletteToolTransferDragSourceListener
 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorWithFlyOutPalette
 * 
 * @author cmahoney
 */
public class PaletteToolTransferDropTargetListener
	extends org.eclipse.gef.dnd.TemplateTransferDropTargetListener {

	/**
	 * Constructs a listener on the specified viewer.
	 * 
	 * @param viewer
	 *            the EditPartViewer
	 */
	public PaletteToolTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	/**
	 * If the template is a palette entry with a creation tool, then the request
	 * from the creation tool is used.
	 */
	protected Request createTargetRequest() {

		CreationTool tool = getCreationTool();

		if (tool != null) {

			tool.setViewer(getViewer());
			tool.setEditDomain(getViewer().getEditDomain());

			return tool.createCreateRequest();
		}

		return null;
	}

	/**
	 * Gets the creation tool from the template if it is a palette entry.
	 * 
	 * @return The creation tool or null if not applicable.
	 */
	private CreationTool getCreationTool() {
		Object template = TemplateTransfer.getInstance().getTemplate();
		if (template instanceof PaletteToolEntry) {
			Tool tool = ((PaletteToolEntry) template).createTool();
			if (tool instanceof CreationTool) {
				return (CreationTool) tool;
			}
		}
		return null;
	}

	protected void handleDrop() {

		super.handleDrop();

		if (getTargetRequest() instanceof CreateRequest) {
			Object newObject = getCreateRequest().getNewObject();

			Collection newObjectCollection = (newObject instanceof Collection ? (Collection) newObject
				: Collections.singletonList(newObject));
			getCreationTool().selectNewShapes(getViewer(),
				newObjectCollection);

		}
	}

	/**
	 * This template transfer uses the creation tool in the template instead of
	 * a creation factory.
	 */
	protected CreationFactory getFactory(Object template) {
		throw new UnsupportedOperationException("Factory method not used."); //$NON-NLS-1$
	}
}
