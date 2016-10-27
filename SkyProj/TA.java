import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class TA extends Thread {
	// to write Objects
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	// to write raw bytes
	private InputStream isFromB;
	private InputStream isFromC;

	private OutputStream osToB;
	private OutputStream osToC;

	//sends
	//A to B Primitives  A-pos3 to B-pis3
	//A to C Objects     A-pos1 C-pis1

	//receives
	//B to A primitives  B-pos4 to A-pis4
	//C to A Objects     C-pos2 A-pis2

	public TA(InputStream isFromB, InputStream isFromC, OutputStream osToB, OutputStream osToC, ObjectOutputStream oos,
			ObjectInputStream ois) {
		super("Thread A");
		this.isFromB = isFromB;
		this.isFromC = isFromC;
		this.osToB = osToB;
		this.osToC = osToC;
		this.ois = ois;
		this.oos = oos;
	}
	
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
	}

	public void run() {

		try {
			oos = new ObjectOutputStream(osToC);
			ois = new ObjectInputStream(isFromC);

			for (int i = 0; i < driver.numIterations; i++) {
				msg("Sending Primitive From A To B: value = " + i);
				osToB.write(i);
				osToB.flush();
				
				Thread.sleep(driver.delayTime);

				Message mToC = new Message(i, 0);
				msg("Sending Message Object From A to C: id = " + mToC.id + ", number = " + mToC.number);
				oos.writeObject(mToC);
				oos.flush();

				Thread.sleep(driver.delayTime);
				
				int valueFromB = (int) isFromB.read();
				msg("Received Primitive From B to A: value = " + valueFromB);

				Thread.sleep(driver.delayTime);
				
				Message mFromC = (Message) ois.readObject();
				msg("Received Message Object From C to A: id = " + mFromC.id + ", number = " + mFromC.number);

				Thread.sleep(driver.delayTime);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
