package polina;

import java.util.LinkedList;

public class Metadado {
	private String nomeArquivo;
	private LinkedList<Campo> campos;
	private int quantidadeVarchar;
	private int quantidadeInt;
	private int quantidadeChar;
	private int quantidadeBool;
	private int quantidadeTotal;
	
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
	
	@Override
	public boolean equals(Object o) {
		return this.nomeArquivo.equals(((Metadado) o).getNomeArquivo());
	}

	public int getQuantidadeVarchar() {
		return quantidadeVarchar;
	}

	public void setQuantidadeVarchar(int quantidadeVarchar) {
		this.quantidadeVarchar = quantidadeVarchar;
	}
	
	public int getQuantidadeInt() {
		return quantidadeInt;
	}

	public void setQuantidadeInt(int quantidadeInt) {
		this.quantidadeInt = quantidadeInt;
	}

	public int getQuantidadeChar() {
		return quantidadeChar;
	}

	public void setQuantidadeChar(int quantidadeChar) {
		this.quantidadeChar = quantidadeChar;
	}

	public int getQuantidadeBool() {
		return quantidadeBool;
	}

	public void setQuantidadeBool(int quantidadeBool) {
		this.quantidadeBool = quantidadeBool;
	}

	public void setQuantidadeTotal(int i) {
		this.quantidadeTotal = i;
	}

	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}

}
