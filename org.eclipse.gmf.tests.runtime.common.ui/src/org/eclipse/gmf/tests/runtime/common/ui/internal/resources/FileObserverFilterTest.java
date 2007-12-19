/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.internal.resources;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilter;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilterType;

/**
 * Tests for FileObserverFilter.
 * 
 * @author Anthony Hunter
 */
public class FileObserverFilterTest
	extends TestCase {

	protected class File
		implements IFile {

		private IPath path;

		public File(IPath path) {
			this.path = path;
		}

		public void setCharset(String newCharset, IProgressMonitor monitor)
			throws CoreException {
			// blank
		}

		public void appendContents(InputStream source, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void appendContents(InputStream source, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void create(InputStream source, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void create(InputStream source, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void createLink(IPath localLocation, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void delete(boolean force, boolean keepHistory,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public InputStream getContents()
			throws CoreException {
			return null;
		}

		public InputStream getContents(boolean force)
			throws CoreException {
			return null;
		}

		public IPath getFullPath() {
			return path;
		}

		public IFileState[] getHistory(IProgressMonitor monitor)
			throws CoreException {
			return null;
		}

		public String getName() {
			return null;
		}

		public boolean isReadOnly() {
			return false;
		}

		public void move(IPath destination, boolean force, boolean keepHistory,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void setContents(IFileState source, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void setContents(IFileState source, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void setContents(InputStream source, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void setContents(InputStream source, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void accept(IResourceProxyVisitor visitor, int memberFlags)
			throws CoreException {/* Empty block */
		}

		public void accept(IResourceVisitor visitor, int depth,
				boolean includePhantoms)
			throws CoreException {/* Empty block */
		}

		public void accept(IResourceVisitor visitor, int depth, int memberFlags)
			throws CoreException {/* Empty block */
		}

		public void accept(IResourceVisitor visitor)
			throws CoreException {
			/* Empty block */
		}

		public void clearHistory(IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void copy(IPath destination, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void copy(IPath destination, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void copy(IProjectDescription description, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void copy(IProjectDescription description, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public IMarker createMarker(String type)
			throws CoreException {
			return null;
		}

		public void delete(boolean force, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void delete(int updateFlags, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void deleteMarkers(String type, boolean includeSubtypes,
				int depth)
			throws CoreException {/* Empty block */
		}

		public boolean exists() {
			return false;
		}

		public IMarker findMarker(long id)
			throws CoreException {
			return null;
		}

		public IMarker[] findMarkers(String type, boolean includeSubtypes,
				int depth)
			throws CoreException {
			return null;
		}

		public String getFileExtension() {
			return null;
		}

		public IPath getLocation() {
			return path;
		}

		public IMarker getMarker(long id) {
			return null;
		}

		public long getModificationStamp() {
			return 0;
		}

		public IContainer getParent() {
			return null;
		}

		public String getPersistentProperty(QualifiedName key)
			throws CoreException {
			return null;
		}

		public IProject getProject() {
			return null;
		}

		public IPath getProjectRelativePath() {
			return null;
		}

		public IPath getRawLocation() {
			return null;
		}

		public Object getSessionProperty(QualifiedName key)
			throws CoreException {
			return null;
		}

		public int getType() {
			return 0;
		}

		public IWorkspace getWorkspace() {
			return null;
		}

		public boolean isAccessible() {
			return false;
		}

		public boolean isDerived() {
			return false;
		}

		public boolean isLinked() {
			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isLocal(int)
		 * @deprecated
		 */
		public boolean isLocal(int depth) {
			return false;
		}

		public boolean isPhantom() {
			return false;
		}

		public boolean isSynchronized(int depth) {
			return false;
		}

		public boolean isTeamPrivateMember() {
			return false;
		}

		public void move(IPath destination, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void move(IPath destination, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void move(IProjectDescription description, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void move(IProjectDescription description, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void refreshLocal(int depth, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void setDerived(boolean isDerived)
			throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setLocal(boolean, int, org.eclipse.core.runtime.IProgressMonitor)
		 * @deprecated
		 */
		public void setLocal(boolean flag, int depth, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		public void setPersistentProperty(QualifiedName key, String value)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setReadOnly(boolean)
		 * @deprecated
		 */
		public void setReadOnly(boolean readOnly) {/* Empty block */
		}

		public void setSessionProperty(QualifiedName key, Object value)
			throws CoreException {/* Empty block */
		}

		public void setTeamPrivateMember(boolean isTeamPrivate)
			throws CoreException {/* Empty block */
		}

		public void touch(IProgressMonitor monitor)
			throws CoreException {
			/* Empty block */
		}

		@SuppressWarnings("unchecked")
		public Object getAdapter(Class adapter) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getLocalTimeStamp()
		 */
		public long getLocalTimeStamp() {
			return 0;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setLocalTimeStamp(long)
		 */
		public long setLocalTimeStamp(long value)
			throws CoreException {
			return 0;
		}

		/**
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.runtime.jobs.ISchedulingRule)
		 */
		public boolean contains(ISchedulingRule rule) {
			return false;
		}

		/**
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.runtime.jobs.ISchedulingRule)
		 */
		public boolean isConflicting(ISchedulingRule rule) {
			return false;
		}

		public String getCharset()
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFile#setCharset(java.lang.String)
		 * @deprecated
		 */
		public void setCharset(String newCharset)
			throws CoreException {
			/* Empty block */}

		/**
		 * @see org.eclipse.core.resources.IFile#getEncoding()
		 * @deprecated
		 */
		public int getEncoding()
			throws CoreException {
			// Can't get rid of warning as depricated abstract method
			// must be implemented
			return 0;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.resources.IFile#getCharset(boolean)
		 */
		public String getCharset(boolean checkImplicit)
			throws CoreException {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.resources.IFile#getContentDescription()
		 */
		public IContentDescription getContentDescription()
			throws CoreException {
			return null;
		}

		public String getCharsetFor(Reader reader) throws CoreException {
			return null;
		}

		public ResourceAttributes getResourceAttributes() {
			return null;
		}

		public void revertModificationStamp(long value) throws CoreException {
			/*Empty block*/
		}

		public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
			/*Empty block*/
		}

		public URI getLocationURI() {
			return null;
		}

		public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			/*Empty block*/
		}

		public URI getRawLocationURI() {
			return null;
		}

		public boolean isLinked(int options) {
			return false;
		}

		public IResourceProxy createProxy() {
			return null;
		}

        public int findMaxProblemSeverity(String type, boolean includeSubtypes,
                int depth)
            throws CoreException {
            return 0;
        }
        
        /*
         * @see org.eclipse.core.resources.IResource#isHidden()
         */
        public boolean isHidden() {
			return false;
		}

        /*
         * @see org.eclipse.core.resources.IResource#setHidden(boolean)
         */
		public void setHidden(boolean isHidden) throws CoreException {
			/* not implemented */
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isDerived(int)
		 */
		public boolean isDerived(int arg0) {
			return false;
		}
	}

	protected class Folder
		implements IFolder {

		private IPath path;

		public Folder(IPath path) {
			this.path = path;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.resources.IContainer#setDefaultCharset(java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void setDefaultCharset(String charset, IProgressMonitor monitor)
			throws CoreException {
			// do nothing
		}
		/**
		 * @see org.eclipse.core.resources.IFolder#create(boolean, boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void create(boolean force, boolean local,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#create(int, boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void create(int updateFlags, boolean local,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#createLink(org.eclipse.core.runtime.IPath,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void createLink(IPath localLocation, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#delete(boolean, boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void delete(boolean force, boolean keepHistory,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#getFile(java.lang.String)
		 */
		public IFile getFile(String name) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#getFolder(java.lang.String)
		 */
		public IFolder getFolder(String name) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#move(org.eclipse.core.runtime.IPath,
		 *      boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void move(IPath destination, boolean force, boolean keepHistory,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#exists(org.eclipse.core.runtime.IPath)
		 */
		public boolean exists(IPath pth) {
			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findDeletedMembersWithHistory(int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public IFile[] findDeletedMembersWithHistory(int depth,
				IProgressMonitor monitor)
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(org.eclipse.core.runtime.IPath,
		 *      boolean)
		 */
		public IResource findMember(IPath pth, boolean includePhantoms) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(org.eclipse.core.runtime.IPath)
		 */
		public IResource findMember(IPath pth) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(java.lang.String,
		 *      boolean)
		 */
		public IResource findMember(String name, boolean includePhantoms) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(java.lang.String)
		 */
		public IResource findMember(String name) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#getFile(org.eclipse.core.runtime.IPath)
		 */
		public IFile getFile(IPath pth) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#getFolder(org.eclipse.core.runtime.IPath)
		 */
		public IFolder getFolder(IPath pth) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#members()
		 */
		public IResource[] members()
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#members(boolean)
		 */
		public IResource[] members(boolean includePhantoms)
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#members(int)
		 */
		public IResource[] members(int memberFlags)
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
		 */
		@SuppressWarnings("unchecked")
		public Object getAdapter(Class adapter) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceProxyVisitor,
		 *      int)
		 */
		public void accept(IResourceProxyVisitor visitor, int memberFlags)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceVisitor,
		 *      int, boolean)
		 */
		public void accept(IResourceVisitor visitor, int depth,
				boolean includePhantoms)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceVisitor,
		 *      int, int)
		 */
		public void accept(IResourceVisitor visitor, int depth, int memberFlags)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceVisitor)
		 */
		public void accept(IResourceVisitor visitor)
			throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#clearHistory(org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void clearHistory(IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.runtime.IPath,
		 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void copy(IPath destination, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.runtime.IPath,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void copy(IPath destination, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.resources.IProjectDescription,
		 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void copy(IProjectDescription description, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.resources.IProjectDescription,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void copy(IProjectDescription description, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#createMarker(java.lang.String)
		 */
		public IMarker createMarker(String type)
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#delete(boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void delete(boolean force, IProgressMonitor monitor)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#delete(int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void delete(int updateFlags, IProgressMonitor monitor)
			throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#deleteMarkers(java.lang.String,
		 *      boolean, int)
		 */
		public void deleteMarkers(String type, boolean includeSubtypes,
				int depth)
			throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#exists()
		 */
		public boolean exists() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#findMarker(long)
		 */
		public IMarker findMarker(long id)
			throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#findMarkers(java.lang.String,
		 *      boolean, int)
		 */
		public IMarker[] findMarkers(String type, boolean includeSubtypes,
				int depth)
			throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getFileExtension()
		 */
		public String getFileExtension() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getFullPath()
		 */
		public IPath getFullPath() {
			return path;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getLocation()
		 */
		public IPath getLocation() {
			return path;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getMarker(long)
		 */
		public IMarker getMarker(long id) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getModificationStamp()
		 */
		public long getModificationStamp() {

			return 0;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getName()
		 */
		public String getName() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getParent()
		 */
		public IContainer getParent() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getPersistentProperty(org.eclipse.core.runtime.QualifiedName)
		 */
		public String getPersistentProperty(QualifiedName key)
			throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getProject()
		 */
		public IProject getProject() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getProjectRelativePath()
		 */
		public IPath getProjectRelativePath() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getRawLocation()
		 */
		public IPath getRawLocation() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getSessionProperty(org.eclipse.core.runtime.QualifiedName)
		 */
		public Object getSessionProperty(QualifiedName key)
			throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getType()
		 */
		public int getType() {

			return 0;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getWorkspace()
		 */
		public IWorkspace getWorkspace() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isAccessible()
		 */
		public boolean isAccessible() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isDerived()
		 */
		public boolean isDerived() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isLinked()
		 */
		public boolean isLinked() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isLocal(int)
		 * @deprecated
		 */
		public boolean isLocal(int depth) {
			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isPhantom()
		 */
		public boolean isPhantom() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isReadOnly()
		 * @deprecated
		 */
		public boolean isReadOnly() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isSynchronized(int)
		 */
		public boolean isSynchronized(int depth) {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isTeamPrivateMember()
		 */
		public boolean isTeamPrivateMember() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.runtime.IPath,
		 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void move(IPath destination, boolean force,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.runtime.IPath,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void move(IPath destination, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.resources.IProjectDescription,
		 *      boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void move(IProjectDescription description, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.resources.IProjectDescription,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void move(IProjectDescription description, int updateFlags,
				IProgressMonitor monitor)
			throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#refreshLocal(int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void refreshLocal(int depth, IProgressMonitor monitor)
			throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setDerived(boolean)
		 */
		public void setDerived(boolean isDerived)
			throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setLocal(boolean, int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 * @deprecated
		 */
		public void setLocal(boolean flag, int depth, IProgressMonitor monitor)
			throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setPersistentProperty(org.eclipse.core.runtime.QualifiedName,
		 *      java.lang.String)
		 */
		public void setPersistentProperty(QualifiedName key, String value)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setReadOnly(boolean)
		 * @deprecated
		 */
		public void setReadOnly(boolean readOnly) {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setSessionProperty(org.eclipse.core.runtime.QualifiedName,
		 *      java.lang.Object)
		 */
		public void setSessionProperty(QualifiedName key, Object value)
			throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setTeamPrivateMember(boolean)
		 */
		public void setTeamPrivateMember(boolean isTeamPrivate)
			throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#touch(org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void touch(IProgressMonitor monitor)
			throws CoreException {
			/* Empty block */
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResource#getLocalTimeStamp()
		 */
		public long getLocalTimeStamp() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResource#setLocalTimeStamp(long)
		 */
		public long setLocalTimeStamp(long value)
			throws CoreException {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.runtime.jobs.ISchedulingRule)
		 */
		public boolean contains(ISchedulingRule rule) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.runtime.jobs.ISchedulingRule)
		 */
		public boolean isConflicting(ISchedulingRule rule) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IContainer#getDefaultCharset()
		 */
		public String getDefaultCharset()
			throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#setDefaultCharset(java.lang.String)
		 * @deprecated
		 */
		public void setDefaultCharset(String charset)
			throws CoreException {
			/* Empty block */
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.resources.IContainer#getDefaultCharset(boolean)
		 */
		public String getDefaultCharset(boolean checkImplicit)
			throws CoreException {
			return null;
		}

		public ResourceAttributes getResourceAttributes() {
			return null;
		}

		public void revertModificationStamp(long value) throws CoreException {
			/*Empty block*/
		}

		public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
			/*Empty block*/
		}

		public URI getLocationURI() {
			return null;
		}

		public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			/*Empty block*/
		}

		public URI getRawLocationURI() {
			return null;
		}

		public boolean isLinked(int options) {
			return false;
		}

		public IResourceProxy createProxy() {
			return null;
		}

        public int findMaxProblemSeverity(String type, boolean includeSubtypes,
                int depth)
            throws CoreException {
            return 0;
        }
        
        /*
         * @see org.eclipse.core.resources.IResource#isHidden()
         */
        public boolean isHidden() {
			return false;
		}

        /*
         * @see org.eclipse.core.resources.IResource#setHidden(boolean)
         */
		public void setHidden(boolean isHidden) throws CoreException {
			/* not implemented */
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isDerived(int)
		 */
		public boolean isDerived(int arg0) {
			return false;
		}
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(FileObserverFilterTest.class);
	}

	public FileObserverFilterTest(String name) {
		super(name);
	}

	protected void setUp() {
		/* Empty block */
	}

	public void test_allFileObserverFilter() {
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.ALL);
		IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile file = new File(path);
		assertTrue(filter.matches(file));
	}

	public void test_extensionFileObserverFilter() {
		String[] extensions = {"txt"}; //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.EXTENSION, extensions);
		IPath txt = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile txtFile = new File(txt);
		assertTrue(filter.matches(txtFile));
		IPath mdx = new Path("c:\\test.mdx"); //$NON-NLS-1$
		IFile mdxFile = new File(mdx);
		assertFalse(filter.matches(mdxFile));
	}

	public void test_fileFileObserverFilter() {
		IPath txt = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile txtFile = new File(txt);
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.FILE, txtFile);
		assertTrue(filter.matches(txtFile));
		IPath mdx = new Path("c:\\test.mdx"); //$NON-NLS-1$
		IFile mdxFile = new File(mdx);
		assertFalse(filter.matches(mdxFile));
	}

	public void test_pathFileObserverFilter() {
		IPath path = new Path("c:\\test"); //$NON-NLS-1$
		IFolder folder = new Folder(path);
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.FOLDER, folder);
		IPath yes = new Path("c:\\test\\test.txt"); //$NON-NLS-1$
		IFile yesFile = new File(yes);
		assertTrue(filter.matches(yesFile));
		IPath no = new Path("c:\\other\\test.txt"); //$NON-NLS-1$
		IFile noFile = new File(no);
		assertFalse(filter.matches(noFile));
		assertFalse(filter.matches(folder));
	}
}