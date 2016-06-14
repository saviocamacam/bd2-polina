package polina;

import java.util.LinkedList;
import java.util.Scanner;

public class Polina {
	public static int op = -1;
	public static String nomeArquivo;
	public static Scanner scanner;
	public static GerenciadorMetadados gerenciadorMeta;
	public static GerenciadorInsert gerenciadorInsert;
	public static GerenciadorDelete gerenciadorDelete;
	
	public static void main(String[] args) {
		do{
			System.out.println("(1) Criar arquivo\n(2) Inserir arquivo\n(3) Listar Registros\n(4) Excluir registros\n(5) Sair");
			scanner = new Scanner(System.in);
			op = scanner.nextInt();
			if(op == 1) {
				System.out.println("- Informe o nome do arquivo .sql para criacao: ");
				nomeArquivo = scanner.next();
				gerenciadorMeta = new GerenciadorMetadados(Arquivo.lerArquivoCreate(nomeArquivo));
				Arquivo.escreverGerenciador(gerenciadorMeta);
				
			} else if (op == 2) {
				System.out.println("- Informe o nome do arquivo .sql para insercao: ");
				nomeArquivo = scanner.next();
				gerenciadorInsert = new GerenciadorInsert(Arquivo.lerArquivoInsert(nomeArquivo));
				if(Arquivo.escreverInserts(gerenciadorInsert)) {
					System.out.println("Insercoes com sucesso: " +gerenciadorInsert.getInsertSucesso());
				}
				else {
					System.out.println("Arquivo cheio!");
				}
			} else if (op == 3) {
				System.out.println("- Informe o nome do arquivo .sql para listagem: ");
				nomeArquivo = scanner.next();
				ArquivoBinario arquivoRecuperado = new ArquivoBinario(nomeArquivo);
				arquivoRecuperado.setContent(Arquivo.lerArquivoBin(nomeArquivo));
				Metadado metadadoRecuperado = Arquivo.lerArquivoMet(arquivoRecuperado.getNomeArquivo());
				GerenciadorInsert gerenciador = new GerenciadorInsert();
				gerenciador.addMeta(metadadoRecuperado);
				LinkedList<Insert> insertRecuperado = gerenciador.recuperaInsert(arquivoRecuperado);
				System.out.println(arquivoRecuperado.getRegistros());
				while(!insertRecuperado.isEmpty()) {
					System.out.println(insertRecuperado.removeFirst().toString());
				}
			} else if (op == 4) {
				System.out.println("- Informe o nome do arquivo .sql para deletar: ");
				nomeArquivo = scanner.next();
				gerenciadorDelete = new GerenciadorDelete(Arquivo.lerArquivoDelete(nomeArquivo));
				if(Arquivo.executarDeletes(gerenciadorDelete)) {
					System.out.println("Deletes com sucesso: " + gerenciadorDelete.getDeleteSucesso());
				}
				else {
					System.out.println("Erro eu deletar");
				}
				gerenciadorDelete.extrairDeletes();
				
				
				System.out.println("aqui");
			}
		} while (op != 5);
	}
}