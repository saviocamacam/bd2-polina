package polina;

import java.util.LinkedList;

public class GerenciadorCampo {
	private LinkedList<Campo> listaCampos;
	private String[] camposRecebidos;
	
	public GerenciadorCampo() {
		this.listaCampos = new LinkedList<>();
	}
	
	public GerenciadorCampo(String[] camposRecebidos) {
		this.camposRecebidos = camposRecebidos;
		this.listaCampos = new LinkedList<>();
		createCampos();
	}
	
	public GerenciadorCampo(String[] camposRecebidos, String[] values) {
		this.camposRecebidos = camposRecebidos;
		this.listaCampos = new LinkedList<>();
		createCampos(values);
	}
	
	private void createCampos(String[] values) {
		
	}

	public void createCampos() {
		Campo campo = null;
		int offBaseVar = 0;
		for(String s : camposRecebidos) {
			String[] temp = s.split(" ");
			if (temp.length == 3) {
				if(temp[1].equals("VARCHAR")) {
					offBaseVar += 4;
				}
			}
		}
		int offBaseFixo = offBaseVar;
		for(String s : camposRecebidos) {
			String[] temp = s.split(" ");
			if(temp.length == 2) {
				campo = new Campo(temp[0], temp[1]);
				if(temp[1].equals("INTEGER")) {
					offBaseFixo += 4;
				}
				else if(temp[1].equals("BOOLEAN")) {
					offBaseFixo += 1;
				}
				campo.setOffBase(offBaseFixo);
			}
			else if (temp.length == 3) {
				campo = new Campo(temp[0], temp[1], Integer.valueOf(temp[2]));
				if(temp[1].equals("CHAR")) {
					offBaseFixo += Integer.valueOf(temp[2]);
				}
				campo.setOffBase(offBaseFixo);
			}
			if(campo != null) {
				listaCampos.add(campo);
			}
		}
		
		
		
	}
	
	public void adicionarCampos(String[] linhaRecebida) {
		Campo campo = null;
		if(linhaRecebida.length == 3) {
			campo = new Campo(linhaRecebida[0], linhaRecebida[1]);
			campo.setOffBase(Integer.valueOf(linhaRecebida[2]));
		}
		else if (linhaRecebida.length == 4) {
			campo = new Campo(linhaRecebida[0], linhaRecebida[1], Integer.valueOf(linhaRecebida[2]));
			campo.setOffBase(Integer.valueOf(linhaRecebida[3]));
		}
		if(campo != null) {
			listaCampos.add(campo);
		}
	}

	public String[] getCamposRecebidos() {
		return camposRecebidos;
	}

	public void setCamposRecebidos(String[] camposRecebidos) {
		this.camposRecebidos = camposRecebidos;
	}

	public LinkedList<Campo> getListaCampos() {
		return listaCampos;
	}

	public void setListaCampos(LinkedList<Campo> listaCampos) {
		this.listaCampos = listaCampos;
	}

}
