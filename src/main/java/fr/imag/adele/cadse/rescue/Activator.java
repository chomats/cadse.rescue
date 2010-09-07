/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Copyright (C) 2006-2010 Adele Team/LIG/Grenoble University, France
 */
package fr.imag.adele.cadse.rescue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.LogicalWorkspace;
import fr.imag.adele.cadse.core.impl.CadseCore;
import fr.imag.adele.fede.workspace.si.persistence.Persistence;


public class Activator extends Plugin {

	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		Class<Item> item_clss = Item.class;
		try {
			item_clss.getMethod("isStatic");
			rescue_2();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			rescue_1();
		}
	}

	protected void rescue_2() throws IOException, FileNotFoundException {
		LogicalWorkspace wl = CadseCore.getLogicalWorkspace();

		Collection<Item> items = wl.getItems();
		File location_melusine = new File(getLocation(true), ".melusine");
		for (Item anItem : items) {
			if (!anItem.isRuntime() && anItem.getId() != null) {
				File itemFile = new File(location_melusine, anItem.getId().toString() + ".ser");
				System.out.println("[Rescue save] " + anItem.getId());
				Persistence.writeSer(anItem, itemFile);
			}
		}
	}

	protected void rescue_1() throws IOException, FileNotFoundException {
		LogicalWorkspace wl = CadseCore.getLogicalWorkspace();

		Collection<Item> items = wl.getItems();
		File location_melusine = new File(getLocation(true), ".melusine");
		for (Item anItem : items) {
			if (anItem instanceof ItemType) {
				continue;
			}
			if (anItem.getId() != null) {
				File itemFile = new File(location_melusine, anItem.getId().toString() + ".ser");
				System.out.println("[Rescue save] " + anItem.getId());
				Persistence.writeSer(anItem, itemFile);
			}
		}
	}

	public File getLocation(boolean wait) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getLocation().toFile();
	}

}
