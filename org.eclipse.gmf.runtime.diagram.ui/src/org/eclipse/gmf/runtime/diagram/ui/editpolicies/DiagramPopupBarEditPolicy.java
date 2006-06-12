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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteStack;

/**
 * This is the default popup bar editpolicy installed on diagrams. The popup bar
 * is populated using the element types of the tools of the palette drawer of
 * the last selected palette tool. If the diagram was just opened, the popup bar
 * is populated using the element types of the tools of the palette drawer that
 * is initially open. If there is no drawer initially open, then
 * <code>fillWithDefaults()</code> is called to initially populate the popup
 * bar.
 * 
 * @author cmahoney
 */
public class DiagramPopupBarEditPolicy
	extends PopupBarEditPolicy
	implements PaletteListener {

	/**
	 * Holds the last active palette tool.
	 */
	private ToolEntry theLastTool = null;

	// /**
	// * Creates a new instance.
	// */
	// public DiagramPopupBarEditPolicy() {
	// super();
	// this.setIsDisplayAtMouseHoverLocation(true);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		addPaletteListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		removePaletteListener();
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy#fillActionDescriptors()
	 */
	protected void fillPopupBarDescriptors() {
		fillBasedOnLastActivePaletteTool();
		if (getPopupBarDescriptors().isEmpty()) {
			fillBasedOnOpenPaletteDrawer();
			if (getPopupBarDescriptors().isEmpty()) {
				fillWithDefaults();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.palette.PaletteListener#activeToolChanged(org.eclipse.gef.ui.palette.PaletteViewer,
	 *      org.eclipse.gef.palette.ToolEntry)
	 */
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (!(tool instanceof SelectionToolEntry)) {
			theLastTool = tool;
		}
	}

	/**
	 * Adds this edit policy as a palette listener.
	 */
	private void addPaletteListener() {
		PaletteViewer paletteViewer = getHost().getViewer().getEditDomain()
			.getPaletteViewer();
		if (paletteViewer != null) {
			paletteViewer.addPaletteListener(this);
		}
	}

	/**
	 * Removes this edit policy as a palette listener.
	 */
	private void removePaletteListener() {
		PaletteViewer paletteViewer = getHost().getViewer().getEditDomain()
			.getPaletteViewer();
		if (paletteViewer != null) {
			paletteViewer.removePaletteListener(this);
		}
		theLastTool = null;
	}

	/**
	 * Adds popup bar descriptors for all the shape tools in the palette
	 * container of the last active palette tool.
	 */
	private void fillBasedOnLastActivePaletteTool() {
		if (theLastTool == null)
			return;

		PaletteContainer palContainer = theLastTool.getParent();
		fillWithPaletteToolsInContainer(palContainer);
	}

	/**
	 * Adds popup bar descriptors for all the shape tools in the given palette
	 * container.
	 * 
	 * @param palContainer
	 *            the <code>PaletteContainer</code>
	 */
	private void fillWithPaletteToolsInContainer(PaletteContainer palContainer) {
		if (palContainer != null) {
			List theEntries = palContainer.getChildren();
			int isz = theEntries.size();
			for (int i = 0; i < isz; i++) {
				PaletteEntry theEntry = (PaletteEntry) theEntries.get(i);

				if (theEntry != null) {
					if (theEntry instanceof PaletteToolEntry) {
						PaletteToolEntry theXtoolsEntry = (PaletteToolEntry) theEntry;
						Tool tempTool = theXtoolsEntry.createTool();
						if ((tempTool != null)
							&& (tempTool instanceof CreationTool)) {
							CreationTool theXtoolsTool = (CreationTool) tempTool;
							IElementType theToolType = theXtoolsTool
								.getElementType();
							if ((theToolType != null)) {
								addPopupBarDescriptor(theToolType, IconService
									.getInstance().getIcon(theToolType));
							}
						}
					} else if (theEntry instanceof PaletteStack) {
						// RATLC00524208: fix for the pallete stack optimzation
						PaletteStack theStack = (PaletteStack) theEntry;
						fillWithPaletteToolsInContainer(theStack);
					}
				}
			}
		}
	}

	/**
	 * Adds popup bar descriptors for all the shape tools in the palette drawer
	 * that is initially open.
	 */
	private void fillBasedOnOpenPaletteDrawer() {
		PaletteViewer paletteViewer = getHost().getViewer().getEditDomain()
			.getPaletteViewer();
        if (paletteViewer != null) {
            for (Iterator iter = paletteViewer.getPaletteRoot().getChildren()
                .iterator(); iter.hasNext();) {
                Object child = iter.next();
                if (child instanceof PaletteDrawer) {
                    PaletteDrawer drawer = (PaletteDrawer) child;
                    if (drawer.isInitiallyOpen()) {
                        fillWithPaletteToolsInContainer(drawer);
                        break;
                    }
                }
            }
        }
	}

	/**
	 * Subclasses can override to provide default tools if the popup bar cannot
	 * be populated based on the state of the palette.
	 */
	protected void fillWithDefaults() {
		// by default, add no popup bar descriptors.
	}

}