/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AbstractTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EMFTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.WMFTranscoder;
import org.osgi.framework.Bundle;

/**
 * @author dhabib
 */
public class Metafile2SvgTest
    extends TestCase {

    /**
     * Indicates whether or not we are re-generating the SVG files from the EMF
     * files Check out .svg files to be updated so they are writable, flip this
     * bit to 'true' and run the test to regenerate the svg files. If a .svg
     * file is not writable, it is skipped.
     */
    private static final boolean GENERATE_SVG_FILES = false;

    private Bundle bundle;

    public Metafile2SvgTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(Metafile2SvgTest.class);
    }

    protected void setUp()
        throws Exception {
        super.setUp();

        // Initialize the path the the resources.
        bundle = Platform
            .getBundle("org.eclipse.gmf.tests.runtime.draw2d.ui.render");//$NON-NLS-1$
    }

    protected void tearDown()
        throws Exception {
        super.tearDown();
    }

    public void testEmf2Svg()
        throws Exception {
        // Enumerate all the *.emf files in the source directory.
        String rootDir = "resources/metafiles/"; //$NON-NLS-1$
        Enumeration enumFiles = bundle.getEntryPaths(rootDir);

        Vector failures = new Vector();

        while (enumFiles.hasMoreElements()) {
            String metafileName = (String) enumFiles.nextElement();
            IPath filePath = new Path(metafileName);
            if (filePath != null && metafileName.endsWith("emf") || //$NON-NLS-1$
                metafileName.endsWith("wmf")) { //$NON-NLS-1$
                // Get the svg file to write/compare with.
                String svgFilename = metafileName.substring(0, metafileName
                    .length() - 3)
                    + "svg"; //$NON-NLS-1$
                URL url = FileLocator.find(bundle, filePath, null);
                InputStream metafileInputStream = url.openStream();

                try {
                    if (GENERATE_SVG_FILES) {
                        URL pluginURL = bundle.getEntry("/"); //$NON-NLS-1$ 
                        String pluginPath = FileLocator.resolve( pluginURL ).getPath().substring(1);
                        File svgFile = new File(pluginPath + svgFilename);

                        if (svgFile.exists()) {
                            if (!svgFile.canWrite()) {
                                // File is read only, skip it (probably not
                                // checked out)
                                continue;
                            }
                            svgFile.delete();
                        }

                        svgFile.createNewFile();

                        FileOutputStream svgOutputStream = new FileOutputStream(
                            svgFile);

                        // Translate the metafile to SVG.
                        AbstractTranscoder tc;

                        if (metafileName.endsWith(".emf")) //$NON-NLS-1$
                        {
                            tc = new EMFTranscoder();
                        } else {
                            tc = new WMFTranscoder();
                        }

                        tc.transcode(metafileInputStream, svgOutputStream, svgFile.getAbsolutePath());

                        // Close the output stream.
                        svgOutputStream.flush();
                        svgOutputStream.close();
                    } else {
                        // Create an outputstream to hold the generated svg
                        // data.
                        ByteArrayOutputStream svgOutputStream = new ByteArrayOutputStream(
                            10000);

                        // Translate the metafile to SVG.
                        AbstractTranscoder tc;

                        if (metafileName.endsWith(".emf")) //$NON-NLS-1$
                        {
                            tc = new EMFTranscoder();
                        } else {
                            tc = new WMFTranscoder();
                        }

                        URL svgUrl = FileLocator.find(bundle, new Path(
                                svgFilename), null);
                        tc.transcode(metafileInputStream, svgOutputStream, svgUrl.toString());

                        // Compare the generated SVG data to the file

                        // read the svg file on the disk.
                        InputStream svgInputStream = svgUrl.openStream();
                        byte[] fileBytes = new byte[svgInputStream.available()];

                        svgInputStream.read(fileBytes);
                        svgInputStream.close();

                        // Read the output stream.
                        svgOutputStream.flush();
                        svgOutputStream.close();
                        byte[] generatedBytes = svgOutputStream.toByteArray();

                        if (fileBytes.length != generatedBytes.length) {
                            String errorMessage = "Data sizes are not equal: " + svgFilename + //$NON-NLS-1$
                                " Expected: " + fileBytes.length + //$NON-NLS-1$
                                " Received: " + generatedBytes.length + "\n"; //$NON-NLS-2$//$NON-NLS-1$
                            failures.add(errorMessage);
                            // Write the generated data out.
                            writeErrorFile(svgFilename, generatedBytes);
                            continue;
                        }

                        for (int i = 0; i < fileBytes.length; i++) {
                            if (fileBytes[i] != generatedBytes[i]) {
                                String errorMessage = "Data is different in file \'" + svgFilename + "\' at byte " + i + "\n"; //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
                                failures.add(errorMessage);
                                // Write the generated data out.
                                writeErrorFile(svgFilename, generatedBytes);
                                break;
                            }
                        }
                    }

                    metafileInputStream.close();
                } catch (Exception e) {
                    String errorMessage = "Caught exception while processing file " + metafileName + //$NON-NLS-1$
                        "\n" + e.toString(); //$NON-NLS-1$

                    failures.add(errorMessage);
                }
            }
        }

        if (failures.size() > 0) {
            String failureString = ""; //$NON-NLS-1$
            for (int index = 0; index < failures.size(); index++) {
                failureString = failureString + (String) failures.get(index);
            }
            Assert.assertEquals(
                "Found failures:\n" + failureString, 0, failures.size()); //$NON-NLS-1$
        }
    }

    /**
     * Writes out a svg error file based on the specified filename. Filename is
     * appended with _failed.svg and the specified data is written to it.
     * 
     * @param svgFilename
     * @param data
     * @throws IOException
     */
    private void writeErrorFile(String svgFilename, byte[] data)
        throws IOException {
        String filename = svgFilename.substring(0, svgFilename.length() - 4)
            + "_failed.svg"; //$NON-NLS-1$

        File errorFile = new File(filename);

        if (errorFile.exists()) {
            if (!errorFile.canWrite()) {
                return;
            }
            errorFile.delete();
        }

        errorFile.createNewFile();

        FileOutputStream svgOutputStream = new FileOutputStream(errorFile);
        svgOutputStream.write(data);
        svgOutputStream.flush();
        svgOutputStream.close();
    }
}
