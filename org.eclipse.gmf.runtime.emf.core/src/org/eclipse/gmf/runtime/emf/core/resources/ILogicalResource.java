/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.resources;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <p>
 * Interface implemented by {@link Resource}s that provide a virtual, or
 * "logical", view of an EMF model that is persisted in multiple distinct
 * physical resources.  In every way, the resource looks and behaves like a
 * monolithic resource; it is only in the persistence to multiple resources
 * that it differs.
 * </p><p>
 * The roots of the overall "logical" resource are under the control of the
 * client:  they may be added or removed at will, and even stored in separate
 * physical resources like any other element.  Elements in the content forest
 * may be {@link #separate(EObject, URI) separated} into a different physical
 * resource from its container.  Multiple elements in different locations in
 * the tree structure may be separated into different or even the same physical
 * resources.  Each separate element is a root in the resource that stores it.
 * </p><p>
 * Implementations of this interface cannot be instantiated directly.  Rather,
 * the MSL's
 * {@linkplain org.eclipse.gmf.runtime.emf.core.resources.MResourceFactory resource factory}
 * implementation creates <code>ILogicalResources</code> for any file extensions
 * or URI schemes on which it is registered.  To obtain the logical resource
 * view of any resource in an MSL editing domain, use the
 * {@link org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#asLogicalResource(Resource)}
 * method.
 * </p>
 *
 * @author Christian W. Damus (cdamus)
 * 
 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#asLogicalResource(Resource)
 * @see org.eclipse.gmf.runtime.emf.core.resources.MResourceFactory
 */
public interface ILogicalResource
	extends Resource, IAdaptable {
	
	/**
	 * Load option specifying whether to load the resource as a logical resource.
	 * If not loading as a logical resource, then only the physical resource
	 * indicated by the URI is loaded.
	 * The option value is expected to be a {@link Boolean}:  <code>TRUE</code>
	 * to load the URI as a logical resource (the default);
	 * <code>FALSE</code> to load only the single physical resource.  In the
	 * false case, the {@link #OPTION_LOAD_ALL_UNITS} and
	 * {@link #OPTION_AUTO_LOAD_UNITS} options are ignored.
	 * 
	 * @see #OPTION_LOAD_ALL_UNITS
	 * @see #OPTION_AUTO_LOAD_UNITS
	 */
	public static final String OPTION_LOAD_AS_LOGICAL = "LogicalResource.loadAsLogical"; //$NON-NLS-1$
	
	/**
	 * Load option specifying whether all physical resources (units) comprising
	 * the logical resource are to be loaded immediately.
	 * The option value is expected to be a {@link Boolean}:  <code>TRUE</code>
	 * to indicate that all resources should be loaded immediately;
	 * <code>FALSE</code> to load of subunits later (the default).
	 * 
	 * @see #load(EObject)
	 * @see #OPTION_AUTO_LOAD_UNITS
	 */
	public static final String OPTION_LOAD_ALL_UNITS = "LogicalResource.loadAllUnits"; //$NON-NLS-1$
	
	/**
	 * Load option specifying whether physical resources (units) comprising
	 * the logical resource should be loaded automatically, as needed, if
	 * supported by the metamodel.
	 * The option value is expected to be a {@link Boolean}:  <code>TRUE</code>
	 * to indicate that resources should be loaded automatically (the default);
	 * <code>FALSE</code> to require explicit loading of resources.
	 * <p>
	 * This option is ignored if all resources are loaded immediately.
	 * </p>
	 * 
	 * @see #load(EObject)
	 * @see #OPTION_LOAD_ALL_UNITS
	 */
	public static final String OPTION_AUTO_LOAD_UNITS = "LogicalResource.autoLoadUnits"; //$NON-NLS-1$
	
	/**
	 * Queries whether the specified <code>eObject</code>'s metamodel permits it
	 * to be stored as a separate resource.  The <code>eObject</code> must
	 * be contained in me and must not already be a separate resource.
	 * 
	 * @param eObject a model element
	 * @return whether it can be stored separately
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is
	 *     not contained within me
	 * 
	 * @see #isSeparate(EObject)
	 */
	boolean canSeparate(EObject eObject);
	
	/**
	 * Queries whether the specified <code>eObject</code> is the root of a
	 * separate resource in this logical resource.  The eObject must be
	 * contained in me.
	 * 
	 * @param eObject a model element
	 * @return whether it is the root of a separate resource
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is
	 *     not contained withing me
	 */
	boolean isSeparate(EObject eObject);
	
	/**
	 * Ensures that the specified <code>eObject</code> is the root of a
	 * separate resource, if it is {@linkplain #canSeparate(EObject) allowed}
	 * to be such.
	 * 
	 * @param eObject a model element
	 * @param uri URI for the physical resource to store the
	 *    <code>eObject</code> in, or <code>null</code> for the default
	 * 
	 * @throws CannotSeparateException if something unforeseen goes wrong in
	 *     the separation of the element.  This may be failure to make a
	 *     version-controlled file writeable, failure to meet metamodel-specific
	 *     constraints on separation, attempt to separate into the same physical
	 *     resource that already stores the element, etc.
	 * @throws IllegalArgumentException if the <code>eObject</code> is already
	 *     separate or if it is not contained within me
	 * 
	 * @see #canSeparate(EObject)
	 * @see #absorb(EObject)
	 */
	void separate(EObject eObject, URI uri) throws CannotSeparateException;
	
	/**
	 * Ensures that the specified <code>eObject</code> is not the root of a
	 * separate resource.
	 * 
	 * @param eObject a model element
	 * 
	 * @throws CannotAbsorbException if something unforeseen goes wrong in
	 *     the absorption of the element.  This may be failure to make a
	 *     version-controlled file writeable, failure to meet metamodel-specific
	 *     constraints on absorption, etc.
	 * @throws IllegalArgumentException if the <code>eObject</code> is not
	 *     separate, if it is not contained within me, or if it is not
	 *     {@linkplain #isLoaded(EObject) loaded}
	 * 
	 * @see #separate(EObject, URI)
	 */
	void absorb(EObject eObject) throws CannotAbsorbException;
	
	/**
	 * In the case that a resource having separate elements was loaded without
	 * the option to {@linkplain #OPTION_LOAD_ALL_UNITS load all units}, queries
	 * whether the specified <code>eObject</code> is currently loaded.  If it
	 * is not loaded, then it is just a stand-in for the absent sub-tree of
	 * the logical model.
	 * 
	 * @param eObject an element in the logical resource.  It may be a separate
	 *      element (in which case the question of loaded is interesting) or
	 *      it may not.  In the latter case, the element is necessarily
	 *      to be considered as loaded
	 * @return <code>true</code> if the <code>eObject</code> is fully loaded
	 *      from its physical resource; <code>false</code>, otherwise
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is not
	 *     contained within me
	 *     
	 * @see #OPTION_LOAD_ALL_UNITS
	 * @see #load(EObject)
	 */
	boolean isLoaded(EObject eObject);
	
	/**
	 * In the case that a resource having separate elements was loaded without
	 * the option to {@linkplain #OPTION_LOAD_ALL_UNITS load all units}, loads
	 * the specified eObject from its physical resource.
	 * 
	 * @param eObject a separate element in the logical resource
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is not
	 *     separate or if it is not contained within me
	 * @throws IOException if any exception occurs while loading the
	 *     <code>eObject</code>'s physical resource
	 *     
	 * @see #OPTION_LOAD_ALL_UNITS
	 * @see #isLoaded(EObject)
	 */
	void load(EObject eObject) throws IOException;
		
	/**
	 * <p>
	 * Obtains a mapping of the separate resource comprising me.  The
	 * mapping includes every element that is stored as a root of a physical
	 * resource, mapped to that physical resource.
	 * </p><p>
	 * <b>Note</b> that the resources in the resulting map are unmodifiable
	 * views of the actual resources.  Their URIs, modified/loaded states,
	 * contents, and other information may be retrieved but not modified
	 * in any way (throwing {@link UnsupportedOperationException}s) including
	 * by loading or saving.  However, adapters can be added to these views
	 * to track changes to the underlying resource, though use of content
	 * adapters is discouraged as this is most appropriate on the logical
	 * resource.
	 * </p>
	 * 
	 * @return a mapping of {@link EObject} ==&gt; {@link Resource}.  Note that
	 *     the resources in this map are unmodifiable views of the actual
	 *     resources
	 */
	Map getMappedResources();
}
