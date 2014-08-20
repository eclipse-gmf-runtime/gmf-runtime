#!/bin/sh
# Copyright (c) 2012, 2014 IBM Corporation and others.
# All rights reserved.   This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   IBM - Initial API and implementation

# Script may take 5-6 command line parameters:
# Hudson job name: ${JOB_NAME}
# Hudson build id: ${BUILD_ID}
# Hudson workspace: ${WORKSPACE}
# $1: Build type: n(ightly), m(aintenance), s(table), r(elease)
# $2: An optional label to append to the version string when creating drop files, e.g. M5 or RC1
# 
set -e

if [ $# -eq 1 -o $# -eq 2 ]; then
	buildType=$1
	if [ -n "$2" ]; then
		dropFilesLabel=$2
	fi
else
	echo "Usage: $0 [i | s | r | m] [qualifier]"
	echo "Example: $0 i"
	echo "Example: $0 s M7"
	exit 1
	if [ $# -ne 0 ]; then
		exit 1
	fi
fi

if [ -z "$JOB_NAME" ]; then
	echo "Error there is no Hudson JOB_NAME defined"; 
	exit 0
fi

if [ -z "$BUILD_ID" ]; then
	echo "Error there is no Hudson BUILD_ID defined"; 
	exit 0
fi

if [ -z "$WORKSPACE" ]; then
	echo "Error there is no Hudson WORKSPACE defined"; 
	exit 0
fi

# Determine the local update site we want to publish from
localTarget=${WORKSPACE}/org.eclipse.gmf.runtime.repository/target
localUpdateSite=${localTarget}/repository/
echo "`date +%Y-%m-%d-%H:%M:%S` Using local update-site: $localUpdateSite"

# Determine remote update site we want to promote to (integration and maintenance are published on interim site, stable builds on milestone site, release builds on releases site)
case $buildType in
	m|M) remoteSite=maintenance ;;
	i|I) remoteSite=interim ;;
	s|S) remoteSite=milestones ;;
	r|R) remoteSite=releases ;;
	*) exit 0 ;;
esac
echo "`date +%Y-%m-%d-%H:%M:%S` Publishing as $remoteSite ( $buildType ) build"
remoteUpdateSiteBase="modeling/gmp/gmf-runtime/updates/$remoteSite"
remoteUpdateSite="/home/data/httpd/download.eclipse.org/$remoteUpdateSiteBase"
echo "`date +%Y-%m-%d-%H:%M:%S` Publishing to remote update-site: $remoteUpdateSite"

if [ -z "$dropFilesLabel" -a "$buildType" != i ]; then
	echo "Please provide a drop files label to append to the version (e.g. M5, RC1) if this is not an I build."
	exit 0
fi

# Prepare a temp directory
tmpDir="$localTarget/$JOB_NAME-publish-tmp"
rm -fr $tmpDir
mkdir -p $tmpDir
cd $tmpDir
echo "`date +%Y-%m-%d-%H:%M:%S` Working in `pwd`"

# Download and prepare Eclipse SDK, which is needed to process the update site
echo "`date +%Y-%m-%d-%H:%M:%S` Downloading eclipse to $PWD"
cp /home/data/httpd/download.eclipse.org/eclipse/downloads/drops4/R-4.4-201406061215/eclipse-SDK-4.4-linux-gtk-x86_64.tar.gz .
tar -xzf eclipse-SDK-4.4-linux-gtk-x86_64.tar.gz
cd eclipse
chmod 700 eclipse
cd ..
if [ ! -d "eclipse" ]; then
	echo "Failed to download an Eclipse SDK, being needed for provisioning."
	exit
fi
# Prepare Eclipse SDK to provide WTP releng tools (used to postprocess repository, i.e set p2.mirrorsURL property)
echo "`date +%Y-%m-%d-%H:%M:%S` Installing WTP Releng tools"
./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.equinox.p2.director -repository http://download.eclipse.org/webtools/releng/repository/ -installIUs org.eclipse.wtp.releng.tools.feature.feature.group
# Clean up
rm eclipse-SDK-4.4-linux-gtk-x86_64.tar.gz

# Generate drop files
echo "`date +%Y-%m-%d-%H:%M:%S` Converting update site to runnable form"
./eclipse/eclipse -nosplash -consoleLog -application org.eclipse.equinox.p2.repository.repo2runnable -source file:$localUpdateSite -destination file:drops/eclipse
qualifiedVersion=$(find $localUpdateSite/features/ -maxdepth 1 | grep "org.eclipse.gmf_" | sed 's/.jar$//')
echo "`date +%Y-%m-%d-%H:%M:%S` qualifiedVersion is $qualifiedVersion"
qualifiedVersion=${qualifiedVersion#*_}
echo "`date +%Y-%m-%d-%H:%M:%S` qualifiedVersion is $qualifiedVersion"
qualifier=${qualifiedVersion##*.}
echo "`date +%Y-%m-%d-%H:%M:%S` qualifier is $qualifier"
qualifier=${qualifier#v}
echo "`date +%Y-%m-%d-%H:%M:%S` qualifier is $qualifier"
version=${qualifiedVersion%.*}
echo "`date +%Y-%m-%d-%H:%M:%S` version is $version"
dropDir="$(echo $buildType | tr '[:lower:]' '[:upper:]')$qualifier"
echo "`date +%Y-%m-%d-%H:%M:%S` dropDir is $dropDir"
localDropDir=drops/$version/$dropDir
echo "`date +%Y-%m-%d-%H:%M:%S` Creating drop files in local directory $tmpDir/$localDropDir"
mkdir -p $localDropDir

# Prepare local update site (merging is performed later, if required)
stagedUpdateSite="updates/$remoteSite/$dropDir"
mkdir -p $stagedUpdateSite
cp -R $localUpdateSite/* $stagedUpdateSite
echo "`date +%Y-%m-%d-%H:%M:%S` Copied $localUpdateSite to local directory $stagedUpdateSite."

# Append drop file suffix if one is specified				
if [ -n "$dropFilesLabel" ]; then
	version=$version$dropFilesLabel
	echo "`date +%Y-%m-%d-%H:%M:%S` version is now $version"
elif [ "$buildType" != r -a "$buildType" != R ]; then
	version="$(echo $buildType | tr '[:lower:]' '[:upper:]')$qualifier"
	echo "`date +%Y-%m-%d-%H:%M:%S` version is now $version"
else
	echo "`date +%Y-%m-%d-%H:%M:%S` version is now $version"
fi
				
cp eclipse/epl-v10.html drops/eclipse
cp eclipse/notice.html drops/eclipse
cd drops

# gmf-runtime runtime
zip -r ../$localDropDir/gmf-runtime-$version.zip \
	eclipse/epl-v10.html eclipse/notice.html \
	eclipse/features/org.eclipse.gmf_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.action_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.action.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.action_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.dnd_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.dnd.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.properties_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.actions_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.dnd_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.geoshapes_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.properties_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.providers_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.providers.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.render_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.resources.editor_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.render_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.render.awt_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.clipboard.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.commands.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.type.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.type.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.ui.properties_* \
	eclipse/plugins/org.eclipse.gmf.runtime.gef.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.providers_* \
	eclipse/plugins/org.eclipse.gmf_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.printing_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.printing.win32_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.printing_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.printing.render_* \
	eclipse/features/org.eclipse.gmf.runtime.thirdparty_* \
	eclipse/plugins/org.apache.batik.dom.svg_* \
	eclipse/plugins/org.apache.batik.bridge_* \
	eclipse/plugins/org.apache.batik.dom_* \
	eclipse/plugins/org.apache.batik.ext.awt_* \
	eclipse/plugins/org.apache.batik.transcoder_* \
	eclipse/plugins/org.apache.batik.svggen_* \
	eclipse/plugins/org.apache.batik.util_* \
	eclipse/plugins/org.w3c.dom.svg_* \
	eclipse/plugins/org.apache.xerces_* \
	eclipse/plugins/org.apache.xml.resolver_* \
	eclipse/plugins/org.apache.batik.css_* \
	eclipse/plugins/org.apache.batik.util.gui_* \
	eclipse/plugins/org.apache.batik.parser_* \
	eclipse/plugins/org.apache.batik.xml_* \
	eclipse/plugins/org.w3c.css.sac_* \
	eclipse/plugins/org.w3c.dom.smil_* \
	eclipse/plugins/javax.xml_* \
	eclipse/plugins/org.apache.xml.serializer_* \
	eclipse/plugins/org.apache.batik.pdf_* \
	eclipse/plugins/org.w3c.dom.events_* \
	eclipse/features/org.eclipse.gmf.runtime.notation_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.edit_*
md5sum ../$localDropDir/gmf-runtime-$version.zip > ../$localDropDir/gmf-runtime-$version.zip.md5
echo "`date +%Y-%m-%d-%H:%M:%S` Created gmf-runtime-$version.zip"
			
# gmf-runtime SDK
zip -r ../$localDropDir/gmf-sdk-runtime-$version.zip \
	eclipse/epl-v10.html eclipse/notice.html \
	eclipse/features/org.eclipse.gmf_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.action_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.action.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.action_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.dnd_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.dnd.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.properties_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.actions_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.dnd_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.geoshapes_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.properties_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.providers_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.providers.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.render_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.resources.editor_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.render_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.render.awt_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.clipboard.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.commands.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.type.core_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.type.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.ui.properties_* \
	eclipse/plugins/org.eclipse.gmf.runtime.gef.ui_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.providers_* \
	eclipse/plugins/org.eclipse.gmf_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.printing_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.printing.win32_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.printing_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.printing.render_* \
	eclipse/features/org.eclipse.gmf.runtime.thirdparty_* \
	eclipse/plugins/org.apache.batik.dom.svg_* \
	eclipse/plugins/org.apache.batik.bridge_* \
	eclipse/plugins/org.apache.batik.dom_* \
	eclipse/plugins/org.apache.batik.ext.awt_* \
	eclipse/plugins/org.apache.batik.transcoder_* \
	eclipse/plugins/org.apache.batik.svggen_* \
	eclipse/plugins/org.apache.batik.util_* \
	eclipse/plugins/org.w3c.dom.svg_* \
	eclipse/plugins/org.apache.xerces_* \
	eclipse/plugins/org.apache.xml.resolver_* \
	eclipse/plugins/org.apache.batik.css_* \
	eclipse/plugins/org.apache.batik.util.gui_* \
	eclipse/plugins/org.apache.batik.parser_* \
	eclipse/plugins/org.apache.batik.xml_* \
	eclipse/plugins/org.w3c.css.sac_* \
	eclipse/plugins/org.w3c.dom.smil_* \
	eclipse/plugins/javax.xml_* \
	eclipse/plugins/org.apache.xml.serializer_* \
	eclipse/plugins/org.apache.batik.pdf_* \
	eclipse/plugins/org.w3c.dom.events_* \
	eclipse/features/org.eclipse.gmf.runtime.notation_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.edit_* \
	eclipse/features/org.eclipse.gmf.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.core.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.action.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.action.ide.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.action.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.dnd.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.dnd.ide.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.services.properties.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.core.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.actions.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.dnd.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.geoshapes.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.properties.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.providers.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.providers.ide.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.render.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.resources.editor.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.render.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.draw2d.ui.render.awt.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.clipboard.core.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.commands.core.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.core.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.type.core.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.type.ui.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.ui.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.emf.ui.properties.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.gef.ui.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.providers.source_* \
	eclipse/plugins/org.eclipse.gmf.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.printing.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.common.ui.printing.win32.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.printing.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.diagram.ui.printing.render.source_* \
	eclipse/features/org.eclipse.gmf.runtime.thirdparty.source_* \
	eclipse/plugins/org.apache.batik.dom.svg.source_* \
	eclipse/plugins/org.apache.batik.bridge.source_* \
	eclipse/plugins/org.apache.batik.dom.source_* \
	eclipse/plugins/org.apache.batik.ext.awt.source_* \
	eclipse/plugins/org.apache.batik.transcoder.source_* \
	eclipse/plugins/org.apache.batik.svggen.source_* \
	eclipse/plugins/org.apache.batik.util.source_* \
	eclipse/plugins/org.w3c.dom.svg.source_* \
	eclipse/plugins/org.apache.xerces.source_* \
	eclipse/plugins/org.apache.xml.resolver.source_* \
	eclipse/plugins/org.apache.batik.css.source_* \
	eclipse/plugins/org.apache.batik.util.gui.source_* \
	eclipse/plugins/org.apache.batik.parser.source_* \
	eclipse/plugins/org.apache.batik.xml.source_* \
	eclipse/plugins/org.w3c.css.sac.source_* \
	eclipse/plugins/org.w3c.dom.smil.source_* \
	eclipse/plugins/javax.xml.source_* \
	eclipse/plugins/org.apache.xml.serializer.source_* \
	eclipse/plugins/org.apache.batik.pdf.source_* \
	eclipse/plugins/org.w3c.dom.events.source_* \
	eclipse/features/org.eclipse.gmf.runtime.notation.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.source_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.edit.source_* \
	eclipse/features/org.eclipse.gmf.runtime.notation.sdk_* \
	eclipse/plugins/org.eclipse.gmf.runtime.notation.sdk_* \
	eclipse/features/org.eclipse.gmf.runtime.sdk_* \
	eclipse/plugins/org.eclipse.gmf.runtime.sdk_* \
	eclipse/features/org.eclipse.gmf.examples.runtime.ui.pde_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.ui.pde_* 
md5sum ../$localDropDir/gmf-sdk-runtime-$version.zip > ../$localDropDir/gmf-sdk-runtime-$version.zip.md5
echo "`date +%Y-%m-%d-%H:%M:%S` Created gmf-sdk-runtime-$version.zip"
			
# gmf-runtime examples
zip -r ../$localDropDir/gmf-examples-runtime-$version.zip \
	eclipse/epl-v10.html eclipse/notice.html \
	eclipse/features/org.eclipse.gmf.examples.runtime_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.common.service_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.decorator_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.layout_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.emf.clipboard_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.geoshapes_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.logic_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.logic.model_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.logic.model.edit_* \
	eclipse/plugins/org.eclipse.gmf.examples.runtime.diagram.logic.model.editor_* 
md5sum ../$localDropDir/gmf-examples-runtime-$version.zip > ../$localDropDir/gmf-examples-runtime-$version.zip.md5
echo "`date +%Y-%m-%d-%H:%M:%S` Created gmf-examples-runtime-$version.zip"
			
# gmf-runtime automated-tests
zip -r ../$localDropDir/gmf-tests-runtime-$version.zip \
	eclipse/epl-v10.html eclipse/notice.html \
	eclipse/features/org.eclipse.gmf.tests.runtime_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.common.core_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.common.ui_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.common.ui.services_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.common.ui.services.action_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.common.ui.services.provider_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.diagram.ui_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.draw2d.ui_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.draw2d.ui.render_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.clipboard.core_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.commands.core_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.core_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.type.core_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.type.ui_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.ui_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.emf.ui.properties_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime-feature_* \
	eclipse/plugins/org.eclipse.gmf.tests.runtime.gef.ui_*
md5sum ../$localDropDir/gmf-tests-runtime-$version.zip > ../$localDropDir/gmf-tests-runtime-$version.zip.md5
echo "`date +%Y-%m-%d-%H:%M:%S` Created gmf-tests-runtime-$version.zip"
	
cd $tmpDir
		
cd $stagedUpdateSite
zip -r ../../../$localDropDir/gmf-runtime-Update-$version.zip features plugins binary artifacts.jar content.jar
md5sum ../../../$localDropDir/gmf-runtime-Update-$version.zip > ../../../$localDropDir/gmf-runtime-Update-$version.zip.md5
echo "`date +%Y-%m-%d-%H:%M:%S` Created gmf-runtime-Update-Site-$version.zip"

cd $tmpDir

#generating build.cfg file to be referenced from downloads web page
echo "hudson.job.name=${JOB_NAME}" > $localDropDir/build.cfg
echo "hudson.job.id=${BUILD_NUMBER} (${jobDir##*/})" >> $localDropDir/build.cfg
echo "hudson.job.url=${BUILD_URL}" >> $localDropDir/build.cfg

remoteDropDir=/home/data/httpd/download.eclipse.org/modeling/gmp/gmf-runtime/downloads/$localDropDir
mkdir -p $remoteDropDir
cp -R $localDropDir/* $remoteDropDir/
echo "`date +%Y-%m-%d-%H:%M:%S` Published drop files in local directory $tmpDir/$localDropDir to $remoteDropDir"

# Ensure p2.mirrorURLs property is used in update site
echo "`date +%Y-%m-%d-%H:%M:%S` Setting p2.mirrorsURL to http://www.eclipse.org/downloads/download.php?format=xml&file=/$remoteUpdateSiteBase"
./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.wtp.releng.tools.addRepoProperties -vmargs -DartifactRepoDirectory=$PWD/$stagedUpdateSite -Dp2MirrorsURL="http://www.eclipse.org/downloads/download.php?format=xml&file=/$remoteUpdateSiteBase"

# Create p2.index file
if [ ! -e "$stagedUpdateSite/p2.index" ]; then
	echo "`date +%Y-%m-%d-%H:%M:%S` Creating p2.index file"
	echo "version = 1" > $stagedUpdateSite/p2.index
	echo "metadata.repository.factory.order = content.xml,\!" >> $stagedUpdateSite/p2.index
	echo "artifact.repository.factory.order = artifacts.xml,\!" >> $stagedUpdateSite/p2.index
fi

# Backup then clean remote update site
if [ -d "$remoteUpdateSite" ]; then
	echo "`date +%Y-%m-%d-%H:%M:%S` Creating backup of remote update site $remoteUpdateSite to $tmpDir/BACKUP."
	if [ -d $tmpDir/BACKUP ]; then
		rm -fr $tmpDir/BACKUP
	fi
	mkdir $tmpDir/BACKUP
	cp -R $remoteUpdateSite $tmpDir/BACKUP
	rm -fr $remoteUpdateSite
fi

echo "`date +%Y-%m-%d-%H:%M:%S` Publishing local $stagedUpdateSite directory to remote update site $remoteUpdateSite/$dropDir"
mkdir -p $remoteUpdateSite
cp -R $stagedUpdateSite $remoteUpdateSite

# Create the composite update site
cat > p2.composite.repository.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project name="p2 composite repository">
<target name="default">
<p2.composite.repository>
<repository compressed="true" location="${remoteUpdateSite}" name="${JOB_NAME}"/>
<add>
<repository location="${dropDir}"/>
</add>
</p2.composite.repository>
</target>
</project>
EOF

echo "`date +%Y-%m-%d-%H:%M:%S` Update the composite update site"
./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.ant.core.antRunner -buildfile p2.composite.repository.xml default

# Clean up
echo "`date +%Y-%m-%d-%H:%M:%S` Cleaning up"
#rm -fr eclipse
#rm -fr update-site
