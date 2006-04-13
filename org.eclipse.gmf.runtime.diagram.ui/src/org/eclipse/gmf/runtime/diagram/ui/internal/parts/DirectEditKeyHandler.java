/******************************************************************************
 * Copyright (c) 2006  IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Dmitry Stadnik (Borland) - contribution for bugzilla 136582
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class DirectEditKeyHandler extends KeyHandler {

	private GraphicalViewer viewer;

	/**
     * Constructor 
     * 
	 * @param viewer the <code>GraphicalViewer</code> that is the receiving viewer of the direct edit
     * request.
	 */
	public DirectEditKeyHandler(GraphicalViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * @return the <code>GraphicalViewer</code> that is the receiving viewer of the direct edit
     * request.
	 */
	protected GraphicalViewer getViewer() {
		return viewer;
	}

	/**
	 * @return the <code>GraphicalEditPart</code> that is the currently in focus inside
     * the <code>GraphicalViewer</code>
	 */
	protected GraphicalEditPart getFocusPart() {
		return (GraphicalEditPart) getViewer().getFocusEditPart();
	}

	/**
	 * Tests to see if the key pressed was an letter or number
	 * @param event KeyEvent to be tested
	 * @return true if the key pressed is Alpha Numeric, otherwise false.
	 */
	protected boolean isAlphaNum(KeyEvent event) {

		final String allowedStartingCharacters = "`~!@#$%^&*()-_=+{}[]|;:',.<>?\""; //$NON-NLS-1$

		// IF the character is a letter or number or is contained
		// in the list of allowed starting characters ...
		if (Character.isLetterOrDigit(event.character)
			|| !(allowedStartingCharacters.indexOf(event.character) == -1)) {

			// And the character hasn't been modified or is only modified
			// with SHIFT
			if (event.stateMask == 0 || event.stateMask == SWT.SHIFT)
				return true;
		}

		return false;
	}

	/* 
     * (non-Javadoc)
	 * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public boolean keyPressed(KeyEvent event) {
		if (isAlphaNum(event)) {
			// Create a Direct Edit Request and cache the character typed
			Request request = new Request(RequestConstants.REQ_DIRECT_EDIT);
			request.getExtendedData().put(RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR, new Character(event.character));
			// Send the request to the current edit part in focus
			getFocusPart().performRequest(request);
			return true;
		}
		return super.keyPressed(event);
	}
}
