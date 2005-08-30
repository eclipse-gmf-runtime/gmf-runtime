/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.editparts;

import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedScalableFreeformLayeredPane;

/**
 * A specialized <code>DiagramRootEditPart</code> that supports rendering of
 * images.
 * 
 * @author cmahoney
 */
public class RenderedDiagramRootEditPart
	extends DiagramRootEditPart {

	/**
	 * Creates a scalable freeform layered pane that supports rendering of
	 * images.
	 */
	protected ScalableFreeformLayeredPane createScalableFreeformLayeredPane() {
		setLayers(new RenderedScalableFreeformLayeredPane());
		return getLayers();
	}
	
	/**
	 * 
	 */
	protected void refreshEnableAntiAlias() {
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getPreferencesHint().getPreferenceStore();
		boolean antiAlias = preferenceStore.getBoolean(
			IPreferenceConstants.PREF_ENABLE_ANTIALIAS);
		if (getLayers() instanceof RenderedScalableFreeformLayeredPane)
			((RenderedScalableFreeformLayeredPane) getLayers()).setAntiAlias(antiAlias);
	}

	
	
}