import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class TC extends Thread {
	// to write Objects
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	// to write raw bytes
	private InputStream isFromA;

	private OutputStream osToA;
	private OutputStream osToB;

	//A to C Objects - A-pos1 C-pis1
	//C to A Objects - C-pos2 A-pis2
	//C to B primitives  C-pos5 to B-pis5

	public TC(InputStream isFromA, OutputStream osToA, OutputStream osToB, ObjectInputStream ois,
			ObjectOutputStream oos) {
		super("Thread C");
		this.isFromA = isFromA;
		this.osToA = osToA;
		this.osToB = osToB;
		this.ois = ois;
		this.oos = oos;
	}
	
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
	}

	public void run() {

		try {
			ois = new ObjectInputStream(isFromA);
			oos = new ObjectOutputStream(osToA);

			for (int i = 0; i < driver.numIterations; i++) {
				Message m = (Message) ois.readObject();
				msg("Received Message Object From A to C: id = " + m.id + ", number = " + m.number);

				Thread.sleep(driver.delayTime);
				
				msg("Sending Primitive From C To B: value = " + i);
				osToB.write(i);
				osToB.flush();

				Thread.sleep(driver.delayTime);
				
				Message mToA = new Message(i, 2);
				msg("Sending Message Object From C to A: id = " + mToA.id + ", number = " + mToA.number);
				oos.writeObject(mToA);
				oos.flush();

				Thread.sleep(driver.delayTime);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
