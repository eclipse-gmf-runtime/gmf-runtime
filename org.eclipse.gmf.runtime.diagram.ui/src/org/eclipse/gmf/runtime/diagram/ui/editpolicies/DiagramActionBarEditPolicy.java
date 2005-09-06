/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
 * This is the default action bar editpolicy installed on diagrams. The
 * actionbar is populated using the element types of the tools of the palette
 * drawer of the last selected palette tool. If the diagram was just opened, the
 * actionbar is populated using the element types of the tools of the palette
 * drawer that is initially open. If there is no drawer initially open, then
 * <code>buildDefaultActionBar()</code> is called to initially populate the
 * actionbar.
 * 
 * @author cmahoney
 */
public class DiagramActionBarEditPolicy
	extends ActionBarEditPolicy
	implements PaletteListener {

	/**
	 * Holds the last active palette tool.
	 */
	private ToolEntry theLastTool = null;

//	/**
//	 * Creates a new instance.
//	 */
//	public DiagramActionBarEditPolicy() {
//		super();
//		this.setIsDisplayAtMouseHoverLocation(true);
//	}

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
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ActionBarEditPolicy#fillActionDescriptors()
	 */
	protected void fillActionDescriptors() {
		fillBasedOnLastActivePaletteTool();
		if (getActionBarDescriptors().isEmpty()) {
			fillBasedOnOpenPaletteDrawer();
			if (getActionBarDescriptors().isEmpty()) {
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
	 * Adds action bar descriptors for all the shape tools in the palette
	 * container of the last active palette tool.
	 */
	private void fillBasedOnLastActivePaletteTool() {
		if (theLastTool == null)
			return;

		PaletteContainer palContainer = theLastTool.getParent();
		fillWithPaletteToolsInContainer(palContainer);
	}

	/**
	 * Adds action bar descriptors for all the shape tools in the given palette
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
								addActionBarDescriptor2(theToolType, IconService
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
	 * Adds action bar descriptors for all the shape tools in the palette drawer
	 * that is initially open.
	 */
	private void fillBasedOnOpenPaletteDrawer() {
		PaletteViewer paletteViewer = getHost().getViewer().getEditDomain()
			.getPaletteViewer();
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

	/**
	 * Subclasses can override to provide default tools if actionbar cannot be
	 * populated based on the state of the palette.
	 */
	protected void fillWithDefaults() {
		// by default, add no action bar descriptors.
	}

}