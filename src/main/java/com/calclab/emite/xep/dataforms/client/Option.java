/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.dataforms.client;

import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;

/**
 * A XEP-0004 field option
 */
public class Option implements HasXML {

	private final XMLPacket xml;

	public Option() {
		xml = XMLBuilder.create("option").getXML();
	}

	protected Option(final XMLPacket xml) {
		this.xml = xml;
	}

	public String getLabel() {
		return xml.getAttribute("label");
	}

	public void setLabel(final String label) {
		xml.setAttribute("label", label);
	}

	public String getValue() {
		return xml.getChildText("value");
	}

	public void setValue(final String value) {
		xml.setChildText("value", value);
	}

	@Override
	public XMLPacket getXML() {
		return xml;
	}
}
