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
			String[] piecesBreaked= s.split("[[,][ ][(][)][;]]");
			//String nomeTable = new String(piecesBreaked[2]);
			for(String piece : piecesBreaked) {
				if(!piece.equals(insert)  && !piece.equals(into) && !piece.equals(voidS)) {
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

	public static void escreverGerenciador(GerenciadorMetadados novoGerenciador) {
		for(Metadado meta : novoGerenciador.extrairMetaDados()) {
			escreverArquivoMet(meta);
			inicializarArquivoBin(meta);
		}
	}
	
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

	public static void escreverInserts(GerenciadorInsert gerenciadorInsert) {
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
		/*while(i < conjuntoGerenciadores.size()) {
			if(insertBruto.getFirst().getNomeArquivo().equals(conjuntoGerenciadores.get(i))) {
				
			}
			else
				i++;
		}*/
		
		
		while(!insertBruto.isEmpty()) {
			if(conjuntoGerenciadores.get(i).primeiroMeta().getNomeArquivo().equals(insertBruto.peek().getNomeArquivo())) {
				conjuntoGerenciadores.get(i).addInsert(insertBruto.removeFirst());
			} else
				i++;
		}
		
		while(!conjuntoGerenciadores.isEmpty()) {
			LinkedList<Offiset> listaOffset = new LinkedList<>();
			GerenciadorInsert gerenciador = conjuntoGerenciadores.removeFirst();
			
			byte[] arquivoBinario = Arquivo.lerArquivoBin(gerenciador.getListaMeta().peek().getNomeArquivo());
			
			CabecalhoArquivo cabecalho = new CabecalhoArquivo(arquivoBinario);
			int iterator = 0;
			
			LinkedList<ByteArrayOutputStream> listaTemp = new LinkedList<>();
			ByteArrayOutputStream bufferInsert = new ByteArrayOutputStream();
			ByteArrayOutputStream bufferArquivo = new ByteArrayOutputStream();
			
			while(!gerenciador.getListaInsert().isEmpty()) {
				int tamOff = cabecalho.getPrimeiroLivre() - gerenciador.getListaInsert().peek().getListaContentsSerialized().length;
				Offiset offset = new Offiset((short) tamOff);
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
				byte[] livres = new byte[2000 - (cabecalho.getDadosSerializados().length + bufferInsert.toByteArray().length)];
				bufferArquivo.write(livres);
				bufferArquivo.write(bufferInsert.toByteArray());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Arquivo.escreverAquivoBin(bufferArquivo.toByteArray(), gerenciador.getListaMeta().peek().getNomeArquivo());
		}
	}
		  
		 
}