/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter;

import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

/**
 * Wrapper for creating <code>TableColumn</code> column information.
 * 
 * @author jcorchis
 */
public class SortFilterCollectionColumn {

	/** Column infomation with default values */
	private int alignment = SWT.LEFT;
	private int width = 80;
	private boolean resizable = true;
	private String caption;
	private ViewerSorter columnSorter;

	/**
	 * Constructor for the <code>SortFilterCollectionColumn</code>. All instances 
	 * must have a column label.
	 * @param caption the column caption
	 */
	public SortFilterCollectionColumn(String caption) {
		this.caption = caption;
	}

	/**
	 * Constructor for the <code>SortFilterCollectionColumn</code>.
	 * @param caption the column caption
	 * @param sorter the sorter
	 */
	public SortFilterCollectionColumn(String caption, ViewerSorter sorter) {
		this.caption = caption;
		this.columnSorter = sorter;
	}

	/**
	 * SortFilterCollectionColumn constructor with the specified
	 * column attributes.
	 * @param caption the column name
	 * @param alignment the column's alignment
	 * @param width the column's width in pixels
	 * @param sorter the sorter
	 */
	public SortFilterCollectionColumn(
		String caption,
		int alignment,
		int width,
		ViewerSorter sorter) {
		this.caption = caption;
		this.alignment = alignment;
		this.width = width;
		this.columnSorter = sorter;
	}

	/**
	 * Gets the column's display name
	 * @return String the column's display name
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Gets the horizontal alignment type
	 * @return int the horizontal alignment type
	 */
	public int getAlignment() {
		return alignment;
	}

	/**
	 * Gets the width column's width.
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the horizontal alignment type. Use SWT
	 * @param alignment the horizontal alignment type
	 */
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	/**
	 * Determines if column is resizable
	 * @return boolean <code>true</code> if column is resizable, otherwise <code>false</code>
	 */
	public boolean isResizable() {
		return resizable;
	}

	/** 
	 * Returns a sorter which will sort the table by this column.
	 * @return the <code>ViewerSorter</code> which knows how to sort the <code>Table</code>
	 * by this column.
	 */
	public ViewerSorter getColumnSorter() {
		return columnSorter;
	}

	/** 
	 * Uses the getCaption() to compare the equalvalence of the objects.
	 * @return true if the <code>SortFilterCollectionColumn</code> have the same name.
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof SortFilterCollectionColumn)) {
			return false;
		}

		return getCaption().equals(
			((SortFilterCollectionColumn) obj).getCaption());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getCaption().hashCode();
	}
}
