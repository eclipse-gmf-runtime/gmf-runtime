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
