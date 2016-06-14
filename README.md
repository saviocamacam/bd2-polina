===========================================================
BANCO DE DADOS 2 | APS: IMPLEMENTAÇÃO DE ARMAZENAMENTO EM DISCO
===========================================================
HENRIQUE SOUZA | SAVIO CAMACAM

Descrição

O problema consiste na simulação de Sistema de Gerenciamento de Banco de Dados, recolhendo sentenças de manipulação e tratando arquivos binários.
O programa recebe do usuário os parâmetros:
* Arquivo contendo sentenças SQL CREATE no padrão;
```
CREATE TABLE tableName(field1, field2, fieldn);
```
* Arquivo conténdo sentenças SQL INSERT no padrão;
```
INSERT INTO tableName(field1, field2, fieldn) VALUES (value1, value2, valuen);
```
* Arquivo conténdo sentenças SQL DELETE no padrão;
```
DELETE FROM tableName WHERE fieldn <operator> valuen; (operador: = != > <)
```

Executando o programa no Windows usando o PowerShell ou no terminal do Linux, siga até o diretório onde se encontra o arquivo armazenamento_bd2.jar e execute o comando:
```
java -jar armazenamento_bd2.jar
```
Visualizando o conteúdo binário de cada arquivo, siga até o diretório onde se encontra o arquivo em questão e execute o comando:
```
xxd -b nomearquivo
```
Ou, para visualizar o cabecalho
```
xxd -b nomearquivo | head
```

Como opções de menu:
* (1) Criar arquivo: é informado o nome de um arquivo contendo senteças SQL CREATE. O arquivo deve estar no diretório do executável. Não é necessário informar a extensão;
* (2) Inserir registro: é informado o nome de um arquivo contendo senteças SQL CREATE. O arquivo deve estar no diretório do executável. Não é necessário informar a extensão;
* (3) Listar registros: informado o nome do arquivo que deseja ser listado (fonte).
* (4) Excluir registros: é informado o nome de um arquivo contendo senteças SQL DELETE. O arquivo deve estar no diretório do executável. Não é necessário informar a extensão;
* (5) Sair: programa é encerrado.

A qualquer tempo pode ser executada qualquer função do programa, desde que acessando arquivos que existam no diretório de execução do programa.
Apenas números inteiros devem ser inseridos para garantir sua execução.
Ao executar funções que geram novas saidas, ao criar o arquivo, se ele existir será sobrescrito.
Nos casos de inserção que mais inserções forem colocadas que o suportado, um aviso será informado na saída padrão informando que o arquivo está cheio.
Ao ser enviado um comando para exclusão, caso nenhuma exclusão for feita, o programa informará "Erro ao deletar", 
do contrário, informará na saída padrão quantos deletes foram executados.
Na listagem na saída padrão, o programa também informa quantos registros foram recuperados.
Repare que foi implementada a desfragmentação, assim outros inserts podem ser feitos no mesmo arquivo depois de feitas exclusões.
