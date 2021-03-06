package alehad.cloud.test.msg.model;

import java.util.List;

public interface IMessageStore {
	
	// basic CRUD operations
	
	public List<StoredMessage> getMessages();
	
	public Message getMessage(int id);
	
	public Message createMessage(Message msg);

	public Message updateMessage(int id, Message msg);

	public void deleteMessage(int id);
}
