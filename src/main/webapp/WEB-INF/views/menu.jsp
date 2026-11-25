<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h1>Clínica - Sistema de Agendamento</h1>
<div class="menu">
    <a href="controller?acao=ListarConsultas">Consultas</a>
    <a href="controller?acao=FormConsulta">Nova Consulta</a>
    <a href="controller?acao=ListarPacientes">Pacientes</a>
    <a href="controller?acao=ListarMedicos">Médicos</a>
    <a href="api/consultas" target="_blank">API REST (JSON)</a>
</div>
<%-- mensagens de sucesso (vem pelo parametro msg). V2: JSTL no lugar do scriptlet --%>
<c:if test="${param.msg == 'salvo'}">
    <p class="sucesso">Registro salvo com sucesso!</p>
</c:if>
<c:if test="${param.msg == 'excluido'}">
    <p class="sucesso">Registro excluído com sucesso!</p>
</c:if>
