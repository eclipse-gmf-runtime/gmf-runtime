/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import java.awt.geom.Point2D;

import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;

/**
 * The following page types are supported by Page Setup Dialog:
 * { LETTER, LEBAL, EXECUTIVE, 11x17, A3, A4, B4, B5, USER_DEFINED }
 * 
 * @author etworkow
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class PageSetupPageType {

	private String fName;
	private Point2D.Double fDimension;
	private int fIndex;
	
	/** Represents LETTER page size. */
	public static PageSetupPageType LETTER = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.letter"), new Point2D.Double(8.5, 11), 0); //$NON-NLS-1$
	
	/** Represents LEGAL page size. */
	public static PageSetupPageType LEGAL = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.legal"), new Point2D.Double(8.5, 14), 1);//$NON-NLS-1$
	
	/** Represents EXECUTIVE page size. */
	public static PageSetupPageType EXECUTIVE = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.executive"), new Point2D.Double(7.25, 10.5), 2);//$NON-NLS-1$
	
	/** Represents 11x17 page size. */
	public static PageSetupPageType llX17 = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.11by17"), new Point2D.Double(11, 17), 3);//$NON-NLS-1$
	
	/** Represents A3 page size. */
	public static PageSetupPageType A3 = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.A3"), new Point2D.Double(11.69, 16.54), 4);//$NON-NLS-1$
	
	/** Represents A4 page size. */
	public static PageSetupPageType A4 = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.A4"), new Point2D.Double(8.268, 11.69), 5);//$NON-NLS-1$
	
	/** Represents B4 page size. */
	public static PageSetupPageType B4 = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.B4"), new Point2D.Double(14.33, 10.12), 6);//$NON-NLS-1$
	
	/** Represents B5 page size. */
	public static PageSetupPageType B5 = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.B5"), new Point2D.Double(7.165, 10.12), 7);//$NON-NLS-1$
	
	/** Represents USER_DEFINED page size. */
	public static PageSetupPageType USER_DEFINED = new PageSetupPageType
		(PresentationResourceManager.getInstance().getString("PageSetupDialog.paper.size.userDefined"), new Point2D.Double(0.0, 0.0), 8);//$NON-NLS-1$
	
	/**
	 * Creates an instance of PageSetupPageType.
	 * 
	 * @param name String name of the page size
	 * @param dimension Point2D.Double dimension of page (height and width)
	 * @param index int index of page size
	 */
	private PageSetupPageType(String name, Point2D.Double dimension, int index) {
		fName = name;
		fDimension = dimension;
		fIndex = index;
	}
	
	/**
	 * Get page size name.
	 * 
	 * @return Strin name of page size, ex. "Letter"
	 */
	public String getName() {
		return fName;
	}
	
	/**
	 * Get width of page.
	 * 
	 * @return double page width
	 */
	public double getWidth() {
		return fDimension.x;
	}
	
	/**
	 * Get height of page.
	 * 
	 * @return double page height
	 */
	public double getHeight() {
		return fDimension.y;
	}
	
	/**
	 * Get index of page.
	 * 
	 * @return double page width
	 */
	public int getIndex() {
		return fIndex;
	}
	
	/**
	 * List of all supported page sizes.
	 */
	public static PageSetupPageType[] pages = {
		LETTER,
		LEGAL,
		EXECUTIVE,
		llX17,
		A3,
		A4,
		B4,
		B5,
		USER_DEFINED
	};
}
