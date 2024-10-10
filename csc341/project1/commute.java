import java.lang.Thread;
import java.lang.Runnable;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.concurrent.locks.*;
/*main method*/
public class commute {
	private static Lock lock = new ReentrantLock();
	public static void main(String[] args) {
		//System.out.println("run it");
		
		//bus
		Bus metro55 = new Bus(53,"metro-55.txt");//# of stops,filename
		Bus bbb3 = new Bus(38,"bbb-3.txt");
		Bus bbb7 =  new Bus(60,"bbb-7.txt");
		Bus bbb10r =  new Bus(60,"bbb-r10.txt");
		Bus bbb12r =  new Bus(60,"bbb-r12.txt");
		Bus bbb16 =  new Bus(60,"bbb-16.txt");

		//train
		Rail railA = new Rail(44,"rail-A-line.txt");
		Rail railB = new Rail(14,"rail-B-line.txt");
		Rail railC = new Rail(16,"rail-C-line.txt");
		Rail railD = new Rail(8,"rail-D-line.txt");
		Rail railE = new Rail(39,"rail-E-line.txt");
		




		//create threads
		Thread thread1 = new Thread(metro55);
		Thread thread2 = new Thread(bbb3);
		Thread thread3 = new Thread(bbb7);
		Thread thread4 = new Thread(bbb10r);
		Thread thread5 = new Thread(bbb12r);
		Thread thread6 = new Thread(bbb16);
		Thread thread7 = new Thread(railA);
		Thread thread8 = new Thread(railB);
		Thread thread9 = new Thread(railC);
		Thread thread10 = new Thread(railD);
		Thread thread11 = new Thread(railE);

		//start threads
		lock.lock();
		try {
			//bus
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			thread5.start();
			thread6.start();
			thread7.start();
			thread8.start();
			thread9.start();
			thread10.start();
			thread11.start();


			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
			thread5.join();
			thread6.join();
			thread7.join();
			thread8.join();
			thread9.join();
			thread10.join();
			thread11.join();

			//rail
			thread7.join();
		} catch(Exception ex) {
			//String exception = ex.toString();
		} finally {
			lock.unlock();
		}
		System.out.println("..................All threads have completed!!!");
	}
}
/*bus class*/
class Bus implements Runnable {
	private static Lock lock = new ReentrantLock();
	int i;
	int stops;
	int curr_delay;
	int next_delay;
	static final int max = 5;//5 mins
	static final int min = 1;//1 min
	static final int m = 60000;
	static final int s = 1000;
	String name;
	String object;
	String curr_stop;
	String next_stop;
	File file;
	Scanner input;
	Random rand;
	/*
	@param f number of stops
	*/
	Bus(int n,String s) {
		lock.lock();
		object = "bus";
		this.i = 0;
		this.stops = n;
		this.name = s.substring(0,s.length()-4);
		this.rand = new Random();
		try {
			file = new File(s);
			input = new Scanner(file);
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		finally {
			lock.unlock();
		}
		
	}
	@Override
	public void run() {
		try {
			lock.lock();
			curr_delay = rand.nextInt(max - min + 1) + min;
			next_delay = curr_delay;
			//Thread.sleep(curr_delay*150);
			if(input.hasNext())
					next_stop = input.nextLine();
			lock.unlock();
			for(; i < stops; i+=1) {
				lock.lock();
				//System.out.print(i+" ");
				curr_delay = next_delay;
				curr_stop = next_stop;
				
				
				//next stop
				if(input.hasNext())
					next_stop = input.nextLine();

				//next stop
				next_delay = rand.nextInt(max - min + 1) + min;
				
				//bus arrived
				System.out.print(object+" "+name);
				if(i == 0) {
					System.out.print(" has departed from: [");
				} else {
					System.out.print(" arrived at: [");
				}
				System.out.print(curr_stop+"] ");
				if(i == stops-1) {
					System.out.println("Final Destination");
					lock.unlock();
				} else {
					System.out.print("next stop: [");
					System.out.print(next_stop+"] ETA: ");
				
					//next stop
					System.out.println(curr_delay+" min"+(curr_delay==1?"":"s"));
					lock.unlock();
					Thread.sleep(curr_delay*s);//60,000 = 60 secs
				}
				
			}
			lock.lock();
			input.close();
		}
		catch(Exception ex) {

		} finally {
			lock.unlock();
		}
	}
}
/*rail class*/
class Rail extends Bus {
	private static Lock lock = new ReentrantLock();
	Rail(int n, String s) {
		super(n, s);
		this.lock.lock();
		this.object = "train";
		this.lock.unlock();
	}
}
