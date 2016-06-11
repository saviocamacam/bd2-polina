package polina;

import java.io.ByteArrayOutputStream;

public class ArquivoBinario {
	private String nomeArquivo;
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

}
