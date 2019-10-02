import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class ContactBookTool {
		
	private HashMap<String, HashMap<String, ContactCard>> searchMap = new HashMap<String, HashMap<String, ContactCard>>();
	
	public ContactBookTool() {
		
	}
	
	public HashMap<String, Integer> searchFor(String field, String value) {
		HashMap<String, Integer> results = searchMap.get(field);
		return results;
	}
	
	public void addContact(JSONObject updatedInfo){
		ContactCard contact = ContactCard.createEmptyCard(); 
		contact.dictionaryToFieldUpdate(updatedInfo);
		
		Iterator<?> keys = updatedInfo.keys();
		int hashCode = contact.hashCode();
		while (keys.hasNext() ) {
			String key = (String)keys.next();
			String value = updatedInfo.getString(key);
			searchMap.put(key, )
		}
	}
}
