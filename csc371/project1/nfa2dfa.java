import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class nfa2dfa {
	public static String[][] m;
	public static String[][] dst;
	public static int initialState;
	public static int finalState;
	public static int[] helper;
	public static Map<String, Integer> values;

	public static void main(String args[]) {
		//https://stackoverflow.com/questions/6998119/java-create-a-matrix-of-strings
		//String[][] matrix = { {"10","20","OK"},{"5","30","KO"}, {"20","100","NA"}, {"10","60","OK"} };
		initializeVariables();
		System.out.println("\n\texample 1\nnfa:");
		example1();
		System.out.println("\ndfa:");
		createDFA();

		initializeVariables();
		System.out.println("\n\texample 2\nnfa:");
		example2();
		System.out.println("\ndfa:");
		createDFA();

		initializeVariables();
		System.out.println("\n\texample 3\nnfa:");
		example3();
		System.out.println("\ndfa:");
		createDFA();

		initializeVariables();
		System.out.println("\n\texample 4\nnfa:");
		example4();
		System.out.println("\ndfa:");
		createDFA();
		//System.out.println(java.util.Arrays.deepToString(m));
		
	}
	public static void createDFA() {
		dst = new String[8][4];
		Stack<String> stack = new Stack<>();
		stack.push("0");
		boolean hasTrap = false;
		while(!stack.isEmpty()) {
			String qState = stack.pop();
			String next = "";
			String s = "";
			//AAAAAAAA
			//given m(q,a)
			//loop used to get individual '012'
			for(char q: qState.toCharArray()) {
				s = get((int)(q-'0'),'a');//qState
				if(s == null)
					break;
				
				for(char c:s.toCharArray()) {
					if(Character.isDigit(c))
						if(next.indexOf(c) == -1) // does not contain c
							next += c;//parse ints only
				}
				//System.out.println("next: " + next);
			}
			//LLLLLLLLL
			for(char q: next.toCharArray()) {
				String tmp = get((int)(q-'0'),'L');//should be next qstate
				if(tmp == null)
					break;
				
				for(char c:tmp.toCharArray()) {
					if(Character.isDigit(c))
						if(next.indexOf(c) == -1) // does not contain c
							next += c;//parse ints only
				}
				//System.out.println("next: " + next);
			}
			if(next != "" && getDFA(qState, 'a') == null) {
					setDFA(qState,'a',next);
					stack.push(next);
			} else if(getDFA(qState, 'a') == null && next == "") {
				setDFA(qState,'a',"Trap");
				hasTrap = true;
			}
			
			//BBBBBBBB
			//given m(q,b)
			//loop used to get individual '012'
			next = "";
			for(char q: qState.toCharArray()) {
				s = get((int)(q-'0'),'b');//qState
				if(s == null)
					break;
				
				for(char c:s.toCharArray()) {
					if(Character.isDigit(c))
						if(next.indexOf(c) == -1) // does not contain c
							next += c;//parse ints only
				}
				//System.out.println("next: " + next);
			}
			//LLLLLLLLLL
			for(char q: next.toCharArray()) {
				String tmp = get((int)(q-'0'),'L');//should be next qState
				if(tmp == null)
					break;
				
				for(char c:tmp.toCharArray()) {
					if(Character.isDigit(c))
						if(next.indexOf(c) == -1) // does not contain c
							next += c;//parse ints only
				}
				//System.out.println("next: " + next);
			}
			if(next != "" && getDFA(qState, 'b') == null && s != null) {
					setDFA(qState,'b',next);	
					stack.push(next);
			} else if(getDFA(qState, 'b') == null && next == "") {
				setDFA(qState,'b',"Trap");
				hasTrap = true;
			}
			

			//LLLLLLLL
			//given m(q,L)
			//loop used to get individual '012'
			next = "";
			for(char q: qState.toCharArray()) {
				s = get((int)(q-'0'),'L');//qState
				if(s == null)
					break;
				for(char c:s.toCharArray()) {
					if(Character.isDigit(c))
						if(next.indexOf(c) == -1) // does not contain c
							next += c;//parse ints only
				}
				//System.out.println("next: " + next);
			}
			if(next != "" && getDFA(qState, 'L') == null && s != null) {
					//setDFA(qState,'L',next);	
					//stack.push(next);
			}// else if(getDFA(qState, 'L') == null && next == "") setDFA(qState,'L',"Trap");
			//System.out.println(java.util.Arrays.toString(stack.toArray()));
		}
		//if dfa has trap
		if(hasTrap) {
			setDFA("Trap",'a',"Trap");
			setDFA("Trap",'b',"Trap");
		}
	}
	public static String getDFA(String q, char c) {
		return dst[values.get(q)][charToInt(c)];
	}
	public static void setDFA(String q, char c, String s) {
		dst[values.get(q)][charToInt(c)] = s;
		if(q.indexOf((char)(finalState+'0')) == -1)
			System.out.println("d["+q+"]"+"["+c+"] = "+s);
		else
			System.out.println("d["+q+"]"+"["+c+"] = "+s+"\tF");
	}
	public static void example1() {
		set(0, 'a', "0,1");
		set(1, 'b', "1,2");
		set(2, 'a', "2");
	}
	public static void example2() {
		set(0, 'a', "0,1");
		set(1, 'b', "1,2");
		set(2, 'a', "2");
		set(0, 'L', "2");
	}
	public static void example3() {
		set(0, 'a', "0,1");
		set(1, 'b', "1,2");
		set(2, 'a', "2");
		set(1, 'L', "1,2");
	}
	public static void example4() {
		set(0, 'a', "0,1");
		set(0, 'b', "1");
		set(1, 'a', "2");
		set(1, 'b', "2");
		set(2, 'b', "2");
	}
	public static int subsetToIndex(String subset) {
		System.out.print(subset+": ");
		//0,1,2 = 012 -> binary -> 7 aka 111
		//trap = 1000
		int dst = -1;
		int tmp = -1;
		for(char c: subset.toCharArray()) {
			if(Character.isDigit(c)) {
				tmp = c-'0';
				switch(tmp) {
				case 0:
					dst += 1;
					break;
				case 1:
					dst += 2;
					break;
				case 2:
					dst += 4;
					break;
				}
			} else if(c == 'T') {
				//trap state
			}
		}
		return dst;
	}
	/* Initializes matrices m and dst, as well as iState and fState */
	public static void initializeVariables() {
		m = new String[][]{ {"", "", ""}, {"", "", ""}, {"", "", ""} };
		dst = new String[8][4];
		//all examples have same initial and final state
		initialState = 0;
		finalState = 1;

		values = new HashMap<String, Integer>();
		values.put("0",0);
		values.put("1",1);
		values.put("2",2);
		values.put("01",3);
		values.put("02",4);
		values.put("12",5);
		values.put("012",6);
		values.put("Trap",7);

		helper = new int[]{0,1,2};
	}
	/* converts input symbol to integer for indexing */
	public static int charToInt(char c) {
		int n = -1;
		
		if(c == 'a' ||  c == 'b')
			n = (int)c - (int)'a';
		else if(c == 'L')
			n = 2;
		else
			System.out.println("error");

		return n;
	}
	/*
		@return String value of cell m[q][c]
		@param int q: correspond to nth state
		@param char c: correspond to input symbol
	*/
	public static String get(int q, char c) {
		int j = charToInt(c);
		return m[q][j];
	}

	/*
		sets i, j cell in matrix
		m[q][c] = s
		@param int q: correspond to nth state
		@param char c: correspond to input symbol
		@param String s: subsets of states
	*/
	public static void set(int q, char c, String s) {
		int j = charToInt(c);
		m[q][j] = s;
		printState(q,c,s);
	}
	public static void printState(int q, char c, String s) {
		System.out.println("d["+((char)('0'+q))+"]"+"["+c+"] = "+s);
	}
	/*

	(q0, a) = {q0, q1},
	(q1, b) = {q1, q2},
	(q2, a) = {q2},

	(q0, a) = {q0, q1},
	(q1, b) = {q1, q2},
	(q2, a) = {q2},
	(q0, L) = {q2},

	(q0, a) = {q0, q1},
	(q1, b) = {q1, q2},
	(q2, a) = {q2},
	(q1, L) = {q1, q2},

	(q0, a) = {q0, q1},
	(q0, b) = {q1},
	(q1, a) = {q2},
	(q1, b) = {q2},
	(q2, b) = {q2},

	*/
}
