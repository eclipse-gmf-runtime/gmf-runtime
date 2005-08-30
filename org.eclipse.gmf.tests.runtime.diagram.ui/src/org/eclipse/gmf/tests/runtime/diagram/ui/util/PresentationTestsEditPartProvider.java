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

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import com.ibm.xtools.notation.View;


/**
 * @author mmostafa
 */
public class PresentationTestsEditPartProvider
	extends AbstractEditPartProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#getDiagramEditPartClass(com.ibm.xtools.notation.View)
	 */
	protected Class getDiagramEditPartClass(View view) {
		if (view.getType().equals(PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND))
			return DiagramEditPart.class;
		return super.getDiagramEditPartClass(view);
	}
}
