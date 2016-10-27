import java.io.Serializable;

@SuppressWarnings("serial")
class Message implements Serializable{
	public int number;
	public int id;

	public Message(int number, int id) {
		this.number = number;
		this.id = id;
	}
	
	public String toString(){
		return number + " " + id;
	}
	
	
}