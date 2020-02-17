/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ITraceMe;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

public class ModifyWorldTransform implements IEmf2SvgConverter, ITraceMe {

	// From MSDN:
	//
	// MWT_IDENTITY
	// Resets the current world transformation by using the identity matrix. If
	// this mode is specified, the XFORM structure pointed to by lpXform is
	// ignored.

	// MWT_LEFTMULTIPLY
	// Multiplies the current transformation by the data in the XFORM structure.
	// (The data in the XFORM structure becomes the left multiplicand, and the
	// data for the current transformation becomes the right multiplicand.)

	// MWT_RIGHTMULTIPLY
	// Multiplies the current transformation by the data in the XFORM structure.
	// (The data in the XFORM structure becomes the right multiplicand, and the
	// data for the current transformation becomes the left multiplicand.)

	private static final int	XFORM_OFFSET		= 0;
	private static final int	MWT_OFFSET			= 24;

	private int _mwt;
	private float[] _xform = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};

	public void readEMFRecord(Record rec) throws IOException {
		_xform = rec.getTransformAt(XFORM_OFFSET);
		_mwt = rec.getIntAt(MWT_OFFSET);
	}

	public void render(Graphics2D g, DeviceContext context) throws TranscoderException {
		context.modifyWorldTransform(_mwt, _xform);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("XForm=("); //$NON-NLS-1$
		for (int ix=0; ix < 6; ix++) {
			addFloat(sb, _xform[ix], ix < 5);
		}
		switch (_mwt) {
		case DeviceContext.MWT_IDENTITY:
			sb.append(", IDENTITY"); //$NON-NLS-1$
			break;
		case DeviceContext.MWT_LEFTMULTIPLY:
			sb.append(", LEFTMULTIPLY"); //$NON-NLS-1$
			break;
		case DeviceContext.MWT_RIGHTMULTIPLY:
			sb.append(", RIGHTMULTIPLY"); //$NON-NLS-1$
			break;
		default:
			sb.append("(bad transform operation)"); //$NON-NLS-1$
			break;
		}
		return sb.toString();
	}

	private void addFloat(StringBuffer sb, float f, boolean addComma) {
		sb.append(f);
		if (addComma) {
			sb.append(',');
		}
	}
}
