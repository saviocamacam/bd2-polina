package polina;

import java.util.LinkedList;

public class GerenciadorDelete {
	private LinkedList<String> listaString;
	private LinkedList<Delete> listaDelete;
	private LinkedList<Metadado> listaMeta;
	private int deleteSucesso = 0;
	
	public GerenciadorDelete(LinkedList<String> lerArquivoDelete) {
		this.listaString = lerArquivoDelete;
		this.listaDelete = new LinkedList<>();
		this.listaMeta = new LinkedList<>();
	}
	
	public GerenciadorDelete() {
		this.listaDelete = new LinkedList<>();
		this.listaMeta = new LinkedList<>();
	}

	public LinkedList<Delete> extrairDeletes() {
		while(!listaString.isEmpty()) {
			String deleteString = listaString.removeFirst();
			String[] deleteExtraido = deleteString.split("[[|]]");
			String[] nomeArquivo = deleteExtraido[0].split("[[ ]]");
			Metadado novoMetadado = Arquivo.lerArquivoMet(nomeArquivo[0]);
			
			if(!listaMeta.contains(novoMetadado)) {
				listaMeta.add(novoMetadado);
			}
			
			String[] campoValue = deleteExtraido[1].split("[ ]");
			Campo campo = new Campo(campoValue[0], novoMetadado.getNomeTipo(campoValue[0]));
			campo.setOperator(campoValue[1]);
			campo.setValue(campoValue[2]);
			Delete delete = new Delete(campo);
			
			delete.setNomeArquivo(nomeArquivo[0]);
			
			listaDelete.add(delete);
		}
		return listaDelete;
	}

	public LinkedList<Metadado> getListaMeta() {
		return listaMeta;
	}

	public void setListaMeta(LinkedList<Metadado> listaMeta) {
		this.listaMeta = listaMeta;
	}

	public LinkedList<String> getListaString() {
		return listaString;
	}

	public void setListaString(LinkedList<String> listaString) {
		this.listaString = listaString;
	}

	public int getDeleteSucesso() {
		return deleteSucesso;
	}

	public void setDeleteSucesso(int deleteSucesso) {
		this.deleteSucesso = deleteSucesso;
	}

	public LinkedList<Delete> getListaDelete() {
		return listaDelete;
	}

	public void setListaDelete(LinkedList<Delete> listaDelete) {
		this.listaDelete = listaDelete;
	}
	public void addMeta(Metadado meta) {
		this.listaMeta.add(meta);
	}
	public Metadado primeiroMeta() {
		return this.listaMeta.peek();
	}
	public void addDelete(Delete removeFirst) {
		listaDelete.add(removeFirst);
	}

}
