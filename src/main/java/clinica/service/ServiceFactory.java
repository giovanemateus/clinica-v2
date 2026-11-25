package clinica.service;

import clinica.dao.DAOFactory;

/**
 * FACTORY METHOD dos services.
 * E aqui que as classes concretas (ConsultaDAO) sao ligadas as abstracoes,
 * assim os Commands e a API nao precisam montar as dependencias.
 */
public class ServiceFactory {

    public static AgendaService criarAgendaService() {
        return new AgendaService(DAOFactory.criarConsultaDAO());
    }

    public static ConsultaService criarConsultaService() {
        return new ConsultaService(DAOFactory.criarConsultaDAO(), criarAgendaService());
    }
}
