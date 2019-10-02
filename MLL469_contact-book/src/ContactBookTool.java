import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;

public class ContactBookTool {
		
	// Internal structure as follows: index into the map through an existing value ('Bob', '311', 'example@example.com').
	// O(1) time, worst-case O(cards * fields) space total
	// Index into the subsequent map with the keys it has values for ('name', 'number', 'email' respectively).
	// O(1) time, i.e. worst-case O(number of fields) time
	// What is returned is a list of references to ContactCards which matches those values for those fields (Mostly one item lists, but can be many).
	// For example if two contacts are named 'Bob', the returned List will be the two ContactCards of the 'Bob's, which are distinct. 
	private HashMap<String, HashMap<String, List<ContactCard>>> searchByValueMap = new HashMap<String, HashMap<String, List<ContactCard>>>();
	private HashMap<Integer, ContactCard> contactList = new HashMap<Integer, ContactCard>();
	
	public ContactBookTool() {
		
	}
	
	// ----------------------------------------------------------------------
	// VIEW THE CONTACT BOOK IN CONSOLE
	// ----------------------------------------------------------------------
		
	
	public void viewContact(ContactCard contact) {
		HashMap<String, String> cardProps = contact.getProperties();
		System.out.println("--------------------------------- (hashcode entry " + Integer.toString(contact.hashCode()) + ")");
		for (Entry<String, String> entry : cardProps.entrySet()) {
	
			String field = entry.getKey();
			String value = entry.getValue();
			System.out.println(field + ": " + value);
		}
	}
	
	
	public void viewContactBook() {
		System.out.println("\n***CONTACT BOOK****");
		if (this.contactList.isEmpty()) {
			System.out.println("This book has no contacts...yet.");
		}
		List<ContactCard> mapToList = new ArrayList<ContactCard>(contactList.values());
		for (ContactCard card : mapToList) {
			viewContact(card);
		}
	}

	public List<ContactCard> getContactList() {
		List<ContactCard> mapToList = new ArrayList<ContactCard>(contactList.values());
		return mapToList;
	}
	
// ----------------------------------------------------------------------
// SEARCH THE CONTACT BOOK (VIEW IN CONSOLE)
// ----------------------------------------------------------------------
	
	public void viewValueMaps(String value) {
		HashMap<String, List<ContactCard>> resultsOfSearchForVal = searchForValue(value);
		if (!resultsOfSearchForVal.isEmpty()) {
			for (Entry<String, List<ContactCard>> entry : resultsOfSearchForVal.entrySet()) {
				String field = entry.getKey();
				System.out.println("\n****SEARCH RESULTS****");
				System.out.println(">" + field + " items matching '" + value + "':");
				viewFieldMatches(field, resultsOfSearchForVal);
			}
		}
		else {
			System.out.println("No contacts possess this value ('"+value+"')...yet.");
		}
	}
	
	public  void viewFieldMatches(String field, HashMap<String, List<ContactCard>> resultsOfSearchForVal) {
		if (resultsOfSearchForVal.containsKey(field)){
			List<ContactCard> results = resultsOfSearchForVal.get(field);
			HashMap<Integer, ContactCard> seen = new HashMap<Integer, ContactCard>();
			
			List<ContactCard> noDuplicates = new ArrayList<ContactCard>(); 
			for (ContactCard card : results) {
				if (!seen.containsKey(card.hashCode())) {
					noDuplicates.add(card);
				}
				seen.put(card.hashCode(), card);
			}
			
			for (ContactCard card : noDuplicates) {
				viewContact(card);
			}
		}
		else { 
			System.out.println("This result query does not have any fields matching: " + field);
		}
	}

	public void viewListOfCards(List<ContactCard> cards) {
		for (ContactCard card : cards) {
			this.viewContact(card);
		}
	}

	// ----------------------------------------------------------------------
	// SEARCH THE CONTACT BOOK (RETURN OBJECTS)
	// ----------------------------------------------------------------------
	
	public List<ContactCard> getListOfMatches(String field, String value) {
		List<ContactCard> results = this.searchValuesForMatchingField(field,this.searchForValue(value));
		int i = 0;
		System.out.println("\n***GET LIST OF FIELD:VALUE MATCHES***");
		for (ContactCard card : results) {
			System.out.println("Index " + Integer.toString(i));
			this.viewContact(card);
			i++;
		}
		return results;
	}
	
	public HashMap<String, List<ContactCard>> searchForValue(String value) {
		if (this.searchByValueMap.containsKey(value)) {
			HashMap<String, List<ContactCard>> results = this.searchByValueMap.get(value);
			return results;
		}
		else {
			return new HashMap<String, List<ContactCard>>();
		}
	}

	public  List<ContactCard> searchValuesForMatchingField(String field, HashMap<String, List<ContactCard>> resultsOfSearchForVal) {
		if (resultsOfSearchForVal.containsKey(field)){
			List<ContactCard> results = resultsOfSearchForVal.get(field);
			return results;
		}
		else { 
			return Collections.emptyList();
		}
	}
	
	
	
	// ----------------------------------------------------------------------
	// CHANGE THE CONTACT BOOK (CRUD)
	// ----------------------------------------------------------------------
	
	public void addContactJSON(JSONObject cardInfo){
		ContactCard immutableCard = ContactCard.immutableCardViaJSON(cardInfo);
		addToSearchMap(immutableCard);
		this.contactList.put(immutableCard.hashCode(), immutableCard);
	}
	
	public void addContactMap(HashMap<String, String> map){
		ContactCard immutableCard = ContactCard.immutableCardViaMap(map);
		addToSearchMap(immutableCard);
		this.contactList.put(immutableCard.hashCode(), immutableCard);
	}
	
	public void editContact(JSONObject updatedInfo, ContactCard contact){
		
		// Original contact.
		HashMap<String, String> properties = contact.getProperties();
		
		// Edit fields of interest.
		Iterator<?> keys = updatedInfo.keys();
		while (keys.hasNext() ) {
			String key = (String)keys.next();
			String value = updatedInfo.getString(key);
			properties.put(key, value);
		}
		
		removeFromSearchMap(contact); // Old search map will have to change.
		this.contactList.remove(contact.hashCode());
		this.addContactJSON(updatedInfo); // Includes adding its new properties to the searchMap.
		contact = null;
	}
	
	public void deleteContact(ContactCard contact) {
		removeFromSearchMap(contact);
		this.contactList.remove(contact.hashCode());
		contact = null;
	}
	
	// ----------------------------------------------------------------------
	// PRIVATE HELPER FUNCTIONS TO MAINTAIN THE SEARCH MAP
	// ----------------------------------------------------------------------
		
	
	private void addToSearchMap(ContactCard contact) {
		HashMap<String, String> cardProps = contact.getProperties();
		
		for (Entry<String, String> entry : cardProps.entrySet()) {
		
			String field = entry.getKey();
			String value = entry.getValue();
			if (this.searchByValueMap.containsKey(value)) {
				
				HashMap<String, List<ContactCard>> matchingFields = this.searchByValueMap.get(value);
				
				if (matchingFields.containsKey(field)) {
					List<ContactCard> matchingCards = matchingFields.get(field);
					matchingCards.add(contact);
				}
				else {
					List<ContactCard> matchingCards = new ArrayList<ContactCard>();
					matchingCards.add(contact);
				
					matchingFields.put(field, matchingCards);
				}
			}
			else { // The searchByValueMap does not have this value, so we add new structures
				List<ContactCard> matchingCards = new ArrayList<ContactCard>();
				matchingCards.add(contact);
				
				HashMap<String, List<ContactCard>> matchingFields = new HashMap<String, List<ContactCard>>();
				matchingFields.put(field,  matchingCards);
				
				this.searchByValueMap.put(value, matchingFields);
			}
		}
	}
	
	
	private void removeFromSearchMap(ContactCard contact) {
		HashMap<String, String> cardProps = contact.getProperties();
		
		for (Entry<String, String> entry : cardProps.entrySet()) {
		
			String value = entry.getValue();
			
			HashMap<String, List<ContactCard>> fieldMap = searchByValueMap.get(value);
			
			for (Entry<String, List<ContactCard>> cardRef : fieldMap.entrySet()) {
				List<ContactCard> dummyMatchingCards = new ArrayList<ContactCard>(cardRef.getValue()); 
				
				for (ContactCard card : dummyMatchingCards) {
					if (card.hashCode() == contact.hashCode()) { 
						cardRef.getValue().remove(card);
						break;
					}
				}
			}
		}
	}
}
