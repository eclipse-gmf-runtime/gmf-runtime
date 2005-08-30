/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg;

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
