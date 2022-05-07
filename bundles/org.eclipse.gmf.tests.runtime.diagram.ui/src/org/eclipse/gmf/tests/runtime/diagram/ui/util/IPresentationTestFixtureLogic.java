/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
 
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IEditorPart;

/**
 * @author choang
 *
 * <p>Interface to define what a Test Fixture Logic methods.  Used
 * <br>by @see org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase to setup
 * </br> the fixture for the tests.
  */
public interface IPresentationTestFixtureLogic
{
	
	/*
	 * Consider refactoring this so that it is not stateless and stores the member varaibles so it is the actual
	 * fixture instead of just the FixtureLogic.  The reason for this is because I can see the need for the craeteConnectView
	 * method to potential update member variables that the test would want to access.
	 * 
	 */
	static public final Point ptStart = new Point(100, 100);

	static public final Point ptEnd = new Point(300, 300);

	static public final Point ptMiddle = new Point(200, 200);

	
	
	/**
	 * Method createProject.
	 * @param aProjectName  Name of project to be created
	 * @param workspace Workspace to create the project in.
	 * @return IProject The project with the specificed name <code>aProjectname</code>
	 * @throws Exception
	 */
	public IProject createProject(String aProjectName,IWorkspace workspace) throws Exception;
	
	/**
	 * Method createDiagramFile.
	 * @param fullFilePath  
	 * @param project The project which the diagram should be created under
	 * @return IFile A file handle to the diagram created.
	 * @throws Exception
	 */
	public IFile createDiagramFile(String fullFilePath, IProject project) throws Exception;

	
	/**
	 * Method createConnectorView.
	 * @param project The project to for which to create the shapes and connectors in.
	 * @param editor 
	 * @return IConnectorView The connectView associated with the test.  Will be null if there is no connector to test.
	 * @throws Exception
	 * 
	 * <p>Will create the shapes and connectors for this test in the diagram and return the connector view (if there is one needed for the test)
	 * <p>Probably should be reneamed to createShapesAndConnectors( ...)
	 */
	public View createConnectorView(IProject project,IEditorPart editor) throws Exception;
	
}
