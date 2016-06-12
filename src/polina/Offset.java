package polina;

public class Offset {
	private short offiset;
	private short tamDeslocamento;
	private byte[] offisetSerialized;
	
	public Offset(short offiset, short tamDeslocamento) {
		this.offiset = offiset;
		this.tamDeslocamento = tamDeslocamento;
	}
	
	public Offset(short offiset) {
		this.offiset = offiset;
	}
	public short getOffiset() {
		return offiset;
	}
	public void setOffiset(short offiset) {
		this.offiset = offiset;
	}
	public byte[] getOffisetSerialized() {
		return offisetSerialized;
	}
	public void setOffisetSerialized(byte[] offisetSerialized) {
		this.offisetSerialized = offisetSerialized;
	}
	public short getTamDeslocamento() {
		return tamDeslocamento;
	}
	public void setTamDeslocamento(short tamDeslocamento) {
		this.tamDeslocamento = tamDeslocamento;
	}

}
