package com.calclab.emite.xfunctional.client.ui;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.xfunctional.client.ui.TestRunnerView.Level;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class TestRunnerLogic {

    public TestRunnerLogic(final TestRunnerPanel runner) {
	final Session session = Suco.get(Session.class);
	Connection connection = Suco.get(Connection.class);
	connection.onStanzaReceived(new Listener<IPacket>() {
	    @Override
	    public void onEvent(IPacket stanza) {
		runner.print(getLevel(session.getState()), "RECEIVED: " + stanza.toString());
	    }

	});

	connection.onStanzaSent(new Listener<IPacket>() {
	    @Override
	    public void onEvent(IPacket stanza) {
		runner.print(getLevel(session.getState()), "SENT: " + stanza.toString());
	    }
	});

	session.onStateChanged(new Listener0() {
	    @Override
	    public void onEvent() {
		runner.setSessionState(session.getState().toString());
	    }
	});
	runner.setSessionState(session.getState().toString());
	runner.setStatus("Click over a test to run.");
    }

    private Level getLevel(State state) {
	if (state == State.loggedIn || state == State.ready) {
	    return Level.stanzas;
	} else {
	    return Level.debug;
	}
    }
}