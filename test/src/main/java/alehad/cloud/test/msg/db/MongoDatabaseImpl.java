package alehad.cloud.test.msg.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import alehad.cloud.test.msg.model.IMessageStore;
import alehad.cloud.test.msg.model.Message;

public class MongoDatabaseImpl implements IMessageStore {

	// for now we will statically initialize mongo db
	// once this gets containerized, we will update the initialization bit
	
	private static MongoDatabaseImpl instance;
	
	private static MongoClient mongodbClient;
	private static MongoDatabase mongodb;
	private static MongoCollection<Document> mongodbCollection;
	
	private static String _mongodbName = "MessengerDB";
	private static String _mongodbCollectionName = "messages";
	
	private static String _msgId = "msgId";
	private static String _msg = "msg";

	
	public static MongoDatabaseImpl getInstance() {
		if (instance == null) {
			instance = new MongoDatabaseImpl();
		}
		return instance;
	}
	
	private MongoDatabaseImpl() {
		// will need to update host/port once moving to K8
		mongodbClient = new MongoClient("localhost", 27017);
		
		mongodb = mongodbClient.getDatabase(_mongodbName); // if not present, Mongo will create it
		// do we need to authenticate?
		
		boolean createCollection = true;
		
		MongoIterable<String> collections = mongodb.listCollectionNames();
		
		while (collections.iterator().hasNext() && createCollection) {
			if (collections.iterator().next().equalsIgnoreCase(_mongodbCollectionName)) {
				createCollection = false;
			}
		}

		if (createCollection) {
			mongodb.createCollection(_mongodbCollectionName); // create a table -- in Mongo terms that is a Collection
			mongodbCollection = mongodb.getCollection(_mongodbCollectionName);
			
			Document document = new Document();
			document.put(_msgId, 1);
			document.put(_msg, "hey mongo!");
			mongodbCollection.insertOne(document);
		}
		else {
			mongodbCollection = mongodb.getCollection(_mongodbCollectionName);
		}
	}
	
	@Override
	public List<Message> getMessages() {

		List<Message> messages = new ArrayList<Message>();
		
		FindIterable<Document> iterable = mongodbCollection.find();
		MongoCursor<Document> cursor = iterable.iterator();
		
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			
			if (doc.containsKey(_msgId) && doc.containsKey(_msg)) {
				Message msg = new Message();
				int id = doc.getInteger(_msgId);
				msg.setId(id);
				msg.setMessage(doc.getString(_msg));
				messages.add(msg);
			}
		}
		return messages;
	}

	@Override
	public Message getMessage(int id) {
		Message  msg = null;
		Document doc = mongodbCollection.find(eq(_msgId, id)).first();

		if (doc != null) {
			msg = new Message(doc.getInteger(_msgId), doc.getString(_msg));
		}

		return msg;
	}

	@Override
	public Message createMessage(Message msg) {
		// check if message id already exists
		Bson filter = eq(_msgId, msg.getId());
		if (mongodbCollection.find(filter).first() == null) {
			Document doc = new Document();
			doc.put(_msgId, msg.getId());
			doc.put(_msg, msg.getMessage());
			try {
				mongodbCollection.insertOne(doc);
			}
			finally {
				// TODO: define exception to throw or how to indicate unsuccessful op 
			}
		}
		else {
			// TODO: this should set descriptive message -- eg use update method instead
		}
		return msg;
	}

	@Override
	public Message updateMessage(int id, Message msg) {
		Bson filter = eq(_msgId, id);
		Bson updateOperation = set(_msg, msg.getMessage());
		UpdateOptions options = new UpdateOptions().upsert(true); // this will insert new message if id not found
		mongodbCollection.updateOne(filter, updateOperation, options); // does not throw
		return msg;
	}

	@Override
	public void deleteMessage(int id) {
		Bson filter = eq(_msgId, id);
		try {
			mongodbCollection.deleteOne(filter);		
		}
		finally {
		}
	}
	
}