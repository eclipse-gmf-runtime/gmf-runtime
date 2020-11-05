#!/bin/bash
# Copyright (c) 2012, 2016, 2019, 2020 IBM Corporation and others.
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#   IBM - Initial API and implementation

# Script needs 4-5 environment parameters:
# Hudson job name: ${JOB_NAME}
# Hudson build id: ${BUILD_ID}
# Hudson workspace: ${WORKSPACE}
# ${BUILD_TYPE}: n(ightly), m(aintenance), s(table), r(elease)
# ${BUILD_LABEL}: An optional label to append to the version string when creating drop files, e.g. M5 or RC1
#
set -e

export PROJECT_NAME="GMF Runtime"
export PROJECT_PREFIX="org.eclipse.gmf.runtime"
export SSH_ACCOUNT="genie.gmf-runtime@projects-storage.eclipse.org"

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

if [ -z "$BUILD_TYPE" ]; then
    echo "Error there is no BUILD_TYPE defined";
    exit 0
else
    buildType=$BUILD_TYPE
fi

if [ -z "$BUILD_LABEL" ]; then
    echo "Error there is no BUILD_LABEL defined";
    exit 0
else
    if [ "$BUILD_LABEL" != "NONE" ]; then
        dropFilesLabel=$BUILD_LABEL
    fi
fi

message() {
    echo "$(date +%Y-%m-%d-%H:%M:%S) $1"
}

# Determine the local update site we want to publish from
localTarget=${WORKSPACE}/org.eclipse.gmf.runtime.repository/target
localUpdateSite=${localTarget}/repository/
message "Using local update-site: $localUpdateSite"

# Determine remote update site we want to promote to:
# - integration and maintenance are published on interim site
# - stable builds on milestone site
# - release builds on releases site
case $buildType in
        m|M) remoteSite=maintenance ;;
        i|I) remoteSite=interim ;;
        s|S) remoteSite=milestones ;;
        r|R) remoteSite=releases ;;
        *) exit 0 ;;
esac

message "Publishing as $remoteSite (${buildType}) build"
remoteUpdateSiteBase="modeling/gmp/gmf-runtime/updates/$remoteSite"
remoteUpdateSite="/home/data/httpd/download.eclipse.org/$remoteUpdateSiteBase"
message "Publishing to remote update-site: $remoteUpdateSite"

if [ -z "$dropFilesLabel" -a "$buildType" != i  -a "$buildType" != I -a "$buildType" != r  -a "$buildType" != R ]; then
    echo "Please provide a drop files label to append to the version (e.g. M5, RC1) if this is not an I or R build."
    exit 0
fi

# Prepare a temp directory
tmpDir="$localTarget/$JOB_NAME-publish-tmp"
rm -fr $tmpDir
mkdir -p $tmpDir
cd $tmpDir
message "Working in $(pwd)"

# Compute dropDir
qualifiedVersion=$(find "$localUpdateSite"/features/ -maxdepth 1 | grep "org.eclipse.gmf.runtime_" | sed 's/.jar$//')
qualifiedVersion=${qualifiedVersion#*_}
qualifier=${qualifiedVersion##*.}
qualifier=${qualifier#v}
version=${qualifiedVersion%.*}
dropDir="$(echo "$buildType" | tr '[:lower:]' '[:upper:]')$qualifier"
message "dropDir is $dropDir"

# Prepare local update site (merging is performed later, if required)
stagedUpdateSite="updates/$remoteSite/$dropDir"
mkdir -p "$stagedUpdateSite"
cp -R "$localUpdateSite"/* "$stagedUpdateSite"
message "Copied $localUpdateSite to local directory $stagedUpdateSite."

# Append drop file suffix if one is specified
if [ -n "$dropFilesLabel" ]; then
    version=$version$dropFilesLabel
    message "version is now $version"
elif [ "$buildType" != r -a "$buildType" != R ]; then
    version="$(echo "$buildType" | tr '[:lower:]' '[:upper:]')$qualifier"
    message "version is now $version"
else
    message "version is now $version"
fi

# Publish the new repository
message "Publishing local $stagedUpdateSite directory to remote update site $remoteUpdateSite/$dropDir"
ssh "$SSH_ACCOUNT" mkdir -p $remoteUpdateSite
scp -r "$stagedUpdateSite" "$SSH_ACCOUNT:$remoteUpdateSite"

# Create/refresh the composite, with references only to the N=5 most recent builds (if nightly)

create_composite() {
    local name="$1"
    shift
    local location="$1"
    shift

    cat > "$location"/compositeArtifacts.xml <<EOF
<?xml version='1.0' encoding='UTF-8'?>
<?compositeArtifactRepository version='1.0.0'?>
<repository name='$name $version' type='org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository' version='1.0.0'>
  <properties size='1'>
    <property name='p2.timestamp' value='$P2_TIMESTAMP'/>
  </properties>
  <children size='$#'>
EOF

    cat > "$location"/compositeContent.xml <<EOF
<?xml version='1.0' encoding='UTF-8'?>
<?compositeMetadataRepository version='1.0.0'?>
<repository name='$name $version' type='org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository' version='1.0.0'>
  <properties size='1'>
    <property name='p2.timestamp' value='$P2_TIMESTAMP'/>
  </properties>
  <children size='$#'>
EOF

    for entry in "$@"; do
        echo "    <child location='$entry'/>" >> "$location"/compositeArtifacts.xml
        echo "    <child location='$entry'/>" >> "$location"/compositeContent.xml
    done

    cat >> "$location"/compositeArtifacts.xml <<EOF
  </children>
</repository>
EOF

    cat >> "$location"/compositeContent.xml <<EOF
  </children>
</repository>
EOF

}

entries=""
if ssh "$SSH_ACCOUNT" test -d "$remoteUpdateSite" ; then
    message "Add existing update sites to the composite update site repository file."
    if [ "$buildType" != "$R" ]; then
        for e in $(ssh "$SSH_ACCOUNT" find ${remoteUpdateSite}/ -mindepth 1 -maxdepth 1 -type d | sort | tail -n 5); do
            entries="$entries $(basename $e)"
        done
    else
        for e in $(ssh "$SSH_ACCOUNT" find ${remoteUpdateSite}/ -mindepth 1 -maxdepth 1 -type d | sort); do
            entries="$entries $(basename $e)"
        done
    fi
fi

message "Composite entries: $entries"
create_composite "GMF Runtime" . $entries
ssh "$SSH_ACCOUNT" rm -f ${remoteUpdateSite}/compositeArtifacts.jar ${remoteUpdateSite}/compositeContent.jar
ssh "$SSH_ACCOUNT" rm -f ${remoteUpdateSite}/compositeArtifacts.xml ${remoteUpdateSite}/compositeContent.xml
scp composite*xml "$SSH_ACCOUNT:${remoteUpdateSite}"
