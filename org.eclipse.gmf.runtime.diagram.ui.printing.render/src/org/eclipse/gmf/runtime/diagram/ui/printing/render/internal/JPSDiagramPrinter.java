/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.internal;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.SheetCollate;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.printing.IPrintHelper;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageSetupPageType;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper.PageMargins;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingPlugin;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.DiagramPrinter;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.PrintHelperUtil;
import org.eclipse.gmf.runtime.diagram.ui.printing.util.DiagramPrinterUtil;
import org.eclipse.gmf.runtime.diagram.ui.util.DiagramEditorUtil;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.graphics.GraphicsToGraphics2DAdaptor;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedScaledGraphics;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class supports printing using the Java Print Service API.
 * The logic of calculating page break etc. follows that of SWT printing
 * but the actual printing is done asynchronously in a platform independent way.
 * 
 * Much of the paging code was derived from the previous DiagramPrinter.
 *
 * @author James Bruck (jbruck)
 */
public class JPSDiagramPrinter extends DiagramPrinter implements
		java.awt.print.Printable {

	private static double AWT_DPI_CONST = 72.0;
	private PrintService printService;
	private PageData[] pages;
	private IPrintHelper printHelper;
	private int printerMinimumX = 0;
	private int printerMinimumY = 0;

	public JPSDiagramPrinter(PreferencesHint preferencesHint, IMapMode mm) {
		super(preferencesHint, mm);
		this.preferencesHint = preferencesHint;
		this.mapMode = mm;
	}

	public JPSDiagramPrinter(PreferencesHint preferencesHint) {
		this(preferencesHint, MapModeUtil.getMapMode());
	}

	public void setPrintHelper(IPrintHelper helper) {
		this.printHelper = helper;
	}

	/**
	 * Create a new print service given a printer name.
	 * 
	 * @param printerName
	 */
	public void setPrinter(String printerName) {
		AttributeSet attributes = new HashPrintServiceAttributeSet(
				new PrinterName(printerName, Locale.getDefault()));
		PrintService[] services = PrintServiceLookup.lookupPrintServices(
				DocFlavor.SERVICE_FORMATTED.PRINTABLE, attributes);
		printService = services[0];
	}

	/**
	 * Prints the contents of the diagram editor part.
	 */
	public void run() {

		Iterator<Diagram> it = diagrams.iterator();
		Shell shell = new Shell();
		try {
			while (it.hasNext()) {
				Diagram diagram = it.next();
				DiagramEditor openedDiagramEditor = DiagramEditorUtil
						.findOpenedDiagramEditorForID(ViewUtil
								.getIdStr(diagram));
				DiagramEditPart dgrmEP = openedDiagramEditor == null ? PrintHelperUtil
						.createDiagramEditPart(diagram, preferencesHint, shell)
						: openedDiagramEditor.getDiagramEditPart();

				boolean loadedPreferences = openedDiagramEditor != null
						|| PrintHelperUtil.initializePreferences(dgrmEP,
								preferencesHint);

				RootEditPart rep = dgrmEP.getRoot();
				if (rep instanceof DiagramRootEditPart) {
					this.mapMode = ((DiagramRootEditPart) rep).getMapMode();
				}

				IPreferenceStore preferenceStore = ((DiagramGraphicalViewer) dgrmEP
						.getViewer()).getWorkspaceViewerPreferenceStore();
				if (preferenceStore
						.getBoolean(WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS)) {
					if (dgrmEP.getDiagramPreferencesHint().getPreferenceStore() != null) {
						preferenceStore = (IPreferenceStore) dgrmEP
								.getDiagramPreferencesHint()
								.getPreferenceStore();
					}
				}
				doPrintDiagram(printService.createPrintJob(), dgrmEP,
						loadedPreferences, preferenceStore);
			}
		} finally {
			dispose();
			shell.dispose();
		}
	}

	/**
	 * Print the diagram figure using specified scale factor.
	 * 
	 * @param dgrmEP The diagram edit part to print
	 * @param loadedPreferences true if existing prefs could be loaded
     * 		successfully, false if not and defaults are being used.  This parameter
     * 		is important to obtain the correct page break bounds.
	 * @param fPreferences the preferenceStore that could either contain
     * 		existing preferences or defaults
	 */
	protected void printToScale(DiagramEditPart dgrmEP,
			boolean loadedPreferences, IPreferenceStore fPreferences) {

		Rectangle figureBounds = PrintHelperUtil.getPageBreakBounds(dgrmEP,
				loadedPreferences);

		org.eclipse.draw2d.geometry.Point pageBounds = PageInfoHelper
				.getPageSize(fPreferences, getMapMode());
		//
		// Translate to offset initial figure position
		//
		translated = new Point((int) (-figureBounds.x * userScale),
				(int) (-figureBounds.y * userScale));
		//
		// Calculate the number of page rows and columns
		//
		int numRows = 0, numCols = 0;

		PageMargins margins = adjustMarginsToScale(PageInfoHelper
				.getPageMargins(fPreferences, getMapMode()));

		FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];

		org.eclipse.draw2d.geometry.Point pageCount = getPageCount(dgrmEP,
				figureBounds, pageBounds, true);

		numCols = pageCount.x;
		numRows = pageCount.y;

		int row = 1, col = 1, finalRow = 0, finalColumn = 0;

		List<PageData> pageList = new java.util.ArrayList<PageData>();

		if (this.printRangePageSelection) {
			//
			// Print only the pages specified in the page range...
			//
			row = calculateRowFromPage(this.pageFrom, numCols);
			col = calculateColumnFromPage(this.pageFrom, numCols, row);

			finalRow = calculateRowFromPage(this.pageTo, numCols);
			finalColumn = calculateColumnFromPage(this.pageTo, numCols,
					finalRow);
		}
		//
		// Print the pages in row, column order
		//
		for (; row <= numRows; row++) {
			for (; col <= numCols; col++) {

				pageList.add(new PageData(pageList.size(), row, col, dgrmEP,
						figureBounds, margins, fontData, fPreferences));

				if (row == finalRow && col == finalColumn
						&& this.printRangePageSelection == true) {
					break;
				}
			}
			if (row == finalRow && col == finalColumn
					&& this.printRangePageSelection == true) {
				break;
			}
			col = 1;
		}
		pages =  pageList.toArray(new PageData[pageList.size()]);
	}

	/**
	 * Print the diagram figure to fit the number and rows and columns
     * specified by the user.
	 * 
	 * @param dgrmEP The diagram edit part to print
	 * @param loadedPreferences true if existing prefs could be loaded
     * 		successfully, false if not and defaults are being used.  This parameter
     * 		is important to obtain the correct page break bounds.
	 * @param fPreferences the preferenceStore that could either contain
     * 		existing preferences or defaults
	 */
	protected void printToPages(DiagramEditPart dgrmEP,
			boolean loadedPreferences, IPreferenceStore fPreferences) {

		Rectangle figureBounds = PrintHelperUtil.getPageBreakBounds(dgrmEP,
				loadedPreferences);

		org.eclipse.draw2d.geometry.Point pageBounds = PageInfoHelper
				.getPageSize(fPreferences, getMapMode());
		org.eclipse.draw2d.geometry.Point pageCount = getPageCount(dgrmEP,
				figureBounds, pageBounds, false);
		int numCols = pageCount.x;
		int numRows = pageCount.y;

		float actualWidth = 0;
		float actualHeight = 0;
		if (this.rows == 1 && this.columns == 1 && fitToPage) {
			figureBounds = dgrmEP.getChildrenBounds();
			actualWidth = figureBounds.width;
			actualHeight = figureBounds.height;
		} else {
			actualWidth = numCols * pageBounds.x;
			actualHeight = numRows * pageBounds.y;
		}

		int totalHeight = (this.rows * pageBounds.y);
		int totalWidth = (this.columns * pageBounds.x);

		float vScale = totalHeight / actualHeight;
		float hScale = totalWidth / actualWidth;

		this.userScale = Math.min(hScale, vScale);

		PageMargins margins = adjustMarginsToScale(PageInfoHelper
				.getPageMargins(fPreferences, getMapMode()));

		translated = new Point((int) (-figureBounds.x * userScale),
				(int) (-figureBounds.y * userScale));

		FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];

		int row = 1, col = 1, finalRow = 0, finalColumn = 0;
		List<PageData> pageList = new java.util.ArrayList<PageData>();

		if (this.printRangePageSelection) {
			//
			// Print only the pages specified in the page range
			// this corresponds to the physical pages, not the print range of
			// pages on one physical page.
			//
			row = calculateRowFromPage(this.pageFrom, this.columns);
			col = calculateColumnFromPage(this.pageFrom, this.columns, row);

			finalRow = calculateRowFromPage(this.pageTo, this.columns);
			finalColumn = calculateColumnFromPage(this.pageTo, this.columns,
					finalRow);
		}

		for (; row <= rows; row++) {
			for (; col <= columns; col++) {

				pageList.add(new PageData(pageList.size(), row, col, dgrmEP,
						figureBounds, margins, fontData, fPreferences));

				if (row == finalRow && col == finalColumn
						&& this.printRangePageSelection == true) {
					break;
				}
			}
			if (row == finalRow && col == finalColumn
					&& this.printRangePageSelection == true) {
				break;
			}
			col = 1;
		}
		pages = pageList.toArray(new PageData[pageList.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.print.Printable#print(java.awt.Graphics,
	 *      java.awt.print.PageFormat, int)
	 */
	public int print(java.awt.Graphics printGraphics, PageFormat pageFormat,
			int pageIndex) throws PrinterException {

		if (pageIndex >= pages.length) {
			return java.awt.print.Printable.NO_SUCH_PAGE;
		}

		try {			
			swtGraphics = new GraphicsToGraphics2DAdaptor(
					(java.awt.Graphics2D) printGraphics, new Rectangle(0, 0,
							(int) pageFormat.getWidth(), (int) pageFormat
									.getHeight()));

			graphics = createMapModeGraphics(createPrinterGraphics(swtGraphics));
			//
			// Take into account screen display DPI and the graphic DPI
			// 72.0 DPI is an AWT constant @see java.awt.Graphics2D
			//
			graphics.scale(AWT_DPI_CONST / display_dpi.x);

			drawPage(pages[pageIndex]);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			dispose();
		}

		return java.awt.print.Printable.PAGE_EXISTS;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.DiagramPrinter#createMapModeGraphics(org.eclipse.draw2d.Graphics)
	 */
	protected MapModeGraphics createMapModeGraphics(Graphics theGraphics) {
		return new RenderedMapModeGraphics(theGraphics, getMapMode());
	}

	
	protected RenderedScaledGraphics createPrinterGraphics(Graphics theGraphics) {
		return new RenderedScaledGraphics(theGraphics);
	}
	
	
	/**
	 * Set printing options in a format that is suitable for the Java print
	 * service
	 * 
	 * @param jobName
	 *            The printer job name to use
	 * @param fPreferences
	 *            obtain page information from preferences
	 * @return PrintRequestAttribute set suitable for Java print service
	 */
	protected PrintRequestAttributeSet initializePrintOptions(DocPrintJob printJob,
			String jobName,
			IPreferenceStore fPreferences) {

		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

		if (fPreferences
				.getBoolean(WorkspaceViewerProperties.PREF_USE_PORTRAIT)) {
			printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
		} else {
			printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
		}

		String pageSize = fPreferences
				.getString(WorkspaceViewerProperties.PREF_PAGE_SIZE);

		if (pageSize.compareToIgnoreCase(PageSetupPageType.LETTER.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
		} else if (pageSize.compareToIgnoreCase(PageSetupPageType.LEGAL
				.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.NA_LEGAL);
		} else if (pageSize.compareToIgnoreCase(PageSetupPageType.EXECUTIVE
				.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.EXECUTIVE);
		} else if (pageSize.compareToIgnoreCase(PageSetupPageType.A3.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.ISO_A3);
		} else if (pageSize.compareToIgnoreCase(PageSetupPageType.A4.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		} else if (pageSize.compareToIgnoreCase(PageSetupPageType.B4.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.ISO_B4);
		} else if (pageSize.compareToIgnoreCase(PageSetupPageType.B5.getName()) == 0) {
			printRequestAttributeSet.add(MediaSizeName.ISO_B5);
		}

		MediaSizeName media = (MediaSizeName) printRequestAttributeSet
				.get(Media.class);
		MediaSize mediaSize = MediaSize.getMediaSizeForName(media);
		
		printRequestAttributeSet.add(new MediaPrintableArea((float) 0.0,
				(float) 0.0, (mediaSize.getX(MediaSize.INCH)), (mediaSize
						.getY(MediaSize.INCH)), MediaPrintableArea.INCH));
		
		try {
			//
			// Calculate the minimum printable area.
			//
			PrinterJob printerJob = PrinterJob.getPrinterJob();
			printerJob.setPrintService(printService);

			PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
			pageFormat.setOrientation(PageFormat.PORTRAIT);

			Paper paper = new Paper();
			paper.setSize((mediaSize.getX(MediaSize.INCH) * 72), (mediaSize
					.getY(MediaSize.INCH) * 72));
			paper.setImageableArea(0, 0, mediaSize.getX(MediaSize.INCH) * 72, mediaSize
					.getY(MediaSize.INCH) * 72);
			pageFormat.setPaper(paper);
		
			pageFormat = PrinterJob.getPrinterJob().validatePage(pageFormat);

			printerMinimumX = getMapMode().DPtoLP(
					(int) Math.round(pageFormat.getImageableX()));
			printerMinimumY = getMapMode().DPtoLP(
					(int) Math.round(pageFormat.getImageableY()));

		} catch (PrinterException e) {
			//
			// Assume a default minimum printable area.
			//
			printerMinimumX = getMapMode().DPtoLP(18);
			printerMinimumY = getMapMode().DPtoLP(18);
		}

		printRequestAttributeSet.add(new Copies(printHelper
				.getDlgNumberOfCopies()));

		if (printHelper.getDlgCollate()) {
			printRequestAttributeSet.add(SheetCollate.COLLATED);
		} else {
			printRequestAttributeSet.add(SheetCollate.UNCOLLATED);
		}

		printRequestAttributeSet.add(new JobName(jobName, Locale.getDefault()));

		return printRequestAttributeSet;
	}

	/**
	 * Prints to scale or prints to rows x columns pages
	 * 
	 * @param printJob
	 * @param diagramEditPart
	 * @param loadedPreferences
	 * @param fPreferences
	 */
	protected void doPrintDiagram(DocPrintJob printJob,
			DiagramEditPart diagramEditPart, boolean loadedPreferences,
			IPreferenceStore fPreferences) {

		
		PrintRequestAttributeSet printRequestAttributeSet = initializePrintOptions(printJob,
				diagramEditPart
				.getDiagramView().getName(), fPreferences);

		if (isScaledPercent) {
			printToScale(diagramEditPart, loadedPreferences, fPreferences);
		} else {
			printToPages(diagramEditPart, loadedPreferences, fPreferences);
		}
		
		Doc doc = new SimpleDoc(this, DocFlavor.SERVICE_FORMATTED.PRINTABLE,
				new HashDocAttributeSet());

		try {
			printJob.print(doc, printRequestAttributeSet);
		} catch (PrintException e) {

			Trace.catching(DiagramPrintingPlugin.getInstance(),
					DiagramPrintingDebugOptions.EXCEPTIONS_CATCHING,
					DiagramPrinterUtil.class, e.getMessage(), e);
			Log.warning(DiagramPrintingPlugin.getInstance(),
					DiagramPrintingStatusCodes.RESOURCE_FAILURE,
					e.getMessage(), e);

			MessageDialog
					.openError(
							Display.getDefault().getActiveShell(),
							DiagramUIPrintingMessages.JPSDiagramPrinterUtil_ErrorTitle,
							DiagramUIPrintingMessages.JPSDiagramPrinterUtil_ErrorMessage);
		}
	}

	/**
	 * 
	 * This method paints a portion of the diagram. (The area painted
     * representing one page.)
     * 
	 * @param page indicates which page to print.
	 */
	protected void drawPage(PageData page) {
		this.graphics.pushState();

		internalDrawPage(page.diagram, page.bounds, page.preferences,
				page.margins, graphics, page.row, page.column, false);
		
		// TODO: Re-enable printing of header and footer in phase 2
		this.graphics.popState();
	}

	/**
	 * The real rendering of the page to the given graphical object occurs here.
	 */
	protected void internalDrawPage(DiagramEditPart dgrmEP,
			Rectangle figureBounds, IPreferenceStore fPreferences,
			PageMargins margins, Graphics g, int rowIndex, int colIndex,
			boolean RTL_ENABLED) {

		org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper
				.getPageSize(fPreferences, false, getMapMode());

		int width = pageSize.x, height = pageSize.y;

		g.pushState();
		
		g.translate(translated.x, translated.y);
		g.scale(userScale);

		int translateX = -(width * (colIndex - 1));
		int translateY = -(height * (rowIndex - 1));

		int scaledTranslateX = (int) (translateX / userScale);
		int scaledTranslateY = (int) (translateY / userScale);

		int scaledWidth = (int) (width / userScale);
		int scaledHeight = (int) (height / userScale);

		if (RTL_ENABLED) {
			scaledTranslateX += (margins.left * (colIndex - 1))
					+ (margins.right * (colIndex));
			scaledTranslateY += ((margins.top * rowIndex) + (margins.bottom * (rowIndex - 1)));
		} else {
			scaledTranslateX += ((margins.left * colIndex) + (margins.right * (colIndex - 1)));
			scaledTranslateY += ((margins.top * rowIndex) + (margins.bottom * (rowIndex - 1)));
		}

		g.translate(scaledTranslateX, scaledTranslateY);

		Rectangle clip = new Rectangle(
						(scaledWidth - margins.left - margins.right) * (colIndex - 1) + figureBounds.x, 
						(scaledHeight - margins.bottom - margins.top)* (rowIndex - 1) + figureBounds.y, 
						 scaledWidth - margins.right - margins.left, 
						 scaledHeight - margins.top - margins.bottom);
		g.clipRect(clip);

		dgrmEP.getLayer(LayerConstants.PRINTABLE_LAYERS).paint(g);

		g.popState();
	}
	
	/**
	 *  Adjust the page margins to be compatible with the user scale.
	 *  
	 * @param margins the page margins to adjust
	 * @return adjusted page margins
	 */
	private PageMargins adjustMarginsToScale(PageMargins margins) {

		margins.left /= userScale;
		margins.top /= userScale;
		margins.bottom /= userScale;
		margins.right /= userScale;

		//
		// The following calculations are being made in the SWTDiagramPrinter but
		// do not appear necessary.
		//
		
		// margins.left -= printerMinimumX / userScale;
		// margins.top -= printerMinimumY / userScale;
		// margins.bottom += printerMinimumY / userScale;
		// margins.right += printerMinimumX / userScale;

		// if (margins.left < 0)
		// margins.left = 0;
		// if (margins.right < 0)
		// margins.right = 0;
		// if (margins.top < 0)
		// margins.top = 0;
		// if (margins.bottom < 0)
		// margins.bottom = 0;

		//
		// Take into account screen display DPI and the graphic DPI
		// 72.0 DPI is an AWT constant @see java.awt.Graphics2D
		//
		// margins.left = (int) ((margins.left * display_dpi.x) /
		// AWT_DPI_CONST);
		// margins.top = (int) ((margins.top * display_dpi.y) / AWT_DPI_CONST);
		// margins.bottom = (int) ((margins.bottom * display_dpi.y) /
		// AWT_DPI_CONST);
		// margins.right = (int) ((margins.right * display_dpi.x) /
		// AWT_DPI_CONST);

		return margins;
	}

}
