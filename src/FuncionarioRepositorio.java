import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Lista de funcionários mantida apenas em memória.
 */
public class FuncionarioRepositorio {

    private final List<Funcionario> funcionarios = new ArrayList<>();

    /** Retorna false se já existir um funcionário com o mesmo id. */
    public boolean adicionar(Funcionario funcionario) {
        if (buscar(funcionario.getId()).isPresent()) {
            return false;
        }
        funcionarios.add(funcionario);
        return true;
    }

    public Optional<Funcionario> buscar(int id) {
        for (Funcionario f : funcionarios) {
            if (f.getId() == id) {
                return Optional.of(f);
            }
        }
        return Optional.empty();
    }

    public List<Funcionario> listar() {
        return Collections.unmodifiableList(funcionarios);
    }

    /** Retorna false se o id não existir. */
    public boolean remover(int id) {
        return funcionarios.removeIf(f -> f.getId() == id);
    }
}