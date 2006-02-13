/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.tests.runtime.diagram.ui.framework;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.ui.util.FileUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.TestsPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


/**
 * This is an Abstract base class for Diagram testcases
 * 
 * @author Jody Schofield
 */
public abstract class DiagramTestCase extends TestCase {

	private IProject project = null;
	private IDiagramWorkbenchPart diagramWorkbenchPart = null;
	private IFile diagramFile = null;
    private Diagram diagramView;
    private TransactionalEditingDomain editingDomain;
    private Resource resource;


	/**
	 * Constructs a Diagram TestCase with a given name
	 * 
	 * @param name Name of the diagram test case
	 */
	public DiagramTestCase(String name) {
		super(name);
	}

	/**
	 * Sets up the fixture.  The default setup includes:
	 * - creating the project
	 * - creating a diagram
	 * - opening the diagram
	 * - adding shapes
	 * - adding connectors
	 * 
	 * This method is called before each test method is executed.
	 */
	protected void setUp() throws Exception {

		// Create a project
		createProject();

		// Create and open a diagram
		setDiagramFile(createDiagram());
        createResource();
		openDiagram();

		// Allow the OS to process editor related events
		flushEventQueue();

		createShapesAndConnectors();
	}
	
	/**
	 * Will delete the project that was used for the test and removed all the
	 * resources in it.
	 */
	protected void tearDown() throws Exception {

		// Allow the OS to process editor related events
		flushEventQueue();

		// Close the diagram
		closeDiagram();
        diagramView = null;
        
        // unload the resource
        resource.unload();
        resource = null;

		// Close and delete the project
		closeProject();		
	}

	protected IFile getDiagramFile() {
		return diagramFile;
	}
    
    protected Diagram getDiagram() {
        return diagramView;
    }
    
    protected void setDiagram(Diagram diagram) {
        this.diagramView = diagram;
    }
	
	protected void setDiagramFile(IFile theFile) {
		diagramFile = theFile;
	}
	/**
	 * Get the name for the project to be created
	 * 
	 * @return project name
	 */
	protected String getProjectName() {
		return "pxdemodelproj"; //$NON-NLS-1$
	}

	/**
	 * Get the project.  The project is created in the
	 * createProject method.
	 * 
	 * @return the project that was created
	 */
	protected IProject getProject() {
		return project;
	}

	/**
	 * Creates a project to be used for the test.
	 */
	protected void createProject() throws Exception {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsroot = workspace.getRoot();
		
		project = wsroot.getProject( getProjectName() );
		
		IProjectDescription desc =
			workspace.newProjectDescription(project.getName());

		// Create the project if it doesn't exist
		if (!project.exists()) {
			project.create(desc, null);
		}

		// Open the project if it isn't open
		if (!project.isOpen()) {
			project.open(null);
		}
	}
    
    /**
     * Creates the editing domain and resource and adds the diagram to
     * that resource.
     */
    protected void createResource() {
        editingDomain = MEditingDomain.INSTANCE;
        
        IFile file = getDiagramFile();
        
        if (file != null) {
            String filePath = file.getLocation().toOSString();
            resource = editingDomain.loadResource(filePath);

        } else {
            resource = editingDomain
                .createResource("null:/org.eclipse.gmf.tests.runtime.diagram.ui"); //$NON-NLS-1$
        }

        final Diagram d = getDiagram();  
        
        if (d != null) {

            AbstractEMFOperation operation = new AbstractEMFOperation(
                editingDomain, "AbstractPresentationTestFixture setup") { //$NON-NLS-1$

                protected IStatus doExecute(IProgressMonitor monitor,
                        IAdaptable info)
                    throws ExecutionException {

                    resource.getContents().add(getDiagram());
                    return Status.OK_STATUS;
                };
            };

            try {
                operation.execute(new NullProgressMonitor(), null);
            } catch (ExecutionException ie) {
                fail("createResource failed: " + ie.getLocalizedMessage()); //$NON-NLS-1$
            }
        }
    }

	/**
     * Close and delete the project
     */
	protected void closeProject() {
	
		try {
			project.delete(true, true, null);
		} catch (CoreException e) {
			
			Log.error(TestsPlugin.getDefault(),
				IStatus.ERROR, "Failed to delete project", e); //$NON-NLS-1$
		} finally {
			
			project = null;
		}
	}

//	protected IFile createDiagramFile(String filePath) throws Exception {
//		IFile file = project.getFile(filePath);
//		if (!file.exists()) {
//			FileUtil.createFile(
//				file,
//				new FileInputStream(filePath),
//				new NullProgressMonitor());
//		}
//
//		return file;
//	}

	protected void deleteDiagramFile() throws Exception {
		FileUtil.deleteFile(getDiagramFile(), new NullProgressMonitor());
	}

	/**
	 * Close the diagram
	 */
	protected void closeDiagram() {	
		if (getDiagramWorkbenchPart() instanceof IEditorPart) {
			IWorkbenchPage page = getDiagramWorkbenchPart().getSite().getPage();
			
			page.closeEditor(
					(IEditorPart) getDiagramWorkbenchPart(),
					false);
		}
		setDiagramWorkbenchPart(null);
		setDiagramFile(null);
	}

	/**
	 * Clears the diaplay's event queue.
	 */
	protected void flushEventQueue() {
		Display display = Display.getDefault();
		while (display.readAndDispatch()) {
			// do nothing
		}
	}
	
	/**
	 * Returns the editor.
	 * 
	 * @return IDiagramWorkbenchPart
	 */
	public IDiagramWorkbenchPart getDiagramWorkbenchPart() {
		return diagramWorkbenchPart;
	}

	/**
	 * Sets the diagramWorkbenchPart.
	 * 
	 * @param diagramWorkbenchPart
	 *            The editorPart to set
	 */
	protected void setDiagramWorkbenchPart(IDiagramWorkbenchPart diagramWorkbenchPart) {
		this.diagramWorkbenchPart = diagramWorkbenchPart;
	}

	/**
	 * Return the Diagrams EditPart.
	 *
	 * @return The DiagramEditPart for the diagram being tested
	 */
	public DiagramEditPart getDiagramEditPart() {
		assertNotNull(getDiagramWorkbenchPart());		
		return getDiagramWorkbenchPart().getDiagramEditPart();
	}

	/**
	 * Returns the Workbench page for the current Diagram Workbench Part
	 * @return IWorkbenchPage for the current Diagram
	 */
	protected IWorkbenchPage getWorkbenchPage() {
		return getDiagramWorkbenchPart().getSite().getPage();
	}

	/**
	 * Implement to create the diagram and the diagram file for which the test
	 * should run under.  This method should return the file for the diagram.
	 * @return the file for the diagram
	 */
	protected abstract IFile createDiagram() throws Exception;

	/**
	 * Implement to open the diagram.
	 * 
	 * @throws Exception
	 */
	protected void openDiagram() throws Exception {

		if( getDiagramFile() == null)
			return;

		IWorkbenchPage page =
			PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage();

		setDiagramWorkbenchPart((IDiagramWorkbenchPart)IDE.openEditor(page, getDiagramFile(), true));
	}

	/**
	 * Implement this to creates the shapes and the connectors for the tests.
	 * Will set the connect view if there is one needed for the test.
	 */
	protected abstract void createShapesAndConnectors() throws Exception;


	
	
	
	
	
	
	


	//
//	protected boolean isDirty() {
//		if (getDiagramWorkbenchPart() instanceof IEditorPart) {
//			return ((IEditorPart) getDiagramWorkbenchPart()).isDirty();
//		}
//		return false;
//	}
//
//	protected DiagramState getDiagramState() {
//
//		final DiagramState[] returnState = new DiagramState[1];
//
////		// wrapping around model operation so i can return the result
////		getDiagramEditPart()
////			.getDiagramEditDomain()
////			.getModelOperation()
////			.executeAsReadAction(DefaultKey.ACTION, new Runnable() {
////			public void run() {
////
////				returnState[0] = new DiagramState(getDiagramWorkbenchPart());
////
////			}
////		});
//
//		return returnState[0];
//	}
//
//	/**
//	 * Description: Will execute the <code>Command</code> and then the <code>ITestCommandCallBack</code>,
//	 * which has the logic to verify that the command executed successful.
//	 * <p>
//	 * The command is executed within an UndoInterval and WriteAction model
//	 * operation.
//	 * 
//	 * @throws <AssertFailError>
//	 *             if the command did not run successfully
//	 * @author choang
//	 */
//	protected void testCommand(
//		final ICommand command,
//		final ITestCommandCallback callback) {
//		testCommand(new EtoolsProxyCommand(command), callback);
//	}
//
//	/**
//	 * Description: Will execute the <code>Command</code> and then the <code>ITestCommandCallBack</code>,
//	 * which has the logic to verify that the command executed successful.
//	 * <p>
//	 * The command is executed within an UndoInterval and WriteAction model
//	 * operation.
//	 * 
//	 * @throws <AssertFailError>
//	 *             if the command did not run successfully
//	 * @author choang
//	 */
//	protected void testCommand(
//		final Command command,
//		final ITestCommandCallback callback) {
//
//		// Had to wrap each command in separate model operations
//		// as if we didn't we got some weird behavior in some of the tests
//		// such as the ConnectorTests#testSelfConnections where we get a null
//		// pointer
//		// exception.
//
//		final DiagramState state1 = getDiagramState();
//
//		getCommandStack().execute(command);
//		flushEventQueue();
//
////		getDiagramEditPart()
////			.getDiagramEditDomain()
////			.getModelOperation()
////			.executeAsReadAction(DefaultKey.ACTION, new Runnable() {
////			public void run() {
////				callback.onCommandExecution();
////			}
////		});
//		DiagramState state2 = getDiagramState();
//
//		// checking if the command stack is in an undoable state first
//		// not that selfConnections and deleteConnections tests are
//		// failing .. if i do a check via command.canUndo() instead of
//		// using getCommandSTack().canUndo()
//		// which suggest that something is out of synch between the command
//		// and the command stack .. need to look into it later.
//		if (getCommandStack().canUndo()) {
//
//			getCommandStack().undo();
//			flushEventQueue();
//
//			assertTrue(state1.equals(getDiagramState()));
//
//			getCommandStack().redo();
//			flushEventQueue();
//		}
//
//		assertTrue(state2.equals(getDiagramState()));
//
//	}
//
//	/**
//	 * Description: Will execute the <code>Action</code> and then the <code>ITestCommandCallBack</code>,
//	 * which has the logic to verify that the command executed successful. This
//	 * method will test if the action implements the Disposable interface from
//	 * GEF If it does it will call the dispose() method on the action. Callers
//	 * should not call it themselves
//	 * 
//	 * @throws <AssertFailError>
//	 *             if the command did not run successfully
//	 *  
//	 */
//	protected void testAction(IAction action, ITestActionCallback callback) {
//		flushEventQueue();
//		assertTrue(action.isEnabled());
//		action.run();
//		flushEventQueue();
//		if (action instanceof Disposable)
//			 ((Disposable) action).dispose();
//		if (callback != null)
//			callback.onRunExecution();
//	}
//
//	/**
//	 * Description: Will execute the <code>Action</code> and then the <code>ITestCommandCallBack</code>,
//	 * which has the logic to verify that the command executed successful. This
//	 * method will test if the action implements the IDisposableAction
//	 * interface from common.ui If it does it will first set active the
//	 * diagrameditorpart of the diagram and then call the init() method before
//	 * running the action. At the end, it will call the dispose() method on the
//	 * action. Callers should not call these two methods themselves themselves
//	 * 
//	 * @throws <AssertFailError>
//	 *             if the command did not run successfully
//	 *  
//	 */
//	protected void testAction(
//		IDisposableAction action,
//		ITestActionCallback callback) {
//
//		getWorkbenchPage().activate(getDiagramWorkbenchPart());
//		action.init();
//
//		if (action.isEnabled()) {
//
//			action.run();
//			flushEventQueue();
//		}
//
//		action.dispose();
//		if (callback != null)
//			callback.onRunExecution();
//	}
//
//	/**
//	 * Does the same as <code>testAction</code> but also does an undo and
//	 * redo afterwards and compares the diagram state.
//	 * 
//	 * @param action
//	 * @param callback
//	 */
//	protected void testActionAndUndoRedo(
//		IDisposableAction action,
//		ITestActionCallback callback) {
//
//		final DiagramState state1 = getDiagramState();
//
//		getWorkbenchPage().activate(getDiagramWorkbenchPart());
//		action.init();
//
//		if (action.isEnabled()) {
//
//			action.run();
//			flushEventQueue();
//		}
//
//		action.dispose();
//		if (callback != null)
//			callback.onRunExecution();
//
//		DiagramState state2 = getDiagramState();
//		assertTrue("testActionAndUndoRedo: Action cannot be undone.", getCommandStack().canUndo()); //$NON-NLS-1$
//		getCommandStack().undo();
//		assertTrue("diagram state different after undo of action", state1.equals(getDiagramState())); //$NON-NLS-1$
//		getCommandStack().redo();
//		assertTrue("diagram state different after redo of action", state2.equals(getDiagramState())); //$NON-NLS-1$
//	}
//
//	/**
//	 * Method testProperty. Generic method for testing a property change in a
//	 * view.
//	 * 
//	 * @param view
//	 *            IView to set the property value in
//	 * @param property
//	 *            String ID of the property to test
//	 * @param expectedValue
//	 *            Object that is the value of the property to test
//	 */
//	protected void testProperty(
//		final IView view,
//		final String property,
//		final Object expectedValue) {
//
//		DiagramEditPart diagramEP = getDiagramEditPart();
//		assertNotNull("The DiagramEditPart is null", diagramEP); //$NON-NLS-1$
//
//		RootEditPart rootEP = diagramEP.getRoot();
//		assertNotNull("The RootEditPart is null", rootEP); //$NON-NLS-1$
//
//		EditPartViewer viewer = rootEP.getViewer();
//		assertNotNull("The EditPartViewer is null", viewer); //$NON-NLS-1$
//
//		Map epRegistry = viewer.getEditPartRegistry();
//		assertNotNull("The EditPartRegistery is null", epRegistry); //$NON-NLS-1$
//
//		final IGraphicalEditPart ep = (IGraphicalEditPart) epRegistry.get(view);
//		assertNotNull("Couldn't find the GraphicalEditPart in the Registery", ep); //$NON-NLS-1$
//
//		Request request =
//			new ChangePropertyValueRequest(
//				StringStatics.BLANK,
//				property,
//				expectedValue);
//
//		Command cmd = ep.getCommand(request);
//
//		testCommand(cmd, new ITestCommandCallback() {
//			public void onCommandExecution() {
//
//				assertEquals(expectedValue, ep.getPropertyValue(property));
//			}
//		});
//	}
//
//
//	protected void clearDiagram() {
//		testAction(
//			SelectAllAction.createSelectAllAction(getWorkbenchPage()),
//			null);
//
//		testAction(
//			GlobalActionManager.getInstance().createActionHandler(
//				getWorkbenchPage(),
//				GlobalActionId.DELETE),
//			null);
//	}
//
//	/**
//	 * Return the figure in which elements are being added to.
//	 * 
//	 * @return <code>getDiagramEditPart().getFigure()</code>.
//	 */
//	protected IFigure getDrawSurfaceFigure() {
//		return getDiagramEditPart().getFigure();
//	}
//
//	/**
//	 * Return the editpart in which elements are being added to.
//	 * 
//	 * @return <code>getDiagramEditPart()</code>.
//	 */
//	protected IGraphicalEditPart getDrawSurfaceEditPart() {
//		return getDiagramEditPart();
//	}
//
//	/** Return the supplied editpart's {@link ShapeNodeEditPart}children. */
//	protected List getShapesIn(IGraphicalEditPart parent) {
//
//		assertNotNull(parent);
//		List shapes = new ArrayList();
//		Iterator it = parent.getChildren().iterator();
//		while (it.hasNext()) {
//			Object child = it.next();
//			if (child instanceof ShapeNodeEditPart) {
//				shapes.add(child);
//			}
//		}
//		return shapes;
//	}
//
//	/** Return <code>getDiagramEditPart().getConnectors()</code>. */
//	protected List getConnectors() {
//		return getDiagramEditPart().getConnectors();
//	}
//
//
//	/**
//	 * Method getCommandStack.
//	 * 
//	 * @return CommandStack Command stack for the diagram editor
//	 */
//	public CommandStack getCommandStack() {
//		return (CommandStack) getDiagramWorkbenchPart().getAdapter(
//			CommandStack.class);
//	}
//

}
