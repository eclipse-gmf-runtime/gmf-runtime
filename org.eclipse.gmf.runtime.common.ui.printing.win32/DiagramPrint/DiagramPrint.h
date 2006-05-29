//*****************************************************************************
//
//	File:		DiagramPrint.h
//
//	Purpose:	main header file for the DiagramPrint DLL
//
//	Author:		Vishy Ramaswamy
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


#if !defined(AFX_DiagramPrint_H__B4FE3EB9_4E9B_497B_AD5D_E096E963F147__INCLUDED_)
#define AFX_DiagramPrint_H__B4FE3EB9_4E9B_497B_AD5D_E096E963F147__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CDiagramPrintApp
// See DiagramPrint.cpp for the implementation of this class
//

class CDiagramPrintApp : public CWinApp
{
public:
	CDiagramPrintApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDiagramPrintApp)
	//}}AFX_VIRTUAL

	//{{AFX_MSG(CDiagramPrintApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DiagramPrint_H__B4FE3EB9_4E9B_497B_AD5D_E096E963F147__INCLUDED_)
