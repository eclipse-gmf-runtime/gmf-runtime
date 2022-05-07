/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.DiagramViewFactory;

/**
 * This is a view provider for the presentation tests. It needs to provide a
 * default diagram view so that we can create diagrams in this package without
 * depending on UML. If ever this capability becomes available in presentation
 * itself, then it can be removed from here.
 * 
 * @author cmahoney
 */
public class PresentationTestsViewProvider
	extends AbstractViewProvider {

	/** the diagram kind to use to request a 'presentation tests diagram' */
	public static final String PRESENTATION_TESTS_DIAGRAM_KIND = "PresentationTestsDiagramKind"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider#getDiagramViewClass(IAdaptable,
	 *      java.lang.String)
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		return PRESENTATION_TESTS_DIAGRAM_KIND.equals(diagramKind) ? DiagramViewFactory.class
			: null;
	}
}