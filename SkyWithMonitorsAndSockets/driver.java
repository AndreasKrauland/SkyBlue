import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class driver {

	public static int delayTime = 200; //adjust this to see the message passing in real time or faster
	public static int numIterations = 50; // adjust these to decide how many times the messages should be passed
	
	public static PipedInputStream pis1;
	public static PipedOutputStream pos1;

	public static PipedInputStream pis2;
	public static PipedOutputStream pos2;

	public static PipedInputStream pis3;
	public static PipedOutputStream pos3;

	public static PipedInputStream pis4;
	public static PipedOutputStream pos4;

	public static PipedInputStream pis5;
	public static PipedOutputStream pos5;

	public static ObjectOutputStream oos;
	public static ObjectInputStream ois;

	public static void main(String[] args) {
		try {
			// set up a pipe
			System.out.println("Pipe setup");
			pos1 = new PipedOutputStream(); // A sends
			pis1 = new PipedInputStream(pos1); // C Receives

			pos2 = new PipedOutputStream(); // C sends
			pis2 = new PipedInputStream(pos2); // A Receives

			pos3 = new PipedOutputStream(); // A sends
			pis3 = new PipedInputStream(pos3); // B Receives

			pos4 = new PipedOutputStream(); // B sends
			pis4 = new PipedInputStream(pos4); // A receives

			pos5 = new PipedOutputStream(); // C sends
			pis5 = new PipedInputStream(pos5); // B Receives

			//A to C Objects     A-pos1 C-pis1
			//C to A Objects 	 C-pos2 A-pis2
			//A to B Primitives  A-pos3 to B-pis3
			//B to A primitives  B-pos4 to A-pis4
			//C to B primitives  C-pos5 to B-pis5

			System.out.println("Object creation");
			TA ta = new TA(pis4, pis2, pos3, pos1, oos, ois);
			TB tb = new TB(pis3, pis5, pos4);
			TC tc = new TC(pis1, pos2, pos5, ois, oos);

			System.out.println("Thread execution");
			ta.start();
			tb.start();
			tc.start();
		} catch (Exception exc) {
			System.out.println(exc);
		} // end CATCH
	}
}
