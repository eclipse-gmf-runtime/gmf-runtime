/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;

/**
 * The following page types are supported by Page Setup Dialog:
 * { LETTER, LEBAL, EXECUTIVE, 11x17, A3, A4, B4, B5, USER_DEFINED }
 * 
 * @author etworkow
 */
public class PageSetupPageType {

	private String fName;
	private Point2D.Double fDimension;
	private int fIndex;
	
	/** Represents LETTER page size. */
	public static PageSetupPageType LETTER = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_letter, new Point2D.Double(8.5, 11), 0); 
	
	/** Represents LEGAL page size. */
	public static PageSetupPageType LEGAL = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_legal, new Point2D.Double(8.5, 14), 1);
	
	/** Represents EXECUTIVE page size. */
	public static PageSetupPageType EXECUTIVE = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_executive, new Point2D.Double(7.25, 10.5), 2);
	
	/** Represents 11x17 page size. */
	public static PageSetupPageType llX17 = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_11by17, new Point2D.Double(11, 17), 3);
	
	/** Represents A3 page size. */
	public static PageSetupPageType A3 = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_A3, new Point2D.Double(11.6929134d, 16.5354331d), 4);
	
	/** Represents A4 page size. */
	public static PageSetupPageType A4 = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_A4, new Point2D.Double(8.26771654d, 11.6929134d), 5);
	
	/** Represents B4 page size. */
	public static PageSetupPageType B4 = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_B4, new Point2D.Double(9.84251969d, 13.8976378d), 6);
	
	/** Represents B5 page size. */
	public static PageSetupPageType B5 = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_B5, new Point2D.Double(6.92913386d, 9.84251969d), 7);
	
	/** Represents USER_DEFINED page size. */
	public static PageSetupPageType USER_DEFINED = new PageSetupPageType
		(DiagramUIMessages.PageSetupDialog_paper_size_userDefined, new Point2D.Double(0.0, 0.0), 8);
	
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
