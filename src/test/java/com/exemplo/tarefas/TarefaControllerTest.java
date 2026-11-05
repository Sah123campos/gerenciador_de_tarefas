package com.exemplo.tarefas;

import com.exemplo.tarefas.model.Status;
import com.exemplo.tarefas.model.Tarefa;
import com.exemplo.tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TarefaControllerTest {

    @Autowired MockMvc mvc;
    @Autowired TarefaRepository repository;

    @BeforeEach
    void limpar() {
        repository.deleteAll();
    }

    @Test
    void criaTarefaValida() throws Exception {
        mvc.perform(post("/tarefas")
                        .param("titulo", "Nova tarefa de teste")
                        .param("prioridade", "ALTA")
                        .param("status", "PENDENTE")
                        .param("dataVencimento", "2030-01-15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tarefas"));

        assertThat(repository.findAll()).singleElement()
                .satisfies(t -> assertThat(t.getTitulo()).isEqualTo("Nova tarefa de teste"));
    }

    @Test
    void rejeitaTituloVazio() throws Exception {
        mvc.perform(post("/tarefas").param("titulo", " ")
                        .param("prioridade", "MEDIA").param("status", "PENDENTE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("O título é obrigatório")));
        assertThat(repository.count()).isZero();
    }

    @Test
    void listaFiltraEAlterna() throws Exception {
        Tarefa t = new Tarefa();
        t.setTitulo("Comprar pão");
        t = repository.save(t);

        mvc.perform(get("/tarefas").param("busca", "pão"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comprar pão")));

        mvc.perform(post("/tarefas/" + t.getId() + "/alternar"))
                .andExpect(status().is3xxRedirection());
        Tarefa atualizada = repository.findById(t.getId()).orElseThrow();
        assertThat(atualizada.getStatus()).isEqualTo(Status.CONCLUIDA);
        assertThat(atualizada.getConcluidaEm()).isNotNull();
    }

    @Test
    void tarefaInexistenteRetorna404() throws Exception {
        mvc.perform(get("/tarefas/9999/editar")).andExpect(status().isNotFound());
    }
}
