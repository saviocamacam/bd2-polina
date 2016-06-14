/*
 * Cada arquivo de deletes gera um Gerenciador de Deletes que contém a lista de exclusões a serem feitas, assim como os 
 * metadados dos arquivos envolvidos em cada exclusão;
 * */
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
	/*Esse método transforma cada linha ou senteça de delete num objeto de Delete manipulável nesse programa
	 * Os campos valores importantes são o nome da tabela, o campo de comparação e o operador;
	*/
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
			String temp = "";
			if(campoValue.length > 3) {
				int i = 3;
				while(i < campoValue.length-1) {
					temp = temp.concat(campoValue[i] + " ");
					i++;
				}
				temp = temp.concat(campoValue[i]);
			}
			else
				temp = temp.concat(campoValue[2]);
			campo.setValue(temp);
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
