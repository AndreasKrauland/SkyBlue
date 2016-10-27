import java.util.*;
public class Gnome extends Thread {

	//variables
	public static long time = System.currentTimeMillis();
	public static Object lock = new Object(); //required to be public to be accessed by sky.
	public static Object table = new Object(); // creates a table object
	public static Object table1 = new Object();
	public static Object table2 = new Object();
	public static Object table3 = new Object();
	public static Object movie = new Object();
	public static Object watchedMovie = new Object();
	public static Object bed = new Object(); //bed
	//public static Table[] table = new Table[Main.tableCount];
	public static boolean table1Full = false; // checks whether table is full
	public static boolean table2Full = false;
	public static boolean table3Full = false;
	public static boolean table4Full = false;
	public static boolean stillFillingTables= true;
	
	public static Vector<Object> waitingGnomes = new Vector<Object>();

	int tCount=0;
	int tSize=0;
	
	public static int gnomeCount = 0;
	public static int gnomeCountDinner = 0;
	public static boolean doorLocked =true; 
	public static int sittingAtTable1= 0;
	public static int sittingAtTable2= 0;
	public static int sittingAtTable3= 0;
	public static int sittingAtTable4= 0;
	
	public static int totalTables = 0;
	public static int dinnerCounter = 0;
	public static int highestTable = 0;
	Random rand = new Random();
	String name;
	int num_gnome;

	
	//constructor
	public Gnome(String name){
		setName(name);
	}
	//methods
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName()+":"+m);
		 }
	
	public void gnomeWait(){ // puts a lock on the monitor
		try {
			synchronized(lock){
				lock.wait();
			}
		} catch (InterruptedException e) {
			msg("Ready to walk in!");
			e.printStackTrace();
		}
	}
	
	public void alertGnome(){ // releases the lock on the monitor
		synchronized(lock){
			lock.notify();
		}
	}
	
	public void goHome(){
		msg("is going home from work");
		try{
			java.lang.Thread.sleep((long) rand.nextInt(1000));  //gnomes sleep for random times.
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void signalOthers(){//service method
		Main.gnomeArrival.notify();  //notifies the first gnome.
		Main.gnomeArrival.remove(); //removes first
	}
	
	//--------------------giant method separator---------------------
	//-------------------------------------------------------
	public synchronized void waitForEveryone(){
		while(doorLocked){ // conditional statement. when door opens, gnomes get to see sky
		if(gnomeCount <(Main.num_gnomes-1)){
			gnomeCount++;
			msg("not everyone is here, there are " + gnomeCount + " gnomes waiting");
			this.gnomeWait();
		}
		else{
			msg("Ahh! Everyone's here");
			doorLocked = false; // opens the door
			while(gnomeCount != 0){ // last gnome to arrive gets here.  
				this.alertGnome(); 
				gnomeCount--;
			}
			try {
				java.lang.Thread.sleep(100);// allows gnomes to get ready before sky starts telling a story
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			msg("Lets wakeup sky!"); // wakes up sky after all of the gnomes have been alerted.
			synchronized(Sky.lock){
				Sky.lock.notify();
			}
		}
	}
		
	}
	//---------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	
	public void storyTime(){
		msg("Time for a story!");
		synchronized(lock){
			try {
				lock.wait(); // wats for sky to tell a story
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//----------------------------------------------------------------
	//-------------------------------------------------------
	public void addToTable(){
		gnomeCountDinner++;
		if(gnomeCountDinner == Main.num_gnomes){
			synchronized(Sky.lock){
				Sky.lock.notify();
			}
		}
			synchronized(table){ //really just table 1
				if(table1Full == false && stillFillingTables){// if full go to next table
					if(gnomeCountDinner == Main.num_gnomes){  // each gnome gets to eat. once all gnomes are in a group, stillfillingtables=false
						stillFillingTables = false;
					}
					try {
						sittingAtTable1++; // how many gnomes are sitting at a table
						if(sittingAtTable1 ==3){  // at this point if all 3 seats are taken, the next gnome takes the next table
							table1Full = true;
						}
						msg("takes a seat at table 1 ");
						table.wait(); // waits for sky to call for dinner
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			}
			synchronized(table1){ //really just table 2
				if(table2Full == false && stillFillingTables){
					if(gnomeCountDinner == Main.num_gnomes){
						stillFillingTables = false;
					}
					try {
						sittingAtTable2++;
						if(sittingAtTable2 ==3){
							table2Full = true;
						}
						msg("takes a seat at table 2");
						table1.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			synchronized(table2){ //really just table 3
				if(table3Full == false && stillFillingTables){
					if(gnomeCountDinner == Main.num_gnomes){
						stillFillingTables = false;
					}
					try {
						sittingAtTable3++;
						if(sittingAtTable3 ==3){
							table3Full = true;
						}
						msg("takes a seat at table 3");
						table2.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			synchronized(table3){ //really just table 4
				if(table4Full == false && stillFillingTables){
					if(gnomeCountDinner == Main.num_gnomes){
						stillFillingTables = false;
					}
					try {
						sittingAtTable4++;
						if(sittingAtTable4 ==3){
							table4Full = true;
						}
						msg("takes a seat at table 4");
						table3.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				java.lang.Thread.sleep((long) rand.nextInt(1000)); 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
	//-----------------------------------------------------------------------
	public void eatDinner(){
		msg("is eating!");
		try {
			java.lang.Thread.sleep((long) rand.nextInt(2000)); 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dinnerCounter++;
		if(dinnerCounter % 3 == 0 || dinnerCounter == Main.num_gnomes){ // if this is the last gnome to leave, signal sky and reset counter
			synchronized(Sky.lock){
				Sky.lock.notify();
			}
		}
	}
	
	public void movieTime(){
		while(Sky.movieRunNum !=2){//all notifications will go back to here
			synchronized(movie){ //initially waits to get notified for a seat
				try {
					movie.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(Sky.numWatching !=3 && Sky.movieRunNum !=2){//if there are still seats left,
					synchronized(movie){//num watching gets updated in sky
						try {
							msg("Yay I got a spot!");
							movie.wait(); 
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					synchronized(watchedMovie){ // so that these dont go into the moviequeue when(notifyall
						try{
						watchedMovie.wait(); 
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					
				}
			}
		}
	}
	
	public void goToBed(){
		synchronized(bed){
			try {
				msg("Goodnight!");
				bed.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msg("I'm up!");
	}
	
	public synchronized void exchange(){
		final Object diamond = new Object();
		synchronized (diamond){
			waitingGnomes.add(diamond);

		}

		msg("Hands a diamond!");
		try {
			java.lang.Thread.sleep(1); // watches movie
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void goWork(){
		try {
			java.lang.Thread.sleep(1000); // waits for gnomes to catch up
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg("Off to work we go!");
	}
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------RUN
	//run
	public void run(){
		msg("has started");
		this.goHome();
		this.waitForEveryone();
		this.storyTime(); //waits for sky to tell story
		this.addToTable();
		this.eatDinner();
		this.movieTime();
		this.goToBed();
		this.exchange();
		this.goWork();
	}
}
