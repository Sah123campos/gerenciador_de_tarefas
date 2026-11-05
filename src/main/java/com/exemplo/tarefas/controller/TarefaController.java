package com.exemplo.tarefas.controller;

import com.exemplo.tarefas.model.Prioridade;
import com.exemplo.tarefas.model.Status;
import com.exemplo.tarefas.model.Tarefa;
import com.exemplo.tarefas.service.TarefaNaoEncontradaException;
import com.exemplo.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @ModelAttribute
    public void dadosComuns(Model model) {
        model.addAttribute("prioridades", Prioridade.values());
        model.addAttribute("todosStatus", Status.values());
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Status status,
                         @RequestParam(required = false) Prioridade prioridade,
                         @RequestParam(required = false) String busca,
                         Model model) {
        model.addAttribute("tarefas", service.listar(status, prioridade, busca));
        model.addAttribute("resumo", service.resumo());
        model.addAttribute("filtroStatus", status);
        model.addAttribute("filtroPrioridade", prioridade);
        model.addAttribute("busca", busca);
        return "tarefas/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        return "tarefas/formulario";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("tarefa") Tarefa tarefa,
                        BindingResult result,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "tarefas/formulario";
        }
        service.salvar(tarefa);
        redirect.addFlashAttribute("mensagem", "Tarefa criada com sucesso!");
        return "redirect:/tarefas";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("tarefa", service.buscarPorId(id));
        return "tarefas/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("tarefa") Tarefa tarefa,
                            BindingResult result,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            tarefa.setId(id);
            return "tarefas/formulario";
        }
        service.atualizar(id, tarefa);
        redirect.addFlashAttribute("mensagem", "Tarefa atualizada com sucesso!");
        return "redirect:/tarefas";
    }

    @PostMapping("/{id}/alternar")
    public String alternar(@PathVariable Long id,
                           @RequestHeader(value = "Referer", required = false) String referer) {
        service.alternarConclusao(id);
        return "redirect:" + destinoSeguro(referer);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.excluir(id);
        redirect.addFlashAttribute("mensagem", "Tarefa excluída.");
        return "redirect:/tarefas";
    }

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String naoEncontrada(TarefaNaoEncontradaException e, Model model) {
        model.addAttribute("erro", e.getMessage());
        return "tarefas/erro";
    }

    /** Volta para a lista mantendo os filtros, sem redirecionar para fora da aplicação. */
    private String destinoSeguro(String referer) {
        if (referer != null) {
            try {
                java.net.URI uri = java.net.URI.create(referer);
                String caminho = uri.getRawPath();
                if (caminho != null && caminho.startsWith("/tarefas") && !caminho.startsWith("//")) {
                    return uri.getRawQuery() == null ? caminho : caminho + "?" + uri.getRawQuery();
                }
            } catch (IllegalArgumentException ignorada) {
                // Referer inválido: volta para a lista
            }
        }
        return "/tarefas";
    }
}
