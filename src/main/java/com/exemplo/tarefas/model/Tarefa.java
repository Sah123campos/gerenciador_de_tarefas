package com.exemplo.tarefas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String descricao;

    @NotNull(message = "Informe a prioridade")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade = Prioridade.MEDIA;

    @NotNull(message = "Informe o status")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDENTE;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataVencimento;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    private LocalDateTime concluidaEm;

    @PrePersist
    void aoCriar() {
        criadaEm = LocalDateTime.now();
        atualizarConclusao();
    }

    @PreUpdate
    void aoAtualizar() {
        atualizarConclusao();
    }

    private void atualizarConclusao() {
        if (status == Status.CONCLUIDA && concluidaEm == null) {
            concluidaEm = LocalDateTime.now();
        } else if (status != Status.CONCLUIDA) {
            concluidaEm = null;
        }
    }

    /** Verdadeiro se a tarefa não foi concluída e a data de vencimento já passou. */
    public boolean isAtrasada() {
        return status != Status.CONCLUIDA
                && dataVencimento != null
                && dataVencimento.isBefore(LocalDate.now());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public LocalDateTime getCriadaEm() { return criadaEm; }
    public LocalDateTime getConcluidaEm() { return concluidaEm; }
}
