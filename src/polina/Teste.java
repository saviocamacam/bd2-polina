package polina;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Teste implements Serializable {
	private int campo1;
	private int campo2;
	private int campo3;
	private int campo4;
	
	public Teste(int campo1, int campo2, int campo3, int campo4) {
		this.campo1 = campo1;
		this.campo2 = campo2;
		this.campo3 = campo3;
		this.campo4 = campo4;
	}
	
	/*public static void main(String args[]) throws IOException {
		FileOutputStream fos = new FileOutputStream("temp.out");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		Teste ts = new Teste(10, 20, 30, 40);
		oos.writeObject(ts);
		oos.flush();
		oos.close();
	}*/

}
