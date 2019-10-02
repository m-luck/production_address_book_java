import java.util.List;

import org.json.JSONObject;

public class Tester {

	public static void main(String[] args) {
		ContactBookTool book = new ContactBookTool();
		book.viewContactBook();
		book.viewValueMaps("michael");
		
		JSONObject courant = new JSONObject();
		courant.put("name","R. Courant");
		
		JSONObject courant_clone = new JSONObject();
		courant_clone.put("name","R. Courant");
		
		JSONObject courant_jr = new JSONObject();
		courant_jr.put("name","R. Courant");
		courant_jr.put("address","New Earth");
		
		book.addContactJSON(courant); 
		book.addContactJSON(courant_clone);
		book.addContactJSON(courant_jr); 
		book.viewContactBook();
		book.viewValueMaps("R. Courant");

		List<ContactCard> matchingCards = book.getListOfMatches("name","R. Courant");
		ContactCard toRemove = matchingCards.get(0);
		book.deleteContact(toRemove);
		book.viewContactBook();
		
		matchingCards = book.getListOfMatches("name","R. Courant");
		ContactCard toEdit = matchingCards.get(0);
		book.editContact(courant_jr, toEdit);
		book.viewContactBook();
	}
}
