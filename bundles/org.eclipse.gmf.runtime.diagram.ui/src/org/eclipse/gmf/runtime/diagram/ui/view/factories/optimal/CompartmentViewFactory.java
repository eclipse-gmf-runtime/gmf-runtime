/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal;

import org.eclipse.gmf.runtime.notation.Compartment;
import org.eclipse.gmf.runtime.notation.NotationFactory;

/**
 * Factory for {@link org.eclipse.gmf.runtime.notation.Compartment} view.
 * <p>Styles always present:
 * <ul>
 * <li>{@link org.eclipse.gmf.runtime.notation.DrawerStyle}
 * <li>{@link org.eclipse.gmf.runtime.notation.TitleStyle}
 * <li>{@link org.eclipse.gmf.runtime.notation.CanonicalStyle}
 * </ul> 
 * </p>
 * <p>Smaller memory footprint then Node + Style.</p>
 * <p>
 * Does not support addition/removal of:
 * <ul>
 * <li>Source Edges
 * <li>Target Edges
 * </ul>
 * </p>
 * 
 * @author aboyko
 * @since 1.2
 *
 */
public class CompartmentViewFactory extends BasicCompartmentViewFactory {

	@Override
	protected Compartment createNode() {
		return NotationFactory.eINSTANCE.createCompartment();
	}

}
