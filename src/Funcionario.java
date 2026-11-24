import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base de todos os tipos de funcionário. As subclasses definem apenas o que
 * muda entre os cargos: se batem ponto e quantas horas extras podem fazer.
 */
public abstract class Funcionario {

    public static final Duration ALMOCO = Duration.ofHours(1);
    public static final Duration JORNADA_PADRAO = Duration.ofHours(8);
    public static final LocalTime ENTRADA_MINIMA = LocalTime.of(6, 0);
    public static final LocalTime SAIDA_MAXIMA = LocalTime.of(22, 0);

    private final int id;
    private final String nome;
    private final List<RegistroPonto> registros = new ArrayList<>();

    protected Funcionario(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public final int getId() {
        return id;
    }

    public final String getNome() {
        return nome;
    }

    public final List<RegistroPonto> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public abstract String getCargo();

    /** Gerentes e estagiários não batem ponto. */
    public abstract boolean batePonto();

    /** Limite de horas extras por dia (0 para quem não bate ponto). */
    public abstract int getLimiteHorasExtras();

    /**
     * Valida e registra o ponto do dia. Horas trabalhadas = (saída - entrada) - 1h de almoço.
     * Horas extras = o que passar da jornada padrão de 8h.
     */
    public final RegistroPonto registrarPonto(LocalDate data, LocalTime entrada, LocalTime saida)
            throws RegistroInvalidoException {

        if (!batePonto()) {
            throw new RegistroInvalidoException(
                    "Funcionários do tipo " + getCargo() + " não batem ponto; "
                            + "não é possível registrar horários.");
        }
        if (!saida.isAfter(entrada)) {
            throw new RegistroInvalidoException(
                    "O horário de saída deve ser posterior ao horário de entrada.");
        }
        if (entrada.isBefore(ENTRADA_MINIMA)) {
            throw new RegistroInvalidoException("O horário de entrada não pode ser antes das 06:00.");
        }
        if (saida.isAfter(SAIDA_MAXIMA)) {
            throw new RegistroInvalidoException("O horário de saída não pode ser após as 22:00.");
        }
        for (RegistroPonto existente : registros) {
            if (existente.getData().equals(data)) {
                throw new RegistroInvalidoException("Já existe um registro para este funcionário nesta data.");
            }
        }

        Duration trabalhadas = Duration.between(entrada, saida).minus(ALMOCO);
        if (trabalhadas.isNegative() || trabalhadas.isZero()) {
            throw new RegistroInvalidoException(
                    "O período informado não é maior que a 1 hora de almoço descontada.");
        }

        Duration extras = trabalhadas.compareTo(JORNADA_PADRAO) > 0
                ? trabalhadas.minus(JORNADA_PADRAO)
                : Duration.ZERO;
        if (extras.compareTo(Duration.ofHours(getLimiteHorasExtras())) > 0) {
            throw new RegistroInvalidoException(
                    "Limite de horas extras excedido: " + getCargo() + " pode fazer no máximo "
                            + getLimiteHorasExtras() + "h extras por dia (registro teria "
                            + RegistroPonto.formatar(extras) + ").");
        }

        RegistroPonto registro = new RegistroPonto(data, entrada, saida, trabalhadas, extras);
        registros.add(registro);
        return registro;
    }

    @Override
    public String toString() {
        return String.format("[id %d] %s - %s", id, nome, getCargo());
    }
}