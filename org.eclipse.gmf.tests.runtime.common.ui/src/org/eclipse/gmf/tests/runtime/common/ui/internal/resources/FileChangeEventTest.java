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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEvent;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEventType;

/**
 * Tests for FileChangeEvent.
 * 
 * @author Anthony Hunter
 */
public class FileChangeEventTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(FileChangeEventTest.class);
    }

    public FileChangeEventTest(String name) {
        super(name);
    }

    protected void setUp() {/* Empty block */
    }

    public void test_deleteFileChangeEvent() {
        IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile file = new MockFile(path);
        FileChangeEvent event = new FileChangeEvent(FileChangeEventType.DELETED, file);
        assertTrue(event.getEventType() == FileChangeEventType.DELETED);
        assertTrue(event.getFile().equals(file));
    }

    public void test_changeFileChangeEvent() {
        IPath oldPath = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile file = new MockFile(oldPath);
        FileChangeEvent event = new FileChangeEvent(FileChangeEventType.CHANGED, file);
        assertTrue(event.getEventType() == FileChangeEventType.CHANGED);
        assertTrue(event.getFile().equals(file));
    }

    public void test_moveFileChangeEvent() {
        IPath oldPath = new Path("c:\\folder\\test.txt"); //$NON-NLS-1$
        IFile oldFile = new MockFile(oldPath);
        IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile file = new MockFile(path);
        FileChangeEvent event = new FileChangeEvent(FileChangeEventType.MOVED, oldFile, file);
        assertTrue(event.getEventType() == FileChangeEventType.MOVED);
        assertTrue(event.getFile().equals(file));
        assertTrue(event.getOldFile().equals(oldFile));
    }

    public void test_renameFileChangeEvent() {
        IPath oldPath = new Path("c:\\oldtest.txt"); //$NON-NLS-1$
        IFile oldFile = new MockFile(oldPath);
        IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile file = new MockFile(path);
        FileChangeEvent event = new FileChangeEvent(FileChangeEventType.RENAMED, oldFile, file);
        assertTrue(event.getEventType() == FileChangeEventType.RENAMED);
        assertTrue(event.getFile().equals(file));
        assertTrue(event.getOldFile().equals(oldFile));
    }
}
