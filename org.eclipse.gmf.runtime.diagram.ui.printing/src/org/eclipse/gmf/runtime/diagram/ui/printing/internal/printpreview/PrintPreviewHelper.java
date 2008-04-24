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

package org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview;



import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.PageBreakEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.PageBreaksFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper.PageMargins;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingPlugin;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.HeaderAndFooterHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.PrintHelperUtil;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.DiagramMapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * Print Preview Action to display the Print Preview dialog. There are no static
 * methods, so you must create an instance of this class.
 * 
 * Call doPrintPreview() after you've made an instance.
 *  
 * This class should be combined with the DiagramPrinter to reuse functionality.
 * 
 * @author Wayne Diu, wdiu
 */
public class PrintPreviewHelper{

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
    
    /**
     * Temporary shell to be used when creating the diagram editpart.
     */
    private Shell tempShell;

	/* Toolbar items are in left to right order */

	/**
	 * Print item on toolbar
	 */
	protected ToolItem printTool;
	
	/**
	 * Enable or disable the print option
	 */
	protected boolean enablePrinting = true;

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
	 * The print preview helper is capable of showing zoom input.
	 * userScale is a value between 0 and 1.
	 */
	protected double userScale = 1;
	
	/**
	 * Determine if we should consider fit to page options or not.
	 */
	private boolean fitToPage = false;

	/**
	 * Initialize all toolbar images
	 */
	protected void initializeToolbarImages() {
		printImage = DiagramUIPrintingPluginImages.DESC_PRINT.createImage();

		disabledPrintImage = DiagramUIPrintingPluginImages.DESC_PRINT_DISABLED
			.createImage();

		pageImage = DiagramUIPrintingPluginImages.DESC_PAGE.createImage();

		leftImage = DiagramUIPrintingPluginImages.DESC_LEFT.createImage();

		disabledLeftImage = DiagramUIPrintingPluginImages.DESC_LEFT_DISABLED
			.createImage();

		rightImage = DiagramUIPrintingPluginImages.DESC_RIGHT.createImage();

		disabledRightImage = DiagramUIPrintingPluginImages.DESC_RIGHT_DISABLED
			.createImage();

		upImage = DiagramUIPrintingPluginImages.DESC_UP.createImage();
		disabledUpImage = DiagramUIPrintingPluginImages.DESC_UP_DISABLED
			.createImage();

		downImage = DiagramUIPrintingPluginImages.DESC_DOWN.createImage();

		disabledDownImage = DiagramUIPrintingPluginImages.DESC_DOWN_DISABLED
			.createImage();

		closeImage = DiagramUIPrintingPluginImages.DESC_CLOSE.createImage();
	}

	/**
	 * Enable or disable printing depending on where the print preview is invoked from.
	 * 
	 * @param enablePrinting
	 */
	public void enablePrinting(boolean enablePrinting){
		this.enablePrinting = enablePrinting;
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

		setUserScale(PrintHelperUtil.getScale());
		
		if (getDiagramEditorPart() == null) {
			MessageDialog
				.openInformation(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					DiagramUIPrintingMessages.PrintPreview_Title, 
					DiagramUIPrintingMessages.PrintPreview_NotEnabled); 
			return;
		}

		if (!isPrinterInstalled()) {
			WindowUtil
				.doMessageBox(DiagramUIPrintingMessages.PrintPreview_NoPrinterInstalled, 
					DiagramUIPrintingMessages.PrintPreview_Title, 
					SWT.ICON_ERROR, PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());
			return;
		}

		initializeToolbarImages();

		initializeMapMode();

		diagramEditPart = null;
		pageBreakBounds = null;
		
		userX = 0;
		userY = 0;
		
		if (getTotalNumberOfRows() == 1 && getTotalNumberOfColumns() == 1) {
			numberOfRowsToDisplay = 1;
			numberOfColumnsToDisplay = 1;
		}
		else if (getTotalNumberOfRows() == 1) {
			numberOfRowsToDisplay = 1;
			numberOfColumnsToDisplay = 2;
		}
		else {
		numberOfRowsToDisplay = 2;
		numberOfColumnsToDisplay = 2;
		}

		Display display = Display.getDefault();
		
        //check for rtl Torientation...
        int style = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getStyle();
        if ((style & SWT.MIRRORED) != 0) {
            shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.TITLE
                | SWT.CLOSE | SWT.BORDER | SWT.RIGHT_TO_LEFT);
        }
        else
            shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.TITLE
                | SWT.CLOSE | SWT.BORDER);
        
		

		shell.setSize(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getShell().getSize());
		shell.setText(DiagramUIPrintingMessages.PrintPreview_Title);
		shell.setLocation(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getShell().getLocation());
		shell.setLayout(new GridLayout(1, true));

		ToolBar bar = new ToolBar(shell, SWT.FLAT | SWT.HORIZONTAL);

		printTool = new ToolItem(bar, SWT.NULL);
		printTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_PrintToolItem);
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

		if (printActionHelper == null || !enablePrinting) {
			printTool.setEnabled(false);
		}

		new ToolItem(bar, SWT.SEPARATOR);

		pagesTool = new ToolItem(bar, SWT.DROP_DOWN);
		pagesTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_PagesToolItem);
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
				
				refreshComposite();

			}

			public void widgetSelected(SelectionEvent event) {
				// Create the menu if it has not already been created

				if (menu == null) {
					// Lazy create the menu.
					menu = new Menu(shell);
					MenuItem menuItem = new MenuItem(menu, SWT.NONE);
					menuItem.setText(DiagramUIPrintingMessages.PrintPreview_1Up);
					menuItem.addSelectionListener(new SelectionAdapter() {

						public void widgetSelected(SelectionEvent e) {
							updatePreview(1, 1);
						}
					});

					menuItem = new MenuItem(menu, SWT.NONE);
					menuItem.setText(DiagramUIPrintingMessages.PrintPreview_2Up);
					menuItem.addSelectionListener(new SelectionAdapter() {

						public void widgetSelected(SelectionEvent e) {
							updatePreview(2, 1);
						}
					});

					menuItem = new MenuItem(menu, SWT.NONE);
					menuItem.setText(DiagramUIPrintingMessages.PrintPreview_4Up);
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
        if ((style & SWT.MIRRORED) != 0) {
            //switch left and right for RTL...
            leftTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_RightToolItem);
            leftTool.setImage(rightImage);
            leftTool.setDisabledImage(disabledRightImage);
        }
        else { 
            leftTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_LeftToolItem);
            leftTool.setImage(leftImage);
            leftTool.setDisabledImage(disabledLeftImage);
        }
		
		leftTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (userX > 0) {
					userX--;
					refreshComposite();
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
        if ((style & SWT.MIRRORED) != 0) {
            //switch left and right for RTL
            rightTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_LeftToolItem);
            rightTool.setImage(leftImage);
            rightTool.setDisabledImage(disabledLeftImage);
        }
        else { 
            rightTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_RightToolItem);
            rightTool.setImage(rightImage);
            rightTool.setDisabledImage(disabledRightImage);    
        }
		
		rightTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				//check for max pages to be safe
				if (!(userX + numberOfColumnsToDisplay + 1 > getTotalNumberOfColumns())) {
					userX++;
					refreshComposite();
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
		upTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_UpToolItem);
		upTool.setImage(upImage);
		upTool.setDisabledImage(disabledUpImage);
		upTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (userY > 0) {
					userY--;
					refreshComposite();
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
		downTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_DownToolItem);
		downTool.setImage(downImage);
		downTool.setDisabledImage(disabledDownImage);
		downTool.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (!(userY + numberOfRowsToDisplay + 1 > getTotalNumberOfRows())) {
					userY++;
					refreshComposite();
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
							
		ToolItem separator = new ToolItem(bar, SWT.SEPARATOR);
		final Text textField = new Text(bar, SWT.SINGLE | SWT.BORDER);
		textField.setText("XXXXX");//$NON-NLS-1$
		textField.setEnabled(true);
		textField.pack();
		textField.setText(getDisplayScale(PrintHelperUtil.getScale()));
		
		separator.setWidth(textField.getBounds().width);
		separator.setControl(textField);
		
		textField.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				// do nothing.
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				String scaleFactor = ((Text) e.getSource()).getText();

				int percentageIndex = scaleFactor.indexOf("%"); //$NON-NLS-1$
				if (percentageIndex > 0) {
					scaleFactor = scaleFactor.substring(0, percentageIndex);
				}
				int scalePercentage = Integer.parseInt(scaleFactor);
				setPercentScaling(scalePercentage);
				refreshComposite();
				((Text) e.getSource()).setText(getDisplayScale(scalePercentage));
			}
		});
		
		new ToolItem(bar, SWT.SEPARATOR);
		
		ToolItem fitToPageSeparator = new ToolItem(bar, SWT.SEPARATOR);
		Button buttonFitToPage = new Button(bar, SWT.PUSH);
		buttonFitToPage.setText(DiagramUIPrintingMessages.PrintPreview_FitToPage_ButtonText);
		buttonFitToPage.setEnabled(true);
		buttonFitToPage.pack();
			
		fitToPageSeparator.setWidth(buttonFitToPage.getBounds().width);
		fitToPageSeparator.setControl(buttonFitToPage);
		
		buttonFitToPage.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {

				FitToPagesDialog fitToPages = new FitToPagesDialog(shell);
				if (fitToPages.open() == Dialog.OK) {
					int pagesWide = fitToPages.getPagesWide();
					int pagesTall = fitToPages.getPagesTall();
					PrintHelperUtil.setScaleToWidth(pagesWide);
					PrintHelperUtil.setScaleToHeight(pagesTall);

					setFitToPage(pagesWide, pagesTall);
					refreshComposite();
					textField.setText(getDisplayScale(PrintHelperUtil
							.getScale()));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
								
		
								
		new ToolItem(bar, SWT.SEPARATOR);
		closeTool = new ToolItem(bar, SWT.NULL);
		closeTool.setToolTipText(DiagramUIPrintingMessages.PrintPreview_CloseToolItem);
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
		
		refreshComposite();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		dispose();
		shell.dispose();

	}

	private IMapMode mm;
	
	/**
	 * The constructor.
	 */
	public PrintPreviewHelper() {
		//do nothing
	}
	
	/**
	 * @return <code>IMapMode</code> to do the coordinate mapping
	 */
	protected IMapMode getMapMode() {
		return mm;
	}

	/**
	 * Initialize the map mode variable
	 */
	private void initializeMapMode() {
		DiagramEditor diagramEditor = getDiagramEditorPart();
		
		assert diagramEditor != null;
		
		IDiagramGraphicalViewer viewer = diagramEditor.getDiagramGraphicalViewer();
		
		if (viewer != null) {
			RootEditPart rootEP = viewer.getRootEditPart();
			
			if (rootEP instanceof DiagramRootEditPart) {
				this.mm = ((DiagramRootEditPart) rootEP).getMapMode();;
				return;
				
			}
		}
		
		this.mm = MapModeUtil.getMapMode();
	}
	
	/**
	 * Return and cache the total number of rows used by the diagram
	 * 
	 * @return int total number of rows used by the diagram
	 */
	private int getTotalNumberOfRows() {
		float numRows = ((float) (getBounds().height * userScale))
			/ PageInfoHelper
				.getPageSize(getPreferenceStore(), true, getMapMode()).y;
		
		return Math.max(1, (int) Math.ceil(numRows));
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
			pageBreakBounds = PrintHelperUtil.getPageBreakBounds(getDiagramEditPart(), true).getCopy();
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
			diagramEditPart = getDiagramEditorPart().getDiagramEditPart();
		}
		if (diagramEditPart == null) {
			Diagram diagram = getDiagramEditorPart().getDiagram(); //do not getDiagramEditPart
			PreferencesHint preferencesHint = getPreferencesHint(getDiagramEditorPart());
			diagramEditPart = PrintHelperUtil.createDiagramEditPart(diagram, preferencesHint, getTempShell());
			PrintHelperUtil.initializePreferences(diagramEditPart, preferencesHint);
		}
		return diagramEditPart;
	}
	
    /**
     * Lazily creates a new shell.
     * @return
     */
    private Shell getTempShell() {
        if (tempShell == null) {
            tempShell = new Shell();
        }
        return tempShell;
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
		float numCols = ((float) (getBounds().width * userScale))
			/ PageInfoHelper
				.getPageSize(getPreferenceStore(), true, getMapMode()).x;
				
		return Math.max(1, (int) Math.ceil(numCols));
	}

	/**
	 * Make sure printer is installed. Should not be able to print preview if no
	 * printer is installed, even though technically it will work.
	 * 
	 * Call this immediately with the rest of the initialization.
	 */
	protected boolean isPrinterInstalled() {
		try {
			PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
					null, null);
			return printServices.length > 0;
		} catch (SWTError e) {
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
	* A convenience method for refreshing the displayed image in the preview.
    */
	private void refreshComposite(){
		
		updateCompositeForNumberOfColumns(numberOfRowsToDisplay,
				numberOfColumnsToDisplay);
		
		updateLeftRightUpDownButtonsForToolbar();
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
			.getPageSize(getPreferenceStore(), false, getMapMode());
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
		
		PageMargins margins = PageInfoHelper.getPageMargins(getPreferenceStore(), getMapMode());
		

		//make sure height and width are not 0, if too small <4, don't bother
		if (!(imageHeight <= 4 || imageWidth <= 4)) {

			//or imageWidth / pageSize.x
			float scale = ( imageHeight / (float) pageSize.y)
				/ (float) DiagramMapModeUtil.getScale(getMapMode());
		
			scale *= userScale;
			
			margins.left /= userScale;   
			margins.right /= userScale;
			margins.bottom /= userScale;
			margins.top /= userScale;
									
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

        GC gc = null;
        
        //check for rtl orientation...
        if ((shell.getStyle() & SWT.MIRRORED) != 0) {
            gc = new GC(image, SWT.RIGHT_TO_LEFT);
        }
        else
            gc = new GC(image);

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
				.getPageSize(getPreferenceStore(), false, getMapMode());
		
		g.pushState();
		
		Rectangle bounds = getBounds();

		if (PrintHelperUtil.getScaleToWidth() == 1	&& PrintHelperUtil.getScaleToHeight() == 1 && fitToPage) {
			bounds = getDiagramEditPart().getChildrenBounds();
		}		
							
		int scaledPageSizeWidth = (int)(pageSize.x/userScale) ;
		int scaledPageSizeHeight = (int)(pageSize.y/userScale) ;
						
		//offset by page break figure bounds, then offset by the row or column we're at, and then take margins into account
		int translateX = - bounds.x - (scaledPageSizeWidth * (col + userX)) + (margins.left * (col + userX + 1)) + (margins.right * (col + userX));
		int translateY = - bounds.y - (scaledPageSizeHeight * (row + userY)) + (margins.top * (row + userY + 1)) + (margins.bottom * (row + userY));
		
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
				(pageSize.x - getMapMode().DPtoLP(gc.textExtent(headerOrFooter).x)) / 2,
				getMapMode().DPtoLP(HeaderAndFooterHelper.TOP_MARGIN_DP));

			headerOrFooter =
				HeaderAndFooterHelper.makeHeaderOrFooterString(
					WorkspaceViewerProperties.FOOTER_PREFIX,
					1 + userY + row,
					1 + userX + col,
					getDiagramEditPart());

			g.drawText(
				headerOrFooter,
				(pageSize.x - getMapMode().DPtoLP(gc.textExtent(headerOrFooter).x)) / 2,
				pageSize.y - getMapMode().DPtoLP(HeaderAndFooterHelper.BOTTOM_MARGIN_DP));

			g.popState(); //for drawing the text				
		}

		g.scale(scale);

		g.translate(translateX, translateY);
		
        Rectangle clip = new Rectangle(
        		(scaledPageSizeWidth - margins.left - margins.right) * (col + userX) + bounds.x, 
    			(scaledPageSizeHeight - margins.top - margins.bottom)* (row + userY) + bounds.y, 
    			scaledPageSizeWidth - margins.left - margins.right,
    			scaledPageSizeHeight - margins.top - margins.bottom);
		g.clipRect(clip);		
		
		//should be from printer and screen ratio and image size
		getDiagramEditPart().getLayer(LayerConstants.PRINTABLE_LAYERS).paint(g);
		
		g.popState();
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
		if (tempShell != null) {
	        tempShell.dispose();
	        tempShell = null;
		}
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
		return new MapModeGraphics(scaledGraphics, getMapMode());
	}
				
	/**
	 * Sets the scale factor.
	 * 
	 * @param scale : valid input is an integer larger than 0 representing a scale percentage 
	 */
	private void setUserScale(int scale){
		userScale = scale/100f;
		PrintHelperUtil.setScale(scale);
	}
				
	/**
	 * Prepare a string appropriate to show the scale factor to the user.
	 * 
	 * @param scale the scale factor, an integer greater than 0.
	 * @return A string of the scale factor to be displayed to the user.
	 */
	private String getDisplayScale(int scale) {
		return String.valueOf(scale) + "%"; //$NON-NLS-1$
	}

	
	/**
	 * Determine the page count when fit to page is used.
	 * 
	 * @param dgrmEP - The diagram edit part
	 * @param figureBounds - The bounds of the figure
	 * @param pageSize - Page size  
	 * @param applyUserScale - The user scale
	 * @return Point.x contains the total number of pages that span in a column
	 *         Point.y contains the total number of pages that span in a row
	 */
	protected org.eclipse.draw2d.geometry.Point getPageCount(
			DiagramEditPart dgrmEP, Rectangle figureBounds,
			org.eclipse.draw2d.geometry.Point pageSize, boolean applyUserScale) {
		RootEditPart rootEditPart = dgrmEP.getRoot();

		if (rootEditPart instanceof DiagramRootEditPart) {

			DiagramRootEditPart diagramRootEditPart = (DiagramRootEditPart) rootEditPart;
			PageBreakEditPart pageBreakEditPart = diagramRootEditPart
					.getPageBreakEditPart();

			double fNumCols = ((PageBreaksFigure) pageBreakEditPart.getFigure())
					.getPageCount().y
					* (applyUserScale ? userScale : 1);

			double fNumRows = ((PageBreaksFigure) pageBreakEditPart.getFigure())
					.getPageCount().x
					* (applyUserScale ? userScale : 1);

			int numCols = (int) Math.ceil(fNumCols);
			int numRows = (int) Math.ceil(fNumRows);

			return new org.eclipse.draw2d.geometry.Point(numCols, numRows);

		} else {
			double fNumRows = (figureBounds.height * (applyUserScale ? userScale : 1))
					/ pageSize.y;
			int numRows = (int) Math.ceil(fNumRows);

			double fNumCols = (figureBounds.width * (applyUserScale ? userScale	: 1))
					/ pageSize.x;
			int numCols = (int) Math.ceil(fNumCols);

			return new org.eclipse.draw2d.geometry.Point(numCols, numRows);
		}
	}

	/**
	 * Reset the fit to page flag and set the user scale when the
	 * preview is triggered from the print dialog.
	 * 
	 * @param userScale a whole number greater than zero
	 */
	public void setPercentScaling(int userScale){
		fitToPage = false;
		setUserScale(userScale);
	}
	
	/**
	 * Recalculates a zoom ratio that can be used when displaying fit to page.
	 * 
	 * @param rows  The number of rows to fit the display to.
	 * @param columns The number of columns to fit the display to.
	 */
	public void setFitToPage(int width, int height) {

		fitToPage = true;

		initializeMapMode();
		
		Rectangle figureBounds = PrintHelperUtil.getPageBreakBounds(
				getDiagramEditPart(), true);

		org.eclipse.draw2d.geometry.Point pageBounds = PageInfoHelper
				.getPageSize(getPreferenceStore(), getMapMode());
		org.eclipse.draw2d.geometry.Point pageCount = getPageCount(
				getDiagramEditPart(), figureBounds, pageBounds, false);
		int numCols = pageCount.x;
		int numRows = pageCount.y;

		float actualWidth = 0;
		float actualHeight = 0;

		if (height == 1 && width == 1) {
			figureBounds = getDiagramEditPart().getChildrenBounds();
			actualWidth = figureBounds.width;
			actualHeight = figureBounds.height;
		} else {
			actualWidth = numCols * pageBounds.x;
			actualHeight = numRows * pageBounds.y;
		}

		int totalHeight = (height * pageBounds.y);
		int totalWidth = (width * pageBounds.x);

		int vScale = (int) ((totalHeight * 100) / actualHeight);
		int hScale = (int) ((totalWidth * 100) / actualWidth);

		setUserScale(Math.min(hScale, vScale));
	}
	
	
	/**
	 * A dialog that prompts the user for scaling the print settings
	 * to the number of pages wide and tall.
	 * Scale to Pages will affect zoom and offsets.  It will maximize the 
	 * scaling factor and modify offsets to fit the entire image within 
	 * the pages specified.
	 * 
	 * @author James Bruck (jbruck)
	 *
	 */
	private class FitToPagesDialog extends Dialog {

		/**
		 *  The text field that holds the pages width
		 */
		private Text textWide;
		/**
		 *  The text field that holds the pages height
		 */
		private Text textTall;
		/**
		 *  The number of pages wide
		 */
		private int pagesWide = 0;
		/**
		 *  The number of pages tall
		 */
		private int pagesTall = 0;
		
		public FitToPagesDialog(Shell parent) {
			super(parent);
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
		 */
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);

			newShell.setText(DiagramUIPrintingMessages.PrintPreview_FitToPage_DialogTitle);
		}

		/**
		 * A helper that creates a label.
		 * 
		 * @param parent
		 * @param text
		 * @return a newly created label.
		 */
		Label label(Composite parent, String text) {
			Label result = new Label(parent, SWT.NONE);
			result.setText(text);
			return result;
		}

		/**
		 * A helper that gets layout data or creates 
		 * it as necessary.
		 * 
		 * @param control
		 * @return GridData for the layout
		 */
		GridData getLayoutData(Control control) {
			GridData result = (GridData) control.getLayoutData();

			if (result == null) {
				result = new GridData();
				control.setLayoutData(result);
			}
			return result;
		}

		/**
		 * A helper to layout items horizontally.
		 * 
		 * @param control
		 * @param inset
		 * @return The layed out control
		 */
		Control layoutHorizontalIndent(Control control, int inset) {
			GridData data = getLayoutData(control);
			data.horizontalIndent = inset;

			return control;
		}

		/**
		 * A helper to right justify a control.
		 * 
		 * @param control
		 * @return The layed out control
		 */
		Control layoutAlignRight(Control control) {
			GridData data = getLayoutData(control);

			data.horizontalAlignment = SWT.END;
			data.grabExcessHorizontalSpace = false;

			return control;
		}

		/**
		 * A helper to layout a certain number of dialog units wide.
		 * @param control
		 * @param dlus
		 * @return the aligned out control
		 */
		Control layoutWidth(Control control, int dlus) {
			if (dlus > 0) {
				GridData data = getLayoutData(control);
				data.widthHint = convertHorizontalDLUsToPixels(dlus);
			}
			return control;
		}

		/**
		 * A helper that lays out the given control horizontally.
		 * 
		 * @param control
		 * @param grab
		 * @return The aligned control
		 */
		Control layoutFillHorizontal(Control control, boolean grab) {
			GridData data = getLayoutData(control);

			data.horizontalAlignment = SWT.FILL;
			data.grabExcessHorizontalSpace = grab;

			return control;
		}

		/**
		 * A helper that creates a new text field.
		 * 
		 * @param parent
		 * @param width
		 * @return a newly created text field
		 */
		Text text(Composite parent, int width) {
			Text result = new Text(parent, SWT.SINGLE | SWT.BORDER);
			layoutFillHorizontal(result, false);
			layoutWidth(result, width);

			return result;
		}

		/**
		 * A helper that lays out a control grabbing both horizontal and
		 * vertical extra spacing.
		 * 
		 * @param control
		 * @return 
		 */
		Control layoutFillBoth(Control control) {
			GridData data = getLayoutData(control);

			data.horizontalAlignment = SWT.FILL;
			data.grabExcessHorizontalSpace = true;
			data.verticalAlignment = SWT.FILL;
			data.grabExcessVerticalSpace = true;

			return control;
		}

		/**
		 * A helper that creates a new group.
		 * 
		 * @param parent
		 * @param text
		 * @return A newly created group.
		 */
		Group group(Composite parent, String text) {
			Group result = new Group(parent, SWT.NONE);
			result.setText(text);
			layoutFillBoth(result);
			return result;
		}

		/**
		 * Layout the given control with the number of columns specified.
		 * 
		 * @param composite
		 * @param columns
		 * @return The aligned out control
		 */
		Composite layout(Composite composite, int columns) {
			GridLayout g = new GridLayout(columns, false);
			g.marginLeft = 6;
			g.marginRight = 6;
			g.marginTop = 6;
			g.marginBottom = 3;
			composite.setLayout(g);

			return composite;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createDialogArea(Composite parent) {

			Composite composite = new Composite(parent, SWT.NONE);
			layout(composite, 1);
			Composite scaleGroup = group(composite,
					DiagramUIPrintingMessages.JPSPrintDialog_Scaling);
			layout(scaleGroup, 5);

			layoutHorizontalIndent(layoutAlignRight(label(scaleGroup,
					DiagramUIPrintingMessages.JPSPrintDialog_PagesWide)), 15);

			textWide = text(scaleGroup, 20);
			textWide.setText(String.valueOf(PrintHelperUtil.getScaleToWidth()));

			layoutHorizontalIndent(layoutAlignRight(label(scaleGroup,
					DiagramUIPrintingMessages.JPSPrintDialog_PagesTall)), 15);
			textTall = text(scaleGroup, 20);
			textTall
					.setText(String.valueOf(PrintHelperUtil.getScaleToHeight()));

			return composite;
		}

		public int getPagesWide() {
			return pagesWide;
		}

		public int getPagesTall() {
			return pagesTall;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
		 */
		@Override
		protected void okPressed() {
			pagesWide = Integer.parseInt(textWide.getText());
			pagesTall = Integer.parseInt(textTall.getText());
			super.okPressed();
		}
	}
		
}