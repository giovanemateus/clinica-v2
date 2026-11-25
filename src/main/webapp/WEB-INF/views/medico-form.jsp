<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Médico</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>${empty medico.id or medico.id == 0 ? 'Novo Médico' : 'Editar Médico'}</h2>

<c:if test="${not empty erro}">
    <p class="erro"><c:out value="${erro}"/></p>
</c:if>

<form class="cadastro" action="controller" method="post">
    <input type="hidden" name="acao" value="SalvarMedico">
    <input type="hidden" name="id" value="${medico.id}">

    <label>Nome *</label>
    <input type="text" name="nome" value="<c:out value='${medico.nome}'/>" required>

    <label>CRM *</label>
    <input type="text" name="crm" value="<c:out value='${medico.crm}'/>" required>

    <label>Especialidade *</label>
    <input type="text" name="especialidade" value="<c:out value='${medico.especialidade}'/>" required>

    <label>Telefone</label>
    <input type="text" name="telefone" value="<c:out value='${medico.telefone}'/>">

    <input class="botao" type="submit" value="Salvar">
</form>
</body>
</html>
