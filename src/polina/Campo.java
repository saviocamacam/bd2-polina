package polina;

public class Campo {
	private String nomeCampo;
	private String tipo;
	private int tamanho;
	
	public Campo(String nomeCampo, String tipo) {
		this.nomeCampo = nomeCampo;
		this.tipo = tipo;
	}
	public Campo(String nomeCampo, String tipo, int tamanho) {
		this.nomeCampo = nomeCampo;
		this.tipo = tipo;
		this.tamanho = tamanho;
	}
	public int getTamanho() {
		return tamanho;
	}
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNomeCampo() {
		return nomeCampo;
	}
	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}
	@Override
	public String toString() {
		if(tamanho == 0) {
			return nomeCampo + " " + tipo +"\n";
		}
		else
			return nomeCampo + " " + tipo + " " + tamanho + "\n";
	}
	
}
