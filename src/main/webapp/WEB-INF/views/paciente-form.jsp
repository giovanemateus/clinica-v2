<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Paciente</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>${empty paciente.id or paciente.id == 0 ? 'Novo Paciente' : 'Editar Paciente'}</h2>

<c:if test="${not empty erro}">
    <p class="erro"><c:out value="${erro}"/></p>
</c:if>

<form class="cadastro" action="controller" method="post">
    <input type="hidden" name="acao" value="SalvarPaciente">
    <input type="hidden" name="id" value="${paciente.id}">

    <label>Nome *</label>
    <input type="text" name="nome" value="<c:out value='${paciente.nome}'/>" required>

    <label>CPF *</label>
    <input type="text" name="cpf" value="<c:out value='${paciente.cpf}'/>" placeholder="000.000.000-00" required>

    <label>Telefone</label>
    <input type="text" name="telefone" value="<c:out value='${paciente.telefone}'/>">

    <label>E-mail</label>
    <input type="email" name="email" value="<c:out value='${paciente.email}'/>">

    <input class="botao" type="submit" value="Salvar">
</form>
</body>
</html>
