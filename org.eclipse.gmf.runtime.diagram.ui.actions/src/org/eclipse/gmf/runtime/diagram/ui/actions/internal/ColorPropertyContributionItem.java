/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ColorPropertyContributionItem
	extends PropertyChangeContributionItem
	implements Listener {

	/**
	 * An image descriptor that overlays two images: a basic icon
	 * and a thin color bar underneath it
	 */
	private static class ColorMenuImageDescriptor
		extends CompositeImageDescriptor {
		/** the basic icon */
		private ImageData basicImgData;
		/** the color of the thin color bar */
		private RGB rgb;

		/**
		 * Creates a new color menu image descriptor
		 * @param basicIcon The basic Image data
		 * @param rgb The color bar RGB value
		 */
		public ColorMenuImageDescriptor(ImageData basicImgData, RGB rgb) {
			this.basicImgData = basicImgData;
			this.rgb = rgb;
		}

		/**
		* @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
		*/
		protected void drawCompositeImage(int width, int height) {
			// draw the base image
			drawImage(basicImgData, 0, 0);

			// draw the thin color bar underneath
			if (rgb != null) {
				ImageData colorBar =
					new ImageData(14, 4, 1, new PaletteData(new RGB[] { rgb }));
				drawImage(colorBar, 1, 13);
			}
		}

		/**
		 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
		 */
		protected Point getSize() {
			return ICON_SIZE;
		}
	}

	/**
	 * An image descriptor that creates a box with a given color
	 */
	private static class ColorBoxImageDescriptor extends ImageDescriptor {
		/** the color value in RGB scheme */
		private RGB rgb;

		/**
		 * Creates a new instance of the color box image descriptor with
		 * a given color
		 * 
		 * @param rgb The color value in RGB scheme
		 */
		public ColorBoxImageDescriptor(RGB rgb) {
			this.rgb = rgb;
		}

		/**
		* @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
		*/
		public ImageData getImageData() {
			ImageData data =
				new ImageData(
					ICON_SIZE.x,
					ICON_SIZE.y,
					1,
					new PaletteData(new RGB[] { rgb, OUTLINE_COLOR }));

			for (int i = 0; i < ICON_SIZE.y; i++)
				data.setPixel(0, i, 1);
			for (int i = 0; i < ICON_SIZE.y; i++)
				data.setPixel(ICON_SIZE.x - 1, i, 1);
			for (int i = 0; i < ICON_SIZE.x; i++)
				data.setPixel(i, 0, 1);
			for (int i = 0; i < ICON_SIZE.x; i++)
				data.setPixel(i, ICON_SIZE.y - 1, 1);
			return data;
		}
	}

	/**
	 * A descirptor for an inventory color
	 */
	private static class InventoryColorDescriptor {
		public RGB colorValue;
		public String colorName;

		public InventoryColorDescriptor(RGB colorValue, String colorName) {
			this.colorValue = colorValue;
			this.colorName = colorName;
		}		
	}

	/** inventory colors */
	private static final InventoryColorDescriptor WHITE = new InventoryColorDescriptor(new RGB(255, 255, 255), getResources().getString("ColorPropertyChangeAction.white")); //$NON-NLS-1$
	private static final InventoryColorDescriptor BLACK = new InventoryColorDescriptor(new RGB(0, 0, 0), getResources().getString("ColorPropertyChangeAction.black")); //$NON-NLS-1$
	private static final InventoryColorDescriptor LIGHT_GRAY = new InventoryColorDescriptor(new RGB(192, 192, 192), getResources().getString("ColorPropertyChangeAction.lightGray")); //$NON-NLS-1$
	private static final InventoryColorDescriptor GRAY = new InventoryColorDescriptor(new RGB(128, 128, 128), getResources().getString("ColorPropertyChangeAction.gray")); //$NON-NLS-1$
	private static final InventoryColorDescriptor DARK_GRAY = new InventoryColorDescriptor(new RGB(64, 64, 64), getResources().getString("ColorPropertyChangeAction.darkGray")); //$NON-NLS-1$
	private static final InventoryColorDescriptor RED = new InventoryColorDescriptor(new RGB(227, 164, 156), getResources().getString("ColorPropertyChangeAction.red")); //$NON-NLS-1$
	private static final InventoryColorDescriptor GREEN = new InventoryColorDescriptor(new RGB(166, 193, 152), getResources().getString("ColorPropertyChangeAction.green")); //$NON-NLS-1$
	private static final InventoryColorDescriptor BLUE = new InventoryColorDescriptor(new RGB(152, 168, 191), getResources().getString("ColorPropertyChangeAction.blue")); //$NON-NLS-1$
	private static final InventoryColorDescriptor YELLOW = new InventoryColorDescriptor(new RGB(225, 225, 135), getResources().getString("ColorPropertyChangeAction.yellow")); //$NON-NLS-1$
	private static final InventoryColorDescriptor PURPLE = new InventoryColorDescriptor(new RGB(184, 151, 192), getResources().getString("ColorPropertyChangeAction.magenta")); //$NON-NLS-1$
	private static final InventoryColorDescriptor TEAL = new InventoryColorDescriptor(new RGB(155, 199, 204), getResources().getString("ColorPropertyChangeAction.cyan")); //$NON-NLS-1$
	private static final InventoryColorDescriptor PINK = new InventoryColorDescriptor(new RGB(228, 179, 229), getResources().getString("ColorPropertyChangeAction.pink")); //$NON-NLS-1$
	private static final InventoryColorDescriptor ORANGE = new InventoryColorDescriptor(new RGB(237, 201, 122), getResources().getString("ColorPropertyChangeAction.orange")); //$NON-NLS-1$
	/** default color icon width */
	private static final Point ICON_SIZE = new Point(16, 16);
	/** custom color group maximum length */
	private static final int CUSTOM_SIZE = 3;
	/** the default preference color */
	private static final RGB DEFAULT_PREF_COLOR = new RGB(0, 0, 0);
	/** the default preference color */
	private static final RGB OUTLINE_COLOR = new RGB(192, 192, 192);
	/** a color value that indicates the default color */
	private static final String DEFAULT = "Default"; //$NON-NLS-1$
	/** a color value that indicates to browse for a color */
	private static final String CHOOSE = "Choose"; //$NON-NLS-1$
	/** a color value that indicates to browse for a color */
	private static final String CLEAR = "Clear"; //$NON-NLS-1$
	/** the preference id */
	private String preferenceId;
	/** the basic image data */
	private ImageData basicImageData;
	/** the disabledbasic image data */
	private ImageData disabledBasicImageData;
	/** the disabled basic image data **/
	private Image disabledBasicImage;
	/** the overlayed image */
	private Image overlyedImage;
	/** the last selected color */
	private Integer lastColor;
	/** the custom color list */
	private List customColors = new ArrayList();
	/** the inventory color list */
	private List inventoryColors = new ArrayList();
	/** the inventory color list  key: anRGB, value: anImage */
	private HashMap imageColorMap = new HashMap();
	/** the drop down menu */
	private Menu menu;

	/**
	 * Creates a new color property contribution item
	 * 
	 * @param workbenchPage The part service
	 * @param id The item id
	 * @param propertyName The color property name (externalizable)
	 * @param propertyId The color property id
	 * @param preferenceId The id of color property in pref store (optionl)
	 * @param toolTipText The tool tip text
	 * @param basicImageData The basic image data
	 */
	public ColorPropertyContributionItem(
		IWorkbenchPage workbenchPage,
		String id,
		String propertyId,
		String propertyName,
		String preferenceId,
		String toolTipText,
		ImageData basicImageData,
		ImageData disabledBasicImageData) {
		super(workbenchPage, id, propertyId, propertyName);
		Assert.isNotNull(toolTipText);
		Assert.isNotNull(basicImageData);
		this.preferenceId = preferenceId;
		this.basicImageData = basicImageData;
		this.disabledBasicImageData = disabledBasicImageData;
		setLabel(toolTipText);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#init()
	 */
	protected void init() {
		super.init();
		this.overlyedImage =
			new ColorMenuImageDescriptor(getBasicImageData(), null).createImage();
		this.disabledBasicImage = new ColorMenuImageDescriptor(this.disabledBasicImageData, null).createImage();
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#dispose()
	 */
	public void dispose() {
		if (overlyedImage != null && !overlyedImage.isDisposed()){
			overlyedImage.dispose();
			overlyedImage = null;
		}
		if (menu != null && !menu.isDisposed()){
			menu.dispose();
			menu = null;
		}
		for (Iterator i = imageColorMap.values().iterator(); i.hasNext();) {
			Image image = (Image) i.next();
			if (! image.isDisposed()) {
				image.dispose();
			}
		}
		if (disabledBasicImage != null && !disabledBasicImage.isDisposed()){
			disabledBasicImage.dispose();
			disabledBasicImage = null;
		}
		imageColorMap = new HashMap();
		
		super.dispose();
	}

	/**
	 * Render the UI as a
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#createToolItem(org.eclipse.swt.widgets.ToolBar, int)
	 */
	protected ToolItem createToolItem(ToolBar parent, int index) {
		ToolItem ti = new ToolItem(parent, SWT.DROP_DOWN, index);
		ti.addListener(SWT.Selection, getItemListener());
		ti.setImage(overlyedImage);
		ti.setDisabledImage(this.disabledBasicImage);
		return ti;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#createMenuItem(org.eclipse.swt.widgets.Menu, int)
	 */
	protected MenuItem createMenuItem(Menu parent, int index) {
		MenuItem mi = index >= 0 
			? new MenuItem(parent, SWT.CASCADE, index) 
			: new MenuItem(parent, SWT.CASCADE);
		createMenu(mi);
		mi.setImage(overlyedImage);
		return mi;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#handleWidgetEvent(org.eclipse.swt.widgets.Event)
	 */
	protected void handleWidgetEvent(Event e) {
		switch (e.type) {
			case SWT.Selection :
				handleWidgetSelection(e);
				break;
			default :
				super.handleWidgetEvent(e);
		}
	}

	/**
	 * Handles a widget selection event.
	 */
	private void handleWidgetSelection(Event e) {
		if (e.detail == 4) { // on drop-down button
			createMenu(getItem());
		} else { // on icon button
			if (lastColor != null)
				runWithEvent(e);
		}
	}

	/**
	 * Creates the color menu
	 */
	private void createMenu(Item item) {
		if (menu != null && !menu.isDisposed())
			menu.dispose();

		if (item instanceof ToolItem) {
			ToolItem toolItem = (ToolItem) item;
			menu = new Menu(toolItem.getParent());
			Rectangle b = toolItem.getBounds();
			Point p =
				toolItem.getParent().toDisplay(new Point(b.x, b.y + b.height));
			menu.setLocation(p.x, p.y); // waiting for SWT 0.42
			menu.setVisible(true);
		} else if (item instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) item;
			menu = new Menu(menuItem.getParent());
			menuItem.setMenu(menu);
		}

		Assert.isNotNull(menu, "falid to create menu"); //$NON-NLS-1$
		buildMenu(menu);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeContributionItem#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return lastColor;
	}

	/**
	 * Builds the color menu consisting of :
	 * inventory colors, custom colors, default and color picker
	 * 
	 * @param theMenu The menu
	 */
	private void buildMenu(Menu theMenu) {
		// inventory colors
		createInventoryColorMenuItem(theMenu, WHITE);
		createInventoryColorMenuItem(theMenu, BLACK);
		createInventoryColorMenuItem(theMenu, LIGHT_GRAY);
		createInventoryColorMenuItem(theMenu, GRAY);
		createInventoryColorMenuItem(theMenu, DARK_GRAY);
		createInventoryColorMenuItem(theMenu, RED);
		createInventoryColorMenuItem(theMenu, GREEN);
		createInventoryColorMenuItem(theMenu, BLUE);
		createInventoryColorMenuItem(theMenu, YELLOW);
		createInventoryColorMenuItem(theMenu, PURPLE);
		createInventoryColorMenuItem(theMenu, TEAL);
		createInventoryColorMenuItem(theMenu, PINK);
		createInventoryColorMenuItem(theMenu, ORANGE);

		// history colors
		if (!customColors.isEmpty()) {
			createMenuSeparator(theMenu);
			Iterator iter = customColors.iterator();
			while (iter.hasNext()) {
				RGB rgb = (RGB) iter.next();
				createColorMenuItem(theMenu, rgb);
			}
			createClearCustomColorMenuItem(theMenu);
		}

		// default color and color picker
		createMenuSeparator(theMenu);
		createDefaultColorMenuItem(theMenu);
		createChooseColorMenuItem(theMenu);
	}

	/**
	 * Creates a new menu separator
	 * 
	 * @param theMenu The menu
	 */
	private void createMenuSeparator(Menu theMenu) {
		new MenuItem(theMenu, SWT.SEPARATOR);
	}

	/**
	 * Creates a inventory color menu item with the given color name and RGB value
	 * 
	 * @param theMenu The menu
	 * @param color The color RGB value
	 * @param colorName the color name (externalizable)
	 */
	private void createInventoryColorMenuItem(
		Menu theMenu,
		InventoryColorDescriptor color) {
		
		RGB rgb = color.colorValue;
		Image image = (Image) imageColorMap.get(rgb);		
		if (image == null){
			image = new ColorBoxImageDescriptor(color.colorValue).createImage();
			imageColorMap.put(rgb, image);
		}		
		MenuItem mi = createMenuItem(theMenu, color.colorName, image);
		mi.setData(rgb);
		inventoryColors.add(rgb);		
	}

	/**
	 * Creates a color menu item with the RGB value as a name
	 * 
	 * @param theMenu The menu
	 * @param rgb The color RGB value
	 */
	private void createColorMenuItem(Menu theMenu, RGB rgb) {
		Image image = (Image) imageColorMap.get(rgb);		
		if (image == null){
			image = new ColorBoxImageDescriptor(rgb).createImage();
			imageColorMap.put(rgb, image);
		}
		MenuItem mi = createMenuItem(theMenu, rgb.toString(), image);
		mi.setData(rgb);		
	}

	/**
	 * Creates a menu item for the default color
	 * 
	 * @param theMenu The menu
	 */
	private void createDefaultColorMenuItem(Menu theMenu) {
		String text = getResources().getString("ColorPropertyChangeAction.default"); //$NON-NLS-1$
		Image image = null; //new ColorBoxImageDescriptor(color).createImage();
		MenuItem mi = createMenuItem(theMenu, text, image);
		mi.setData(DEFAULT);
	}

	/**
	 * Creates a menu item for the color picker
	 * 
	 * @param theMenu The menu
	 */
	private void createChooseColorMenuItem(Menu theMenu) {
		String text = getResources().getString("ColorPropertyChangeAction.moreColors"); //$NON-NLS-1$
		Image image = null; //new ColorBoxImageDescriptor(color).createImage();
		MenuItem mi = createMenuItem(theMenu, text, image);
		mi.setData(CHOOSE);
	}

	/**
	 * Creates a menu item to clear the custom color menu group
	 * 
	 * @param theMenu The menu
	 */
	private void createClearCustomColorMenuItem(Menu theMenu) {
		String text = getResources().getString("ColorPropertyChangeAction.clearColors"); //$NON-NLS-1$
		Image image = null; //new ColorBoxImageDescriptor(color).createImage();
		MenuItem mi = createMenuItem(theMenu, text, image);
		mi.setData(CLEAR);
	}

	/**
	 * Creates a menu item with a given text and image with the push style
	 * 
	 * @param theMenu The menu
	 * @param text The menu item text
	 * @param image The menu item image
	 */
	private MenuItem createMenuItem(Menu theMenu, String text, Image image) {
		MenuItem mi = new MenuItem(theMenu, SWT.PUSH);
		if (text != null)
			mi.setText(text);
		if (image != null)
			mi.setImage(image);
		mi.addListener(SWT.Selection, this);
		return mi;
	}

	/**
	 * Handle the selection of menu items in the color menu
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		MenuItem menuItem = (MenuItem) event.widget;
		Object data = menuItem.getData();

		RGB rgb = null;

		if (data instanceof RGB) {
			rgb = (RGB) data;
		} else if (data.equals(CHOOSE)) {
			rgb = getBrowseColor();
		} else if (data.equals(DEFAULT)) {
			rgb = getDefaultColor();
		} else if (data.equals(CLEAR)) {
			customColors.clear();
		}

		if (rgb != null) {
			if (getToolItem() != null) {
				// if a new custom color add it to history
				if (!customColors.contains(rgb)
					&& !inventoryColors.contains(rgb)) {
					if (customColors.size() == CUSTOM_SIZE)
						customColors.remove(0);
					customColors.add(rgb);
				}

				// create a new overlayed icon with the new color
				if (overlyedImage != null)
					overlyedImage.dispose();
				overlyedImage =
					new ColorMenuImageDescriptor(
						getBasicImageData(),
						rgb)
						.createImage();
				getItem().setImage(overlyedImage);
			}

			// set the new color as the last color
			lastColor = FigureUtilities.RGBToInteger(rgb);

			// run the action
			runWithEvent(event);
		}
	}

	/**
	 * Returns the color to use in the browse mode. By default,
	 * this comes from the color picker dialog
	 * 
	 * @return The color to use in browse mode
	 */
	protected RGB getBrowseColor() {
		ColorDialog dialog =
			new ColorDialog(Display.getCurrent().getActiveShell());
		WindowUtil.centerDialog(
			dialog.getParent(),
			Display.getCurrent().getActiveShell());
		dialog.open();
		return dialog.getRGB();
	}

	/**
	 * Returns the color to use in the default mode. By default,
	 * this comes from the preference store
	 * 
	 * @return The color to use in default mode
	 */
	protected RGB getDefaultColor() {
		IPreferenceStore store = (IPreferenceStore) getDiagramEditPart()
			.getDiagramPreferencesHint().getPreferenceStore();
		RGB color = null;
		if (preferenceId != null)
			color = PreferenceConverter.getColor(store, preferenceId);
		return color != null ? color : DEFAULT_PREF_COLOR;
	}
	
	/**
	 * Gets the basic image data.
	 * @return ImageData basicImageData
	 */
	protected ImageData getBasicImageData(){
		return this.basicImageData;
	}

	/**
	 * Create the FONT color menu
	 * 
	 * @param workbenchPage The part service
	 * @return The FONT color menu
	 */
	public static ColorPropertyContributionItem createFontColorContributionItem(IWorkbenchPage workbenchPage) {
		String propertyName = getResources().getString("PropertyDescriptorFactory.FontColor"); //$NON-NLS-1$
		String toolTipText = getResources().getString("ColorChangeActionMenu.fontColor"); //$NON-NLS-1$
		ImageData basicImageData = DiagramActionsResourceManager.getInstance()
			.getImageDescriptor(DiagramActionsResourceManager.IMAGE_FONT_COLOR)
			.getImageData();
		ImageData disabledBasicImageData = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_FONT_COLOR_DISABLED)
			.getImageData();

		return new ColorPropertyContributionItem(
			workbenchPage,
			ActionIds.CUSTOM_FONT_COLOR,
			Properties.ID_FONTCOLOR,
			propertyName,
			null,
			toolTipText,
			basicImageData,
			disabledBasicImageData);
	}

	/**
	 * Create the LINE color menu
	 * 
	 * @param workbenchPage The part service
	 * @return The LINE color menu
	 */
	public static ColorPropertyContributionItem createLineColorContributionItem(IWorkbenchPage workbenchPage) {
		String propertyName = getResources().getString("PropertyDescriptorFactory.LineColor"); //$NON-NLS-1$
		String toolTipText = getResources().getString("ColorChangeActionMenu.lineColor"); //$NON-NLS-1$
		ImageData basicImageData = DiagramActionsResourceManager.getInstance()
			.getImageDescriptor(DiagramActionsResourceManager.IMAGE_LINE_COLOR)
			.getImageData();
		ImageData disabledBasicImageData = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_LINE_COLOR_DISABLED)
			.getImageData();

		return new ColorPropertyContributionItem(
			workbenchPage,
			ActionIds.CUSTOM_LINE_COLOR,
			Properties.ID_LINECOLOR,
			propertyName,
			IPreferenceConstants.PREF_LINE_COLOR,
			toolTipText,
			basicImageData,
			disabledBasicImageData);
	}

	/**
	 * Create the FILL color menu
	 * 
	 * @param workbenchPage The part service
	 * @return The FILL color menu
	 */
	public static ColorPropertyContributionItem createFillColorContributionItem(IWorkbenchPage workbenchPage) {
		String propertyName = getResources().getString("PropertyDescriptorFactory.FillColor"); //$NON-NLS-1$
		String toolTipText = getResources().getString("ColorChangeActionMenu.fillColor"); //$NON-NLS-1$
		ImageData basicImageData = DiagramActionsResourceManager.getInstance()
			.getImageDescriptor(DiagramActionsResourceManager.IMAGE_FILL_COLOR)
			.getImageData();
		ImageData disabledBasicImageData = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_FILL_COLOR_DISABLED)
			.getImageData();

		return new ColorPropertyContributionItem(
			workbenchPage,
			ActionIds.CUSTOM_FILL_COLOR,
			Properties.ID_FILLCOLOR,
			propertyName,
			IPreferenceConstants.PREF_FILL_COLOR,
			toolTipText,
			basicImageData,
			disabledBasicImageData);
	}

	/**
	 * @return a short cut to the resources
	 */
	private static AbstractUIResourceManager getResources() {
		return DiagramActionsResourceManager.getInstance();
	}

}
