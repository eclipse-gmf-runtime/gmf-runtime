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

package org.eclipse.gmf.runtime.diagram.ui.printing.render.model;


/**
 * This class is used as part of the infrastructure required for data-bindings
 * used with the JPS dialog.
 *
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
public class PrintOptions extends PrintModelElement {
    public static String PROPERTY_DESTINATION = "destination"; //$NON-NLS-1$
    
    public static String PROPERTY_PERCENT_SCALING = "percentScaling"; //$NON-NLS-1$
    public static String PROPERTY_SCALE_FACTOR = "scaleFactor"; //$NON-NLS-1$
    public static String PROPERTY_FIT_TO_WIDTH = "fitToPagesWidth"; //$NON-NLS-1$
    public static String PROPERTY_FIT_TO_HEIGHT = "fitToPagesHeight"; //$NON-NLS-1$
    
    public static String PROPERTY_ALL_PAGES = "allPages"; //$NON-NLS-1$
    public static String PROPERTY_RANGE_FROM = "rangeFrom"; //$NON-NLS-1$
    public static String PROPERTY_RANGE_TO = "rangeTo"; //$NON-NLS-1$
    
    public static String PROPERTY_COPIES = "copies"; //$NON-NLS-1$
    public static String PROPERTY_COLLATE = "collate"; //$NON-NLS-1$

    private PrintDestination destination;
    
    private boolean percentScaling;
    private int scaleFactor;
    private int fitToPagesWidth;
    private int fitToPagesHeight;
    
    private boolean allPages;
    private int rangeFrom;
    private int rangeTo;
    
    private int copies;
    private boolean collate;
    
    public PrintOptions() {
        super();
    }

    public PrintDestination getDestination() {
        return destination;
    }
    
    public void setDestination(PrintDestination destination) {
        PrintDestination oldDestination = this.destination;
        this.destination = destination;
        firePropertyChange(PROPERTY_DESTINATION, oldDestination, destination);
    }
    
    public boolean isPercentScaling() {
        return percentScaling;
    }
    
    public void setPercentScaling(boolean percentScaling) {
        boolean oldScaling = this.percentScaling;
        this.percentScaling = percentScaling;
        firePropertyChange(PROPERTY_PERCENT_SCALING, oldScaling, percentScaling);
    }
    
    public int getScaleFactor() {
        return scaleFactor;
    }
    
    public void setScaleFactor(int scaleFactor) {
        int oldFactor = this.scaleFactor;
        this.scaleFactor = scaleFactor;
        firePropertyChange(PROPERTY_SCALE_FACTOR, oldFactor, scaleFactor);
    }
    
    public int getFitToPagesWidth() {
        return fitToPagesWidth;
    }
    
    public void setFitToPagesWidth(int fitToPagesWidth) {
        int oldWidth = this.fitToPagesWidth;
        this.fitToPagesWidth = fitToPagesWidth;
        firePropertyChange(PROPERTY_FIT_TO_WIDTH, oldWidth, fitToPagesWidth);
    }

    public int getFitToPagesHeight() {
        return fitToPagesHeight;
    }
    
    public void setFitToPagesHeight(int fitToPagesHeight) {
        int oldHeight = this.fitToPagesHeight;
        this.fitToPagesHeight = fitToPagesHeight;
        firePropertyChange(PROPERTY_FIT_TO_HEIGHT, oldHeight, fitToPagesHeight);
    }

    public boolean isAllPages() {
        return allPages;
    }
    
    public void setAllPages(boolean allPages) {
        boolean oldAll = this.allPages;
        this.allPages = allPages;
        firePropertyChange(PROPERTY_ALL_PAGES, oldAll, allPages);
    }

    public int getRangeFrom() {
        return rangeFrom;
    }
    
    public void setRangeFrom(int rangeFrom) {
        int oldFrom = this.rangeFrom;
        this.rangeFrom = rangeFrom;
        firePropertyChange(PROPERTY_RANGE_FROM, oldFrom, rangeFrom);
    }
    
    public int getRangeTo() {
        return rangeTo;
    }
    
    public void setRangeTo(int rangeTo) {
        int oldTo = this.rangeTo;
        this.rangeTo = rangeTo;
        firePropertyChange(PROPERTY_RANGE_TO, oldTo, rangeTo);
    }
    
    public int getCopies() {
        return copies;
    }
    
    public void setCopies(int copies) {
        int oldCopies = this.copies;
        this.copies = copies;
        firePropertyChange(PROPERTY_COPIES, oldCopies, copies);
    }
    
    public boolean isCollate() {
        return collate;
    }
    
    public void setCollate(boolean collate) {
        boolean oldCollate = this.collate;
        this.collate = collate;
        firePropertyChange(PROPERTY_COLLATE, oldCollate, collate);
    }
}
