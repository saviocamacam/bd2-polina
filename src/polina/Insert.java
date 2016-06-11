package polina;

import java.io.IOException;
import java.util.LinkedList;

public class Insert {
	private String nomeArquivo;
	private LinkedList<Campo> listaContents;
	private byte[] contentsSerialized;
	private int bitMap;
	private byte[] bitMapSerialized;
	
	public Insert() {
		this.listaContents = new LinkedList<>();
	}	

	public LinkedList<Campo> getListaContents() {
		return listaContents;
	}

	public void setListaContents(LinkedList<Campo> listaContents) {
		this.listaContents.addAll(listaContents);
	}

	public int getBitMap() {
		return bitMap;
	}

	public void setBitMap(int bitMap, Metadado meta) {
		this.bitMap = bitMap;
		setBitMapSerialized(meta);
	}

	public byte[] getListaContentsSerialized() {
		return contentsSerialized;
	}

	public void setListaContentsSerialized(byte[] listaContentsSerialized) {
		this.contentsSerialized = listaContentsSerialized;
	}

	public byte[] getBitMapSerialized() {
		return bitMapSerialized;
	}

	public void setBitMapSerialized(Metadado meta) {
		
		this.bitMapSerialized = cutBitMap(getQt(meta.getCampos().size()));
	}
	
	private byte[] cutBitMap(int qt) {
		byte[] cutted = new byte[qt];
		try {
			byte[] nonCutted = Serializer.toByteArrayInt(bitMap);
			int i;
			int j = nonCutted.length-1;
			for(i= qt-1 ; i >= 0; i--) {
				cutted[i] = nonCutted[j];
				j--;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cutted;
	}

	private int getQt(int qtCampo) {
		int i = 0;
		if(qtCampo <= 8) {
			i = 1;
		}
		else if(qtCampo > 8 && qtCampo <= 16) {
			i = 2;
		}
		else if(qtCampo > 16 && qtCampo <= 24) {
			i = 3;
		}
		return i;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	@Override
	public String toString() {
		String temp = "";
		while(!listaContents.isEmpty()) {
			temp += listaContents.removeFirst().getValue() + " ";
		}
		return temp;
	}

	public LinkedList<Campo> deserializeContents(Metadado metadado) {
		int i = 0;
		int aPartir = 0;
		int bitMap = recuperaBitmap(metadado.getQuantidadeTotal(), metadado.getCampos().size());
		this.bitMap = bitMap;
		LinkedList<Campo> recuperados = new LinkedList<>();
		boolean[] binaryArray = getBynaryArry(bitMap, metadado.getCampos().size());
		while(i < metadado.getCampos().size()) {
			Campo campoMetadado = metadado.getCampos().get(i);
			String tipo = campoMetadado.getTipo();
			
			if(binaryArray[i]) {
				
					if(tipo.equals("VARCHAR")) {
						Offiset deslocamento = null;
						byte[] byteDeslocamento = new byte[2];
						byte[] tamDeslocamento = new byte[2];
						try {
							byteDeslocamento = getRegistroSerializado(aPartir, 2);
							aPartir += 2;
							tamDeslocamento = getRegistroSerializado(aPartir, 2);
							deslocamento = new Offiset(Serializer.toShortByteArray(byteDeslocamento), Serializer.toShortByteArray(tamDeslocamento));
							aPartir += 2;
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						byte[] varcharByteArrayRecuperado = getRegistroSerializado(deslocamento.getOffiset(), deslocamento.getTamDeslocamento());
						try {
							String varcharRecuperado = Serializer.toStringByteArray(varcharByteArrayRecuperado);
							Campo campo = new Campo(campoMetadado.getNomeCampo());
							campo.setValue(varcharRecuperado);
							recuperados.add(campo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if(tipo.equals("INTEGER")){
						byte[] integerByteArrayRecuperado = getRegistroSerializado((campoMetadado.getOffBase()-4), 4);
						try {
							int integerRecuperado = Serializer.toIntByteArray(integerByteArrayRecuperado);
							Campo campo = new Campo(campoMetadado.getNomeCampo());
							campo.setValue(Integer.toString(integerRecuperado));
							recuperados.add(campo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if(tipo.equals("BOOLEAN")){
						byte[] booleanByteArrayRecuperado = getRegistroSerializado((campoMetadado.getOffBase()-1) , 1);
						try {
							boolean booleanRecuperado = Serializer.toBoolByteArray(booleanByteArrayRecuperado);
							Campo campo = new Campo(campoMetadado.getNomeCampo());
							campo.setValue(Boolean.toString(booleanRecuperado));
							recuperados.add(campo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if(tipo.equals("CHAR")){
						byte[] charByteArrayRecuperado = getRegistroSerializado((campoMetadado.getOffBase()-campoMetadado.getTamanho()) , campoMetadado.getTamanho());
						try {
							String charRecuperado = Serializer.toStringByteArray(charByteArrayRecuperado);
							Campo campo = new Campo(campoMetadado.getNomeCampo());
							campo.setValue(charRecuperado);
							recuperados.add(campo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
			else {
				if(tipo.equals("VARCHAR")) {
					aPartir += 4;
				}
				else if(tipo.equals("INTEGER")){
					
				}
				else if(tipo.equals("BOOLEAN")){

				}
				else if(tipo.equals("CHAR")){

				}
			}
			i++;
		}
		return recuperados;
		
	}
	
	private int recuperaBitmap(int quantidadeTotal, int qtCampo) {
		int bitmap = 0;
		byte[] bitmapSerialized = new byte[getQt(qtCampo)];
		int i = 0;
		while(i < getQt(qtCampo)) {
			bitmapSerialized[i] = contentsSerialized[quantidadeTotal++];
			i++;
		}
		try {
			if(getQt(qtCampo) == 1) {
				byte aux = bitmapSerialized[0];
				bitmapSerialized = new byte[2];
				bitmapSerialized[1] = aux;
			}
			bitmap = Serializer.toShortByteArray(bitmapSerialized);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}

	private byte[] getRegistroSerializado(int aPartir, int tamanho) {
		byte[] registros = new byte[tamanho];
		int i=aPartir;
		int j=0;
		for(j=0 ; j < tamanho; j++) {
			registros[j] = contentsSerialized[i];
			i++;
		}
		return registros;
	}
	
	private boolean[] getBynaryArry(int bitmap, int qtCampos) {
		String maxAmpStr = Integer.toBinaryString(bitmap);
	    char[] arr = maxAmpStr.toCharArray();
	    boolean[] binaryarray = new boolean[qtCampos];
	    for (int i=0; i<maxAmpStr.length(); i++){
	        if (arr[i] == '1'){             
	            binaryarray[i] = true;  
	        }
	        else if (arr[i] == '0'){
	            binaryarray[i] = false; 
	        }
	    }
		return binaryarray;
	}

	public Campo getCampoNome(String nomeCampo) {
		Campo campo = listaContents.get(listaContents.indexOf(new Campo(nomeCampo)));
		return campo;
	}
}
