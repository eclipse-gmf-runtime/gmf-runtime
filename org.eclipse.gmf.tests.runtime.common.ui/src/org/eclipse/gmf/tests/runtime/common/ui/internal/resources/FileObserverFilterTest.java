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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilter;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilterType;

/**
 * Tests for FileObserverFilter.
 * 
 * @author Anthony Hunter
 */
public class FileObserverFilterTest extends TestCase {

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
        FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.ALL);
        IPath path = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile file = new MockFile(path);
        assertTrue(filter.matches(file));
    }

    public void test_extensionFileObserverFilter() {
        String[] extensions = { "txt" }; //$NON-NLS-1$
        FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.EXTENSION, extensions);
        IPath txt = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile txtFile = new MockFile(txt);
        assertTrue(filter.matches(txtFile));
        IPath mdx = new Path("c:\\test.mdx"); //$NON-NLS-1$
        IFile mdxFile = new MockFile(mdx);
        assertFalse(filter.matches(mdxFile));
    }

    public void test_fileFileObserverFilter() {
        IPath txt = new Path("c:\\test.txt"); //$NON-NLS-1$
        IFile txtFile = new MockFile(txt);
        FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.FILE, txtFile);
        assertTrue(filter.matches(txtFile));
        IPath mdx = new Path("c:\\test.mdx"); //$NON-NLS-1$
        IFile mdxFile = new MockFile(mdx);
        assertFalse(filter.matches(mdxFile));
    }

    public void test_pathFileObserverFilter() {
        IPath path = new Path("c:\\test"); //$NON-NLS-1$
        IFolder folder = new MockFolder(path);
        FileObserverFilter filter = new FileObserverFilter(FileObserverFilterType.FOLDER, folder);
        IPath yes = new Path("c:\\test\\test.txt"); //$NON-NLS-1$
        IFile yesFile = new MockFile(yes);
        assertTrue(filter.matches(yesFile));
        IPath no = new Path("c:\\other\\test.txt"); //$NON-NLS-1$
        IFile noFile = new MockFile(no);
        assertFalse(filter.matches(noFile));
        assertFalse(filter.matches(folder));
    }
}
