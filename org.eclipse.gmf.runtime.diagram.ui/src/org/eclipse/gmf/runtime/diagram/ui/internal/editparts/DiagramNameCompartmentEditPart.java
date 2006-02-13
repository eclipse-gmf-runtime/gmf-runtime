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

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconOptions;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.CreateDiagramLinkCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;

/**
 * Name compartment for NalDiagramView elements.  
 * This compartment is not editable. 
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class DiagramNameCompartmentEditPart extends TextCompartmentEditPart {
	
	private IconOptions iconOptions;
	protected static final int ICON_INDEX = 0;
	protected static final int NUM_ICONS = 1;	

	/**
	 * @param view
	 */
	public DiagramNameCompartmentEditPart(View view) {
		super(view);
		iconOptions = new IconOptions();
		iconOptions.set(IconOptions.GET_STEREOTYPE_IMAGE_FOR_ELEMENT);
		iconOptions.set(IconOptions.NO_DEFAULT_STEREOTYPE_IMAGE);
		setNumIcons(NUM_ICONS);
	}

	protected IFigure createFigure() {
		WrapLabel label = new WrapLabel();
		label.setLabelAlignment(PositionConstants.TOP);
		label.setIconAlignment(PositionConstants.TOP);
		label.setTextAlignment(PositionConstants.TOP);
		label.setTextWrap(true);
		label.setTextWrapAlignment(PositionConstants.CENTER);
		return label;
	}

	/** Return the semantic element associated to this editpart. */
	public EObject resolveSemanticElement() {
		return (EObject) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {

			public Object run() {
				View primary = getPrimaryView();
				if (primary != null)
					return ViewUtil.resolveSemanticElement(primary);
				return null;
			}
		});
	}

	/**
	 * Returns the icon image associated with the diagram.
	 * @param the 
	 * @return Image
	 */
	protected Image getLabelIcon(int i) {
		EObject element = resolveSemanticElement();
		if (element != null)
			return IconService.getInstance().getIcon(new EObjectAdapter(element));
		return null;
	}
	
	/**
	 * @return <tt>false</tt> 
	 */
	protected boolean isEditable() { 
		return false; 
	}
	
	/**
	 * Selectable if the parent edit part is not a DiagramLinkEditPart.
	 *  @return <tt>false</tt> if the parent is a DiagramLinkEditPart.
	 */
	public boolean isSelectable() {
		return false;
	}
	
	public void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DiagramLinkComponentEditPolicy());	
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setVisibilty(boolean)
	 */
	protected void setVisibility(boolean vis) {
		super.setVisibility(vis && resolveSemanticElement() != null);
	}

	private class DiagramLinkComponentEditPolicy extends ComponentEditPolicy {
		
		/**
		 * Returns a command to set the model to null. 
		 */
		public Command createDeleteViewCommand(GroupRequest request) {
            
            TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
                .getEditingDomain();
            
			CreateDiagramLinkCommand com = new CreateDiagramLinkCommand(editingDomain,
				DiagramUIMessages.Command_CreateDiagramLink,
					(View)getHost().getParent().getModel(), 
					null);
			return new EtoolsProxyCommand(com);
		}
	}
}
