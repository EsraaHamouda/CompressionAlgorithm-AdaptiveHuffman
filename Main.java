import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Main {
	public static int mainNumber = 100;
	static Vector<Node> v = new Vector<Node>();

	public static String compress(String data) {
		Map<Character, String> table = new HashMap<Character, String>();
		table.put('a', "000");
		table.put('b', "001");
		table.put('c', "010");

		String bitResult = "";
		Node root = new Node();
		root.symbol = "NYT";
		root.number = 100;

		bitResult += root.right.code;
		for (int i = 1; i < data.length(); i++) {

			if (isItFirstOcurrance(data, data.charAt(i), i)) {
				Node NYTOfNewNode = splitNYT(root,
						String.valueOf(data.charAt(0)));

				bitResult += NYTOfNewNode.code;
				bitResult += NYTOfNewNode.right.code;

			} else {
				Node nodeSym = goToSymbol(root, data.charAt(i));
				bitResult += nodeSym.code;
			}
			updateTree(data, root, data.charAt(i), i);

		}
		return bitResult;
	}

	public static void main(String[] args) {
		//String path = "";
		String data = "abcccaa";
		String bitResult = compress(data);

		Vector<Integer> vec = new Vector<Integer>();
		vec = binaryToDecimel(bitResult);
		String textVec = vec.toString();
		System.out.println("Compression Result = " + textVec);
		// wirteOnFile(path, textVec);
	}

	public static void wirteOnFile(String path, String text) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(path, false)))) {
			out.print(text);

		} catch (IOException e) {

		}
	}

	public static Vector<Integer> binaryToDecimel(String result) {
		Vector<Integer> bytes = new Vector<Integer>();
		for (int i = 0; i < result.length(); i += 31) {
			if (i + 31 >= result.length())
				break;
			String aa = result.substring(i, i + 31);
			int num = Integer.parseInt(aa, 2);
			bytes.add(num);
		}
		if (result.length() % 31 != 0) {
			String aa = result.substring(result.length()
					- (result.length() % 31));
			int num = Integer.parseInt(aa, 2);
			bytes.add(num);
		}
		return bytes;
	}

	public static void updateTree(String data, Node root, char c, int limit) {
		Node oldNode = new Node();
		if (isItFirstOcurrance(data, c, limit)) {
			Node currentNYT = goToCurrentNYT(root);
			oldNode = splitNYT(currentNYT, String.valueOf(c));
		} else {

			oldNode = goToSymbol(root, c);

		}
		boolean isRoot = isItRoot(oldNode);
		while (!isRoot) {
			Node outputSwapWith = swapWith(root, oldNode);
			if (outputSwapWith != null) {
				swapNodes(oldNode, outputSwapWith);
				increamentCounter(oldNode);

			} else
				break;

			oldNode = oldNode.parent;
			isRoot = isItRoot(oldNode);
		}

	}

	public static Node goToSymbol(Node root, char c) {
		if (root.left != null) {
			root = root.left;
			if (root.symbol.equals(String.valueOf(c))) {
				return root;
			} else
				goToSymbol(root.left, c);
		}
		if (root.right != null) {
			root = root.right;
			if (root.symbol.equals(String.valueOf(c))) {
				return root;
			} else
				goToSymbol(root.right, c);
		}

		return null;
	}

	public static Node goToCurrentNYT(Node root) {
		if (root.left != null) {
			goToCurrentNYT(root.left);
		}
		if (root.right != null) {
			goToCurrentNYT(root.right);
		}
		if (root.left == null && root.right == null && root.symbol == "NYT") {
			return root;
		}
		return null;
	}

	public static Node splitNYT(Node oldNyt, String symbol) {
		Map<Character, String> table = new HashMap<Character, String>();
		table.put('a', "000");
		table.put('b', "001");
		table.put('c', "010");

		Node newNyt = new Node();
		Node newSymbolNode = new Node();
		newSymbolNode.symbol = symbol;
		newSymbolNode.counter = 1;
		newSymbolNode.parent = oldNyt;
		newSymbolNode.code = table.get(symbol.charAt(0));

		newNyt.symbol = "NYT";
		oldNyt.left = newNyt;
		oldNyt.right = newSymbolNode;
		oldNyt.counter++;
		return newSymbolNode.parent;

	}

	public static boolean isItRoot(Node currentNode) {
		return (currentNode.parent == null);
	}

	public static void increamentCounter(Node currentNode) {
		currentNode.counter++;
	}

	// public static boolean needSwap()
	// {
	//
	// }
	public static boolean isItFirstOcurrance(String data, char c, int limit) {
		for (int i = 0; i < limit; i++) {
			if (data.charAt(i) == c)
				return true;

		}
		return false;
	}

	public static void inOrderTraversal(Node focusedNode) {
		if (focusedNode == null)
			return;
		inOrderTraversal(focusedNode.left);
		System.out.println(focusedNode.toString());
		inOrderTraversal(focusedNode.right);
	}

	public static void distributeNumbers(Node node) {
		// node.number = 100;

		if (node.left != null) {
			mainNumber--;
			node.number = mainNumber;

			distributeNumbers(node.left);
		}
		if (node.right != null) {
			mainNumber--;
			node.number = mainNumber;
			distributeNumbers(node.right);
		}
		if (node.left == null && node.right == null) {
			mainNumber--;
			node.number = mainNumber;
		}

	}

	public static Node swapWith(Node root, Node node) {

		if (root.left != null) {
			if (root.number > node.number && node.counter >= root.counter
					&& root != node.parent)
				return root;
			else {
				root = root.left;
				swapWith(root, node);

			}
		}
		if (root.right != null) {
			if (root.number > node.number && node.counter >= root.counter
					&& root != node.parent)
				return root;
			else {
				root = root.right;
				swapWith(root, node);

			}
		}
		if (node.left == null && node.right == null) {
			if (root.number > node.number && node.counter >= root.counter
					&& root != node.parent)
				return root;
		}
		return null;
	}

	public static void swapNodes(Node n1, Node n2) {
		Node temp = new Node();
		temp.code = n2.code;
		temp.number = n2.number;
		temp.counter = n2.counter;
		temp.left = n2.left;
		temp.right = n2.right;
		temp.symbol = n2.symbol;
		temp.parent = n2.parent;
		// //////
		n2.code = n1.code;
		n2.number = n1.number;
		n2.counter = n1.counter;
		n2.left = n1.left;
		n2.right = n1.right;
		n2.symbol = n1.symbol;
		n2.parent = n1.parent;

		// //////////////

		n1.code = temp.code;
		n1.number = temp.number;
		n1.counter = temp.counter;
		n1.left = temp.left;
		n1.right = temp.right;
		n1.symbol = temp.symbol;
		n1.parent = temp.parent;

	}
}
