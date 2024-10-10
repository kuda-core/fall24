import java.lang.Thread;
import java.lang.Runnable;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/*main method*/
public class commute {
	public static void main(String[] args) {
		System.out.println("run it");
		Bus metro55 = new Bus(53,"metro-55.txt");//# of stops,filename
		Bus bbb3 = new Bus(38,"bbb-3.txt");

		//create threads
		Thread thread1 = new Thread(metro55);
		Thread thread2 = new Thread(bbb3);

		//start threads
		thread1.start();
		thread2.start();
	}
}
/*bus class*/
class Bus implements Runnable {
	int i;
	int stops;
	int curr_delay;
	int next_delay;
	static final int max = 5;//5 mins
	static final int min = 1;//1 min
	static final int m = 60000;
	static final int s = 1000;
	String bus_name;
	String curr_stop;
	String next_stop;
	File file;
	Scanner input;
	Random rand;
	/*
	@param f number of stops
	*/
	Bus(int n,String s) {
		this.i = 0;
		this.stops = n;
		this.bus_name = s.substring(0,s.length()-4);
		this.rand = new Random();
		try {
			file = new File(s);
			input = new Scanner(file);
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	@Override
	public void run() {
		try {
			
			
			curr_delay = rand.nextInt(max - min + 1) + min;
			next_delay = curr_delay;
			Thread.sleep(curr_delay*50);
			if(input.hasNext())
					next_stop = input.nextLine();
			for(; i < stops; i+=1) {
				System.out.print(i+" ");
				curr_delay = next_delay;
				curr_stop = next_stop;
				
				
				//next stop
				if(input.hasNext())
					next_stop = input.nextLine();

				//next stop
				next_delay = rand.nextInt(max - min + 1) + min;
				
				//bus arrived
				System.out.print("bus "+bus_name);
				if(i == 0) {
					System.out.print(" has departed from: [");
				} else {
					System.out.print(" arrived at: [");
				}
				System.out.print(curr_stop+"] ");
				if(i == stops-1) {
					System.out.println("Final Destination");
				} else {
					System.out.print("next stop: [");
					System.out.print(next_stop+"] ETA: ");
				
					//next stop
					System.out.println(curr_delay+" min"+(curr_delay==1?"":"s"));
					Thread.sleep(curr_delay*m);//60,000 = 60 secs
				}
				
				
			}
			input.close();
			

		}
		catch(InterruptedException ex) {

		}
	}
}
/*rail class*/
