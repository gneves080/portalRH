public final class Estagiario extends Funcionario {

    public Estagiario(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getCargo() {
        return "Estagiário";
    }

    @Override
    public boolean batePonto() {
        return false;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 0;
    }
}