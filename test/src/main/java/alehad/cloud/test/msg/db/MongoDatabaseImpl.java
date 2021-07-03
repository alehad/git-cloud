package alehad.cloud.test.msg.db;

import java.util.ArrayList;
import java.util.Collections;
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
import alehad.cloud.test.msg.model.StoredMessage;

public class MongoDatabaseImpl implements IMessageStore {

	// for now we will statically initialize mongo db
	// once this gets containerized, we will update the initialization bit
	
	private static MongoDatabaseImpl instance;
	
	private static MongoClient mongodbClient;
	private static MongoDatabase mongodb;
	private static MongoCollection<Document> mongodbCollection;
	
	private static String _mongodbName = "MessengerDBv2";
	private static String _mongodbCollectionName = "messages";
	
	private static String _msgId = "msgId";
	private static String _msg   = "msg";
	private static String _auth  = "author";

	
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
		}
		else {
			mongodbCollection = mongodb.getCollection(_mongodbCollectionName);
		}
	}
	
	@Override
	public List<StoredMessage> getMessages() {

		List<StoredMessage> messages = new ArrayList<StoredMessage>();
		
		FindIterable<Document> iterable = mongodbCollection.find();
		MongoCursor<Document> cursor = iterable.iterator();
		
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			
			if (doc.containsKey(_msgId)) {
				messages.add(new StoredMessage(doc.getLong(_msgId), doc.getString(_msg), doc.getString(_auth)));
			}
		}

		Collections.sort(messages);

		return messages;
	}

	@Override
	public Message getMessage(int id) {
		Message  msg = null;
		Document doc = mongodbCollection.find(eq(_msgId, Long.valueOf(id))).first();

		if (doc != null) {
			msg = new StoredMessage(doc.getLong(_msgId), doc.getString(_msg), doc.getString(_auth));
		}

		return msg;
	}

	@Override
	public Message createMessage(Message msg) {
		// check if message id already exists
		Document doc = new Document();
		doc.put(_msgId, mongodbCollection.countDocuments() + 1); // potenital issue with deleted messages in the middle
		doc.put(_msg, msg.getMessage());
		doc.put(_auth, msg.getAuthor());
		try {
			mongodbCollection.insertOne(doc);
		}
		finally {
			// TODO: define exception to throw or how to indicate unsuccessful op 
		}
		return msg;
	}

	@Override
	public Message updateMessage(int id, Message msg) {
		Bson filter = eq(_msgId, Long.valueOf(id));
		Bson updateMsg = set(_msg, msg.getMessage());
		Bson updateAuth = set(_auth, msg.getAuthor());
		Bson update = combine(updateMsg, updateAuth);
		UpdateOptions options = new UpdateOptions().upsert(true); // this will insert new message if id not found
		mongodbCollection.updateOne(filter, update, options); // does not throw
		return msg;
	}

	@Override
	public void deleteMessage(int id) {
		Bson filter = eq(_msgId, Long.valueOf(id));
		try {
			mongodbCollection.deleteOne(filter);		
		}
		finally {
		}
	}
	
}