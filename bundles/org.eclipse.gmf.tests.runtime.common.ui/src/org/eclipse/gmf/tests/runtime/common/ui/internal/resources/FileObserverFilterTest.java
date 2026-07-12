/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.internal.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Map;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for FileObserverFilter.
 *
 * @author Anthony Hunter
 */
public class FileObserverFilterTest {

	protected class File implements IFile {

		private IPath path;

		public File(IPath path) {
			this.path = path;
		}

		@Override
		public void setCharset(String newCharset, IProgressMonitor monitor) throws CoreException {
			// blank
		}

		@Override
		public void appendContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void appendContents(InputStream source, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void create(InputStream source, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void create(InputStream source, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public InputStream getContents() throws CoreException {
			return null;
		}

		@Override
		public InputStream getContents(boolean force) throws CoreException {
			return null;
		}

		@Override
		public IPath getFullPath() {
			return path;
		}

		@Override
		public IFileState[] getHistory(IProgressMonitor monitor) throws CoreException {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public boolean isReadOnly() {
			return false;
		}

		@Override
		public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void setContents(IFileState source, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void setContents(IFileState source, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void setContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void setContents(InputStream source, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException {/* Empty block */
		}

		@Override
		public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms)
				throws CoreException {/* Empty block */
		}

		@Override
		public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException {/* Empty block */
		}

		@Override
		public void accept(IResourceVisitor visitor) throws CoreException {
			/* Empty block */
		}

		@Override
		public void clearHistory(IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		@Override
		public void copy(IPath destination, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void copy(IPath destination, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public IMarker createMarker(String type) throws CoreException {
			return null;
		}

		@Override
		public void delete(boolean force, IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		@Override
		public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		@Override
		public void deleteMarkers(String type, boolean includeSubtypes, int depth)
				throws CoreException {/* Empty block */
		}

		@Override
		public boolean exists() {
			return false;
		}

		@Override
		public IMarker findMarker(long id) throws CoreException {
			return null;
		}

		@Override
		public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
			return null;
		}

		@Override
		public String getFileExtension() {
			return null;
		}

		@Override
		public IPath getLocation() {
			return path;
		}

		@Override
		public IMarker getMarker(long id) {
			return null;
		}

		@Override
		public long getModificationStamp() {
			return 0;
		}

		@Override
		public IContainer getParent() {
			return null;
		}

		@Override
		public String getPersistentProperty(QualifiedName key) throws CoreException {
			return null;
		}

		@Override
		public IProject getProject() {
			return null;
		}

		@Override
		public IPath getProjectRelativePath() {
			return null;
		}

		@Override
		public IPath getRawLocation() {
			return null;
		}

		@Override
		public Object getSessionProperty(QualifiedName key) throws CoreException {
			return null;
		}

		@Override
		public int getType() {
			return 0;
		}

		@Override
		public IWorkspace getWorkspace() {
			return null;
		}

		@Override
		public boolean isAccessible() {
			return false;
		}

		@Override
		public boolean isDerived() {
			return false;
		}

		@Override
		public boolean isLinked() {
			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isLocal(int)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public boolean isLocal(int depth) {
			return false;
		}

		@Override
		public boolean isPhantom() {
			return false;
		}

		@Override
		public boolean isSynchronized(int depth) {
			return false;
		}

		@Override
		public boolean isTeamPrivateMember() {
			return false;
		}

		@Override
		public void move(IPath destination, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void move(IPath destination, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		@Override
		public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		@Override
		public void setDerived(boolean isDerived) throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setLocal(boolean, int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		@Override
		public void setPersistentProperty(QualifiedName key, String value) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setReadOnly(boolean)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void setReadOnly(boolean readOnly) {/* Empty block */
		}

		@Override
		public void setSessionProperty(QualifiedName key, Object value) throws CoreException {/* Empty block */
		}

		@Override
		public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException {/* Empty block */
		}

		@Override
		public void touch(IProgressMonitor monitor) throws CoreException {
			/* Empty block */
		}

		@Override
		public Object getAdapter(Class adapter) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getLocalTimeStamp()
		 */
		@Override
		public long getLocalTimeStamp() {
			return 0;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setLocalTimeStamp(long)
		 */
		@Override
		public long setLocalTimeStamp(long value) throws CoreException {
			return 0;
		}

		/**
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.runtime.jobs.ISchedulingRule)
		 */
		@Override
		public boolean contains(ISchedulingRule rule) {
			return false;
		}

		/**
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.runtime.jobs.ISchedulingRule)
		 */
		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			return false;
		}

		@Override
		public String getCharset() throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFile#setCharset(java.lang.String)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void setCharset(String newCharset) throws CoreException {
			/* Empty block */}

		/**
		 * @see org.eclipse.core.resources.IFile#getEncoding()
		 * @deprecated
		 */
		@Deprecated
		@Override
		public int getEncoding() throws CoreException {
			// Can't get rid of warning as depricated abstract method
			// must be implemented
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IFile#getCharset(boolean)
		 */
		@Override
		public String getCharset(boolean checkImplicit) throws CoreException {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IFile#getContentDescription()
		 */
		@Override
		public IContentDescription getContentDescription() throws CoreException {
			return null;
		}

		@Override
		public String getCharsetFor(Reader reader) throws CoreException {
			return null;
		}

		@Override
		public ResourceAttributes getResourceAttributes() {
			return null;
		}

		@Override
		public void revertModificationStamp(long value) throws CoreException {
			/* Empty block */
		}

		@Override
		public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
			/* Empty block */
		}

		@Override
		public URI getLocationURI() {
			return null;
		}

		@Override
		public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			/* Empty block */
		}

		@Override
		public URI getRawLocationURI() {
			return null;
		}

		@Override
		public boolean isLinked(int options) {
			return false;
		}

		@Override
		public IResourceProxy createProxy() {
			return null;
		}

		@Override
		public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException {
			return 0;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isHidden()
		 */
		@Override
		public boolean isHidden() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#setHidden(boolean)
		 */
		@Override
		public void setHidden(boolean isHidden) throws CoreException {
			/* not implemented */
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isDerived(int)
		 */
		@Override
		public boolean isDerived(int arg0) {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#getPersistentProperties()
		 */
		@Override
		public Map getPersistentProperties() throws CoreException {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#getSessionProperties()
		 */
		@Override
		public Map getSessionProperties() throws CoreException {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isHidden(int)
		 */
		@Override
		public boolean isHidden(int arg0) {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isTeamPrivateMember(int)
		 */
		@Override
		public boolean isTeamPrivateMember(int arg0) {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#hasFilters()
		 */
		public boolean hasFilters() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isGroup()
		 */
		public boolean isGroup() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#setDerived(boolean,
		 * org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException {
			//
		}

		/*
		 * @see org.eclipse.core.resources.IResource#setLinkLocation(java.net.URI, int,
		 * org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void setLinkLocation(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			//
		}

		/*
		 * @see
		 * org.eclipse.core.resources.IResource#setLinkLocation(org.eclipse.core.runtime
		 * .IPath, int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void setLinkLocation(IPath location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			//
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isVirtual()
		 */
		@Override
		public boolean isVirtual() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#getPathVariableManager()
		 */
		@Override
		public IPathVariableManager getPathVariableManager() {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isFiltered()
		 */
		public boolean isFiltered() {
			return false;
		}

		@Override
		public void accept(IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException {
			// TODO Auto-generated method stub

		}
	}

	protected class Folder implements IFolder {

		private IPath path;

		public Folder(IPath path) {
			this.path = path;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IContainer#setDefaultCharset(java.lang.String,
		 * org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void setDefaultCharset(String charset, IProgressMonitor monitor) throws CoreException {
			// do nothing
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#create(boolean, boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void create(boolean force, boolean local, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#create(int, boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void create(int updateFlags, boolean local, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#createLink(org.eclipse.core.runtime.IPath,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#delete(boolean, boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#getFile(java.lang.String)
		 */
		@Override
		public IFile getFile(String name) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#getFolder(java.lang.String)
		 */
		@Override
		public IFolder getFolder(String name) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFolder#move(org.eclipse.core.runtime.IPath,
		 *      boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#exists(org.eclipse.core.runtime.IPath)
		 */
		@Override
		public boolean exists(IPath pth) {
			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findDeletedMembersWithHistory(int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public IFile[] findDeletedMembersWithHistory(int depth, IProgressMonitor monitor) throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(org.eclipse.core.runtime.IPath,
		 *      boolean)
		 */
		@Override
		public IResource findMember(IPath pth, boolean includePhantoms) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(org.eclipse.core.runtime.IPath)
		 */
		@Override
		public IResource findMember(IPath pth) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(java.lang.String,
		 *      boolean)
		 */
		@Override
		public IResource findMember(String name, boolean includePhantoms) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#findMember(java.lang.String)
		 */
		@Override
		public IResource findMember(String name) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#getFile(org.eclipse.core.runtime.IPath)
		 */
		@Override
		public IFile getFile(IPath pth) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#getFolder(org.eclipse.core.runtime.IPath)
		 */
		@Override
		public IFolder getFolder(IPath pth) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#members()
		 */
		@Override
		public IResource[] members() throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#members(boolean)
		 */
		@Override
		public IResource[] members(boolean includePhantoms) throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#members(int)
		 */
		@Override
		public IResource[] members(int memberFlags) throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
		 */
		@Override
		public Object getAdapter(Class adapter) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceProxyVisitor,
		 *      int)
		 */
		@Override
		public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceVisitor,
		 *      int, boolean)
		 */
		@Override
		public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceVisitor,
		 *      int, int)
		 */
		@Override
		public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#accept(org.eclipse.core.resources.IResourceVisitor)
		 */
		@Override
		public void accept(IResourceVisitor visitor) throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#clearHistory(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void clearHistory(IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.runtime.IPath,
		 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void copy(IPath destination, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.runtime.IPath,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void copy(IPath destination, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.resources.IProjectDescription,
		 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#copy(org.eclipse.core.resources.IProjectDescription,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#createMarker(java.lang.String)
		 */
		@Override
		public IMarker createMarker(String type) throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#delete(boolean,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void delete(boolean force, IProgressMonitor monitor) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#delete(int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#deleteMarkers(java.lang.String,
		 *      boolean, int)
		 */
		@Override
		public void deleteMarkers(String type, boolean includeSubtypes, int depth)
				throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#exists()
		 */
		@Override
		public boolean exists() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#findMarker(long)
		 */
		@Override
		public IMarker findMarker(long id) throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#findMarkers(java.lang.String,
		 *      boolean, int)
		 */
		@Override
		public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getFileExtension()
		 */
		@Override
		public String getFileExtension() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getFullPath()
		 */
		@Override
		public IPath getFullPath() {
			return path;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getLocation()
		 */
		@Override
		public IPath getLocation() {
			return path;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getMarker(long)
		 */
		@Override
		public IMarker getMarker(long id) {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getModificationStamp()
		 */
		@Override
		public long getModificationStamp() {

			return 0;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getName()
		 */
		@Override
		public String getName() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getParent()
		 */
		@Override
		public IContainer getParent() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getPersistentProperty(org.eclipse.core.runtime.QualifiedName)
		 */
		@Override
		public String getPersistentProperty(QualifiedName key) throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getProject()
		 */
		@Override
		public IProject getProject() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getProjectRelativePath()
		 */
		@Override
		public IPath getProjectRelativePath() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getRawLocation()
		 */
		@Override
		public IPath getRawLocation() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getSessionProperty(org.eclipse.core.runtime.QualifiedName)
		 */
		@Override
		public Object getSessionProperty(QualifiedName key) throws CoreException {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getType()
		 */
		@Override
		public int getType() {

			return 0;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#getWorkspace()
		 */
		@Override
		public IWorkspace getWorkspace() {

			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isAccessible()
		 */
		@Override
		public boolean isAccessible() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isDerived()
		 */
		@Override
		public boolean isDerived() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isLinked()
		 */
		@Override
		public boolean isLinked() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isLocal(int)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public boolean isLocal(int depth) {
			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isPhantom()
		 */
		@Override
		public boolean isPhantom() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isReadOnly()
		 * @deprecated
		 */
		@Deprecated
		@Override
		public boolean isReadOnly() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isSynchronized(int)
		 */
		@Override
		public boolean isSynchronized(int depth) {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#isTeamPrivateMember()
		 */
		@Override
		public boolean isTeamPrivateMember() {

			return false;
		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.runtime.IPath,
		 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void move(IPath destination, boolean force, IProgressMonitor monitor)
				throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.runtime.IPath,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void move(IPath destination, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.resources.IProjectDescription,
		 *      boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor)
				throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#move(org.eclipse.core.resources.IProjectDescription,
		 *      int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
				throws CoreException {/* Empty block */

		}

		/**
		 * @see org.eclipse.core.resources.IResource#refreshLocal(int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setDerived(boolean)
		 */
		@Override
		public void setDerived(boolean isDerived) throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setLocal(boolean, int,
		 *      org.eclipse.core.runtime.IProgressMonitor)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setPersistentProperty(org.eclipse.core.runtime.QualifiedName,
		 *      java.lang.String)
		 */
		@Override
		public void setPersistentProperty(QualifiedName key, String value) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setReadOnly(boolean)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void setReadOnly(boolean readOnly) {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setSessionProperty(org.eclipse.core.runtime.QualifiedName,
		 *      java.lang.Object)
		 */
		@Override
		public void setSessionProperty(QualifiedName key, Object value) throws CoreException {
			/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#setTeamPrivateMember(boolean)
		 */
		@Override
		public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException {/* Empty block */
		}

		/**
		 * @see org.eclipse.core.resources.IResource#touch(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void touch(IProgressMonitor monitor) throws CoreException {
			/* Empty block */
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IResource#getLocalTimeStamp()
		 */
		@Override
		public long getLocalTimeStamp() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IResource#setLocalTimeStamp(long)
		 */
		@Override
		public long setLocalTimeStamp(long value) throws CoreException {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.
		 * runtime.jobs.ISchedulingRule)
		 */
		@Override
		public boolean contains(ISchedulingRule rule) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.
		 * runtime.jobs.ISchedulingRule)
		 */
		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IContainer#getDefaultCharset()
		 */
		@Override
		public String getDefaultCharset() throws CoreException {
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IContainer#setDefaultCharset(java.lang.String)
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void setDefaultCharset(String charset) throws CoreException {
			/* Empty block */
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IContainer#getDefaultCharset(boolean)
		 */
		@Override
		public String getDefaultCharset(boolean checkImplicit) throws CoreException {
			return null;
		}

		@Override
		public ResourceAttributes getResourceAttributes() {
			return null;
		}

		@Override
		public void revertModificationStamp(long value) throws CoreException {
			/* Empty block */
		}

		@Override
		public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
			/* Empty block */
		}

		@Override
		public URI getLocationURI() {
			return null;
		}

		@Override
		public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			/* Empty block */
		}

		@Override
		public URI getRawLocationURI() {
			return null;
		}

		@Override
		public boolean isLinked(int options) {
			return false;
		}

		@Override
		public IResourceProxy createProxy() {
			return null;
		}

		@Override
		public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException {
			return 0;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isHidden()
		 */
		@Override
		public boolean isHidden() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#setHidden(boolean)
		 */
		@Override
		public void setHidden(boolean isHidden) throws CoreException {
			/* not implemented */
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isDerived(int)
		 */
		@Override
		public boolean isDerived(int arg0) {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#getPersistentProperties()
		 */
		@Override
		public Map getPersistentProperties() throws CoreException {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#getSessionProperties()
		 */
		@Override
		public Map getSessionProperties() throws CoreException {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isHidden(int)
		 */
		@Override
		public boolean isHidden(int arg0) {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isTeamPrivateMember(int)
		 */
		@Override
		public boolean isTeamPrivateMember(int arg0) {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IFolder#createGroup(int,
		 * org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void createGroup(int updateFlags, IProgressMonitor monitor) throws CoreException {
			//
		}

		/*
		 * @see org.eclipse.core.resources.IContainer#getFilters()
		 */
		@Override
		public IResourceFilterDescription[] getFilters() throws CoreException {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#hasFilters()
		 */
		public boolean hasFilters() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isGroup()
		 */
		public boolean isGroup() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#setDerived(boolean,
		 * org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException {
			//
		}

		/*
		 * @see org.eclipse.core.resources.IContainer#createFilter(int,
		 * org.eclipse.core.resources.IFileInfoMatcherDescription, int,
		 * org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public IResourceFilterDescription createFilter(int type, FileInfoMatcherDescription matcherDescription,
				int updateFlags, IProgressMonitor monitor) throws CoreException {
			return null;
		}

		/*
		 * @see
		 * org.eclipse.core.resources.IContainer#removeFilter(org.eclipse.core.resources
		 * .IResourceFilterDescription, int, org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void removeFilter(IResourceFilterDescription filterDescription, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isVirtual()
		 */
		@Override
		public boolean isVirtual() {
			return false;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#getPathVariableManager()
		 */
		@Override
		public IPathVariableManager getPathVariableManager() {
			return null;
		}

		/*
		 * @see org.eclipse.core.resources.IResource#isFiltered()
		 */
		public boolean isFiltered() {
			return false;
		}

		@Override
		public void accept(IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException {
			// TODO Auto-generated method stub

		}
	}

	@BeforeEach
	public void setUp() {
		/* Empty block */
	}

	@Test
	public void test_allFileObserverFilter() {
		FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.ALL);
		IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile file = new File(path);
		assertTrue(filter.matches(file));
	}

	@Test
	public void test_extensionFileObserverFilter() {
		String[] extensions = { "txt" }; //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.EXTENSION, extensions);
		IPath txt = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile txtFile = new File(txt);
		assertTrue(filter.matches(txtFile));
		IPath mdx = new Path("c:\\test.mdx"); //$NON-NLS-1$
		IFile mdxFile = new File(mdx);
		assertFalse(filter.matches(mdxFile));
	}

	@Test
	public void test_fileFileObserverFilter() {
		IPath txt = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile txtFile = new File(txt);
		FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.FILE, txtFile);
		assertTrue(filter.matches(txtFile));
		IPath mdx = new Path("c:\\test.mdx"); //$NON-NLS-1$
		IFile mdxFile = new File(mdx);
		assertFalse(filter.matches(mdxFile));
	}

	@Test
	public void test_pathFileObserverFilter() {
		IPath path = new Path("c:\\test"); //$NON-NLS-1$
		IFolder folder = new Folder(path);
		FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.FOLDER, folder);
		IPath yes = new Path("c:\\test\\test.txt"); //$NON-NLS-1$
		IFile yesFile = new File(yes);
		assertTrue(filter.matches(yesFile));
		IPath no = new Path("c:\\other\\test.txt"); //$NON-NLS-1$
		IFile noFile = new File(no);
		assertFalse(filter.matches(noFile));
		assertFalse(filter.matches(folder));
	}
}