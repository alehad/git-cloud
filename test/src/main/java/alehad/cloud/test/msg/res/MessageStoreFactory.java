package alehad.cloud.test.msg.res;

import alehad.cloud.test.msg.db.MongoDatabaseImpl;
import alehad.cloud.test.msg.db.SimpleDatabase;
import alehad.cloud.test.msg.model.IMessageStore;

public class MessageStoreFactory {

	// keep default constructor for now
	public MessageStoreFactory() {
		// no-op default constructor
	}
	
	public IMessageStore initializeMessageStore() {
		// create simple database for now
		//return SimpleDatabase.getInstance();
		return MongoDatabaseImpl.getInstance();
	}
}
