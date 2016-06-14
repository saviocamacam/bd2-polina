package polina;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Arquivo {
	public static Writer writer;
	public Arquivo() {
		
	}
	//Tokenina e retorna uma lista de Strings com os inserts válidos lidos do arquivo de creates
	public static LinkedList<String> lerArquivoCreate(String nomeArquivo) {
		LinkedList<String> retornaCreates = new LinkedList<>();
		List<String> linhas = new ArrayList<>();
		String create = new String("CREATE");
		String table = new String("TABLE");
		String voidS = new String("");
		try {
			linhas = Files.readAllLines(Paths.get(nomeArquivo+".sql", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		int flagNomeArquivo = 0; 
		int flagVar = 0;
		String temp = new String();
		for(String s : linhas) {
			String[] piecesBreaked= s.split("[[,][ ][(][)][;]]");
			for(String piece : piecesBreaked) {
				if(piece.equals(table)) flagNomeArquivo = 1;
				if(!piece.equals(create)  && !piece.equals(table) && !piece.equals(voidS)) {
					if(piece.equals(new String("VARCHAR")) || piece.equals(new String("CHAR"))) {
						temp = temp.concat(piece+" ");
						flagVar++;
					}
					else if(piece.equals(new String("INTEGER")) || piece.equals(new String("BOOLEAN"))) {
						temp = temp.concat(piece+"|");
						flagVar=0;
					}
					else if(flagNomeArquivo == 1) {
						temp = temp.concat(piece+"|");
						flagNomeArquivo = 0;
					}
					else if(flagVar == 2) {
						temp = temp.concat(piece+"|");
						flagVar = 0;
					}
					else {
						temp = temp.concat(piece+" ");
						flagVar++;
					}
					System.out.println(piece);
				}
			}
			
			if(i+1 != linhas.size()) {
				String[] nextPiece = linhas.get(i+1).split("[[,][ ][(][)][;]]");
				if(nextPiece[0].equals(create)) {
					retornaCreates.add(temp);
					temp = new String();
				}
				i++;
			}
			else {
				retornaCreates.add(temp);
			}
		}
		return retornaCreates;
	}
	//Devolve uma lista de strings para os inserts para um Gerenciador de Inserts
	public static LinkedList<String > lerArquivoInsert(String nomeArquivo) {
		LinkedList<String> retornaInserts = new LinkedList<>();
		List<String> linhas = new ArrayList<>();
		String insert = new String("INSERT");
		String into = new String("INTO");
		String voidS = new String("");
		String values = new String("VALUES");
		
		try {
			linhas = Files.readAllLines(Paths.get(nomeArquivo+".sql", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		String temp = new String();
		for(String s : linhas) {
			String[] piecesBreaked= s.split("[[ ][(][)][;][']]");
			//String[] primeiraParte = piecesBreaked[0].split(" ");
			
			//String[] novaString = newPiecesBreaked(primeiraParte, piecesBreaked);
			
			for(String piece : piecesBreaked) {
				if(!piece.equals(insert)  && !piece.equals(into) && !piece.equals(voidS) && !piece.equals(" ")) {
					if(piece.equals(values)) {
						temp = temp.concat("|");
					}
					else temp = temp.concat(piece+" ");
				}
			}
			
			if(i+1 != linhas.size()) {
				String[] nextPiece = linhas.get(i+1).split("[[,][ ][(][)][;]]");
				if(nextPiece[0].equals(insert)) {
					retornaInserts.add(temp);
					temp = new String();
				}
				i++;
			}
			else {
				retornaInserts.add(temp);
			}
		}
		return retornaInserts;
	}
	//Devolve uma lista de strings contendo as linhas de deletes para um Gerenciador de Deletes
	public static LinkedList<String> lerArquivoDelete(String nomeArquivo) {
		LinkedList<String> retornaDeletes = new LinkedList<>();
		List<String> linhas = new ArrayList<>();
		String delete = new String("DELETE");
		String from = new String("FROM");
		String where = new String("WHERE");
		
		try {
			linhas = Files.readAllLines(Paths.get(nomeArquivo + ".sql", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		String temp = new String();
		for(String s : linhas) {
			String[] piecesBreaked = s.split("[[,][ ][(][)][;][']]");
			for(String piece : piecesBreaked) {
				if(!piece.equals(delete) && !piece.equals(from)) {
					if(piece.equals(where)) {
						temp = temp.concat("|");
					}
					else temp = temp.concat(piece+ " ");
				}
			}
			if(i+1 != linhas.size()) {
				String[] nextPiece = linhas.get(i+1).split("[[,][ ][(][)][;]]");
				if(nextPiece[0].equals(delete)) {
					retornaDeletes.add(temp);
					temp = new String();
				}
				i++;
			}
			else {
				retornaDeletes.add(temp);
			}
			
		}		
		return retornaDeletes;
	}
	
	public static Metadado lerArquivoMet(String nomeArquivo) {
		List<String> linhas = new ArrayList<>();
		int quantidadeVarchar = 0;
		int quantidadeInt = 0;
		int quantidadeChar = 0;
		int quantidadeBool = 0;
		try {
			linhas = Files.readAllLines(Paths.get(nomeArquivo+".met", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GerenciadorCampo novoGerenciador = new GerenciadorCampo();
		for(String s : linhas) {
			String[] vetS = s.split(" ");
			novoGerenciador.adicionarCampos(vetS);
			
		}
		for(Campo campo : novoGerenciador.getListaCampos()) {
			if(campo.getTipo().equals("VARCHAR"))
				quantidadeVarchar += 4;
			else if(campo.getTipo().equals("INTEGER"))
				quantidadeInt += 4;
			else if(campo.getTipo().equals("CHAR"))
				quantidadeChar += campo.getTamanho();
			else if(campo.getTipo().equals("BOOLEAN"))
				quantidadeBool += 1;
			
		}
		
		Metadado metaDado = new Metadado(nomeArquivo, novoGerenciador.getListaCampos());
		metaDado.setQuantidadeVarchar(quantidadeVarchar);
		metaDado.setQuantidadeInt(quantidadeInt);
		metaDado.setQuantidadeChar(quantidadeChar);
		metaDado.setQuantidadeBool(quantidadeBool);
		metaDado.setQuantidadeTotal(quantidadeInt + quantidadeChar + quantidadeVarchar + quantidadeBool);
		return metaDado;
	}
	//Gera o arquivo de metadado de uma dada tabela
	public static void escreverArquivoMet(Metadado meta) {
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(meta.getNomeArquivo()+".met"), "utf-8"));
			int i = 0;
			while(i != meta.getCampos().size()) {
				try {
					writer.write(meta.getCampos().get(i).toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				i++;
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	//Dado um conjunto de metadados retirados de um create, gera diversos metadados
	public static void escreverGerenciador(GerenciadorMetadados novoGerenciador) {
		for(Metadado meta : novoGerenciador.extrairMetaDados()) {
			escreverArquivoMet(meta);
			inicializarArquivoBin(meta);
		}
	}
	//Inicializa os arquivos binários dos creates realizados
	private static void inicializarArquivoBin(Metadado meta) {
		ArquivoBinario arquivoBinario = new ArquivoBinario(meta.getNomeArquivo(), 2000);
		OutputStream output = null;
		try {
			output = new BufferedOutputStream(new FileOutputStream(meta.getNomeArquivo()));
			output.write(arquivoBinario.getContent());
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void escreverAquivoBin(byte[] content, String nomeArquivo) {
		try	{
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(nomeArquivo));
				output.write(content);
			}
			finally {
				output.close();
			}
		}
	    catch(FileNotFoundException ex){
	    	System.out.println("File not found.");
	    }
	    catch(IOException ex){
	    	System.out.println(ex);
	    }
	}
	
	
	public static byte[] lerArquivoBin(String nomeArquivo) {
	    File file = new File(nomeArquivo);
	    byte[] result = null;
	    try {
	      InputStream input =  new BufferedInputStream(new FileInputStream(file));
	      result = readAndClose(input);
	    }
	    catch (FileNotFoundException ex){
	      System.out.println(ex);
	    }
	    return result;
	}
	
	private static byte[] readAndClose(InputStream aInput){
		byte[] bucket = new byte[32*1024]; 
		ByteArrayOutputStream result = null; 
		try {
			try {
				result = new ByteArrayOutputStream(bucket.length);
		        int bytesRead = 0;
		        while(bytesRead != -1){
		        	bytesRead = aInput.read(bucket);
		        	if(bytesRead > 0){
		        		result.write(bucket, 0, bytesRead);
		        	}
		        }
			}
			finally {
				aInput.close();
				//result.close(); this is a no-operation for ByteArrayOutputStream
			}
		}
		catch (IOException ex){
			System.out.println(ex);
		}
		return result.toByteArray();
	}

	public static boolean escreverInserts(GerenciadorInsert gerenciadorInsert) {
		LinkedList<GerenciadorInsert> conjuntoGerenciadores = new LinkedList<>();
		LinkedList<Insert> insertBruto = new LinkedList<>();
		
		for(Insert insert : gerenciadorInsert.extrairInsert()) {
			insertBruto.add(insert);
		}
		
		LinkedList<Metadado> conjuntoMeta = gerenciadorInsert.getListaMeta();
		while(!conjuntoMeta.isEmpty()) {
			GerenciadorInsert gerenciador = new GerenciadorInsert();
			gerenciador.addMeta(conjuntoMeta.removeFirst());
			conjuntoGerenciadores.add(gerenciador);
		}
		
		int i = 0;
		while(!insertBruto.isEmpty()) {
			if(conjuntoGerenciadores.get(i).primeiroMeta().getNomeArquivo().equals(insertBruto.peek().getNomeArquivo())) {
				conjuntoGerenciadores.get(i).addInsert(insertBruto.removeFirst());
			} else
				i++;
		}
		
		while(!conjuntoGerenciadores.isEmpty()) {
			LinkedList<Offset> listaOffset = new LinkedList<>();
			GerenciadorInsert gerenciador = conjuntoGerenciadores.removeFirst();
			
			byte[] arquivoBinario = Arquivo.lerArquivoBin(gerenciador.getListaMeta().peek().getNomeArquivo());
			CabecalhoArquivo cabecalho = new CabecalhoArquivo(arquivoBinario);
			int iterator = 0;
			
			byte[] conteudoAnterior = getRegistroSerializado(cabecalho.getPrimeiroLivre(), arquivoBinario, 2000);
			
			LinkedList<ByteArrayOutputStream> listaTemp = new LinkedList<>();
			ByteArrayOutputStream bufferInsert = new ByteArrayOutputStream();
			ByteArrayOutputStream bufferArquivo = new ByteArrayOutputStream();
			
			while(!gerenciador.getListaInsert().isEmpty()) {
				int tamOff = cabecalho.getPrimeiroLivre() - gerenciador.getListaInsert().peek().getListaContentsSerialized().length;
				Offset offset = new Offset((short) tamOff);
				//offset.setOffisetSerialized(Serializer.toShortByteArray(ca));
				listaOffset.add(offset);
				cabecalho.setPrimeiroLivre((short)(tamOff));
				try {
					ByteArrayOutputStream bufferTemp = new ByteArrayOutputStream();
					bufferTemp.write(gerenciador.getListaInsert().removeFirst().getListaContentsSerialized());
					listaTemp.add(bufferTemp);
					
					iterator++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(i=listaTemp.size()-1 ; i>=0 ; i-- ) {
				try {
					bufferInsert.write(listaTemp.get(i).toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			iterator += cabecalho.getRegistros();
			cabecalho.setRegistros((short)iterator);
			cabecalho.setDeslocamentoArquivos(listaOffset);
			try {
				bufferArquivo.write(cabecalho.getDadosSerializados());
				int tam = 2000 - (cabecalho.getDadosSerializados().length + bufferInsert.toByteArray().length + conteudoAnterior.length);
				if(tam < 0) {
					return false;
				}else {
					byte[] livres = new byte[2000 - (cabecalho.getDadosSerializados().length + bufferInsert.toByteArray().length + conteudoAnterior.length)];
					bufferArquivo.write(livres);
					bufferArquivo.write(bufferInsert.toByteArray());
					bufferArquivo.write(conteudoAnterior);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Arquivo.escreverAquivoBin(bufferArquivo.toByteArray(), gerenciador.getListaMeta().peek().getNomeArquivo());
		}
		return true;
	}
	
	private static byte[] getRegistroSerializado(int aPartir, byte[] arquivoBinario, int tamanho) {
		byte[] registros = new byte[tamanho-aPartir];
		int i=aPartir;
		int j=0;
		for(j=0 ; j < tamanho-aPartir; j++) {
			registros[j] = arquivoBinario[i];
			i++;
		}
		return registros;
	}
	
	public static boolean executarDeletes(GerenciadorDelete gerenciadorDelete) {
		LinkedList<GerenciadorDelete> conjuntoGerenciadoresDeletes = new LinkedList<>();
		LinkedList<Delete> deleteBruto = new LinkedList<>();
		
		for(Delete delete: gerenciadorDelete.extrairDeletes()) {
			deleteBruto.add(delete);
		}
		
		LinkedList<Metadado> conjuntoMeta = gerenciadorDelete.getListaMeta();
		
		while(!conjuntoMeta.isEmpty()) {
			GerenciadorDelete gerenciador = new GerenciadorDelete();
			gerenciador.addMeta(conjuntoMeta.removeFirst());
			conjuntoGerenciadoresDeletes.add(gerenciador);
		}
		
		int i = 0;
		
		while(!deleteBruto.isEmpty()) {
			if(conjuntoGerenciadoresDeletes.get(i).primeiroMeta().getNomeArquivo().equals(deleteBruto.peek().getNomeArquivo())) {
				conjuntoGerenciadoresDeletes.get(i).addDelete(deleteBruto.removeFirst());
			} else
				i++;
		}
		while(!conjuntoGerenciadoresDeletes.isEmpty()) {
			short deletesFromLocalManager = 0;
			GerenciadorDelete gerenciador = conjuntoGerenciadoresDeletes.removeFirst();
			Metadado metadado = gerenciador.primeiroMeta();
			byte[] arquivoBinario = Arquivo.lerArquivoBin(gerenciador.getListaMeta().peek().getNomeArquivo());
			ArquivoBinario arquivoRecuperado = new ArquivoBinario(metadado.getNomeArquivo());
			arquivoRecuperado.setContent(arquivoBinario);
			
			CabecalhoArquivo cabecalho = new CabecalhoArquivo(arquivoBinario);
			//arquivoRecuperado.setCabecalho(cabecalho);
			
			GerenciadorInsert gerenciadorInsert = new GerenciadorInsert();
			gerenciadorInsert.addMeta(metadado);
			
			LinkedList<Insert> insertRecuperado = gerenciadorInsert.recuperaInsert(arquivoRecuperado);
			
			for(Delete delete : gerenciador.getListaDelete()) {
				i = 0;
				while(i < insertRecuperado.size()) {
					Insert insert = insertRecuperado.get(i);
					
					if(insert.getListaContents().indexOf(new Campo(delete.getCampo().getNomeCampo())) < 0) {
						i++;
					}
					else {
						Campo compare = insert.getCampoNome(delete.getCampo().getNomeCampo());
						
						String tipo = metadado.getNomeTipo(delete.getCampo().getNomeCampo());
						
						//Exclusão por igualdade
						if(delete.getCampo().getOperator().equals("=")) {
							if(tipo.equals("VARCHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) == 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("INTEGER")) {
								if(compare.getIntegerValue() == delete.getCampo().getIntegerValue()) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("CHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) == 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("BOOLEAN")) {
								if(compare.getBooleanValue() == delete.getCampo().getBooleanValue()) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
						}
						//Exclusão por diferença
						else if (delete.getCampo().getOperator().equals("!=")) {
							if(tipo.equals("VARCHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) != 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("INTEGER")) {
								if(compare.getIntegerValue() != delete.getCampo().getIntegerValue()) {
									insertRecuperado.removeFirst();
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("CHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) != 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("BOOLEAN")) {
								if(compare.getBooleanValue() != delete.getCampo().getBooleanValue()) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
						}
						//Exclusão por maior
						else if (delete.getCampo().getOperator().equals(">")) {
							if(tipo.equals("VARCHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) > 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("INTEGER")) {
								if(compare.getIntegerValue() > delete.getCampo().getIntegerValue()) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("CHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) > 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							
						}
						//Exclusão por maior
						else if (delete.getCampo().getOperator().equals("<")) {
							if(tipo.equals("VARCHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) < 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("INTEGER")) {
								if(compare.getIntegerValue() < delete.getCampo().getIntegerValue()) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
							else if(tipo.equals("CHAR")) {
								if(compare.getStringValue().compareToIgnoreCase(delete.getCampo().getStringValue()) < 0) {
									insertRecuperado.remove(i);
									i--;
									deletesFromLocalManager++;
								}
							}
						}
						i++;
					}
				}
			}
			short registrosAnteriores = cabecalho.getRegistros();
			cabecalho.setExcluidos(deletesFromLocalManager);
			cabecalho.resetDeslocamentos();
			cabecalho.serializarDadosFixos();
			cabecalho.setRegistros((short) (registrosAnteriores-deletesFromLocalManager));
			int deletes = gerenciadorDelete.getDeleteSucesso();
			deletes += deletesFromLocalManager;
				gerenciadorDelete.setDeleteSucesso(deletes);
				LinkedList<ByteArrayOutputStream> listaTemp = new LinkedList<>();
				ByteArrayOutputStream bufferInsert = new ByteArrayOutputStream();
				ByteArrayOutputStream bufferArquivo = new ByteArrayOutputStream();
				LinkedList<Offset> listaOffset = new LinkedList<>();
				
				while(!insertRecuperado.isEmpty()) {
					int tamOff = cabecalho.getPrimeiroLivre() - insertRecuperado.peek().getListaContentsSerialized().length;
					Offset offset = new Offset((short) tamOff);
					listaOffset.add(offset);
					cabecalho.setPrimeiroLivre((short)(tamOff));
					try {
						ByteArrayOutputStream bufferTemp = new ByteArrayOutputStream();
						bufferTemp.write(insertRecuperado.removeFirst().getListaContentsSerialized());
						listaTemp.add(bufferTemp);
					
						//iterator++;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
				for(i=listaTemp.size()-1 ; i>=0 ; i-- ) {
					try {
						bufferInsert.write(listaTemp.get(i).toByteArray());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
				cabecalho.setDeslocamentoArquivos(listaOffset);
				try {
					bufferArquivo.write(cabecalho.getDadosSerializados());
					int tam = 2000 - (cabecalho.getDadosSerializados().length + bufferInsert.toByteArray().length);
					byte[] livres = new byte[tam];
					bufferArquivo.write(livres);
					bufferArquivo.write(bufferInsert.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Arquivo.escreverAquivoBin(bufferArquivo.toByteArray(), gerenciador.getListaMeta().peek().getNomeArquivo());
			}
		if(gerenciadorDelete.getDeleteSucesso() <= 0) {
			return false;
		}
		else
			return true;
	}
		  
		 
}