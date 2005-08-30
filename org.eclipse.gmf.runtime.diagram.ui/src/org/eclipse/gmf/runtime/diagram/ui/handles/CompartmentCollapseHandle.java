/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.handles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;

import org.eclipse.gmf.runtime.diagram.core.listener.PropertyChangeNotifier;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.CollapseFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.CompartmentCollapseTracker;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import com.ibm.xtools.notation.DrawerStyle;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.View;

/**
 * A handle for collapsing resizable compartments
 *  
 * @author melaasar
 */
public class CompartmentCollapseHandle
	extends AbstractHandle
	implements PropertyChangeListener {

	/** 
	 * Positions the supplied figure in its owner's top left corner offset by [1,1] 
	 */
	private class CollapseHandleLocator implements Locator {
		public void relocate(IFigure target) {
			Rectangle theBounds = getOwnerFigure().getClientArea().getCopy();
			getOwnerFigure().translateToAbsolute(theBounds);
			target.translateToRelative(theBounds);
			target.setLocation(theBounds.getLocation());
		}
	}

	/** handle figure dimension */
	public static Dimension SIZE =
		new Dimension(
			MapMode.LPtoDP(ResizableCompartmentFigure.MIN_CLIENT_SIZE),
			MapMode.LPtoDP(ResizableCompartmentFigure.MIN_CLIENT_SIZE));

	/** the handle figure */
	protected CollapseFigure collapseFigure = null;

	/** the owner's model listener */
	private PropertyChangeNotifier notifier;

	/**
	 * Creates a new Compartment Collapse Handle 
	 * @param owner
	 */
	public CompartmentCollapseHandle(IGraphicalEditPart owner) {
		setOwner(owner);
		setLocator(new CollapseHandleLocator());
		setCursor(Cursors.ARROW);
		setSize(SIZE);
		setLayoutManager(new StackLayout());
		add(collapseFigure = new CollapseFigure());
		View view = owner.getNotationView();
		if (view!=null){
			DrawerStyle style = (DrawerStyle) view.getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
			if (style != null){
				collapseFigure.setCollapsed(style.isCollapsed());
				return;
			}
		}
		collapseFigure.setCollapsed(false);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int, TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		IFigure found = super.findFigureAt(x, y, search);
		return (collapseFigure.equals(found)) ? this : found;
	}

	/**
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new CompartmentCollapseTracker(
			(IResizableCompartmentEditPart) getOwner());
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Properties.ID_COLLAPSED))
			collapseFigure.setCollapsed(
				((Boolean) evt.getNewValue()).booleanValue());
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		IGraphicalEditPart owner = (IGraphicalEditPart) getOwner();
		View view = owner.getNotationView();
		if (view!=null){
			notifier = ViewUtil.getPropertyChangeNotifier(view);
			notifier.addPropertyChangeListener(CompartmentCollapseHandle.this);
		}
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#removeNotify()
	 */
	public void removeNotify() {
		notifier.removePropertyChangeListener(this);
		super.removeNotify();
	}

}
