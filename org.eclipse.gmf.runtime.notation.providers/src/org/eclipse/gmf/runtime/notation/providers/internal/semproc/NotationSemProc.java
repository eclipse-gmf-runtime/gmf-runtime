/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.notation.providers.internal.semproc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The following class implements the UML semantic procedures.
 * 
 * @author rafikj
 */
public class NotationSemProc {

	private static NotationPackage notation = NotationPackage.eINSTANCE;

	private static Set autoNamedElements = new HashSet();

	private static Set nonAutoNamedElements = new HashSet();

	static {

		autoNamedElements.add(notation.getDiagram());
	}

	/**
	 * Handle sem-proc event.
	 */
	public static void handleEvent(Notification event) {

		int eventType = event.getEventType();

		Object notifier = event.getNotifier();

		Object feature = event.getFeature();

		Object newValue = event.getNewValue();
		Object oldValue = event.getOldValue();

		if (notifier instanceof EObject) {

			EObject eObject = (EObject) notifier;

			if ((feature instanceof EReference)
				&& (((EReference) feature).isContainment())) {

				if ((eventType == Notification.ADD)
					&& (newValue instanceof EObject)) {

					autoName(eObject, (EReference) feature, (EObject) newValue);

					postProcessAdd(eObject, (EObject) newValue);

				} else if ((eventType == Notification.ADD_MANY)
					&& (newValue instanceof Collection)) {

					autoName(eObject, (EReference) feature,
						(Collection) newValue);

					postProcessAdd(eObject, (Collection) newValue);

				} else if ((eventType == Notification.REMOVE)
					&& (oldValue instanceof EObject)) {

					postProcessRemove(eObject, (EObject) oldValue);

				} else if ((eventType == Notification.REMOVE_MANY)
					&& (oldValue instanceof Collection)) {

					postProcessRemove(eObject, (Collection) oldValue);

				} else if (((eventType == Notification.SET) || (eventType == Notification.UNSET))) {

					if (newValue instanceof EObject) {

						autoName(eObject, (EReference) feature,
							(EObject) newValue);

						postProcessAdd(eObject, (EObject) newValue);
					}

					if (oldValue instanceof EObject)
						postProcessRemove(eObject, (EObject) oldValue);
				}

			} else if (feature instanceof EAttribute) {

				if ((eventType == Notification.SET)
					|| (eventType == Notification.UNSET))
					postProcessSet(eObject, (EAttribute) feature, newValue,
						oldValue);
			}

			if (eventType == EventTypes.CREATE)
				postProcessCreate(eObject);

			else if (eventType == EventTypes.DESTROY)
				preProcessDestroy(eObject);
		}
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

		if (((name == null) || (name.equals(MSLConstants.EMPTY_STRING))
			&& (canAutoName(eObject.eClass())))) {

			name = MetaModelUtil.getLocalName(eObject.eClass());

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
	 * Post-process add.
	 */
	private static void postProcessAdd(EObject newContainer, Collection objects) {

		for (Iterator i = objects.iterator(); i.hasNext();) {

			Object object = i.next();

			if (object instanceof EObject) {

				EObject eObject = (EObject) object;

				postProcessAdd(newContainer, eObject);
			}
		}
	}

	/**
	 * Post-process add.
	 */
	private static void postProcessAdd(EObject newContainer, EObject eObject) {
		// do nothing.
	}

	/**
	 * Post-process remove.
	 */
	private static void postProcessRemove(EObject oldContainer,
			Collection objects) {

		for (Iterator i = objects.iterator(); i.hasNext();) {

			Object object = i.next();

			if (object instanceof EObject) {

				EObject eObject = (EObject) object;

				postProcessRemove(oldContainer, eObject);
			}
		}
	}

	/**
	 * Post-process remove.
	 */
	private static void postProcessRemove(EObject oldContainer, EObject eObject) {
		// do nothing.
	}

	/**
	 * Post-process an attribute set.
	 */
	private static void postProcessSet(EObject eObject, EAttribute attribute,
			Object newValue, Object oldValue) {
		// do nothing.
	}

	/**
	 * Post-process create.
	 */
	private static void postProcessCreate(EObject eObject) {
		// do nothing.
	}

	/**
	 * Pre-process destroy.
	 */
	private static void preProcessDestroy(EObject eObject) {

		Collection semanticReferencers = EObjectUtil.getReferencers(eObject,
			new EReference[] {NotationPackage.eINSTANCE
				.getView_Element()});

		for (Iterator i = semanticReferencers.iterator(); i.hasNext();) {

			EObject referencer = (EObject) i.next();

			if (referencer != null) {
				EObjectUtil.destroy(referencer);
			}
		}

		if (eObject instanceof View) {

			View view = (View) eObject;

			Collection allEdges = new HashSet();

			allEdges.addAll(view.getSourceEdges());
			allEdges.addAll(view.getTargetEdges());
			
			Resource resource = eObject.eResource();
			MSLEditingDomain domain = null;

			if (resource != null)
				domain = (MSLEditingDomain) MEditingDomain
					.getEditingDomain(resource);

			if (domain == null)
				domain = (MSLEditingDomain) MEditingDomain.INSTANCE;


			for (Iterator i = allEdges.iterator(); i.hasNext();) {

				EObject edge = ProxyUtil.resolve(domain, (InternalEObject) i.next());

				if (edge != null)
					EObjectUtil.destroy(edge);
			}

			Collection nodeEntryKeys = EObjectUtil
				.getReferencers(eObject,
					new EReference[] {NotationPackage.eINSTANCE
						.getNodeEntry_Key()});

			for (Iterator i = nodeEntryKeys.iterator(); i.hasNext();) {

				EObject nodeEntry = (EObject) i.next();

				if (nodeEntry != null)
					EObjectUtil.destroy(nodeEntry);
			}
		}
	}
}