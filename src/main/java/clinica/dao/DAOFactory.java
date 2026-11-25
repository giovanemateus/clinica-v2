package clinica.dao;

/**
 * FACTORY METHOD dos DAOs (DIP).
 * Os Commands, as APIs e os services pedem o DAO por aqui e recebem
 * a interface (abstracao). So esta classe conhece as classes concretas.
 */
public class DAOFactory {

    public static IConsultaDAO criarConsultaDAO() {
        return new ConsultaDAO();
    }

    public static IMedicoDAO criarMedicoDAO() {
        return new MedicoDAO();
    }

    public static IPacienteDAO criarPacienteDAO() {
        return new PacienteDAO();
    }

    public static IRegistroAtendimentoDAO criarRegistroAtendimentoDAO() {
        return new RegistroAtendimentoDAO();
    }
}
