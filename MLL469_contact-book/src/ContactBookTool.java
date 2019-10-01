import java.util.HashMap;
import org.json.simple.JSONObject;

public class ContactBookTool {
		
	private HashMap<String, HashMap<String, Integer>> searchMap = new HashMap<String, HashMap<String, Integer>>();
	
	public ContactBookTool() {
		
	}
	
	public HashMap<String, Integer> searchFor(String field, String value) {
		HashMap<String, Integer> results = searchMap.get(field);
		return results;
	}
	
	public void addContact(JSONObject jObject){
		ContactCard contact = ContactCard.createEmptyCard(); 
		contact.dictionaryToFieldUpdate();
	}
}
