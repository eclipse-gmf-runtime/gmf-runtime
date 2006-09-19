/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.CollapseFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.CompartmentCollapseTracker;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A handle for collapsing resizable compartments
 *  
 * @author melaasar
 */
public class CompartmentCollapseHandle
	extends AbstractHandle
	implements PropertyChangeListener, NotificationListener {

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
	public static Dimension SIZE = new Dimension(11, 11);

	/** the handle figure */
	protected CollapseFigure collapseFigure = null;

	/**
     * Creates a new Compartment Collapse Handle
     * 
     * @param owner
     */
    public CompartmentCollapseHandle(IGraphicalEditPart owner) {
        setOwner(owner);
        setLocator(new CollapseHandleLocator());
        setCursor(Cursors.ARROW);

        setSize(SIZE);
        setLayoutManager(new StackLayout());

        if (owner != null && owner.getParent() != null
            && owner.getParent() instanceof IGraphicalEditPart)
            add(collapseFigure = new CollapseFigure(((IGraphicalEditPart) owner
                .getParent()).getFigure()));
        else
            add(collapseFigure = new CollapseFigure());

        View view = owner.getNotationView();
        if (view != null) {
            DrawerStyle style = (DrawerStyle) view
                .getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
            if (style != null) {
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
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void notifyChanged(Notification notification) {
		if (NotationPackage.eINSTANCE.getDrawerStyle_Collapsed()==notification.getFeature())
			collapseFigure.setCollapsed(notification.getNewBooleanValue());
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		IGraphicalEditPart owner = (IGraphicalEditPart) getOwner();
		View view = owner.getNotationView();
		if (view!=null){
			getDiagramEventBroker().addNotificationListener(owner.getNotationView(),CompartmentCollapseHandle.this);
		}
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#removeNotify()
	 */
	public void removeNotify() {
		IGraphicalEditPart owner = (IGraphicalEditPart) getOwner();
		getDiagramEventBroker().removeNotificationListener(owner.getNotationView(),this);
		super.removeNotify();
	}
	
    private DiagramEventBroker getDiagramEventBroker() {
        TransactionalEditingDomain theEditingDomain = ((IGraphicalEditPart) getOwner())
            .getEditingDomain();
        if (theEditingDomain != null) {
            return DiagramEventBroker.getInstance(theEditingDomain);
        }
        return null;
    }

}
