package com.exemplo.tarefas;

import com.exemplo.tarefas.model.Prioridade;
import com.exemplo.tarefas.model.Status;
import com.exemplo.tarefas.model.Tarefa;
import com.exemplo.tarefas.repository.TarefaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/** Cria algumas tarefas de exemplo quando o banco está vazio. */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final TarefaRepository repository;

    public DadosIniciais(TarefaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        LocalDate hoje = LocalDate.now();
        repository.save(nova("Estudar Spring Boot", "Revisar controllers, services e JPA.",
                Prioridade.ALTA, Status.EM_ANDAMENTO, hoje.plusDays(2)));
        repository.save(nova("Pagar conta de luz", null,
                Prioridade.ALTA, Status.PENDENTE, hoje.minusDays(1)));
        repository.save(nova("Fazer compras do mês", "Arroz, feijão, café e frutas.",
                Prioridade.MEDIA, Status.PENDENTE, hoje.plusDays(5)));
        repository.save(nova("Ler um capítulo do livro", null,
                Prioridade.BAIXA, Status.CONCLUIDA, null));
    }

    private Tarefa nova(String titulo, String descricao, Prioridade p, Status s, LocalDate venc) {
        Tarefa t = new Tarefa();
        t.setTitulo(titulo);
        t.setDescricao(descricao);
        t.setPrioridade(p);
        t.setStatus(s);
        t.setDataVencimento(venc);
        return t;
    }
}
