/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResource;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;

/**
 * This class contains a set of <code>Resource</code> related utility methods.
 * 
 * @author rafikj
 */
public class ResourceUtil {

	/**
	 * Gets the MSL framework’s resource set. The resource set is the root
	 * object from which we can get the resources from which we can get the
	 * resource objects.
	 * 
	 * @return The resource set.
	 */
	public static ResourceSet getResourceSet() {
		return MEditingDomain.INSTANCE.getResourceSet();
	}

	/**
	 * Gets the editing domain.
	 * 
	 * @return The editing domain.
	 */
	public static EditingDomain getEditingDomain() {
		return MEditingDomain.INSTANCE;
	}

	/**
	 * Gets the adapter factory.
	 * 
	 * @return The adapter factory.
	 */
	public static AdapterFactory getAdapterFactory() {
		return MSLAdapterFactoryManager.getAdapterFactory();
	}
	
	/**
	 * Queries whether a <code>resource</code> is a logical resource.
	 * This can be considered as a foreign method for the {@link Resource} API.
	 * 
	 * @param resource a resource
	 * @return whether the <code>resource</code> is a logical resource
	 * 
	 * @see ILogicalResource
	 */
	public static boolean isLogicalResource(Resource resource) {
		return MEditingDomain.INSTANCE.isLogicalResource(resource);
	}

	/**
	 * Obtains a view of the specified <code>resource</code> as a logical
	 * resource.  Note that, in the case where the <code>resource</code> does not
	 * actually support the logical resource API's capabilities, an adapter is
	 * returned that implements the interface but does not allow the separation
	 * of any elements.
	 * 
	 * @param resource a resource for which we want to obtain a logical view
	 * @return the logical view of the resource
	 */
	public static ILogicalResource asLogicalResource(Resource resource) {
		return MEditingDomain.INSTANCE.asLogicalResource(resource);
	}
	
	/**
	 * Gets the path of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The file path of the resource.
	 */
	public static String getFilePath(Resource resource) {
		return MEditingDomain.INSTANCE.getResourceFileName(resource);
	}

	/**
	 * Sets the path of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @param path
	 *            The new file path.
	 */
	public static void setFilePath(Resource resource, String path) {
		MEditingDomain.INSTANCE.setResourceFileName(resource, path);
	}

	/**
	 * Finds a resource given its path.
	 * 
	 * @param path
	 *            The resource path.
	 * @return The found resource.
	 */
	public static Resource findResource(String path) {
		return MEditingDomain.INSTANCE.findResource(path);
	}

	/**
	 * Sets the path of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @param path
	 *            The new file path.
	 * @param options
	 *            The file path options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public static void setFilePath(Resource resource, String path, int options) {
		MEditingDomain.INSTANCE.setResourceFileName(resource, path, options);
	}

	/**
	 * Finds a resource given its path.
	 * 
	 * @param path
	 *            The resource path.
	 * @param options
	 *            The file path options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @return The found resource.
	 */
	public static Resource findResource(String path, int options) {
		return MEditingDomain.INSTANCE.findResource(path, options);
	}

	/**
	 * Gets the first root object in the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The first object.
	 */
	public static EObject getFirstRoot(Resource resource) {

		if (resource.isLoaded()) {

			List contents = resource.getContents();

			if (!contents.isEmpty())
				return (EObject) contents.get(0);
		}

		return null;
	}

	/**
	 * Creates a resource and object of type rootEClass that becomes the root of
	 * the resource.
	 * 
	 * @param path
	 *            Optional path to be assigned to the resource.
	 * @param rootEClass
	 *            The root object's type (optional)
	 * @param options
	 *            The create options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @return The new resource.
	 */
	public static Resource create(String path, EClass rootEClass, int options) {
		return MEditingDomain.INSTANCE
			.createResource(path, rootEClass, options);
	}

	/**
	 * Loads a resource from a given file.
	 * 
	 * @param path
	 *            The resource's file path.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @return The loaded resource.
	 */
	public static Resource load(String path, int options) {
		return MEditingDomain.INSTANCE.loadResource(path, options);
	}

	/**
	 * Loads a resource with a given file path from the given input stream using
	 * the given load options.
	 * 
	 * @param path
	 *            The resource's file path.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @param inputStream
	 *            The input stream from which to load the resource's contents.
	 * @return The loaded resource.
	 */
	public static Resource load(String path, int options,
			InputStream inputStream) {
		return MEditingDomain.INSTANCE.loadResource(path, options, inputStream);
	}

	/**
	 * Loads an unloaded resource.
	 * 
	 * @param resource
	 *            The resource to load.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public static void load(Resource resource, int options) {
		MEditingDomain.INSTANCE.loadResource(resource, options);
	}

	/**
	 * Loads an unloaded resource from the given input stream using the given
	 * load options.
	 * 
	 * @param resource
	 *            The resource to load.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @param inputStream
	 *            The input stream from which to load the resource's contents.
	 */
	public static void load(Resource resource, int options,
			InputStream inputStream) {
		MEditingDomain.INSTANCE.loadResource(resource, options, inputStream);
	}

	/**
	 * Unloads a loaded resource.
	 * 
	 * @param resource
	 *            The resource to unload.
	 * @param options
	 *            The unload options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public static void unload(Resource resource, int options) {
		MEditingDomain.INSTANCE.unloadResource(resource, options);
	}

	/**
	 * Saves a resource.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param options
	 *            The save options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public static void save(Resource resource, int options) {
		MEditingDomain.INSTANCE.saveResource(resource, options);
	}

	/**
	 * Sets the path of a resource then saves it.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param path
	 *            The new file path.
	 * @param options
	 *            The save options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public static void saveAs(Resource resource, String path, int options) {
		MEditingDomain.INSTANCE.saveResourceAs(resource, path, options);
	}

	/**
	 * Creates a resource and object of type rootEClass that becomes the root of
	 * the resource.
	 * 
	 * @param path
	 *            Optional path to be assigned to the resource.
	 * @param eClass
	 *            The root object's type (optional)
	 * 
	 * @return The new resource.
	 */
	public static Resource create(String path, EClass eClass) {
		return MEditingDomain.INSTANCE.createResource(path, eClass);
	}

	/**
	 * Loads a resource from a given file.
	 * 
	 * @param path
	 *            The resource's file path.
	 * @return The loaded resource.
	 */
	public static Resource load(String path) {
		return MEditingDomain.INSTANCE.loadResource(path);
	}

	/**
	 * Loads an unloaded resource.
	 * 
	 * @param resource
	 *            The resource to load.
	 */
	public static void load(Resource resource) {
		MEditingDomain.INSTANCE.loadResource(resource);
	}

	/**
	 * Unloads a loaded resource.
	 * 
	 * @param resource
	 *            The resource to unload.
	 */
	public static void unload(Resource resource) {
		MEditingDomain.INSTANCE.unloadResource(resource);
	}

	/**
	 * Saves a resource.
	 * 
	 * @param resource
	 *            The resource to save.
	 */
	public static void save(Resource resource) {
		MEditingDomain.INSTANCE.saveResource(resource);
	}

	/**
	 * Sets the path of a resource then saves it.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param path
	 *            The new file path.
	 */
	public static void saveAs(Resource resource, String path) {
		MEditingDomain.INSTANCE.saveResourceAs(resource, path);
	}

	/**
	 * Finds an object in a resource given its ID.
	 * 
	 * @param resource
	 *            The resource.
	 * @param id
	 *            The ID.
	 * @return The found object.
	 */
	public static EObject findObject(Resource resource, String id) {

		if (resource.isLoaded())
			return resource.getEObject(id);
		else
			return null;
	}

	/**
	 * Checks if a resource is modifiable.
	 * 
	 * @param resource
	 *            The resource.
	 * @return True if resource is modifiable.
	 */
	public static boolean isModifiable(Resource resource) {

		if (resource instanceof MResource)
			return ((MResource) resource).getHelper().isModifiable(resource);

		return MSLUtil.isModifiable(resource);
	}

	/**
	 * Gets the resource’s type which could be either: MODELING: the resource is
	 * a modeling resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The resource's type.
	 */
	public static MObjectType getType(Resource resource) {

		if (resource instanceof MResource)
			return ((MResource) resource).getHelper().getType();

		return MObjectType.MODELING;
	}

	/**
	 * Gets the imports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The imports of the resource.
	 */
	public static Collection getImports(Resource resource) {
		return MEditingDomain.INSTANCE.getImports(resource);
	}

	/**
	 * Gets the exports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The exports of the resource.
	 */
	public static Collection getExports(Resource resource) {
		return MEditingDomain.INSTANCE.getExports(resource);
	}

	/**
	 * Gets all imports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The imports of the resource.
	 */
	public static Collection getAllImports(Resource resource) {
		return MEditingDomain.INSTANCE.getAllImports(resource);
	}

	/**
	 * Gets all exports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The exports of the resource.
	 */
	public static Collection getAllExports(Resource resource) {
		return MEditingDomain.INSTANCE.getAllExports(resource);
	}

	/**
	 * Sets the value of a path variable.
	 * 
	 * @param var
	 *            The variable name.
	 * @param val
	 *            The variable value (a URI, not just a file path).
	 */
	public static void setPathVariable(String var, String val) {
		MEditingDomain.INSTANCE.setPathVariable(var, val);
	}

	/**
	 * Removes a path variable.
	 * 
	 * @param var
	 *            The variable name.
	 */
	public static void removePathVariable(String var) {
		MEditingDomain.INSTANCE.removePathVariable(var);
	}

	/**
	 * Gets the value of a path variable.
	 * 
	 * @param var
	 *            The variable name.
	 * @return The variable value (a URI, not just a file path), or
	 *    an empty string if the variable is undefined.
	 */
	public static String getPathVariable(String var) {
		return MEditingDomain.INSTANCE.getPathVariable(var);
	}

	/**
	 * Gets the path of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The file path of the resource.
	 */
	public static String getFileString(Resource resource) {
		return getFilePath(resource);
	}

	/**
	 * Return the file in the workspace for a resource.
	 * 
	 * @param resource
	 *            the resource.
	 * @return the file in the workspace for a resource.
	 */
	public static IFile getFile(Resource resource) {

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		String fileString = getFilePath(resource);

		if (fileString != null)
			return workspaceRoot.getFileForLocation(new Path(fileString));

		return null;
	}

	/**
	 * Creates re-gui-ided copies of the resources provided in the map
	 * 
	 * @param resource2URI
	 *            a map with the resources to copy as keys and their new URIs as
	 *            values
	 * @throws IOException
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#copyResources(java.util.Map)
	 */
	public static void copyResources(Map resource2URI)
		throws IOException {
		MEditingDomain.INSTANCE.copyResources(resource2URI);
	}

	private ResourceUtil() {
		// private
	}
}