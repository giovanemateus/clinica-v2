<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pacientes</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>Pacientes</h2>
<a href="controller?acao=FormPaciente">+ Novo Paciente</a><br><br>

<table>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>CPF</th>
        <th>Telefone</th>
        <th>E-mail</th>
        <th>Ações</th>
    </tr>
    <c:forEach var="p" items="${pacientes}">
        <tr>
            <td>${p.id}</td>
            <td><c:out value="${p.nome}"/></td>
            <td><c:out value="${p.cpf}"/></td>
            <td><c:out value="${p.telefone}"/></td>
            <td><c:out value="${p.email}"/></td>
            <td>
                <a href="controller?acao=FormPaciente&id=${p.id}">Editar</a> |
                <%-- V2: exclusao por POST (nao apaga mais por um simples link GET) --%>
                <form class="inline" action="controller" method="post" onsubmit="return confirm('Deseja excluir este paciente?')">
                    <input type="hidden" name="acao" value="ExcluirPaciente">
                    <input type="hidden" name="id" value="${p.id}">
                    <button type="submit" class="link">Excluir</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
