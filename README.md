# Checkpoint 5 — Bug Hunt PetFiap

## Identificação

**Grupo:** GreenAV

| Integrante | RM | Turma |
|---|---|---|
| Gabriel Vergueiro | 564439 | 2CCPX |
| Isabella Piñeiro Santana | 562779 | 2CCPX |
| Pedro Henrique Souza Barreto | 564437 | 2CCPX |
| Filippo Picino Mendez | 565994 | 2CCPX |
| Pam Mitchell Barbosa Durham | 562635 | 2CCPX |
| Gustavo Henrique Rodovalho | 563695 | 2CCPX |

| Campo | Resultado |
|---|---|
| **Total de bugs corrigidos** | 12 / 12 causas de comportamento documentadas abaixo |
| **Total de ajustes de Clean Code** | 6 / 6, incluindo os ajustes anteriores preservados |
| **Total de testes novos escritos** | 6 / 6 |
| **Suíte final** | 26 testes, 0 falhas, 0 erros, 0 ignorados (20 originais + 6 novos) |

O projeto Maven está na subpasta `petfiap-bughunt`. Este README segue as seções do
`AVALIACAO_README_TEMPLATE.md`, que foi preservado sem alterações.
As correções anteriores que já cumpriam o contrato foram mantidas.
O gerador de protocolos e o ID de persistência também foram mantidos por orientação expressa.
As mudanças desta etapa foram separadas em commits semânticos após autorização.

### Execução dos testes

Requisitos: JDK 17 ou superior e Maven.

```powershell
cd petfiap-bughunt
mvn test
```

Os testes usam JUnit e Mockito, sem iniciar Spring ou conectar ao Oracle.
No Eclipse, importe essa subpasta como **Existing Maven Projects** e execute
`src/test/java` com **Run As → JUnit Test**.
Não foram adicionadas dependências nem alterado o `pom.xml`.
O `application.properties` continua com `SEU_RM` e `SUA_SENHA`.
O primeiro build pode precisar de rede para obter as dependências; a execução dos testes não usa banco ou rede.
Validação final realizada com JDK 21, compilação para Java 17 e Maven 3.9.11:
`mvn test` terminou com **BUILD SUCCESS** em 02/10/2026.

## Parte 1 — Bugs encontrados

A numeração acompanha a ordem do histórico de correções, com o preço do banho
acrescentado ao final. As duas recusas de cancelamento são uma única causa raiz:
a ausência de validação do estado atual.
Os caminhos abaixo são relativos a `petfiap-bughunt/src/main/java/br/com/fiap/petfiap/`;
as linhas aproximadas referem-se ao código atual.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | O nome informado para o pet não chegava ao atendimento. | `builder/AtendimentoBuilder.java`, linha 24: o parâmetro era atribuído a si mesmo. | Preservado `this.petNome = petNome`, já corrigido em `c04e17e`. | Builder; escopo de atributos e parâmetros |
| bug02 | Solicitar TOSA criava um Banho. | `factory/AtendimentoFactory.java`, linha 17: o case instanciava a subclasse errada. | Preservado `new Tosa(...)`, corrigido em `444c46a`. | Factory; polimorfismo |
| bug03 | A consulta não recebia os dados; a tentativa de correção anterior também impedia a compilação. | `model/ConsultaVeterinaria.java`, linha 17: inicialmente usava `super()`; em `e4a3b81` passou a chamar um construtor de quatro argumentos que não existe. | Completada a chamada com os cinco argumentos, incluindo `tutorNome`. | Herança; encadeamento de construtores Commit `a31363d`. |
| bug04 | A Tosa herdava duração de 30 minutos; depois da tentativa anterior, o projeto deixou de compilar. | `model/Tosa.java`, linha 40: `getDuracaoMinutos(String porte)` era uma sobrecarga; `823d20c` adicionou `@Override` sem corrigir a assinatura. | Alterado para `getDuracaoMinutos()` com `@Override`, retornando 60. | Sobrescrita versus sobrecarga Commit `9171e80`. |
| bug05 | Buscar ID inexistente retornava `null`. | `service/AgendaService.java`, linha 41: um `catch (Exception)` escondia a exceção de domínio. | Preservado `orElseThrow`, sem a captura genérica, corrigido em `1318f06`. | Exceções unchecked; propagação de erros |
| bug06 | Chamadas ao Singleton criavam instâncias diferentes e reiniciavam a sequência. | `model/GeradorProtocolo.java`, método `getInstancia`: a instância criada não era armazenada. | Preservada a atribuição ao campo estático, corrigida em `1b97860`; nenhuma alteração no gerador nesta etapa. | Singleton; estado compartilhado |
| bug07 | O Builder aceitava atendimento sem nome do pet. | `builder/AtendimentoBuilder.java`, linha 41: faltava validar `petNome`. | Preservada a recusa de nome nulo ou em branco, corrigida em `b642f20`. | Validação; objeto válido |
| bug08 | O Builder aceitava atendimento sem porte. | `builder/AtendimentoBuilder.java`, linha 45: faltava validar `petPorte`. | Preservada a recusa de porte nulo ou em branco, corrigida em `ea43b4c`. | Encapsulamento; validação |
| bug09 | O mesmo pet podia ter dois agendamentos no mesmo horário. | `service/AgendaService.java`, linhas 29–31: nome e data eram comparados com `==`. | Preservadas as comparações por valor com `Objects.equals`, corrigidas em `9349452`, e o filtro `AGENDADO`. | Identidade versus igualdade de objetos |
| bug10 | A agenda aceitava data/hora no passado. | `service/AgendaService.java`, linhas 23–25: a validação não existia. | Preservada a validação antes de acessar o repository, corrigida em `c7d646a`. | Regras de negócio; validação antecipada |
| bug11 | Atendimentos CONCLUIDO e CANCELADO podiam ser cancelados. | `model/Atendimento.java`, linhas 65–72: `cancelar()` mudava o status sem verificar a origem. | Preservada a exigência de `AGENDADO`, corrigida em `0149b9c`, com `StatusInvalidoException` nos outros estados. | Máquina de estados; exceções de domínio |
| bug12 | O teste novo do banho encontrou PEQUENO a R$100; GRANDE também estava invertido. | `model/Banho.java`, linhas 27–32: os retornos de PEQUENO e GRANDE estavam trocados. | Corrigidos os preços para R$60, R$80 e R$100, respectivamente. | Regra de negócio; teste de regressão Commit `8d9dfe3`. |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | `AtendimentoFactory.criar` | Parâmetros de uma letra dificultavam entender os argumentos. | Preservado o ajuste de `23dde3f`: nomes como `protocolo`, `opcaoAtendimento`, `petNome` e `dataHora`. |
| clean02 | `AgendaService.buscarPorId` | A captura genérica escondia a causa do erro e exigia tratar `null` depois. | Preservado o ajuste de `1318f06`: propagar a exceção de domínio criada por `orElseThrow`. |
| clean03 | `AgendaService.agendar` | O serviço misturava regras da agenda e impressão de recibo no console. | Removida a impressão; após validar, o método retorna `repository.save(novo)`. Commit `b950a56`. |
| clean04 | Final de `AtendimentoController` | Código morto e planos para funcionalidades futuras deixavam o controller maior sem comportamento em uso. | Removidos `calcularDescontoFidelidade` e os comentários sobre descontos ainda não implementados. Commit `244ea3b`. |
| clean05 | `AtendimentoBuilder.construir` | O comentário atribuía a validação ao controller, contradizendo o código; a indentação escondia a estrutura do método. | Atualizado o comentário para a validação no Builder e alinhados os blocos. Commit `dd4ba39`. |
| clean06 | Imports de `Atendimento` e `AtendimentoController` | Imports com `*` não mostravam quais tipos cada classe usa. | Substituídos por imports explícitos; mantidos os mesmos tipos e o mesmo mapeamento JPA. Commit `30012b9`. |

Também foi normalizada a indentação de blocos já corrigidos no service e no model,
e restaurado `@Override` em `Tosa.calcularPontosFidelidade`.
As regras de status, as validações existentes e os valores já corretos foram preservados.

## Parte 3 — Testes novos (regras que estavam sem cobertura)

Cada teste fica em uma classe nova e segue Arrange, Act e Assert.
As dependências do serviço são mocks do `AtendimentoRepository`.
Os sete arquivos que contêm os 20 testes entregues permanecem intactos.

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | `BanhoPrecoTest.deveCobrarPrecoDoContratoQuandoPorteDoPetVariar` | Banho custa R$60 / R$80 / R$100 para PEQUENO / MEDIO / GRANDE. | Vermelho: `PEQUENO ==> expected: <60.0> but was: <100.0>`, revelando bug12. Commit `154e1b3`. |
| teste02 | `TosaDuracaoTest.deveDurar60MinutosQuandoAtendimentoForTosa` | Uma Tosa referenciada como `Atendimento` dura 60 minutos. | Verde: a assinatura do bug04 já havia sido completada para permitir compilar. A assinatura antiga herdava 30 minutos. Commit `710e717`. |
| teste03 | `ConsultaVeterinariaPrecoTest.deveCobrar150ReaisQuandoPorteDoPetVariar` | Consulta custa R$150 em qualquer um dos três portes. | Verde de cara: o preço fixo já estava correto e foi mantido. Commit `b141409`. |
| teste04 | `AgendaServiceDataPassadaTest.deveRecusarAgendamentoQuandoDataHoraEstiverNoPassado` | Recusar data passada com `IllegalArgumentException`, sem consultar nem salvar no repository. | Verde de cara nesta etapa: o bug10 já estava corrigido no histórico. `verifyNoInteractions` protege a ordem da validação. Commit `8ad1c8e`. |
| teste05 | `AgendaServiceCancelamentoTest.devePermitirCancelamentoSomenteQuandoAtendimentoEstiverAgendado` | Cancelar AGENDADO e salvar; recusar CONCLUIDO e CANCELADO sem alterar estado ou salvar. | Verde de cara nesta etapa: a validação do bug11 já estava corrigida no histórico. O teste percorre os três estados do contrato. Commit `929bac5`. |
| teste06 | `AgendaServiceConclusaoCanceladaTest.deveRecusarConclusaoQuandoAtendimentoEstiverCancelado` | Recusar conclusão de CANCELADO com `StatusInvalidoException`, preservando o estado e sem salvar. | Verde de cara: `concluir()` já permitia somente AGENDADO. Commit `847f22d`. |

O enunciado descreve quatro regras inicialmente defeituosas e duas inicialmente corretas.
Neste histórico, data passada e cancelamento já tinham sido corrigidos antes da criação dos testes,
e a Tosa precisou ser reparada antes de executar qualquer teste.
Por isso, a execução real dos seis testes novos teve uma falha e cinco sucessos,
antes da correção do banho. Essa diferença registra o estado encontrado, sem inventar execuções.

## Parte 4 — Perguntas de reflexão

### 1. A suíte como contrato (Aula 15)

O enunciado descreve o legado com 20 testes e nove falhas, mas esta revisão recebeu correções anteriores.
O primeiro `mvn test` nem chegou aos testes: apontou o construtor incompleto de `ConsultaVeterinaria` e o `@Override` incompatível em `Tosa`.
Depois de reparar essas duas causas, os 20 testes originais passaram sem nenhuma alteração nos arquivos de teste.
O teste novo `BanhoPrecoTest` mostrou exatamente `expected: <60.0> but was: <100.0>`, direcionando a leitura para `Banho.calcularPreco`.
A correção trocou os retornos dos portes pequeno e grande, mantendo o preço médio.
Essa verificação é repetível e também exercita os demais cenários, sem montar chamadas curl manualmente.
Como os testes usam mocks, uma indisponibilidade do Oracle não impede a verificação do contrato de negócio.

### 2. Mock e injeção de dependência (Aulas 13 a 15)

Em produção, o Spring cria o bean `AgendaService` e injeta o repository no campo anotado com `@Autowired`.
O repository real é uma implementação gerada pelo Spring Data JPA e suas operações podem acessar o banco.
Em `AgendaServiceTest`, `@ExtendWith(MockitoExtension.class)` inicializa as anotações do Mockito.
O `@Mock` cria um substituto do `AtendimentoRepository`, enquanto `@InjectMocks` fornece esse substituto ao service.
Por exemplo, `when(repository.findById(99L)).thenReturn(Optional.empty())` simula um ID inexistente sem SQL.
O service continua executando o código real de `buscarPorId`, portanto o teste verifica a exceção de domínio.
Nenhum contexto Spring ou conexão Oracle é necessário, porque o colaborador externo foi substituído pelo mock.

### 3. `==` vs `.equals()` (Aula 7)

Em referências, `==` pergunta se duas variáveis apontam para a mesma instância.
Dois nomes ou horários com o mesmo conteúdo podem ser objetos diferentes, especialmente quando chegam de requisições separadas.
Literais como `"Rex"` podem compartilhar uma instância pelo pool de Strings, escondendo o defeito em exemplos simples.
`AgendaServiceTest` recria a data com `LocalDateTime.parse`, de modo que o conteúdo é igual sem depender da identidade.
A comparação anterior com `==` deixava esse agendamento duplicado passar.
O código preservado usa `Objects.equals` para comparar valores e ainda exige que o atendimento existente esteja AGENDADO.
Assim, o conflito gera `HorarioOcupadoException` e o repository não recebe uma gravação.

### 4. Sobrescrita vs sobrecarga (Aula 7)

`Atendimento` define `getDuracaoMinutos()` sem parâmetros, com duração padrão de 30 minutos.
A Tosa antiga declarava `getDuracaoMinutos(String porte)`, criando uma sobrecarga que não substituía o método da classe pai.
Por isso, `atendimento.getDuracaoMinutos()` no resumo do controller continuava usando a duração padrão.
Adicionar `@Override` à assinatura antiga, como aconteceu no histórico, transforma esse engano em erro de compilação.
A correção completa removeu o parâmetro, preservou `@Override` e retornou os 60 minutos exigidos pelo contrato.
`TosaDuracaoTest` chama o método por uma referência `Atendimento`, verificando o despacho polimórfico usado pelo controller.
A anotação ajuda o compilador a detectar a assinatura errada antes que ela se torne um defeito em execução.

### 5. Singleton manual vs bean do Spring (Aula 14)

O Singleton manual guarda uma instância estática de `GeradorProtocolo` e concentra nela o contador de protocolos.
O defeito inicial retornava uma instância nova sem guardá-la, fazendo cada chamada começar outra sequência.
A correção anterior `1b97860` passou a armazenar a instância; esta etapa manteve o arquivo integralmente.
Os testes entregues verificam a mesma referência e a sequência 1, 2, 3 em chamadas sequenciais.
Já `AgendaService`, anotado com `@Service`, tem sua criação e reutilização administradas pelo container Spring no escopo singleton padrão.
Esse escopo vale por container e não torna automaticamente seguros os dados mutáveis de um bean.
Os testes sequenciais do gerador também não comprovam segurança concorrente; nenhuma mudança de protocolo foi feita, conforme solicitado.

### 6. Cobertura de testes: onde parar? (Aula 15)

Vale manter os testes que passaram de cara, pois eles registram regras que poderiam sofrer regressões.
`ConsultaVeterinariaPrecoTest`, por exemplo, protege o preço fixo para todos os portes mesmo sem exigir correção.
`AgendaServiceConclusaoCanceladaTest` protege uma transição proibida que os testes originais não verificavam.
Os testes de data passada e cancelamento também protegem correções anteriores e verificam que operações recusadas não salvam.
Com prazo limitado, priorizaríamos o caminho feliz principal, regras de preço e erros que permitem operações inválidas ou gravações indevidas.
O caso do banho mostra que todos os testes antigos podem passar e ainda faltar uma regra importante do contrato.
A meta deve ser confiança nas regras e nos efeitos observáveis; atingir 100% de linhas, sozinho, não comprova correção.

## Parte 5 — Espaço livre (opcional)
