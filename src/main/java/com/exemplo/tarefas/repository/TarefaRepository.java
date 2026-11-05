package com.exemplo.tarefas.repository;

import com.exemplo.tarefas.model.Prioridade;
import com.exemplo.tarefas.model.Status;
import com.exemplo.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("""
            SELECT t FROM Tarefa t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:prioridade IS NULL OR t.prioridade = :prioridade)
              AND (:busca IS NULL
                   OR LOWER(t.titulo) LIKE LOWER(CONCAT('%', :busca, '%'))
                   OR LOWER(t.descricao) LIKE LOWER(CONCAT('%', :busca, '%')))
            ORDER BY
              CASE t.status WHEN 'CONCLUIDA' THEN 1 ELSE 0 END,
              CASE WHEN t.dataVencimento IS NULL THEN 1 ELSE 0 END,
              t.dataVencimento,
              CASE t.prioridade WHEN 'ALTA' THEN 0 WHEN 'MEDIA' THEN 1 ELSE 2 END,
              t.criadaEm DESC
            """)
    List<Tarefa> filtrar(@Param("status") Status status,
                         @Param("prioridade") Prioridade prioridade,
                         @Param("busca") String busca);

    long countByStatus(Status status);
}
