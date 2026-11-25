<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Erro</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>Ocorreu um erro</h2>
<p class="erro"><c:out value="${erro}"/></p>
<a href="javascript:history.back()">Voltar</a>
</body>
</html>
