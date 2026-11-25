<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consultas</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<c:choose>
    <c:when test="${not empty medico}">
        <h2>Consultas do(a) Dr(a). <c:out value="${medico.nome}"/></h2>
        <a href="controller?acao=ListarConsultas">Ver todas as consultas</a><br><br>
    </c:when>
    <c:otherwise>
        <h2>Consultas</h2>
    </c:otherwise>
</c:choose>

<table>
    <tr>
        <th>ID</th>
        <th>Data</th>
        <th>Hora</th>
        <th>Paciente</th>
        <th>Médico</th>
        <th>Especialidade</th>
        <th>Tipo</th>
        <th>Status</th>
        <th>Exames</th>
        <th>Valor total (R$)</th>
        <th>Ações</th>
    </tr>
    <c:forEach var="c" items="${consultas}">
        <tr>
            <td>${c.id}</td>
            <td>${c.data}</td>
            <td>${c.hora}</td>
            <td><c:out value="${c.paciente.nome}"/></td>
            <td><c:out value="${c.medico.nome}"/></td>
            <td><c:out value="${c.especialidade}"/></td>
            <td><c:out value="${c.tipoAtendimento}"/></td>
            <td><c:out value="${c.status}"/></td>
            <td><c:out value="${c.exames}"/></td>
            <td>${c.valorTotal}</td>
            <td>
                <a href="controller?acao=VerConsulta&id=${c.id}">Ver</a> |
                <a href="controller?acao=FormConsulta&id=${c.id}">Editar</a> |
                <%-- V2: exclusao por POST (nao apaga mais por um simples link GET) --%>
                <form class="inline" action="controller" method="post" onsubmit="return confirm('Deseja excluir esta consulta?')">
                    <input type="hidden" name="acao" value="ExcluirConsulta">
                    <input type="hidden" name="id" value="${c.id}">
                    <button type="submit" class="link">Excluir</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty consultas}">
    <p>Nenhuma consulta cadastrada.</p>
</c:if>
</body>
</html>
