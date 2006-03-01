/******************************************************************************
 * Copyright (c) 2002, 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The following class implements the Notation semantic procedures.
 * 
 * @author rafikj, cmahoney
 */
public class NotationSemProc extends TriggerListener {

	private static NotationPackage notation = NotationPackage.eINSTANCE;

	private static Set autoNamedElements = new HashSet();

	private static Set nonAutoNamedElements = new HashSet();

	static {

		autoNamedElements.add(notation.getDiagram());
	}

	/**
	 * Handle sem-proc event.
	 */
    protected Command trigger(TransactionalEditingDomain editingDomain, Notification notification) {

		int eventType = notification.getEventType();

		Object notifier = notification.getNotifier();

		Object feature = notification.getFeature();

		Object newValue = notification.getNewValue();

		if (notifier instanceof EObject) {

			EObject eObject = (EObject) notifier;

			if ((feature instanceof EReference)
				&& (((EReference) feature).isContainment())) {

				if ((eventType == Notification.ADD)
					&& (newValue instanceof EObject)) {

					return getAutoNameCommand(editingDomain, eObject, (EReference) feature, (EObject) newValue);

				} else if ((eventType == Notification.ADD_MANY)
					&& (newValue instanceof Collection)) {

                    return getAutoNameCommand(editingDomain, eObject, (EReference) feature,
						(Collection) newValue);

				} else if (((eventType == Notification.SET) || (eventType == Notification.UNSET))) {

					if (newValue instanceof EObject) {

                        return getAutoNameCommand(editingDomain, eObject, (EReference) feature,
							(EObject) newValue);

					}

				}
			}
            
            ///////////////////////////////////////////////////////
            // TODO:  Remove when we can contribute edit helper advice when destroying an EObject.    
            if (notification.getEventType() == Notification.REMOVE) {
                final EObject removedObject = (EObject) notification.getOldValue();

                if (removedObject.eResource() == null) {
                    // this is the equivalent of a destroy
                    
                    return new RecordingCommand(editingDomain) {
                        protected void doExecute() {
                            preProcessDestroy(removedObject);
                        }
                    };
                    
                } else if (NotationPackage.eINSTANCE.getView_SourceEdges()
                    .equals(feature)) {

                    final Edge edge = (Edge) notification.getOldValue();
                    if (edge.getSource() == null) {
                        return new RecordingCommand(editingDomain) {

                            protected void doExecute() {
                                EMFCoreUtil.destroy(edge);
                            }
                        };
                    }
                    
                } else if (NotationPackage.eINSTANCE.getView_TargetEdges()
                    .equals(feature)) {

                    final Edge edge = (Edge) notification.getOldValue();
                    if (edge.getTarget() == null) {
                        return new RecordingCommand(editingDomain) {

                            protected void doExecute() {
                                EMFCoreUtil.destroy(edge);
                            }
                        };
                    }
                    
                }     
            }
            ///////////////////////////////////////////////////////
		}
        return null;
	}

    /**
     * Returns a command that will auto-name the collection of
     * <code>EObjects</code> passed in.
     * 
     * @param editingDomain
     * @param container
     * @param reference
     * @param objects
     * @return
     */
    private Command getAutoNameCommand(
            TransactionalEditingDomain editingDomain, final EObject container,
            final EReference reference, final Collection objects) {
        return new RecordingCommand(editingDomain) {

            protected void doExecute() {
                autoName(container, reference, objects);
            }

        };
    }

    /**
     * Returns a command that will auto-name the <code>EObject</code> passed
     * in.
     * 
     * @param editingDomain
     * @param container
     * @param reference
     * @param eObject
     * @return
     */
    private Command getAutoNameCommand(
            TransactionalEditingDomain editingDomain, final EObject container,
            final EReference reference, final EObject eObject) {
        return new RecordingCommand(editingDomain) {

            protected void doExecute() {
                autoName(container, reference, eObject);
            }

        };
    }

    /**
     * Can I auto-name object?
     */
	private static boolean canAutoName(EClass eClass) {

		if (autoNamedElements.contains(eClass))
			return true;

		if (nonAutoNamedElements.contains(eClass))
			return false;

		Iterator i = eClass.getESuperTypes().iterator();

		while (i.hasNext()) {

			if (canAutoName((EClass) i.next()))
				return true;
		}

		return false;
	}

	/**
	 * Auto-name objects.
	 */
	private static void autoName(EObject container, EReference reference,
			Collection objects) {

		for (Iterator i = objects.iterator(); i.hasNext();) {

			Object object = i.next();

			if (object instanceof EObject) {

				EObject eObject = (EObject) object;

				autoName(container, reference, eObject);
			}
		}
	}

	/**
	 * Auto-name object.
	 */
	private static void autoName(EObject container, EReference reference,
			EObject eObject) {

		String name = null;

		if (eObject instanceof Diagram)
			name = ((Diagram) eObject).getName();

		else
			return;

		if (((name == null) || (name.equals(EMFCoreConstants.EMPTY_STRING))
			&& (canAutoName(eObject.eClass())))) {

			name = PackageUtil.getLocalizedName(eObject.eClass());

			if (reference.isMany()) {

				Set set = new HashSet();

				Iterator i = ((Collection) container.eGet(reference))
					.iterator();

				while (i.hasNext()) {

					Object sibling = i.next();

					if (sibling != null) {

						String n = null;

						if (sibling instanceof Diagram)
							n = ((Diagram) sibling).getName();

						if (n != null)
							set.add(n);
					}
				}

				for (int j = 1; j <= Integer.MAX_VALUE; j++) {

					String n = name + j;

					if (!set.contains(n)) {

						name = n;
						break;
					}
				}
			}

			if (eObject instanceof Diagram)
				((Diagram) eObject).setName(name);
		}
	}
    
	/**
	 * Pre-process destroy.
	 */
	private static void preProcessDestroy(EObject eObject) {
        Collection semanticReferencers = EMFCoreUtil.getReferencers(
            eObject, new EReference[] {NotationPackage.eINSTANCE
                .getView_Element()});

        for (Iterator i = semanticReferencers.iterator(); i.hasNext();) {

            EObject referencer = (EObject) i.next();

            if (referencer != null) {
                EMFCoreUtil.destroy(referencer);
            }
        }

        if (eObject instanceof View) {

            Collection nodeEntryKeys = EMFCoreUtil.getReferencers(eObject,
                new EReference[] {NotationPackage.eINSTANCE
                    .getNodeEntry_Key()});

            for (Iterator i = nodeEntryKeys.iterator(); i.hasNext();) {

                EObject nodeEntry = (EObject) i.next();

                if (nodeEntry != null)
                    EMFCoreUtil.destroy(nodeEntry);
            }
        }
	}

}