/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
