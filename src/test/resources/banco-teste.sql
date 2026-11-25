-- Banco usado nos testes do DAO (H2 em memoria, modo MySQL)
DROP TABLE IF EXISTS registro_atendimento;
DROP TABLE IF EXISTS consulta;
DROP TABLE IF EXISTS paciente;
DROP TABLE IF EXISTS medico;

CREATE TABLE paciente (
  id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(100) NOT NULL, cpf VARCHAR(14) NOT NULL,
  telefone VARCHAR(20), email VARCHAR(100)
);

CREATE TABLE medico (
  id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(100) NOT NULL, crm VARCHAR(20) NOT NULL,
  especialidade VARCHAR(60) NOT NULL, telefone VARCHAR(20)
);

CREATE TABLE consulta (
  id INT AUTO_INCREMENT PRIMARY KEY, paciente_id INT NOT NULL, medico_id INT NOT NULL,
  data DATE NOT NULL, hora TIME NOT NULL, especialidade VARCHAR(60), tipo_atendimento VARCHAR(20),
  status VARCHAR(20), valor DECIMAL(10,2), forma_pagamento VARCHAR(20), observacoes VARCHAR(500),
  data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, exames VARCHAR(200), valor_total DECIMAL(10,2),
  FOREIGN KEY (paciente_id) REFERENCES paciente(id), FOREIGN KEY (medico_id) REFERENCES medico(id)
);

CREATE TABLE registro_atendimento (
  id INT AUTO_INCREMENT PRIMARY KEY, consulta_id INT NOT NULL UNIQUE, diagnostico VARCHAR(500) NOT NULL,
  prescricao VARCHAR(500), data_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (consulta_id) REFERENCES consulta(id) ON DELETE CASCADE
);

INSERT INTO paciente (nome, cpf) VALUES ('Paciente Teste', '111.111.111-11');
INSERT INTO medico (nome, crm, especialidade) VALUES ('Medico Um', 'CRM 1', 'Cardiologia');
INSERT INTO medico (nome, crm, especialidade) VALUES ('Medico Dois', 'CRM 2', 'Pediatria');
