package alehad.cloud.test.msg.db;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import alehad.cloud.test.msg.model.IMessageStore;
import alehad.cloud.test.msg.model.Message;
import alehad.cloud.test.msg.model.StoredMessage;

public class SimpleDatabase implements IMessageStore {

	private List<StoredMessage> messages = new ArrayList<StoredMessage>();
	
	private static SimpleDatabase instance;
	
	public static SimpleDatabase getInstance() {
		if (instance == null) {
			instance = new SimpleDatabase();
		}
		return instance;
	}

	private SimpleDatabase() {
		// initiate message list with couple of messages
		//messages.add(new Message("hello!"));
		//messages.add(new Message("hi there!"));
	}

	@Override
	public List<StoredMessage> getMessages() {
		return messages;
	}

	@Override
	public Message getMessage(int id) {
		Message message = null;
		ListIterator<StoredMessage> iterator = instance.messages.listIterator();
		int storedMsgId = 1; // naive implementation to support Message refactoring
		while (iterator.hasNext()) {
			message = iterator.next();
			if (storedMsgId++ == id) {
				break;
			}
		}
		//TODO: fix implementation, currently it will return last element if id not found
		return message;
	}

	@Override
	public Message createMessage(Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message updateMessage(int id, Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMessage(int id) {
		// TODO Auto-generated method stub
		
	}
}
