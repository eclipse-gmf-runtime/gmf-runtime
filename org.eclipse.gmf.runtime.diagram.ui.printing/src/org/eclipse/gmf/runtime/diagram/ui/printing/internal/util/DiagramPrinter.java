/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.internal.util;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.PageBreakEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.PageBreaksFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper.PageMargins;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.util.DiagramEditorUtil;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.PrinterGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

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
    
    private boolean printRangePageSelection = false;
    
    private int pageFrom = 1, pageTo = 1;

    private GC gc;
    
    private Graphics swtGraphics;
    
    private Graphics graphics;
    
    private PrinterGraphics printerGraphics;
    
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
     * diagramming preference values for properties of shapes, connections, and
     * diagrams can be retrieved. This hint is mapped to a preference store in
     * the {@link DiagramPreferencesRegistry}.
     */
    private PreferencesHint preferencesHint;
    private IMapMode mm;
    
    private boolean fitToPage = false;
    
    /**
     * change the fit to page state
     * @param fitToPage the new fit to page state
     */
    public void setFitToPage(boolean fitToPage){
        this.fitToPage = fitToPage;
    }

    /**
     * Creates a new instance. The following variables must be initialized
     * before calling <code>run()</code>:
     * <li><code>printer</code></li>
     * <li><code>display_dpi</code></li>
     * <li><code>diagrams</code></li>
     * @param mm <code>IMapMode</code> to do the coordinate mapping
     */
    public DiagramPrinter(PreferencesHint preferencesHint, IMapMode mm) {
        super();
        this.preferencesHint = preferencesHint;
        this.mm = mm;
    }
    
    /**
     * Creates a new instance. The following variables must be initialized
     * before calling <code>run()</code>:
     * <li><code>printer</code></li>
     * <li><code>display_dpi</code></li>
     * <li><code>diagrams</code></li>
     * @param mm <code>IMapMode</code> to do the coordinate mapping
     */
    public DiagramPrinter(PreferencesHint preferencesHint) {
        this(preferencesHint, MapModeUtil.getMapMode());
    }
    
    /**
     * @return <code>IMapMode</code> to do the coordinate mapping
     */
    protected IMapMode getMapMode() {
        return mm;
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
    
    public void setPrintRangePageSelection(boolean isPrintRangePageSelected) {
        this.printRangePageSelection = isPrintRangePageSelected;
    }
    
    public void setPrintRangePages(int pageFrom, int pageTo) {
        assert pageFrom > 0 : "From page in print range page selection must be bigger than zero."; //$NON-NLS-1$
        assert (pageTo > 0 && pageTo >= pageFrom) : "To page in print range page selection must be bigger than zero and from page.";  //$NON-NLS-1$
        this.pageFrom = pageFrom;
        this.pageTo = pageTo;
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
        
        assert diagrams != null;
        Iterator it = diagrams.iterator();

        Shell shell = new Shell();
        try {
            while (it.hasNext()) {
                Object obj = it.next();
                //the diagrams List is only supposed to have Diagram objects
                Assert.isTrue(obj instanceof Diagram);
                Diagram diagram = (Diagram)obj;
                DiagramEditor openedDiagramEditor = DiagramEditorUtil
						.findOpenedDiagramEditorForID(ViewUtil
								.getIdStr(diagram));
				DiagramEditPart dgrmEP = openedDiagramEditor == null ? PrintHelper
						.createDiagramEditPart(diagram, preferencesHint, shell)
						: openedDiagramEditor.getDiagramEditPart();
                
                boolean loadedPreferences = openedDiagramEditor != null || PrintHelper.initializePreferences(dgrmEP, preferencesHint);

                RootEditPart rep = dgrmEP.getRoot();
                if (rep instanceof DiagramRootEditPart) 
                    this.mm = ((DiagramRootEditPart)rep).getMapMode();
                
                initialize();
                
                
                IPreferenceStore pref = null;
                
                assert dgrmEP.getViewer() instanceof DiagramGraphicalViewer;
        
                pref = ((DiagramGraphicalViewer)dgrmEP.getViewer()).getWorkspaceViewerPreferenceStore();
                
                if (pref.getBoolean(WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS)) {
                    
                    //get workspace settings...
                    if (dgrmEP.getDiagramPreferencesHint().getPreferenceStore() != null)
                        pref = (IPreferenceStore)dgrmEP.getDiagramPreferencesHint().getPreferenceStore(); 
                }
                
                doPrintDiagram(dgrmEP, loadedPreferences, pref);
                
                dispose();
            }
            printer.endJob();
        } finally {
            shell.dispose();
        }        
    }
    
    /**
     * Calculates the row in a grid, given a page number.
     * | 1 | 2 | 3 |
     * | 4 | 5 | 6 |
     * | 7 | 8 | 9 |
     * 
     * Given pageNum=5 and totalNumColumns=3, will return 2
     * (2nd row).
     * 
     * @param pageNum the page number in the grid.
     * @param totalNumColumns total number of columns of the grid.
     * @return row number corresponding to the page number.
     */
    private int calculateRowFromPage(int pageNum, int totalNumColumns) {
        int row = pageNum / totalNumColumns;
        if (pageNum % totalNumColumns != 0)
            row++;
        return row;
    }
    
    /**
     * Calculates the column in a grid, given a page number.
     * | 1 | 2 | 3 |
     * | 4 | 5 | 6 |
     * | 7 | 8 | 9 |
     * 
     * Given pageNum=5 and totalNumColumns=3, will return 2
     * (2nd column).
     *  
     * @param pageNum the page number in the grid.
     * @param totalNumColumns total number of columns of the grid.
     * @param cRow the corresponding row of the page number.
     * @return row number corresponding to the page number.
     */
    private int calculateColumnFromPage(int pageNum, int totalNumColumns, int cRow) {
        int col = pageNum - ((cRow - 1) * totalNumColumns);
        return col;
    }
    
    /**
     * Obtains the total number of pages that span columns and rows
     * @param dgrmEP
     * @return Point.x contains the total number of pages that span in a column
     *         Point.y contains the total number of pages that span in a row
     */
    private org.eclipse.draw2d.geometry.Point getPageCount(DiagramEditPart dgrmEP, Rectangle figureBounds, org.eclipse.draw2d.geometry.Point pageSize, boolean applyUserScale) {
        RootEditPart rootEditPart = dgrmEP.getRoot();
        if (rootEditPart instanceof DiagramRootEditPart) {
            //this is the most accurate way to obtain total rows and columns...
            
            DiagramRootEditPart diagramRootEditPart = (DiagramRootEditPart) rootEditPart;
            PageBreakEditPart pageBreakEditPart = diagramRootEditPart
                .getPageBreakEditPart();
            float fNumCols = ((PageBreaksFigure)pageBreakEditPart.getFigure()).getPageCount().y * 
                (applyUserScale ? userScale : 1);
            float fNumRows = ((PageBreaksFigure)pageBreakEditPart.getFigure()).getPageCount().x * 
                (applyUserScale ? userScale : 1);
            
            int numCols = (int) Math.ceil(fNumCols);
            int numRows = (int) Math.ceil(fNumRows);
            
            return new org.eclipse.draw2d.geometry.Point(numCols,numRows);
        }
        else {
            //otherwise, calculate manually...
            
            float fNumRows = (figureBounds.height * (applyUserScale ? userScale : 1))
                / pageSize.y;
            int numRows = (int) Math.ceil(fNumRows);

            float fNumCols = (figureBounds.width * (applyUserScale ? userScale : 1))
                / pageSize.x;
            int numCols = (int) Math.ceil(fNumCols);
            
            return new org.eclipse.draw2d.geometry.Point(numCols, numRows);
        }
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
        
        //check for rtl orientation...
        int style = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getStyle();
        if ((style & SWT.MIRRORED) != 0)
            this.gc = new GC(printer, SWT.RIGHT_TO_LEFT);
        else
            this.gc = new GC(printer);

        gc.setXORMode(false);

        this.swtGraphics = new SWTGraphics(gc);
        this.printerGraphics = createPrinterGraphics(swtGraphics);
        this.graphics = createMapModeGraphics(printerGraphics);
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
            offsetX = (int) (getMapMode()
                .DPtoLP((int) (offsetX / 2.0f * display_dpi.x / printer.getDPI().x)) / userScale);
            offsetY = (int) (getMapMode()
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
        org.eclipse.draw2d.geometry.Point pageBounds = PageInfoHelper.getPageSize(fPreferences, getMapMode());

        //translate to offset initial figure position
        translated = new Point((int) (-figureBounds.x * userScale), (int) (-figureBounds.y * userScale));
        
        //calculate the number of page rows and columns
        int numRows = 0, numCols = 0;
        
        PageMargins margins = PageInfoHelper.getPageMargins(fPreferences, getMapMode());
        adjustMargins(margins, userScale, getPrinterOffset());

        GC gc_ = new GC(Display.getDefault(),this.gc.getStyle());
        gc_.setAntialias(this.gc.getAntialias());

        FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];
        Font font = new Font(printer, fontData);
        
        org.eclipse.draw2d.geometry.Point pageCount = getPageCount(dgrmEP, figureBounds, pageBounds, true);
        numCols = pageCount.x;
        numRows = pageCount.y;

        //finalRow and finalColumn will be used if we are printing within a page range...
        int row = 1, col = 1, finalRow = 0, finalColumn = 0;
        
        if (this.printRangePageSelection) {
            //print only the pages specified in the page range...
            row = calculateRowFromPage(this.pageFrom, numCols);
            col = calculateColumnFromPage(this.pageFrom, numCols, row);
            
            finalRow = calculateRowFromPage(this.pageTo, numCols);
            finalColumn = calculateColumnFromPage(this.pageTo, numCols, finalRow);
        }
        
        try {
            //print the pages in row, column order
            for (; row <= numRows; row++) {
                for (; col <= numCols; col++) {
                    printer.startPage();
                    drawPage(gc_, dgrmEP, fPreferences, figureBounds, margins, font, row, col);
                    printer.endPage();
                    
                    if (row == finalRow && col == finalColumn && this.printRangePageSelection == true)
                        break;
                }
                
                if (row == finalRow && col == finalColumn && this.printRangePageSelection == true)
                    break;
                
                col = 1;
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
            getMapMode().DPtoLP(HeaderAndFooterHelper.LEFT_MARGIN_DP)
                + (width - getMapMode().DPtoLP(gc_.textExtent(headerOrFooter).x))
                / 2, getMapMode().DPtoLP(HeaderAndFooterHelper.TOP_MARGIN_DP));

        headerOrFooter = HeaderAndFooterHelper.makeHeaderOrFooterString(
            WorkspaceViewerProperties.FOOTER_PREFIX, rowIndex, colIndex,
            dgrmEP);

        this.graphics.drawText(headerOrFooter,
            getMapMode().DPtoLP(HeaderAndFooterHelper.LEFT_MARGIN_DP)
                + (width - getMapMode().DPtoLP(gc_.textExtent(headerOrFooter).x))
                / 2, height - getMapMode().DPtoLP(HeaderAndFooterHelper.BOTTOM_MARGIN_DP));

        this.graphics.popState(); //for drawing the text

    }
    
    /**
     * This method paints a portion of the diagram. (The area painted
     * representing one page.)
     * 
     * @param gc_ a graphics context that is not null which this method will use
     * for figuring out the font's extent
     * @param dgrmEP the DiagramEditPart that will be printed
     * @param fPreferences the preferenceStore that could either contain
     * existing preferences or defaults
     * @param figureBounds the page break bounds we'll have to offset by
     * @param font the Font to print the header or footer with
     * @param rowIndex index of row we're printing
     * @param colIndex index of column we're priniting
     * to check if it is the first time the method is getting called for the current
     * print.
     */
    protected void drawPage(GC gc_, DiagramEditPart dgrmEP,
            IPreferenceStore fPreferences, Rectangle figureBounds,
            PageMargins margins, Font font, int rowIndex, int colIndex) {

        org.eclipse.draw2d.geometry.Point pageSize = PageInfoHelper
            .getPageSize(fPreferences, false, getMapMode());
        boolean rtlEnabled = ((this.gc.getStyle() & SWT.MIRRORED) != 0);
        if (rtlEnabled) 
        {
            // draw everything on an offscreen image first and then draw that image
            // onto the printer gc...this takes care of certain drawing bugs.
            // This causes issues with printing resolution as it uses a display image
            // which is typically 72dpi
            // This code should be removed when a resolution to Bugzilla 162459 is found

            Image image = new Image(Display.getDefault(), getMapMode().LPtoDP(pageSize.x), getMapMode().LPtoDP(pageSize.y));

            GC imgGC = new GC(image, (rtlEnabled) ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT);
            imgGC.setXORMode(false);
      
            SWTGraphics sg = new SWTGraphics(imgGC);
              
            //for scaling
            ScaledGraphics g1 = new ScaledGraphics(sg);
          
            //for himetrics and svg
            MapModeGraphics mmg = createMapModeGraphics(g1);
              
            //if mmg's font is null, gc.setFont will use a default font
            imgGC.setFont(mmg.getFont());
              
            internalDrawPage(dgrmEP,figureBounds,fPreferences,margins,mmg,rowIndex, colIndex,true);
            
            this.graphics.pushState();
        
            this.graphics.drawImage(image, 0, 0);
              
            this.graphics.popState();

            //draw the header and footer after drawing the image to avoid getting the image getting drawn over them
            drawHeaderAndFooter(gc_, dgrmEP, figureBounds, font, rowIndex, colIndex);
            disposeImageVars(imgGC, image, sg, g1, mmg);
        } else {
            internalDrawPage(dgrmEP,figureBounds,fPreferences,margins,this.graphics,rowIndex, colIndex,false);
            //draw the header and footer after drawing the image to avoid getting the image getting drawn over them
            drawHeaderAndFooter(gc_, dgrmEP, figureBounds, font, rowIndex, colIndex);
        }
    }
    
    private void internalDrawPage(DiagramEditPart dgrmEP,
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
            (scaledWidth - margins.left - margins.right) * (colIndex - 1)
                + figureBounds.x, (scaledHeight - margins.bottom - margins.top)
                * (rowIndex - 1) + figureBounds.y, scaledWidth - margins.right
                - margins.left, scaledHeight - margins.top - margins.bottom);
        g.clipRect(clip);

        dgrmEP.getLayer(LayerConstants.PRINTABLE_LAYERS).paint(g);

        g.popState();
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
        
        PageMargins margins = PageInfoHelper.getPageMargins(fPreferences, getMapMode());
        //do not include margins
        org.eclipse.draw2d.geometry.Point pageBounds = PageInfoHelper
            .getPageSize(fPreferences, getMapMode());
        org.eclipse.draw2d.geometry.Point pageCount = getPageCount(dgrmEP, figureBounds, pageBounds, false);
        int numCols = pageCount.x;
        int numRows = pageCount.y;
        
        float actualWidth = 0;
        float actualHeight = 0;
        if (this.rows==1 && this.columns==1 && fitToPage){
        	figureBounds = dgrmEP.getChildrenBounds();
            actualWidth = figureBounds.width;
            actualHeight = figureBounds.height;
        }else {
            actualWidth = numCols * pageBounds.x;
            actualHeight = numRows * pageBounds.y;
        }

        int totalHeight = (this.rows * pageBounds.y);
        int totalWidth  = (this.columns * pageBounds.x);

        float vScale =  totalHeight / actualHeight;
        float hScale = totalWidth / actualWidth;

        this.userScale = Math.min(hScale, vScale);

        // translate to offset figure position
        translated = new Point((int) (-figureBounds.x * userScale),
            (int) (-figureBounds.y * userScale));

        adjustMargins(margins, userScale, getPrinterOffset());

        GC gc_ = new GC(Display.getDefault());

        FontData fontData = JFaceResources.getDefaultFont().getFontData()[0];
        Font font = new Font(printer, fontData);

        int row = 1, col = 1, finalRow = 0, finalColumn = 0;
        
        if (this.printRangePageSelection) {
            //print only the pages specified in the page range
            //this corresponds to the physical pages, not the print range of pages on one physical page.
            row = calculateRowFromPage(this.pageFrom, this.columns);
            col = calculateColumnFromPage(this.pageFrom, this.columns, row);
            
            finalRow = calculateRowFromPage(this.pageTo, this.columns);
            finalColumn = calculateColumnFromPage(this.pageTo, this.columns, finalRow);
        }
        
        try {
            // print the pages in row, column order
            for (; row <= rows; row++) {
                for (; col <= columns; col++) {
                    printer.startPage();
                    drawPage(gc_, dgrmEP, fPreferences, figureBounds, margins,
                        font, row, col);
                    printer.endPage();
                    
                    if (row == finalRow && col == finalColumn && this.printRangePageSelection == true)
                        break;
                }
                
                if (row == finalRow && col == finalColumn && this.printRangePageSelection == true)
                    break;
                
                col = 1;
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
        if (this.graphics != null) {
            try {
                this.graphics.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                this.graphics = null;                
            }
        }
        
        if (this.printerGraphics != null) {
            try {
                this.printerGraphics.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                this.printerGraphics = null;
            }
        }
        
        if (this.swtGraphics != null) {
            try {
                this.swtGraphics.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                this.swtGraphics = null;
            }
        }
        
        if (this.gc != null) {
            try {
                this.gc.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                this.gc = null;
            }
        }
        
        //reset the printer offset, just in case the next diagram to be printed 
        //uses a different map mode.
        printerOffset = null;
        
    }
    
    private void disposeImageVars(GC imgGC, Image image, SWTGraphics sg, 
            ScaledGraphics g1, MapModeGraphics mmg) {
        
        if (mmg != null) {
            try {
                mmg.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                mmg = null;                
            }
        }
        
        if (g1 != null) {
            try {
                g1.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                g1 = null;                
            }
        }
        
        if (sg != null) {
            try {
                sg.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                sg = null;                
            }
        }
        
        if (imgGC != null) {
            try {
                imgGC.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                imgGC = null;                
            }
        }
        
        if (image != null) {
            try {
                image.dispose();
            }
            catch (NullPointerException e) {
                //do nothing
            }
            finally {
                image = null;                
            }
        }
    }

    /**
     * Creates the <code>MapModeGraphics</code>.
     * 
     * @param theGraphics
     *            the <code>PrinterGraphics</code> object
     * @return the new <code>MapModeGraphics</code>
     */
    protected MapModeGraphics createMapModeGraphics(Graphics theGraphics) {
        return new MapModeGraphics(theGraphics, getMapMode());
    }
    
    /**
     * Creates the <code>PrinterGraphics</code>.
     * 
     * @param theGraphics
     *          the <code>Graphics</code> object
     * @return the new <code>PrinterGraphics</code>
     */
    protected PrinterGraphics createPrinterGraphics(Graphics theGraphics) {
        return new PrinterGraphics(theGraphics, printer, true);
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