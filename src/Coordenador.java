public final class Coordenador extends Funcionario {

    public Coordenador(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getCargo() {
        return "Coordenador";
    }

    @Override
    public boolean batePonto() {
        return true;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 5;
    }
}