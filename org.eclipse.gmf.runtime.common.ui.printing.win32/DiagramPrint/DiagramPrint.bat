set PATH=C:\Program Files\Microsoft Visual Studio\Common\MSDev98\Bin;%PATH%
call VCVARS32.BAT
echo "Building Print Component"
msdev DiagramPrint.dsw /MAKE ALL /REBUILD
