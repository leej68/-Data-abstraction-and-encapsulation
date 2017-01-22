// Justin Lee
// CSE 143 A1
// AssassinManager.java is made to manage a game of Assassin
// it keeps track of who is stalking who and who killed who
// while also showing the order in which they were killed 
// in the grave yard

import java.util.List;

public class AssassinManager {
	
	private AssassinNode killChain;
	private AssassinNode graveyard;
	
	//pre: if name is Empty or null throws new illegal argument exception
	//initializes the field killChain to the first name in the List name
	//creates a new node to point to killChain node and adds the rest of the names
	public AssassinManager(List<String> name) {
		if(name.isEmpty() || name ==  null) {
			throw new IllegalArgumentException();
		}

		this.killChain = new AssassinNode(name.get(0));
		AssassinNode first = this.killChain;
		for(int i = 1; i < name.size(); i++) { 
			first.next = new AssassinNode(name.get(i));
			first = first.next;
		}
	}
	
	// prints out line by line who is stalking who
	public void printKillRing() {
		AssassinNode first = killChain;
		while(first.next != null) {
			System.out.println("    " + first.name + " is stalking " + first.next.name);
			first = first.next;
		}
		System.out.println("    " + first.name + " is stalking " + killChain.name);
		
	}
	
	// prints out line by line who was killed by who
	public void printGraveyard() {
		AssassinNode first = graveyard;
		if(first.next != null) {
			System.out.println("    " + first.name + " was killed by " + first.killer);
			first = first.next;
		}
	}
	
	// calls on the contains method to see it killChain contains 
	// the given name
	public boolean killRingContains(String name) {
		return contains(killChain, name);
	}
	
	// calls on the contains method to see if grave yard 
	//contains the given name
	public boolean graveyardContains(String name) {
		return contains(graveyard, name);
	}
	
	//goes through the Kill chain and grave yard to see
	//if they contain the given name (while ignoring case) 
	//then returns true or false
	private boolean contains(AssassinNode current, String name) {
		AssassinNode front = current;
		while(front != null) {
			if(name.equalsIgnoreCase(front.name)) {
				return true;
			}
			front = front.next;
		}
		return false;
	}
		
	//returns true if there is only one name left in the
	//killChain
	public boolean isGameOver() {
		return killChain.next == null;
	}
	
	//returns the name of the winner and returns 
	//null if the game is not over
	public String winner() {
		if(isGameOver()) {
			return killChain.name;
		}
		return null;
	}
	
	//pre: if the game is over throw illegalStateException
	// if the killChain does not contain the given name
	//throw illegalArgumentException
	public void kill(String name) {
		if(isGameOver()) {
			throw new IllegalStateException();
		} else if(!killRingContains(name)) {
			throw new IllegalArgumentException();
		}
		
		AssassinNode finding = killChain;
		AssassinNode found = null;
		
		// if the first person in the killChain is being killed
		if(name.equalsIgnoreCase(killChain.name)) {
			while(finding.next != null) {
				finding = finding.next;
			}
			killChain.killer = finding.name;
			finding = killChain;
		
		//the first person in the killChain is not being killed
		} else {
			found = killChain;
			finding = killChain.next;
		
			while(finding != null && !name.equalsIgnoreCase(finding.name)) {
				finding = finding.next;
				found = found.next;
			}
			finding.killer = found.name;
		}
		addToFront(finding, found);
	}
	
	// adds the person that is killed to the front of the kill ring
	private void addToFront(AssassinNode victim, AssassinNode murderer) {
		if(murderer ==  null) {
			killChain = killChain.next;
		} else {
			murderer.next = victim.next;
		}
		if(graveyard == null) {
			graveyard = victim;
			graveyard.next = null;
		} else {
			victim.next = graveyard;
			graveyard = victim;
		}
	}
}
