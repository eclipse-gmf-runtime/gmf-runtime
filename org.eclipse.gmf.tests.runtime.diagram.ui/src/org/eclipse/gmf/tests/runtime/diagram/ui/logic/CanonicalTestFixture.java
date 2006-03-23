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
package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;

public class CanonicalTestFixture extends LogicTestFixture {

	public void destroy(EObject eObject) {
		DestroyElementRequest destroy = new DestroyElementRequest(
				getEditingDomain(),
				eObject,
				false);
		IElementType context = ElementTypeRegistry.getInstance().getElementType(
				destroy.getEditHelperContext());
		
		execute(context.getEditCommand(destroy));
	}

	/** Sets the <tt>COLLAPSED</tt> property value. */
	public void setCollapsed(IResizableCompartmentEditPart rep, boolean collapse) {
		SetPropertyCommand spc = new SetPropertyCommand(getEditingDomain(), new EObjectAdapter(
				(View) rep.getModel()), Properties.ID_COLLAPSED,
				Properties.ID_COLLAPSED, Boolean.valueOf(collapse));
		execute(spc);
		assertEquals(
				"Enexpected collapsed value", collapse, ((Boolean) rep.getStructuralFeatureValue(NotationPackage.eINSTANCE.getDrawerStyle_Collapsed())).booleanValue());//$NON-NLS-1$
	}

	/** Sets the <tt>IS_VISIBLE</tt> property value. */
	public void setVisible(IGraphicalEditPart gep, boolean visible) {
		SetPropertyCommand spc = new SetPropertyCommand(getEditingDomain(), new EObjectAdapter(
				(View) gep.getModel()), Properties.ID_ISVISIBLE,
				Properties.ID_ISVISIBLE, Boolean.valueOf(visible));
		execute(spc);
		assertEquals(
				"Enexpected visible value", visible, ((Boolean) gep.getStructuralFeatureValue(NotationPackage.eINSTANCE.getView_Visible())).booleanValue());//$NON-NLS-1$
	}
	
	/** Sets the <tt>IS_VISIBLE</tt> property value. */
	public void setVisible(View view, boolean visible) {
		SetPropertyCommand spc = new SetPropertyCommand(getEditingDomain(), new EObjectAdapter(
				view), Properties.ID_ISVISIBLE,
				Properties.ID_ISVISIBLE, Boolean.valueOf(visible));
		execute(spc);
		assertEquals(
				"Enexpected visible value", visible, ((Boolean) ViewUtil.getStructuralFeatureValue(view,NotationPackage.eINSTANCE.getView_Visible())).booleanValue());//$NON-NLS-1$
	}

	/** Sets the <tt>ID_ISCANONICAL</tt> property value. */
	public void enableCanonical(IGraphicalEditPart gep, boolean enabled) {
		SetPropertyCommand spc = new SetPropertyCommand(getEditingDomain(), new EObjectAdapter(
				(View) gep.getModel()), Properties.ID_ISCANONICAL,
				Properties.ID_ISCANONICAL, Boolean.valueOf(enabled));
		execute(spc);
		assertEquals(
				"Unexpected canonical value", enabled, ((Boolean) gep.getStructuralFeatureValue(NotationPackage.eINSTANCE.getCanonicalStyle_Canonical())).booleanValue());//$NON-NLS-1$
	}

	/** Returns the list of shape views that resolve to the supplied eClass. */
	public List getShapes(EClass eClass, EditPart editPart) {
		List retval = new ArrayList();
		Iterator children = editPart.getChildren().iterator();
		while (children.hasNext()) {
			IAdaptable child = (IAdaptable) children.next();
			View view = (View) child.getAdapter(View.class);
			if (view != null) {
				EObject eObject = ViewUtil.resolveSemanticElement(view);
				if (eClass.equals(eObject.eClass())) {
					retval.add(child);
				}
			}
		}
		return retval;
	}		

	
	/** Returns the list of shape views that resolve to the supplied eClass. */
	public List getShapes(EClass eClass) {
		return getShapes(eClass, getDiagramEditPart());
	}

	/** Sets the model's read-only flag to the supplied value. */
	public void makeModelReadOnly(boolean readOnly) {
		IFile file = getDiagramFile(); // getModelFile();
		assertNotNull("file expected.", file);//$NON-NLS-1$
		ResourceAttributes resourceAttributes = file.getResourceAttributes();
		resourceAttributes.setReadOnly(readOnly);
		try {
			file.setResourceAttributes(resourceAttributes);
		} catch (CoreException e) {
			// nothing we can do here 
		}
		//setResourceAttributes
		println(file.getFullPath() + ".isReadOnly() == " + file.isReadOnly());//$NON-NLS-1$
	}

	/**
	 * Closes the diagram editor.
	 * 
	 * @param save
	 *            saves the diagram editor if set <tt>true</tt>.
	 */
	public void closeDiagramEditor(boolean save) {
		IWorkbenchPage page = getDiagramWorkbenchPart().getSite().getPage();
		if (page != null) {
			getDiagramWorkbenchPart().getSite().getPage();
			page.closeEditor((IEditorPart)getDiagramWorkbenchPart(), save);
			setDiagramWorkbenchPart(null);
			
			assertNull(
					"unexpected editor", page.findEditor(new FileEditorInput(getDiagramFile())));//$NON-NLS-1$
		}
	}
	
	public IGraphicalEditPart getCanonicalCompartment(int index) {
		List circuits = getShapes( SemanticPackage.eINSTANCE.getCircuit());
		assertTrue( "Failed to create circuit shapes.", !circuits.isEmpty() );//$NON-NLS-1$
		
		IGraphicalEditPart circuitEP = (IGraphicalEditPart)circuits.get(index);
		IGraphicalEditPart logicCompartment = circuitEP.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
		
		return logicCompartment;
	}
}
