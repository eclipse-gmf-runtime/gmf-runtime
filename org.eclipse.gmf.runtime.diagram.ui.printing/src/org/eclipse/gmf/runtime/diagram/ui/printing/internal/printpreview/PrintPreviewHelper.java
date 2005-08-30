/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper.PageMargins;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingPlugin;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramPrintingResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.HeaderAndFooterHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.PrintHelper;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Print Preview Action to display the Print Preview dialog. There are no static
 * methods, so you must create an instance of this class.
 * 
 * Call doPrintPreview() after you've made an instance.
 * 
 * @author Wayne Diu, wdiu
 */
public class PrintPreviewHelper {

	/**
	 * Action helper for print. This must be passed in to have something happen
	 * when print is pressed
	 */
	protected IPrintActionHelper printActionHelper;

	/**
	 * Increment userX everytime the user moves right, decrement userX everytime
	 * the user moves left.
	 * 
	 * userX >= 0
	 */
	protected int userX = 0;

	/**
	 * Increment userY everytime the user moves down, decrement userY everytime
	 * the user moves up.
	 * 
	 * userY >= 0
	 */
	protected int userY = 0;

	/**
	 * Number of rows, initialized with initial number of rows of pages to
	 * display
	 */
	protected int numberOfRowsToDisplay = 2;

	/**
	 * Number of columns, initialized with initial number of columns of pages to
	 * display
	 */
	protected int numberOfColumnsToDisplay = 2;

	/**
	 * Total number of rows taken required for the diagram
	 */
	protected int totalNumberOfRows = -1;

	/**
	 * Total number of columns required for the diagram
	 */
	protected int totalNumberOfColumns = -1;

	/**
	 * The diagram edit part
	 */
	protected DiagramEditPart diagramEditPart;

	/**
	 * Max bounds of a page for no page break
	 */
	protected Rectangle pageBreakBounds;

	/* SWT interface variables */

	/**
	 * Body of the shell.
	 */
	protected Composite body;

	/**
	 * Composite for the pages
	 */
	protected Composite composite;

	/**
	 * Height of the button bar, initialized right before the button bar is
	 * created.
	 */
	protected int buttonBarHeight;

	/**
	 * Shell for the new pop up
	 */
	protected Shell shell;

	/* Toolbar items are in left to right order */

	/**
	 * Print item on toolbar
	 */
	protected ToolItem printTool;

	/**
	 * Pages item on toolbar
	 */
	protected ToolItem pagesTool;

	/**
	 * Left item on toolbar
	 */
	protected ToolItem leftTool;

	/**
	 * Right item on toolbar
	 */
	protected ToolItem rightTool;

	/**
	 * Up item on toolbar
	 */
	protected ToolItem upTool;

	/**
	 * Down item on toolbar
	 */
	protected ToolItem downTool;

	/**
	 * Close item on toolbar
	 */
	protected ToolItem closeTool;

	/**
	 * It's easiest to keep track of the page images using an image list, but I
	 * could also have done getImage on the labels
	 */
	private List imageList = new ArrayList();

	/**
	 * Border size
	 */
	protected static final int BORDER_SIZE = 20;

	/**
	 * the background color
	 */
	private static final Color BACKGROUND_COLOR = new Color(Display
		.getDefault(), 124, 124, 124);

	/* Images */
	/**
	 * Enabled print image
	 */
	protected Image printImage;

	/**
	 * Disabled Print image
	 */
	protected Image disabledPrintImage;

	/**
	 * Page image, unlikely to ever be disabled
	 */
	protected Image pageImage;

	/**
	 * Enabled left image
	 */
	protected Image leftImage;

	/**
	 * Disabled left image
	 */
	protected Image disabledLeftImage;

	/**
	 * Enabled right image
	 */
	protected Image rightImage;

	/**
	 * Disabled right image
	 */
	protected Image disabledRightImage;

	/**
	 * Enabled up image
	 */
	protected Image upImage;

	/**
	 * Diabled up image
	 */
	protected Image disabledUpImage;

	/**
	 * Enabled down image
	 */
	protected Image downImage;

	/**
	 * Disabled down image
	 */
	protected Image disabledDownImage;

	/**
	 * Close image, unlikely to ever be disabled
	 */
	protected Image closeImage;

	/**
	 * Initialize all toolbar images
	 */
	protected void initializeToolbarImages() {
		printImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_print.gif"); //$NON-NLS-1$

		disabledPrintImage = DiagramPrintingResourceManager.getInstance()
			.createImage("dtool16/print_preview_print.gif"); //$NON-NLS-1$

		pageImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_pages.gif"); //$NON-NLS-1$

		leftImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_left.gif"); //$NON-NLS-1$

		disabledLeftImage = DiagramPrintingResourceManager.getInstance()
			.createImage("dtool16/print_preview_left.gif"); //$NON-NLS-1$

		rightImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_right.gif"); //$NON-NLS-1$

		disabledRightImage = DiagramPrintingResourceManager.getInstance()
			.createImage("dtool16/print_preview_right.gif"); //$NON-NLS-1$

		upImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_up.gif"); //$NON-NLS-1$

		disabledUpImage = DiagramPrintingResourceManager.getInstance()
			.createImage("dtool16/print_preview_up.gif"); //$NON-NLS-1$

		downImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_down.gif"); //$NON-NLS-1$

		disabledDownImage = DiagramPrintingResourceManager.getInstance()
			.createImage("dtool16/print_preview_down.gif"); //$NON-NLS-1$

		closeImage = DiagramPrintingResourceManager.getInstance().createImage(
			"etool16/print_preview_close.gif"); //$NON-NLS-1$
	}

	/**
	 * Do the print preview.
	 * 
	 * @param printActionHelper,
	 *            an IPrintActionHelper with a doPrint method that will be
	 *            called when the print button is pressed. I use this parameter
	 *            so that this class can be extensible, e.g.
	 *            ModelerPrintActionHelper will implement it for Modeler and
	 *            something else will implement it for a different editor.
	 */
	public void doPrintPreview(IPrintActionHelper prActionHelper) {
		this.printActionHelper = prActionHelper;

		if (getDiagramEditorPart() == null) {
			MessageDialog
				.openInformation(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					DiagramPrintingResourceManager
						.getI18NString("PrintPreview.Title"), //$NON-NLS-1$
					DiagramPrintingResourceManager
						.getI18NString("PrintPreview.NotEnabled")); //$NON-NLS-1$
			return;
		}

		if (!isPrinterInstalled()) {
			WindowUtil
				.doMessageBox(DiagramPrintingResourceManager
					.getI18NString("PrintPreview.NoPrinterInstalled"), //$NON-NLS-1$
					DiagramPrintingResourceManager
						.getI18NString("PrintPreview.Title"), //$NON-NLS-1$
					SWT.ICON_ERROR, PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());
			return;
		}

		initializeToolbarImages();

		diagramEditPart = null;
		pageBreakBounds = null;
		
		userX = 0;
		userY = 0;

		numberOfRowsToDisplay = 2;
		numberOfColumnsToDisplay = 2;

		totalNumberOfRows = -1;
		totalNumberOfColumns = -1;

		Display display = Display.getDefault();
		shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.TITLE
			| SWT.CLOSE | SWT.BORDER);

		shell.setSize(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getShell().getSize());
		shell.setText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.Title")); //$NON-NLS-1$
		shell.setLocation(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getShell().getLocation());
		shell.setLayout(new GridLayout(1, true));

		ToolBar bar = new ToolBar(shell, SWT.FLAT | SWT.HORIZONTAL);

		printTool = new ToolItem(bar, SWT.NULL);
		printTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.PrintToolItem")); //$NON-NLS-1$
		printTool.setImage(printImage);
		printTool.setDisabledImage(disabledPrintImage);
		printTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				//should not be enabled
				Assert.isNotNull(printActionHelper);
				printActionHelper
					.doPrint(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActivePart());
				shell.setActive();
				
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		if (printActionHelper == null) {
			printTool.setEnabled(false);
		}

		new ToolItem(bar, SWT.SEPARATOR);

		pagesTool = new ToolItem(bar, SWT.DROP_DOWN);
		pagesTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.PagesToolItem")); //$NON-NLS-1$
		pagesTool.setImage(pageImage);

		pagesTool.addSelectionListener(new SelectionAdapter() {

			private Menu menu = null;

			//also update userX, userY, numberOfRowsToDisplay,
			// numberOfColumnsToDisplay
			private void updatePreview(int newNumberOfColumnsToDisplay,
					int newNumberOfRowsToDisplay) {
				numberOfRowsToDisplay = newNumberOfRowsToDisplay;
				numberOfColumnsToDisplay = newNumberOfColumnsToDisplay;

				//When switching the number of pages to display to a bigger
				//number, you can get an extra blank page on the right or on
				//the bottom. This check prevents the extra blank page.

				if (userX + numberOfColumnsToDisplay > getTotalNumberOfColumns()) {
					//move it left
					userX = getTotalNumberOfColumns()
						- numberOfColumnsToDisplay;
					//be safe, check for 0
					if (userX < 0)
						userX = 0;
				}

				if (userY + numberOfRowsToDisplay > getTotalNumberOfRows()) {
					//move it up
					userY = getTotalNumberOfRows() - numberOfRowsToDisplay;
					//be safe, check for 0
					if (userY < 0)
						userY = 0;
				}

				updateLeftRightUpDownButtonsForToolbar();

				updateCompositeForNumberOfColumns(numberOfRowsToDisplay,
					numberOfColumnsToDisplay);

			}

			public void widgetSelected(SelectionEvent event) {
				// Create the menu if it has not already been created

				if (menu == null) {
					// Lazy create the menu.
					menu = new Menu(shell);
					MenuItem menuItem = new MenuItem(menu, SWT.NONE);
					menuItem.setText(DiagramPrintingResourceManager
						.getI18NString("PrintPreview.1Up")); //$NON-NLS-1$
					menuItem.addSelectionListener(new SelectionAdapter() {

						public void widgetSelected(SelectionEvent e) {
							updatePreview(1, 1);
						}
					});

					menuItem = new MenuItem(menu, SWT.NONE);
					menuItem.setText(DiagramPrintingResourceManager
						.getI18NString("PrintPreview.2Up")); //$NON-NLS-1$
					menuItem.addSelectionListener(new SelectionAdapter() {

						public void widgetSelected(SelectionEvent e) {
							updatePreview(2, 1);
						}
					});

					menuItem = new MenuItem(menu, SWT.NONE);
					menuItem.setText(DiagramPrintingResourceManager
						.getI18NString("PrintPreview.4Up")); //$NON-NLS-1$
					menuItem.addSelectionListener(new SelectionAdapter() {

						public void widgetSelected(SelectionEvent e) {
							updatePreview(2, 2);
						}
					});
				}

				final ToolItem toolItem = (ToolItem) event.widget;
				final ToolBar toolBar = toolItem.getParent();
				org.eclipse.swt.graphics.Rectangle toolItemBounds = toolItem
					.getBounds();
				//left aligned under the pages button
				Point point = toolBar.toDisplay(new Point(toolItemBounds.x,
					toolItemBounds.y));
				menu.setLocation(point.x, point.y + toolItemBounds.height);
				setMenuVisible(true);

			}

			private void setMenuVisible(boolean visible) {
				menu.setVisible(visible);
			}

		});

		new ToolItem(bar, SWT.SEPARATOR);

		leftTool = new ToolItem(bar, SWT.NULL);
		leftTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.LeftToolItem")); //$NON-NLS-1$
		leftTool.setImage(leftImage);
		leftTool.setDisabledImage(disabledLeftImage);
		leftTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (userX > 0) {
					userX--;
					updateCompositeForNumberOfColumns(numberOfRowsToDisplay,
						numberOfColumnsToDisplay);
					updateLeftRightUpDownButtonsForToolbar();
				}
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		rightTool = new ToolItem(bar, SWT.NULL);
		rightTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.RightToolItem")); //$NON-NLS-1$
		rightTool.setImage(rightImage);
		rightTool.setDisabledImage(disabledRightImage);
		rightTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				//check for max pages to be safe
				if (!(userX + numberOfColumnsToDisplay + 1 > getTotalNumberOfColumns())) {
					userX++;
					updateCompositeForNumberOfColumns(numberOfRowsToDisplay,
						numberOfColumnsToDisplay);
					updateLeftRightUpDownButtonsForToolbar();
				}
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		upTool = new ToolItem(bar, SWT.NULL);
		upTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.UpToolItem")); //$NON-NLS-1$
		upTool.setImage(upImage);
		upTool.setDisabledImage(disabledUpImage);
		upTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (userY > 0) {
					userY--;
					updateCompositeForNumberOfColumns(numberOfRowsToDisplay,
						numberOfColumnsToDisplay);
					updateLeftRightUpDownButtonsForToolbar();
				}
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		downTool = new ToolItem(bar, SWT.NULL);
		downTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.DownToolItem")); //$NON-NLS-1$
		downTool.setImage(downImage);
		downTool.setDisabledImage(disabledDownImage);
		downTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (!(userY + numberOfRowsToDisplay + 1 > getTotalNumberOfRows())) {
					userY++;
					updateCompositeForNumberOfColumns(numberOfRowsToDisplay,
						numberOfColumnsToDisplay);
					updateLeftRightUpDownButtonsForToolbar();
				}
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		new ToolItem(bar, SWT.SEPARATOR);
		closeTool = new ToolItem(bar, SWT.NULL);
		closeTool.setToolTipText(DiagramPrintingResourceManager
			.getI18NString("PrintPreview.CloseToolItem")); //$NON-NLS-1$
		closeTool.setImage(closeImage);
		closeTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				dispose();
				shell.close();
				shell.dispose();
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		buttonBarHeight = bar.getBounds().height - bar.getBounds().y;

		bar.setBounds(0, 0, shell.getSize().x, buttonBarHeight);

		//do the body in the middle
		body = new Composite(shell, SWT.NULL);
		body.setLayout(new GridLayout(1, true));
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		body.setBackground(BACKGROUND_COLOR);

		composite = new Composite(body, SWT.NULL);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		updateCompositeForNumberOfColumns(2, 2);

		updateLeftRightUpDownButtonsForToolbar();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		shell.dispose();

	}

	/**
	 * The constructor.
	 */
	public PrintPreviewHelper() {
		//
	}

	/**
	 * Return and cache the total number of rows used by the diagram
	 * 
	 * @return int total number of rows used by the diagram
	 */
	private int getTotalNumberOfRows() {
		if (totalNumberOfRows < 0) {
			float numRows = ((float) getBounds().height)
				/ PageInfoHelper
					.getPageSize(getPreferenceStore(), false).y;
			totalNumberOfRows = Math.max(1, (int) Math.ceil(numRows));
		}

		return totalNumberOfRows;
	}

	/**
	 * Return the diagram editor part that we are doing the print preview for
	 * 
	 * @return DiagramEditor, diagram editor part that we are doing the print
	 *         preview for. I do not return IDiagramWorkbenchPart because
	 *         DiagramEditorPart contains some extra methods for the page break
	 *         checks.
	 */
	private DiagramEditor getDiagramEditorPart() {
		//more explicit than using window
		IEditorPart editorPart = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (!(editorPart instanceof DiagramEditor)) {
			return null;
		}

		DiagramEditor diagramEditorPart = (DiagramEditor) editorPart;

		return diagramEditorPart;
	}

	/**
	 * Return the diagram bounds of the diagram we are doing the print preview
	 * for.
	 * 
	 * @return Rectangle with diagram bounds of the diagram we are doing the
	 *         print preview for.
	 */
	protected Rectangle getDiagramBounds() {
		return PageInfoHelper.getChildrenBounds(getDiagramEditPart(), null);
		//null because we are not ignoring anything
	}

	/**
	 * Return and cache the page break bounds of the diagram we are doing the
	 * print preview for.
	 * 
	 * @return Rectangle with page break bounds of the diagram we are doing the
	 *         print preview for.
	 */
	protected Rectangle getPageBreakBounds() {
		if (pageBreakBounds == null) {
			pageBreakBounds = PrintHelper.getPageBreakBounds(getDiagramEditPart(), true);
		}
		return pageBreakBounds;
	}

	/**
	 * Return the pageBreakBounds if possible. If not, return the diagram
	 * bounds.
	 * 
	 * @return Rectangle with page break bounds if possible. If not possible,
	 *         just return the diagram bounds.
	 */
	protected Rectangle getBounds() {
		//don't worry about storing it, it's cached
		return (getPageBreakBounds() == null) ? getDiagramBounds()
			: getPageBreakBounds();
	}

	/**
	 * Return the diagram edit part that we are doing the print preview for.
	 * Uses getDiagramEditorPart().
	 * 
	 * @return DiagramEditPart, diagram edit part that we are doing the print
	 *         preview for
	 */
	protected DiagramEditPart getDiagramEditPart() {
		if (diagramEditPart == null) {
			Diagram diagram = getDiagramEditorPart().getDiagram(); //do not getDiagramEditPart
			PreferencesHint preferencesHint = getPreferencesHint(getDiagramEditorPart());
			diagramEditPart = PrintHelper.createDiagramEditPart(diagram, preferencesHint);
			PrintHelper.initializePreferences(diagramEditPart, preferencesHint);
		}
		return diagramEditPart;
	}
	
	protected PreferencesHint getPreferencesHint(IEditorPart editorPart) {
		if (editorPart instanceof IDiagramWorkbenchPart) {
			RootEditPart rootEP = ((IDiagramWorkbenchPart) editorPart)
				.getDiagramGraphicalViewer().getRootEditPart();
			if (rootEP instanceof IDiagramPreferenceSupport) {
				return ((IDiagramPreferenceSupport) rootEP)
					.getPreferencesHint();
			}
		}
		return PreferencesHint.USE_DEFAULTS;
	}
	

	/**
	 * Return and cache the total number of columns used by the diagram
	 * 
	 * @return int total number of columns used by the diagram
	 */
	private int getTotalNumberOfColumns() {
		if (totalNumberOfColumns < 0) {
			float numCols = ((float) getBounds().width)
				/ PageInfoHelper
					.getPageSize(getPreferenceStore(), false).x;
			totalNumberOfColumns = Math.max(1, (int) Math.ceil(numCols));
		}

		return totalNumberOfColumns;
	}

	/**
	 * Make sure printer is installed. Should not be able to print preview if no
	 * printer is installed, even though technically it will work.
	 * 
	 * Call this immediately with the rest of the initialization.
	 */
	protected boolean isPrinterInstalled() {
		Printer printer = null;
		try {
			printer = new Printer();
		} catch (SWTError e) {
			//I cannot printer.dispose(); because it may not have been
			//initialized
			Trace.catching(DiagramPrintingPlugin.getInstance(),
				DiagramPrintingDebugOptions.EXCEPTIONS_CATCHING,
				PrintPreviewHelper.class, "isPrinterInstalled", //$NON-NLS-1$
				e);

			if (e.code == SWT.ERROR_NO_HANDLES) {
				//it might have really been ERROR_NO_HANDLES, but there's
				//no way for me to really know
				return false;
			}

			//if (e.code != SWT.ERROR_NO_HANDLES)
			Log.error(DiagramPrintingPlugin.getInstance(),
				DiagramPrintingStatusCodes.GENERAL_UI_FAILURE,
				"Failed to make instance of Printer object", e); //$NON-NLS-1$

			//else if another swt error
			Trace.throwing(DiagramPrintingPlugin.getInstance(),
				DiagramPrintingDebugOptions.EXCEPTIONS_CATCHING,
				PrintPreviewHelper.class, "isPrinterInstalled", //$NON-NLS-1$
				e);
			throw e;
		}

		printer.dispose();

		return true;
	}

	/**
	 * Clean up by deleting the images in the image list
	 */
	private void disposeImages() {
		while (imageList.size() > 0) {
			Assert.isTrue(imageList.get(0) instanceof Image);
			if (!((Image) imageList.get(0)).isDisposed())
				((Image) imageList.get(0)).dispose();
			imageList.remove(0);
		}
	}

	/**
	 * Draw the composite centered on the body based on the number of columns.
	 * Also calls the method to make the images and insert them into the
	 * composite.
	 * 
	 * @param numberOfRows
	 *            the number of rows that the composite should contain. I need
	 *            this to figure out the height of the image.
	 * @param numberOfColumns
	 *            the number of columns that the composite should contain. I
	 *            need this to figure out the width of the image.
	 */
	private void updateCompositeForNumberOfColumns(int numberOfRows,
			int numberOfColumns) {
		Assert.isNotNull(shell);
		Assert.isNotNull(composite);

		WindowUtil.disposeChildren(composite);
		disposeImages();

		//the next two lines of code are intentional
		composite.setLayout(null);
		composite.pack();

		composite.setLayout(new GridLayout(numberOfColumns, true));

		//(shell height - toolbar height - top border - bottom border - title -
		// ((# of rows - 1) x vertical border between images)) / # of rows
		int imageHeight = (shell.getSize().y - buttonBarHeight - BORDER_SIZE
			- BORDER_SIZE - BORDER_SIZE - ((numberOfRows - 1) * BORDER_SIZE))
			/ numberOfRows;

		//(shell width - left border - right border - ((# of columns - 1) x
		// horizontal border between images)) / # of columns
		int imageWidth = (shell.getSize().x - BORDER_SIZE - BORDER_SIZE - ((numberOfColumns - 1) * BORDER_SIZE))
			/ numberOfColumns;

		//now adjust to the limiting one based on aspect ratio

		//to make this conform to the page breaks, RATLC00247228
		//get printer ratio from the page, not the real printer

		org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper
			.getPageSize(getPreferenceStore(), false);
		Assert.isNotNull(pageSize);
		
		//width / height
		float printerRatio = ((float) pageSize.x) / ((float) pageSize.y);

		if (imageHeight * printerRatio < imageWidth) {
			//round down
			imageWidth = (int) (imageHeight * printerRatio);
		} else if (imageWidth * (1 / printerRatio) < imageHeight) {
			//round down
			imageHeight = (int) (imageWidth * (1.0f / printerRatio));
		}
		
		PageMargins margins = PageInfoHelper.getPageMargins(getPreferenceStore());

		//make sure height and width are not 0, if too small <4, don't bother
		if (!(imageHeight <= 4 || imageWidth <= 4)) {

			//or imageWidth / pageSize.x
			float scale = ((float) imageHeight / (float) pageSize.y)
				/ (float) MapMode.getScale();

			for (int i = 0; i < numberOfRows; i++) {
				for (int j = 0; j < numberOfColumns; j++) {
					Label label = new Label(composite, SWT.NULL);
					Image pageImg = makeImage(imageWidth, imageHeight, i, j,
						scale, margins);
					label.setImage(pageImg);
					imageList.add(pageImg);
				}
			}

		}

		composite.pack();

		//GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_CENTER
		// won't do it for you
		org.eclipse.swt.graphics.Rectangle compositeBounds = composite
			.getBounds();

		//this approximation is OK
		compositeBounds.x = (shell.getSize().x - BORDER_SIZE - compositeBounds.width) / 2;
		compositeBounds.y = (shell.getSize().y - buttonBarHeight - BORDER_SIZE
			- BORDER_SIZE - BORDER_SIZE - compositeBounds.height) / 2;
		composite.setBounds(compositeBounds);
	}

	/**
	 * Update the enabled and disabled states for the toolbar
	 */
	protected void updateLeftRightUpDownButtonsForToolbar() {
		if (userX == 0) {
			leftTool.setEnabled(false);
		} else {
			leftTool.setEnabled(true);
		}

		//should be (user + 1) + (display - 1), the +1 and -1 can be taken out
		if (userX + numberOfColumnsToDisplay + 1 > getTotalNumberOfColumns()) {
			rightTool.setEnabled(false);
		} else {
			rightTool.setEnabled(true);
		}

		if (userY == 0) {
			upTool.setEnabled(false);
		} else {
			upTool.setEnabled(true);
		}

		if (userY + numberOfRowsToDisplay + 1 > getTotalNumberOfRows()) {
			downTool.setEnabled(false);
		} else {
			downTool.setEnabled(true);
		}
	}

	/**
	 * Makes the image for the location at row, col. row and col start from 0.
	 * The image will have a size of imageWidth x imageHeight.
	 * 
	 * @param imageWidth
	 *            int of pixels of width of image
	 * @param imageWidth
	 *            int of pixels of height of image
	 * @param row
	 *            int of row to make image at, starting at 0
	 * @param col
	 *            int of column to make image at, starting at 0
	 * @return Image of size imageWidth * imageHeight
	 */
	protected Image makeImage(int imageWidth, int imageHeight, int row,
			int col, float scale, PageMargins margins) {

		Image image = new Image(Display.getDefault(), imageWidth, imageHeight);

		GC gc = new GC(image);

		SWTGraphics sg = new SWTGraphics(gc);
		//for scaling
		ScaledGraphics g1 = new ScaledGraphics(sg);
	
		//for himetrics and svg
		MapModeGraphics mmg = createMapModeGraphics(g1);
		
		//if mmg's font is null, gc.setFont will use a default font
		gc.setFont(mmg.getFont());
		drawPage(mmg, gc, scale, row, col, margins);

		gc.dispose();

		return image;

	}
	
	/**
	 * Convenience method to determine if a page at row y and
	 * column x exists.
	 * Pages start at 1.
	 * 
	 * For example, the first page is 1-1.
	 * 
	 * @param x, column number of the page to check
	 * @param y, row number of the page to check
	 * 
	 * @return boolean true if the page exists, false if it doesn't
	 */
	private boolean doesPageExist(int x, int y) {
		return x > 0 && y > 0 && x <= getTotalNumberOfColumns() && y <= getTotalNumberOfRows();
	}
	
	/**
	 * Draw page to ScaledSWTGraphics which should have been initialized on a gc
	 * from an image.
	 * 
	 * @param g
	 *            the ScaledSWTGraphics we are drawing to.
	 * @param gc
	 *            the GC that has a font set in it.
	 * @param scale
	 *            how much we should scale by to fit a page in the image
	 * @param row
	 *            using this value, we calculate how much to translate by. This
	 *            is not the row according to the page breaks on the diagram,
	 *            but it is the row according to the number of pages we are
	 *            displaying in the print preview.
	 * @param col
	 *            using this value, we calculate how much to translate by. This
	 *            is not the col according to the page breaks on the diagram,
	 *            but it is the col according to the number of pages we are
	 *            displaying in the print preview.
	 */
	protected void drawPage(Graphics g, GC gc, float scale, int row, int col, PageMargins margins) {
		org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper
		.getPageSize(getPreferenceStore(), false);
		
		g.pushState();

		Rectangle bounds = getBounds();

		int translateX = (int) (0f
				- bounds.x
				* scale
				+ ((float) getTotalNumberOfColumns()
					* pageSize.x - bounds.width)
				* scale / 2f
				- (((float) pageSize.x)
				* ((float) (col + userX)) * scale)),
		translateY = (int) (0f
				- bounds.y
				* scale
				+ ((float) getTotalNumberOfRows()
					* pageSize.y - bounds.height)
				* scale / 2f
				- (((float) pageSize.y)
				* ((float) (row + userY)) * scale));
		
		
		//offset by page break figure bounds, then offset by the row or column we're at, and then take margins into account
		translateX = - bounds.x - (pageSize.x * (col + userX)) + (margins.left * (col + userX + 1)) + (margins.right * (col + userX));
		translateY = - bounds.y - (pageSize.y * (row + userY)) + (margins.top * (row + userY + 1)) + (margins.bottom * (row + userY));
		
		//To set a specific font, we could do this
		//For a completely accurate print preview, the font is printer specific
		//and may not be supported by the screen, so it is pointless
		//FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];
		//Font font = new Font(device, fontData);
		//g.setFont(font);
		
		if (doesPageExist(1 + userX + col, 1 + userY + row)) {
			g.pushState(); //draw text, don't make it too small or big
			g.scale(scale);

			String headerOrFooter =
				HeaderAndFooterHelper.makeHeaderOrFooterString(
					WorkspaceViewerProperties.HEADER_PREFIX,
					1 + userY + row,
					1 + userX + col,
					getDiagramEditPart());
			
			g.drawText(
				headerOrFooter,				
				(pageSize.x - MapMode.DPtoLP(gc.textExtent(headerOrFooter).x)) / 2,
				HeaderAndFooterHelper.TOP_MARGIN);

			headerOrFooter =
				HeaderAndFooterHelper.makeHeaderOrFooterString(
					WorkspaceViewerProperties.FOOTER_PREFIX,
					1 + userY + row,
					1 + userX + col,
					getDiagramEditPart());

			g.drawText(
				headerOrFooter,
				(pageSize.x - MapMode.DPtoLP(gc.textExtent(headerOrFooter).x)) / 2,
				pageSize.y - HeaderAndFooterHelper.BOTTOM_MARGIN);

			g.popState(); //for drawing the text				
		}

		g.scale(scale);

		g.translate(translateX, translateY);
		
		Rectangle clip = new Rectangle((pageSize.x - margins.left - margins.right)
			* (col + userX) + bounds.x, (pageSize.y - margins.top - margins.bottom)
			* (row + userY) + bounds.y, pageSize.x - margins.left - margins.right,
			pageSize.y - margins.top - margins.bottom);
		g.clipRect(clip);		
		
		//should be from printer and screen ratio and image size
		getDiagramEditPart().getLayer(LayerConstants.PRINTABLE_LAYERS).paint(g);
		
		g.popState();
	}

	/**
	 * Safely dispose an image
	 * 
	 * @param image, the Image to dispose.
	 */
	private void safeDisposeImage(Image image) {
		if (image != null && !image.isDisposed())
			image.dispose();
	}

	/**
	 * Dispose resources.
	 */
	protected void dispose() {
		disposeImages();
		safeDisposeImage(printImage);
		safeDisposeImage(disabledPrintImage);
		safeDisposeImage(pageImage);
		safeDisposeImage(leftImage);
		safeDisposeImage(disabledLeftImage);
		safeDisposeImage(rightImage);
		safeDisposeImage(disabledRightImage);
		safeDisposeImage(upImage);
		safeDisposeImage(disabledUpImage);
		safeDisposeImage(downImage);
		safeDisposeImage(disabledDownImage);
		safeDisposeImage(closeImage);
	}

	/**
	 * Convience method to get the workspcae preference store.
	 * 
	 * @return IPreferenceStore
	 */
	private IPreferenceStore getWorkspaceViewerPreferenceStore() {
		assert getDiagramEditPart().getViewer() instanceof DiagramGraphicalViewer;
		return ((DiagramGraphicalViewer) getDiagramEditPart().getViewer()).getWorkspaceViewerPreferenceStore();
	}
	
	private IPreferenceStore getGlobalPreferenceStore() {
		return (IPreferenceStore) getDiagramEditPart()
				.getDiagramPreferencesHint().getPreferenceStore();
	}
	
	private IPreferenceStore getPreferenceStore() {
		if (getWorkspaceViewerPreferenceStore().getBoolean(WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS))
			return getGlobalPreferenceStore();
		else
			return getWorkspaceViewerPreferenceStore();
	}
	
	/**
	 * Creates the <code>MapModeGraphics</code>.
	 * 
	 * @param scaledGraphics
	 * @return the <code>MapModeGraphics</code>
	 */
	protected MapModeGraphics createMapModeGraphics(
			ScaledGraphics scaledGraphics) {
		return new MapModeGraphics(scaledGraphics);
	}
	
	
	

	
	
	
	
	
}