-- =============================================================
-- Script do banco de dados - Sistema de Agendamento da Clinica (V2)
-- V2: colunas exames e valor_total na consulta (padrao Decorator)
-- =============================================================
SET NAMES utf8mb4;

DROP DATABASE IF EXISTS clinica_db;
CREATE DATABASE clinica_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clinica_db;

CREATE TABLE paciente (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  nome     VARCHAR(100) NOT NULL,
  cpf      VARCHAR(14)  NOT NULL,
  telefone VARCHAR(20),
  email    VARCHAR(100)
);

CREATE TABLE medico (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  nome          VARCHAR(100) NOT NULL,
  crm           VARCHAR(20)  NOT NULL,
  especialidade VARCHAR(60)  NOT NULL,
  telefone      VARCHAR(20)
);

-- Relacionamento 1:N -> um medico tem varias consultas / um paciente tem varias consultas
CREATE TABLE consulta (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  paciente_id      INT NOT NULL,
  medico_id        INT NOT NULL,
  data             DATE NOT NULL,
  hora             TIME NOT NULL,
  especialidade    VARCHAR(60),
  tipo_atendimento VARCHAR(20),
  status           VARCHAR(20),
  valor            DECIMAL(10,2),
  forma_pagamento  VARCHAR(20),
  observacoes      VARCHAR(500),
  data_criacao     DATETIME DEFAULT CURRENT_TIMESTAMP,
  exames           VARCHAR(200),
  valor_total      DECIMAL(10,2),
  FOREIGN KEY (paciente_id) REFERENCES paciente(id),
  FOREIGN KEY (medico_id)   REFERENCES medico(id)
);

-- Relacionamento 1:1 -> cada consulta tem no maximo UM registro (consulta_id UNIQUE)
CREATE TABLE registro_atendimento (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  consulta_id   INT NOT NULL UNIQUE,
  diagnostico   VARCHAR(500) NOT NULL,
  prescricao    VARCHAR(500),
  data_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (consulta_id) REFERENCES consulta(id) ON DELETE CASCADE
);

-- ------------------ Dados de exemplo ------------------
INSERT INTO paciente (nome, cpf, telefone, email) VALUES
('João Pedro Almeida', '444.444.444-44', '(11) 98888-4444', 'joao@email.com'),
('Maria Clara Santos', '555.555.555-55', '(11) 98888-5555', 'maria@email.com'),
('Lucas Oliveira',     '666.666.666-66', '(11) 98888-6666', 'lucas@email.com');

INSERT INTO medico (nome, crm, especialidade, telefone) VALUES
('Ana Beatriz Souza',   'CRM-SP 123456', 'Cardiologia',  '(11) 90000-1111'),
('Carlos Eduardo Lima', 'CRM-SP 234567', 'Dermatologia', '(11) 90000-2222'),
('Fernanda Rocha',      'CRM-SP 345678', 'Pediatria',    '(11) 90000-3333');

-- Consultas no proximo dia util (para a demonstracao funcionar em qualquer dia)
SET @dia = CURDATE() + INTERVAL (CASE DAYOFWEEK(CURDATE()) WHEN 6 THEN 3 WHEN 7 THEN 2 ELSE 1 END) DAY;

INSERT INTO consulta (paciente_id, medico_id, data, hora, especialidade, tipo_atendimento, status, valor, forma_pagamento, observacoes, exames, valor_total) VALUES
(1, 1, @dia, '08:00', 'Cardiologia',  'Presencial',   'Agendada',   350.00, 'Convênio', 'Primeira consulta',    'Eletrocardiograma,Hemograma', 515.00),
(2, 1, @dia, '08:30', 'Cardiologia',  'Presencial',   'Confirmada', 350.00, 'PIX',      '',                     '',                            350.00),
(3, 3, @dia, '10:00', 'Pediatria',    'Presencial',   'Agendada',   280.00, 'Convênio', 'Acompanhado pela mãe', 'Hemograma',                   325.00),
(2, 2, @dia, '14:00', 'Dermatologia', 'Telemedicina', 'Realizada',  300.00, 'Cartão',   '',                     '',                            300.00);

INSERT INTO registro_atendimento (consulta_id, diagnostico, prescricao) VALUES
(4, 'Dermatite de contato', 'Pomada de hidrocortisona 2x ao dia por 7 dias');
