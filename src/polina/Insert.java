package polina;

import java.util.LinkedList;

public class Insert {
	private LinkedList<String> listaContents;
	private LinkedList<byte[]> listaContentsSerialized;
	private byte bitMap[];
	
	public Insert() {
		
	}

	public LinkedList<String> getListaContents() {
		return listaContents;
	}

	public void setListaContents(LinkedList<String> listaContents) {
		this.listaContents = listaContents;
	}

	public byte[] getBitMap() {
		return bitMap;
	}

	public void setBitMap(byte[] bitMap) {
		this.bitMap = bitMap;
	}

	public LinkedList<byte[]> getListaContentsSerialized() {
		return listaContentsSerialized;
	}

	public void setListaContentsSerialized(LinkedList<byte[]> listaContentsSerialized) {
		this.listaContentsSerialized = listaContentsSerialized;
	}
	
	private void initializeBitmap(int tamCampo) {
		this.bitMap = new byte[tamCampo];
	}

}
