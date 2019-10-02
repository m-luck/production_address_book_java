import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;
/**
 * 
 * @author Michael Lukiman
 *
 */
public class ContactCard {
	private HashMap<String, String> cardProperties;
	private int hashCodeVal = -1; 
	
	private ContactCard() {
		cardProperties = new HashMap<String, String>();
		cardProperties.put("name", "Empty");
		cardProperties.put("number","No number");
		cardProperties.put("email","No email");
		cardProperties.put("address","No address");
	};
	
	public static ContactCard immutableCard(JSONObject jObject) {
		ContactCard contact = createEmptyCard();
		contact.dictionaryToFieldUpdate(jObject);
		return contact;
	}
	
	private static ContactCard createEmptyCard() {
		ContactCard contact = new ContactCard(); 
		return contact;
	}
	
	private void dictionaryToFieldUpdate(JSONObject jObject) {
			Iterator<?> keys = jObject.keys();
			
			while (keys.hasNext() ) {
				String key = (String)keys.next();
				String value = jObject.getString(key);
				if (this.cardProperties.containsKey(key)) { // Prevents superfluous information in the JSON making it into the ContactCard data structure. 
					this.cardProperties.put(key, value); 
				}
			}
	}
	
	public HashMap<String, String> getProperties() {
		return cardProperties;
	}
	
	@Override
	public int hashCode() {
		/**
		 * Returns a unique code using all of object's fields, used to locate the object in hashmap.
		 * @return An integer primitive to be used as the object's unique hashcode.
		 */
		if (this.hashCodeVal != -1) { 
			return hashCodeVal;
			} // Prevents from rehashing code if already exists. The public points of entry to class only allows new objects, so hashcode does not change. 
		else {
			String name = cardProperties.get("name");
			String number = cardProperties.get("number");
			String email = cardProperties.get("email");
			String address = cardProperties.get("address");
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((number == null) ? 0 : number.hashCode());
			result = prime * result + ((email == null) ? 0 : email.hashCode());
			result = prime * result + ((email == null) ? 0 : address.hashCode());
			return result;
		}
	};
	
	
};
