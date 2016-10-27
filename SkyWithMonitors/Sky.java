
public class Sky extends Thread {

	// variables
	public static long time = System.currentTimeMillis();
	public static Object lock = new Object();
	public Object door = new Object();
	boolean dinnerTime = true;
	public static int movieRunNum = 0;
	int tableNumber = 0;
	int gnomeNum = 0;
	int count = 0;
	int countDinner = 0;
	public static int numWatching = 0;
	int totWatched = 0;

	// constructor
	public Sky() {
		setName("Sky");
	}

	// methods
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ":" + m);
	}

	public synchronized void doChoreS() { // essentialy waits for gnomes to walk
											// inside the house.
		msg("doing chores!");
		try {
			synchronized (lock) {
				lock.wait();
				msg("Woke up!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tellStory() {
		msg("Telling a story!!!");
		count = Main.num_gnomes;
		while (count != 0) {
			try {
				java.lang.Thread.sleep(2); // REGARDLESS OF synch MUTEX this is
											// still needed.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (Gnome.lock) {
				Gnome.lock.notify();
			}
			count--;
		}
	}

	public void dinner() {
		try {
			java.lang.Thread.sleep(2000); // watches movie
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countDinner = 0;
		Gnome.stillFillingTables = false;

		while (dinnerTime == true) {
			while (Gnome.sittingAtTable1 > 0) { // if a gnome is sitting at a
												// table
				msg("table 1 is up!");
				synchronized (Gnome.table) {
					countDinner++;
					Gnome.table.notify();
					Gnome.sittingAtTable1--;
				}
			}
			if (dinnerTime = true && countDinner != Main.num_gnomes) {  // only waits if dinner is being served.
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			while (Gnome.sittingAtTable2 > 0) { // table 2
				msg("table 2 is up!");
				synchronized (Gnome.table1) {
					countDinner++;
					Gnome.table1.notify();
					Gnome.sittingAtTable2--;
				}
			}
			if (dinnerTime = true&& countDinner != Main.num_gnomes) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			while (Gnome.sittingAtTable3 > 0) { // table 3
				msg("table 3 is up!");
				synchronized (Gnome.table2) {
					countDinner++;
					Gnome.table2.notify();
					Gnome.sittingAtTable3--;
				}
			}
			if (dinnerTime = true&& countDinner != Main.num_gnomes) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			while (Gnome.sittingAtTable4 > 0) { // table 4
				msg("table 4 is up!");
				synchronized (Gnome.table3) {
					countDinner++;
					Gnome.table3.notify();
					Gnome.sittingAtTable4--;
				}
			}
			if (dinnerTime = true&& countDinner != Main.num_gnomes) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		msg("waiting for last gnome to eat!");
		synchronized (lock) { //waits for last gnome to signal from dinner
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		msg("Dinner is finished!");
	}

	public void movieTime() {
        msg("Movie Time!!");
		while (movieRunNum != 2 && totWatched <= Main.num_gnomes) { // totWatched = number of gnomes who have watched, for gnomes less than 3.
			synchronized (Gnome.movie) {
				msg("first gnome can take a seat");
				numWatching++;
				totWatched++;
				Gnome.movie.notify(); // notifies a gnome to take a seat
			}
			synchronized (Gnome.movie) {
				msg("first gnome can take a seat");
				numWatching++;
				totWatched++;
				Gnome.movie.notify(); // notifies a gnome to take a seat
			}
			synchronized (Gnome.movie) {
				msg("first gnome can take a seat");
				numWatching++;
				totWatched++;
				Gnome.movie.notify(); // notifies a gnome to take a seat
			}
			try {
				java.lang.Thread.sleep(2000); // watches movie
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			movieRunNum++;
			msg("Movie ran: " + movieRunNum + "times");
			synchronized (Gnome.movie) {
				numWatching = 0;
				Gnome.movie.notifyAll();
			}
		}
		synchronized(Gnome.watchedMovie){
			Gnome.watchedMovie.notifyAll();
		}
		synchronized (Gnome.movie) {
			numWatching = 0;
			Gnome.movie.notifyAll();
		}
	  
	}
	
	public void sleepTillMorning(){
		try {
			java.lang.Thread.sleep(5000); // sleeping
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(Gnome.bed){
			Gnome.bed.notifyAll();
		}
	}

	public void exchange(){
		try {
			java.lang.Thread.sleep(2); // waits for gnomes to catch up
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(Gnome.waitingGnomes.size() >0){
			try {
				java.lang.Thread.sleep(1); // needs to have delay between threads
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg("Gives a kiss!");
			Gnome.waitingGnomes.remove(0);
		}
		msg("Goodbye everyone!");
	}
	// run
	public void run() {
		msg("has started!");
		this.doChoreS();
		this.tellStory();
		this.dinner();
		this.movieTime();
		this.sleepTillMorning();
		this.exchange();
	}

}
