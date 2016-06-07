CREATE TABLE curso(
    id INTEGER, 
    nome VARCHAR(20), 
    instituicao VARCHAR(20), 
    telefone VARCHAR(11));
CREATE TABLE materia(id INTEGER, nome VARCHAR(25), professor VARCHAR(25));
CREATE TABLE tarefa(id INTEGER, tipo VARCHAR(10), dataFinal VARCHAR(10));