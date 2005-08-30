/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.internal.services.view.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractDiagramViewFactory;

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

	/**
	 * This is the presentation diagram view class to use.
	 */
	public static class PresentationTestsDiagramViewFactory
		extends AbstractDiagramViewFactory {

		// TODO: delete me

	}

	/** the diagram kind to use to request a 'presentation tests diagram' */
	public static final String PRESENTATION_TESTS_DIAGRAM_KIND = "PresentationTestsDiagramKind"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.AbstractViewProvider#getDiagramViewClass(IAdaptable,
	 *      java.lang.String)
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		return PRESENTATION_TESTS_DIAGRAM_KIND.equals(diagramKind) ? PresentationTestsDiagramViewFactory.class
			: null;
	}
}