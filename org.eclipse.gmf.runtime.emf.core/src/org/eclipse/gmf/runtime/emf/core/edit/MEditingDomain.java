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

package org.eclipse.gmf.runtime.emf.core.edit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;

/**
 * <p>
 * This class implements an EMF <code>EditingDomain</code> and adds some MSL
 * specific features like undo intervals, adhoc undo/redo, some resource
 * utilities and some eventing features.
 * </p>
 * <p>
 * This is the entry point to the MSL API; one would first call
 * <code>createNewDomain()</code> to create a new instance of the editing
 * domain with its own undo stack and resource set. The modeling tools use the
 * single instance of the editing domain: <code>INSTANCE</code>.
 * </p>
 * 
 * @author rafikj
 */
public abstract class MEditingDomain
	extends org.eclipse.gmf.runtime.emf.core.EditingDomain
	implements EditingDomain {

	/**
	 * A singleton instance of the editing domain.
	 */
	public final static MEditingDomain INSTANCE = new MSLEditingDomain();

	/**
	 * Creates a new MSL editing domain.
	 * 
	 * @return The new editing domain.
	 */
	public static MEditingDomain createNewDomain() {
		return new MSLEditingDomain();
	}

	/**
	 * Creates an EObject given its EClass. The object is detached.
	 * 
	 * @param eClass
	 *            The <code>EClass</code>.
	 * @return The new <code>EObject</code>.
	 */
	public abstract EObject create(EClass eClass);

	/**
	 * Gets the file name of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The file name of the resource.
	 */
	public abstract String getResourceFileName(Resource resource);

	/**
	 * Sets the file name of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @param fileNameURI
	 *            The new file path.
	 */
	public abstract void setResourceFileName(Resource resource,
			String fileNameURI);

	/**
	 * Sets the file name of the resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @param fileNameURI
	 *            The new file path.
	 * @param options
	 *            The file name options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public abstract void setResourceFileName(Resource resource,
			String fileNameURI, int options);

	/**
	 * Finds a resource given its path.
	 * 
	 * @param fileNameURI
	 *            The resource path.
	 * @param options
	 *            the file name options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @return The found resource.
	 */
	public abstract Resource findResource(String fileNameURI, int options);

	/**
	 * Finds a resource given its path.
	 * 
	 * @param fileNameURI
	 *            The resource path.
	 * @return The found resource.
	 */
	public abstract Resource findResource(String fileNameURI);

	/**
	 * Converts a URI to use a pathmap or platform URI.
	 * 
	 * @param uri
	 *            The URI to convert.
	 * @return The converted URI.
	 */
	public abstract URI convertURI(URI uri);

	/**
	 * Creates a resource and object of type rootEClass that becomes the root of
	 * the resource.
	 * 
	 * @param fileNameURI
	 *            Optional file name to be assigned to the resource.
	 * @return The new resource.
	 */
	public abstract Resource createResource(String fileNameURI);

	/**
	 * Creates a resource and object of type rootEClass that becomes the root of
	 * the resource.
	 * 
	 * @param fileNameURI
	 *            Optional file name to be assigned to the resource.
	 * @param options
	 *            The create options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @return The new resource.
	 */
	public abstract Resource createResource(String fileNameURI, int options);

	/**
	 * Creates a resource and object of type rootEClass that becomes the root of
	 * the resource.
	 * 
	 * @param fileNameURI
	 *            Optional file name to be assigned to the resource.
	 * @param rootEClass
	 *            The root object's type (optional).
	 * @return The new resource.
	 */
	public abstract Resource createResource(String fileNameURI,
			EClass rootEClass);

	/**
	 * Creates a resource and object of type rootEClass that becomes the root of
	 * the resource.
	 * 
	 * @param fileNameURI
	 *            Optional file name to be assigned to the resource.
	 * @param rootEClass
	 *            The root object's type (optional)
	 * @return The new resource.
	 */
	public abstract Resource createResource(String fileNameURI,
			EClass rootEClass, int options);

	/**
	 * Loads a resource from a given file.
	 * 
	 * @param fileNameURI
	 *            The resource's file path.
	 * @return The loaded resource.
	 */
	public abstract Resource loadResource(String fileNameURI);

	/**
	 * Loads a resource from a given file.
	 * 
	 * @param fileNameURI
	 *            The resource's file path.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @return The loaded resource.
	 */
	public abstract Resource loadResource(String fileNameURI, int options);

	/**
	 * Loads a resource with a given file path from the given input stream using
	 * the given load options.
	 * 
	 * @param fileNameURI
	 *            The resource's file path.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 * @param inputStream
	 *            The input stream from which to load the resource's contents.
	 * @return The loaded resource.
	 */
	public abstract Resource loadResource(String fileNameURI, int options,
			InputStream inputStream);

	/**
	 * Loads an unloaded resource.
	 * 
	 * @param resource
	 *            The resource to load.
	 */
	public abstract void loadResource(Resource resource);

	/**
	 * Loads an unloaded resource.
	 * 
	 * @param resource
	 *            The resource to load.
	 * @param options
	 *            The load options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public abstract void loadResource(Resource resource, int options);

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
	public abstract void loadResource(Resource resource, int options,
			InputStream inputStream);

	/**
	 * Unloads a loaded resource.
	 * 
	 * @param resource
	 *            The resource to unload.
	 */
	public abstract void unloadResource(Resource resource);

	/**
	 * Unloads a loaded resource.
	 * 
	 * @param resource
	 *            The resource to unload.
	 * @param options
	 *            The unload options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public abstract void unloadResource(Resource resource, int options);

	/**
	 * Saves a resource.
	 * 
	 * @param resource
	 *            The resource to save.
	 */
	public abstract void saveResource(Resource resource);

	/**
	 * Saves a resource.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param options
	 *            The save options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public abstract void saveResource(Resource resource, int options);

	/**
	 * Sets the path of a resource then saves it.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param fileNameURI
	 *            The new file path.
	 */
	public abstract void saveResourceAs(Resource resource, String fileNameURI);

	/**
	 * Sets the path of a resource then saves it.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param fileNameURI
	 *            The new file path.
	 * @param options
	 *            The save options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public abstract void saveResourceAs(Resource resource, String fileNameURI,
			int options);

	/**
	 * Queries whether a <code>resource</code> is a logical resource.
	 * This can be considered as a foreign method for the {@link Resource} API.
	 * 
	 * @param resource a resource
	 * @return whether the <code>resource</code> is a logical resource
	 * 
	 * @see ILogicalResource
	 */
	public abstract boolean isLogicalResource(Resource resource);

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
	public abstract ILogicalResource asLogicalResource(Resource resource);

	/**
	 * Opens an undo interval. An undo interval must be open before starting a
	 * write action.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(Runnable)} method, instead.
	 */
	public abstract void openUndoInterval();

	/**
	 * Opens an undo interval. An undo interval must be open before starting a
	 * write action.
	 * 
	 * @param label
	 *            The label.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(String, Runnable)} method, instead.
	 */
	public abstract void openUndoInterval(String label);

	/**
	 * Opens an undo interval. An undo interval must be open before starting a
	 * write action.
	 * 
	 * @param label
	 *            The label.
	 * @param description
	 *            The description.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(String, String, Runnable)} method, instead.
	 */
	public abstract void openUndoInterval(String label, String description);

	/**
	 * Closes the currently open undo interval and returns a reference to the
	 * closed interval if interval is not empty. If interval is empty returns
	 * null;
	 * 
	 * @return The undo interval or null if interval is empty.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(Runnable)} method or
	 *    a variant, instead.
	 */
	public abstract MUndoInterval closeUndoInterval();

	/**
	 * Can the current interval be undone?
	 * 
	 * @return True if current interval can be undone.
	 */
	public abstract boolean canUndoCurrentInterval();

	/**
	 * Can the current interval be redone?
	 * 
	 * @return True if current interval can be redone.
	 */
	public abstract boolean canRedoCurrentInterval();

	/**
	 * Sets can the current interval be undone.
	 */
	public abstract void setCanUndoCurrentInterval(boolean canUndo);

	/**
	 * Sets can the current interval be redone.
	 */
	public abstract void setCanRedoCurrentInterval(boolean canRedo);

	/**
	 * Runs the runnable instance in an undo interval and will take care of
	 * opening and closing an undo interval.
	 * 
	 * @param runnable
	 *            The runnable.
	 * @return The undo interval.
	 */
	public abstract MUndoInterval runInUndoInterval(Runnable runnable);

	/**
	 * Runs the runnable instance in an undo interval and will take care of
	 * opening and closing an undo interval.
	 * 
	 * @param label
	 *            The label.
	 * @param runnable
	 *            The runnable.
	 * @return The undo interval.
	 */
	public abstract MUndoInterval runInUndoInterval(String label,
			Runnable runnable);

	/**
	 * Runs the runnable instance in an undo interval and will take care of
	 * opening and closing an undo interval.
	 * 
	 * @param label
	 *            The label.
	 * @param description
	 *            The description.
	 * @param runnable
	 *            The runnable.
	 * @return The undo interval.
	 */
	public abstract MUndoInterval runInUndoInterval(String label,
			String description, Runnable runnable);

	/**
	 * Checks if there is an open undo interval.
	 * 
	 * @return True if an undo interval is open, false otherwise.
	 */
	public abstract boolean isUndoIntervalOpen();
	
	/**
	 * Yields for other read actions on other threads. Only the actions with
	 * read actions open (NO WRITE) can yield. This is a blocking call.
	 */
	public abstract void yieldForReads();

	/**
	 * Starts a read action. Read operations are required for reading models.
	 * 
	 * @deprecated Use the {@link #runAsRead(MRunnable)} method, instead.
	 */
	public abstract void startRead();

	/**
	 * Starts a write action. Write operations are required for modifying
	 * models.
	 * 
	 * @deprecated Use the {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public abstract void startWrite();

	/**
	 * Starts an unchecked action. Unchecked operations are required for
	 * modifying models outside of any undo interval. These should be used only
	 * in extreme situations for making modifications outside of undo intervals.
	 * 
	 * @deprecated Use the {@link #runAsUnchecked(MRunnable)} method, instead.
	 */
	public abstract void startUnchecked();

	/**
	 * Completes the currently open action and adds it to the currently open
	 * undo interval if applicable.
	 * 
	 * @deprecated Use the {@link #runAsRead(MRunnable)} or
	 *     {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public abstract void complete();

	/**
	 * <p>
	 * Completes the currently open action and adds it to the currently open
	 * undo interval if applicable.
	 * </p>
	 * <p>
	 * The resulting status indicates warnings or informational messages from
	 * validation. Note that, if errors are reported by validation, then the
	 * action is automatically abandoned and an
	 * {@link MSLActionAbandonedException}is thrown instead of a status being
	 * returned. Thus, clients are required to handle the situation in which the
	 * action is abandoned because the model changes that they expected to have
	 * applied will not have been effected.
	 * </p>
	 * 
	 * @return a status object containing any warnings or informational messages
	 *         produced by validation. This may be a multi-status if there are
	 *         multiple validation messages
	 * 
	 * @throws MSLActionAbandonedException
	 *             if live constraints find errors
	 * 
	 * @deprecated Use the {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public abstract IStatus completeAndValidate()
		throws MSLActionAbandonedException;

	/**
	 * Abandons and discards the currently open action.
	 * 
	 * @deprecated Use the {@link MRunnable#abandon()} method, instead.
	 */
	public abstract void abandon();

	/**
	 * Runs the runnable instance in a read action and will take care of
	 * starting and completing the read action.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public abstract Object runAsRead(MRunnable runnable);

	/**
	 * <p>
	 * Runs the runnable instance in a write action and will take care of
	 * starting and completing the write action.
	 * </p>
	 * <p>
	 * Note that if this method does not need to start a write action (because
	 * one is already in progress), then it will not attempt to complete it,
	 * either. In such cases, the <code>runnable</code>'s status will be
	 * {@link MRunnable#setStatus(IStatus) set}to an OK status because no
	 * validation is performed.
	 * </p>
	 * <p>
	 * At any point during the execution of the <code>runnable</code>, it may
	 * be {@linkplain MRunnable#abandon() abandoned}. In this case, its status
	 * will be set to a {@link IStatus#CANCEL}value and the write action will
	 * be abandoned when the <code>runnable</code> returns.
	 * </p>
	 * 
	 * @param runnable
	 *            The runnable. The runnable's status is assigned according to
	 *            the results of live validation
	 * 
	 * @throws MSLActionAbandonedException
	 *             if the action is abandoned because live validation detects
	 *             errors
	 * 
	 * @see MRunnable#abandon()
	 */
	public abstract Object runAsWrite(MRunnable runnable)
		throws MSLActionAbandonedException;

	/**
	 * Runs the runnable instance in an unchecked action and will take care of
	 * starting and completing the unchecked action.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public abstract Object runAsUnchecked(MRunnable runnable);

	/**
	 * Runs the runnable instance without sending events.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public abstract Object runSilent(MRunnable runnable);

	/**
	 * Runs the runnable instance without semantic procedures.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public abstract Object runWithNoSemProcs(MRunnable runnable);

	/**
	 * Runs the runnable instance without validation.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public abstract Object runUnvalidated(MRunnable runnable);

	/**
	 * Runs the runnable instance with options. This method could be used to
	 * combine the effects of runSilent, runUnchecked, runUnvalidated and
	 * runWithNoSemProcs.
	 * 
	 * @see MRunOption
	 * 
	 * @param runnable
	 *            The runnable.
	 * @param options
	 *            The run options. This is a bit mask of values from
	 *            <code>MResourceOption</code>.
	 */
	public abstract Object runWithOptions(MRunnable runnable, int options);

	/**
	 * Checks if one can read, i.e., a read or write action is in progress.
	 * 
	 * @return True if a read or write action is in progress.
	 */
	public abstract boolean canRead();

	/**
	 * Checks if one can write, i.e., a write or unchecked action is in
	 * progress.
	 * 
	 * @return True if a write or unchecked action is in progress.
	 */
	public abstract boolean canWrite();

	/**
	 * Checks if a write action is in progress.
	 * 
	 * @return True if a write action is in progress.
	 */
	public abstract boolean isWriteInProgress();

	/**
	 * Checks if an unchecked action is in progress.
	 * 
	 * @return True if an unchecked action is in progress.
	 */
	public abstract boolean isUncheckedInProgress();

	/**
	 * Checks if given notification is caused by undo.
	 * 
	 * @param notification
	 *            The notification.
	 * @return True if notification is caused by undo.
	 */
	public abstract boolean isUndoNotification(Notification notification);

	/**
	 * Checks if given notification is caused by redo.
	 * 
	 * @param notification
	 *            The notification.
	 * @return True if notification is caused by redo.
	 */
	public abstract boolean isRedoNotification(Notification notification);

	/**
	 * Sends notification to registered listeners.
	 * 
	 * @param notification
	 *            The notification.
	 */
	public abstract void sendNotification(Notification notification);

	/**
	 * Sends notification to registered listeners.
	 * 
	 * @param notifier
	 *            The notifier.
	 * @param eventType
	 *            The event type.
	 */
	public abstract void sendNotification(Object notifier, int eventType);

	/**
	 * returns a the map of Schema to Location passed file.
	 * 
	 * @param uri
	 *            the file uri
	 * @return a collection of required schema's location
	 */
	public abstract Map getSchemaToLocationMap(URI uri)
		throws IOException;

	/**
	 * returns a the map of Schema to Location passed file.
	 * 
	 * @param inputStream
	 *            input stream to use
	 * @return a collection of required schema's location
	 */
	public abstract Map getSchemaToLocationMap(InputStream inputStream)
		throws IOException;

	/**
	 * Sets the value of a path variable.
	 * 
	 * @param var
	 *            The variable name.
	 * @param val
	 *            The variable value (a URI, not just a file path).
	 */
	public abstract void setPathVariable(String var, String val);

	/**
	 * Removes a path variable.
	 * 
	 * @param var
	 *            The variable name.
	 */
	public abstract void removePathVariable(String var);

	/**
	 * Gets the value of a path variable.
	 * 
	 * @param var
	 *            The variable name.
	 * @return The variable value (a URI, not just a file path),
	 *    or an empty string if the variable is undefined.
	 */
	public abstract String getPathVariable(String var);

	/**
	 * Gets the imports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The imports of the resource.
	 */
	public abstract Collection getImports(Resource resource);

	/**
	 * Gets the exports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The exports of the resource.
	 */
	public abstract Collection getExports(Resource resource);

	/**
	 * Gets all imports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The imports of the resource.
	 */
	public abstract Collection getAllImports(Resource resource);

	/**
	 * Gets all exports of a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The exports of the resource.
	 */
	public abstract Collection getAllExports(Resource resource);

	/**
	 * Gets all resource sets.
	 * 
	 * @return The set of all resource sets.
	 */
	public static Set getResourceSets() {
		return MSLEditingDomain.getResourceSets();
	}

	/**
	 * Gets the editing domain of a given resource set.
	 * 
	 * @param resourceSet
	 *            The resource set.
	 * @return The editing domain.
	 */
	public static MEditingDomain getEditingDomain(ResourceSet resourceSet) {
		return MSLEditingDomain.getEditingDomain(resourceSet);
	}

	/**
	 * Gets the editing domain of a given resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The editing domain.
	 */
	public static MEditingDomain getEditingDomain(Resource resource) {
		return MSLEditingDomain.getEditingDomain(resource);
	}

	/**
	 * Creates re-gui-ided copies of the resources provided in the map. The
	 * resources must be loaded resources
	 * 
	 * @param resource2URI
	 *            a map with the resources to copy as keys and their new URIs as
	 *            values
	 * @throws IOException
	 */
	public abstract void copyResources(Map resource2URI)
		throws IOException;
}