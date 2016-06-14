package polina;

import java.util.LinkedList;
/*
 * Esse objeto guarda os metadados dos arquivos manipulados
 * */
public class GerenciadorMetadados {
	private LinkedList<String> listaString;
	private LinkedList<Metadado> metadados;
	
	public GerenciadorMetadados(LinkedList<String> listaString) {
		this.listaString = listaString;
		this.metadados = new LinkedList<>();
	}

	public LinkedList<Metadado> getMetadados() {
		return metadados;
	}

	public void setMetadados(LinkedList<Metadado> metadados) {
		this.metadados = metadados;
	}

	public LinkedList<String> getListaString() {
		return listaString;
	}

	public void setListaString(LinkedList<String> listaString) {
		this.listaString = listaString;
	}
	
	public LinkedList<Metadado> extrairMetaDados() {
		while(!listaString.isEmpty()) {
			String metadado = listaString.removeFirst();
			
			String[] metadadoExtraido = metadado.split("[|]");
			
			metadados.add(new Metadado(metadadoExtraido[0], new GerenciadorCampo(metadadoExtraido).getListaCampos()));
		}
		return this.metadados;
	}
	

}
