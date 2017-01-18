package cs.dartmouth.edu.gcmdemo.backend.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactDatastore {

	private static final Logger mLogger = Logger
			.getLogger(ContactDatastore.class.getName());
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getKey() {
		return KeyFactory.createKey(Contact.CONTACT_PARENT_ENTITY_NAME,
				Contact.CONTACT_PARENT_KEY_NAME);
	}


	public static boolean add(Contact contact) {
		if (getContactByName(contact.mName, null) != null) {
			mLogger.log(Level.INFO, "contact exists");
			return false;
		}

		Key parentKey = getKey();


		Entity entity = new Entity(Contact.CONTACT_ENTITY_NAME, contact.mName,
				parentKey);
		entity.setProperty(Contact.FIELD_NAME_NAME, contact.mName);
		entity.setProperty(Contact.FIELD_NAME_ADDR, contact.mAddress);
		entity.setProperty(Contact.FIELD_NAME_PHONENUM, contact.mPhoneNumber);

		mDatastore.put(entity);

		return true;
	}

	public static boolean update(Contact contact) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getKey(),
					Contact.CONTACT_ENTITY_NAME, contact.mName));
			result.setProperty(Contact.FIELD_NAME_ADDR, contact.mAddress);
			result.setProperty(Contact.FIELD_NAME_PHONENUM,
					contact.mPhoneNumber);

			mDatastore.put(result);
		} catch (Exception ex) {

		}
		return false;
	}

	public static boolean delete(String name) {
		// you can also use name to get key, then use the key to delete the
		// entity from datastore directly
		// because name is also the entity's key

		// query
		Filter filter = new FilterPredicate(Contact.FIELD_NAME_NAME,
				FilterOperator.EQUAL, name);

		Query query = new Query(Contact.CONTACT_ENTITY_NAME);
		query.setFilter(filter);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = mDatastore.prepare(query);

		Entity result = pq.asSingleEntity();
		boolean ret = false;
		if (result != null) {
			// delete
			mDatastore.delete(result.getKey());
			ret = true;
		}

		return ret;
	}

	public static ArrayList<Contact> query(String name) {
		ArrayList<Contact> resultList = new ArrayList<Contact>();
		if (name != null && !name.equals("")) {
			Contact contact = getContactByName(name, null);
			if (contact != null) {
				resultList.add(contact);
			}
		} else {
			Query query = new Query(Contact.CONTACT_ENTITY_NAME);
			// get every record from datastore, no filter
			query.setFilter(null);
			// set query's ancestor to get strong consistency
			query.setAncestor(getKey());

			PreparedQuery pq = mDatastore.prepare(query);

			for (Entity entity : pq.asIterable()) {
				Contact contact = getContactFromEntity(entity);
				if (contact != null) {
					resultList.add(contact);
				}
			}
		}
		return resultList;
	}

	public static Contact getContactByName(String name, Transaction txn) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getKey(),
					Contact.CONTACT_ENTITY_NAME, name));
		} catch (Exception ex) {

		}

		return getContactFromEntity(result);
	}

	private static Contact getContactFromEntity(Entity entity) {
		if (entity == null) {
			return null;
		}

		return new Contact(
				(String) entity.getProperty(Contact.FIELD_NAME_NAME),
				(String) entity.getProperty(Contact.FIELD_NAME_ADDR),
				(String) entity.getProperty(Contact.FIELD_NAME_PHONENUM));
	}
}
