/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.printing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.printing.internal.CommonPrintingDebugOptions;
import org.eclipse.gmf.runtime.common.ui.printing.internal.CommonPrintingStatusCodes;
import org.eclipse.gmf.runtime.common.ui.printing.internal.PrintingPlugin;

/**
 * The PrintHelper implements the IPrintHelper interface for the platform
 * specfic print code.
 * 
 * This is for Win32.
 * 
 * @author wdiu, Wayne Diu
 */
public class PrintHelper
	implements IPrintHelper {

	/**
	 * The WIN32 folder name to append to the starting directory. Do not
	 * externalize the directory name strings.
	 */
	private static final String WIN32_FOLDER = File.separator + "os" //$NON-NLS-1$
		+ File.separator + "win32" + File.separator + "x86"; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The starting directory has .win32 appended to it, since we are in a.
	 * win32 fragment. Do not externalize the directory name strings.
	 */
	private static final String WIN32_END = ".win32"; //$NON-NLS-1$

	/**
	 * DLL Name
	 */
	private static final String DLL_NAME = "DiagramPrint.dll"; //$NON-NLS-1$

	/*
	 * Load the dll
	 */
	static {
		try {
			String startingFolder = new Path(Platform.resolve(
				PrintingPlugin.getDefault().getBundle().getEntry("/"))//$NON-NLS-1$
				.getFile()).toOSString();

			//strip the ending, os string may have file separator
			if (startingFolder.endsWith(File.separator)) {
				startingFolder = startingFolder.substring(0, startingFolder
					.length()
					- File.separator.length());
			}

			//get the version separator, if necessary
			String version = (String) PrintingPlugin.getDefault().getBundle()
				.getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
			int end = startingFolder.length() - version.length() - 1;
			//very strange if end isn't at least 1
			if (startingFolder.endsWith(version) && end >= 1) {
				startingFolder = startingFolder.substring(0, end);
				startingFolder = startingFolder.concat(WIN32_END).concat(
					StringStatics.PLUGIN_VERSION_SEPARATOR).concat(version)
					.concat(WIN32_FOLDER);
			} else {
				startingFolder = startingFolder.concat(WIN32_END).concat(
					WIN32_FOLDER);
			}

			//According to eclipse bug
			//53767 Absolute path preceeded by forward slash
			//https://bugs.eclipse.org/bugs/show_bug.cgi?id=53767
			//it is OK for an absolute path to have a forward
			//slash in front of it.

			//strip the preceeding / or \, be safe and check for both
			if (startingFolder.startsWith(StringStatics.FORWARD_SLASH)
				|| startingFolder.startsWith(StringStatics.BACKWARD_SLASH)) {
				startingFolder = startingFolder.substring(1);
			}

			// Globalization support required for the print dll.
			// Drive:\install
			// directory\com.ibm.xtools.common.printing.win32\os\win32\x86\"locale"\DiagramPrint.dll
			// ...check for DLL using the full locale name.
			// ...if the file exists, then that's all we need
			Path dllPath = new Path(startingFolder + File.separator
				+ Locale.getDefault().toString() + File.separator + DLL_NAME);
			if (dllPath.toFile().exists())
				// file is there, might as well load the dll
				System.load(dllPath.toString());
			else {
				// ...otherwise we just use the language and country code
				dllPath = new Path(startingFolder + File.separator
					+ Locale.getDefault().getLanguage() + "_" //$NON-NLS-1$
					+ Locale.getDefault().getCountry() + File.separator
					+ DLL_NAME);
				if (dllPath.toFile().exists()) {
					// file is there, might as well load the dll
					System.load(dllPath.toString());
				} else {
					// ... well just try the language code allone
					dllPath = new Path(startingFolder + File.separator
					+ Locale.getDefault().getLanguage() + File.separator
					+ DLL_NAME);
					if (dllPath.toFile().exists()) {
						// file is there, might as well load the dll
						System.load(dllPath.toString());
					}
					else {
						//load English for an unsupported locale
						dllPath = new Path(startingFolder + File.separator
							+ Locale.ENGLISH.toString() + File.separator
							+ DLL_NAME);
						//for this last one, don't bother checking to see if the
						//file exists so that an exception will be thrown if it
						//doesn't
						System.load(dllPath.toString());
					}
				}
			}

		} catch (UnsatisfiedLinkError ule) {
			Trace.catching(PrintingPlugin.getDefault(),
				CommonPrintingDebugOptions.EXCEPTIONS_CATCHING,
				PrintHelper.class, "Link", ule); //$NON-NLS-1$
			Log.error(PrintingPlugin.getDefault(),
				CommonPrintingStatusCodes.RESOURCE_FAILURE,
				"Failed to load DiagramPrint.dll", ule); //$NON-NLS-1$
			Trace.throwing(PrintingPlugin.getDefault(),
				CommonPrintingDebugOptions.EXCEPTIONS_THROWING,
				PrintHelper.class, "Link", ule); //$NON-NLS-1$*/
			throw ule;
		} catch (IOException ioe) {
			Trace.catching(PrintingPlugin.getDefault(),
				CommonPrintingDebugOptions.EXCEPTIONS_CATCHING,
				PrintHelper.class, "Link path", ioe); //$NON-NLS-1$
			Log.error(PrintingPlugin.getDefault(),
				CommonPrintingStatusCodes.RESOURCE_FAILURE,
				"Failed to resolve link path for DiagramPrint.dll", ioe); //$NON-NLS-1$
			//cannot resolve the link path will give a link error
			UnsatisfiedLinkError ule = new UnsatisfiedLinkError();
			Trace.throwing(PrintingPlugin.getDefault(),
				CommonPrintingDebugOptions.EXCEPTIONS_THROWING,
				PrintHelper.class, "Link path", ule); //$NON-NLS-1$*/
			throw ule;
		}
	}

	/*
	 * Static methods from dll
	 */

	/*
	 * Set methods
	 */

	/**
	 * Init print dialog box's scale fit to.
	 * 
	 * @param m
	 * @param n
	 */
	public static native void initScaleFitTo(int m, int n);

	/**
	 * Init print dialog box's scale percent.
	 * 
	 * @param percent 
	 */
	public static native void initScalePercent(int percent);

	/**
	 * Resets the dialog. Call this before addDiagramString if you've already
	 * added strings.
	 */
	public static native void resetDialog();

	/**
	 * Add a string to the diagrams list of the dialog box.
	 * 
	 * @param string 
	 */
	public static native void addDiagramString(String string);

	/**
	 * Set the owner hwnd so that the displyed print dialog is modal.
	 * 
	 * @param windowClass	String with class
	 * @param title			String with shell title
	 */
	public static native void setHwndOwner(String windowClass, String title);

	/*
	 * Get methods
	 */

	/**
	 * If not > 0 then user has clicked percent scaling
	 * 
	 * @return scale to m int
	 */
	public static native int getScaleFitToM();

	/**
	 * If not > 0 then user has clicked percent scaling
	 * 
	 * @return scale to n int
	 */
	public static native int getScaleFitToN();

	/**
	 * If not > 0 then user has clicked fit to m x n scaling
	 * 
	 * @return scale percent int
	 */
	public static native int getScalePercent();

	/**
	 * Returns true if the diagram at the index was selected
	 * 
	 * @param index
	 *            the index of the diagrams you passed in
	 * @return true if selected, false if not selected
	 */
	public static native boolean isDiagramSelected(int index);

	//one of the three is unnecssary, but it makes more sense
	/**
	 * Returns if diagram print range all was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public static native boolean getDiagramPrintRangeAll();

	/**
	 * Returns if diagram print range current was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public static native boolean getDiagramPrintRangeCurrent();

	/**
	 * Returns if diagram print range selection was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public static native boolean getDiagramPrintRangeSelection();

	//one of the three is unnecssary, but it makes more sense
	/**
	 * Returns if print range all was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public static native boolean getPrintRangeAll();

	/**
	 * Returns if print range pages was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public static native boolean getPrintRangePages();

	/**
	 * Returns if collate was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public static native boolean getCollate();

	/**
	 * Returns the pages from int value, check if (getPrintRangesPages()) first.
	 * 
	 * @return int of the pages from box
	 */
	public static native int getPagesFrom();

	/**
	 * Returns the pages from int value, check if (getPrintRangesPages()) first.
	 * 
	 * @return int of the pages to box
	 */
	public static native int getPagesTo();

	/**
	 * Returns the number of copies the user wants to print. This is nCopies
	 * from PRINTDLG
	 * 
	 * @return int with the number of copies
	 */
	public static native int getNumberOfCopies();

	/*
	 * Don't do getDevMode() and gethDevNames() for hDevMode and hDevNames from
	 * PRINTDLG because they are filled by passing in PrinterData to open..
	 */

	/**
	 * Opens the dialog box.
	 * 
	 * @param pd
	 * @return <code>true</code> if dialog box opened succesfully, <code>false</code> otherwise
	 */
	public static native boolean open(PrinterData pd);

	/**
	 * Open the print dialog box with the diagram list.
	 * 
	 * XXX: internal access SWT_Window0 is a hardcoded internal access string
	 * 
	 * @param diagramList
	 */
	public PrinterData openPrintDlg(List diagramList) {

		//set owner to make it modal
		String title = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getShell().getText();
		
		assert null!=title : "title cannot be null"; //$NON-NLS-1$

		//XXX: internal access
		//SWT_Window0 is a hardcoded internal access string of the class
		//name and it could change. However, setHwndOwner will try to
		//obtain the window even if it can't find that matching class by
		//using the window's title.
		setHwndOwner("SWT_Window0", title); //$NON-NLS-1$

		//initialize dialog
		resetDialog();
		initScaleFitTo(1, 1);
		initScalePercent(100);

		if (diagramList != null) {
			for (int c = 0; c < diagramList.size(); c++) {
				assert (diagramList.get(c) instanceof String);
				addDiagramString((String) diagramList.get(c));
			}
		}

		//prevent default initialization from empty constructor
		PrinterData printerData = new PrinterData(StringStatics.BLANK,
			StringStatics.BLANK);
		if (open(printerData)) {
			//save the printerData
			if (getPrintRangePages()) {
				printerData.scope = PrinterData.PAGE_RANGE;
				printerData.startPage = getPagesFrom();
				printerData.endPage = getPagesTo();
			} else /* if (getPrintRangeAll()) */{
				printerData.scope = PrinterData.ALL_PAGES;
			}
			printerData.printToFile = false;
			//not supported by dialog, no need to set printerData.fileName

			printerData.copyCount = getNumberOfCopies();
			printerData.collate = getCollate();

		} else {
			return null;
		}

		return printerData;

	}

	/**
	 * Make an instance of PrintHelper
	 */
	public PrintHelper() {
		/* empty constructor */
	}

	/*
	 * The get methods to hide the static methods
	 */

	/**
	 * If not > 0 then user has clicked percent scaling
	 * 
	 * @return scale to m int
	 */
	public int getDlgScaleFitToM() {
		return getScaleFitToM();
	}

	/**
	 * If not > 0 then user has clicked percent scaling
	 * 
	 * @return scale to n int
	 */
	public int getDlgScaleFitToN() {
		return getScaleFitToN();
	}

	/**
	 * If not > 0 then user has clicked fit to m x n scaling
	 * 
	 * @return scale percent int
	 */
	public int getDlgScalePercent() {
		return getScalePercent();
	}

	/**
	 * Returns true if the diagram at the index was selected
	 * 
	 * @param index
	 *            the index of the diagrams you passed in
	 * @return true if selected, false if not selected
	 */
	public boolean isDlgDiagramSelected(int index) {
		return isDiagramSelected(index);
	}

	//one of the three is unnecssary, but it makes more sense
	/**
	 * Returns if diagram print range all was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getDlgDiagramPrintRangeAll() {
		return getDiagramPrintRangeAll();
	}

	/**
	 * Returns if diagram print range current was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getDlgDiagramPrintRangeCurrent() {
		return getDiagramPrintRangeCurrent();
	}

	/**
	 * Returns if diagram print range selection was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getDlgDiagramPrintRangeSelection() {
		return getDiagramPrintRangeSelection();
	}

	//one of the three is unnecssary, but it makes more sense
	/**
	 * Returns if print range all was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getDlgPrintRangeAll() {
		return getPrintRangeAll();
	}

	/**
	 * Returns if print range pages was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getDlgPrintRangePages() {
		return getPrintRangePages();
	}

	/**
	 * Returns if collate was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getDlgCollate() {
		return getCollate();
	}

	/**
	 * Returns the pages from int value, check if (getDlgPrintRangesPages())
	 * first.
	 * 
	 * @return int of the pages from box
	 */
	public int getDlgPagesFrom() {
		return getPagesFrom();
	}

	/**
	 * Returns the pages from int value, check if (getDlgPrintRangesPages())
	 * first.
	 * 
	 * @return int of the pages to box
	 */
	public int getDlgPagesTo() {
		return getPagesTo();
	}

	/**
	 * Returns the number of copies the user wants to print. This is nCopies
	 * from PRINTDLG
	 * 
	 * @return int with the number of copies
	 */
	public int getDlgNumberOfCopies() {
		return getNumberOfCopies();
	}
}