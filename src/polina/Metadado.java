package polina;

import java.util.LinkedList;

public class Metadado {
	private String nomeArquivo;
	private LinkedList<Campo> campos;
	
	public Metadado(String nomeArquivo, LinkedList<Campo> campos) {
		this.nomeArquivo = nomeArquivo;
		this.campos = campos;
	}

	public LinkedList<Campo> getCampos() {
		return campos;
	}

	public void setCampos(LinkedList<Campo> campos) {
		this.campos = campos;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
