/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DescriptionCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DiagramNameCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TextEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * Supports the creation of <b>presentation</b> editparts elements.  
 * Presentation elements are commonly used by all plugins.
 *  
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 * 
 */
public class PresentationEditPartProvider extends AbstractEditPartProvider {

	/** list of supported shape editparts. */
	private Map shapeMap = new HashMap();
	{
		shapeMap.put(ViewType.NOTE, NoteEditPart.class);
		shapeMap.put(ViewType.TEXT, TextEditPart.class);
	}
	/** list of supportted text editparts. */
	private Map textCompartmentMap = new HashMap();
	{
		textCompartmentMap.put(ViewType.DIAGRAM_NAME, DiagramNameCompartmentEditPart.class);
		textCompartmentMap.put(CommonParserHint.DESCRIPTION, DescriptionCompartmentEditPart.class);
	}
	
	/** list of supported connector editparts. */
	private Map connectorMap = new HashMap();
	{
		connectorMap.put(ViewType.NOTEATTACHMENT, NoteAttachmentEditPart.class);
	}
	
	/**
	 * Set the editpart class to the editpart mapped to the supplied view's semantic hint.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#setConnectorEditPartClass(org.eclipse.gmf.runtime.diagram.ui.internal.view.IConnectorView)
	 */
	protected Class getConnectorEditPartClass(View view) {
		return((Class) connectorMap.get(view.getType()));
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
			if(getReferencedElementEClass(view)== NotationPackage.eINSTANCE.getDiagram()){
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
	public RootEditPart createRootEditPart() {
		return new DiagramRootEditPart();
	}
}
