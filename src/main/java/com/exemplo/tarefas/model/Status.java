package com.exemplo.tarefas.model;

public enum Status {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
