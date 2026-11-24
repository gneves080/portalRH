public final class Analista extends Funcionario {

    public Analista(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getCargo() {
        return "Analista";
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