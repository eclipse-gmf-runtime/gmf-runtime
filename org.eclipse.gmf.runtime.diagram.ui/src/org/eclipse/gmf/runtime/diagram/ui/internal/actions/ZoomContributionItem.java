/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.IUIConstants;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ZoomContributionItem
	extends CustomContributionItem
	implements ZoomListener, Listener {

	/**
	 * Custom zoom operations
	 */
	private static final String ZOOM_IN = DiagramUIMessages.ZoomAction_ZoomCombo_ZoomIn;
	private static final String ZOOM_OUT = DiagramUIMessages.ZoomAction_ZoomCombo_ZoomOut;
	private static final String ZOOM_100 = DiagramUIMessages.ZoomAction_ZoomCombo_Zoom100;
	private static final String ZOOM_FIT = DiagramUIMessages.ZoomAction_ZoomCombo_ZoomToFit;
	private static final String ZOOM_WIDTH = DiagramUIMessages.ZoomAction_ZoomCombo_ZoomToWidth;
	private static final String ZOOM_HEIGHT = DiagramUIMessages.ZoomAction_ZoomCombo_ZoomToHeight;
	private static final String ZOOM_SELECTION = DiagramUIMessages.ZoomAction_ZoomCombo_ZoomToSelection;
	
	private static final String ZOOM_IN_ACTION = DiagramUIMessages.ZoomAction_ZoomIn;
	private static final String ZOOM_OUT_ACTION = DiagramUIMessages.ZoomAction_ZoomOut;
	private static final String ZOOM_100_ACTION = DiagramUIMessages.ZoomAction_Zoom100;
	private static final String ZOOM_FIT_ACTION = DiagramUIMessages.ZoomAction_ZoomToFit;
	private static final String ZOOM_WIDTH_ACTION = DiagramUIMessages.ZoomAction_ZoomToWidth;
	private static final String ZOOM_HEIGHT_ACTION = DiagramUIMessages.ZoomAction_ZoomToHeight;
	private static final String ZOOM_SELECTION_ACTION = DiagramUIMessages.ZoomAction_ZoomToSelection;
	
	/**
	 * The part's zoom manager
	 */
	private ZoomManager zoomManager;
	/**
	 * The zoom image
	 */
	private List zoomImages = new ArrayList();

	/**
	 * constructor
	 * @param workbenchPage the workbench page
	 */
	public ZoomContributionItem(IWorkbenchPage workbenchPage) {
		super(workbenchPage, ActionIds.CUSTOM_ZOOM);
		setLabel(DiagramUIMessages.ZoomActionMenu_ZoomLabel);		
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.actions.CustomContributionItem#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#setWorkbenchPart(org.eclipse.ui.IWorkbenchPart)
	 */
	protected void setWorkbenchPart(IWorkbenchPart workbenchPart) {
		super.setWorkbenchPart(workbenchPart);
		if (workbenchPart != null)
			setZoomManager(
				(ZoomManager) workbenchPart.getAdapter(ZoomManager.class));
	}

	/**
	 * Returns true if the operation set is not empty and only if the diagram is selected. 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return getZoomManager() != null;
	}

	/**
	 * Returns the zoomManager.
	 * @return ZoomManager
	 */
	public ZoomManager getZoomManager() {
		return zoomManager;
	}

	/**
	 * Sets the ZoomManager
	 * @param zm The ZoomManager
	 */
	public void setZoomManager(ZoomManager zm) {
		if (zoomManager == zm)
			return;
		if (zoomManager != null)
			zoomManager.removeZoomListener(this);

		zoomManager = zm;
		update();

		if (zoomManager != null)
			zoomManager.addZoomListener(this);
	}

	/**
	 * @see org.eclipse.jface.action.ContributionItem#dispose()
	 */
	public void dispose() {
		if (getZoomManager() != null) {
			getZoomManager().removeZoomListener(this);
			zoomManager = null;
		}
		Iterator iter = zoomImages.iterator();
		while (iter.hasNext())
			 ((Image) iter.next()).dispose();
		zoomImages.clear();
		super.dispose();
	}

	/**
	 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
	 */
	public void zoomChanged(double zoom) {
		update();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#createControl(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createControl(Composite parent) {
		Combo combo = new Combo(parent, SWT.DROP_DOWN);
		combo.addListener(SWT.Selection, this);
		combo.addListener(SWT.KeyDown, this);
		combo.setItems(getZoomLevelsAsText());
		combo.setVisibleItemCount(IUIConstants.DEFAULT_DROP_DOWN_SIZE);
		return combo;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#createMenuItem(org.eclipse.swt.widgets.Menu, int)
	 */
	protected MenuItem createMenuItem(Menu parent, int index) {
		MenuItem mi = index >= 0 
			? new MenuItem(parent, SWT.CASCADE, index) 
			: new MenuItem(parent, SWT.CASCADE);
		createMenu(mi);
		mi.setImage(DiagramUIPluginImages.get(DiagramUIPluginImages.IMG_ZOOM_IN));
		return mi;
	}

	/**
	 * Creates the zoom menu
	 * 
	 * @param mi
	 */
	private void createMenu(MenuItem mi) {
		Menu menu = new Menu(mi.getParent());
		createMenuItem(menu, ZOOM_IN_ACTION, ZOOM_IN,
			DiagramUIPluginImages.DESC_ZOOM_IN);
		createMenuItem(menu, ZOOM_OUT_ACTION, ZOOM_OUT,
			DiagramUIPluginImages.DESC_ZOOM_OUT);
		createMenuItem(menu, ZOOM_100_ACTION, ZOOM_100,
			DiagramUIPluginImages.DESC_ZOOM_100);
		createMenuItem(menu, ZOOM_FIT_ACTION, ZOOM_FIT,
			DiagramUIPluginImages.DESC_ZOOM_TOFIT);
		createMenuItem(menu, ZOOM_WIDTH_ACTION, ZOOM_WIDTH,
			DiagramUIPluginImages.DESC_ZOOM_TOFIT);
		createMenuItem(menu, ZOOM_HEIGHT_ACTION, ZOOM_HEIGHT,
			DiagramUIPluginImages.DESC_ZOOM_TOFIT);
		createMenuItem(menu, ZOOM_SELECTION_ACTION, ZOOM_SELECTION,
			DiagramUIPluginImages.DESC_ZOOM_TOFIT);
		mi.setMenu(menu);
	}

	/**
	 * Creates a menu item with a given text and image with the push style
	 * 
	 * @param menu The menu
	 * @param text The menu item text
	 * @param data The callback data to determine which zoom action to take
	 * @param imageDescriptor The menu item image
	 * @return mentu item
	 */
	 private MenuItem createMenuItem(
		Menu menu,
		String text,
		String data,
		ImageDescriptor imageDescriptor) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText(text);
		mi.setData(data);
		if (imageDescriptor != null)
			mi.setImage(createImage(imageDescriptor));
		mi.addListener(SWT.Selection, this);
		return mi;
	}

	/**
	 * Get the zoom levels as text string array from the zoom manager.
	 * 
	 * @return String array with zoom levels. 
	 * 
	 * @see org.eclipse.gef.editparts.ZoomManager#getZoomLevelsAsText()
	 */
	public String[] getZoomLevelsAsText() {
		int nNumericZoomLevels =
			(getZoomManager() != null)
				? getZoomManager().getZoomLevels().length
				: 0;
		String[] allZoomLevels = new String[nNumericZoomLevels + 6];

		allZoomLevels[0] = ZOOM_IN;
		allZoomLevels[1] = ZOOM_OUT;
		allZoomLevels[2] = ZOOM_FIT;
		allZoomLevels[3] = ZOOM_WIDTH;
		allZoomLevels[4] = ZOOM_HEIGHT;
		allZoomLevels[5] = ZOOM_SELECTION;

		if (getZoomManager() != null) {
			String[] numericZoomLevels = getZoomManager().getZoomLevelsAsText();
			for (int i = 0; i < getZoomManager().getZoomLevels().length; i++) {
				allZoomLevels[i + 6] = numericZoomLevels[i];
			}
		}

		return allZoomLevels;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#refreshItem()
	 */
	protected void refreshItem() {
		if (getZoomManager() != null) {
			if (getControl() != null) {
				Combo combo = (Combo) getControl();
				combo.setItems(getZoomLevelsAsText());
				String zoom = getZoomManager().getZoomAsText();
				int index = combo.indexOf(zoom);
				if (index != -1)
					combo.select(index);
				else
					combo.setText(zoom);
			}
		}
		super.refreshItem();
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		//Enter is commonly known as ascii 13 on all platforms.  To prevent
		//casting, this is the same as \r.
		if (event.type == SWT.KeyDown && event.character != '\r')
			return;
		if (getControl() != null)
			setZoomAsText(((Combo) getControl()).getText());
		else
			setZoomAsText((String) event.widget.getData());
	}

	/**
	 * Sets the zoom level to the zoom string using the zoom manager.
	 * First, it checks for the special cases.  If it isn't one of the special
	 * cases, it uses the zoom manager to do the zooming.
	 * There are six special cases, zoom to fit, zoom in, zoom out, zoom to width,
	 * zoom to height, and zoom to selected shapes.
	 * @param zoomText the zoom string which we will zoom to.
	 * @see org.eclipse.gef.editparts.ZoomManager#setZoomAsText(java.lang.String)
	 */
	public void setZoomAsText(String zoomText) {
		if (ZOOM_IN.equals(zoomText))
			getZoomManager().zoomIn();
		else if (ZOOM_OUT.equals(zoomText))
			getZoomManager().zoomOut();
		else if (ZOOM_100.equals(zoomText))
			getZoomManager().setZoom(1);
		else if (ZOOM_FIT.equals(zoomText))
			zoomToFit(true, true, false);
		else if (ZOOM_WIDTH.equals(zoomText))
			zoomToFit(true, false, false);
		else if (ZOOM_HEIGHT.equals(zoomText))
			zoomToFit(false, true, false);
		else if (ZOOM_SELECTION.equals(zoomText))
			zoomToFit(true, true, true);
		else
			getZoomManager().setZoomAsText(zoomText);
	}
	
	/**
	 * Performs the zoom operation.  Always zooms when possible.
	 *  
	 * @param onWidth true to perform zoom on the width
	 * @param onHeight true to perform zoom on the height
	 * @param selectionOnly true to only zoom the selected items, false to zoom
	 * the entire diagram
	 */
	protected void zoomToFit(boolean onWidth,
			boolean onHeight,
			boolean selectionOnly) {
		zoomToFit(onWidth, onHeight, selectionOnly, false);
	}

	/**
	 * Performs the zoom operation.
	 *  
	 * @param onWidth true to perform zoom on the width
	 * @param onHeight true to perform zoom on the height
	 * @param selectionOnly true to only zoom the selected items, false to zoom
	 * the entire diagram
	 * @param zoomOutOnly true to only zoom out and avoid zooming to greater
	 * than 100%, false to always zoom even if it could make the shapes on the
	 * diagram very large
	 */
	protected void zoomToFit(
		boolean onWidth,
		boolean onHeight,
		boolean selectionOnly,
		boolean zoomOutOnly) {

			Iterator editParts;
			if (selectionOnly) {
				editParts = getStructuredSelection().iterator();
			} else {
				List allEditParts = getDiagramEditPart().getConnections();
				allEditParts.addAll(getDiagramEditPart().getChildrenAffectingZoom());
				editParts = allEditParts.iterator();
			}

			Rectangle rectangle = null;
			while (editParts.hasNext()) {
				IFigure f = ((GraphicalEditPart) editParts.next()).getFigure();
				rectangle = rectangle == null ? f.getBounds().getCopy() : rectangle.getUnion(f.getBounds());
			}

			// IF nothing to Zoom...
			if( rectangle == null ) {
				// do nothing
				return;
			}

			// Translate the region into pixels
			MapModeUtil.getMapMode(getDiagramEditPart().getFigure()).LPtoDP(rectangle);
			
			Viewport viewport = getZoomManager().getViewport();

			float xratio =
				viewport.getHorizontalRangeModel().getExtent()
					/ (float) rectangle.width;
			float yratio =
				viewport.getVerticalRangeModel().getExtent()
					/ (float) rectangle.height;

			double zoom = 1.0;
			if (onHeight && onWidth) {
				zoom =
					(yratio < xratio)
						? Math.floor(yratio * 100)
						: Math.floor(xratio * 100);
			} else if (onWidth) {
				zoom = Math.round(xratio * 100);
			} else if (onHeight) {
				zoom = Math.round(yratio * 100);
			}
			
			if (zoomOutOnly && zoom >= 100) {
				//we should always continue in order to set the viewport
				//location
				zoom = 100;
			}

			// apply thresholds
			zoom =
				Math.min(
					(int) (zoomManager.getMaxZoom() * 100),
					Math.max((int) (zoomManager.getMinZoom() * 100), zoom));

			int viewX =
				Math.round(rectangle.getTopLeft().x * (float) zoom / 100.0f);
			int viewY =
				Math.round(rectangle.getTopLeft().y * (float) zoom / 100.0f);

			getZoomManager().setZoom(zoom / 100);
			viewport.setHorizontalLocation(viewX);
			viewport.setVerticalLocation(viewY);
			
			// always refresh the zoom text when zoom to fit, 
			// required when the zoom percentage did not change but we wish
			// to display the text as a percentage.
			update();
	}

	/**
	 * Creates an image and caches it
	 * @param descriptor
	 * @return image
	 */
	private Image createImage(ImageDescriptor descriptor) {
		int index = zoomImages.indexOf(descriptor);
		if (index != -1)
			return (Image) zoomImages.get(index);
		Image image = descriptor.createImage();
		zoomImages.add(image);
		return image;
	}

}
