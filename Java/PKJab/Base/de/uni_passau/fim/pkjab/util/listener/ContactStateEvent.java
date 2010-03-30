package de.uni_passau.fim.pkjab.util.listener;

import java.util.EventObject;

import de.uni_passau.fim.pkjab.model.Contact;
import de.uni_passau.fim.pkjab.util.UserState;

public class ContactStateEvent extends EventObject {
	
	public ContactStateEvent(Contact source, UserState state) {
		super(source);
	}
	
	public UserState getState() {
		return getSource().getState();
	}

	public Contact getSource() {
		return (Contact)source;
	}
}
