<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>Consulta nº ${consulta.id}</h2>

<table>
    <tr><th>Paciente</th><td><c:out value="${consulta.paciente.nome}"/></td></tr>
    <tr><th>Médico</th><td><c:out value="${consulta.medico.nome}"/></td></tr>
    <tr><th>Data</th><td>${consulta.data}</td></tr>
    <tr><th>Hora</th><td>${consulta.hora}</td></tr>
    <tr><th>Especialidade</th><td><c:out value="${consulta.especialidade}"/></td></tr>
    <tr><th>Tipo de atendimento</th><td><c:out value="${consulta.tipoAtendimento}"/></td></tr>
    <tr><th>Status</th><td><c:out value="${consulta.status}"/></td></tr>
    <tr><th>Valor da consulta</th><td>R$ ${consulta.valor}</td></tr>
    <tr><th>Exames</th><td><c:out value="${empty consulta.exames ? 'Nenhum' : consulta.exames}"/></td></tr>
    <%-- V2: descricao e custo montados pelo Decorator --%>
    <tr><th>Atendimento</th><td><c:out value="${atendimento.descricao}"/></td></tr>
    <tr><th>Valor total</th><td>R$ ${consulta.valorTotal}</td></tr>
    <tr><th>Forma de pagamento</th><td><c:out value="${consulta.formaPagamento}"/></td></tr>
    <tr><th>Observações</th><td><c:out value="${consulta.observacoes}"/></td></tr>
    <tr><th>Criada em</th><td>${consulta.dataCriacao}</td></tr>
</table>
<br>
<a href="controller?acao=FormConsulta&id=${consulta.id}">Editar</a> |
<a href="controller?acao=ListarConsultas">Voltar</a>

<%-- Relacionamento 1:1: cada consulta tem no maximo um registro de atendimento --%>
<h3>Registro de Atendimento</h3>
<c:choose>
    <c:when test="${not empty consulta.registro}">
        <table>
            <tr><th>Diagnóstico</th><td><c:out value="${consulta.registro.diagnostico}"/></td></tr>
            <tr><th>Prescrição</th><td><c:out value="${consulta.registro.prescricao}"/></td></tr>
            <tr><th>Data do registro</th><td>${consulta.registro.dataRegistro}</td></tr>
        </table>
    </c:when>
    <c:otherwise>
        <p>Esta consulta ainda não tem registro de atendimento.</p>
        <form class="cadastro" action="controller" method="post">
            <input type="hidden" name="acao" value="SalvarRegistro">
            <input type="hidden" name="consultaId" value="${consulta.id}">
            <label>Diagnóstico *</label>
            <textarea name="diagnostico" rows="2" required></textarea>
            <label>Prescrição</label>
            <textarea name="prescricao" rows="2"></textarea>
            <input class="botao" type="submit" value="Salvar Registro">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
