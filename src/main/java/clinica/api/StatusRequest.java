package clinica.api;

/** DTO do PATCH: so o novo status. Ex.: { "status": "Cancelada" } */
public record StatusRequest(String status) {
}
