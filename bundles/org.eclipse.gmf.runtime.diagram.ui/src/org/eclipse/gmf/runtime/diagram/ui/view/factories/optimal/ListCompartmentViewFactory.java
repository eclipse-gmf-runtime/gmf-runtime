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

import org.eclipse.gmf.runtime.notation.ListCompartment;
import org.eclipse.gmf.runtime.notation.NotationFactory;

/**
 * Factory for {@link org.eclipse.gmf.runtime.notation.ListCompartment} view.
 * <p>Styles always present:
 * <ul>
 * <li>{@link org.eclipse.gmf.runtime.notation.DrawerStyle}
 * <li>{@link org.eclipse.gmf.runtime.notation.SortingStyle}
 * <li>{@link org.eclipse.gmf.runtime.notation.FilteringStyle}
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
public class ListCompartmentViewFactory extends BasicCompartmentViewFactory {

	@Override
	protected ListCompartment createNode() {
		return NotationFactory.eINSTANCE.createListCompartment();
	}

}
