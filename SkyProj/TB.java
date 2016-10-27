import java.io.InputStream;
import java.io.OutputStream;

public class TB extends Thread {

	// to write raw bytes
	private InputStream isFromA;
	private InputStream isFromC;

	private OutputStream osToA;

	//Receives
	//A to B Primitives  A-pos3 to B-pis3
	//C to B primitives  C-pos5 to B-pis5

	//Sends
	//B to A primitives  B-pos4 to A-pis4

	public TB(InputStream isFromA, InputStream isFromC, OutputStream osToA) {
		super("Thread B");
		this.isFromA = isFromA;
		this.isFromC = isFromC;
		this.osToA = osToA;
	}

	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
	}
	
	public void run() {

		try {
			for (int i = 0; i < driver.numIterations; i++) {
				int valueFromA = isFromA.read();
				msg("Received Primitive From A to B: value = " + valueFromA);

				Thread.sleep(driver.delayTime);
				
				int valueFromC = (int) isFromC.read();
				msg("Received Primitive From C to B: value = " + valueFromC);

				Thread.sleep(driver.delayTime);
				
				msg("Sending Primitive From B to A: value = " + i);
				osToA.write(i);
				osToA.flush();

				Thread.sleep(driver.delayTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
