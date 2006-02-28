/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.Disposable;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.action.IDisposableAction;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SelectAllAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RefreshConnectionsRequest;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.DiagramState;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.IPresentationTestFixture;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestActionCallback;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestCommandCallback;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author choang
 * 
 * The abstract base class should be used by any tests that we write for the shapes team
 * It provides a framework for which the tests will run.
 * 
 * It contains implementation of some ulitiy helper methods also.
 * 
 * what's left for you to do?
 * <p>1.  Implement the following abstract methods
 * 	<p>a.  createConnectorViews - which setups up the diagram with the common shapes and connectors that will be used for your tests
 * 	<p>b.  setDefaultDiagramExt - sets the diagram extension type which will be used for the tests.  Based on the ext given
 * 		the diagram manager will use that as a hint to determine which DiagramEditor class to use to manuipulate the diagram.
 * 	<p>c.  public static suite() - to return the Test that will be our Action Menus will run.  Note this is not defined as an abstract
 * 		method here because it needs to be a static method in your test class.
 *     d.   setTestFixtureLogic() - which sets the <code>org.eclipse.gmf.tests.runtime.diagram.ui.util.IPresentataionTestFixtureLogic</code> class
 * 			that will be responsible for creating the fixture(i.e test data) for this test.
 * <p>2.  Add your tests methods
 *		You need to name your tests method like test*.  The Junit framework will  run all the methods that start with test*.  For each test
 * in you class the Junit framework will first run the setup(), then your testName1() method and the tearDown().
 * 
 * 
 */
public abstract class AbstractTestBase extends TestCase { 

	protected IPresentationTestFixture testFixture = null;
	
	/** Verbose system property. */
	public static final String SYSPROP_VERBOSE = "presentation.test.verbose";//$NON-NLS-1$
	
	/** verbose flag. */
	private static boolean _verbose = Boolean.getBoolean(SYSPROP_VERBOSE);
	
	/**
	 * Constructor for AbstractTestBase.
	 * @param TestName
	 */
	public AbstractTestBase(String arg0) {
		super(arg0);
		setTestFixture();
	}

	/**
	 * Enable verbose mode.  If enabled, {@link junit.framework.Assert#fail(java.lang.String)} 
	 * will print the supplied string; otherwise the string is ignored.
	 * 
	 * Verbose mode can also be enabled using the {@link #SYSPROP_VERBOSE} system property.
	 * @param enabled boolean flag
	 */
	protected final void enableVerbose( boolean enabled ) {
		_verbose = enabled;
	}
	
	/** Return the verbose mode. */
	public final boolean isVerbose() {
		return _verbose;
		
	}
		
	/** Calls <code>System.out.println(msg)</code> if in verbose mode. */
	public static final void println( Object msg ) {
		if ( _verbose ) {
			System.out.println(msg);
		}
	}
	
	/** Calls <code>System.out.print(msg)</code> if in verbose mode. */
	public static final void print( Object msg ) {
		if ( _verbose ) {
			System.out.print(msg);
		}
	}
	
	/**
	 * Method getCommandStack.
	 * @return CommandStack  Command stack for the diagram editor
	 */
	protected CommandStack getCommandStack() {
		return getTestFixture().getCommandStack();
	}
	/**
	 * Method setTestFixtureLogic.
	 *
	 * Sets the fixture logic for the tests.  A fixture is the set of "data" that the test will run against
	 * Typically many tests will use the same fixture.
	 * 
	 */
	protected abstract void setTestFixture();

	protected IPresentationTestFixture getTestFixture() {
		return testFixture;
	}

	protected IDiagramWorkbenchPart getDiagramWorkbenchPart() {
		return getTestFixture().getDiagramWorkbenchPart();
	}

	protected IWorkbenchPage getWorkbenchPage() {
		return getDiagramWorkbenchPart().getSite().getPage();
	}

	protected DiagramEditPart getDiagramEditPart() {
		return getTestFixture().getDiagramEditPart();
	}

	protected Diagram getDiagram() {
		return getTestFixture().getDiagram();
	}

	protected void saveDiagram() {
		if (getDiagramWorkbenchPart() instanceof IEditorPart) {
			IWorkbenchPage page = getDiagramWorkbenchPart().getSite().getPage();

			page.saveEditor((IEditorPart) getDiagramWorkbenchPart(), false);
			flushEventQueue();
		}
	}

	protected boolean isDirty() {
		if (getDiagramWorkbenchPart() instanceof IEditorPart) {
			return ((IEditorPart) getDiagramWorkbenchPart()).isDirty();
		}
		return false;
	}

	protected DiagramState getDiagramState() {

		try {
			return (DiagramState) TransactionUtil
				.getEditingDomain(getDiagram()).runExclusive(
					new RunnableWithResult.Impl() {

					public void run() {

						setResult(new DiagramState(getDiagramEditPart()));

					}
				});
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		return null;
	}

	/**
	 * Description:  Will execute the <code>Command</code> and then the <code>ITestCommandCallBack</code>, which 
	 * has the logic to verify that the command executed successful.
	 * <p>The command is executed within an UndoInterval and WriteAction model operation.
	 * @throws <AssertFailError> if the command did not run successfully 
	 * @author choang
	 */
	protected void testCommand(
		final ICommand command,
		final ITestCommandCallback callback) {
		testCommand(new EtoolsProxyCommand(command), callback);
	}
	
	/**
	 * Description:  Will execute the <code>Command</code> and then the <code>ITestCommandCallBack</code>, which 
	 * has the logic to verify that the command executed successful.
	 * <p>The command is executed within an UndoInterval and WriteAction model operation.
	 * @throws <AssertFailError> if the command did not run successfully 
	 * @author choang
	 */
	protected void testCommand(
		final Command command,
		final ITestCommandCallback callback) {
		
		assertNotNull(command);

		// Had to wrap each command in separate model operations
		// as if we didn't we got some weird behavior in some of the tests
		// such as the ConnectorTests#testSelfConnections where we get a null pointer
		// exception.

		final DiagramState state1 = getDiagramState();

		getCommandStack().execute(command);
		flushEventQueue();

		try {
			TransactionUtil.getEditingDomain(getDiagram()).runExclusive(
				new Runnable() {
				public void run() {
					callback.onCommandExecution();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		DiagramState state2 = getDiagramState();

		// checking if the command stack is in an undoable state first
		// not that selfConnections and deleteConnections tests are
		// failing .. if i do a check via command.canUndo() instead of
		// using getCommandSTack().canUndo()
		// which suggest that something is out of synch between the command
		// and the command stack .. need to look into it later.
		if (getCommandStack().canUndo()) {

			getCommandStack().undo();
			flushEventQueue();

			assertTrue(state1.equals(getDiagramState()));

			getCommandStack().redo();
			flushEventQueue();
		}

		assertTrue(state2.equals(getDiagramState()));

	}

	/**
	 * Description:  Will execute the <code>Action</code> and then the <code>ITestCommandCallBack</code>, which 
	 * has the logic to verify that the command executed successful.
	 * This method will test if the action implements the Disposable interface from GEF
	 * If it does it will call the dispose() method on the action. Callers should not 
	 * call it themselves
	 * @throws <AssertFailError> if the command did not run successfully 
	 * 
	 */
	protected void testAction(IAction action, ITestActionCallback callback) {
		flushEventQueue();
		assertTrue(action.isEnabled());
		action.run();
		flushEventQueue();
		if (action instanceof Disposable)
			((Disposable) action).dispose();
		if (callback != null)
			callback.onRunExecution();
	}

	/**
	 * Description:  Will execute the <code>Action</code> and then the <code>ITestCommandCallBack</code>, which 
	 * has the logic to verify that the command executed successful.
	 * This method will test if the action implements the IDisposableAction interface from common.ui
	 * If it does it will first set active the diagrameditorpart of the diagram and then call the init() method before running the action. At the end,
	 * it will call the dispose() method on the action. Callers should not 
	 * call these two methods themselves themselves
	 * @throws <AssertFailError> if the command did not run successfully 
	 * 
	 */
	protected void testAction(IDisposableAction action, ITestActionCallback callback) {

		getWorkbenchPage().activate(getDiagramWorkbenchPart());
		action.init();

		if( action.isEnabled() ) {
		
			action.run();
			flushEventQueue();
		}

		action.dispose();
		if (callback != null)
			callback.onRunExecution();
	}
	
	/**
	 * Does the same as <code>testAction</code> but also does an undo and
	 * redo afterwards and compares the diagram state.
	 * 
	 * @param action
	 * @param callback
	 */
	protected void testActionAndUndoRedo(IDisposableAction action, ITestActionCallback callback) {

		final DiagramState state1 = getDiagramState();

		getWorkbenchPage().activate(getDiagramWorkbenchPart());
		action.init();

		if( action.isEnabled() ) {
		
			action.run();
			flushEventQueue();
		}

		action.dispose();
		if (callback != null)
			callback.onRunExecution();
		
		DiagramState state2 = getDiagramState();
		assertTrue("testActionAndUndoRedo: Action cannot be undone.", getCommandStack().canUndo()); //$NON-NLS-1$
		getCommandStack().undo();
		assertTrue("diagram state different after undo of action", state1.equals(getDiagramState())); //$NON-NLS-1$
		getCommandStack().redo();
		assertTrue("diagram state different after redo of action", state2.equals(getDiagramState())); //$NON-NLS-1$
	}

	/**
	 * Method testProperty.
	 * Generic method for testing a property change in a view.
	 * 
	 * @param view IView to set the property value in
	 * @param property String ID of the property to test
	 * @param expectedValue Object that is the value of the property to test
	 */
	protected void testProperty(
		final View view,
		final String property,
		final Object expectedValue) {
		
		DiagramEditPart diagramEP = getDiagramEditPart();
		assertNotNull( "The DiagramEditPart is null", diagramEP ); //$NON-NLS-1$
		
		RootEditPart rootEP = diagramEP.getRoot();
		assertNotNull( "The RootEditPart is null", rootEP ); //$NON-NLS-1$
		
		EditPartViewer viewer = rootEP.getViewer();
		assertNotNull( "The EditPartViewer is null", viewer ); //$NON-NLS-1$
		
		Map epRegistry = viewer.getEditPartRegistry();
		assertNotNull( "The EditPartRegistery is null", epRegistry ); //$NON-NLS-1$
		
		final IGraphicalEditPart ep = (IGraphicalEditPart) epRegistry.get(view);
		assertNotNull( "Couldn't find the GraphicalEditPart in the Registery", ep ); //$NON-NLS-1$

		Request request = new ChangePropertyValueRequest(
				StringStatics.BLANK,
				property,
				expectedValue );
		
		Command cmd = ep.getCommand( request );

		testCommand(cmd, new ITestCommandCallback() {
			public void onCommandExecution() {
					assertEquals( expectedValue, ep.getStructuralFeatureValue((EStructuralFeature)PackageUtil.getElement(property)) );
			}
		});
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		getTestFixture().setup();
	}

	/**
	 * Clears the display's event queue.
	 * Same as calling <code>getTestFixture().flushEventQueue()</code>
	 */
	protected void flushEventQueue() {
		getTestFixture().flushEventQueue();
	}

	/** Same as calling <code>getTestFixture().tearDown()</code>. */
	protected void tearDown() throws Exception {

		flushEventQueue();
		getTestFixture().tearDown();
	}

	/**
	 * Creates a new shape view as a child of the diagram at the given location
	 * @param editor
	 * @param semanticElement
	 * @param location
	 * @return IShapeView
	 * @deprecated use createShapeView(IDiagramWorkbenchPart,Eobject,Point)
	 */
	/*
	protected IShapeView createShapeView(
		IDiagramWorkbenchPart editor,
		IElement semanticElement,
		Point location) {

		CompoundCommand cc = new CompoundCommand();

		CreateViewRequest request =	new CreateViewRequest(semanticElement);
		request.setLocation(location);

		cc.add(editor.getDiagramEditPart().getCommand(request));

		RefreshConnectorsRequest rcRequest =
			new RefreshConnectorsRequest(request.getNewObject());
		cc.add(
			getDiagramWorkbenchPart().getDiagramEditPart().getCommand(
				rcRequest));

		getCommandStack().execute(cc);

		return (IShapeView)
			((IAdaptable) (request.getNewObject()).get(0)).getAdapter(
			IShapeView.class);
	}
	*/

	/**
	 * Creates a new shape view as a child of the diagram at the given location
	 * @param editor
	 * @param semanticElement
	 * @param location
	 * @return IShapeView
	 */
	protected View createShapeView(
		DiagramEditPart diagramEP,
		EObject semanticElement,
		Point location) {

		CompoundCommand cc = new CompoundCommand();

		CreateViewRequest request = new CreateViewRequest(semanticElement,
			getTestFixture().getPreferencesHint());
		request.setLocation(location);

		cc.add(diagramEP.getCommand(request));

		RefreshConnectionsRequest rcRequest =
			new RefreshConnectionsRequest((List)request.getNewObject());
		cc.add(getDiagramEditPart().getCommand(rcRequest));

		getCommandStack().execute(cc);

		return (View)
			((IAdaptable) ((List)request.getNewObject()).get(0)).getAdapter(
			View.class);
	}

	protected void clearDiagram() {
		testAction(SelectAllAction.createSelectAllAction(getWorkbenchPage()), null);

		testAction(
			GlobalActionManager.getInstance().createActionHandler(
				getWorkbenchPage(),
				GlobalActionId.DELETE), null);
	}
	
	/** 
	 * Return the figure in which elements are being added to. 
	 * @return <code>getDiagramEditPart().getFigure()</code>.
	 */
	protected IFigure getDrawSurfaceFigure() {
		return getDiagramEditPart().getFigure();
	}
	
	/** 
	 * Return the editpart in which elements are being added to.
	 * @return <code>getDiagramEditPart()</code>.
	 */
	protected IGraphicalEditPart getDrawSurfaceEditPart() {
		return getDiagramEditPart();
	}
	
	
	/** Return the supplied editpart's {@link ShapeNodeEditPart}children. */
	protected List getShapesIn(IGraphicalEditPart parent) {
		assertNotNull(parent);
		List shapes = new ArrayList();
		
		Iterator it = parent.getChildren().iterator();
		while (it.hasNext()) {
			Object child = it.next();
			if (child instanceof ShapeNodeEditPart) {
				shapes.add(child);
			}
		}
		return shapes;
	}

	/** Return <code>getDiagramEditPart().getConnectors()</code>. */
	protected List getConnectors() {
		return getDiagramEditPart().getConnections();
	}
	
	/* Will run teardown if the setup fails.
	 * @see junit.framework.TestCase#runBare()
	 */
	public void runBare()
		throws Throwable {
		
		try {
			setUp();
			runTest();
		}
		finally {
			tearDown();
		}
	}
}
