package HTMLvalidator;

//Justin Lee
//CSE 143 A1
//HTMLManager takes in a set of HTML tags and transfers them into a 
// a list that can be used to correct style mistakes in the HTML code

import java.util.List;
import java.util.ArrayList;
import cse143.util.*;
	
public class HTMLManager {
	
	private List<HTMLTag> html;
	
	//pre : if the the queue of pages equal null than throws new IllegalArgumentException
	//post : initializes the list into an arraylist which is used to store all 
	//the HTML tags in queue page.
	public HTMLManager(Queue<HTMLTag> page) {
		if(page == null) {
			throw new IllegalArgumentException();
		}
		
		html = new ArrayList<HTMLTag>();
		while(!page.isEmpty()){
			this.html.add(page.dequeue());
		}
	}
	
	//pre: if index < 0 or > then the number of tags in queue page 
	//then throw IllegalArgumentException
	//post : this method adds a HTML tag into the arraylist at the specific point index
	public void add(int index, HTMLTag tag) { 
		if(index < 0 || index > html.size()) {
			throw new IllegalArgumentException();
		}
		html.add(index, tag);
	}
	
	//pre: if index < 0 or > then the number of tags in queue page 
	//then throw IllegalArgumentException
	// post : removes an HTML tag at specific point index
	public void remove(int index) {
		if(index < 0 || index > html.size()) {
			throw new IllegalArgumentException();
		}
		html.remove(index);
	}
	
	// post : this does a internal rewrite of the class by making a new
	//ArrayList. Doing so creates a copy completely independent from the field 
	// which in turn is a deep copy
	public List<HTMLTag> getTags() {
		return new ArrayList<HTMLTag>(this.html);
	}
	
	//post : returns a String version of all the tags in the 
	//field Arraylist 
	public String toString() {
		String represent = "";
		for(HTMLTag tags : this.html) {
			represent += tags;
		}
		return represent;
	}

    
	//uses the field arraylist as input and creates a new arraylist as output
	//stack is used to store tags which is later used to compare whether the
	//opening tags, which are transfered to the stack, need to be closed.
	public void fixHTML() {
		Stack<HTMLTag> fix = new ArrayStack<HTMLTag>();
		List<HTMLTag> htmlCheck = new ArrayList<HTMLTag>();
		for(int i = 0; i < html.size(); i++) {
			HTMLTag temp = this.html.get(i); // used to reduce redundancy and runs over the input
			if(temp.isOpening()) {
				fix.push(temp);
				htmlCheck.add(temp);
			} else if(temp.isComment() || temp.isSelfClosing()) {
				htmlCheck.add(temp);
			} else if(temp.isClosing() && !fix.isEmpty()) {
				HTMLTag stackTag = fix.pop();
				if(temp.matches(stackTag)) {
					htmlCheck.add(temp);
				} else {
					htmlCheck.add(stackTag.getMatching());// create the matching one to put into the output
					i--;
				}
			}
		}
		// if there is anything still in the stack 
		// it turns the tag into its ending equivalent tag
		while(!fix.isEmpty()) {
			htmlCheck.add(fix.pop().getMatching());
		}
		//puts the output(which is the corrected version of the input)
		//back into the input.
		this.html = htmlCheck;
	}
}
		

