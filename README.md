# Avaliador de Crédito

Este projeto implementa um serviço que avalia a situação financeira de um cliente e gera um relatório de avaliação de crédito.

## Funcionalidades

O projeto oferece as seguintes funcionalidades:

- Verificar a situação financeira de um cliente por CPF utilizando o método `GET /avaliacoes-credito/situacao-cliente?cpf={cpf}`
- Realizar avaliação de crédito com base na renda e CPF do cliente utilizando o método `POST /avaliacoes-credito`
- Solicitar a emissão de cartão de crédito com base na avaliação de crédito utilizando o método `POST /avaliacoes-credito/solicitacoes-cartao`

## Tecnologias utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias:

- Java
- Spring Boot
- Maven
- Banco de dados PostgreSQL

## Como executar o projeto

Para executar o projeto, siga as instruções abaixo:

1. Faça o clone do repositório para o seu computador.
2. Importe o projeto em sua IDE preferida.
3. Execute o arquivo `AvaliadorCreditoApplication.java`.
4. Acesse o endereço `http://localhost:8080` em seu navegador.

## Descrição dos métodos

### status

Método que verifica se o serviço está funcionando corretamente. O método retorna uma mensagem com o texto "ok" e status HTTP 200.

Exemplo de requisição:

```http
GET /avaliacoes-credito
