package edu.dartmouth.cs.gae_sample.backend.data;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Contact {
	public static final String CONTACT_PARENT_ENTITY_NAME = "ContactParent";
	public static final String CONTACT_PARENT_KEY_NAME = "ContactParent";

	public static final String CONTACT_ENTITY_NAME = "Contact";
	public static final String FIELD_NAME_NAME = "name";
	public static final String FIELD_NAME_ADDR = "addr";
	public static final String FIELD_NAME_PHONENUM = "phone";
	public static final String KEY_NAME = FIELD_NAME_NAME;
	@Id
	Long id;

	@Index
	public String mName;
	public String mAddress;
	public String mPhoneNumber;

	public Contact(String _name, String _addr, String num) {
		mName = _name;
		mAddress = _addr;
		mPhoneNumber = num;
	}

}
