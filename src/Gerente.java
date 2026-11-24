public final class Gerente extends Funcionario {

    public Gerente(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getCargo() {
        return "Gerente";
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