# Checkpoint 5 — Bug Hunt PetFiap

> Copie este arquivo para a raiz do seu repositório com o nome **README.md**
> e preencha todas as seções.

## Identificação

**Grupo:** ___

| Integrante | RM | Turma |
|---|---|---|
| | | |
| | | |
| | | |
| | | |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | ___ / 12 |
| **Total de ajustes de Clean Code** | ___ / 6 |
| **Total de testes novos escritos** | ___ / 6 |
| **Suíte final (Run As → JUnit Test)** | ___ testes, ___ falhas |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | O nome informado para o pet não aparece no atendimento criado. | `AtendimentoBuilder.java`, método `comPet`: o parâmetro era atribuído a si mesmo, sem `this.petNome`. | Alterar para `this.petNome = petNome`. | Padrão Builder; escopo de atributos e parâmetros |
| bug02 | O Builder aceita a criação de atendimento sem nome do pet. | `AtendimentoBuilder.java`, método `construir`: não valida `petNome`. | Validar nome nulo ou vazio e lançar `IllegalArgumentException`. | Validação; objeto válido |
| bug03 | O Builder aceita a criação de atendimento sem porte do pet. | `AtendimentoBuilder.java`, método `construir`: não valida `petPorte`. | Validar porte nulo ou vazio e lançar `IllegalArgumentException`. | Validação; encapsulamento |
| bug04 | Ao solicitar uma TOSA, a Factory devolve um objeto `Banho`. | `AtendimentoFactory.java`, caso `"TOSA"` retorna `new Banho(...)`. | Retornar `new Tosa(...)`. | Padrão Factory; polimorfismo |
| bug05 | A consulta criada não mantém protocolo, pet, porte, tutor ou data. | `ConsultaVeterinaria.java`, construtor chama `super()` sem parâmetros. | Encaminhar todos os parâmetros para `super(protocolo, petNome, petPorte, tutorNome, dataHora)`. | Herança; reutilização de construtor |
| bug06 | A duração da Tosa continua sendo 30 minutos. | `Tosa.java`, método declarado como `getDuracaoMinutos(String porte)`, criando sobrecarga em vez de sobrescrita. | Alterar para `getDuracaoMinutos()` e usar `@Override`. | Sobrescrita versus sobrecarga; polimorfismo |
| bug07 | O Singleton cria objetos diferentes e reinicia a numeração. | `GeradorProtocolo.java`, `getInstancia()` retorna `new GeradorProtocolo()` sem atribuir a `instancia`. | Atribuir a nova instância ao campo estático antes de retorná-la. | Padrão Singleton |
| bug08 | Um atendimento no mesmo horário pode ser agendado novamente para o mesmo pet. | `AgendaService.java`, comparação de nome e data usa `==`. | Usar comparação por valor, como `Objects.equals(...)`. | Igualdade de objetos; `equals` |
| bug09 | Buscar um atendimento inexistente retorna `null` em vez de lançar a exceção prevista. | `AgendaService.java`, captura genérica transforma a exceção em `null`. | Remover o `catch` genérico e propagar `AtendimentoNaoEncontradoException`. | Exceções; tratamento de erros |
| bug10 | É possível agendar atendimento com data e hora no passado. | `AgendaService.java`, método `agendar` não valida `dataHora`. | Validar a data antes de consultar o repository e lançar `IllegalArgumentException`. | Regra de negócio; validação |
| bug11 | Um atendimento concluído pode ser cancelado. | `Atendimento.java`, método `cancelar` altera o status sem verificar o estado atual. | Permitir cancelamento somente quando o status for `AGENDADO`; caso contrário, lançar `StatusInvalidoException`. | Máquina de estados; exceções de domínio |
| bug12 | Um atendimento já cancelado pode ser cancelado novamente. | `Atendimento.java`, método `cancelar` também aceita o status `CANCELADO`. | Aplicar a mesma validação: somente atendimentos `AGENDADO` podem ser cancelados. | Regras de transição de status; encapsulamento |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | | | |
| clean02 | | | |
| clean03 | | | |
| clean04 | | | |
| clean05 | | | |
| clean06 | | | |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | | | |
| teste02 | | | |
| teste03 | | | |
| teste04 | | | |
| teste05 | | | |
| teste06 | | | |

---

## Parte 4 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do
> projeto como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. A suíte como contrato (Aula 15)
O projeto chegou com 20 testes, 9 vermelhos. Descreva como você usou as
mensagens de falha (ex.: `expected: <Rex> but was: <null>`) para caçar os bugs.
O que a suíte de testes tem de melhor do que testar tudo na mão com curl?

### 2. Mock e injeção de dependência (Aulas 13 a 15)
No `AgendaServiceTest`, o `@Mock` cria um `AtendimentoRepository` falso e o
`@InjectMocks` o injeta no service. Explique a relação disso com o `@Autowired`
que o Spring faz em produção — quem "injeta" em cada mundo, e por que o teste
consegue rodar sem banco e sem subir o Spring?

### 3. `==` vs `.equals()` (Aula 7)
Um dos bugs fazia o agendamento duplicado passar pela verificação de conflito.
Explique por que `==` entre Strings e `LocalDateTime` falhou aqui, por que ele
"funciona por sorte" com literais como `"Rex"`, e o que a sua correção mudou.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: um método parecia sobrescrever
`getDuracaoMinutos`, mas na verdade criava uma assinatura nova. Explique a
diferença entre override e overload nesse caso e por que a anotação `@Override`
teria impedido o bug.

### 5. Singleton manual vs bean do Spring (Aula 14)
O `GeradorProtocolo` é um Singleton escrito à mão e causou um dos bugs.
Explique o que ele garante, qual foi o bug, e por que o `AgendaService`
(`@Service`) não corre o mesmo risco no container do Spring.

### 6. Cobertura de testes: onde parar? (Aula 15)
Dos 6 testes novos que você escreveu, alguns ficaram vermelhos (revelaram
bugs) e outros verdes de cara (regras já corretas). Vale a pena manter os que
ficaram verdes? Em um projeto real com prazo, o que você priorizaria testar:
caminho feliz, caminhos de erro, ou 100% de cobertura? Justifique.

---

## Parte 5 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
