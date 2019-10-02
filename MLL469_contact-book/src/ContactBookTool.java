import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;

public class ContactBookTool {
		
	// Internal structure as follows: index into the map through an existing value ('Bob', '311', 'example@example.com').
	// O(1) time, worst-case O(cards * fields) space
	// Index into the subsequent map with the keys it has values for ('name', 'number', 'email' respectively).
	// O(1) time
	// What is returned is a list of ContactCards which matches those values for those fields (Mostly one item lists, but can be many).
	// For example if two contacts are named 'Bob', the returned List will be the two ContactCards of the 'Bob's, which are distinct. 
	private HashMap<String, HashMap<String, List<ContactCard>>> searchByValueMap = new HashMap<String, HashMap<String, List<ContactCard>>>();
	
	public ContactBookTool() {
		
	}
	
	public HashMap<String, List<ContactCard>> searchForValue(String value) {
		HashMap<String, List<ContactCard>> results = searchByValueMap.get(value);
		return results;
	}
	
	public  List<ContactCard> searchValuesForMatchingField(String field, HashMap<String, List<ContactCard>> resultsOfSearchForVal) {
		if (resultsOfSearchForVal.containsKey(field)){
			List<ContactCard> results = resultsOfSearchForVal.get(field);
			return results;
		}
		else { 
			return null;
		}
	}
	
	public void addContact(JSONObject cardInfo){
		ContactCard immutableCard = ContactCard.immutableCard(cardInfo);
		
	}
	
	private void addToSearchMap(ContactCard contact) {
		HashMap<String, String> cardProps = contact.getProperties();
		
		for (Entry<String, String> entry : cardProps.entrySet()) {
		
			String key = entry.getKey();
			String value = entry.getValue();
			if (this.searchByValueMap.containsKey(value)) {
				HashMap<String, List<ContactCard>> matchingFields = this.searchByValueMap.get(value);
				matchingFields.add(contact)
			}
			else {
				
			}
		}
	}
	
	
	private void removeFromSearchMap(ContactCard contact) {
		HashMap<String, String> cardProps = contact.getProperties();
		
		for (Entry<String, String> entry : cardProps.entrySet()) {
		
			String key = entry.getKey();
			String value = entry.getValue();
			
			HashMap<String, List<ContactCard>> fieldMap = searchByValueMap.get(value);
			
			for (Entry<String, List<ContactCard>> cardRef : fieldMap.entrySet()) {
				
				String field = cardRef.getKey();
				List<ContactCard> matchingCards = cardRef.getValue();
				List<ContactCard> dummyMatchingCards = new ArrayList<ContactCard>(matchingCards); 
				for (ContactCard card : matchingCards) {
					if (card.hashCode() == contact.hashCode()) { 
						matchingCards.remove(card);
						break;
					}
				}
			}
		}
	}

	public void editContact(JSONObject updatedInfo, ContactCard contact){
	}
}
