# Gerenciador de Tarefas

Aplicação web para gerenciar tarefas, feita com **Spring Boot 3**, **Thymeleaf**, **Spring Data JPA** e banco **H2**.

🔗 **Demonstração online:** https://sah123campos.github.io/gerenciador_de_tarefas/

> O GitHub Pages só hospeda arquivos estáticos e não executa Java. Por isso, a demonstração online é o
> `index.html` da raiz: uma versão em HTML/CSS/JavaScript com o mesmo visual e as mesmas funções, que salva
> as tarefas no navegador (localStorage). A aplicação Spring Boot completa fica em `src/` e roda com Maven.

## Funcionalidades
- Criar, editar e excluir tarefas (título, descrição, prioridade, status e vencimento)
- Marcar como concluída / reabrir com um clique
- Resumo por status e filtros por status, prioridade e busca por texto
- Destaque para tarefas atrasadas
- Validação de formulário com mensagens em português
- Dados salvos em arquivo (`./data`), com tarefas de exemplo na primeira execução

## Requisitos
- Java 21+
- Maven 3.9+

## Como executar
```bash
mvn spring-boot:run
```
Acesse http://localhost:8080. O console do banco fica em http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:file:./data/tarefas`, usuário `sa`, sem senha).

## Testes
```bash
mvn test
```

## Estrutura
```
src/main/java/com/exemplo/tarefas
├── controller/   TarefaController, HomeController
├── model/        Tarefa, Prioridade, Status
├── repository/   TarefaRepository (consulta com filtros)
├── service/      TarefaService
└── DadosIniciais.java (tarefas de exemplo)
src/main/resources
├── templates/    lista.html, formulario.html, erro.html, fragments/layout.html
└── static/css/   estilo.css
```
