CREATE TABLE curso(
    id INTEGER,
    nome VARCHAR(20),
    instituicao VARCHAR(20), 
    cidade VARCHAR(20)
    fundacao INTEGER,
    estadoCurso BOOLEAN,
    duracao INTEGER,
    validacao BOOLEAN,
    coordenador CHAR(20)); 
CREATE TABLE materia(id INTEGER, nome CHAR(25), professor VARCHAR(25));
CREATE TABLE tarefa(id INTEGER, tipo VARCHAR(10), dataFinal VARCHAR(10));