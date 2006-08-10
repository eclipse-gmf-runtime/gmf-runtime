/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.NoteFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DiagramLinkDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;

/**
 * Note shape which provides textual annotations for diagram elements.  Notes are 
 * attachable. 
 * <p>
 * Notes support diagram links.
 * <p>
 * @author jcorchis
 */
public class NoteEditPart extends ShapeNodeEditPart {
    
    // resource listener
    private ResourceListener listener = null;
    
    private boolean diagramLinkMode = false;

	/**
	 * constructor
	 * @param view the view controlled by this edit part
	 */
	public NoteEditPart(View view) {
		super(view);
	}

	/**
	 * Creates a note figure.
	 */
	protected NodeFigure createNodeFigure() {
		IMapMode mm = getMapMode();
		Insets insets = new Insets(mm.DPtoLP(5), mm.DPtoLP(5), mm.DPtoLP(5), mm.DPtoLP(14));
		NoteFigure noteFigure = new NoteFigure(mm.DPtoLP(100), mm.DPtoLP(56), insets);
		Object model = getModel();
		if (model!=null && model instanceof View){
			View notationView = (View)model;
			if ( notationView!=null && 
				 (notationView.getEAnnotation(Properties.DIAGRAMLINK_ANNOTATION)!=null ||
				  notationView.getType() == null ||
				  notationView.getType().length() == 0)){
                diagramLinkMode = true;
				noteFigure.setDiagramLinkMode(true);
            }
		}
		return noteFigure;
	}

	/** Adds support for diagram links. */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new NonSemanticEditPolicy());

		// Add Note support for diagram links
		// The following two edit policies support the links.		
		installEditPolicy(
			EditPolicyRoles.DRAG_DROP_ROLE,
			new DiagramLinkDragDropEditPolicy());
		
		installEditPolicy(
			EditPolicyRoles.OPEN_ROLE,
			new OpenDiagramEditPolicy());

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}
	
	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}
    
    public Object getPreferredValue(EStructuralFeature feature) {
        Object preferenceStore = getDiagramPreferencesHint()
            .getPreferenceStore();
        if (preferenceStore instanceof IPreferenceStore) {
            if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()) {
                
                return FigureUtilities.RGBToInteger(PreferenceConverter
                    .getColor((IPreferenceStore) preferenceStore,
                        IPreferenceConstants.PREF_NOTE_LINE_COLOR));
                
            } else if (feature == NotationPackage.eINSTANCE
                .getFillStyle_FillColor()) {
                
                return FigureUtilities.RGBToInteger(PreferenceConverter
                    .getColor((IPreferenceStore) preferenceStore,
                        IPreferenceConstants.PREF_NOTE_FILL_COLOR));
                
            }
        }

        return super.getPreferredValue(feature);
    } 
    
    private class ResourceListener extends ResourceSetListenerImpl{
        private WeakReference resourceRef = null; 
        private EditPart editPart= null;
        public ResourceListener(Resource resource, EditPart editPart){
            resourceRef = new WeakReference(resource);
            this.editPart = editPart;
        }
        
        /**
         * Disposes my context from the operation history when a resource is
         * unloaded from my editing domain.
         */
        public void resourceSetChanged(ResourceSetChangeEvent event) {
            if (editPart ==null || resourceRef.get()==null)
                return;
            boolean unloaded = isResourceUnloaded(event.getNotifications());
            if (unloaded && editPart.isActive()) {
                editPart.refresh();
            }
        }
        
        /**
         * Finds resources that have sent unload notifications.
         * 
         * @param notifications
         *            notifications received from a transaction
         * @return a set of resources that the notifications indicate have been
         *         unloaded, or <code>null</code> if none
         */
        private boolean isResourceUnloaded(Collection notifications) {
            for (Iterator iter = notifications.iterator(); iter.hasNext();) {
                Notification next = (Notification) iter.next();
                if (next.getNotifier()!=resourceRef.get())
                    return false;
                if (NotificationFilter.RESOURCE_UNLOADED.matches(next)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isPostcommitOnly() {
            // only interested in post-commit "resourceSetChanged" event
            return true;
        }

    }
    
    protected void addSemanticListeners() {
        // the resource listener is needed only in diagram link mode
        if (diagramLinkMode){
            if (listener==null){
                listener = new ResourceListener(getNotationView().getElement().eResource(),this);
            }
            getEditingDomain().addResourceSetListener(listener);
        }
        super.addSemanticListeners();
    }

    protected void removeSemanticListeners() {
        //the resource listener is needed only in diagram link mode
        if (listener!=null)
            getEditingDomain().removeResourceSetListener(listener);
        super.removeSemanticListeners();
    }

}
