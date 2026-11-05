package com.exemplo.tarefas.service;

import com.exemplo.tarefas.model.Prioridade;
import com.exemplo.tarefas.model.Status;
import com.exemplo.tarefas.model.Tarefa;
import com.exemplo.tarefas.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Tarefa> listar(Status status, Prioridade prioridade, String busca) {
        String termo = (busca == null || busca.isBlank()) ? null : busca.trim();
        return repository.filtrar(status, prioridade, termo);
    }

    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    @Transactional
    public Tarefa salvar(Tarefa tarefa) {
        return repository.save(tarefa);
    }

    @Transactional
    public Tarefa atualizar(Long id, Tarefa dados) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setTitulo(dados.getTitulo());
        tarefa.setDescricao(dados.getDescricao());
        tarefa.setPrioridade(dados.getPrioridade());
        tarefa.setStatus(dados.getStatus());
        tarefa.setDataVencimento(dados.getDataVencimento());
        return repository.save(tarefa);
    }

    /** Alterna entre concluída e pendente. */
    @Transactional
    public Tarefa alternarConclusao(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setStatus(tarefa.getStatus() == Status.CONCLUIDA ? Status.PENDENTE : Status.CONCLUIDA);
        return repository.save(tarefa);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public Map<String, Long> resumo() {
        Map<String, Long> resumo = new LinkedHashMap<>();
        resumo.put("total", repository.count());
        for (Status s : Status.values()) {
            resumo.put(s.name(), repository.countByStatus(s));
        }
        return resumo;
    }
}
