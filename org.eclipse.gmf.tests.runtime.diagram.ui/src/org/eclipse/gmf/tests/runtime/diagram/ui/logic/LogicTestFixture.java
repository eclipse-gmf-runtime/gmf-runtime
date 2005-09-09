package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.LogicDiagramFileCreator;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.LogicEditorUtil;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.EditorUtil;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture;
import org.eclipse.ui.PlatformUI;


public class LogicTestFixture
	extends AbstractPresentationTestFixture {

	protected void createProject()
		throws Exception {
		IWorkspace workspace = null;
		String aProjectName = "logicProj"; //$NON-NLS-1$

		workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsroot = workspace.getRoot();
		IProject project = wsroot.getProject(aProjectName);
		IProjectDescription desc =
			workspace.newProjectDescription(project.getName());

		IPath locationPath = Platform.getLocation();
		locationPath = null;
		desc.setLocation(locationPath);
		if (!project.exists())
			project.create(desc, null);
		if (!project.isOpen())
			project.open(null);

		setProject(project);
	}

	protected void createDiagram()
		throws Exception {
		
		IFile diagramFile = LogicEditorUtil.createNewDiagramFile(
			LogicDiagramFileCreator.getInstance(), 
			getProject().getFullPath(),
			"logicTest", //$NON-NLS-1$
			EditorUtil.getInitialContents(),
			"logic", //$NON-NLS-1$
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
			new NullProgressMonitor());
		setDiagramFile(diagramFile);
		
		openDiagram();
	}

	protected void createShapesAndConnectors()
		throws Exception {
		
		IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
		CompoundCommand cc = new CompoundCommand();

		CreateViewAndElementRequest shapeRequest =
			new CreateViewAndElementRequest(typeCircuit, getPreferencesHint());
		
		shapeRequest.setLocation(new Point(200, 200));
  
		// execute the commands to get the note edit part
		cc.add(getDiagramEditPart().getCommand(shapeRequest));

		shapeRequest.setLocation(new Point(500, 500));
		cc.add(getDiagramEditPart().getCommand(shapeRequest));

		execute(cc);
	}

	public PreferencesHint getPreferencesHint() {
		return PreferencesHint.USE_DEFAULTS;
	}

	/** Executes the supplied command. */
	protected Collection execute(ICommand cmd) {
		EtoolsProxyCommand command = new EtoolsProxyCommand(cmd);
		execute(command);
		return DiagramCommandStack.getReturnValues(command);
	}
	
	/** Executes the supplied command. */
	protected void execute(Command cmd) {
		getCommandStack().execute(cmd);
	}

	/**
	 * Creates a semantic element
	 * 
	 * @param type
	 *            type of element to create
	 * @param parent
	 *            containing element.
	 * @return a new element; <tt>null</tt> if element creation failed.
	 */
	public EObject createElement(IElementType type, EObject parent, EReference ref) {
	
		CreateElementRequest cer = new CreateElementRequest(parent, type, ref);
		ICommand cmd = type.getEditHelper().getEditCommand(cer);
		print("\tcreating semantic " + type.getDisplayName() + " element... ");//$NON-NLS-2$//$NON-NLS-1$
	
		Collection result = execute(cmd);
		
		assertTrue("Failed to create " + type.getDisplayName() + " element.",//$NON-NLS-2$//$NON-NLS-1$
				!result.isEmpty());
		println("OK.");//$NON-NLS-1$
		return (EObject) result.iterator().next();
	}

	/** Creates an operation in the supplied class. */
	public LED createLED(EObject parent) {
		IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
		return (LED)createElement(typeLED, parent, SemanticPackage.eINSTANCE.getContainerElement_Children());
	}

}
