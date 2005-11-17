/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.printing;

import java.util.List;
import java.util.Locale;

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
	 * Dynamic link library name without the extension
	 */
	private static final String DLL_NAME = "DiagramPrint"; //$NON-NLS-1$

	/**
	 * Country and language separator
	 */
	private static final String SEPARATOR = StringStatics.UNDER_SCORE;

	//to avoid throwing exceptions because of trying to load dlls for
	//unsupported locales, maintain an array of supported countries and
	//languages
	
	//in the array below, if there are multiple entries for the same
	//language, put the country specific one first
	
	/**
	 * Supported languages strings.  Default locale is SUPPORTED[0].
	 */
	private static final String SUPPORTED[] = {"en", "de", "es", "fr", "it", "ja", "ko", "pt_BR", "zh_CN", "zh_TW"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

	/**
	 * Default locale, English language
	 */
	private static final String DEFAULT_LOCALE = SUPPORTED[0];
	
	
	/*
	 * Load the dll
	 */
	static {
		
		//Although a far nicer way to do this dll loading is by using
		//Bundle-NativeCode in manifest.mf, I am not doing it that way
		//because of Bugzilla https://bugs.eclipse.org/bugs/show_bug.cgi?id=116497
		
		//When the Bugzilla has been fixed, this static block can be replaced
		//with one line of code
		//System.loadLibrary("DiagramPrint");

		//These lines would need to be added to the manifest.mf
		//Bundle-NativeCode: os/win32/x86/de/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; language=de,
		// os/win32/x86/es/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; language=es,
		// os/win32/x86/fr/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; language=fr,
		// os/win32/x86/it/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; language=it,
		// os/win32/x86/ja/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; language=ja,
		// os/win32/x86/ko/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; language=ko,
		// os/win32/x86/pt_BR/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; selection-filter = "(osgi.nl = pt_BR)",	
		// os/win32/x86/zh_CN/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; selection-filter = "(osgi.nl = zh_CN)",
		// os/win32/x86/zh_TW/DiagramPrint.dll ; osname=WindowsXP ; processor = x86 ; selection-filter = "(osgi.nl = zh_TW)",
		// os/win32/x86/en/DiagramPrint.dll ; osname=WindowsXP ; processor = x86		

		
		boolean success = false;
		String language = Locale.getDefault().getLanguage().toLowerCase();
		String country = Locale.getDefault().getCountry().toUpperCase();

		//try from most specific to least specific, following osgi
		//guidelines

		if (language != null) {

			String localizedVersion = (country != null) ? language + SEPARATOR + country : language;

			for (int i = 0; i < SUPPORTED.length; i++) {
				
				if (localizedVersion.equals(SUPPORTED[i])) {
					success = true;
				}
				else if (language.equals(SUPPORTED[i])) {
					//this check is redundant in the unlikely event that country
					//is null and language equals the localizedVersion
					localizedVersion = language;
					success = true;
				}
				
				if (success) {
					success = false;
					//Runtime.getRuntime().findLibrary() is protected,
					//surround loadLibrary() with try catch
					try {
						System.loadLibrary(DLL_NAME + SEPARATOR
							+ localizedVersion);
						success = true;
						break;
					} catch (UnsatisfiedLinkError ule) {
						//try without the country
						//loadedDLL is still false
						Trace.catching(PrintingPlugin.getDefault(),
							CommonPrintingDebugOptions.EXCEPTIONS_CATCHING,
							PrintHelper.class, "Link", ule); //$NON-NLS-1$
						Log.error(PrintingPlugin.getDefault(),
							CommonPrintingStatusCodes.RESOURCE_FAILURE,
							"Failed to load DiagramPrint dll for " //$NON-NLS-1$
								+ localizedVersion);
						Trace.throwing(PrintingPlugin.getDefault(),
							CommonPrintingDebugOptions.EXCEPTIONS_THROWING,
							PrintHelper.class, "Link", ule); //$NON-NLS-1$*/
					}
				}
			}
		}

		if (!success) {
			try {
				System.loadLibrary(DLL_NAME + SEPARATOR + DEFAULT_LOCALE);
			} catch (UnsatisfiedLinkError ule) {
				Trace.catching(PrintingPlugin.getDefault(),
					CommonPrintingDebugOptions.EXCEPTIONS_CATCHING,
					PrintHelper.class, "Link", ule); //$NON-NLS-1$
				Log.error(PrintingPlugin.getDefault(),
					CommonPrintingStatusCodes.RESOURCE_FAILURE,
					"Failed to load DiagramPrint_en.dll for language " + ((language == null) ? "null" : language) + " and country " + ((country == null) ? "null" : country), ule); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				Trace.throwing(PrintingPlugin.getDefault(),
					CommonPrintingDebugOptions.EXCEPTIONS_THROWING,
					PrintHelper.class, "Link", ule); //$NON-NLS-1$*/
				throw ule;
			}
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