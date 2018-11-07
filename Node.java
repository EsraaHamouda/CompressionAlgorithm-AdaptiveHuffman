class Node {

	public int counter;

	public Node left;
	public Node right;
	public Node parent;
	
	public String code;
	public String symbol;
	public int number;
	
	public Node() {
		super();
		
	}

	public String toString() {
		return "[counter=" + counter + ", left=" + left + ", right="
				+ right + ", parent=" + parent + ", code=" + code + ", symbol="
				+ symbol + ", number=" + number + "]";
	}

	
	

}
