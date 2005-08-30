/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.index;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * This class manages the reverse reference map feature at the resource level.
 * When resources are loaded the reverse map gets updated without causing more
 * resources to be loaded.
 * 
 * @author rafikj
 */
public class MSLResourceIndexer {

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLResourceIndexer(MSLEditingDomain domain) {

		super();

		this.domain = domain;
	}

	/**
	 * Registers a reference in the imports and exports maps.
	 */
	public void registerReference(final Resource referencer,
			final Resource referenced) {

		if ((referencer != null) && (referenced != null)
			&& (referencer != referenced)) {

			Map importsMap = getImportsMap(referencer);

			if (importsMap == null)
				importsMap = createImportsMap(referencer);

			Integer importsCount = (Integer) importsMap.get(referenced);

			if (importsCount == null) {

				domain.sendNotification(new NotificationImpl(
					EventTypes.IMPORT, (Object) null, referenced, -1) {

					public Object getNotifier() {
						return referencer;
					}
				});

				importsCount = new Integer(1);

			} else
				importsCount = new Integer(importsCount.intValue() + 1);

			importsMap.put(referenced, importsCount);

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap == null)
				exportsMap = createExportsMap(referenced);

			Integer exportsCount = (Integer) exportsMap.get(referencer);

			if (exportsCount == null) {

				domain.sendNotification(new NotificationImpl(
					EventTypes.EXPORT, (Object) null, referencer, -1) {

					public Object getNotifier() {
						return referenced;
					}
				});

				exportsCount = new Integer(1);

			} else
				exportsCount = new Integer(exportsCount.intValue() + 1);

			exportsMap.put(referencer, exportsCount);
		}
	}

	/**
	 * Dregisters a reference in the imports and exports maps.
	 */
	public void deregisterReference(final Resource referencer,
			final Resource referenced) {

		if ((referencer != null) && (referenced != null)
			&& (referencer != referenced)) {

			Map importsMap = getImportsMap(referencer);

			if (importsMap != null) {

				Integer importsCount = (Integer) importsMap.get(referenced);

				if (importsCount != null) {

					if (importsCount.intValue() < 2) {

						importsMap.remove(referenced);

						domain.sendNotification(new NotificationImpl(
							EventTypes.IMPORT, referenced, (Object) null, -1) {

							public Object getNotifier() {
								return referencer;
							}
						});
					} else
						importsMap.put(referenced, new Integer(importsCount
							.intValue() - 1));
				}

				if (importsMap.isEmpty())
					removeImportsMap(referencer);
			}

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap != null) {

				Integer exportsCount = (Integer) exportsMap.get(referencer);

				if (exportsCount != null) {

					if (exportsCount.intValue() < 2) {

						exportsMap.remove(referencer);

						domain.sendNotification(new NotificationImpl(
							EventTypes.EXPORT, referencer, (Object) null, -1) {

							public Object getNotifier() {
								return referenced;
							}
						});
					} else
						exportsMap.put(referencer, new Integer(exportsCount
							.intValue() - 1));
				}

				if (exportsMap.isEmpty())
					removeExportsMap(referenced);
			}
		}
	}

	/**
	 * Registers references by traversing the referencer resource.
	 */
	public void registerReferences(Resource referencer) {

		List referencerContents = referencer.getContents();

		if (referencerContents == null)
			return;

		for (Iterator i = referencerContents.iterator(); i.hasNext();) {

			EObject referencerRoot = (EObject) i.next();

			if (referencerRoot != null) {

				domain.getObjectIndexer().registerReferences(referencerRoot,
					referencerRoot);

				Object[] exports = getExports(referencer).toArray();

				for (int j = 0; j < exports.length; j++) {

					Resource resource = (Resource) exports[j];

					List resourceContents = resource.getContents();

					if (resourceContents == null)
						continue;

					for (Iterator k = resourceContents.iterator(); k.hasNext();) {

						EObject resourceRoot = (EObject) k.next();

						if (resourceRoot != null)
							domain.getObjectIndexer().resolveReferences(
								resourceRoot, resourceRoot);
					}
				}
			}
		}
	}

	/**
	 * Cleans reference maps.
	 */
	public void deregisterReferences(final Resource referencer) {

		Object[] imports = getImports(referencer).toArray();

		for (int i = 0; i < imports.length; i++) {

			final Resource referenced = (Resource) imports[i];

			Map importsMap = getImportsMap(referencer);

			if (importsMap != null) {

				importsMap.remove(referenced);

				domain.sendNotification(new NotificationImpl(
					EventTypes.IMPORT, referenced, (Object) null, -1) {

					public Object getNotifier() {
						return referencer;
					}
				});

				if (importsMap.isEmpty())
					removeImportsMap(referencer);
			}

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap != null) {

				exportsMap.remove(referencer);

				domain.sendNotification(new NotificationImpl(
					EventTypes.EXPORT, referencer, (Object) null, -1) {

					public Object getNotifier() {
						return referenced;
					}
				});

				if (exportsMap.isEmpty())
					removeExportsMap(referenced);
			}
		}
	}

	/**
	 * Gets the imports of a resource.
	 */
	public Set getImports(Resource referencer) {

		Map importsMap = getImportsMap(referencer);

		if (importsMap != null)
			return Collections.unmodifiableSet(importsMap.keySet());
		else
			return Collections.EMPTY_SET;
	}

	/**
	 * Gets the exports of a resource.
	 */
	public Set getExports(Resource referenced) {

		Map exportsMap = getExportsMap(referenced);

		if (exportsMap != null)
			return Collections.unmodifiableSet(exportsMap.keySet());
		else
			return Collections.EMPTY_SET;
	}

	/**
	 * Gets imports map of a given resource.
	 */
	private Map getImportsMap(Resource referencer) {

		for (int i = 0, count = referencer.eAdapters().size(); i < count; i++) {

			Adapter adapter = (Adapter) referencer.eAdapters().get(i);

			if (adapter instanceof MSLImportsAdapter)
				return (Map) adapter;
		}

		return null;
	}

	/**
	 * Creates imports map of a given resource.
	 */
	private Map createImportsMap(Resource referencer) {

		Map map = new MSLImportsAdapter();

		referencer.eAdapters().add(map);

		return map;
	}

	/**
	 * Removes imports map of a given resource.
	 */
	private Map removeImportsMap(Resource referencer) {

		for (int i = 0, count = referencer.eAdapters().size(); i < count; i++) {

			Adapter adapter = (Adapter) referencer.eAdapters().get(i);

			if (adapter instanceof MSLImportsAdapter) {

				referencer.eAdapters().remove(i);

				return (Map) adapter;
			}
		}

		return null;
	}

	/**
	 * Gets exports map of a given resource.
	 */
	private Map getExportsMap(Resource referenced) {

		for (int i = 0, count = referenced.eAdapters().size(); i < count; i++) {

			Adapter adapter = (Adapter) referenced.eAdapters().get(i);

			if (adapter instanceof MSLExportsAdapter)
				return (Map) adapter;
		}

		return null;
	}

	/**
	 * Creates exports map of a given resource.
	 */
	private Map createExportsMap(Resource referenced) {

		Map map = new MSLExportsAdapter();

		referenced.eAdapters().add(map);

		return map;
	}

	/**
	 * Removes exports map of a given resource.
	 */
	private Map removeExportsMap(Resource referenced) {

		for (int i = 0, count = referenced.eAdapters().size(); i < count; i++) {

			Adapter adapter = (Adapter) referenced.eAdapters().get(i);

			if (adapter instanceof MSLExportsAdapter) {

				referenced.eAdapters().remove(i);

				return (Map) adapter;
			}
		}

		return null;
	}
}