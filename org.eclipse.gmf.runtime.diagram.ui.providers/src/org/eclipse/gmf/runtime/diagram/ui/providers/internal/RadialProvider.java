/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.IPropertyValueDeferred;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ChangeBoundsDeferredRequest;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNodesOperation;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramProvidersResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.SetAllBendpointRequest;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 * 
 * RadialProvider class that provides for LayoutType.RADIAL.
 */
public class RadialProvider
	extends AbstractLayoutEditPartProvider {

	/**
	 * @see com.ibm.xtools.common.service.IProvider#provides(IOperation)
	 */
	public boolean provides(IOperation operation) {
		Assert.isNotNull(operation);

		View cview = getContainer(operation);
		if (cview == null)
			return false;
		
		IAdaptable layoutHint = ((LayoutNodesOperation) operation).getLayoutHint(); 
		String layoutType = (String) layoutHint.getAdapter(String.class);
		return LayoutType.RADIAL.equals(layoutType);		
	} 

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider#layoutEditParts(org.eclipse.gef.GraphicalEditPart, org.eclipse.core.runtime.IAdaptable)
	 */
	public Command layoutEditParts(
		GraphicalEditPart containerEP,
		IAdaptable layoutHint) {

		List children = containerEP.getChildren();
		return layout(containerEP, children, findRootView(children), layoutHint);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider#layoutEditParts(java.util.List, org.eclipse.core.runtime.IAdaptable)
	 */
	public Command layoutEditParts(
		List selectedObjects,
		IAdaptable layoutHint) {

		if (selectedObjects.size()== 0){
			return null;
		}
		
		GraphicalEditPart editPart = (GraphicalEditPart) selectedObjects.get(0);		
		GraphicalEditPart containerEditPart = (GraphicalEditPart) editPart.getParent();
		
		return layout(containerEditPart, selectedObjects, findRootView(selectedObjects), layoutHint);
	}

	/**
	 * Method layout.
	 * 
	 * @param layoutType
	 * @param containerEP
	 * @param selectedObjects
	 * @param rootEP
	 * @return Command
	 * @throws InvalidParameterException
	 *             if either parameter is null.
	 */
	public Command layout(
		GraphicalEditPart containerEP,
		List selectedObjects,
		ShapeEditPart rootEditPart,
		IAdaptable layoutHint) {

		if (containerEP == null || selectedObjects == null) {
			InvalidParameterException ipe = new InvalidParameterException();
			Trace.throwing(DiagramProvidersPlugin.getInstance(), DiagramActionsDebugOptions.EXCEPTIONS_THROWING, getClass(), "layout()", //$NON-NLS-1$
			ipe);
			throw ipe;
		}

		if (rootEditPart == null)
			rootEditPart = findRootView(selectedObjects);

		List parts = new ArrayList(selectedObjects.size());

		// Only add IShapeView to the master list
		ListIterator li = selectedObjects.listIterator();
		while (li.hasNext()) {
			EditPart ep = (EditPart) li.next();
			if (!ep.equals(rootEditPart)
				&& (ep instanceof ShapeEditPart
					|| ep instanceof ConnectionNodeEditPart)) {
				parts.add(ep);
			}
		}

		Command cmd = null;
		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
		RadialLayout radialLayout =
			new RadialLayout(rootEditPart, parts, 0, Math.PI * 2, false);

		cmd = radialLayout.getPrelayoutCommand();
		if (cmd != null)
			cc.add(cmd);
		
		try {
			cmd = radialLayout.getCommand();
			parts.add(rootEditPart);
		} catch (LayoutEstheticsException e) {
			// since the Layout esthetics have been violated, use the default layout
			// instead.
			parts.add(rootEditPart);
			
			ArrangeRequest request = new ArrangeRequest(
				RequestConstants.REQ_ARRANGE_DEFERRED);
			request.setViewAdaptersToArrange(parts);
			cmd = containerEP.getCommand(request);
		}
		
		if (cmd != null)
			cc.add(cmd);
		
		Request req = new Request(RequestConstants.REQ_REFRESH);
		cmd = rootEditPart.getParent().getCommand(req);
		if (cmd != null)
			cc.add(cmd);
		
		// position the entire radial circle
		OffsetRadialPartsCommand orpc = new OffsetRadialPartsCommand(parts); 
		cmd = new EtoolsProxyCommand(orpc);
		if (cmd != null)
			cc.add(cmd);
		
		return cc;
	}

	/**
	 * @author sshaw
	 *
	 * Command to update the entire position of the Radial circle.
	 */
	static protected class OffsetRadialPartsCommand extends AbstractModelCommand {
		private List editParts;
		private Rectangle origRect;
		
		/**
		 * @param editParts
		 * @param ptRoot
		 */
		public OffsetRadialPartsCommand(List editParts) {
			super("", null); //$NON-NLS-1$
			this.editParts = editParts;
			origRect = calcBoundBox();
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.gef.commands.Command#execute()
		 */
		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			if (null == editParts)
				return newCancelledCommandResult();
			
			Rectangle radialRect = calcBoundBox();
			
			IGraphicalEditPart firstEP = (IGraphicalEditPart)editParts.get(0);
			IMapMode mm = MapModeUtil.getMapMode(firstEP.getFigure());
			
			// consider ideal location
			Rectangle newRadialRect = new Rectangle(radialRect);
			newRadialRect.translate( -radialRect.getTopLeft().x + mm.DPtoLP(50),
							 -radialRect.getTopLeft().y + mm.DPtoLP(50));
			
			if (origRect.x > newRadialRect.x && origRect.y > newRadialRect.y) {
				newRadialRect = new Rectangle( Math.max(newRadialRect.x, origRect.x - (radialRect.width / 2)), 
											   Math.max(newRadialRect.y, origRect.y - (radialRect.height / 2)), 
											radialRect.width, radialRect.height);
			}
			
			final Point translate = new Point(newRadialRect.getTopLeft().x - radialRect.getTopLeft().x,
										newRadialRect.getTopLeft().y - radialRect.getTopLeft().y);
			 
			ListIterator li = editParts.listIterator();
			while (li.hasNext()) {
				IGraphicalEditPart gep = (IGraphicalEditPart)li.next();
				View view = gep.getNotationView();
				if (view!=null){
					Integer pos = (Integer)ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_X());
					ViewUtil.setStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_X(), new Integer(pos.intValue() + translate.x));
					pos = (Integer)ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_Y());
					ViewUtil.setStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_Y(), new Integer(pos.intValue() + translate.y));
				}
			}
			
			// clear for garbage collection;
			editParts = null;
			return newOKCommandResult();
		}

		/**
		 * @return
		 */
		private Rectangle calcBoundBox() {
			Rectangle radialRect = null;
			ListIterator li = editParts.listIterator();
			while (li.hasNext()) {
				IGraphicalEditPart gep = (IGraphicalEditPart)li.next();
				if (null == radialRect) {
					radialRect = new Rectangle(gep.getFigure().getBounds());
				}
				else {
					radialRect.union(gep.getFigure().getBounds());
				}
			}
			return null == radialRect ?  new Rectangle() : radialRect;
		}
	}
	
	/**
	 * Method findRootView. Given a list of views, calculate the root view that
	 * all other views are ultimately related to.
	 * 
	 * @param views
	 *            List of editparts to determine the root view from.
	 * @return ShapeEditPart shape editpart object that represents the root
	 *         view.
	 */
	protected ShapeEditPart findRootView(List editparts) {
		if (editparts == null)
			throw new InvalidParameterException();

		// TodoKit: I am sure we must find better ways to dig up the root of a
		// tree, for now I assume it to be
		// the first in the collection as I know it was the first view created.
		int count = editparts.size(); 
		if (count > 0) {
			EditPart ep = (EditPart) editparts.get(0);
			if (ep instanceof ShapeEditPart) {
				return (ShapeEditPart) editparts.remove(0);
			}
		}

		return null;
	}

	/**
	 * @author sshaw
	 *
	 * Nested RuntimeException class thrown when the esthetics of the RadialLayout
	 * are violated.  i.e. when certain conditions are met that ensure that the RadialLayout
	 * will not look good.
	 */
	static protected class LayoutEstheticsException extends RuntimeException {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3084395663087786098L;

		/**
		 * @param message
		 */
		public LayoutEstheticsException(String message) {
			super(message);
		}
	}
	
	/**
	 * @author sshaw
	 * 
	 * Helper class to build the radial layout based on a root editpart.
	 */
	static protected class RadialLayout {

		private ShapeEditPart rootEP;
		private List allEditparts = new ArrayList();
		private double startTheta;
		private double totalTheta;
		private boolean rootPositionLocked;

		public RadialLayout(
			ShapeEditPart rootEP,
			List shapeViews,
			double startTheta,
			double totalTheta,
			boolean rootPositionLocked) {
			this.rootEP = rootEP;
			this.allEditparts.addAll(shapeViews);
			this.startTheta = startTheta;
			this.totalTheta = totalTheta;
			this.rootPositionLocked = rootPositionLocked;
		}

		/**
		 * Method getRootEditPart.
		 * 
		 * @return ShapeEditPart
		 */
		public ShapeEditPart getRootEditPart() {
			return rootEP;
		}

		/**
		 * Method getCommand.
		 * 
		 * @return Command
		 */
		public Command getCommand() throws LayoutEstheticsException {
			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
			Command cmd = null;
			List restViews = new ArrayList();
			
			List firstCircleParts = new ArrayList();
			
			int innerRingCount =
				findChildViews(rootEP, allEditparts, firstCircleParts, restViews);

			double theta = Math.PI;
			if (innerRingCount > 1) {
				theta = totalTheta / innerRingCount;
			} 
						
			// initialize the childAndSectionViewMap structure
			Map childAndSectionMap = new Hashtable(firstCircleParts.size());
			List circleSectionParts = null;
			List firstCircleShapes = new ArrayList(childAndSectionMap.keySet().size());
			
			for (int i = 0; i < firstCircleParts.size(); i++) {
				EditPart ep = (EditPart) firstCircleParts.get(i);
				if (ep instanceof ShapeEditPart) {
					ShapeEditPart shapeEP = (ShapeEditPart) ep;
					circleSectionParts =
						new ArrayList(firstCircleParts.size());

					List restRestViews = new ArrayList();
					findChildViews(
						shapeEP,
						restViews,
						circleSectionParts,
						restRestViews);

					firstCircleShapes.add(ep);
					childAndSectionMap.put(ep, circleSectionParts);
					childAndSectionMap.put(circleSectionParts, restRestViews);
				} 
			}
			
			CalculateRadialInfoCommand radialInfoCmd = 
				new CalculateRadialInfoCommand(rootEP, firstCircleShapes, theta);
			cc.add(radialInfoCmd);
			
			cmd = positionRings(firstCircleParts, childAndSectionMap, theta, radialInfoCmd);
			if (cmd != null)
				cc.add(cmd);
			
			// route any extra connection, restViews should only contain
			// connections by now, all other views has
			// better be placed already.
			cmd = routeConnection(firstCircleParts);
			if (cmd != null)
				cc.add(cmd);
			
			double increaseTheta = theta;
			cmd = positionNextRings(firstCircleParts, childAndSectionMap, increaseTheta );
			if (cmd != null)
				cc.add(cmd);
			
			return cc;
		}
		
		/**
		 * getPrelayoutCommand
		 * Initializes the set of shapes for the layout operation.
		 * @return Cpmmand that will initialize the shapes for the layout operation.
		 */
		private Command getPrelayoutCommand() {
			List restViews = new ArrayList();
			List firstCircleParts = new ArrayList();
			
			findChildViews(rootEP, allEditparts, firstCircleParts, restViews);
			Command cmd = diminishCircle(firstCircleParts);
				
			int size = getFontSize(rootEP);
			int fontAdjust = size / 8;

			size -= fontAdjust;

			Command c2 = diminishCircle(restViews, size);
			if (c2 != null) {
				if (cmd != null)
					cmd.chain(c2);
				else
					cmd = c2;
			}

			Request req = new Request(RequestConstants.REQ_REFRESH);
			Command c3 = rootEP.getParent().getCommand(req);
			if (c3 != null) {
				if (cmd != null)
					cmd.chain(c3);
				else
					cmd = c3;
			}
			
			return cmd;
		}

		/**
		 * positionNextRings
		 * Method to handle the recursion of the RadialLayout.  
		 * 
		 * @param firstCircleParts
		 * @param childAndSectionMap
		 * @param theta
		 * @return
		 */
		private Command positionNextRings(List firstCircleParts, Map childAndSectionMap, double theta ) {
			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
			int n = 0;
			
			for (int i = 0; i < firstCircleParts.size(); i++) {
				double totalThetaPrim = theta;
				EditPart part = (EditPart) firstCircleParts.get(i);
				if (part instanceof ShapeEditPart) {
					List parts = (List)childAndSectionMap.get(part);
					
					if (parts != null
						&& !parts.isEmpty()) {
						int posViewCount = 0;
						
						n = i;
						// check next in list to see if we can increase theta
						while (i + 1 < firstCircleParts.size()) {
							Object key = firstCircleParts.get(i+1);
							List nextViews = (List)childAndSectionMap.get(key);
							if (null != nextViews && nextViews.size() == 0) {
								totalThetaPrim = Math.min(Math.PI, totalThetaPrim + theta);
								i++;
							}
							else
								break;
						}
						
						// count the ShapeEditParts
						ListIterator li = parts.listIterator();
						while (li.hasNext()) {
							if (li.next() instanceof ShapeEditPart)
								posViewCount++;
						}

						double dTheta = startTheta + (n * theta);
						double thetaPrim = totalThetaPrim / posViewCount;
						double startThetaPrim;
						if (posViewCount < 2) {
							startThetaPrim = dTheta;
						} else {
							startThetaPrim = dTheta - totalThetaPrim / 2 + thetaPrim / 2;
						}

						List restRestViews = (List)childAndSectionMap.get(parts);
						parts.addAll(restRestViews);
						
						RadialLayout radialLayout =
							new RadialLayout((ShapeEditPart)part, parts, startThetaPrim, totalThetaPrim, true);
						Command cmd = radialLayout.getCommand();
						if (cmd != null)
							cc.add(cmd);
					}
				}
			}
			
			if (!cc.isEmpty())
				return cc;
			
			return null;
		}

		/**
		 * @author sshaw
		 * 
		 * This class implements IAdaptable so that a deferred point can be
		 * calculated for an edit part.
		 */
		static protected class RadialPosition implements IAdaptable {

			private ShapeEditPart sep;
			private CalculateRadialInfoCommand radialInfo;
			private double theta;
			private boolean useDelta;
			private Point ptLocation = null;

			/**
			 * Method RadialPosition. Constructor for the inner ring elements.
			 * 
			 * @param sep
			 * @param innerIndex
			 */
			public RadialPosition(ShapeEditPart sep, double theta, CalculateRadialInfoCommand radialInfo, boolean useDelta) {
				this.sep = sep;
				this.theta = theta;
				this.radialInfo = radialInfo;
				this.useDelta = useDelta;
			}

			/**
			 * Method getAdapter.
			 * 
			 * @param adapterType
			 * @return Object
			 */
			public Object getAdapter(Class adapterType) {
				if (adapterType == IPropertyValueDeferred.class) {
					return getPosition();
				}

				return null;
			}

			/**
			 * Method getPosition. Calculates the point based on stored
			 * information about the radius and index of the edit part.
			 * 
			 * @return Point
			 */
			public Point getPosition() {
				if (null == ptLocation) {
					ptLocation = new Point(0, 0);
					
					if (null != radialInfo) {
						ptLocation.x =
							Math.round(
								radialInfo.getRadius() * ((float) Math.cos(theta)) ); 
						ptLocation.y =
							Math.round(
								radialInfo.getRadius() * ((float) Math.sin(theta)) );
						
						if (useDelta)
							ptLocation.translate(radialInfo.getDelta());
					}
					
					ptLocation.translate(
							-sep.getSize().width / 2,
							-sep.getSize().height / 2);
					
					sep = null;
					radialInfo = null;
				}
				
				return ptLocation;
			}
		}

		/**
		 * Method findChildViews. This method finds all of the child views
		 * relative to a given root view and a set of views.
		 * 
		 * @param rootEditPart
		 *            ShapeEditPart to be compared against. If a connection ends
		 *            on this root editpart the end must be related.
		 * @param editparts
		 *            List of a editparts that are used to compare against the
		 *            root editpart.
		 * @param childEPs
		 *            List that is passed in and populated by the method. It
		 *            will be populated the editparts that are related to the
		 *            rootView.
		 * @param restEPs
		 *            List that is passed in and populated by the method. It
		 *            will be populated with the editparts are not related to
		 *            the rootView.
		 * @return int number of related shape editparts in the childViews
		 *         list.
		 */
		protected int findChildViews(
			ShapeEditPart rootEditPart,
			List editparts,
			List childEPs,
			List restEPs) {
			if (rootEditPart == null)
				throw new InvalidParameterException();

			if (childEPs == null)
				throw new InvalidParameterException();

			if (restEPs == null)
				throw new InvalidParameterException();

			Set allSet = new HashSet(editparts.size());
			int posViewCount = 0;
			int count = editparts.size();
			for (int i = 0; i < count; i++) {
				EditPart ep = (EditPart) editparts.get(i);
				allSet.add(ep);
			}

			//get a list of the selected connections and the selected
			//shapes connections
			List connectionEPs = new ArrayList();
			for (int i = 0; i < count; i++) {
				EditPart ep = (EditPart) editparts.get(i);
				if (ep instanceof ShapeEditPart) {
					ShapeEditPart shapeEP = (ShapeEditPart) ep;
					connectionEPs.addAll(shapeEP.getSourceConnections());
					connectionEPs.addAll(shapeEP.getTargetConnections());
				} else if (ep instanceof ConnectionNodeEditPart) {
					connectionEPs.add(ep);
				}
			}

			for (int i = 0; i < connectionEPs.size(); i++) {
				EditPart ep = (EditPart) connectionEPs.get(i);
				if (ep instanceof ConnectionNodeEditPart) {
					ConnectionNodeEditPart connectionEP =
						(ConnectionNodeEditPart) ep;
					EditPart fromEP = connectionEP.getSource();
					EditPart toEP = connectionEP.getTarget();

					EditPart el = null;
					if (fromEP.equals(rootEditPart)) {
						el = toEP;
					} else if (toEP.equals(rootEditPart)) {
						el = fromEP;
					} 

					if (el != null && allSet.contains(el)) {
						childEPs.add(el);
						posViewCount++;
						childEPs.add(connectionEP);

						allSet.remove(el);
					}
				}

			}

			// If the rest collection was requested, pick out all views that
			// was not in the child collection.
			restEPs.addAll(allSet);

			return posViewCount;
		}

		/**
		 * Method getFontSize. Returns the size of the associated font with the
		 * viewEl,
		 * 
		 * @param sep
		 *            ShapeEditPart element to retrieve the fontdata from.
		 * @return int value of the font size (height).
		 */
		protected int getFontSize(ShapeEditPart sep) {
			if (sep == null)
				throw new InvalidParameterException();
			View view = sep.getNotationView();
			if (view!=null){
				FontStyle style = (FontStyle) view.getStyle(NotationPackage.eINSTANCE.getFontStyle());
				if (style != null)
					return style.getFontHeight();
			}
			return 9;
		}

		/**
		 * Method setFontSize. Sets the new font size for a given view element.
		 * This is a convenience wrapper. The same functionality can be
		 * achieved by using the setPropertyValue api.
		 * 
		 * @param viewEl
		 *            IView element to retrieve and set the fontdata from.
		 * @param size
		 *            value of the new font size (height).
		 * @return Command
		 */
		protected Command setFontSize(ShapeEditPart sep, int size) {
			if (sep == null)
				throw new InvalidParameterException();

			ChangePropertyValueRequest cpvr = new ChangePropertyValueRequest(DiagramProvidersResourceManager.getI18NString("RadialProvider.changeFontRequest.label"), Properties.ID_FONTSIZE, new Integer(size)); //$NON-NLS-1$
		
			return getCommand(sep, cpvr, true);
		}

		/**
		 * Method diminishInnerCircle. Given a list of views this method will
		 * parse through them and diminish their size by setting the font.
		 * 
		 * @param editparts
		 *            List of editparts to diminish the size of.
		 * @param fontSize
		 *            int value of the new font size.
		 * @return Command
		 *  
		 */
		protected Command diminishCircle(List editparts) {
			if (editparts == null)
				throw new InvalidParameterException();

			int count = editparts.size();

			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
			
			//diminish by collapsing all compartments and hiding
			// connection labels.
			for (int i = 0; i < count; i++) {
				EditPart editpart = (EditPart) editparts.get(i);
				ChangePropertyValueRequest request = null;

				if (editpart instanceof ShapeEditPart) {
					request = new ChangePropertyValueRequest(
						DiagramProvidersResourceManager
							.getI18NString("RadialProvider.changeVisibilityRequest.label"), Properties.ID_ISVISIBLE, Boolean.FALSE); //$NON-NLS-1$

					ShapeEditPart shapeEditPart = (ShapeEditPart) editpart;
					
					Iterator compartments = shapeEditPart
						.getResizableCompartments().iterator();
					while (compartments.hasNext()) {
						cc.add(((EditPart) compartments.next())
							.getCommand(request));
					}
					
					
				}
			}

			if (!cc.isEmpty())
				return cc;

			return null;
		}

		/**
		 * Method diminishCircle. Given a list of views this method will
		 * parse through them and diminish their size by setting the font and
		 * also by hiding all compartments.
		 * 
		 * @param editparts
		 *            List of editparts to diminish the size of.
		 * @param fontSize
		 *            int value of the new font size.
		 * @return Command
		 *  
		 */
		protected Command diminishCircle(List editparts, int fontSize) {
			if (editparts == null)
				throw new InvalidParameterException();

			long count = editparts.size();

			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$

			// diminish the same as the inner circle first.
			Command cmd = diminishCircle(editparts);
			if (cmd != null)
				cc.add(cmd);
			
			//diminish font for outer circle
			for (int i = 0; i < count; i++) {
				EditPart ep = (EditPart) editparts.get(i);
				if (ep instanceof ShapeEditPart) {
					cmd = setFontSize((ShapeEditPart) ep, fontSize);
					if (cmd != null)
						cc.add(cmd);
				}
			}

			if (!cc.isEmpty())
				return cc;

			return null;
		}

        /**
         * sortFirstCircleParts
         * Sort the circle views in a pattern such that the next iteration of radial 
         * views will be positioned evenly w.r.t. each other.
         * 
         * @param firstCircleViews List of circle views
         * @param childAndSectionViewMap Map of next level views.
         */
        protected void sortFirstCircleParts(List firstCircleParts, Map childAndSectionMap) {
        	List firstCircleShapeParts = new ArrayList(firstCircleParts.size());
        	List rest = new ArrayList(firstCircleParts.size());
        	ListIterator li = firstCircleParts.listIterator();
        	while (li.hasNext()) {
        		Object obj = li.next();
        		if (obj instanceof ShapeEditPart) {
        			firstCircleShapeParts.add(obj);
        		}
        		else
        			rest.add(obj);
        	}
        	
            // figure out how many empty 2nd line children there are
            List emptyNextCircleList = new ArrayList(firstCircleParts.size());
            List nextCircleList = new ArrayList(firstCircleParts.size());
            for (int i = 0; i < firstCircleShapeParts.size(); i++) {
            	List circleList = (List)childAndSectionMap.get(firstCircleShapeParts.get(i));
        		if (null != circleList && circleList.size() == 0)
        			emptyNextCircleList.add(firstCircleShapeParts.get(i));
        		else
        			nextCircleList.add(firstCircleShapeParts.get(i));
            }
            
            firstCircleParts.clear();
            if (nextCircleList.size() > 1) {
            	int addInc = firstCircleShapeParts.size() / nextCircleList.size();
	            int i = 0;
	            while (nextCircleList.size() > 0 || emptyNextCircleList.size() > 0) {
	            	if (i % addInc == 0 && nextCircleList.size() > 0) {
	            		firstCircleParts.add(nextCircleList.remove(0));
	            	}
	            	else {
	            		if (emptyNextCircleList.size() > 0)
	            			firstCircleParts.add(emptyNextCircleList.remove(0));
	            	}
	            	i++;
	            }
            }
            else
            	firstCircleParts.addAll(firstCircleShapeParts);
            
            firstCircleParts.addAll(rest);
        }
        
		/**
		 * Method positionRings. This method positions the view rings around
		 * the root view.
		 * 
		 * @return Command
		 */
		protected Command positionRings(List firstCircleParts, Map childAndSectionMap, 
										double theta, CalculateRadialInfoCommand radialInfo) {
			int n = 0;
 
			if (theta < Math.PI / 32 && isRootPositionLocked())
				throw new LayoutEstheticsException("Angle is too small to resulting in very large radius");//$NON-NLS-1$
			
			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
			List editParts = new ArrayList();
			editParts.add(rootEP);
			
			sortFirstCircleParts(firstCircleParts, childAndSectionMap);
			
			ListIterator li = firstCircleParts.listIterator();
			while (li.hasNext()) {
				EditPart editpart = (EditPart) li.next();

				if (editpart instanceof ShapeEditPart) {
					ShapeEditPart sep = (ShapeEditPart) editpart;
					editParts.add(sep);
					
					IAdaptable deferredPos = new RadialPosition(sep, startTheta + n * theta, radialInfo, isRootPositionLocked());

					ChangeBoundsDeferredRequest request =
						new ChangeBoundsDeferredRequest(deferredPos);
					
					Command cmd = sep.getCommand(request);
					
					if (cmd != null)
						cc.add(cmd);

					n++;
				}
			}
			
			if (!isRootPositionLocked()) {
				IAdaptable deferredRootPos = new RadialPosition(getRootEditPart(), 0, null, isRootPositionLocked());
				ChangeBoundsDeferredRequest request = new ChangeBoundsDeferredRequest(deferredRootPos);
				Command cmd = rootEP.getCommand(request);
				if (cmd != null)
					cc.add(cmd);
			}
			
			if (!cc.isEmpty())
				return cc;

			return null;
		}

		/**
		 * Method routeConnections.
		 * 
		 * @param connections
		 *            List of connections that need to be routed.
		 * @return Command
		 */
		protected Command routeConnection(List connections) {

			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
			ListIterator li = connections.listIterator();

			while (li.hasNext()) {
				EditPart editpart = (EditPart) li.next();
				if (editpart instanceof ConnectionNodeEditPart) {
					Command cmd =
						routeConnection((ConnectionNodeEditPart) editpart);
					if (cmd != null)
						cc.add(cmd);
				}
			}

			if (!cc.isEmpty())
				return cc;

			return null;
		}

		/**
		 * Method routeConnection. Route the given connection accordingly to the
		 * layout algorithm. TBD utilize the "avoid obstructions" routing.
		 * 
		 * @param connectionEP
		 *            ConnectionNodeEditPart connection to be routed.
		 * @return Command
		 */
		protected Command routeConnection(ConnectionNodeEditPart connectionEP) {
			if (connectionEP == null)
				throw new InvalidParameterException();

			// reset connections

			Connection connection = connectionEP.getConnectionFigure();
			PointList newPoints = new PointList(2);
			newPoints.addPoint(connection.getPoints().getFirstPoint());
			newPoints.addPoint(connection.getPoints().getLastPoint());
			SetAllBendpointRequest request =
				new SetAllBendpointRequest(
					RequestConstants.REQ_SET_ALL_BENDPOINT,
					newPoints);

			// recurse through the children to get the compound command
			return connectionEP.getCommand(request);
		}

		/**
		 * Method getCommand. Utility function to optionally recurse through
		 * all child edit parts to send the request to.
		 * 
		 * @param editpart
		 *            EditPart at the Top level to send the command request to.
		 * @param request
		 *            Request that is sent to the EditPart and it's children.
		 * @param bRecursive
		 *            boolean true if the method is to send the request to all
		 *            the children of the editpart as well, false otherwise.
		 * @return Command that is the result of the request to be executed.
		 */
		protected Command getCommand(
			EditPart editpart,
			Request request,
			boolean bRecursive) {
			List children = editpart.getChildren();
			ListIterator li = children.listIterator();

			CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
			Command cmd = editpart.getCommand(request);
			
			if (cmd != null)
				cc.add(cmd);

			if (bRecursive) {
				while (li.hasNext()) {
					IGraphicalEditPart childEP = (IGraphicalEditPart) li.next();
					cmd = getCommand(childEP, request, bRecursive);
					if (cmd != null)
						cc.add(cmd);
				}
			}

			if (!cc.isEmpty())
				return cc;

			return null;
		}
		
		/**
		 * @author sshaw
		 * 
		 * This will perform some interim calculation that depends on the
		 * previous command execution for setting the proper sizes for the view
		 * elements.
		 */
		static protected class CalculateRadialInfoCommand extends Command {

			//	deferred calculation values
			private int radius;
			private double theta;
			private ShapeEditPart rootEP;
			private List firstCircleViews;
			
			public CalculateRadialInfoCommand(ShapeEditPart rootEP, List firstCircleViews, double theta) {
				this.rootEP = rootEP;
				this.firstCircleViews = firstCircleViews;
				this.theta = theta;
			}
	        
			public void execute() {
				radius =
					calculateNeededRadius(firstCircleViews, firstCircleViews.size() * theta);

				// if innerradius is less than the 2 times the diagonal of the
				// RootView extend it some.
				double rootDiagonal = getViewWorstExtent(rootEP);
				if (2 * rootDiagonal > radius) {
					radius += rootDiagonal;
				}
				
				radius = Math.max(MapModeUtil.getMapMode(rootEP.getFigure()).DPtoLP(180), radius);
			}

			/**
			 * Method calculateNeededRadius. This method calculates the minimum
			 * radius needed to fully extent the given views away from a center
			 * point.
			 * 
			 * @param circleEPs
			 *            List of editparts that the radius will be calculated
			 *            from.
			 * @param sectionAngle
			 *            This is the angle in radians that the views will
			 *            extent around.
			 * @return int value of the calculated radius.
			 */
			protected int calculateNeededRadius(
				List circleEPs,
				double sectionAngle) {
				if (circleEPs == null)
					throw new InvalidParameterException();

				double neededDiameter = 0;
				int count = circleEPs.size();
				double maxDiagonal = 0;

				for (int i = 0; i < count; i++) {

					EditPart ep = (EditPart) circleEPs.get(i);
					if (ep instanceof ShapeEditPart) {
						ShapeEditPart sep = (ShapeEditPart) ep;

						double diagonal = getViewWorstExtent(sep);

						neededDiameter += diagonal;

						if (diagonal > maxDiagonal)
							maxDiagonal = diagonal;
					}

				}

				double rad;

				rad = neededDiameter / sectionAngle;

				return (int) Math.round(rad);
			}

			/**
			 * Method getViewWorstExtent. Determines the worst case extent of a
			 * given view to ensure no intersection occurs. The diagonal of the
			 * view extent is used to for this value.
			 * 
			 * @param sep
			 *            ShapeEditPart to calcualte to the worst case extent
			 *            from.
			 * @return double value of the biggest extent where no intersection
			 *         will occur with the view.
			 */
			protected double getViewWorstExtent(ShapeEditPart sep) {
				if (sep == null)
					throw new InvalidParameterException();

				Dimension ext = sep.getSize();				
				return Math.sqrt( 
					(ext.width * ext.width) + (ext.height * ext.height)) * 0.80;
			}

			/**
			 * @return Returns the radius.
			 */
			public int getRadius() {
				return radius;
			}

			/**
			 * @return Returns the delta.
			 */
			public Point getDelta() {
				View view = rootEP.getNotationView();
				if (view!=null){
					Integer posX = (Integer)ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_X());
					Integer posY = (Integer)ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLocation_Y());
					return new Point(posX.intValue(), posY.intValue());
				}
				return new Point(0,0);
			}
		}
		
		/**
		 * @return Returns the rootPositionLocked.
		 */
		public boolean isRootPositionLocked() {
			return rootPositionLocked;
		}
	}
}
