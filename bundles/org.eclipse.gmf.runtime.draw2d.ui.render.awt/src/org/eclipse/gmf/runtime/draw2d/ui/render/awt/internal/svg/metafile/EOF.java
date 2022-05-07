/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**
 * This is a placeholder.  It handles the EOF metafile record which appears at the end of
 * every Enhanced Metafile, though it does nothing.
 * @author dhabib
 */
public class EOF implements IEmf2SvgConverter
{
	public void readEMFRecord( Record rec ) throws IOException
	{
	    // No data to read at the moment.
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
	    // Currently we just discard this record.
	}

}
