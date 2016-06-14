package polina;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class GerenciadorInsert {
	private LinkedList<String> listaString;
	private LinkedList<Insert> listaInsert;
	private LinkedList<Metadado> listaMeta;
	private int insertSucesso = 0;
	
	public GerenciadorInsert(LinkedList<String> listaString) {
		this.listaString = listaString;
		this.listaInsert = new LinkedList<>();
		this.listaMeta = new LinkedList<>();
	}
	
	public GerenciadorInsert() {
		this.listaInsert = new LinkedList<>();
		this.listaMeta = new LinkedList<>();
	}

	public LinkedList<Insert> extrairInsert() {
		while(!listaString.isEmpty()) {
			LinkedList<Campo> camposInsert = new LinkedList<>();
			
			String insertString = listaString.removeFirst();
			String[] insertExtraido = insertString.split("[|]");
			String[] campoMetaExtraido = insertExtraido[0].split("[ ,]");
			String[] campoMeta = retiraNomeTable(campoMetaExtraido);
			String[] campoValuesWithSpace = insertExtraido[1].split("[,]");
			String[] campoValues = retiraEspacos(campoValuesWithSpace);
			Metadado novoMetadado = Arquivo.lerArquivoMet(campoMetaExtraido[0]);
			
			
			
			ByteArrayOutputStream bufferPos = new ByteArrayOutputStream(novoMetadado.getQuantidadeVarchar());
			ByteArrayOutputStream bufferCampoFixo = new ByteArrayOutputStream(novoMetadado.getQuantidadeInt() + novoMetadado.getQuantidadeChar() + novoMetadado.getQuantidadeBool());
			ByteArrayOutputStream bufferCampoVar = new ByteArrayOutputStream();
			
			ByteArrayOutputStream bufferCampos = new ByteArrayOutputStream();
			
			LinkedList<Integer> deslocamentos = new LinkedList<>();
			Campo campoMetadado;
			
			if(!listaMeta.contains(novoMetadado)) {
				listaMeta.add(novoMetadado);
			}
			Insert insert = new Insert();
			insert.setNomeArquivo(novoMetadado.getNomeArquivo());
			int bitmap = getBitMap(novoMetadado, campoMeta);
			insert.setBitMap(bitmap, novoMetadado);
			
			int iterator;
			int i = 0;
			int j = 0;
			int primeiroVary = 0;
			int flag = 0;
			//int indice = 0;
			
			boolean[] bynaryArray = getBynaryArry(bitmap, novoMetadado.getCampos().size());
			
			while(i < novoMetadado.getCampos().size()) {
				campoMetadado = novoMetadado.getCampos().get(i);
				String tipo = campoMetadado.getTipo();
				
				if(bynaryArray[i]) {
					Campo campoInsert = new Campo(campoMeta[j]);
					campoInsert.setValue(campoValues[j]);
					if((tipo.equals("VARCHAR") || tipo.equals("CHAR")) && campoInsert.getValue().length() > campoMetadado.getTamanho())
						flag = 1;
					
					try {
						if(tipo.equals("VARCHAR")) {
							campoInsert.setTamanho(campoInsert.getStringValue().length());
							bufferCampoVar.write(Serializer.toByteArrayString(campoInsert.getStringValue()));
							deslocamentos.add(campoInsert.getStringValue().length());
						}
						else if(tipo.equals("INTEGER")){
							bufferCampoFixo.write(Serializer.toByteArrayInt(campoInsert.getIntegerValue()));
							primeiroVary += 4;
						}
						else if(tipo.equals("BOOLEAN")){
							bufferCampoFixo.write(Serializer.toByteArrayBool(campoInsert.getBooleanValue()));
							primeiroVary += 1;
						}
						else if(tipo.equals("CHAR")){
							campoInsert.setTamanho(campoInsert.getCharValue().length());
							iterator = 0;
							while(iterator < campoMetadado.getTamanho() - campoInsert.getTamanho()) {
								bufferCampoFixo.write(Serializer.toByteArrayChar("#"));
								iterator++;
							}
							bufferCampoFixo.write(Serializer.toByteArrayChar(campoInsert.getCharValue()));
							primeiroVary += campoMetadado.getTamanho();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					camposInsert.add(campoInsert);
					j++;
				}
				else {
					try {
						if(tipo.equals("VARCHAR")) {
							deslocamentos.add(0);
							//bufferPos.write(Serializer.toByteArrayShort(0));
						}
						else if(tipo.equals("INTEGER")){
							bufferCampoFixo.write(Serializer.toByteArrayInt(0));
							primeiroVary += 4;
						}
						else if(tipo.equals("BOOLEAN")){
							bufferCampoFixo.write(Serializer.toByteArrayBool(false));
							primeiroVary += 1;
						}
						else if(tipo.equals("CHAR")){
							iterator = 0;
							while(iterator < campoMetadado.getTamanho()) {
								bufferCampoFixo.write(Serializer.toByteArrayChar("#"));
								iterator++;
							}
							primeiroVary += campoMetadado.getTamanho();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				i++;
			}
			
			insert.setListaContents(camposInsert);
			
			
			primeiroVary += insert.getBitMapSerialized().length;
			
			primeiroVary += deslocamentos.size() * 4;
			while(!deslocamentos.isEmpty()) {
				try {
					if(deslocamentos.peek() != 0) {
						bufferPos.write(Serializer.toByteArrayShort(primeiroVary));
						bufferPos.write(Serializer.toByteArrayShort(deslocamentos.peek()));
						primeiroVary += deslocamentos.removeFirst();
					} else{
						bufferPos.write(Serializer.toByteArrayShort(0));
						bufferPos.write(Serializer.toByteArrayShort(deslocamentos.peek()));
						primeiroVary += deslocamentos.removeFirst();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				bufferCampos.write(bufferPos.toByteArray());
				bufferCampos.write(bufferCampoFixo.toByteArray());
				bufferCampos.write(insert.getBitMapSerialized());
				bufferCampos.write(bufferCampoVar.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(bitmap != 0 && campoMeta.length == campoValues.length && flag != 1) {
				insert.setListaContentsSerialized(bufferCampos.toByteArray());
				listaInsert.add(insert);
				insertSucesso++;
			}
			flag = 0;
		}
		return listaInsert;
	}

	private String[] retiraEspacos(String[] campoValuesWithSpace) {
		int i = 0;
		String[] novo = new String[campoValuesWithSpace.length];
		int j = 0;
		for(String s : campoValuesWithSpace) {
			String temp = "";
			for(i = 0 ; i < s.length() ; i++ ) {
				if(s.charAt(0) == ' ' && s.charAt(s.length()-1) == ' ') {
					temp = temp.concat(s.substring(1, s.length()-1));
					novo[j] = temp;
					temp = new String();
					i = s.length();
				}
				else if(s.charAt(0) == ' ' && i == 0 ) {
					temp = temp.concat(s.substring(1, s.length()));
					novo[j] = temp;
					temp = new String();
					i = s.length();
				}
				else if(s.charAt(i) == ' ' && i == s.length()-1) {
					temp = temp.concat(s.substring(0, s.length()-1));
					novo[j] = temp;
					temp = new String();
					i = s.length();
				}
				else {
					temp = temp.concat(s);
					novo[j] = temp;
					temp = new String();
				}
				j++;
			}
		}
		return novo;
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

	private String[] retiraNomeTable(String[] campoMetaExtraido) {
		int contaNulos = 0;
		for(String s : campoMetaExtraido){
			if(s.equals("") || s.equals(" ")) {
				contaNulos++;
			}
		}
		String[] retorno = new String[campoMetaExtraido.length-(1 + contaNulos)];
		int i = 0;
		int j = 0;
		for(i = 1; i < campoMetaExtraido.length ; i++) {
			if(!campoMetaExtraido[i].equals("")) {
				retorno[j] = campoMetaExtraido[i];
				j++;
			}
		}
		return retorno;
	}

	private int getBitMap(Metadado meta, String[] campoMeta) {
		int bitmap = 0;
		int indiceCampoMeta;
		int indiceCampoInsert;
		int indiceMeta = listaMeta.indexOf(meta);
		int temp=0;
		for(indiceCampoMeta = 0 ; indiceCampoMeta < listaMeta.get(indiceMeta).getCampos().size() ;) {
			for(indiceCampoInsert = 0 ; indiceCampoInsert < campoMeta.length ; indiceCampoInsert++) {
				if(listaMeta.get(indiceMeta).getCampos().get(indiceCampoMeta).equals(new Campo(campoMeta[indiceCampoInsert]))) {
					temp = 0;
					bitmap = (bitmap << 1);
					temp += bitmap | 1;
					bitmap = temp;
					indiceCampoMeta++;
					
				}
				else {
					bitmap = (bitmap << 1);
					indiceCampoMeta++;
					if(indiceCampoMeta == listaMeta.get(indiceMeta).getCampos().size())
						indiceCampoInsert = campoMeta.length;
					indiceCampoInsert--;
				}
			}
			if(campoMeta.length == 0)
				indiceCampoMeta++;
		}
		return bitmap;
	}
	
	public int getInsertSucesso() {
		return insertSucesso;
	}

	public LinkedList<Metadado> getListaMeta() {
		return listaMeta;
	}

	public void setListaMeta(LinkedList<Metadado> listaMeta) {
		this.listaMeta = listaMeta;
	}
	
	public void addMeta(Metadado meta) {
		this.listaMeta.add(meta);
	}
	
	public Metadado primeiroMeta() {
		return this.listaMeta.peek();
	}

	public LinkedList<Insert> getListaInsert() {
		return listaInsert;
	}

	public void setListaInsert(LinkedList<Insert> listaInsert) {
		this.listaInsert = listaInsert;
	}

	public void addInsert(Insert removeFirst) {
		listaInsert.add(removeFirst);
	}

	public LinkedList<Insert> recuperaInsert(ArquivoBinario arquivoRecuperado) {
		LinkedList<Insert> listaRetorno = new LinkedList<>();
		Metadado metadado = listaMeta.peek();
		int i = 0;
		int posicaoFinal = 2000;
		//Insert insert;
		while(i < arquivoRecuperado.getCabecalho().getRegistros()) {
			Insert insert = new Insert();
			Offset offset = arquivoRecuperado.getCabecalho().getDeslocamentoArquivos().get(i);
			insert.setListaContentsSerialized(getRegistroSerializado(offset.getOffiset(), arquivoRecuperado.getContent(), posicaoFinal));
			posicaoFinal = offset.getOffiset();
			LinkedList<Campo> listaCampos = insert.deserializeContents(metadado);
			insert.setListaContents(listaCampos);
			listaRetorno.add(insert);
			i++;
		}
		arquivoRecuperado.setRegistros(i);
		return listaRetorno;
	}
	
	private byte[] getRegistroSerializado(int aPartir, byte[] arquivoBinario, int tamanho) {
		byte[] registros = new byte[tamanho-aPartir];
		int i=aPartir;
		int j=0;
		for(j=0 ; j < tamanho-aPartir; j++) {
			registros[j] = arquivoBinario[i];
			i++;
		}
		return registros;
	}

}
