/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Mar 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.tests.runtime.diagram.ui;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.DiagramCreator;


/**
 * @author sshaw
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramTestFixture extends AbstractPresentationTestFixture {

	protected void createDiagram()
		throws Exception {
		setDiagram(DiagramCreator.createEmptyDiagram(getPreferencesHint(),
			getEditingDomain()));
	}

	
	protected void createProject()
		throws Exception {
		// do nothing

	}
	
	protected void createShapesAndConnectors()
		throws Exception {
		
		DiagramCreator.createNodes(getDiagram(), getPreferencesHint(),
			getEditingDomain());
			
		EList children = getDiagram().getChildren();
		Node node = (Node)children.get(0);
		EList edges = node.getSourceEdges();
		if (edges.size() > 0)
			setConnectorView((Edge)edges.get(0));
		else {
			edges = node.getTargetEdges();
			if (edges.size() > 0) {
				setConnectorView((Edge)edges.get(0));
			}
		}
	}
	
	public void openDiagram() throws Exception {
		createDiagram();

		createDiagramEditPart();
	}
	
	public boolean closeDiagram() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.IPresentationTestFixture#getPreferencesHint()
	 */
	public PreferencesHint getPreferencesHint() {
		return PreferencesHint.USE_DEFAULTS;
	}
}
