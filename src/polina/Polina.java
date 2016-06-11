package polina;

import java.util.LinkedList;
import java.util.Scanner;

public class Polina {
	public static int op = -1;
	public static String nomeArquivo;
	public static Scanner scanner;
	public static GerenciadorMetadados gerenciadorMeta;
	public static GerenciadorInsert gerenciadorInsert;
	
	public static void main(String[] args) {
		do{
			System.out.println("(1) Criar arquivo\n(2) Inserir arquivo\n(3) Listar Registros\n(4) Excluir registros\n(5) Sair");
			scanner = new Scanner(System.in);
			op = scanner.nextInt();
			if(op == 1) {
				System.out.println("- Informe o nome do arquivo .sql para criacao: ");
				//nomeArquivo = scanner.next();
				gerenciadorMeta = new GerenciadorMetadados(Arquivo.lerArquivoCreate("create"));
				Arquivo.escreverGerenciador(gerenciadorMeta);
				/*byte[] testeb = null;
				Teste teste = new Teste(10, 20, 30, 40);
				try {
					testeb = Serializer.serialize(20);
					testeb = Serializer.toByteArray(20);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Arquivo.escreverAquivoBin(testeb, "testeB");*/
				
			} else if (op == 2) {
				System.out.println("- Informe o nome do arquivo .sql para insercao: ");
				//nomeArquivo = scanner.next();
				gerenciadorInsert = new GerenciadorInsert(Arquivo.lerArquivoInsert("insert"));
				Arquivo.escreverInserts(gerenciadorInsert);
				System.out.println(gerenciadorInsert.getInsertSucesso());
				
			} else if (op == 3) {
				System.out.println("- Informe o nome do arquivo .sql para listagem: ");
				//nomeArquivo = scanner.next();
				ArquivoBinario arquivoRecuperado = new ArquivoBinario("materia");
				arquivoRecuperado.setContent(Arquivo.lerArquivoBin("materia"));
				Metadado metadadoRecuperado = Arquivo.lerArquivoMet(arquivoRecuperado.getNomeArquivo());
				GerenciadorInsert gerenciador = new GerenciadorInsert();
				gerenciador.addMeta(metadadoRecuperado);
				LinkedList<Insert> insertRecuperado = gerenciador.recuperaInsert(arquivoRecuperado);
				while(!insertRecuperado.isEmpty()) {
					System.out.println(insertRecuperado.removeFirst().toString());
				}
			} else if (op == 4) {
				
			}
		} while (op != 5);
	}
}