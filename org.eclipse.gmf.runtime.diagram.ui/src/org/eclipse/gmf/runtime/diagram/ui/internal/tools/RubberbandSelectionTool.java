/******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.util.SelectInDiagramHelper;

/**
 * A Tool which selects multiple objects inside a rectangular area of a Graphical Viewer. 
 * If the SHIFT key is pressed at the beginning of the drag, the enclosed items will be
 * appended to the current selection.  If the CONTROL key is pressed at the beginning of
 * the drag, the enclosed items will have their selection state inverted.
 * <P>
 * By default, only editparts whose figure's are on the primary layer will be considered
 * within the enclosed rectangle.
 * 
 * Tauseef Israr
 * September 20, 04.  This class is a copy of MarqueeSelectionTool but provides two
 * additional functionality. 
 * 1. The selection of connectors which is reported here
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74360

           Summary: MarqueeSelectionTool does not select connectors.
           Product: GEF
           Version: unspecified
          Platform: PC
        OS/Version: Windows XP
            Status: NEW
          Severity: normal
          Priority: P3
         Component: GEF
        AssignedTo: gef-inbox@eclipse.org
        ReportedBy: tisrar@ca.ibm.com
 *
 *and the 2. is the auto-scroll capability.
 */
/*
 * @canBeSeenBy %level1
 */
public class RubberbandSelectionTool
	extends AbstractTool
{

static final int TOGGLE_MODE = 1;
static final int APPEND_MODE = 2;

private int mode;

private Figure marqueeRectangleFigure;
private HashSet allChildren = new HashSet();
private List selectedEditParts;
private Request targetRequest;

private Point feedBackStartLocation = null;

private WeakReference weakReference;

private static final Request MARQUEE_REQUEST =
	new Request(RequestConstants.REQ_SELECTION); 

/**
 * Creates a new MarqueeSelectionTool.
 */
public RubberbandSelectionTool() {
	setDefaultCursor(SharedCursors.CROSS); 
	setUnloadWhenFinished(false);
}

private List calculateNewSelection() {

	List newSelections = new ArrayList();
	Iterator children = getAllChildren().iterator();

	// Calculate new selections based on which children fall
	// inside the marquee selection rectangle.  Do not select
	// children who are not visible
	while (children.hasNext()){	
		EditPart child = (EditPart) children.next();
		if (!child.isSelectable())
			continue;
		IFigure figure = ((GraphicalEditPart)child).getFigure();
		Rectangle r = figure.getBounds().getCopy();
		figure.translateToAbsolute(r);

		Rectangle marqueeBounds = getMarqueeBounds();	
		getMarqueeFeedbackFigure().translateToRelative(r);
		if (marqueeBounds.contains(r.getTopLeft())
		  && marqueeBounds.contains(r.getBottomRight())		  
		  && child.getTargetEditPart(MARQUEE_REQUEST) == child){
			newSelections.add(child);
		}
	}
	return newSelections;
}

private Request createTargetRequest() {
	return MARQUEE_REQUEST;
}

/**
 * Erases feedback if necessary and puts the tool into the terminal state.
 */
public void deactivate() {
	if (isInState(STATE_DRAG_IN_PROGRESS)) {
		eraseMarqueeFeedback();
		eraseTargetFeedback();
	}
	super.deactivate();
	allChildren = new HashSet();
	setState(STATE_TERMINAL);
}

private void eraseMarqueeFeedback() {
	if (marqueeRectangleFigure != null) {
		removeFeedback(marqueeRectangleFigure);
		marqueeRectangleFigure = null;		
	}
	feedBackStartLocation = null;
}

private void eraseTargetFeedback() {
	if (selectedEditParts == null)
		return;
	ListIterator oldEditParts = selectedEditParts.listIterator();
	while (oldEditParts.hasNext()) {
		EditPart editPart = (EditPart)oldEditParts.next();
		editPart.eraseTargetFeedback(getTargetRequest());
	}
}

/**
 * Returns a list including all of the children
 * of the edit part passed in.
 */
private HashSet getAllChildren(EditPart editPart, HashSet allChildren1){	
	List children = editPart.getChildren();
	for (int i = 0; i < children.size(); i++) {
		GraphicalEditPart child = (GraphicalEditPart) children.get(i);
		if (!(child instanceof IBorderItemEditPart)){
			allChildren1.add(child);
			getAllChildren(child, allChildren1);
		}
		allChildren1.addAll(child.getSourceConnections());
		allChildren1.addAll(child.getTargetConnections());		
	}
	return allChildren1;
}

/**
 * Return a vector including all of the children
 * of the root editpart
 */
private HashSet getAllChildren() {
	if (allChildren.isEmpty())
		allChildren = getAllChildren(
			getCurrentViewer().getRootEditPart(),
			new HashSet());
	return allChildren;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
 */
protected String getCommandName() {
	return REQ_SELECTION;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
 */
protected String getDebugName() {
	return "Marquee Tool";//$NON-NLS-1$
}

protected IFigure getMarqueeFeedbackFigure() {		
	if (marqueeRectangleFigure == null) {
		marqueeRectangleFigure = new MarqueeRectangleFigure();
		addFeedback(marqueeRectangleFigure);
	}
	return marqueeRectangleFigure;
}

protected Rectangle getMarqueeSelectionRectangle() {
	return new Rectangle(getStartLocation(),getLocation());
}

/**
 * Gets the relative bounds of the marquee feedback figure.
 * @return
 */
private Rectangle getMarqueeBounds(){
	if (getMarqueeFeedbackFigure() == null)
		return new Rectangle();
	
	Rectangle rect = new Rectangle();
	if (feedBackStartLocation == null){
		rect = getMarqueeSelectionRectangle();
		getMarqueeFeedbackFigure().translateToRelative(rect);
		feedBackStartLocation = rect.getLocation();
		return rect;
	}else{
		Point location = getLocation().getCopy();
		getMarqueeFeedbackFigure().translateToRelative(location);
		rect = new Rectangle(feedBackStartLocation,location);
		return rect;
	}
	
}

private int getSelectionMode() {
	return mode;
}

private Request getTargetRequest() {
	if (targetRequest == null)
		targetRequest = createTargetRequest();
	return targetRequest;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
 */
protected boolean handleButtonDown(int button) {
	if (!isGraphicalViewer())
		return true;
	if (button != 1) {
		setState(STATE_INVALID);
		handleInvalidInput();
	}
	if (stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS)) {
		if (getCurrentInput().isControlKeyDown())
			setSelectionMode(TOGGLE_MODE);
		else if (getCurrentInput().isShiftKeyDown())
			setSelectionMode(APPEND_MODE);
		
		// RATLC00740277:
		// clear current focus (if any) before we start computing selections,
		//   because we don't want to select any compartments in the focus
		//   edit part if they shouldn't be selectable
		clearFocus();
	}
	return true;
}

/**
 * Effectively clears the current focus edit part by deliberately setting the
 * diagram contents edit part as the focus.  This ensures that the rubber band
 * won't mistakenly select the selectable compartments and items in the current
 * focus edit part.
 */
private void clearFocus() {
	EditPart focusPart = getCurrentViewer().getFocusEditPart();
	
	if (focusPart != null) {
		// replace the current focus with the contents edit part, which effectively
		//   blocks unwanted selectability of compartments within the previous
		//  focus edit part
		getCurrentViewer().setFocus(getCurrentViewer().getContents());
	}
}

/**
 * Extends the inherited method by first restoring the current viewer's focus
 * edit part to the default (which is the last selected edit part).  This undoes
 * the work-around that sets the diagram root as the focus.
 * 
 * @see #clearFocus()
 */
protected void handleFinished() {
	getCurrentViewer().setFocus(null);
	
	super.handleFinished();
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
 */
protected boolean handleButtonUp(int button) {
	if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
		eraseTargetFeedback();
		performMarqueeSelect();
		eraseMarqueeFeedback();		
	}
	handleFinished();
	return true;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
 */
protected boolean handleDragInProgress() {
	if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {		
		
		showMarqueeFeedback();
		eraseTargetFeedback();		
		selectedEditParts = calculateNewSelection();
		showTargetFeedback();
		SelectInDiagramHelper.exposeLocation((FigureCanvas)getCurrentViewer().getControl(),getLocation());
				
	}
	return true;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
 */
protected boolean handleFocusLost() {
	if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
		handleFinished();
		return true;
	}
	return false;
}

/**
 * This method is called when mouse or keyboard input is invalid and erases the feedback.
 * @return <code>true</code>
 */
protected boolean handleInvalidInput() {
	eraseTargetFeedback();
	eraseMarqueeFeedback();
	return true;
}

/**
 * Handles high-level processing of a key down event. 
 * KeyEvents are forwarded to the current viewer's {@link KeyHandler}, 
 * via {@link KeyHandler#keyPressed(KeyEvent)}.
 * @see AbstractTool#handleKeyDown(KeyEvent)
 */
protected boolean handleKeyDown(KeyEvent e) {
	if (super.handleKeyDown(e))
		return true;
	if (getCurrentViewer().getKeyHandler() != null
		&& getCurrentViewer().getKeyHandler().keyPressed(e))
		return true;
	return false;		
}

private boolean isGraphicalViewer() {
	return getCurrentViewer() instanceof GraphicalViewer;
}

protected void performMarqueeSelect() {
	EditPartViewer viewer = getCurrentViewer();

	List newSelections = calculateNewSelection();

	// If in multi select mode, add the new selections to the already
	// selected group; otherwise, clear the selection and select the new group
	if (getSelectionMode() == APPEND_MODE) {
		for (int i = 0; i < newSelections.size(); i++) {
			EditPart editPart = (EditPart)newSelections.get(i);	
			viewer.appendSelection(editPart); 
		} 
	} else if (getSelectionMode() == TOGGLE_MODE) {
		List selected = new ArrayList(viewer.getSelectedEditParts());
		for (int i = 0; i < newSelections.size(); i++) {
			EditPart editPart = (EditPart)newSelections.get(i);	
			if (editPart.getSelected() != EditPart.SELECTED_NONE)
				selected.remove(editPart);
			else
				selected.add(editPart);
		}
		viewer.setSelection(new StructuredSelection(selected));
	} else {
		viewer.setSelection(new StructuredSelection(newSelections));
	}
}

/**
 * @see org.eclipse.gef.Tool#setViewer(org.eclipse.gef.EditPartViewer)
 */
public void setViewer(EditPartViewer viewer) {
	if (viewer == getCurrentViewer())
		return;
	super.setViewer(viewer);
	if (viewer instanceof GraphicalViewer)
		setDefaultCursor(SharedCursors.CROSS);
	else
		setDefaultCursor(SharedCursors.NO);
	if (viewer != null)
		weakReference = new WeakReference(viewer);
}

private void setSelectionMode(int mode) {
	this.mode = mode;
}

private void showMarqueeFeedback() {
	getMarqueeFeedbackFigure().setBounds(getMarqueeBounds());
}

private void showTargetFeedback() {
	for (int i = 0; i < selectedEditParts.size(); i++) {
		EditPart editPart = (EditPart) selectedEditParts.get(i);
		editPart.showTargetFeedback(getTargetRequest());
	}
}/**
 * Convenience method to removes a figure from the feedback layer.
 * @param figure the figure being removed
 */
	protected void removeFeedback(IFigure figure) {
		EditPartViewer viewer = getCurrentViewer();
		if ((viewer == null)&&(weakReference != null))
			viewer = (EditPartViewer) weakReference.get();
		if (viewer != null) {
			LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(
				LayerManager.ID);
			if (lm == null)
				return;
			lm.getLayer(LayerConstants.FEEDBACK_LAYER).remove(figure);
		}
	}

class MarqueeRectangleFigure 
extends Figure {

private int offset = 0;
private boolean schedulePaint = true;
private static final int DELAY = 110; //animation delay in millisecond
/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {	
	Rectangle bounds1 = getBounds().getCopy();
	graphics.translate(getLocation());
	
	graphics.setXORMode(true);
	graphics.setForegroundColor(ColorConstants.white);
	graphics.setBackgroundColor(ColorConstants.black);
	
	graphics.setLineStyle(Graphics.LINE_DOT);
	
	int[] points = new int[6];
	
	points[0] = 0 + offset;
	points[1] = 0;
	points[2] = bounds1.width - 1;
	points[3] = 0;
	points[4] = bounds1.width - 1;
	points[5] = bounds1.height - 1;
	
	graphics.drawPolyline(points);
	
	points[0] = 0;
	points[1] = 0 + offset;
	points[2] = 0;
	points[3] = bounds1.height - 1;
	points[4] = bounds1.width - 1;
	points[5] = bounds1.height - 1;
	
	graphics.drawPolyline(points);
	
	graphics.translate(getLocation().getNegated());
	
	if (schedulePaint) {
		Display.getCurrent().timerExec(DELAY, new Runnable() {
			public void run() {
				offset++;
				if (offset > 5)
					offset = 0;	
				
				schedulePaint = true;
				repaint();
			}
		});
	}
	
	schedulePaint = false;
}
	
}

}

