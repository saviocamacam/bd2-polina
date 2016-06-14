/*
 * Para cada arquivo binário gerado, é criado um cabeçalho com as informações pertinentes a ele, como a 
 * quantidade de registros inseridos, quantidade de excluidos, deslocamento para o primeiro byte livre
 * e uma lista de deslocamentos para os arquivos
 * */

package polina;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class CabecalhoArquivo  {
	private short registros;
	private short excluidos;
	private short primeiroLivre;
	private LinkedList<Offset> deslocamentoArquivos;
	private byte[] dadosSerializados;
	
	public CabecalhoArquivo() {
		this.registros = 0;
		this.excluidos = 0;
		this.primeiroLivre = 2000;
		serializarDadosFixos();
	}
	public CabecalhoArquivo(short registros, short excluidos, short primeiroLivre, LinkedList<Offset> deslocamentoArquivos) {
		this.registros = registros;
		this.excluidos = excluidos;
		this.primeiroLivre = primeiroLivre;
		this.deslocamentoArquivos = deslocamentoArquivos;
		this.dadosSerializados = new byte[6 + (registros * 2)];
		serializarDadosFixos();
	}
	
	public CabecalhoArquivo(byte[] arquivoBinario) {
		this.deslocamentoArquivos = new LinkedList<>();
		setCabecalho(arquivoBinario);
	}
	
	/*Dado um arquivo binário, esse método inicializa um cabeçalho com seus valores convertidos para binário*/
	private void setCabecalho(byte[] arquivoBinario) {
		int pos = 0;
		try {
			this.registros = Serializer.toShortByteArray(getRegistroSerializado(pos, arquivoBinario, 2));
			this.excluidos = Serializer.toShortByteArray(getRegistroSerializado((pos += 2), arquivoBinario, 2));;
			this.primeiroLivre = Serializer.toShortByteArray(getRegistroSerializado((pos += 2), arquivoBinario, 2));;
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		while(i < registros) {
			try {
				deslocamentoArquivos.add(new Offset(Serializer.toShortByteArray(getRegistroSerializado((pos += 2), arquivoBinario, 2))));
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
		//serializarDadosFixos();
	}
	
	/*
	 * Dada uma posicao inicial e uma final, esse método retorna o conteúdo nesse intervalo que representa alguma
	 * informação no cabeçalho
	 * */
	private byte[] getRegistroSerializado(int aPartir, byte[] arquivoBinario, int tamanho) {
		byte[] registros = new byte[tamanho];
		int i=aPartir;
		int j=0;
		for(j=0 ; j < tamanho; j++) {
			registros[j] = arquivoBinario[i];
			i++;
		}
		return registros;
	}
	/* Esse método converte todos os valores primitivos relacionados ao cabeçalho agrupando as informações
	 * num vetor de bytes identificado como Dados Serializados
	 * */
	public void serializarDadosFixos() {
		ByteArrayOutputStream output = new ByteArrayOutputStream(6 + (registros * 2));	
		try {
			output.write(Serializer.toByteArrayShort(registros));
			output.write(Serializer.toByteArrayShort(excluidos));
			output.write(Serializer.toByteArrayShort(primeiroLivre));
			int i = 0;
			while(i < registros) {
				short deslocamento = deslocamentoArquivos.get(i).getOffiset();
				output.write(Serializer.toByteArrayShort(deslocamento));
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dadosSerializados = output.toByteArray();
	}

	public short getExcluidos() {
		return excluidos;
	}

	public void setExcluidos(short excluidos) {
		this.excluidos = excluidos;
	}

	public short getPrimeiroLivre() {
		return primeiroLivre;
	}

	public void setPrimeiroLivre(short primeiroLivre) {
		this.primeiroLivre = primeiroLivre;
	}

	public LinkedList<Offset> getDeslocamentoArquivos() {
		return deslocamentoArquivos;
	}

	public void setDeslocamentoArquivos(LinkedList<Offset> deslocamentoArquivos) {
		for(Offset o : deslocamentoArquivos) {
			this.deslocamentoArquivos.add(o);
		}
		//this.deslocamentoArquivos = deslocamentoArquivos;
		serializarDadosFixos();
	}

	public byte[] getDadosSerializados() {
		return dadosSerializados;
	}

	public void setDadosSerializados(byte[] dadosSerializados) {
		this.dadosSerializados = dadosSerializados;
	}

	public short getRegistros() {
		return registros;
	}

	public void setRegistros(short registros) {
		this.registros = registros;
	}
	public short getDiferenca() {
		return (short) (registros-excluidos);
	}
	public void resetDeslocamentos() {
		this.registros = 0;
		this.primeiroLivre = 2000;
		this.deslocamentoArquivos = new LinkedList<>();
	}
}
