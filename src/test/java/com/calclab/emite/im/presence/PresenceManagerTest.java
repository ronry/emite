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

package com.calclab.emite.im.presence;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.session.SessionReady;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.core.stanzas.Presence;
import com.calclab.emite.core.stanzas.Presence.Show;
import com.calclab.emite.im.presence.PresenceManager;
import com.calclab.emite.im.presence.PresenceManagerImpl;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.OwnPresenceChangedTestHandler;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class PresenceManagerTest {

	private PresenceManager manager;
	private XmppSessionTester session;

	@Before
	public void beforeTest() {
		session = new XmppSessionTester();
		final SessionReady sessionReady = new SessionReady(session);
		manager = new PresenceManagerImpl(new SimpleEventBus(), session, sessionReady);
	}

	@Test
	public void shouldBroadcastPresenceIfLoggedin() {
		session.setLoggedIn("myself@domain");
		final Presence newOwn = new Presence();
		newOwn.setStatus("this is my new status");
		newOwn.setShow(Show.away);
		manager.changeOwnPresence(newOwn);
		session.verifySent("<presence><show>away</show>" + "<status>this is my new status</status></presence>");
		final Presence current = manager.getOwnPresence();
		assertEquals(Show.away, current.getShow());
		assertEquals("this is my new status", current.getStatus());
	}

	@Test
	public void shouldEventOwnPresence() {
		session.setLoggedIn(uri("myself@domain"));
		final OwnPresenceChangedTestHandler handler = new OwnPresenceChangedTestHandler();
		manager.addOwnPresenceChangedHandler(handler);
		final Presence newOwn = new Presence();
		newOwn.setStatus("status");
		newOwn.setShow(Show.away);
		manager.changeOwnPresence(newOwn);
		assertTrue(handler.isCalledOnce());
		assertEquals("status", handler.getCurrentPresence().getStatus());
		assertEquals(Show.away, handler.getCurrentPresence().getShow());
	}

	@Test
	public void shouldHavePresenceEvenLoggedOut() {
		assertNotNull(manager.getOwnPresence());
	}

	@Test
	public void shouldResetOwnPresenceWhenLoggedOut() {
		session.setLoggedIn(uri("myself@domain"));
		final Presence newOwn = new Presence();
		newOwn.setStatus("status");
		newOwn.setShow(Show.away);
		manager.changeOwnPresence(newOwn);
		assertEquals("status", manager.getOwnPresence().getStatus());
		session.logout();
		assertEquals(Presence.Type.unavailable, manager.getOwnPresence().getType());
	}

	@Test
	public void shouldSendFinalPresence() {
		session.setLoggedIn(uri("myself@domain"));
		session.logout();
		session.verifySent("<presence from='myself@domain' type='unavailable' />");
	}

	@Test
	public void shouldSendInitialPresenceAfterRosterReady() {
		session.setLoggedIn("myself@domain");
		session.setStatus(SessionStatus.rosterReady);
		session.verifySent("<presence from='myself@domain'></presence>");
	}

	@Test
	public void shouldSendPresenceIfLoggedIn() {
		session.setLoggedIn(uri("myself@domain"));
		final Presence newOwn = new Presence();
		newOwn.setShow(Show.dnd);
		manager.changeOwnPresence(newOwn);
		session.verifySent("<presence><show>dnd</show></presence>");

	}

}
