package polina;

public class Offiset {
	private short offiset;
	private short tamDeslocamento;
	private byte[] offisetSerialized;
	
	public Offiset(short offiset, short tamDeslocamento) {
		this.offiset = offiset;
		this.tamDeslocamento = tamDeslocamento;
	}
	
	public Offiset(short offiset) {
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
