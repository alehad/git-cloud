package alehad.cloud.test.msg.res;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import alehad.cloud.test.msg.model.IMessageStore;
import alehad.cloud.test.msg.model.Message;

@Path("messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageRequestHandler {

	private IMessageStore messageStore = null;
	
	public MessageRequestHandler() {
		// no-op default implementation
		// need to clean up later
		MessageStoreFactory factory = new MessageStoreFactory();
		messageStore = factory.initializeMessageStore();
	}
	
	@GET
	public List<Message> getMessages() {
		return messageStore.getMessages();
	}

	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") int id) {
		return messageStore.getMessage(id);
	}

	@POST
	public Message addMessage(Message message) {
		return messageStore.createMessage(message);
	}

	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") int id, Message message) {
		return messageStore.updateMessage(id, message);
	}

	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") int id) {
		messageStore.deleteMessage(id);
	}
}
