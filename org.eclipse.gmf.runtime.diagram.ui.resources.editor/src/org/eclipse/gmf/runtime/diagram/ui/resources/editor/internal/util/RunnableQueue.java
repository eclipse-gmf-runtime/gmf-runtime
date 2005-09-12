/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 */
public class RunnableQueue {
	
	static private final List queue = new ArrayList(4);
	static private boolean running = false;
	
	static public void addRunnable(Runnable r) {
		synchronized(queue) {
			queue.add(r);
		}
	}
	
	static public void addRunnableToHead(Runnable r) {
		synchronized(queue) {
			queue.add(0, r);
		}
	}
	
	static public Runnable runner = new Runnable() {
		public void run() {
			if (running)
				return;
			running = true;
				
			try {
				while (true) {
					int size = 0;
					synchronized (queue) {
						size = queue.size();
					}
					
					if (size == 0)
						break;
						
					Runnable r = null;
					synchronized (queue) { 
						r = (Runnable) queue.get(0);
						queue.remove(0);	
					}
					r.run();
				}
			}
			finally {
				running = false;
			}
		}
	};
}
