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

package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractConnectorView;


/**
 * @author mmostafa
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class DummyConnectorView extends AbstractConnectorView {
	/**
	 * Incarnation Constructor
	 * @param state
	 */
	public DummyConnectorView(Object state) {
		super(state);
	}
}
