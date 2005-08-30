/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002 - 2005.  All Rights Reserved.             |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.internal.util;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper.PageMargins;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.PrinterGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import com.ibm.xtools.notation.Diagram;

/*
 * @canBeSeenBy %level1
 */
public class DiagramPrinter
	implements Runnable {

	protected Printer printer;

	private Point display_dpi;

	private boolean isScaledPercent = false;

	private int rows = 1;

	private int columns = 1;

	private GC gc;

	protected Graphics graphics;
	
	protected Point printerOffset;

	protected Rectangle logicalClientArea;

	private float userScale;

	/**
	 * Used when a Collection of Diagram objects are passed in instead of an
	 * IEditorPart.
	 */
	protected Collection diagrams;

	/**
	 * The initial amount that the diagram should be translated, set by
	 * printToScale or printToPages which calls drawToFitRowsColumns.
	 */
	private Point translated = null;
	
	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connectors, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private PreferencesHint preferencesHint;

	/**
	 * Creates a new instance. The following variables must be initialized
	 * before calling <code>run()</code>:
	 * <li><code>printer</code></li>
	 * <li><code>display_dpi</code></li>
	 * <li><code>diagrams</code></li>
	 */
	public DiagramPrinter(PreferencesHint preferencesHint) {
		super();
		this.preferencesHint = preferencesHint;
	}
	
	/**
	 * Sets the columns.
	 * 
	 * @param columns
	 *            The columns to set.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Sets the diagrams.
	 * 
	 * @param diagrams
	 *            a Collection of Diagram objects
	 */
	public void setDiagrams(Collection diagrams) {
		this.diagrams = diagrams;
	}

	/**
	 * Sets the display DPI.
	 * 
	 * @param display_dpi
	 *            The display_dpi to set.
	 */
	public void setDisplayDPI(Point display_dpi) {
		this.display_dpi = new Point(display_dpi.x, display_dpi.y);
	}

	/**
	 * Sets the printer.
	 * 
	 * @param printer
	 *            The printer to set.
	 */
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	/**
	 * Sets the rows.
	 * 
	 * @param rows
	 *            The rows to set.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Sets the scaled percent.
	 * 
	 * @param scalePercent
	 */
	public void setScaledPercent(int scalePercent) {
		this.isScaledPercent = true;
		this.userScale = (scalePercent) / 100.0f;

	}

	/**
	 * Prints the contents of the diagram editor part.
	 */
	public void run() {
		assert null != printer : "printer must be set"; //$NON-NLS-1$
		if (!(printer.startJob("Printing"))) { //$NON-NLS-1$
			return;
		}

		initialize();

		assert diagrams != null;
		Iterator it = diagrams.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			//the diagrams List is only supposed to have Diagram objects
			Assert.isTrue(obj instanceof Diagram);
			DiagramEditPart dgrmEP = PrintHelper.createDiagramEditPart((Diagram) obj, preferencesHint);
			boolean loadedPreferences = PrintHelper.initializePreferences(dgrmEP, preferencesHint);
			assert dgrmEP.getViewer() instanceof DiagramGraphicalViewer;
			IPreferenceStore fPreferences = ((DiagramGraphicalViewer)dgrmEP.getViewer()).getWorkspaceViewerPreferenceStore();
			doPrintDiagram(dgrmEP, loadedPreferences, fPreferences);
		}

		printer.endJob();
		dispose();
	}

	/**
	 * Prints to scale or prints to rows x columns pages
	 */
	private void doPrintDiagram(DiagramEditPart dgrmEP, boolean loadedPreferences, IPreferenceStore fPreferences) {
		this.graphics.pushState();
		if (isScaledPercent) {
			printToScale(dgrmEP, loadedPreferences, fPreferences);
		} else {
			printToPages(dgrmEP, loadedPreferences, fPreferences);
		}
		this.graphics.popState();
	}

	private void initialize() {

		assert null != printer : "printer must be set"; //$NON-NLS-1$
		
		this.gc = new GC(printer);
		gc.setXORMode(false);

		Graphics g = new SWTGraphics(gc);
		this.graphics = createMapModeGraphics(g);
		this.graphics.scale(computePrinterDisplayScale());
		
		this.logicalClientArea = this.graphics.getClip(new Rectangle(
			this.printer.getClientArea()));
		
	}

	/**
	 * Usually, the printable area is less than the page.
	 * This method returns the offset for each x margin and each y margin.
	 * x margins are left and right
	 * y margins are top and bottom
	 * 
	 * We'll assume the left and right offsets are the same and the
	 * top and bottom offsets are the same.
	 * 
	 * @return Point with x and y offsets
	 */
	protected Point getPrinterOffset() {
		if (printerOffset == null) {
			int offsetX = this.printer.getBounds().width
			- this.printer.getClientArea().width;
			int offsetY = this.printer.getBounds().height
			- this.printer.getClientArea().height;
		
			// assume half on each side
			offsetX = (int) (MapMode
				.DPtoLP((int) (offsetX / 2.0f * display_dpi.x / printer.getDPI().x)) / userScale);
			offsetY = (int) (MapMode
				.DPtoLP((int) (offsetY / 2.0f * display_dpi.y / printer.getDPI().y)) / userScale);
			
			printerOffset = new Point(offsetX, offsetY);
		}
		
		return printerOffset;
	}

	/**
	 * Print the diagram figure using specified scale factor.
	 * 
	 * @param dgrmEP the DiagramEditPart that will be printed
	 * @param loadedPreferences true if existing prefs could be loaded
	 * successfully, false if not and defaults are being used.  This parameter
	 * is important to obtain the correct page break bounds.
	 * @param fPreferences the preferenceStore that could either contain
	 * existing preferences or defaults
	 */
	protected void printToScale(DiagramEditPart dgrmEP, boolean loadedPreferences, IPreferenceStore fPreferences) {

		assert null != printer : "printer must be set"; //$NON-NLS-1$
		Rectangle figureBounds = PrintHelper.getPageBreakBounds(dgrmEP, loadedPreferences);

		//translate to offset initial figure position
		translated = new Point(-figureBounds.x, -figureBounds.y);
		this.graphics.translate(translated.x, translated.y);

		this.graphics.scale(userScale);

		//calculate the number of page rows and columns
		
		org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper.getPageSize(fPreferences, false);
		
		float fNumRows = (figureBounds.height * userScale)
			/ pageSize.y;
		int numRows = (int) Math.ceil(fNumRows);

		float fNumCols = (figureBounds.width * userScale)
			/ pageSize.x;
		int numCols = (int) Math.ceil(fNumCols);
		
		PageMargins margins = PageInfoHelper.getPageMargins(fPreferences);
		adjustMargins(margins, userScale, getPrinterOffset());

		GC gc_ = new GC(Display.getDefault());

		FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];
		Font font = new Font(printer, fontData);

		try {
			//print the pages in row, column order
			for (int row = 1; row <= numRows; row++) {
				for (int col = 1; col <= numCols; col++) {
					printer.startPage();
					drawPage(gc_, dgrmEP, fPreferences, figureBounds, margins, font, row, col);
					printer.endPage();
				}
			}
		} finally {
			//must dispose resources
			font.dispose();
			gc_.dispose();
		}
	}

	/**
	 * Draw the header and footer
	 * 
	 * @param gc_,
	 *            a graphics context that is not null which this method will use
	 *            for figuring ouyt the font's extent
	 * @param figureBounds,
	 *            Rectangle with the bounds of the figure
	 * @param rowIndex,
	 *            int
	 * @param colIndex,
	 *            int
	 */
	protected void drawHeaderAndFooter(GC gc_, DiagramEditPart dgrmEP, Rectangle figureBounds,
			Font font, int rowIndex, int colIndex) {

		int width = this.logicalClientArea.width;
		int height = this.logicalClientArea.height;

		this.graphics.pushState(); //draw text, don't make it too small or big
		this.graphics.setFont(font);

		this.graphics.scale(1.0f / userScale);
		this.graphics.translate(-translated.x, -translated.y);

		String headerOrFooter = HeaderAndFooterHelper.makeHeaderOrFooterString(
			WorkspaceViewerProperties.HEADER_PREFIX, rowIndex, colIndex,
			dgrmEP);

		this.graphics.drawText(headerOrFooter,
			HeaderAndFooterHelper.LEFT_MARGIN
				+ (width - MapMode.DPtoLP(gc_.textExtent(headerOrFooter).x))
				/ 2, HeaderAndFooterHelper.TOP_MARGIN);

		headerOrFooter = HeaderAndFooterHelper.makeHeaderOrFooterString(
			WorkspaceViewerProperties.FOOTER_PREFIX, rowIndex, colIndex,
			dgrmEP);

		this.graphics.drawText(headerOrFooter,
			HeaderAndFooterHelper.LEFT_MARGIN
				+ (width - MapMode.DPtoLP(gc_.textExtent(headerOrFooter).x))
				/ 2, height - HeaderAndFooterHelper.BOTTOM_MARGIN);

		this.graphics.popState(); //for drawing the text

	}

	/**
	 * This method paints a portion of the diagram. (The area painted
	 * representing one page.)
	 * 
	 * @param gc_ a graphics context that is not null which this method will use
	 * for figuring ouyt the font's extent
	 * @param dgrmEP the DiagramEditPart that will be printed
	 * @param fPreferences the preferenceStore that could either contain
	 * existing preferences or defaults
	 * @param figureBounds the page break bounds we'll have to offset by
	 * @param font the Font to print the header or footer with
	 * @param rowIndex index of row we're printing
	 * @param colIndex index of column we're priniting
	 */
	protected void drawPage(GC gc_, DiagramEditPart dgrmEP,
			IPreferenceStore fPreferences, Rectangle figureBounds,
			PageMargins margins, Font font, int rowIndex, int colIndex) {

		org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper
			.getPageSize(fPreferences, false);
		int width = pageSize.x, height = pageSize.y;

		int translateX = -(width * (colIndex - 1));
		int translateY = -(height * (rowIndex - 1));

		int scaledTranslateX = (int) (translateX / userScale);
		int scaledTranslateY = (int) (translateY / userScale);

		int scaledWidth = (int) (width / userScale);
		int scaledHeight = (int) (height / userScale);

		scaledTranslateX += ((margins.left * colIndex) + (margins.right * (colIndex - 1)));
		scaledTranslateY += ((margins.top * rowIndex) + (margins.bottom * (rowIndex - 1)));

		drawHeaderAndFooter(gc_, dgrmEP, figureBounds, font, rowIndex, colIndex);

		this.graphics.pushState();

		this.graphics.translate(scaledTranslateX, scaledTranslateY);

		Rectangle r = new Rectangle((scaledWidth - margins.left - margins.right)
			* (colIndex - 1) + figureBounds.x, (scaledHeight - margins.top - margins.bottom)
			* (rowIndex - 1) + figureBounds.y, scaledWidth - margins.left - margins.right,
			scaledHeight - margins.top - margins.bottom);
		this.graphics.clipRect(r);

		dgrmEP.getLayer(LayerConstants.PRINTABLE_LAYERS).paint(this.graphics);

		this.graphics.popState();
	}

	/**
	 * Print the diagram figure to fit the number and rows and columns
	 * specified by the user.
	 * 
	 * @param dgrmEP the DiagramEditPart that will be printed
	 * @param loadedPreferences true if existing prefs could be loaded
	 * successfully, false if not and defaults are being used.  This parameter
	 * is important to obtain the correct page break bounds.
	 * @param fPreferences the preferenceStore that could either contain
	 * existing preferences or defaults
	 */
	protected void printToPages(DiagramEditPart dgrmEP,
			boolean loadedPreferences, IPreferenceStore fPreferences) {
		assert null != printer : "printer must be set"; //$NON-NLS-1$

		Rectangle figureBounds = PrintHelper.getPageBreakBounds(dgrmEP,
			loadedPreferences);

		org.eclipse.draw2d.geometry.Point pageBounds = PageInfoHelper
			.getPageSize(fPreferences);

		Rectangle translate = new Rectangle(Math.min(0, figureBounds.x), Math
			.min(0, figureBounds.y),
			Math.max(pageBounds.x, figureBounds.width), Math.max(pageBounds.y,
				figureBounds.height));

		// do not include margins
		org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper
			.getPageSize(fPreferences, true);

		// there's usually a difference between the total page area and the
		// actual printable area
		int offsetX = this.printer.getBounds().width
			- this.printer.getClientArea().width;
		int offsetY = this.printer.getBounds().height
			- this.printer.getClientArea().height;

		pageSize.x -= offsetX * columns;
		pageSize.y -= offsetY * rows;

		float vScale = ((float) (this.rows * pageSize.y)) / translate.height;
		float hScale = ((float) (this.columns * pageSize.x)) / translate.width;

		this.userScale = Math.min(hScale, vScale);

		// translate to offset figure position
		translated = new Point((int) (-translate.x * userScale),
			(int) (-translate.y * userScale));
		this.graphics.translate(translated.x, translated.y);

		this.graphics.scale(this.userScale);

		PageMargins margins = PageInfoHelper.getPageMargins(fPreferences);
		adjustMargins(margins, userScale, getPrinterOffset());

		GC gc_ = new GC(Display.getDefault());

		FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];
		Font font = new Font(printer, fontData);

		try {
			// print the pages in row, column order
			for (int row = 1; row <= rows; row++) {
				for (int col = 1; col <= columns; col++) {
					printer.startPage();
					drawPage(gc_, dgrmEP, fPreferences, translate, margins,
						font, row, col);
					printer.endPage();
				}
			}
		} finally {
			// must dispose resources
			font.dispose();
			gc_.dispose();
		}
	}

	/**
	 * Return scale factor between printer and display.
	 * 
	 * @return float
	 */
	private float computePrinterDisplayScale() {
		assert null != printer : "printer must be set"; //$NON-NLS-1$
		assert null != display_dpi : "display_dpi must be set"; //$NON-NLS-1$

		Point dpi = printer.getDPI();
		float scale = dpi.x / (float) display_dpi.x;
		
		return scale;
	}

	/**
	 * Disposes of the resources.
	 */
	private void dispose() {

		if (this.gc != null) {
			this.gc.dispose();
			this.gc = null;
		}
	}

	/**
	 * Creates the <code>MapModeGraphics</code>.
	 * 
	 * @param theGraphics
	 *            the <code>Graphics</code> object
	 * @return the new <code>MapModeGraphics</code>
	 */
	protected MapModeGraphics createMapModeGraphics(Graphics theGraphics) {
		return new MapModeGraphics(new PrinterGraphics(theGraphics, printer,
			true));
	}
	
	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint() {
		return preferencesHint;
	}
	
	/**
	 * Adjust the given PageMargins by the scale and offset
	 * 
	 * @param margins PageMargins to adjust
	 * @param scale margins will be scaled by this amount
	 * @param offset to adjust margins by
	 */
	protected void adjustMargins(PageMargins margins, float scale, Point offset) {
		//scale
		margins.left /= scale;
		margins.top /= scale;
		margins.right /= scale;
		margins.bottom /= scale;
		
		//offsets
		margins.left -= offset.x; 
		margins.right += offset.x;
		margins.top -= offset.y;
		margins.bottom += offset.y;
		
		// this is more readable than doing Math.min for all the above
		if (margins.left < 0)
			margins.left = 0;
		if (margins.right < 0)
			margins.right = 0;
		if (margins.top < 0)
			margins.top = 0;
		if (margins.bottom < 0)
			margins.bottom = 0;
	}

}