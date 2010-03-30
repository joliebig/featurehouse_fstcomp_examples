package de.uni_passau.fim.pkjab.model;

import de.uni_passau.fim.pkjab.model.messages.ChatStateMessage;
import de.uni_passau.fim.pkjab.util.ChatState;

class Contact {

	private ChatState myChatState = null;
	private boolean supportsChatStates = false;

	public void setMyChatState(ChatState chatState) {
		if (chatState == null) {
			throw new IllegalArgumentException();
		}
		
		if (supportsChatStates && (chatState != myChatState)) {
			myChatState = chatState;
			ChatStateMessage msg = new ChatStateMessage();
			msg.setTo(getBareJid());
			msg.setChatState(chatState);
			connection.sendMessage(msg);
		}
	}
	
 public TextMessage sendMessage(String text) {
		myChatState = ChatState.ACTIVE;
		return original(text);
	}
	
 public void setChatState(ChatState chatState) {
		supportsChatStates = true;
		original(chatState);
	}
}
