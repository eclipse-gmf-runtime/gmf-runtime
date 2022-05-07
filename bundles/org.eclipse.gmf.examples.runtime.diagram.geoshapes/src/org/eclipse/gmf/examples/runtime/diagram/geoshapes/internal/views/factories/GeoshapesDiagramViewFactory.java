/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.views.factories;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.StandardDiagramViewFactory;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;


public class GeoshapesDiagramViewFactory
	extends StandardDiagramViewFactory {

	/**
	 * Get the measurement unit to be set in pixels implying that that units
	 * stored in the notation file are identical to device pixels.
	 */
	protected MeasurementUnit getMeasurementUnit() {
		return MeasurementUnit.PIXEL_LITERAL;
	}
}
