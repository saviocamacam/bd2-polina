package polina;

public class Campo {
	private String nomeCampo;
	private String tipo;
	private int tamanho;
	private String value;
	private int offBase;
	
	public Campo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}
	
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
	public String getTipo(String nomeCampo) {
		if(nomeCampo.equals(nomeCampo))
			return tipo;
		else
			return null;
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
			return nomeCampo + " " + tipo + " " + offBase + "\n";
		}
		else
			return nomeCampo + " " + tipo + " " + tamanho + " " + offBase + "\n";
	}
	
	public Integer getIntegerValue() {
		return Integer.valueOf(value);
	}
	
	public String getStringValue() {
		return value;
	}
	
	public boolean getBooleanValue() {
		return Boolean.valueOf(value);
	}
	public String getCharValue() {
		return value;
	}
	
	public int getLenValue() {
		return value.length();
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public boolean equals(Object o) {
		return this.nomeCampo.equals(((Campo) o).getNomeCampo());
	}

	public String getValue() {
		return value;
	}

	public void setOffBase(int offBase) {
		this.offBase = offBase;
	}
	public int getOffBase() {
		return this.offBase;
	}
	
	
}
