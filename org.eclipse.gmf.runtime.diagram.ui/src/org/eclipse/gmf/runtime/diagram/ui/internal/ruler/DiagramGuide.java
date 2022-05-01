/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.gmf.runtime.notation.Alignment;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Representation of a guide.  
 *
 * In addition to maintaining information about which parts are attached to the guide,
 * also maintains information about the edge along which those parts are attached.
 * This information is useful during resize operations to determine the attachment status of a part.
 *
 *  @author jschofie
 */
public class DiagramGuide {

	private static DiagramGuide self = null;

	// This is required to support moving shapes with the guidelines
	private WeakHashMap guideMap;
	
	/**
	 * Private constructor used to protect Singleton
	 */
	private DiagramGuide() {
		guideMap = new WeakHashMap();
	}

	public static DiagramGuide getInstance() {
		if( self == null )
			self = new DiagramGuide();
		return self;
	}

	public List getViews() {
		return new ArrayList(guideMap.keySet());
	}

	public Guide getHorizontalGuide(View part) {
		GuideMap theMap = (GuideMap)guideMap.get(part);
		if( theMap == null ) {
			return null;
		}

		return theMap.getHorizontal();
	}
	
	/**
	 * This method returns the edge along which the given part is attached to this guide.
	 * This information is used by the XYLayoutEditPolicy to determine whether to attach
	 * or detach a part from a guide during resize operations.
	 * 
	 * @param	part	The part whose alignment has to be found
	 * @return	an int representing the edge along which the given part is attached
	 * 			to this guide
	 */
	public int getHorizontalAlignment(View part) {
		Guide guide = getHorizontalGuide(part);
		
		if( guide == null )
			return -2; // Not attached
		
		EMap nodes = guide.getNodeMap();
		Alignment align = (Alignment)nodes.get(part);

		if (align != null) {
			switch( align.getValue() ) {
				case Alignment.TOP:
				case Alignment.LEFT:
					return -1;
				case Alignment.CENTER:
					return 0;
				case Alignment.BOTTOM:
				case Alignment.RIGHT:
					return 1;
			}
		}

		return -2;
	}

	/**
	 * This method returns the edge along which the given part is attached to this guide.
	 * This information is used by the XYLayoutEditPolicy to determine whether to attach
	 * or detach a part from a guide during resize operations.
	 * 
	 * @param	part	The part whose alignment has to be found
	 * @return	an int representing the edge along which the given part is attached
	 * 			to this guide
	 */
	public int getVerticalAlignment(View part) {
		Guide guide = getVerticalGuide(part);
		
		if( guide == null )
			return -2; // Not attached
		
		EMap nodes = guide.getNodeMap();
		Alignment align = (Alignment)nodes.get(part);

		if (align != null) {
			switch( align.getValue() ) {
				case Alignment.TOP:
				case Alignment.LEFT:
					return -1;
				case Alignment.CENTER:
					return 0;
				case Alignment.BOTTOM:
				case Alignment.RIGHT:
					return 1;
			}
		}
	
		return -2;
	}

	public Guide getVerticalGuide(View part) {
		GuideMap theMap = (GuideMap)guideMap.get(part);
		if( theMap == null ) {
			return null;
		}
		
		return theMap.getVertical();
	}
	
	public void setHorizontalGuide(View view,Guide toSet) {
		GuideMap theMap = (GuideMap)guideMap.get(view);
		if( theMap == null && toSet != null ) {
			theMap = new GuideMap();
			guideMap.put(view,theMap);
		}
		
		if( theMap != null )
			theMap.setHorizontal(toSet);
		
		checkAndRemove(view, theMap);
	}

	public void setVerticalGuide(View view, Guide toSet) {
		GuideMap theMap = (GuideMap)guideMap.get(view);
		if( theMap == null && toSet != null ) {
			theMap = new GuideMap();
			guideMap.put(view,theMap);
		}
		
		if( theMap != null )
			theMap.setVertical(toSet);
		
		checkAndRemove(view, theMap);
	}
	
	private void checkAndRemove(View view, GuideMap map)
	{
		if( map != null && map.getHorizontal() == null &&
				map.getVertical() == null ) {
			guideMap.remove(view);
		}
	}
}