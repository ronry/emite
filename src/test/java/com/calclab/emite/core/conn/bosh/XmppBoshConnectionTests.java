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

package com.calclab.emite.core.conn.bosh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.conn.ConnectionSettings;
import com.calclab.emite.core.conn.XmppConnectionBosh;
import com.calclab.emite.xtesting.ServicesTester;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class XmppBoshConnectionTests {

	private final ServicesTester services;
	private final XmppConnectionBosh connection;

	public XmppBoshConnectionTests() {
		services = new ServicesTester();
		connection = new XmppConnectionBosh(new SimpleEventBus());
	}

	@Test
	public void shouldSendInitialBody() {
		connection.setSettings(new ConnectionSettings("localhost"));
		connection.connect();
		assertEquals(1, services.requestSentCount());
		final IsPacketLike matcher = IsPacketLike.build("<body to='localhost' " + "content='text/xml; charset=utf-8' xmlns:xmpp='urn:xmpp:xbosh' "
				+ " ack='1' hold='1' secure='true' xml:lang='en' " + "xmpp:version='1.0' wait='60' xmlns='http://jabber.org/protocol/httpbind' />");
		assertTrue(matcher.matches(services.getSentPacket(0), System.out));
	}
}
