package polina;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class CabecalhoArquivo {
	private short registros = 0;
	private short excluidos = 0;
	private short primeiroLivre = 2000;
	private LinkedList<Offiset> deslocamentoArquivos;
	private byte[] dadosSerializados;
	
	public CabecalhoArquivo() {
		serializarDadosFixos();
	}
	public CabecalhoArquivo(short registros, short excluidos, short primeiroLivre, LinkedList<Offiset> deslocamentoArquivos) {
		this.registros = registros;
		this.excluidos = excluidos;
		this.primeiroLivre = primeiroLivre;
		this.deslocamentoArquivos = deslocamentoArquivos;
		this.dadosSerializados = new byte[6 + (registros * 2)];
		serializarDadosFixos();
	}
	
	private void serializarDadosFixos() {
		ByteArrayOutputStream output = new ByteArrayOutputStream(6 + (registros * 2));	
		try {
			output.write(Serializer.serialize(this.registros));
			output.write(Serializer.serialize(this.excluidos));
			output.write(Serializer.serialize(this.primeiroLivre));
			int i = 0;
			while(i < registros) {
				output.write(Serializer.serialize(this.deslocamentoArquivos.get(i)));
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

	public LinkedList<Offiset> getDeslocamentoArquivos() {
		return deslocamentoArquivos;
	}

	public void setDeslocamentoArquivos(LinkedList<Offiset> deslocamentoArquivos) {
		this.deslocamentoArquivos = deslocamentoArquivos;
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
}
