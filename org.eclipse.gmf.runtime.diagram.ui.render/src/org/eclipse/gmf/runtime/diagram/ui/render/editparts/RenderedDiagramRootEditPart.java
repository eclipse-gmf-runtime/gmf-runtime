/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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