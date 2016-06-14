package polina;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArquivoBinario {
	private String nomeArquivo;
	private int registros;
	private CabecalhoArquivo cabecalho;
	private byte[] content;
	
	public ArquivoBinario(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public ArquivoBinario(String nomeArquivo, int tamArquivo) {
		this.nomeArquivo = nomeArquivo;
		this.cabecalho = new CabecalhoArquivo();
		this.content = new byte[tamArquivo];
		carregaCabecalho();
	}

	private void carregaCabecalho() {
		ByteArrayOutputStream output = new ByteArrayOutputStream(2000);
		output.write(cabecalho.getDadosSerializados(), 0, 6 +(cabecalho.getRegistros() * 2));
		byte[] livres = new byte[2000 - (cabecalho.getDadosSerializados().length)];
		try {
			output.write(livres);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.content = output.toByteArray();
	}
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
		this.cabecalho = new CabecalhoArquivo(content);
	}

	public CabecalhoArquivo getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(CabecalhoArquivo cabecalho) {
		this.cabecalho = cabecalho;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public int getRegistros() {
		return registros;
	}
	public void setRegistros(int registros) {
		this.registros = registros;
	}

}
