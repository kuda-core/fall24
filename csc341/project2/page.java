import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class page {
	public static int ptr;
	public static String src;
	public static int size;
	public static List<Character> list;
	public static int i;
	public static void FIFO(String reference) {
		ptr = 0;// do not edit
		src = reference;// do not edit
		list = new ArrayList<>();
		List<Character> prev;
		char c;
		int index = 0;
		int count = 0;
		int step = 0;
		prev = list;

		System.out.println("Step"+"\tFames"+"\t\tFault");
		while(true) {
			prev = list;
			c = getNext();
			if(c == '_')
				break;
			// do stuff
			if(list.size() < size) {
				list.add(c);
				count++;
			} else {
				if(list.contains(c)) {
					System.out.println((step++)+"\t"+list.toString() + "\tNO");
					continue;
				}
				list.set(index,c);
				index = index + 1;
				index = index%size;
				count++;
			}
			System.out.println((step++)+"\t"+list.toString() + "\tYES");
		}
		System.out.println("Total Page Faults: "+count);
	}
	public static void OPT(String reference) {
		ptr = 0;
		src = reference;
		list = new ArrayList<>();
		int step = 0;
		HashMap<Character, Integer> table = new HashMap<>();
		HashMap<Character, Integer> map = new HashMap<>();
		

		char c;
		step = 0;
		boolean isFault;
		int faults = 0;
		int index = 0;
		System.out.println("Step"+"\tFames"+"\t\tFault");
		while(true) {
			c = getNext();
			if(c == '_')
				break;
			// do stuff
			isFault = false;
			if(table.containsKey(c)) {
				//update page
				table.put(c,step);
			} else {
				if(table.size() >= size) {
					int[] arr = new int[size];
					Character tmp = null;
					
					int oldest = -1;
					for(Character entry : table.keySet()) {
						
						int i = ptr;
						for(char nextChar : reference.substring(ptr).toCharArray()) {
							i++;
							if(nextChar == ' ' || nextChar == ',')
								continue;
							if(nextChar == entry) {
								i--;
								break;
							}

						}
						if(i > oldest || oldest == -1) {
							oldest = i;
							tmp = entry;
						}
					}
					//remove page
					table.remove(tmp);

					//edit list
					index = list.indexOf(tmp);
					list.set(index,c);
				}
				//add page
				table.put(c,step);
				isFault = true;
				faults++;

				if(list.size() < size) {
					list.add(c);
				}
			}
			System.out.println((step++)+"\t"+c+list+"\t"+(isFault?"YES":"NO"));
		}
		System.out.println("Total Page Faults: "+faults);

	}
	public static void LRU(String reference) {
		ptr = 0;
		src = reference;
		list = new ArrayList<>();
		HashMap<Character, Integer> table = new HashMap<>();

		int faults = 0;
		int step = 0;
		int index = 0;
		char prev = '_';
		char c;

		int oldest;
		Character tmp = null;
		boolean isFault;

		System.out.println("Step"+"\tFames"+"\t\tFault");
		while(true) {
			c = getNext();
			if(c == '_')
				break;
			// do stuff
			isFault = false;
			if(table.containsKey(c)) {
				//update page
				table.put(c,step);
				isFault = false;
			} else {
				
				if(table.size() >= size) {
					//https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
					//get lowest clock time
					oldest = -1;
					for(Map.Entry<Character,Integer> entry : table.entrySet()) {
						Character key = entry.getKey();
						Integer value = entry.getValue();
						if(value < oldest || oldest == -1) {
							oldest = value;
							tmp = key;
						}
					}
					//remove oldest page
					table.remove(tmp);


					//edit list
					index = list.indexOf(tmp);
					list.set(index,c);
				}

				//add page
				table.put(c,step);
				faults++;
				isFault = true;

				if(list.size() < size) {
					list.add(c);
				}
			}
			
			System.out.println((step++)+"\t"+list+ "\t"+(isFault?"YES":"NO"));
			
		}
		System.out.println("Total Page Faults: "+faults);
	}
	public static char getNext() {
		while(true) {
			if(ptr < src.length()) {
				char c = src.charAt(ptr++);
				if(c == ' ' || c == ',') {
					continue;
				}
				return c;

			} else {
				return '_';//ERROR
			}
		}
	}
	//https://stackoverflow.com/questions/13185727/reading-a-txt-file-using-scanner-class-in-java
	public static String readFile(String fileName) {
		String ret = "";
		File file = new File(fileName);
		try {
			Scanner in = new Scanner(file);
			ret = in.nextLine();
			in.close();
		} catch(Exception ex) {

		}
		return ret;
	}
	public static void menu() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Press [0-4] for the following [enter]:");
		System.out.println("0: your own file");
		System.out.println("1: Example FIFO");
		System.out.println("2: Example OPT");
		System.out.println("3: Example LRU");
		System.out.println("4: your own String (typed out)");
		
		int option = scan.nextInt();
		scan.nextLine();

		String s = "7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1";
		int sz = -1;
		switch(option) {
		case 0:
			System.out.println("filename:");
			String filename = scan.nextLine();
			s = readFile(filename);
			System.out.println("String read: "+s);

			System.out.println("Press [1-3] for the following [enter]:");
			System.out.println("1: run FIFO algorithm");
			System.out.println("2: run OPT algorithm");
			System.out.println("3: run LRU algorithm");
			option = scan.nextInt();
			scan.nextLine();
			
			System.out.println("Specify the size: ");
			sz = scan.nextInt();
			scan.nextLine();
			size = sz;

			
			switch(option) {
			case 1:
				FIFO(s);
				break;
			case 2:
				OPT(s);
				break;
			case 3:
				LRU(s);
				break;
			}
			break;
		case 1:
			System.out.println("table size: "+size);
			System.out.println("String: "+s);
			FIFO(s);
			break;
		case 2:
			System.out.println("table size: "+size);
			System.out.println("String: "+s);
			OPT(s);
			break;
		case 3:
			System.out.println("table size: "+size);
			System.out.println("String: "+s);
			LRU(s);
			break;
		case 4:
			System.out.println("Please type out your string: ");
			s = scan.nextLine();
			System.out.println("Press [1-3] for the following [enter]:");
			System.out.println("1: FIFO");
			System.out.println("2: OPT");
			System.out.println("3: LRU");
			option = scan.nextInt();
			scan.nextLine();

			System.out.println("Specify the size: ");
			sz = scan.nextInt();
			scan.nextLine();
			size = sz;

			System.out.println("String read: "+s);
			switch(option) {
			case 1:
				FIFO(s);
				break;
			case 2:
				OPT(s);
				break;
			case 3:
				LRU(s);
				break;
			}
			break;
		}
	}
	public static void main(String[] args) {
		System.out.println("lmao");
		String s = "7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1";
		
		size = 3;
		menu();
	}
}
//# replace page that will not be used for longest period of time

/*

LRU
	most recent at the top
	least recent at the bottom
					   a b
					   | |
					   V V
	4 7 0 7 1 0 1 2 1 2 7 1 2

	stack: 4,7,0
	stack: 4,0,7 (7 top)
	stack: 4,0,7,1 (1 new)
	stack: 4,7,1,0 (0 top)
	stack: 4,7,0,1 (1 top)
	stack: 4,7,0,1,2 (2 new)
	stack: 4,7,0,2,1 (1 top)
a:	stack: 4,7,0,1,2 (2 top)
b:	stack: 4,0,1,2,7 (7 top)
	stack: 4,0,2,7,1 (1 top)
	stack: 4,0,7,1,2 (2 top)

	SPLAY TREE:
		for memory of typing history
*/
