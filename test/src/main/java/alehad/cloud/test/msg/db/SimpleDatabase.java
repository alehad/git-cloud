package alehad.cloud.test.msg.db;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import alehad.cloud.test.msg.model.IMessageStore;
import alehad.cloud.test.msg.model.Message;

public class SimpleDatabase implements IMessageStore {

	private List<Message> messages = new ArrayList<Message>();
	
	private static SimpleDatabase instance;
	
	public static SimpleDatabase getInstance() {
		if (instance == null) {
			instance = new SimpleDatabase();
		}
		return instance;
	}

	private SimpleDatabase() {
		// initiate message list with couple of messages
		messages.add(new Message(1, "hello!"));
		messages.add(new Message(2, "hi there!"));
	}

	@Override
	public List<Message> getMessages() {
		return messages;
	}

	@Override
	public Message getMessage(long id) {
		Message message = null;
		ListIterator<Message> iterator = instance.messages.listIterator();
		while (iterator.hasNext()) {
			message = iterator.next();
			if (message.getId() == id) {
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
	public Message updateMessage(long id, Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMessage(long id) {
		// TODO Auto-generated method stub
		
	}
}
