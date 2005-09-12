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

package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * Helper to get information about a page.  Used by page breaks and print
 * preview.
 * 
 * @author Wayne Diu, wdiu
 * @canBeSeenBy %level1
 */
public class PageInfoHelper {
    /**
     * Calculate the diagram bounds excluding the page breaks figure
     * 
     * @param diagramEditPart, the diagram edit part containing the diagram
     * we are calculating the bounds for.
     * @param pageBreakClass, figures of this class should be ignored
     * 
     * @return rectangle bounds of the diagram's children
     */
    public static Rectangle getChildrenBounds(DiagramEditPart diagramEditPart, Class pageBreakClass) {
        Rectangle diagramRec = null;
        Point location = new Point();
        IFigure f = diagramEditPart.getContentPane();
        List list = f.getChildren();

        if (java.util.Collections.EMPTY_LIST == list) {
            return new Rectangle();
        }
        boolean init = true;
        for (int i = 0; i < list.size(); i++) {
            Figure childFigure = (Figure) list.get(i);
            if (pageBreakClass != null && pageBreakClass.isInstance(childFigure)) {
                continue;
            }

            Rectangle r = childFigure.getBounds();
            Point childLocation = childFigure.getLocation();
            if (init) {
                location.x = childLocation.x;
                location.y = childLocation.y;
                diagramRec = r;
                init = false;
            }

            diagramRec = diagramRec.getUnion(r);

            if (childLocation.x < location.x) {
                location.x = childLocation.x;
            }
            if (childLocation.y < location.y) {
                location.y = childLocation.y;
            }

        }

        if (diagramRec == null) {
            // The diagram is empty
            return new Rectangle();
        } else {
            diagramRec.x = location.x;
            diagramRec.y = location.y;
        }

        return diagramRec;
    }
    

    /**
     * Utility method that calculate the printer page size.      
     * @return point the page size point.x == width, point.y == height
     */
    public static Point getPageSize(IPreferenceStore store) {
    	return getPageSize(store, true);
    }
    
    /**
     * Utility method that calculate the printer page size.      
     * @return point the page size point.x == width, point.y == height
     */
    public static Point getPageSize(IPreferenceStore store, boolean subtractMargins) {

		double paperSizeWidth =
			store.getDouble(WorkspaceViewerProperties.PREF_PAGE_WIDTH);
		double paperSizeHeight =
			store.getDouble(WorkspaceViewerProperties.PREF_PAGE_HEIGHT);
		double leftMargin =
			store.getDouble(WorkspaceViewerProperties.PREF_MARGIN_LEFT);
		double topMargin =
			store.getDouble(WorkspaceViewerProperties.PREF_MARGIN_TOP);
		double bottomMargin =
			store.getDouble(WorkspaceViewerProperties.PREF_MARGIN_BOTTOM);
		double rightMargin =
			store.getDouble(WorkspaceViewerProperties.PREF_MARGIN_RIGHT);

		//if (nnn
		//	.LANDSCAPE
		//	.equals(
		//		store.getString(WorkspaceViewerProperties.PAGE_ORIENTATION))) {
		//	double temp = paperSizeWidth;
		//	paperSizeWidth = paperSizeHeight;
		//	paperSizeHeight = temp;
		//}

		if (store.getBoolean(WorkspaceViewerProperties.PREF_USE_LANDSCAPE)) {
			double temp = paperSizeWidth;
			paperSizeWidth = paperSizeHeight;
			paperSizeHeight = temp;
		}
		// inches
		double width = paperSizeWidth;
		double height = paperSizeHeight;
		
		if (subtractMargins) {
			width -= (leftMargin + rightMargin);
			height -= (topMargin + bottomMargin);
		}

		int[] paperSize = { 0, 0 };
		org.eclipse.swt.widgets.Display display = Display.getDefault();
		org.eclipse.swt.graphics.Point displayDPI = display.getDPI();
		paperSize[0] = MapMode.DPtoLP((int)(width * displayDPI.x));
		paperSize[1] = MapMode.DPtoLP((int)(height * displayDPI.y));

		return new Point(paperSize[0], paperSize[1]);
    }
    
    /**
     * Returns a page type (e.g. PSPageType.A4) based on the 
     * size of the page in the default printer.  If no printer is
     * installed a default of PSPageType.LETTER is returned.
     * 
     * @return String the default page size name
     */    
    static public String getPrinterPageType() {
    	
    	//String pageType = nnn.LETTER;
    	String pageType = PageSetupPageType.LETTER.getName();
    	
    	Printer printer = null;

		try {
			printer = new Printer();
		} catch (SWTError e) {
			//I cannot printer.dispose(); because it may not have been
			//initialized
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
				PageInfoHelper.class, "getPrinterPage", //$NON-NLS-1$
				e);

			if (e.code == SWT.ERROR_NO_HANDLES) {
				//it might have really been ERROR_NO_HANDLES, but there's
				//no way for me to really know
				return pageType;
			}

			//if (e.code != SWT.ERROR_NO_HANDLES)
			Log.error(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.GENERAL_UI_FAILURE,
				"Failed to make instance of Printer object", e); //$NON-NLS-1$

			//else if another swt error
			Trace.throwing(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
				PageInfoHelper.class, "getPrinterPage", //$NON-NLS-1$
				e);
			throw e;
		}
    	
    	if (printer != null) {
    		
    		// Physical page size in pixels
    		org.eclipse.swt.graphics.Rectangle physicalSize = printer.getBounds();
    		org.eclipse.swt.graphics.Point dpi = printer.getDPI();
    		
    		// Convert physical size to inches.
    		Point2D.Double size = new Point2D.Double(physicalSize.width / dpi.x, physicalSize.height / dpi.y);
    		
    		Map pageTypes = getPaperSizesMap();
    		Iterator iterator = pageTypes.keySet().iterator();
    		boolean first = true;
    		double bestFit = 0.0;
    		
    		while(iterator.hasNext()) {
    			
    			String thisPageType = (String)iterator.next();
    			Point2D.Double thisSize = (Point2D.Double)pageTypes.get(thisPageType);
    			
    			if (first) {
    				bestFit = size.distance(thisSize);
    				pageType = thisPageType;
    				first = false;
    				continue;
    			}
    			
    			double thisFit = size.distance(thisSize);
    			if (thisFit < bestFit) {
    				bestFit = thisFit;
    				pageType = thisPageType;
    			}
    			
    		}
    		printer.dispose();	    		
    	}     	   	
    	
    	return pageType;
    }
    
	private static final Map pageSizes = new HashMap();

	static {
		pageSizes.put(PageSetupPageType.LETTER, new Point2D.Double(8.5, 11));
		pageSizes.put(PageSetupPageType.LEGAL, new Point2D.Double(8.5, 14));
		pageSizes.put(PageSetupPageType.EXECUTIVE, new Point2D.Double(7.25, 10.5));
		pageSizes.put(PageSetupPageType.llX17, new Point2D.Double(11, 17));
		pageSizes.put(PageSetupPageType.A3, new Point2D.Double(11.69, 16.54));
		pageSizes.put(PageSetupPageType.A4, new Point2D.Double(8.268, 11.69));
		pageSizes.put(PageSetupPageType.B4, new Point2D.Double(14.33, 10.12));
		pageSizes.put(PageSetupPageType.B5, new Point2D.Double(7.165, 10.12));
	}
	
	public static final Map getPaperSizesMap() {
		return pageSizes;
	}
	
	public static final class PageMargins {
		public int left;
		public int right;
		public int top;
		public int bottom;
	}	
	
	
	public static PageMargins getPageMargins(IPreferenceStore preferenceStore) {
		assert Display.getDefault() != null;
		
		org.eclipse.swt.graphics.Point displayDPI = Display.getDefault().getDPI();
		PageMargins margins = new PageMargins();
		margins.left = MapMode
		.DPtoLP((int) ( displayDPI.x * preferenceStore
			.getDouble(WorkspaceViewerProperties.PREF_MARGIN_LEFT)));
		margins.right = MapMode
		.DPtoLP((int) ( displayDPI.x * preferenceStore
			.getDouble(WorkspaceViewerProperties.PREF_MARGIN_RIGHT)));
		margins.top = MapMode
		.DPtoLP((int) ( displayDPI.y * preferenceStore
			.getDouble(WorkspaceViewerProperties.PREF_MARGIN_TOP)));
		margins.bottom = MapMode
		.DPtoLP((int) ( displayDPI.y * preferenceStore
			.getDouble(WorkspaceViewerProperties.PREF_MARGIN_BOTTOM)));
		
		return margins;
		
	}
}
