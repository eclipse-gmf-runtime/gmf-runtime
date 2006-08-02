/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.type;

import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.NullElementType;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Defines utility methods for notation type views.
 * 
 * @author ldamus
 */
public class NotationTypeUtil {

	/**
	 * Relies on the fact the INotationType is used to distinguish notation-only
	 * elements and connections at the time of their creation.
	 * 
	 * @param view
	 *            the view to be tested; must never be <code>null</code>
	 */
	public static boolean hasNotationType(View view) {
		return getNotationType(view) != null;
	}

	private static INotationType getNotationType(View view) {

		ISpecializationType[] specializations = ElementTypeRegistry
				.getInstance().getSpecializationsOf(NullElementType.ID);

		for (int i = 0; i < specializations.length; i++) {
			ISpecializationType nextSpecialization = specializations[i];

			if (nextSpecialization instanceof INotationType) {
				String notationHint = ((INotationType) nextSpecialization)
						.getSemanticHint();

				if (notationHint != null) {

					if (notationHint.equals(view.getType())) {
						return (INotationType) nextSpecialization;
					}
				}
			}
		}
		return null;
	}
}
