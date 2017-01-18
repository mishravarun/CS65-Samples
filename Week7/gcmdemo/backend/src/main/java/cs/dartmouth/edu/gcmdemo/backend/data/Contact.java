package cs.dartmouth.edu.gcmdemo.backend.data;

public class Contact {
	public static final String CONTACT_PARENT_ENTITY_NAME = "ContactParent";
	public static final String CONTACT_PARENT_KEY_NAME = "ContactParent";

	public static final String CONTACT_ENTITY_NAME = "Contact";
	public static final String FIELD_NAME_NAME = "name";
	public static final String FIELD_NAME_ADDR = "addr";
	public static final String FIELD_NAME_PHONENUM = "phone";
	public static final String KEY_NAME = FIELD_NAME_NAME;

	public String mName;
	public String mAddress;
	public String mPhoneNumber;

	public Contact(String _name, String _addr, String num) {
		mName = _name;
		mAddress = _addr;
		mPhoneNumber = num;
	}

}
