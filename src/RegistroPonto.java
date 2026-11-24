import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro de horas de um funcionário em um dia (imutável).
 */
public final class RegistroPonto {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDate data;
    private final LocalTime entrada;
    private final LocalTime saida;
    private final Duration horasTrabalhadas;
    private final Duration horasExtras;

    public RegistroPonto(LocalDate data, LocalTime entrada, LocalTime saida,
                         Duration horasTrabalhadas, Duration horasExtras) {
        this.data = data;
        this.entrada = entrada;
        this.saida = saida;
        this.horasTrabalhadas = horasTrabalhadas;
        this.horasExtras = horasExtras;
    }

    public LocalDate getData() {
        return data;
    }

    public Duration getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public Duration getHorasExtras() {
        return horasExtras;
    }

    public static String formatar(Duration duracao) {
        return String.format("%dh%02dmin", duracao.toHours(), duracao.toMinutesPart());
    }

    @Override
    public String toString() {
        return String.format("%s | entrada %s | saída %s | trabalhadas %s | extras %s",
                data.format(DATA), entrada.format(HORA), saida.format(HORA),
                formatar(horasTrabalhadas), formatar(horasExtras));
    }
}