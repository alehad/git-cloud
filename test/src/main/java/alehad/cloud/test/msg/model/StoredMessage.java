package alehad.cloud.test.msg.model;

public class StoredMessage extends Message implements Comparable <StoredMessage> {
    
    private Long 	msgId;
	
	public StoredMessage() {
		//no-op default constructor
	}

	public StoredMessage(Long msgId, String message, String author) {
        super(message, author);
        this.msgId = msgId;
    }
    
	public Long getMessageId() {
		return msgId;
	}
	public void setMessageId(Long msgId) {
		this.msgId = msgId;
	}

    @Override
    public int compareTo(StoredMessage o) {
        return this.msgId.compareTo(o.msgId);
    }    
}
