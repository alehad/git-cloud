package alehad.cloud.test.msg.model;

import java.util.List;

public interface IMessageStore {
	
	// basic CRUD operations
	
	public List<Message> getMessages();
	
	public Message getMessage(long id);
	
	public Message createMessage(Message msg);

	public Message updateMessage(long id, Message msg);

	public void deleteMessage(long id);
}
