CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,

    data_ultima_alteracao TIMESTAMP WITH TIME ZONE NOT NULL,

    address_logradouro VARCHAR(255),
    address_numero VARCHAR(50),
    address_complemento VARCHAR(255),
    address_bairro VARCHAR(255),
    address_cidade VARCHAR(255),
    address_uf VARCHAR(10),
    address_cep VARCHAR(20)
);