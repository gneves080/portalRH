import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private static final Scanner scanner = new Scanner(System.in);
    private static final FuncionarioRepositorio repositorio = new FuncionarioRepositorio();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   Bem-vindo(a) ao Portal de RH");
        System.out.println("==============================================");

        try {
            boolean executando = true;
            while (executando) {
                exibirMenu();
                Integer opcao = lerInteiro("Escolha uma opção: ");
                if (opcao == null) {
                    continue;
                }
                switch (opcao) {
                    case 1: criarFuncionario(); break;
                    case 2: listarFuncionarios(); break;
                    case 3: buscarFuncionario(); break;
                    case 4: removerFuncionario(); break;
                    case 5: registrarPonto(); break;
                    case 6: listarRegistros(); break;
                    case 0: executando = false; break;
                    default: System.out.println("Opção inválida! Escolha um número entre 0 e 6.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada.");
        }

        System.out.println("Saindo do portal. Até logo!");
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("---------------- MENU ----------------");
        System.out.println("  1 - Criar funcionário");
        System.out.println("  2 - Listar funcionários");
        System.out.println("  3 - Buscar funcionário por id");
        System.out.println("  4 - Remover funcionário por id");
        System.out.println("  5 - Registrar horário de entrada e saída");
        System.out.println("  6 - Listar registros de horas de um funcionário");
        System.out.println("  0 - Sair");
        System.out.println("--------------------------------------");
    }

    private static void criarFuncionario() {
        System.out.println("Tipos: 1 - Gerente | 2 - Coordenador | 3 - Analista | 4 - Assistente | 5 - Estagiário");
        Integer tipo = lerInteiro("Tipo do funcionário: ");
        if (tipo == null) {
            return;
        }
        if (tipo < 1 || tipo > 5) {
            System.out.println("Tipo inexistente! Escolha um número entre 1 e 5.");
            return;
        }

        Integer id = lerInteiro("Id: ");
        if (id == null) {
            return;
        }
        if (repositorio.buscar(id).isPresent()) {
            System.out.println("Já existe um funcionário com o id " + id + ".");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("O nome não pode ser vazio.");
            return;
        }

        Funcionario funcionario;
        switch (tipo) {
            case 1: funcionario = new Gerente(id, nome); break;
            case 2: funcionario = new Coordenador(id, nome); break;
            case 3: funcionario = new Analista(id, nome); break;
            case 4: funcionario = new Assistente(id, nome); break;
            default: funcionario = new Estagiario(id, nome); break;
        }
        repositorio.adicionar(funcionario);
        System.out.println("Funcionário criado: " + funcionario);
    }

    private static void listarFuncionarios() {
        List<Funcionario> todos = repositorio.listar();
        if (todos.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }
        System.out.println("Funcionários cadastrados:");
        for (Funcionario f : todos) {
            System.out.println("  " + f);
        }
    }

    private static void buscarFuncionario() {
        Funcionario f = pedirFuncionario();
        if (f != null) {
            System.out.println("Encontrado: " + f);
        }
    }

    private static void removerFuncionario() {
        Integer id = lerInteiro("Id do funcionário a remover: ");
        if (id == null) {
            return;
        }
        if (repositorio.remover(id)) {
            System.out.println("Funcionário " + id + " removido.");
        } else {
            System.out.println("Nenhum funcionário encontrado com o id " + id + ".");
        }
    }

    private static void registrarPonto() {
        Funcionario f = pedirFuncionario();
        if (f == null) {
            return;
        }
        if (!f.batePonto()) {
            System.out.println("Registro negado: " + f.getCargo() + " não bate ponto.");
            return;
        }

        LocalDate data = lerData("Data (dd/MM/aaaa): ");
        if (data == null) {
            return;
        }
        LocalTime entrada = lerHora("Horário de entrada (HH:mm): ");
        if (entrada == null) {
            return;
        }
        LocalTime saida = lerHora("Horário de saída (HH:mm): ");
        if (saida == null) {
            return;
        }

        try {
            RegistroPonto registro = f.registrarPonto(data, entrada, saida);
            System.out.println("Registro salvo (1h de almoço descontada): " + registro);
        } catch (RegistroInvalidoException e) {
            System.out.println("Registro recusado: " + e.getMessage());
        }
    }

    private static void listarRegistros() {
        Funcionario f = pedirFuncionario();
        if (f == null) {
            return;
        }
        if (!f.batePonto()) {
            System.out.println(f.getCargo() + " não bate ponto, portanto não possui registros de horas.");
            return;
        }
        if (f.getRegistros().isEmpty()) {
            System.out.println("Nenhum registro para " + f.getNome() + ".");
            return;
        }
        System.out.println("Registros de " + f.getNome() + ":");
        for (RegistroPonto r : f.getRegistros()) {
            System.out.println("  " + r);
        }
    }

    /** Pede um id e devolve o funcionário, ou null (com mensagem) se não for possível. */
    private static Funcionario pedirFuncionario() {
        Integer id = lerInteiro("Id do funcionário: ");
        if (id == null) {
            return null;
        }
        Optional<Funcionario> encontrado = repositorio.buscar(id);
        if (!encontrado.isPresent()) {
            System.out.println("Nenhum funcionário encontrado com o id " + id + ".");
            return null;
        }
        return encontrado.get();
    }

    private static Integer lerInteiro(String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Digite um número inteiro.");
            return null;
        }
    }

    private static LocalDate lerData(String mensagem) {
        System.out.print(mensagem);
        try {
            return LocalDate.parse(scanner.nextLine().trim(), DATA);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida! Use o formato dd/MM/aaaa com uma data existente.");
            return null;
        }
    }

    private static LocalTime lerHora(String mensagem) {
        System.out.print(mensagem);
        try {
            return LocalTime.parse(scanner.nextLine().trim(), HORA);
        } catch (DateTimeParseException e) {
            System.out.println("Horário inválido! Use o formato HH:mm (por exemplo, 08:30).");
            return null;
        }
    }
}