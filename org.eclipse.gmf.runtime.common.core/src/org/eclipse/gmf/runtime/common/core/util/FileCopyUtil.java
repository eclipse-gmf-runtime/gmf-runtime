/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for copying files and folders
 * 
 * @author gsturov
 */
public class FileCopyUtil {

	/**
	 * Private constructor
	 */
	private FileCopyUtil() {
		/* empty method body */
	}

	/**
	 * Copies the contents of a source folder to a target folder
	 * 
	 * @param sourceFolder
	 *            the source folder
	 * @param targetFolder
	 *            the target folder
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFolder(String sourceFolder, String targetFolder)
		throws FileNotFoundException, IOException {
		assert (new File(sourceFolder).isDirectory());
		assert (new File(targetFolder).isDirectory());

		File source = new File(sourceFolder);
		String[] files = source.list();
		for (int i = 0; i < files.length; i++) {
			File f = new File(sourceFolder + File.separator + files[i]);
			if (f.isDirectory())
				copyFolder(sourceFolder, targetFolder, files[i]);
			else
				copyFile(sourceFolder, targetFolder, files[i]);
		}
	}

	/**
	 * Copies a source file to a target folder
	 * 
	 * @param sourceFile
	 *            the source file
	 * @param targetFolder
	 *            the target folder
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(String sourceFile, String targetFolder)
		throws FileNotFoundException, IOException {
		assert (new File(sourceFile).isFile());
		assert (new File(targetFolder).isDirectory());

		File source = new File(sourceFile);
		copyFile(source.getParent(), targetFolder, source.getName());
	}

	/**
	 * Copies a folder in a source folder to a target folder
	 * 
	 * @param sourceFolder
	 *            the source folder
	 * @param targetFolder
	 *            the target folder
	 * @param name
	 *            the folder to copy
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void copyFolder(String sourceFolder, String targetFolder,
			String name)
		throws FileNotFoundException, IOException {
		File target = new File(targetFolder + File.separator + name);
		target.mkdir();
		copyFolder(sourceFolder + File.separator + name, targetFolder
			+ File.separator + name);
	}

	/**
	 * Copies a file in a source folder to a target folder
	 * 
	 * @param sourceFolder
	 *            the source folder
	 * @param targetFolder
	 *            the target folder
	 * @param name
	 *            the file to copy
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void copyFile(String sourceFolder, String targetFolder,
			String name)
		throws FileNotFoundException, IOException {
		copyFile(sourceFolder, targetFolder, name, name);
	}

	/**
	 * Copies a file in a source folder to a target folder
	 * 
	 * @param sourceFolder
	 *            the source folder
	 * @param targetFolder
	 *            the target folder
	 * @param sourceName
	 *            of the source file to copy
	 * @param targetName
	 *            of the destination file to copy to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(String sourceFolder, String targetFolder,
			String sourceName, String targetName)
		throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(sourceFolder + File.separator
			+ sourceName);
		OutputStream os = new FileOutputStream(targetFolder + File.separator
			+ targetName);
		byte[] buffer = new byte[102400];
		while (true) {
			int len = is.read(buffer);
			if (len < 0)
				break;
			os.write(buffer, 0, len);
		}
		is.close();
		os.close();
	}
}
