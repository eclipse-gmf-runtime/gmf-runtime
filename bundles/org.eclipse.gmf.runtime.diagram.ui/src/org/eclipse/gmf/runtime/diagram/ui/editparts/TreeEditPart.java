/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconOptions;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.DescriptionStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;

import java.util.StringTokenizer;

/**
 * @author melaasar, mmostafa
 */
public class TreeEditPart
    extends AbstractTreeEditPart
    implements NotificationListener, IEditingDomainProvider {

    /** the element parser */
    private IParser parser;

    /** the element parser */
    private IAdaptable referenceAdapter;
    
    private EObject[] objectListenningTo = new EObject[2];

    /**
     * Cache the editing domain after it is retrieved.
     */
    private TransactionalEditingDomain editingDomain;

    /**
     * Constructor
     * @param model
     */
    public TreeEditPart(Object model) {
        super(model);
        
        EObject reference = ((View)model).getElement();
        if (reference == null) {
        
            this.referenceAdapter = new EObjectAdapter((EObject)model);
        } else {
            this.referenceAdapter =
                new EObjectAdapter(reference);
        }
    }

    /**
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate() {
        if (isActive())
            return;
        super.activate();
        View view = (View)getModel();
        EObject semanticElement = getSemanticElement();
        getDiagramEventBroker().addNotificationListener(view,this);
        getDiagramEventBroker().addNotificationListener(semanticElement,this);
        objectListenningTo[0] = view ;
        objectListenningTo[1] = semanticElement;
    }

    /**
     * @see org.eclipse.gef.EditPart#deactivate()
     */
    public void deactivate() {
        if (!isActive())
            return;
        for (int index = 0; index < objectListenningTo.length; index++) {
            getDiagramEventBroker().removeNotificationListener( objectListenningTo[index],this);
            objectListenningTo[index] = null;
        }
        super.deactivate();
    }

    /** gets the model as a <code>View</code>
     * @return View
     */
    protected View getNotationView() {
        if (getModel() instanceof View)
            return (View)getModel();
        return null;
    }

    /**
     * Return the editpart's underlying semantic element.
     * @return semantic element
     */
    protected EObject getSemanticElement() {
        return ViewUtil.resolveSemanticElement((View)getModel());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
     */
    protected Image getImage() {
        if (referenceAdapter == null){
            return null;
        }
        IconOptions options = new IconOptions();
        options.set(IconOptions.GET_STEREOTYPE_IMAGE_FOR_ELEMENT);
        return IconService.getInstance().getIcon(referenceAdapter, options.intValue());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
     */
    protected String getText() {
        if (getParser() != null)
            return getParser().getPrintString(referenceAdapter,
                ParserOptions.NONE.intValue());
        EObject eObject = ((View) getModel()).getElement();
        if (eObject != null) {
            String name = EMFCoreUtil.getName(eObject);
            if (name != null) {
                return name;
            }
        }

        DescriptionStyle descriptionStyle = (DescriptionStyle) ((View) getModel())
            .getStyle(NotationPackage.eINSTANCE.getDescriptionStyle());

        if (descriptionStyle != null) {
            String text = descriptionStyle.getDescription();
            StringTokenizer tokenizer = new StringTokenizer(text, "\n\r\f"); //$NON-NLS-1$
            if (tokenizer.hasMoreTokens()) {
                text = tokenizer.nextToken();
            }
            if (text.length() > 50) {
                text = text.substring(0, 50);
                text = text.concat(StringStatics.ELLIPSIS);
            }
            return text;
        }

        return super.getText(); 
    }

    /**
     * Method getParser.
     * @return IParser
     */
    private IParser getParser() {
        if (parser == null) {
            if (referenceAdapter != null && referenceAdapter.getAdapter(EObject.class) != null)
                parser = ParserService.getInstance().getParser(referenceAdapter);
        }
        return parser;
    }

    /**
     * Handles the passed property changed event only if the editpart's view is not deleted
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public final void notifyChanged(Notification event) {
        // Receiving an event while a view is deleted could only happen during "undo" of view creation,
        // However, event handlers should be robust by using the event's value and not trying to read 
        // the value from the model
        if ((((View)getModel()).eResource() != null))
            handleNotificationEvent(event);
    }

    /**
     * Handles the supplied notification event. 
     * @param event
     */
    protected void handleNotificationEvent( Notification notification ) {
        Object notifier = notification.getNotifier();
        if (NotationPackage.Literals.VIEW__ELEMENT==notification.getFeature()) {
            reactivateSemanticElement();
        } else if (notification.getNotifier() == getSemanticElement() ||
                   notifier instanceof Style){
            refreshVisuals();
        }
    }
    
    /**
     * deactivates, activates then refreshes the editpart
     */
    protected void reactivateSemanticElement() {
        deactivate();
        activate();
        refresh();
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
     */
    public Object getAdapter(Class key) {
        // Adapt to IActionFilter
        if (key == IActionFilter.class) {
            return ActionFilterService.getInstance();
        }
        
        if (View.class.isAssignableFrom(key)) {
            Object _model = getModel();
            if (key.isInstance(_model))
                return _model;
            else
                return null;
        }
        
        Object model = getModel();
        
        if (model != null && model instanceof View &&
            EObject.class.isAssignableFrom(key)) {
            // Adapt to semantic element
            EObject semanticObject = ((View)model).getElement();
            if (semanticObject!= null && !semanticObject.eIsProxy() && key.isInstance(semanticObject)){
                return semanticObject;
            }
        }
        return super.getAdapter(key);
    }
    
    /**
     * Derives my editing domain from my diagram element. Subclasses may
     * override.
     */
    public EditingDomain getEditingDomain() {
        if (editingDomain == null) {
            editingDomain = TransactionUtil.getEditingDomain(getModel());
        }
        return editingDomain;
    }
    
    /**
     * Gets the diagram event broker from the editing domain.
     * 
     * @return the diagram event broker
     */
    protected DiagramEventBroker getDiagramEventBroker() {
        EditingDomain theEditingDomain = getEditingDomain();
        if (theEditingDomain instanceof TransactionalEditingDomain) {
            return DiagramEventBroker
                .getInstance((TransactionalEditingDomain) theEditingDomain);
        }
        return null;
    }
}
