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

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EAnnotationItemProvider;
import org.eclipse.emf.ecore.provider.EAttributeItemProvider;
import org.eclipse.emf.ecore.provider.EClassItemProvider;
import org.eclipse.emf.ecore.provider.EDataTypeItemProvider;
import org.eclipse.emf.ecore.provider.EEnumItemProvider;
import org.eclipse.emf.ecore.provider.EEnumLiteralItemProvider;
import org.eclipse.emf.ecore.provider.EFactoryItemProvider;
import org.eclipse.emf.ecore.provider.EOperationItemProvider;
import org.eclipse.emf.ecore.provider.EPackageItemProvider;
import org.eclipse.emf.ecore.provider.EParameterItemProvider;
import org.eclipse.emf.ecore.provider.EReferenceItemProvider;
import org.eclipse.emf.ecore.provider.EStringToStringMapEntryItemProvider;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;

/**
 * This class amanges the various EMF adapter factories provided by MSl
 * meta-model providers which must register thgeir factories.
 * 
 * @author rafikj
 */

public class MSLAdapterFactoryManager {

	private static MSLComposedAdapterFactory adapterFactory = new MSLComposedAdapterFactory();

	private static AdapterFactory coreFactory = new MSLEcoreItemProviderAdapterFactory();

	/**
	 * Initialize.
	 */
	public static void init() {

		register(coreFactory);

		MSLUtil.getMetaModel(EcorePackage.eINSTANCE.eClass());
		MSLMetaModelManager.register(EcorePackage.eINSTANCE, null);
	}

	/**
	 * Get the composed factory.
	 */
	public static MSLComposedAdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * Register an adapter factory with the MSL.
	 */
	public static void register(AdapterFactory factory) {

		adapterFactory.removeAdapterFactory(factory);
		adapterFactory.addAdapterFactory(factory);

		adapterFactory.removeAdapterFactory(coreFactory);
		adapterFactory.addAdapterFactory(coreFactory);

		for (Iterator i = MEditingDomain.getResourceSets().iterator(); i
			.hasNext();) {

			ResourceSet resourceSet = (ResourceSet) i.next();

			if (resourceSet != null) {

				Collection factories = resourceSet.getAdapterFactories();

				factories.remove(factory);
				factories.add(factory);

				factories.remove(coreFactory);
				factories.add(coreFactory);
			}
		}
	}
}

/**
 * This class Extends EcoreItemProviderAdapterFactory to fix the problem of
 * memory leak in org.eclipse.emf.core classes for Aurora compare and merge
 * sessions.
 *  
 * @author Duc Luu
 */
class MSLEcoreItemProviderAdapterFactory extends
		EcoreItemProviderAdapterFactory {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEAnnotationAdapter()
	 */
	public Adapter createEAnnotationAdapter() {

		if (eAnnotationItemProvider == null) {

			eAnnotationItemProvider = new EAnnotationItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

		return eAnnotationItemProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEAttributeAdapter()
	 */
	public Adapter createEAttributeAdapter() {

		if (eAttributeItemProvider == null) {

			eAttributeItemProvider = new EAttributeItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

		return eAttributeItemProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEClassAdapter()
	 */
	public Adapter createEClassAdapter() {

		if (eClassItemProvider == null) {

		      eClassItemProvider = new EClassItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eClassItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEDataTypeAdapter()
	 */
	public Adapter createEDataTypeAdapter() {

		if (eDataTypeItemProvider == null) {

			eDataTypeItemProvider = new EDataTypeItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eDataTypeItemProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEEnumAdapter()
	 */
	public Adapter createEEnumAdapter() {

		if (eEnumItemProvider == null) {

			eEnumItemProvider = new EEnumItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eEnumItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEEnumLiteralAdapter()
	 */
	public Adapter createEEnumLiteralAdapter() {

		if (eEnumLiteralItemProvider == null) {

			eEnumLiteralItemProvider = new EEnumLiteralItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eEnumLiteralItemProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEFactoryAdapter()
	 */
	public Adapter createEFactoryAdapter() {

		if (eFactoryItemProvider == null) {

			eFactoryItemProvider = new EFactoryItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eFactoryItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEObjectAdapter()
	 */
	public Adapter createEObjectAdapter() {

		if (eObjectItemProvider == null) {

			eObjectItemProvider = new ReflectiveItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eObjectItemProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEOperationAdapter()
	 */
	public Adapter createEOperationAdapter() {

		if (eOperationItemProvider == null) {

			eOperationItemProvider = new EOperationItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eOperationItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEPackageAdapter()
	 */
	public Adapter createEPackageAdapter() {

		if (ePackageItemProvider == null) {

			ePackageItemProvider = new EPackageItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return ePackageItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEParameterAdapter()
	 */
	public Adapter createEParameterAdapter() {

		if (eParameterItemProvider == null) {

			eParameterItemProvider = new EParameterItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eParameterItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEReferenceAdapter()
	 */
	public Adapter createEReferenceAdapter() {

		if (eReferenceItemProvider == null) {

			eReferenceItemProvider = new EReferenceItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eReferenceItemProvider;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreAdapterFactory#createEStringToStringMapEntryAdapter()
	 */
	public Adapter createEStringToStringMapEntryAdapter() {

		if (eStringToStringMapEntryItemProvider == null) {

			eStringToStringMapEntryItemProvider = new EStringToStringMapEntryItemProvider(this) {

				public void setTarget(Notifier target) {

					if (target != this.target) {

						if (null != this.target) {

							if (null == targets) {
								targets = new ArrayList();
							} else {

								// clean up stale references
								for (Iterator i = targets.iterator(); i
										.hasNext();) {
									Reference reference = (Reference) i
											.next();

									if (null == reference.get()) {
										i.remove();
									}
								}
							}

							targets.add(new WeakReference(this.target));
						}

						this.target = target;
					}
				}

				public void dispose() {

					if (null != target) {
						target.eAdapters().remove(this);
						target = null;
					}

					if (null != targets) {

						for (Iterator i = targets.iterator(); i.hasNext();) {
							Notifier otherTarget = (Notifier) ((Reference) i
									.next()).get();

							if (null != otherTarget) {
								otherTarget.eAdapters().remove(this);
							}
						}

						targets = null;
					}

					if (null != wrappers) {
						wrappers.dispose();
					}
				}
			};
		}

	    return eStringToStringMapEntryItemProvider;		
	}
	
}
