
import java.util.EventObject;

import pkjab.de.uni_passau.fim.pkjab.model.Contact;
import pkjab.de.uni_passau.fim.pkjab.model.messages.Message;

public class MessageReceivedEvent extends EventObject {
	
	private Message message;
	
	// message type?
	public MessageReceivedEvent(Contact source) {
		super(source);
	}
	
	public MessageReceivedEvent(Contact source, Message message) {
		super(source);
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public Contact getSource() {
		return (Contact) super.getSource();
	}
}
