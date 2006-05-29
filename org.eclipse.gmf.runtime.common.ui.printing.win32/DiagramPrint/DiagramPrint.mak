# Microsoft Developer Studio Generated NMAKE File, Based on DiagramPrint.dsp
!IF "$(CFG)" == ""
CFG=DiagramPrint - Win32 Debug
!MESSAGE No configuration specified. Defaulting to DiagramPrint - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "DiagramPrint - Win32 Release" && "$(CFG)" != "DiagramPrint - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "DiagramPrint.mak" CFG="DiagramPrint - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "DiagramPrint - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "DiagramPrint - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

!IF  "$(CFG)" == "DiagramPrint - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\Release
# End Custom Macros

ALL : "$(OUTDIR)\DiagramPrint.dll"


CLEAN :
	-@erase "$(INTDIR)\DiagramPrint.obj"
	-@erase "$(INTDIR)\DiagramPrint.pch"
	-@erase "$(INTDIR)\DiagramPrint.res"
	-@erase "$(INTDIR)\StdAfx.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(OUTDIR)\DiagramPrint.dll"
	-@erase "$(OUTDIR)\DiagramPrint.exp"
	-@erase "$(OUTDIR)\DiagramPrint.lib"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_AFXDLL" /D "_UNICODE" /D "_USRDLL" /Fp"$(INTDIR)\DiagramPrint.pch" /Yu"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /win32 
RSC=rc.exe
RSC_PROJ=/l 0x409 /fo"$(INTDIR)\DiagramPrint.res" /d "NDEBUG" /d "_AFXDLL" 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\DiagramPrint.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=/nologo /subsystem:windows /dll /incremental:no /pdb:"$(OUTDIR)\DiagramPrint.pdb" /machine:I386 /def:".\DiagramPrint.def" /out:"$(OUTDIR)\DiagramPrint.dll" /implib:"$(OUTDIR)\DiagramPrint.lib" 
DEF_FILE= \
	".\DiagramPrint.def"
LINK32_OBJS= \
	"$(INTDIR)\DiagramPrint.obj" \
	"$(INTDIR)\StdAfx.obj" \
	"$(INTDIR)\DiagramPrint.res"

"$(OUTDIR)\DiagramPrint.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "DiagramPrint - Win32 Debug"

OUTDIR=.\Debug
INTDIR=.\Debug
# Begin Custom Macros
OutDir=.\Debug
# End Custom Macros

ALL : "$(OUTDIR)\DiagramPrint.dll"


CLEAN :
	-@erase "$(INTDIR)\DiagramPrint.obj"
	-@erase "$(INTDIR)\DiagramPrint.pch"
	-@erase "$(INTDIR)\DiagramPrint.res"
	-@erase "$(INTDIR)\StdAfx.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\DiagramPrint.dll"
	-@erase "$(OUTDIR)\DiagramPrint.exp"
	-@erase "$(OUTDIR)\DiagramPrint.ilk"
	-@erase "$(OUTDIR)\DiagramPrint.lib"
	-@erase "$(OUTDIR)\DiagramPrint.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MDd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_AFXDLL" /D "_UNICODE" /D "_USRDLL" /Fp"$(INTDIR)\DiagramPrint.pch" /Yu"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /win32 
RSC=rc.exe
RSC_PROJ=/l 0x409 /fo"$(INTDIR)\DiagramPrint.res" /d "_DEBUG" /d "_AFXDLL" 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\DiagramPrint.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=/nologo /subsystem:windows /dll /incremental:yes /pdb:"$(OUTDIR)\DiagramPrint.pdb" /debug /machine:I386 /def:".\DiagramPrint.def" /out:"$(OUTDIR)\DiagramPrint.dll" /implib:"$(OUTDIR)\DiagramPrint.lib" /pdbtype:sept 
DEF_FILE= \
	".\DiagramPrint.def"
LINK32_OBJS= \
	"$(INTDIR)\DiagramPrint.obj" \
	"$(INTDIR)\StdAfx.obj" \
	"$(INTDIR)\DiagramPrint.res"

"$(OUTDIR)\DiagramPrint.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("DiagramPrint.dep")
!INCLUDE "DiagramPrint.dep"
!ELSE 
!MESSAGE Warning: cannot find "DiagramPrint.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "DiagramPrint - Win32 Release" || "$(CFG)" == "DiagramPrint - Win32 Debug"
SOURCE=.\DiagramPrint.cpp

"$(INTDIR)\DiagramPrint.obj" : $(SOURCE) "$(INTDIR)" "$(INTDIR)\DiagramPrint.pch"


SOURCE=.\DiagramPrint.rc

"$(INTDIR)\DiagramPrint.res" : $(SOURCE) "$(INTDIR)"
	$(RSC) $(RSC_PROJ) $(SOURCE)


SOURCE=.\StdAfx.cpp

!IF  "$(CFG)" == "DiagramPrint - Win32 Release"

CPP_SWITCHES=/nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_AFXDLL" /D "_UNICODE" /D "_USRDLL" /Fp"$(INTDIR)\DiagramPrint.pch" /Yc"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

"$(INTDIR)\StdAfx.obj"	"$(INTDIR)\DiagramPrint.pch" : $(SOURCE) "$(INTDIR)"
	$(CPP) @<<
  $(CPP_SWITCHES) $(SOURCE)
<<


!ELSEIF  "$(CFG)" == "DiagramPrint - Win32 Debug"

CPP_SWITCHES=/nologo /MDd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_AFXDLL" /D "_UNICODE" /D "_USRDLL" /Fp"$(INTDIR)\DiagramPrint.pch" /Yc"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

"$(INTDIR)\StdAfx.obj"	"$(INTDIR)\DiagramPrint.pch" : $(SOURCE) "$(INTDIR)"
	$(CPP) @<<
  $(CPP_SWITCHES) $(SOURCE)
<<


!ENDIF 


!ENDIF 

