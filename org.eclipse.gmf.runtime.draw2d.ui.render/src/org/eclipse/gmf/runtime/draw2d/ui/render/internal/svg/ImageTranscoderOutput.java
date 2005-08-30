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

import org.apache.batik.transcoder.TranscoderOutput;

/**
 * @author sshaw
 *
 * This is a dummy class used by the SWTImageTranscoder to satisfy the api 
 * requirements for the Batik transcoder class.  It doesn't do anything except
 * provide type information.
 */
class ImageTranscoderOutput extends TranscoderOutput {
	
	/**
	 * Creates a new instance of SWTImageTranscoderOutput
	 */
	public ImageTranscoderOutput() {
		// empty block
	}
}
