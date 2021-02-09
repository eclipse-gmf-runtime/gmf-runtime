/******************************************************************************
 * Copyright (c) 2002, 2021 IBM Corporation and others.
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

import java.net.URI;
import java.util.Map;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * Mock implementation of {@link IFolder} used for testing.
 */
class MockFolder implements IFolder {

    private IPath path;

    public MockFolder(IPath path) {
        this.path = path;
    }

    public void setDefaultCharset(String charset, IProgressMonitor monitor) throws CoreException {
    }

    public void create(boolean force, boolean local, IProgressMonitor monitor) throws CoreException {
    }

    public void create(int updateFlags, boolean local, IProgressMonitor monitor) throws CoreException {
    }

    public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
    }

    public IFile getFile(String name) {
        return null;
    }

    public IFolder getFolder(String name) {
        return null;
    }

    public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
    }

    public boolean exists(IPath pth) {
        return false;
    }

    public IFile[] findDeletedMembersWithHistory(int depth, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    public IResource findMember(IPath pth, boolean includePhantoms) {
        return null;
    }

    public IResource findMember(IPath pth) {
        return null;
    }

    public IResource findMember(String name, boolean includePhantoms) {
        return null;
    }

    public IResource findMember(String name) {
        return null;
    }

    public IFile getFile(IPath pth) {
        return null;
    }

    public IFolder getFolder(IPath pth) {
        return null;
    }

    public IResource[] members() throws CoreException {
        return null;
    }

    public IResource[] members(boolean includePhantoms) throws CoreException {
        return null;
    }

    public IResource[] members(int memberFlags) throws CoreException {
        return null;
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException {
    }

    public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms) throws CoreException {
    }

    public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException {
    }

    public void accept(IResourceVisitor visitor) throws CoreException {

    }

    public void clearHistory(IProgressMonitor monitor) throws CoreException {
    }

    public void copy(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException {
    }

    public void copy(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException {
    }

    public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public IMarker createMarker(String type) throws CoreException {
        return null;
    }

    public void delete(boolean force, IProgressMonitor monitor) throws CoreException {
    }

    public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public void deleteMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
    }

    public boolean exists() {
        return false;
    }

    public IMarker findMarker(long id) throws CoreException {
        return null;
    }

    public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
        return null;
    }

    public String getFileExtension() {
        return null;
    }

    public IPath getFullPath() {
        return path;
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

    public String getName() {
        return null;
    }

    public IContainer getParent() {
        return null;
    }

    public String getPersistentProperty(QualifiedName key) throws CoreException {
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

    public Object getSessionProperty(QualifiedName key) throws CoreException {
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

    public boolean isReadOnly() {
        return false;
    }

    public boolean isSynchronized(int depth) {
        return false;
    }

    public boolean isTeamPrivateMember() {
        return false;
    }

    public void move(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException {
    }

    public void move(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
    }

    public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException {
    }

    public void setDerived(boolean isDerived) throws CoreException {
    }

    public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException {
    }

    public void setPersistentProperty(QualifiedName key, String value) throws CoreException {
    }

    public void setReadOnly(boolean readOnly) {
    }

    public void setSessionProperty(QualifiedName key, Object value) throws CoreException {
    }

    public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException {
    }

    public void touch(IProgressMonitor monitor) throws CoreException {
    }

    public long getLocalTimeStamp() {
        return 0;
    }

    public long setLocalTimeStamp(long value) throws CoreException {
        return 0;
    }

    public boolean contains(ISchedulingRule rule) {
        return false;
    }

    public boolean isConflicting(ISchedulingRule rule) {
        return false;
    }

    public String getDefaultCharset() throws CoreException {
        return null;
    }

    public void setDefaultCharset(String charset) throws CoreException {
    }

    public String getDefaultCharset(boolean checkImplicit) throws CoreException {
        return null;
    }

    public ResourceAttributes getResourceAttributes() {
        return null;
    }

    public void revertModificationStamp(long value) throws CoreException {
    }

    public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
    }

    public URI getLocationURI() {
        return null;
    }

    public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
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

    public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException {
        return 0;
    }

    public boolean isHidden() {
        return false;
    }

    public void setHidden(boolean isHidden) throws CoreException {
    }

    public boolean isDerived(int arg0) {
        return false;
    }

    public Map getPersistentProperties() throws CoreException {
        return null;
    }

    public Map getSessionProperties() throws CoreException {
        return null;
    }

    public boolean isHidden(int arg0) {
        return false;
    }

    public boolean isTeamPrivateMember(int arg0) {
        return false;
    }

    public void createGroup(int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public IResourceFilterDescription[] getFilters() throws CoreException {
        return null;
    }

    public boolean hasFilters() {
        return false;
    }

    public boolean isGroup() {
        return false;
    }

    public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException {
    }

    public IResourceFilterDescription createFilter(int type, FileInfoMatcherDescription matcherDescription, int updateFlags, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    public void removeFilter(IResourceFilterDescription filterDescription, int updateFlags, IProgressMonitor monitor) throws CoreException {
    }

    public boolean isVirtual() {
        return false;
    }

    public IPathVariableManager getPathVariableManager() {
        return null;
    }

    public boolean isFiltered() {
        return false;
    }

    public void accept(IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException {
    }

    public IMarker createMarker(String type, Map<String, Object> attributes) throws CoreException {
        return null;
    }
}
