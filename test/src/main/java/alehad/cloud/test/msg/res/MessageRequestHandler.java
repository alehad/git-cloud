package alehad.cloud.test.msg.res;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import alehad.cloud.test.msg.model.Message;

@Path("messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageRequestHandler {

	public MessageRequestHandler() {
		// no-op default implementation
	}
	
	@GET
	public Message getMessages() {
		// naive implementation
		return new Message(0, "no messages to give");
	}

	public Message getMessage(long id) {
		// naive implementation
		return new Message(id, "test message " + id);
	}
}
