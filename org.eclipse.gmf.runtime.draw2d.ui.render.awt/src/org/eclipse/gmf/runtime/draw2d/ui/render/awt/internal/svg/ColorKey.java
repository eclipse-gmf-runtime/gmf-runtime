/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg;

import java.awt.Color;

import org.apache.batik.transcoder.TranscodingHints.Key;

/**
 * @author sshaw
 *
 * ColorKey class used for storing hints with the SWTImageTranscoder
 */
public class ColorKey extends Key {

	/* (non-Javadoc)
	 * @see org.apache.batik.transcoder.TranscodingHints.Key#isCompatibleValue(java.lang.Object)
	 */
	public boolean isCompatibleValue(Object v) {
		return (v instanceof Color);
	}

}
