/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DescriptionCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DiagramNameCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TextEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * Supports the creation of <b>diagram</b> editparts elements.  
 * Diagram elements are commonly used by all plugins.
 *  
 * @author schafe
 * 
 */
public class DiagramEditPartProvider extends AbstractEditPartProvider {

	/** list of supported shape editparts. */
	private Map shapeMap = new HashMap();
	{
		shapeMap.put(ViewType.NOTE, NoteEditPart.class);
		shapeMap.put(ViewType.TEXT, TextEditPart.class);
        shapeMap.put(ViewType.GROUP, GroupEditPart.class);
	}
	/** list of supportted text editparts. */
	private Map textCompartmentMap = new HashMap();
	{
		textCompartmentMap.put(ViewType.DIAGRAM_NAME, DiagramNameCompartmentEditPart.class);
		textCompartmentMap.put(CommonParserHint.DESCRIPTION, DescriptionCompartmentEditPart.class);
	}
	
	/** list of supported connection editparts. */
	private Map connectionMap = new HashMap();
	{
		connectionMap.put(ViewType.NOTEATTACHMENT, NoteAttachmentEditPart.class);
	}
	
	/**
	 * Set the editpart class to the editpart mapped to the supplied view's semantic hint.
	 */
	protected Class getEdgeEditPartClass(View view) {
		return((Class) connectionMap.get(view.getType()));
	}
	
	/**
	 * Gets a Node's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 */
	protected Class getNodeEditPartClass(View view){
		String type = view.getType();
		Class clazz = null;
		if(type!=null && type.length()>0){
			clazz = (Class)textCompartmentMap.get(type);
			if(clazz==null)
				clazz = (Class)shapeMap.get(type);
		} else {
			if(NotationPackage.eINSTANCE.getDiagram().isSuperTypeOf(getReferencedElementEClass(view))){
				clazz = NoteEditPart.class;
			}
		}
	    return clazz;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof CreateRootEditPartOperation) {
			return true;
		}
		return super.provides(operation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider#createDiagramRootEditPart()
	 */
	public RootEditPart createRootEditPart(Diagram diagram) {
		return new DiagramRootEditPart(diagram.getMeasurementUnit());
	}
}
