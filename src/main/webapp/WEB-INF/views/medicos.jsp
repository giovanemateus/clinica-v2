<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Médicos</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>Médicos</h2>
<a href="controller?acao=FormMedico">+ Novo Médico</a><br><br>

<table>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>CRM</th>
        <th>Especialidade</th>
        <th>Telefone</th>
        <th>Ações</th>
    </tr>
    <c:forEach var="m" items="${medicos}">
        <tr>
            <td>${m.id}</td>
            <td><c:out value="${m.nome}"/></td>
            <td><c:out value="${m.crm}"/></td>
            <td><c:out value="${m.especialidade}"/></td>
            <td><c:out value="${m.telefone}"/></td>
            <td>
                <a href="controller?acao=ListarConsultas&medicoId=${m.id}">Consultas</a> |
                <a href="controller?acao=FormMedico&id=${m.id}">Editar</a> |
                <%-- V2: exclusao por POST (nao apaga mais por um simples link GET) --%>
                <form class="inline" action="controller" method="post" onsubmit="return confirm('Deseja excluir este médico?')">
                    <input type="hidden" name="acao" value="ExcluirMedico">
                    <input type="hidden" name="id" value="${m.id}">
                    <button type="submit" class="link">Excluir</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
