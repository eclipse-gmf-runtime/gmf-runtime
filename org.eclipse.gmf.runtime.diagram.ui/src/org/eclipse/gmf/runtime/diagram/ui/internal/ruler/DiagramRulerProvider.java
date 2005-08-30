/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands.CreateGuideCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands.DeleteGuideCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands.MoveGuideCommand;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Custom RulerProvider methods.
 * 
 * @author jschofie
 */
public class DiagramRulerProvider extends RulerProvider {

	/*
	 * PropertyChangeListerner for Rulers.
	 */
	private PropertyChangeListener rulerListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {

			if( evt instanceof NotificationEvent ) {
				handleNotificationEvent( (NotificationEvent)evt );
				return;
			}
		}
		
		private void handleNotificationEvent(NotificationEvent event) {
			Object feature = event.getFeature();

			if( (feature == NotationPackage.eINSTANCE.getGuideStyle_HorizontalGuides() &&
						!theRuler.isHorizontal() ) ||
				feature == NotationPackage.eINSTANCE.getGuideStyle_VerticalGuides() &&
						theRuler.isHorizontal())
			{
				Guide guide = null;

				// Add a new Guide
				if( event.getNewValue() != null && event.getOldValue() == null ) {
					guide = (Guide)event.getNewValue();
					PresentationListener.getNotifier(guide).addPropertyChangeListener(guideListener);
				}

				// Remove Guide
				if( event.getNewValue() == null && event.getOldValue() != null ) {
					guide = (Guide)event.getOldValue();
					PresentationListener.getNotifier(guide).removePropertyChangeListener(guideListener);
				}
				
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener)listeners.get(i))
							.notifyGuideReparented(guide);
				}
			}
		}
	};

	private PropertyChangeListener guideListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			
			if( evt instanceof NotificationEvent ) {
				handleNotificationEvent( (NotificationEvent)evt );
				return;
			}
		}
		
		private void handleNotificationEvent(NotificationEvent event) {
		
			Object feature = event.getFeature();
			
			// Notify when the guide's position changes
			if( feature == NotationPackage.eINSTANCE.getGuide_Position() ) {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener)listeners.get(i))
							.notifyGuideMoved(event.getSource());
				}
			}
			
			// Notify when parts are attached and detached
			if( feature == NotationPackage.eINSTANCE.getGuide_NodeMap() ) {

				refreshMap();

				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener)listeners.get(i))
							.notifyPartAttachmentChanged(event.getNewValue(), event.getSource());
				}
			}
		}
	};
	
	private DiagramRuler theRuler;

	public DiagramRulerProvider( DiagramRuler ruler ) {
		theRuler = ruler;
	}
	
	public void init() {
		theRuler.addPropertyChangeListener(rulerListener);

		Iterator iter = getGuides().iterator();
		while(iter.hasNext()) {
			Guide guide = (Guide)iter.next();
			PresentationListener.getNotifier(guide).addPropertyChangeListener(guideListener);
		}
		
		refreshMap();
	}
	
	public void uninit() {
		theRuler.removePropertyChangeListener(rulerListener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getRuler()
	 */
	public Object getRuler() {
		return theRuler;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getAttachedModelObjects(java.lang.Object)
	 */
	public List getAttachedModelObjects(Object guide) {
		Guide toGet = (Guide)guide;
		
		if( toGet.getNodeMap().size() == 0 )
			return Collections.EMPTY_LIST;
	
		List toReturn = new ArrayList();
		
		Iterator iter = toGet.getNodeMap().keySet().iterator();
		while(iter.hasNext()) {
			Node node = (Node)iter.next();
			if( node != null )
				toReturn.add(node);
		}
	
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getCreateGuideCommand(int)
	 */
	public Command getCreateGuideCommand(int position) {
		return new EtoolsProxyCommand( new CreateGuideCommand(theRuler, position) );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getDeleteGuideCommand(java.lang.Object)
	 */
	public Command getDeleteGuideCommand(Object guide) {
		return new EtoolsProxyCommand( new DeleteGuideCommand((Guide)guide) );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getMoveGuideCommand(java.lang.Object, int)
	 */
	public Command getMoveGuideCommand(Object guide, int pDelta) {
		CompoundCommand cmd = new CompoundCommand(PresentationResourceManager.getInstance().getString( "Command.moveGuide" ));  //$NON-NLS-1$

		// Get the Command to Move the Guide
		cmd.add( new EtoolsProxyCommand( new MoveGuideCommand((Guide)guide, pDelta) ) );
		
		// Get the Commands to Remove attached model objects
		Iterator iter = getAttachedModelObjects(guide).iterator();
		while (iter.hasNext()) {
			View part = (View)iter.next();
			
			int x = ((Integer) ViewUtil.getPropertyValue(part,Properties.ID_POSITIONX)).intValue();
			int y = ((Integer) ViewUtil.getPropertyValue(part,Properties.ID_POSITIONY)).intValue();

			SetPropertyCommand spc;

			if( ((DiagramRuler)getRuler()).isHorizontal()) {
				x += MapMode.DPtoLP(pDelta);
				spc = new SetPropertyCommand(new EObjectAdapter(part), Properties.ID_POSITIONX, Properties.ID_POSITIONX, new Integer(x));
			} else {
				y += MapMode.DPtoLP(pDelta);
				spc = new SetPropertyCommand(new EObjectAdapter(part), Properties.ID_POSITIONY, Properties.ID_POSITIONY, new Integer(y));
			}

			cmd.add( new EtoolsProxyCommand(spc) );
		}

		return cmd.unwrap();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuideAt(int)
	 */
	public Object getGuideAt(int position) {
		List guides = getGuides();
		for (int i = 0; i < guides.size(); i++) {
			Object guide = guides.get(i);
			if (position >= (getGuidePosition(guide)-2) &&
					position <= (getGuidePosition(guide)+2) ) {
				return guide;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuidePositions()
	 */
	public int[] getGuidePositions() {
		List guides = getGuides();
		int[] result = new int[guides.size()];
		for (int i = 0; i < guides.size(); i++) {
			result[i] = ((Guide)guides.get(i)).getPosition();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getUnit()
	 */
	public int getUnit() {
		return theRuler.getUnit();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#setUnit(int)
	 */
	public void setUnit(int newUnit) {
		theRuler.setUnit(newUnit);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuidePosition(java.lang.Object)
	 */
	public int getGuidePosition(Object guide) {
		return ((Guide)guide).getPosition();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuides()
	 */
	public List getGuides() {
		return theRuler.getGuides();
	}

	public void refreshMap() {
		if( theRuler.isHorizontal() )
			refreshVerticalGuideMap();
		else 
			refreshHorizontalGuideMap();
	}

	/**
	 * 
	 */
	private void refreshHorizontalGuideMap()
	{
		List guideList = getGuides();
		List viewList = DiagramGuide.getInstance().getViews();

		Iterator iter = guideList.iterator();
		while( iter.hasNext() ) {
			Guide guide = (Guide)iter.next();
			List attachedViewsList = getAttachedModelObjects(guide);
			
			Iterator viewIter = attachedViewsList.iterator();
			while( viewIter.hasNext() ) {
				View view = (View)viewIter.next();
				if( viewList.contains(view) )
					viewList.remove(view);
				else
					DiagramGuide.getInstance().setHorizontalGuide(view,guide);
			}
			
			// Remove remaining views
			viewIter = viewList.iterator();
			while( viewIter.hasNext() ) {
				View view = (View)viewIter.next();
				DiagramGuide.getInstance().setHorizontalGuide(view, null);
			}
		}
	}

	/**
	 * 
	 */
	private void refreshVerticalGuideMap()
	{
		List guideList = getGuides();
		List viewList = DiagramGuide.getInstance().getViews();

		Iterator iter = guideList.iterator();
		while( iter.hasNext() ) {
			Guide guide = (Guide)iter.next();
			List attachedViewsList = getAttachedModelObjects(guide);
			
			Iterator viewIter = attachedViewsList.iterator();
			while( viewIter.hasNext() ) {
				View view = (View)viewIter.next();
				//Node node = (Node)view.getAdapter(Node.class);

				if( viewList.contains(view) )
					viewList.remove(view);
				else
					DiagramGuide.getInstance().setVerticalGuide(view,guide);
			}
			
			// Remove remaining views
			viewIter = viewList.iterator();
			while( viewIter.hasNext() ) {
				View view = (View)viewIter.next();
				DiagramGuide.getInstance().setVerticalGuide(view, null);
			}
		}
	}
}
