/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.gmf.runtime.gef.ui.internal.requests.DirectEditRequestWrapper;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 *
 * <p>
 * Code taken from Eclipse reference bugzilla #98820
 *
 */
public class DiagramGraphicalViewerKeyHandler
	extends GraphicalViewerKeyHandler {

	/**
	 * @param viewer
	 */
	public DiagramGraphicalViewerKeyHandler(GraphicalViewer viewer) {
		super(viewer);
	}

	/**
	 * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public boolean keyPressed(KeyEvent event) {
		if (isAlphaNum(event)) {
			// Create a Direct Edit Request and cache the character typed
			DirectEditRequestWrapper req =
				new DirectEditRequestWrapper(event.character);
			// Send the request to the current edit part in focus
			getFocusPart().performRequest(req);
			return true;
		}
		switch (event.keyCode) {
			case SWT.HOME :
				if ((event.stateMask & SWT.ALT) != 0) {
					if (navigateEndSibling(event, PositionConstants.WEST))
						return true;
				} else {
					if (navigateEndSibling(event, PositionConstants.NORTH))
						return true;
				}
			case SWT.END :
				if ((event.stateMask & SWT.ALT) != 0) {
					if (navigateEndSibling(event, PositionConstants.EAST))
						return true;
				} else {
					if (navigateEndSibling(event, PositionConstants.SOUTH))
						return true;
				}
			case SWT.PAGE_UP :
				if ((event.stateMask & SWT.ALT) != 0) {
					if (navigatePageSibling(event, PositionConstants.WEST))
						return true;
				} else {
					if (navigatePageSibling(event, PositionConstants.NORTH))
						return true;
				}
			case SWT.PAGE_DOWN :
				if ((event.stateMask & SWT.ALT) != 0) {
					if (navigatePageSibling(event, PositionConstants.EAST))
						return true;
				} else {
					if (navigatePageSibling(event, PositionConstants.SOUTH))
						return true;
				}
		}
		return super.keyPressed(event);
	}

	/**
	 * Tests to see if the key pressed was an letter or number
	 * @param event KeyEvent to be tested
	 * @return true if the key pressed is Alpha Numeric, otherwise false.
	 */
	protected boolean isAlphaNum(KeyEvent event) {

		final String allowedStartingCharacters = "`~!@#$%^&*()-_=+{}[]|;:',.<>?\""; //$NON-NLS-1$

		// IF the character is a letter or number or is contained
		// in the list of allowed starting characters ...
		if (Character.isLetterOrDigit(event.character)
			|| !(allowedStartingCharacters.indexOf(event.character) == -1)) {

			// And the character hasn't been modified or is only modified
			// with SHIFT
			if (event.stateMask == 0 || event.stateMask == SWT.SHIFT)
				return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	protected GraphicalEditPart getFocusPart() {
		return (GraphicalEditPart) getViewer().getFocusEditPart();
	}

	/**
	 * @param part
	 * @param event
	 */
	protected void navigateToPart(EditPart part, KeyEvent event) {
		if (part == null)
			return;
		if (!part.isSelectable()) {
			getViewer().deselectAll();
			getViewer().setFocus(part);
		} else if ((event.stateMask & SWT.SHIFT) != 0) {
			getViewer().appendSelection(part);
			getViewer().setFocus(part);
		} else if ((event.stateMask & SWT.CONTROL) != 0)
			getViewer().setFocus(part);
		else
			getViewer().select(part);
		getViewer().reveal(part);
	}

	/**
	 * @return
	 */
	protected List getPartNavigationSiblings() {
		return getFocusPart().getParent().getChildren();
	}

	/**
	 * @param figure
	 * @return
	 */
	protected Point getFigureInterestingPoint(IFigure figure) {
		return figure.getBounds().getCenter();
	}

	protected Viewport findViewport(GraphicalEditPart part) {
		IFigure figure = null;
		Viewport port = null;
		do {
			if (figure == null)
				figure = part.getContentPane();
			else
				figure = figure.getParent();
			if (figure instanceof Viewport) {
				port = (Viewport) figure;
				break;
			}
		} while (figure != null);
		return port;
	}

	/**
	 * @param event
	 * @param direction
	 * @param page
	 * @param list
	 * @return
	 */
	protected boolean navigatePageSibling(KeyEvent event, int direction) {
		GraphicalEditPart epStart = getFocusPart();
		IFigure figure = epStart.getFigure();
		Point pStart = getFigureInterestingPoint(figure);
		figure.translateToAbsolute(pStart);

		GraphicalEditPart epParent = (GraphicalEditPart) epStart.getParent();
		Viewport viewport = findViewport(epParent);
		Rectangle bounds =
			(viewport != null)
				? new Rectangle(viewport.getBounds())
				: epParent.getFigure().getClientArea();
		figure.translateToAbsolute(bounds);
		int pageDistance = 0;
		switch (direction) {
			case PositionConstants.NORTH :
			case PositionConstants.SOUTH :
				pageDistance = bounds.height;
				break;
			case PositionConstants.EAST :
			case PositionConstants.WEST :
				pageDistance = bounds.width;
				break;
		}

		List editParts =
			findPageSibling(
				getPartNavigationSiblings(),
				pStart,
				pageDistance,
				direction,
				epStart);
		if (editParts.isEmpty())
			return false;
		if ((event.stateMask & SWT.SHIFT) != 0) {
			Iterator parts = editParts.iterator();
			while (parts.hasNext())
				navigateToPart((EditPart) parts.next(), event);
		} else {
			EditPart part = (EditPart) editParts.get(editParts.size() - 1);
			navigateToPart(part, event);
		}
		return true;
	}

	/**
	 * @param event
	 * @param direction
	 * @param list
	 * @return
	 */
	protected boolean navigateEndSibling(KeyEvent event, int direction) {
		GraphicalEditPart epStart = getFocusPart();
		IFigure figure = epStart.getFigure();
		Point pStart = getFigureInterestingPoint(figure);
		figure.translateToAbsolute(pStart);
		List editParts =
			findEndSibling(
				getPartNavigationSiblings(),
				pStart,
				direction,
				epStart);
		if (editParts.isEmpty())
			return false;
		if ((event.stateMask & SWT.SHIFT) != 0) {
			Iterator parts = editParts.iterator();
			while (parts.hasNext())
				navigateToPart((EditPart) parts.next(), event);
		} else {
			EditPart part = (EditPart) editParts.get(editParts.size() - 1);
			navigateToPart(part, event);
		}
		return true;
	}

	/**
	 * @param siblings
	 * @param pStart
	 * @param minDistance
	 * @param direction
	 * @param exclude
	 * @return
	 */
	private List findPageSibling(
		List siblings,
		Point pStart,
		int pageDistance,
		int direction,
		EditPart exclude) {
		GraphicalEditPart epCurrent;
		GraphicalEditPart epFinal = null;
		List selection = new ArrayList();
		IFigure figure;
		Point pCurrent;
		int distance = 0;

		Iterator iter = siblings.iterator();
		while (iter.hasNext()) {
			epCurrent = (GraphicalEditPart) iter.next();
			if (epCurrent == exclude
				|| !epCurrent.getFigure().isVisible()
				|| epCurrent.getFigure().getBounds().isEmpty())
				continue;
			figure = epCurrent.getFigure();
			pCurrent = getFigureInterestingPoint(figure);
			figure.translateToAbsolute(pCurrent);
			if (pStart.getPosition(pCurrent) != direction)
				continue;

			int d = pCurrent.getDistanceOrthogonal(pStart);
			if (d <= pageDistance) {
				selection.add(epCurrent);
				if (d > distance) {
					distance = d;
					epFinal = epCurrent;
				}
			}
			if (epFinal != null) {
				selection.remove(epFinal);
				selection.add(epFinal);
			}
		}
		return selection;
	}

	/**
	 * @param siblings
	 * @param pStart
	 * @param direction
	 * @param exclude
	 * @return
	 */
	private List findEndSibling(
		List siblings,
		Point pStart,
		int direction,
		EditPart exclude) {
		GraphicalEditPart epCurrent;
		GraphicalEditPart epFinal = null;
		List selection = new ArrayList();
		IFigure figure;
		Point pCurrent;
		int distance = 0;

		Iterator iter = siblings.iterator();
		while (iter.hasNext()) {
			epCurrent = (GraphicalEditPart) iter.next();
			if (epCurrent == exclude
				|| !epCurrent.getFigure().isVisible()
				|| epCurrent.getFigure().getBounds().isEmpty())
				continue;
			figure = epCurrent.getFigure();
			pCurrent = getFigureInterestingPoint(figure);
			figure.translateToAbsolute(pCurrent);
			if (pStart.getPosition(pCurrent) != direction)
				continue;

			selection.add(epCurrent);
			int d = pCurrent.getDistanceOrthogonal(pStart);
			if (d > distance) {
				distance = d;
				epFinal = epCurrent;
			}
		}
		if (epFinal != null) {
			selection.remove(epFinal);
			selection.add(epFinal);
		}
		return selection;
	}
}
