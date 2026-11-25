package clinica.service;

import clinica.dao.ConsultaDAO;

/**
 * FACTORY METHOD dos services.
 * E aqui que as classes concretas (ConsultaDAO) sao ligadas as abstracoes,
 * assim os Commands e a API nao precisam montar as dependencias.
 */
public class ServiceFactory {

    public static AgendaService criarAgendaService() {
        return new AgendaService(new ConsultaDAO());
    }

    public static ConsultaService criarConsultaService() {
        return new ConsultaService(new ConsultaDAO(), criarAgendaService());
    }
}
