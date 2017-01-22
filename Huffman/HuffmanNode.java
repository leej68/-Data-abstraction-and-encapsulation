package Huffman;

//Justin Lee
//CSE 143 section A1
//represents a single piece of code

public class HuffmanNode implements Comparable<HuffmanNode> {
	public int value;
	public int frequency;
	public HuffmanNode left; // is 0
	public HuffmanNode right; // is a 1
	public boolean isLeaf;
	
	//constructor for a leafNode
	public HuffmanNode(int frequency, int value) {
		this.frequency = frequency;
		this.value =  value;
		this.isLeaf = true;
	}
	
	//constructor for a branchNode
	public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
		this.frequency = frequency;
		this.isLeaf = false;
		this.left = left;
		this.right = right;
	}
	
	//compares the current value to another value
	public int compareTo(HuffmanNode other) {
		return this.frequency - other.frequency;
	}
}
