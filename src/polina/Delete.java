package polina;

public class Delete {
	
	private Campo campo;
	private String nomeArquivo;
	
	public Delete(Campo campo) {
		this.campo = campo;
	}

	public Campo getCampo() {
		return campo;
	}

	public void setCampo(Campo campo) {
		this.campo = campo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
