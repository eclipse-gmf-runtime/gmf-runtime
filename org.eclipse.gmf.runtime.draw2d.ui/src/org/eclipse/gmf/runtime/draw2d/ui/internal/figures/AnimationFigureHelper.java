/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;


/**
 * @author hudsonr
 * Created on Apr 28, 2003
 * 
 * Modified by sshaw to support presentation figures
 * 
 * <p>
 * Code taken from Eclipse reference bugzilla #84957
 */
public class AnimationFigureHelper {

static private Map instances = new HashMap();

static private class AnimationFigureHelperKey {
	public Display display;
	public int inc;
	public int fac;
	
	public AnimationFigureHelperKey(Display display, int inc, int fac) {
		this.display = display;
		this.inc = inc;
		this.fac = fac;
	}
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof AnimationFigureHelperKey) {
			AnimationFigureHelperKey afhk = (AnimationFigureHelperKey)arg0;
			return display.equals(afhk.display) && inc == afhk.inc && fac == afhk.fac;
		}
		return false;
	}
	
	public int hashCode() {
		return display.hashCode() ^ (new Integer(inc)).hashCode() ^ (new Integer(fac)).hashCode();
	}
}

/**
 * Single accessor for an instance of <code>AnimationFigureHelper</code>
 * 
 * @return SingletonInstance of the AnimationFigureHelper
 */
static public AnimationFigureHelper getInstance() {
	AnimationFigureHelperKey key = new AnimationFigureHelperKey(Display.getCurrent(), 
		DEFAULT_DURATION_INCREMENT, DEFAULT_INCREMENT_FACTOR);
	AnimationFigureHelper figureAnimation = 
		(AnimationFigureHelper)instances.get(key);
	if (figureAnimation == null) {
		figureAnimation = new AnimationFigureHelper();
		instances.put(key, figureAnimation);
	}
	
	return figureAnimation;
}

/**
 * Single accessor for an instance of <code>AnimationFigureHelper</code>
 * 
 * @param durationIncrement <code>int</code> increment of the animation in milliseconds.  This describes the duration increment 
 * which is multipled by a fraction of the increment factor to calculate the total duration. This is calculated as the duration 
 * increment multiplied by 1/<code>incrementFactor</code> of the size of the children collection.  Up to a maximum animation 
 * length of <code>incrementFactor</code> / 2 times the duration increment.
 * @param incrementFactor <code>int</code> increment factor of the animation.  This describes the factor by which the duration 
 * increment is multiplied by to retrieve the actual length of the animation.  This is calculated as the duration increment 
 * multiplied by 1/<code>incrementFactor</code> of the size of the children collection.  Up to a maximum animation length of 
 * <code>incrementFactor</code> / 2 times the duration increment.
 * @return Singleton Instance of the <code>AnimationFigureHelper</code>
 */
static public AnimationFigureHelper getInstance(int durationIncrement, int incrementFactor) {
	AnimationFigureHelperKey key = new AnimationFigureHelperKey(Display.getCurrent(), durationIncrement, incrementFactor);
	AnimationFigureHelper figureAnimation = (AnimationFigureHelper)instances.get(key);
	if (figureAnimation == null) {
		figureAnimation = new AnimationFigureHelper(durationIncrement, incrementFactor);
		instances.put(key, figureAnimation);
	}
	
	return figureAnimation;
}

private static final int DEFAULT_DURATION_INCREMENT = 800;
private static final int DEFAULT_INCREMENT_FACTOR = 10;

private double progress;
private Viewport viewport;
private boolean animating;
private boolean recording;
private Map initialStates;
private Map finalStates;
private AnimationModel animationModel;
private int durationIncrement = DEFAULT_DURATION_INCREMENT;
private int incrementFactor = DEFAULT_INCREMENT_FACTOR;
private int totalDuration = -1;

private AnimationFigureHelper() {
	// no initial state initialization required.
}

private AnimationFigureHelper(int durationIncrement, int incrementFactor) {
	this.durationIncrement = durationIncrement;
	this.incrementFactor = incrementFactor;
}

/**
 * @return the duration increment of the animation in milliseconds.  This describes the duration increment which is multipled by a 
 * fraction of the increment factor to calculate the total duration. This is calculated as the duration increment multiplied by 
 * 1/<code>incrementFactor</code> of the size of the children collection.  
 * Up to a maximum animation length of <code>incrementFactor</code> / 2 times the duration increment.
 */
public int getDurationIncrement() {
	return durationIncrement;
}

/**
 * @return the increment factor of the animation.  This describes the factor by which the duration increment is multiplied
 * by to retrieve the actual length of the animation.  This is calculated as the duration increment multiplied by 1/<code>incrementFactor</code> 
 * of the size of the children collection.  Up to a maximum animation length of <code>incrementFactor</code> / 2 times the duration increment.
 */
public int getIncrementFactor() {
	return incrementFactor;
}

/**
 * @return <code>int</code> for the total duration that the animation will take calculated as the duration 
 * increment multiplied by 1/<code>incrementFactor</code> of the size of the children collection.  
 * Up to a maximum animation length of <code>incrementFactor</code> / 2
 */
public int getTotalDuration() {
	if (totalDuration == -1) {
		totalDuration = Math.min(getDurationIncrement() * getIncrementFactor() / 2, Math.max(getDurationIncrement(), (initialStates.size() / 
			getIncrementFactor()) * getDurationIncrement()));
	}
	
	return totalDuration;
}

/**
 * @param root IFigure that will be the source of the animation
 */
public void animate(IFigure root) {
	if (!captureLayout(root))
		return;
	
	// calculate the animation duration based on the size of the figures being animated.
	animationModel = new AnimationModel(getTotalDuration(), true);
	animationModel.animationStarted();
	
	while(step())
		root.getUpdateManager().performUpdate();
	end();
}

/**
 * layoutManagerHook
 * Method that allows the layout manager to register the initial layout constraint before the
 * layout occurs.  
 * TODO This will not be needed in GEF 3.1 where the layout manager allows for a listener before
 * and after the layout.
 * 
 * @param context IFigure that is to be animated during the layout.
 * @return true if the layout is being animated currently in which case the layout manager
 * should do nothing, or false otherwise.
 */
public boolean layoutManagerHook(IFigure context) {
	if (isRecording()) {
		recordInitialState(context);
	}
	
	if (isAnimating())
		return playbackState(context);
	
	return false;
}

/**
 * @return Returns the animating boolean.
 */
public boolean isAnimating() {
	return animating;
}
/**
 * @return Returns the recording boolean.
 */
public boolean isRecording() {
	return recording;
}


private void end() {
	Iterator iter = initialStates.keySet().iterator();
	while (iter.hasNext()) {
		IFigure f = ((IFigure)iter.next());
		f.revalidate();
	}
	
	initialStates = null;
	finalStates = null;
	animating = false;
	viewport = null;
	instances.remove(Display.getCurrent());
}

private boolean captureLayout(IFigure root) {

	recording = true;

	while (!(root instanceof Viewport))
		root = root.getParent();
	viewport = (Viewport)root;
	while (root.getParent()!= null)
		root = root.getParent();

	initialStates = new HashMap();
	finalStates = new HashMap();

	//This part records all layout results.
	root.validate();
	Iterator iter = initialStates.keySet().iterator();
	if (!iter.hasNext()) {
		//Nothing layed out, so abort the animation
		recording = false;
		return false;
	}
	while (iter.hasNext())
		recordFinalState((IFigure)iter.next());

	recording = false;
	animating = true;
	return true;
}

private boolean playbackState(Connection conn) {
	if (!isAnimating())
		return false;

	PointList list1 = (PointList)initialStates.get(conn);
	PointList list2 = (PointList)finalStates.get(conn);
	if (list1 == null || list2 == null) {
		return true;
	}
	if (list1.size() == list2.size()) {
		Point pt1 = new Point(), pt2 = new Point();
		PointList points = conn.getPoints();
		points.removeAllPoints();
		for (int i = 0; i < list1.size(); i++) {
			list1.getPoint(pt2, i);
			list2.getPoint(pt1, i);
			pt1.x = (int)Math.round(pt1.x * progress + (1-progress) * pt2.x);
			pt1.y = (int)Math.round(pt1.y * progress + (1-progress) * pt2.y);
			points.addPoint(pt1);
		}
		conn.setPoints(points);
	}
	return true;
}

private boolean playbackState(IFigure context) {
	if (!isAnimating())
		return false;

	if (context instanceof Connection) {
		return playbackState((Connection)context);
	}
	
	List children = context.getChildren();
	Rectangle rect1, rect2;
	for (int i = 0; i < children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		rect1 = (Rectangle)initialStates.get(child);
		rect2 = (Rectangle)finalStates.get(child);
		if (rect2 == null)
			continue;
		child.setBounds(new Rectangle(
			(int)Math.round(progress * rect2.x + (1-progress) * rect1.x),
			(int)Math.round(progress * rect2.y + (1-progress) * rect1.y),
			(int)Math.round(progress * rect2.width + (1-progress) * rect1.width),
			(int)Math.round(progress * rect2.height + (1-progress) * rect1.height)
		));
	}
	return true;
}


private void recordFinalState(Connection conn) {
	//$TODO
	PointList points1 = (PointList)initialStates.get(conn);
	PointList points2 = conn.getPoints().getCopy();
	
	if (points1 != null && points1.size() != points2.size()) {
		Point p = new Point(), q = new Point();

		int size1 = points1.size() - 1;
		int size2 = points2.size() - 1;

		int i1 = size1;
		int i2 = size2;

		double current1 = 1.0;
		double current2 = 1.0;

		double prev1 = 1.0;
		double prev2 = 1.0;

		while (i1 > 0 || i2 > 0) {
			if (Math.abs(current1 - current2) < 0.1
			  && i1 > 0 && i2 > 0) {
				//Both points are the same, use them and go on;
				prev1 = current1;
				prev2 = current2;
				i1--;
				i2--;
				current1 = (double)i1 / size1;
				current2 = (double)i2 / size2;
			} else if (current1 < current2) {
				//2 needs to catch up
				// current1 < current2 < prev1
				points1.getPoint(p, i1);
				points1.getPoint(q, i1 + 1);
				
				p.x = (int)(((q.x * (current2 - current1) + p.x * (prev1 - current2))
					/ (prev1 - current1)));
				p.y = (int)(((q.y * (current2 - current1) + p.y * (prev1 - current2))
					/ (prev1 - current1)));
				
				points1.insertPoint(p, i1 + 1);

				prev1 = prev2 = current2;
				i2--;
				current2 = (double)i2 / size2;
				
			} else {
				//1 needs to catch up
				// current2< current1 < prev2
				
				points2.getPoint(p, i2);
				points2.getPoint(q, i2 + 1);
				
				p.x = (int)(((q.x * (current1 - current2) + p.x * (prev2 - current1))
					/ (prev2 - current2)));
				p.y = (int)(((q.y * (current1 - current2) + p.y * (prev2 - current1))
					/ (prev2 - current2)));
				
				points2.insertPoint(p, i2 + 1);

				prev2 = prev1 = current1;
				i1--;
				current1 = (double)i1 / size1;
			}
		}
	}
	finalStates.put(conn, points2);
}

private void recordFinalState(IFigure context) {
	if (context instanceof Connection) {
		recordFinalState((Connection)context);
		return;
	}
	Rectangle rect2 = context.getBounds().getCopy();
	Rectangle rect1 = (Rectangle)initialStates.get(context);
	if (rect1.isEmpty()) {
			rect1.x = rect2.x;
			rect1.y = rect2.y;
			rect1.width = rect2.width;
	}
	finalStates.put(context, rect2);
}

private void recordInitialState(Connection connection) {
	if (!isRecording())
		return;
	PointList points = connection.getPoints().getCopy();
	if (points.size() == 2
	  && points.getPoint(0).equals(Point.SINGLETON.setLocation(0,0))
	  && points.getPoint(1).equals(Point.SINGLETON.setLocation(100,100)))
		initialStates.put(connection, null);
	else
		initialStates.put(connection, points);
}

private void recordInitialState(IFigure context) {
	if (!isRecording())
		return;
	
	if (initialStates.get(context) != null)
		return;
	
	if (context instanceof Connection) {
		recordInitialState((Connection)context);
		return;
	}
	List children = context.getChildren();
	IFigure child;
	for (int i=0; i<children.size();i++) {
		child = (IFigure)children.get(i);
		initialStates.put(child, child.getBounds().getCopy());
	}
}

private boolean step() {
	progress = animationModel.getProgress();
	Iterator iter = initialStates.keySet().iterator();
	
	while (iter.hasNext())
		((IFigure)iter.next()).revalidate();
	viewport.validate();
	
	return !animationModel.isFinished();
}


}


