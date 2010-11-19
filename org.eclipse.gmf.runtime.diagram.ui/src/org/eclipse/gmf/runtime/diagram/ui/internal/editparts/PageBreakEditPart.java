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

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.PageBreakNonResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.PageBreaksFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Edit part which controls the page breaks figure. 
 * 
 * @author jcorchis
 */
public class PageBreakEditPart extends AbstractGraphicalEditPart {

	/* Figure associated with this graphic edit part */
	private PageBreaksFigure pageOutlineFigure;

	/* The size for the entire diagram 	 */
	private Rectangle diagramBounds = new Rectangle();

//	/* Attribute used to store the page breaks */
//	private Rectangle pageBreakBounds;

	/**
	 * Method to update this the bounds of the shapes
	 * on the diagram.
	 * @param bounds the bounds of the shapes on the diagram
	 */
	public void set(Point location, Rectangle bounds) {
		
		if (bounds.x < location.x || bounds.y < location.y) {
			// Figure's have been moved to the left or above the 
			// the page breaks figure's location
			calculatePageBreakFigureBounds(true, bounds.getSize());
		} else {
			// Calculate the number of pages needed to contain
			// the diagram
			int width = Math.abs(bounds.right() - location.x);
			int height = Math.abs(bounds.bottom() - location.y);
			
			updatePageCount(width, height);			
			
			getPageBreaksFigure().setLocation(location);
			
			
		}	
	}

	private void updatePageCount(int width, int height) {
		Point printerPageSize = getPageSize();
		float numCols = ((float) width) / printerPageSize.x;
		int cols = Math.max(1, (int) Math.ceil(numCols));
		float numRows = ((float) height) / printerPageSize.y;
		int rows = Math.max(1, (int) Math.ceil(numRows));

		int xInc = printerPageSize.x * cols;
		int yInc = printerPageSize.y * rows;

		getPageBreaksFigure().setSize(new Dimension(xInc, yInc));
		getPageBreaksFigure().setPageCount(rows, cols);
	}
	
	/**
	 * Calculates the size of the page breaks figure and the
	 * number of rows and columns based on the print page size.
	 * This method updates the page breaks figure bounds directly.
	 * 
	 * @param center true to center the page breaks figure,
	 * false to not center it.
	 */
	public void calculatePageBreakFigureBounds(boolean center) {
		calculatePageBreakFigureBounds(center, diagramBounds.getSize());
	}
	
	/**
	 * Calculates the size of the page breaks figure and the
	 * number of rows and columns based on the print page size.
	 * This method will also center the page breaks figure.
	 */
	public void calculatePageBreakFigureBounds() {
		calculatePageBreakFigureBounds(true, diagramBounds.getSize());
	}	
	
	/**
	 * Calculates the size of the page breaks figure and the
	 * number of rows and columns based on the print page size.
	 * This method updates the page breaks figure bounds directly.
	 * 
	 * @param center true to center the page breaks figure,
	 * false to not center it.
	 */
	private void calculatePageBreakFigureBounds(boolean center, Dimension bounds) {
		updatePageCount(bounds.width, bounds.height);
		
		if (center) {
			centerPageBreaksFigure();
		}
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractGraphicEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		pageOutlineFigure = new PageBreaksFigure(PageBreaksFigure.FIGURE);
		pageOutlineFigure.setCursor(Cursors.SIZEALL);
		return pageOutlineFigure;
	}
	
	private PageBreaksFigure getPageBreaksFigure() {
		if (pageOutlineFigure == null) {
			createFigure();
		}
		return pageOutlineFigure;
	}

	/**
	 * @see com.ibm.etools.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(
			EditPolicy.PRIMARY_DRAG_ROLE,
			new PageBreakNonResizableEditPolicy());
	}
	
	/**
	 * Utility method that calculate the printer page size.      
	 * @return point the page size point.x == width, point.y == height
	 */
	public Point getPageSize() {
		IPreferenceStore s = getPreferenceStoreForPageSetup();
		
		IMapMode mm = MapModeUtil.getMapMode();
		RootEditPart drEP = getRoot();
		if (drEP instanceof DiagramRootEditPart) {
			mm = ((DiagramRootEditPart)drEP).getMapMode();
		}
		
		return PageInfoHelper.getPageSize(s, mm);
	}

	/**
	 * Utility method to set the page breaks figure such that the 
	 * all the diagram shapes appear centered within the page breaks
	 * figure.
	 */
	private void centerPageBreaksFigure() {

		Rectangle pageBreakBounds = getPageBreaksFigure().getBounds();

		Rectangle r = diagramBounds;
		Point offset =
			new Point(
				((pageBreakBounds.width - diagramBounds.width) / 2),
				((pageBreakBounds.height - diagramBounds.height) / 2));
		Rectangle centeredRec =
			new Rectangle(
				r.x - offset.x,
				r.y - offset.y,
				pageBreakBounds.width,
				pageBreakBounds.height);
		getPageBreaksFigure().setBounds(centeredRec);
		updatePreferenceStore();
	}

	/**
	 * Resizes the page outline figure based on changes to the diagram's children 
	 * @param bounds the bounds of the figure 
	 */
	public void resize(Rectangle newBounds) {
		Rectangle pageBreakBounds = getFigure().getBounds();
		Point pageSize = getPageSize();

		// Change in the EAST/WEST directions
		if (newBounds.x != diagramBounds.x
			|| newBounds.width != diagramBounds.width) {

			if (newBounds.x < diagramBounds.x
				|| newBounds.right() < diagramBounds.right()) {

				// Move WEST: add cols to the WEST
				if (newBounds.x < pageBreakBounds.x) {
					int effectivewidth =
						Math.abs(pageBreakBounds.right() - newBounds.right())
							+ newBounds.width;
					float cols = ((float) effectivewidth) / pageSize.x;
					int requiredCols = (int) Math.ceil(Math.abs(cols));
					int xOffset = Math.round(requiredCols * pageSize.x);

					getPageBreaksFigure().setCols(requiredCols);
					pageBreakBounds = getPageBreaksFigure().getBounds();
					getPageBreaksFigure().setBounds(
						new Rectangle(
							pageBreakBounds.right() - xOffset,
							pageBreakBounds.y,
							xOffset,
							pageBreakBounds.height));
				}

				// Remove cols from the EAST? 
				pageBreakBounds = getPageBreaksFigure().getBounds();
				int effectiveWidth =
					Math.abs(newBounds.x - pageBreakBounds.x) + newBounds.width;
				float cols = ((float) effectiveWidth) / pageSize.x;
				int requiredCols = (int) Math.ceil(Math.abs(cols));
				int xOffset = Math.round(requiredCols * pageSize.x);
				getPageBreaksFigure().setCols(requiredCols);
				getPageBreaksFigure().setBounds(
					new Rectangle(
						pageBreakBounds.x,
						pageBreakBounds.y,
						xOffset,
						pageBreakBounds.height));

			} else {

				// Move EAST: add cols to the EAST
				if (newBounds.right() > pageBreakBounds.right()) {
					int effectiveWidth =
						Math.abs(newBounds.x - pageBreakBounds.x)
							+ newBounds.width;
					float x = ((float) effectiveWidth) / pageSize.x;
					int reqdCols = (int) Math.ceil(Math.abs(x));
					int xOffset = Math.round(reqdCols * pageSize.x);
					getPageBreaksFigure().setCols(reqdCols);
					pageBreakBounds = getFigure().getBounds();
					getPageBreaksFigure().setBounds(
						new Rectangle(
							pageBreakBounds.x,
							pageBreakBounds.y,
							xOffset,
							pageBreakBounds.height));
				}

				// Remove cols from the WEST?
				pageBreakBounds = getPageBreaksFigure().getBounds();
				int effectiveWidth =
					Math.abs(pageBreakBounds.right() - newBounds.right())
						+ newBounds.width;
				float cols = ((float) effectiveWidth) / pageSize.x;
				int requiredCols = (int) Math.ceil(Math.abs(cols));
				int xOffset = Math.round(requiredCols * pageSize.x);
				getPageBreaksFigure().setCols(requiredCols);
				getPageBreaksFigure().setBounds(
					new Rectangle(
						pageBreakBounds.right() - xOffset,
						pageBreakBounds.y,
						xOffset,
						pageBreakBounds.height));

			}
		}

		// Change in NORTH/SOUTH directions
		if (newBounds.y != diagramBounds.y
			|| newBounds.height != diagramBounds.height) {

			if (newBounds.y < diagramBounds.y
				|| newBounds.bottom() < diagramBounds.bottom()) {

				// Move NORTH: add rows to the NORTH
				if (newBounds.y < pageBreakBounds.y) {
					// Add row to the top
					int effectiveHeight =
						(pageBreakBounds.bottom() - newBounds.bottom())
							+ newBounds.height;
					float rows = ((float) effectiveHeight) / pageSize.y;
					int requiredRows = (int) Math.ceil(Math.abs(rows));
					int yOffset = Math.round(requiredRows * pageSize.y);

					getPageBreaksFigure().setRows(requiredRows);
					pageBreakBounds = getFigure().getBounds();
					getPageBreaksFigure().setBounds(
						new Rectangle(
							pageBreakBounds.x,
							pageBreakBounds.bottom() - yOffset,
							pageBreakBounds.width,
							yOffset));
				}

				// Remove rows to the SOUTH?
				pageBreakBounds = getPageBreaksFigure().getBounds();
				int effectiveHeight =
					Math.abs(newBounds.y - pageBreakBounds.y)
						+ newBounds.height;
				float rows = ((float) effectiveHeight) / pageSize.y;
				int requiredRows = (int) Math.ceil(Math.abs(rows));
				int yOffset = Math.round(requiredRows * pageSize.y);
				getPageBreaksFigure().setRows(requiredRows);
				pageBreakBounds = getFigure().getBounds();
				getPageBreaksFigure().setBounds(
					new Rectangle(
						pageBreakBounds.x,
						pageBreakBounds.y,
						pageBreakBounds.width,
						yOffset));


			} else {

				// Move SOUTH: add rows the SOUTH
				if (newBounds.bottom() > pageBreakBounds.bottom()) {
					// move south outside the page breaks boundary           		
					int effectiveHeight =
						(newBounds.y - pageBreakBounds.y) + newBounds.height;
					float rows = ((float) effectiveHeight) / pageSize.y;
					int requiredRows = (int) Math.ceil(Math.abs(rows));
					int yOffset = Math.round(requiredRows * pageSize.y);

					getPageBreaksFigure().setRows(requiredRows);
					pageBreakBounds = getFigure().getBounds();
					getPageBreaksFigure().setBounds(
						new Rectangle(
							pageBreakBounds.x,
							pageBreakBounds.y,
							pageBreakBounds.width,
							yOffset));
				}

				// Remove rows from the NORTH?
				pageBreakBounds = getPageBreaksFigure().getBounds();
				int effectiveheight =
					Math.abs(pageBreakBounds.bottom() - newBounds.bottom())
						+ newBounds.height;
				float rows = ((float) effectiveheight) / pageSize.y;
				int requiredRows = (int) Math.ceil(Math.abs(rows));
				int yOffset = Math.round(requiredRows * pageSize.y);

				getPageBreaksFigure().setRows(requiredRows);
				pageBreakBounds = getFigure().getBounds();
				getPageBreaksFigure().setBounds(
					new Rectangle(
						pageBreakBounds.x,
						pageBreakBounds.bottom() - yOffset,
						pageBreakBounds.width,
						yOffset));

			}
		}

		this.diagramBounds = newBounds;
	}
	
	/**
	 * Updates the workspace viewer's prefence store values for
	 * the page breaks figure location.
	 */
	public void updatePreferenceStore() {
		Rectangle r = getPageBreaksFigure().getBounds();
		
		//don't use the workspace one
		IPreferenceStore s = ((DiagramGraphicalViewer) getRoot().getViewer())
		.getWorkspaceViewerPreferenceStore();
		
		if (s != null) {
			s.setValue(WorkspaceViewerProperties.PAGEBREAK_X, r.x);
			s.setValue(WorkspaceViewerProperties.PAGEBREAK_Y, r.y);
		}
	}	

	/*
	 * @see AbstractEditPart#getDragTracker(Request)
	 */
	public DragTracker getDragTracker(Request req) {
		return new DragEditPartsTrackerEx(this) {
			protected boolean isMove() {
				return true;
			}
		};
	}
	
	/**
	 * Return the workspace preference store from the DiagramUIPlugin or the
	 * diagram viewer's preference store, depending on what the user chose
	 * in the Page Setup dialog.
	 * 
	 * @return IPreferenceStore for the workspace or diagram viewer
	 */
	private IPreferenceStore getPreferenceStoreForPageSetup() {
		IPreferenceStore workspaceStore = ((DiagramGraphicalViewer) getRoot()
			.getViewer()).getWorkspaceViewerPreferenceStore();

		if (workspaceStore
			.getBoolean(WorkspaceViewerProperties.PREF_USE_DIAGRAM_SETTINGS)) {
			return workspaceStore;
		} else if (getRoot() instanceof IDiagramPreferenceSupport) {
			return (IPreferenceStore)((IDiagramPreferenceSupport) getRoot()).getPreferencesHint()
				.getPreferenceStore();
		}
		return (IPreferenceStore) PreferencesHint.USE_DEFAULTS
			.getPreferenceStore();
	}
}
