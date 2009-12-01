//*****************************************************************************
//
//	File:		DiagramPrint.cpp
//
//	Purpose:	Defines the initialization routines for the DLL.
//				In DiagramPrint.rc, you may have to change the icon line to				
//				ICON            "",1086,156,290,70,25,WS_GROUP
//
//	Author:		Wayne Diu, wdiu
//
//				
// Copyright (c) 2002 - 2005 IBM Corporation and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
//    IBM Corporation - initial API and implementation 
//
//******************************************************************************

#include "stdafx.h"
#include "DiagramPrint.h"
#include "WinPrintHelper.h"
#include <Afxtempl.h>

#define IDD_MODELER_PRINT_DIALOG 1538
#define IDD_MODELER_PRINT_DIALOG_RTL 1547
#define IDC_COLLATE 1041
#define IDC_COPIES 1154
#define IDC_PRINT_RANGE_ALL 1056
#define IDC_PRINT_RANGE_PAGES 1058
#define IDC_PRINT_RANGE_PAGES_START 1152
#define IDC_PRINT_RANGE_PAGES_END 1153
#define IDPROPERTIES 1025
//#include "Windows.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

//
//	Note!
//
//		If this DLL is dynamically linked against the MFC
//		DLLs, any functions exported from this DLL which
//		call into MFC must have the AFX_MANAGE_STATE macro
//		added at the very beginning of the function.
//
//		For example:
//
//		extern "C" BOOL PASCAL EXPORT ExportedFunction()
//		{
//			AFX_MANAGE_STATE(AfxGetStaticModuleState());
//			// normal function body here
//		}
//
//		It is very important that this macro appear in each
//		function, prior to any calls into MFC.  This means that
//		it must appear as the first statement within the 
//		function, even before any object variable declarations
//		as their constructors may generate calls into the MFC
//		DLL.
//
//		Please see MFC Technical Notes 33 and 58 for additional
//		details.
//

/////////////////////////////////////////////////////////////////////////////
// CDiagramPrintApp

BEGIN_MESSAGE_MAP(CDiagramPrintApp, CWinApp)
	//{{AFX_MSG_MAP(CDiagramPrintApp)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDiagramPrintApp construction

CDiagramPrintApp::CDiagramPrintApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}

/////////////////////////////////////////////////////////////////////////////
// The one and only CDiagramPrintApp object
CDiagramPrintApp theApp;
PRINTDLG pd;

//struct for stored values
class DiagramListItem {
public :
	CString string;
	bool selected;
};

//constants
const int WS_EX_LAYOUTRTL	= 0x00400000;
const int LAYOUT_RTL		= 0x00000001;


//stored values
CList<DiagramListItem, DiagramListItem> diagramListItems;
UINT uiPercent = 100, uiPagesM = 1, uiPagesN = 1, uiCopies = 1, uiPagesFrom = 1, uiPagesTo = 0;
bool bPrintRangeAll = false, bPrintRangePages = false;
bool bDiagramPrintRangeAll = false, bDiagramPrintRangeCurrent = false, bDiagramPrintRangeSelection = false;
bool bCollate = false;
bool bLandscape = false;
int  paperSizeIndex = 0; //see PageSetupPageType for page types
short paperWidth = 0, paperLength = 0; //for user defined paper size

//true when the dialog has been initialized.
bool bInitialized = false;

//controls
CListBox *pDiagrams = NULL;

//controls as windows
HWND hAll = NULL, hCurrent = NULL, hSelection = NULL, hDiagrams = NULL, hPages = NULL, hPagesM = NULL, hPagesN = NULL, hPercent = NULL, hPercentVal = NULL;
HWND hPRAll = NULL, hPRPages = NULL, hPRPagesStart = NULL, hPRPagesEnd = NULL;
HWND hwndOwner = NULL;

//Helper for converting jstring to wchar_t
wchar_t * convert(JNIEnv * env, jstring s) {
	//get the string and its length into original and len
	const jchar * original = env->GetStringChars(s, 0);
	const jsize len = env->GetStringLength(s);

	//allocate extra one for the null
	wchar_t * converted = new wchar_t[len+1];

	//copy from original into converted
	memcpy(converted, original, sizeof(wchar_t)*len);
	env->ReleaseStringChars(s, original);

	//null terminate it
	converted[len] = 0;

	return converted;
}


//Enable the print range
//Set enable to true to enable the print range, false to disable it.
void enablePrintRange(boolean enable = true) {
	EnableWindow(hPRAll, enable);
	EnableWindow(hPRPages, enable);
	EnableWindow(hPRPagesStart, enable);
	EnableWindow(hPRPagesEnd, enable);
}

//Enable the print range if only one diagram is selected
void enableDiagramPrintRangeSelectionIfOnlyOneDiagramIsSelected() {
	if (IsWindowEnabled(hSelection)) {
		if (pDiagrams->GetSelCount() == 1) {
			enablePrintRange();
			return;
		}
	}
	enablePrintRange(false);
}

//The print hook for the Windows print dialog template.
//Contains methods to check for messages when the user clicks
//on controls including the OK button.
UINT APIENTRY PrintHookProc(HWND hdlg, UINT uiMsg, WPARAM wParam, LPARAM lParam) {
	if (uiMsg == WM_INITDIALOG) {
		//save all, current, selection as hwnds
		hAll = GetDlgItem(hdlg, IDC_ALL_DIAGRAMS);
		ASSERT(hAll);
		hCurrent = GetDlgItem(hdlg, IDC_CURRENT_DIAGRAM);
		ASSERT(hCurrent);
		hSelection = GetDlgItem(hdlg, IDC_SELECTED_DIAGRAMS);
		ASSERT(hSelection);
		hPercent = GetDlgItem(hdlg, IDC_SCALE_PERCENT);
		ASSERT(hPercent);
		hPages = GetDlgItem(hdlg, IDC_SCALE_FIT);
		ASSERT(hPages);
		hPercentVal = GetDlgItem(hdlg, IDC_PERCENT);
		ASSERT(hPercentVal);
		hPagesM = GetDlgItem(hdlg, IDC_PAGESM);
		ASSERT(hPagesM);
		hPagesN = GetDlgItem(hdlg, IDC_PAGESN);
		ASSERT(hPagesN);

		hPRAll = GetDlgItem(hdlg, IDC_PRINT_RANGE_ALL);
		ASSERT(hPRAll);
		hPRPages = GetDlgItem(hdlg, IDC_PRINT_RANGE_PAGES);
		ASSERT(hPRPages);
		hPRPagesStart = GetDlgItem(hdlg, IDC_PRINT_RANGE_PAGES_START);
		ASSERT(hPRPagesStart);
		hPRPagesEnd = GetDlgItem(hdlg, IDC_PRINT_RANGE_PAGES_END);
		ASSERT(hPRPagesEnd);

		//diagrams print range group

		//select current diagram
		CheckDlgButton(hdlg, IDC_CURRENT_DIAGRAM, true);

		//diagrams listbox
		CWnd *wnd = CWnd::FromHandle(hdlg);
		pDiagrams = (CListBox*) wnd->GetDlgItem(IDC_DIAGRAMS);
		ASSERT(pDiagrams);
		hDiagrams = GetDlgItem(hdlg, IDC_DIAGRAMS);
		ASSERT(hDiagrams);

		//clear first, but should have nothing
		for (int i = 0; i < pDiagrams->GetCount(); i++) {
			pDiagrams->DeleteString(i);
		}

		//go through CList of diagram CStrings and add it
		
		// Make sure to handle width settings or there won't be a horizontal scorllbar
		CClientDC dc(pDiagrams);
		CFont* font = pDiagrams->GetFont();
		dc.SelectObject(font);
		CSize sz;
		int width = 0;
		for (i = 0; i < diagramListItems.GetCount(); i++) {
			pDiagrams->AddString(diagramListItems.GetAt(diagramListItems.FindIndex(i)).string);
     
			sz = dc.GetTextExtent(diagramListItems.GetAt(diagramListItems.FindIndex(i)).string, _tcslen(diagramListItems.GetAt(diagramListItems.FindIndex(i)).string));
			sz.cx += 3 * ::GetSystemMetrics(SM_CXBORDER);
			if(sz.cx > width) { 
				// Extend 
				width = sz.cx;
				pDiagrams->SetHorizontalExtent(width);
			} 
		}

		//disable listbox since all is selected first
		EnableWindow(hDiagrams, false);

		//enable print range group since current diagram is selected
		enablePrintRange(true);

		//scaling group

		//set perecent to 100
		SetDlgItemInt(hdlg, IDC_PERCENT, uiPercent, false);

		//set fit to pages to 1
		SetDlgItemInt(hdlg, IDC_PAGESM, uiPagesM, false);
		SetDlgItemInt(hdlg, IDC_PAGESN, uiPagesN, false);

		//select the uiPercent radio button
		CheckDlgButton(hdlg, IDC_SCALE_PERCENT, true);

		//disable pages since uiPercent is selected first
		EnableWindow(hPagesM, false);
		EnableWindow(hPagesN, false);

		//Even though something may fail, I will set initialized to true
		//because I will have asserted.  If I only set initialized to true
		//when all ASSERTs are OK, then I may prevent other code from running
		//at all which is worse than running with some errors.
		bInitialized = true;

    }
	else if (uiMsg == WM_COMMAND) { //user did something
		if (HIWORD(wParam) == BN_CLICKED) {
			ASSERT(bInitialized);
			//you shouldn't need reinterpret_cast, but I am making it explicit
			if ((reinterpret_cast<HWND>(lParam)) == GetDlgItem(hdlg, IDOK)) {
				//OK pressed here.  I can save the values that I cannot retrieve from
				//the PRINTDLG structured that's filled in for me.
				//These are custom vals.

				//save Print Range values
				bDiagramPrintRangeAll = IsDlgButtonChecked(hdlg, IDC_ALL_DIAGRAMS);
				bDiagramPrintRangeCurrent = IsDlgButtonChecked(hdlg, IDC_CURRENT_DIAGRAM);
				bDiagramPrintRangeSelection = IsDlgButtonChecked(hdlg, IDC_SELECTED_DIAGRAMS);

				//save the Scaling values
				if (IsDlgButtonChecked(hdlg, IDC_SCALE_PERCENT)) {
					uiPercent = GetDlgItemInt(hdlg, IDC_PERCENT, false, false);
					if (uiPercent == 0) { //failure or 0
						uiPercent = 100;
					}
					uiPagesM = -1;
					uiPagesN = -1;
				}
				else { //IDC_SCALE_FIT
					uiPercent = -1;
					uiPagesM = GetDlgItemInt(hdlg, IDC_PAGESM, false, false);
					if (uiPagesM == 0) { //failure or 0
						uiPagesM = 1;
					}
					uiPagesN = GetDlgItemInt(hdlg, IDC_PAGESN, false, false);
					if (uiPagesN == 0) { //failure or 0
						uiPagesN = 1;
					}
				}

				//save the bCollate value here
				//because PD_COLLATE does not always work
				bCollate = IsDlgButtonChecked(hdlg, IDC_COLLATE);

				//save the uiCopies value here
				//because nCopies does not always work
				uiCopies = GetDlgItemInt(hdlg, IDC_COPIES, false, false);

				//save the selected elements
				CWnd *wnd = CWnd::FromHandle(hdlg);
				CListBox *pDiagramz = (CListBox*) wnd->GetDlgItem(IDC_DIAGRAMS);

				for (int i = 0; i < diagramListItems.GetCount(); i++) {
					diagramListItems.GetAt(diagramListItems.FindIndex(i)).selected = pDiagramz->GetSel(i);
				}
			}
			else if ((reinterpret_cast<HWND>(lParam)) == hSelection) { //selection radio
				EnableWindow(hDiagrams, true);
				//check if print range should be enabled or disabled by checking
				//list of selections and making sure only one is selected
				enableDiagramPrintRangeSelectionIfOnlyOneDiagramIsSelected();
			}
			else if ((reinterpret_cast<HWND>(lParam)) == hCurrent) { //selection radio
				EnableWindow(hDiagrams, false);
				enablePrintRange();
			}
			else if ((reinterpret_cast<HWND>(lParam)) == hAll) { //selection radio
				EnableWindow(hDiagrams, false);
				enablePrintRange(false);
			}
			else if ((reinterpret_cast<HWND>(lParam)) == hPercent) { //selection radio
				EnableWindow(hPagesM, false);
				EnableWindow(hPagesN, false);
				EnableWindow(hPercentVal, true);
			}
			else if ((reinterpret_cast<HWND>(lParam)) == hPages) { //selection radio
				EnableWindow(hPagesM, true);
				EnableWindow(hPagesN, true);
				EnableWindow(hPercentVal, false);
			}
		}

		//selection changed message from diagrams listbox
		else if (LOWORD (wParam) == IDC_DIAGRAMS && HIWORD (wParam)== LBN_SELCHANGE ) {
			ASSERT(bInitialized);
			enableDiagramPrintRangeSelectionIfOnlyOneDiagramIsSelected();
		}
	}
	else if (uiMsg == WM_SHOWWINDOW) {
		//apply the settings only when the window shows, as the user 
		//may wish to specify different settings in the properties dialog

		DEVMODE* pDevMode; 

		pDevMode = (DEVMODE*)::GlobalLock(pd.hDevMode); 
		pDevMode->dmFields |= DM_ORIENTATION; 
		if (bLandscape)
			pDevMode->dmOrientation = DMORIENT_LANDSCAPE;
		else
			pDevMode->dmOrientation = DMORIENT_PORTRAIT;

		switch (paperSizeIndex) {
		case 0:
			pDevMode->dmPaperSize = DMPAPER_LETTER;
			break;
		case 1:
			pDevMode->dmPaperSize = DMPAPER_LEGAL;
			break;
		case 2:
			pDevMode->dmPaperSize = DMPAPER_EXECUTIVE;
			break;
		case 3:
			pDevMode->dmPaperSize = DMPAPER_TABLOID;
			break;
		case 4:
			pDevMode->dmPaperSize = DMPAPER_A3;
			break;
		case 5:
			pDevMode->dmPaperSize = DMPAPER_A4;
			break;
		case 6:
			pDevMode->dmPaperSize = DMPAPER_B4;
			break;
		case 7:
			pDevMode->dmPaperSize = DMPAPER_B5;
			break;
		case 8: //user defined
			pDevMode->dmPaperWidth = paperWidth;
			pDevMode->dmPaperLength= paperLength;
			break;
		default:
			pDevMode->dmPaperSize = DMPAPER_LETTER;
		}
			
		::GlobalUnlock(pd.hDevMode);
	}

	return 0L;
}

//Initialize scale fit to with M, N
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_initScaleFitTo
(JNIEnv *, jclass, jint m, jint n) {
	uiPagesM = m;
	uiPagesN = n;
}

//Initialize scale percent to with percent
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_initScalePercent
(JNIEnv *, jclass, jint p) {
	uiPercent = p;
}

//Add a diagram string.  Call reset before you insert the first one
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_addDiagramString
(JNIEnv * env, jclass, jstring diagramName) {
	//convert
	wchar_t *lpBuff = convert(env, diagramName);

	//add to a list of strings
	DiagramListItem item;
	item.string = CString(lpBuff);
	item.selected = false;
	diagramListItems.AddTail(item);
	delete [] lpBuff;
}

//Get the scale to fit M as integer.
JNIEXPORT jint JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getScaleFitToM
(JNIEnv *, jclass) {
	return uiPagesM;
}

//Get the scale to fit N as integer.
JNIEXPORT jint JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getScaleFitToN
(JNIEnv *, jclass) {
	return uiPagesN;
}

//Get the scale to percent as integer.
JNIEXPORT jint JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getScalePercent
(JNIEnv *, jclass){
	return uiPercent;
}

//Returns true if the diagram is selected, false if it wasn't
//Pass in the index starting from 0 of the diagram you want to check is selected.
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_isDiagramSelected
(JNIEnv *, jclass, jint index) {
	//check to see if the index in that list is selected
	//throw exception if out of range, and this is the desired behavior
	return diagramListItems.GetAt(diagramListItems.FindIndex(index)).selected;
}

//Returns if the Diagram Print Range radio button is All.
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getDiagramPrintRangeAll
(JNIEnv *, jclass) {
	return bDiagramPrintRangeAll;
}

//Returns if the Diagram Print Range radio button is Current.
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getDiagramPrintRangeCurrent
(JNIEnv *, jclass) {
	return bDiagramPrintRangeCurrent;
}

//Returns if the Diagram Print Range radio button is Selection
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getDiagramPrintRangeSelection
(JNIEnv *, jclass) {
	return bDiagramPrintRangeSelection;
}

//Returns if the default Print Range radio button is All
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getPrintRangeAll
(JNIEnv *, jclass) {
	return bPrintRangeAll;
}

//Returns if the default Print Range radio button is Pages
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getPrintRangePages
(JNIEnv *, jclass) {
	return bPrintRangePages;
}

//Returns true if the collate checkbox is checked, false if it isn't
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getCollate
(JNIEnv *, jclass) {
	return bCollate;
}

//Resets the dialog.  Call this before adding strings to initialize the dialog.
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_resetDialog
(JNIEnv *, jclass) {
	while (!diagramListItems.IsEmpty()) 
		diagramListItems.RemoveTail();
}

//Returns the start page to print from if you chose Pages in the default print range.
JNIEXPORT jint JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getPagesFrom
(JNIEnv *, jclass) {
	return uiPagesFrom;
}

//Returns the end page to print to if you chose Pages in the default print range.
JNIEXPORT jint JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getPagesTo
(JNIEnv *, jclass) {
	return uiPagesTo;
}

//Returns the number of copies to print.
JNIEXPORT jint JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_getNumberOfCopies
(JNIEnv *, jclass) {
	return uiCopies;
}

//Sets the owner's hWnd by the window class string and the title string so that it's modal
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_setHwndOwner
(JNIEnv * env, jclass, jstring windowClassString, jstring titleString) {
	wchar_t * windowClassWide = convert(env, windowClassString);
	wchar_t * titleWide = convert(env, titleString);

	//ok to be null, then it will have no owner and therefore not modal
	hwndOwner = FindWindow(windowClassWide, titleWide);

	if (hwndOwner == NULL) {
		//risk of getting the wrong window if multiple windows have the same title
		hwndOwner = FindWindow(NULL, titleWide);
	}

	delete [] windowClassWide;
	delete [] titleWide;
}

//Sets the orientation of the page
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_setOrientation
(JNIEnv * env, jclass, jboolean isLandscape) {
	bLandscape = isLandscape;
}

//Sets the paper size (e.g. A4, Letter, Legal, etc). Refer to org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageSetupPageType
JNIEXPORT void JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_setPaperSize
(JNIEnv * env, jclass, jint index, jdouble width, jdouble length) {
	paperSizeIndex = index;

	//width and length are in mm...but we need values in tenths of mm for DEVMODE...
	paperWidth  = (short)(width  * 10);
	paperLength = (short)(length * 10);
}



//Open the print dialog.
//Returns true if OK pressed, false if cancelled
//If OK is pressed and true is returned, you can use the get methods to
//get the values of the dialog.
JNIEXPORT jboolean JNICALL Java_org_eclipse_gmf_runtime_common_ui_printing_PrintHelper_open
(JNIEnv * env, jclass, jobject printerData) {

	// Initialize PRINTDLG
	ZeroMemory(&pd, sizeof(pd));
	pd.lStructSize = sizeof(pd);
	pd.hwndOwner   = hwndOwner;
	pd.hDevMode    = NULL;     //free later
	pd.hDevNames   = NULL;     //free later
	pd.Flags       = PD_ENABLEPRINTTEMPLATE | PD_ENABLEPRINTHOOK | PD_HIDEPRINTTOFILE | PD_COLLATE; 

	//make sure the print dialog is RTL-enabled if the calling workbench is also RTL...
	int ownerStyle = GetWindowLong(hwndOwner,GWL_EXSTYLE);

	if ((ownerStyle & WS_EX_LAYOUTRTL) == WS_EX_LAYOUTRTL)
		pd.lpPrintTemplateName = MAKEINTRESOURCE(IDD_MODELER_PRINT_DIALOG_RTL);
	else
		pd.lpPrintTemplateName = MAKEINTRESOURCE(IDD_MODELER_PRINT_DIALOG);

	pd.hInstance = theApp.m_hInstance; //AfxGetInstanceHandle();
	pd.lpfnPrintHook = PrintHookProc;
	pd.nCopies     = 1;
	pd.nFromPage   = 0xFFFF; 
	pd.nToPage     = 0xFFFF; 
	pd.nMinPage    = 1; 
	pd.nMaxPage    = 0xFFFF; 

	

	if (PrintDlg(&pd)==TRUE) { //OK pressed

		//I can get values that are not custom from the PRINTDLG structure
		//I got the other values when OK was pressed from the print hook

		//the bCollate flag does not always work
		//bCollate = (pd.Flags & PD_COLLATE);

		//the uiCopies does not always work
		//uiCopies = pd.nCopies;

		//save print range
		bPrintRangeAll = !((pd.Flags & PD_PAGENUMS) || (pd.Flags & PD_SELECTION));
		if (pd.Flags & PD_PAGENUMS) {
			bPrintRangePages = true;
			uiPagesFrom = pd.nFromPage;
			uiPagesTo = pd.nToPage;
		}
		else {
			bPrintRangePages = false;
			uiPagesFrom = -1;
			uiPagesTo = -1;
		}

		//set otherData from hDevMode
		jclass objectClass = env->GetObjectClass(printerData); 
		jfieldID id = env->GetFieldID(objectClass, "otherData", "[B");
		jbyteArray byteArray = env->NewByteArray((jsize) GlobalSize(pd.hDevMode));
		jbyte* byteArrayElements = env->GetByteArrayElements(byteArray, 0);
		DEVMODE * pDM = (DEVMODE *)GlobalLock(pd.hDevMode);

		MoveMemory(byteArrayElements, pDM, GlobalSize(pd.hDevMode));
		GlobalUnlock(pd.hDevMode);

		//byte copy done, save into java field
		env->SetObjectField(printerData, id, byteArray);
		env->ReleaseByteArrayElements(byteArray, byteArrayElements, 0);

	
		//Set the orientation 
		if((pDM->dmFields & DM_ORIENTATION) != 0) {		
			jfieldID orientationField = env->GetFieldID(objectClass, "orientation", "I");
			if(orientationField != NULL){
				//The value of DMORIENT_LANDSCAPE==2 and DMORIENT_PORTRAIT==1 
				//match up with PrinterData.LANDSCAPE==2 and PrinterData.PORTRAIT==1 on the java side.
				env->SetIntField(printerData, orientationField, pDM->dmOrientation);
			}
		}
		//set info from devNames
		DEVNAMES* pDN = (DEVNAMES*) GlobalLock(pd.hDevNames);
		env->SetObjectField(printerData, env->GetFieldID(objectClass, "driver", "Ljava/lang/String;"), env->NewString((LPWSTR)pDN + pDN->wDriverOffset,wcslen(((LPWSTR)pDN + pDN->wDriverOffset))));
		env->SetObjectField(printerData, env->GetFieldID(objectClass, "name", "Ljava/lang/String;"), env->NewString((LPWSTR)pDN + pDN->wDeviceOffset, wcslen(((LPWSTR)pDN + pDN->wDeviceOffset))));
		GlobalUnlock(pd.hDevNames);

		
		//free the hDevMode and hDevNames
		GlobalFree(pd.hDevMode);
		GlobalFree(pd.hDevNames);

		return true;
	}

	//free the hDevMode and hDevNames
	GlobalFree(pd.hDevMode);
	GlobalFree(pd.hDevNames);
	hwndOwner = NULL;

	return false;
}
