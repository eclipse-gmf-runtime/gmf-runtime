#!/bin/sh
unset GTK_IM_MODULE
mvn -f org.eclipse.gmf.runtime.releng/pom.xml clean verify
