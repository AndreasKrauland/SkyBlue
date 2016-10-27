import java.util.ArrayDeque;
import java.util.Scanner;

public class Main {
	public static ArrayDeque<Gnome> gnomeArrival = new ArrayDeque<Gnome>(); 
	public static int num_gnomes;
	public static int table_size;
	public static int tableCount;
	

	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		// TODO Auto-generated method stub
		System.out.println("How many gnomes would you like?");
		num_gnomes = input.nextInt();
		System.out.print("How big are the tables?");
		table_size = input.nextInt();
		tableCount = num_gnomes / table_size;
		input.close();
		
		Gnome gnome[]= new Gnome[num_gnomes]; //initializes gnome object array
		
		//intialize sky
		Sky sky = new Sky();
		sky.start();  //this goes first because it will wait immediately
		
		//initialize gnomes
		for(int x = 0; x < num_gnomes; x++){
			gnome[x] = new Gnome("Gnome" + (x+1));  //creates a gnome object
			gnome[x].start();
		}
		
	}

}
