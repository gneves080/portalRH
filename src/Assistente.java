public final class Assistente extends Funcionario {

    public Assistente(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getCargo() {
        return "Assistente";
    }

    @Override
    public boolean batePonto() {
        return true;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 3;
    }
}