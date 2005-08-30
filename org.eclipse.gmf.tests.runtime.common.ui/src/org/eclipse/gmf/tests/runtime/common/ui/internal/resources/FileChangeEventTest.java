/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.ui.internal.resources;

import java.io.InputStream;
import java.io.Reader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
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

import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEvent;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEventType;

/**
 * 
 * 
 * @author Anthony Hunter 
 * <a href="mailto:ahunter@rational.com">ahunter@rational.com</a>
 */
public class FileChangeEventTest extends TestCase {

	protected class File implements IFile {

		private IPath path;

		public File(IPath path) {
			this.path = path;
		}

		public void appendContents(
			InputStream source,
			boolean force,
			boolean keepHistory,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setCharset(String newCharset, IProgressMonitor monitor)
			throws CoreException {
			// blank

		}
		public void appendContents(
			InputStream source,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void create(
			InputStream source,
			boolean force,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void create(
			InputStream source,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void createLink(
			IPath localLocation,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void delete(
			boolean force,
			boolean keepHistory,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public InputStream getContents() throws CoreException {
			return null;
		}

		public InputStream getContents(boolean force) throws CoreException {
			return null;
		}

		public int getEncoding() throws CoreException {
			// Can't get rid of warning as depricated abstract method
			// must be implemented
			return 0;
		}

		public IPath getFullPath() {
			return null;
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

		public void move(
			IPath destination,
			boolean force,
			boolean keepHistory,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setContents(
			IFileState source,
			boolean force,
			boolean keepHistory,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setContents(
			IFileState source,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setContents(
			InputStream source,
			boolean force,
			boolean keepHistory,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setContents(
			InputStream source,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void accept(IResourceProxyVisitor visitor, int memberFlags)
			throws CoreException {/*Empty block*/
		}

		public void accept(
			IResourceVisitor visitor,
			int depth,
			boolean includePhantoms)
			throws CoreException {/*Empty block*/
		}

		public void accept(
			IResourceVisitor visitor,
			int depth,
			int memberFlags)
			throws CoreException {/*Empty block*/
		}

		public void accept(IResourceVisitor visitor) throws CoreException {
			/*Empty block*/
		}

		public void clearHistory(IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void copy(
			IPath destination,
			boolean force,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void copy(
			IPath destination,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void copy(
			IProjectDescription description,
			boolean force,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void copy(
			IProjectDescription description,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public IMarker createMarker(String type) throws CoreException {
			return null;
		}

		public void delete(boolean force, IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void delete(int updateFlags, IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void deleteMarkers(
			String type,
			boolean includeSubtypes,
			int depth)
			throws CoreException {/*Empty block*/
		}

		public boolean exists() {
			return false;
		}

		public IMarker findMarker(long id) throws CoreException {
			return null;
		}

		public IMarker[] findMarkers(
			String type,
			boolean includeSubtypes,
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

		public void move(
			IPath destination,
			boolean force,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void move(
			IPath destination,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void move(
			IProjectDescription description,
			boolean force,
			boolean keepHistory,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void move(
			IProjectDescription description,
			int updateFlags,
			IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void refreshLocal(int depth, IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setDerived(boolean isDerived) throws CoreException {/*Empty block*/
		}

		public void setLocal(boolean flag, int depth, IProgressMonitor monitor)
			throws CoreException {/*Empty block*/
		}

		public void setPersistentProperty(QualifiedName key, String value)
			throws CoreException {/*Empty block*/
		}

		public void setReadOnly(boolean readOnly) {/*Empty block*/
		}

		public void setSessionProperty(QualifiedName key, Object value)
			throws CoreException {/*Empty block*/
		}

		public void setTeamPrivateMember(boolean isTeamPrivate)
			throws CoreException {/*Empty block*/
		}

		public void touch(IProgressMonitor monitor) throws CoreException {/*Empty block*/
		}

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
		public long setLocalTimeStamp(long value) throws CoreException {
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
		
		public String getCharset() throws CoreException
		{
			return null;
		}

		/**
		 * @see org.eclipse.core.resources.IFile#setCharset(java.lang.String)
		 */
		public void setCharset(String newCharset)
			throws CoreException {/*Empty block*/
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
	}
	
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(FileChangeEventTest.class);
	}

	public FileChangeEventTest(String name) {
		super(name);
	}

	protected void setUp() {/*Empty block*/
	}

	public void test_deleteFileChangeEvent() {
		IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile file = new File(path);
		FileChangeEvent event =
			new FileChangeEvent(FileChangeEventType.DELETED, file);
		assertTrue(event.getEventType() == FileChangeEventType.DELETED);
		assertTrue(event.getFile().equals(file));
	}

	public void test_changeFileChangeEvent() {
		IPath oldPath = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile file = new File(oldPath);
		FileChangeEvent event =
			new FileChangeEvent(FileChangeEventType.CHANGED, file);
		assertTrue(event.getEventType() == FileChangeEventType.CHANGED);
		assertTrue(event.getFile().equals(file));
	}
	
	public void test_moveFileChangeEvent() {
		IPath oldPath = new Path("c:\\folder\\test.txt"); //$NON-NLS-1$
		IFile oldFile = new File(oldPath);
		IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile file = new File(path);
		FileChangeEvent event =
			new FileChangeEvent(FileChangeEventType.MOVED, oldFile, file);
		assertTrue(event.getEventType() == FileChangeEventType.MOVED);
		assertTrue(event.getFile().equals(file));
		assertTrue(event.getOldFile().equals(oldFile));
	}

	public void test_renameFileChangeEvent() {
		IPath oldPath = new Path("c:\\oldtest.txt"); //$NON-NLS-1$
		IFile oldFile = new File(oldPath);
		IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
		IFile file = new File(path);
		FileChangeEvent event =
			new FileChangeEvent(FileChangeEventType.RENAMED, oldFile, file);
		assertTrue(event.getEventType() == FileChangeEventType.RENAMED);
		assertTrue(event.getFile().equals(file));
		assertTrue(event.getOldFile().equals(oldFile));
	}
}
