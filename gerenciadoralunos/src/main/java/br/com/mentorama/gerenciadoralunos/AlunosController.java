package br.com.mentorama.gerenciadoralunos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gerenciadoralunos")
public class AlunosController {

    public final List<Aluno> alunos;

    public AlunosController() {
        this.alunos = new ArrayList<>();
    }


    //listar todos os alunos podendo filtrar por nome ou idade
    @GetMapping
    public List<Aluno> findAll(@RequestParam(required = false) String nome, @RequestParam(required = false) Integer idade){
        if(nome != null && idade !=null){
            return alunos.stream()
                    .filter(aluno -> aluno.getNome().contains(nome) && aluno.getIdade().equals(idade))
                    .collect(Collectors.toList());
        } else if (nome!=null){
            return alunos.stream()
                    .filter(aluno -> aluno.getNome().contains(nome))
                    .collect(Collectors.toList());
        } else if (idade!=null){
            return alunos.stream()
                    .filter(aluno -> aluno.getIdade().equals(idade))
                    .collect(Collectors.toList());
        }
        return alunos;
    }

    //buscar aluno por id
    @GetMapping("/{id}")
    public Aluno findById(@PathVariable("id") Integer id){
        return this.alunos.stream()
                .filter(aluno -> aluno.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    //cadastrar novo aluno
    @PostMapping
    public ResponseEntity<Integer> add(@RequestBody Aluno aluno){
        if(aluno.getId() == null){
            aluno.setId(alunos.size()+1);
        }
        alunos.add(aluno);
        return new ResponseEntity<>(aluno.getId(), HttpStatus.CREATED);
    }

    //editar cadastro
    @PutMapping
    public ResponseEntity update(@RequestBody Aluno aluno){
        alunos.stream()
                .filter(estudante -> estudante.getId().equals(aluno.getId()))
                .forEach(estudante -> {
                    estudante.setIdade(aluno.getIdade());
                    estudante.setNome(aluno.getNome());}
                );
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //remover registro de aluno
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id){
        alunos.removeIf(aluno -> aluno.getId().equals(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
