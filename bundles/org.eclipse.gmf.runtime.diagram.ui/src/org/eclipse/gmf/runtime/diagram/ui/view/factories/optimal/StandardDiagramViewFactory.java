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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.DiagramViewFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory for {@link org.eclipse.gmf.runtime.notation.StandardDiagram} view.
 * <p>Styles always present:
 * <ul>
 * <li>{@link org.eclipse.gmf.runtime.notation.DiagramStyle}
 * </ul> 
 * </p>
 * <p>Smaller memory footprint then Diagram + Style.</p>
 * 
 * @author aboyko
 * @since 1.2
 *
 */
public class StandardDiagramViewFactory extends DiagramViewFactory {

	@Override
	protected Diagram createDiagramView() {
		return NotationFactory.eINSTANCE.createStandardDiagram();
	}

	@Override
	protected List createStyles(View view) {
		return new ArrayList<Style>();
	}

}
