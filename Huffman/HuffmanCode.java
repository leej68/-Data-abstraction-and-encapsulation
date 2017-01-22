package Huffman;

//Justin Lee
//CSE 143 section A1
//This program translates a series of letters
//into binary sequences to be used as code

import java.io.*;
import java.util.*;

public class HuffmanCode {
	
	private HuffmanNode overallRoot;
	
	//stores the the count of the character with
	//ASCII value i
	public HuffmanCode(int[] frequencies) {
		Queue<HuffmanNode> count = new PriorityQueue<HuffmanNode>();
		for(int i = 0; i < frequencies.length; i++) {
			if(frequencies[i] > 0) {
				HuffmanNode node = new HuffmanNode(frequencies[i], i);
				count.add(node);
			}
		}
		//stores again but adds the first two stored numbers 
		//together and then extends left and right with those
		//two numbers
		while(count.size() > 1) {
			HuffmanNode small = count.remove();
			HuffmanNode bigger = count.remove();
			count.add(new HuffmanNode(small.frequency + bigger.frequency, small, bigger));
		}
		overallRoot = (HuffmanNode)count.remove();
	}
	
	//reads a file and calls a private method which returns
	//a tree that represents the binary code for the number
	//value of each letter
	public HuffmanCode(Scanner input) {
		overallRoot = null;
		while(input.hasNextLine()) {
			int number = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			overallRoot = create(overallRoot, code, 0, number);
		}
	}
	
	//progresses through a given file and creates a 
	//tree 
	private HuffmanNode create(HuffmanNode root, String code, int point, int number) {
		if(point == code.length()) {
			root = new HuffmanNode(0, number);
		} else {
			if(root == null) {
				root = new HuffmanNode(0, null, null);
			}
			//recursively goes left and right, creating
			//the tree and putting the numerical value of 
			//the letter in the node
			if(code.charAt(point) == '0') {
				root.left = create(root.left, code, point + 1, number);
			} else {
				root.right = create(root.right, code, point + 1, number);
			}
		}
		return root;
	}
	
	//calls on a private method which is used
	//to create a new file
	public void save(PrintStream output) {
		write(output, overallRoot, "");
	}
	
	//creates a new file, printing out the character 
	//it receives every time it gets to a point 
	//when it cannot further progress
	private void write(PrintStream output, HuffmanNode root, String line) {
		if(root.isLeaf) {
			output.println(root.value);
			output.println(line);
		} else {
			write(output, root.left, line + "0");
			write(output, root.right, line + "1");
		}
	}
	
	//while the file still has bits then calls on a private method
	//which returns the character that it finds then calls another 
	//method which prints a character it receives every time it 
	//gets to a point when it cannot further progress. Puts it into a new file
	public void translate(BitInputStream input, PrintStream output) {
		while(input.hasNextBit()) {
			int nextChar = readOneChar(input);
			output.write(nextChar);
		}
	}
	
	//returns the character it gets once it cannot
	//parse further down
	private int readOneChar(BitInputStream input) {
		HuffmanNode first = overallRoot;
		while(!first.isLeaf) {
			int number = input.nextBit();
			if(number == 0) {
				first = first.left;
			} else {
				first = first.right;
			}
		}
		return first.value;
	}
}
