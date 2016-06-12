while(i < campoValues.length && campoValues.length == campoMeta.length) {
				campo = new Campo(campoMeta[i]);
				campo.setValue(campoValues[i]);
				
				int proximoIndice = 0;
				String proximoTipo = "";
				if(i+1 < campoValues.length) {
					Campo proximoCampo = new Campo(campoMeta[i+1]);
					proximoCampo.setValue(campoValues[i+1]);
					
					proximoIndice = novoMetadado.getCampos().indexOf(proximoCampo);
					proximoTipo = novoMetadado.getCampos().get(proximoIndice).getTipo(campoMeta[i+1]);
				}
				
				indice = novoMetadado.getCampos().indexOf(campo);
				String tipo = novoMetadado.getCampos().get(indice).getTipo(campoMeta[i]);
				
				int flagNullPositionVar = 0;
				int flagNullPositionInt = 0;
				int flagNullPositionChar = 0;
				int flagNullPositionBool = 0;
				
				if(proximoIndice - indice == 2 && proximoTipo.equals("VARCHAR")) {
					flagNullPositionVar = 1;
				}
				else if(proximoIndice - indice == 2 && proximoTipo.equals("INTEGER")) {
					flagNullPositionInt = 1;
				}
				else if(proximoIndice - indice == 2 && proximoTipo.equals("CHAR")) {
					flagNullPositionChar = 1;
				}
				else if(proximoIndice - indice == 2 && proximoTipo.equals("BOOLEAN")) {
					flagNullPositionBool = 1;
				}
				
				try {
					if(tipo.equals("VARCHAR")) {
						campo.setTamanho(campo.getStringValue().length());
						bufferCampoVar.write(Serializer.toByteArrayString(campo.getStringValue()));
						deslocamentos.add(campo.getStringValue().length());
						
						if(flagNullPositionVar == 1) {
							deslocamentos.add(0);
							flagNullPositionVar = 0;
						}
					}
					else if(tipo.equals("INTEGER")){
						bufferCampoFixo.write(Serializer.toByteArrayInt(campo.getIntegerValue()));
						primeiroVary += 4;
						
						if(flagNullPositionInt == 1) {
							bufferCampoFixo.write(Serializer.toByteArrayInt(0));
							primeiroVary += 4;
						}
						if(flagNullPositionChar == 1) {
							int iterator = 0;
							while(iterator < novoMetadado.getQuantidadeBool()) {
								bufferCampoFixo.write(Serializer.toByteArrayChar("0"));
								iterator++;
							}
							primeiroVary += campo.getCharValue().length();
						}
						if(flagNullPositionBool == 1) {
							
						}
					}
					else if(tipo.equals("BOOLEAN")){
						bufferCampoFixo.write(Serializer.toByteArrayBool(campo.getBooleanValue()));
						primeiroVary += 1;
						
						if(flagNullPositionVar == 1) {
							bufferCampoFixo.write(Serializer.toByteArrayBool(campo.getBooleanValue()));
							primeiroVary += 1;
						}
					} 
					else if(tipo.equals("CHAR")){
						campo.setTamanho(campo.getCharValue().length());
						bufferCampoFixo.write(Serializer.toByteArrayChar(campo.getCharValue()));
						primeiroVary += campo.getCharValue().length();
						
						if(flagNullPositionVar == 1) {
							bufferCampoFixo.write(Serializer.toByteArrayChar(campo.getCharValue()));
							primeiroVary += campo.getCharValue().length();
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				camposInsert.add(campo);
				i++;
			}