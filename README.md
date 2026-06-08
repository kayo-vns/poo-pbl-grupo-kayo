# ONG de Adocao de Pets

Projeto pratico integrador de Orientacao a Objetos com DDD, TDD, Maven, GitHub Actions e interface grafica integrada a banco H2.

## Tema

1 - Sistema de Gestao de uma ONG de Adocao de Pets.

## Como executar

```bash
mvn clean test
mvn exec:java
```

Ao abrir a interface, e possivel cadastrar pets resgatados, marcar requisitos de saude e visualizar os registros persistidos em um banco H2 local.

## Arquitetura

```text
src/
  domain/          Regras de negocio, entidades, value objects e agregados
  application/     Casos de uso e portas de repositorio
  infrastructure/  Persistencia em H2
  presentation/    Interface grafica Swing
tests/             Testes unitarios do dominio
```

## DDD aplicado

- `Pet` e a raiz do agregado de resgate e adocao.
- `Adopter` representa o candidato a adotante.
- `AdoptionProcess` controla o fluxo de entrevista, aprovacao e conclusao.
- `PetId`, `AdopterId`, `AdoptionProcessId`, `PetName`, `Age`, `ContactInfo` e `MedicalRecord` sao Value Objects imutaveis.
- Regras de negocio ficam no pacote `domain`.

## Principais regras de negocio

- Um pet resgatado inicia em triagem.
- Um pet so pode ficar disponivel para adocao se estiver vacinado, castrado e sem tratamento pendente.
- Um processo de adocao so pode ser aprovado se o adotante tiver entrevista aprovada e o pet estiver disponivel.
- Um processo aprovado pode ser finalizado, alterando o status do pet para adotado.
- Um pet adotado nao pode voltar a ficar disponivel diretamente.

## TDD

Os testes em `tests/domain` cobrem cenarios de sucesso e falha das regras de dominio. Para evidenciar TDD no GitHub, faca commits pequenos seguindo o ciclo:

1. Adicione ou altere um teste.
2. Execute a pipeline e veja falhar quando a regra ainda nao existir.
3. Implemente a regra no dominio.
4. Execute a pipeline novamente ate ficar verde.

## Observacao sobre trabalho individual

O enunciado original pede grupos de 3 a 5 alunos. Como este projeto foi preparado para entrega individual, o `project-meta.json` contem apenas Kayo. Se o professor exigir grupo, adicione os demais integrantes antes de entregar.
