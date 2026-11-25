<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<%@include file="menu.jsp"%>

<h2>${empty consulta.id or consulta.id == 0 ? 'Nova Consulta' : 'Editar Consulta'}</h2>

<c:if test="${not empty erro}">
    <p class="erro"><c:out value="${erro}"/></p>
</c:if>

<%-- AUTOMACAO: sugestao dos proximos horarios livres --%>
<c:if test="${not empty sugestoes}">
    <p><b>Próximos horários disponíveis para este médico:</b></p>
    <ul>
        <c:forEach var="s" items="${sugestoes}">
            <li>${s}</li>
        </c:forEach>
    </ul>
</c:if>

<form class="cadastro" action="controller" method="post">
    <input type="hidden" name="acao" value="SalvarConsulta">
    <input type="hidden" name="id" value="${consulta.id}">

    <label>Paciente *</label>
    <select name="pacienteId" required>
        <option value="">-- selecione --</option>
        <c:forEach var="p" items="${pacientes}">
            <option value="${p.id}" ${p.id == consulta.paciente.id ? 'selected' : ''}><c:out value="${p.nome}"/></option>
        </c:forEach>
    </select>

    <label>Médico *</label>
    <select name="medicoId" required>
        <option value="">-- selecione --</option>
        <c:forEach var="m" items="${medicos}">
            <option value="${m.id}" ${m.id == consulta.medico.id ? 'selected' : ''}><c:out value="${m.nome}"/> - <c:out value="${m.especialidade}"/></option>
        </c:forEach>
    </select>

    <label>Data *</label>
    <input type="date" name="data" value="${consulta.data}" required>

    <label>Horário *</label>
    <select name="hora" required>
        <option value="">-- selecione --</option>
        <c:forEach var="h" items="${horarios}">
            <option value="${h}" ${h == consulta.hora ? 'selected' : ''}>${h}</option>
        </c:forEach>
    </select>

    <label>Tipo de atendimento</label>
    <select name="tipoAtendimento">
        <c:forEach var="t" items="Presencial,Telemedicina,Retorno">
            <option ${t == consulta.tipoAtendimento ? 'selected' : ''}>${t}</option>
        </c:forEach>
    </select>

    <label>Status</label>
    <select name="status">
        <c:forEach var="s" items="Agendada,Confirmada,Realizada,Cancelada">
            <option ${s == consulta.status ? 'selected' : ''}>${s}</option>
        </c:forEach>
    </select>

    <label>Valor da consulta (R$)</label>
    <input type="number" name="valor" step="0.01" min="0" value="${consulta.valor}">

    <%-- V2: exames adicionais (padrao Decorator calcula o valor total) --%>
    <label>Exames</label>
    <div class="exames">
        <c:forEach var="e" items="${exames}">
            <label><input type="checkbox" name="exames" value="${e}" ${fn:contains(consulta.exames, e) ? 'checked' : ''}> ${e}</label>
        </c:forEach>
    </div>
    <small>Hemograma R$ 45,00 | Eletrocardiograma R$ 120,00 | Raio-X R$ 90,00 | Ultrassom R$ 150,00</small>

    <label>Forma de pagamento</label>
    <select name="formaPagamento">
        <c:forEach var="f" items="Dinheiro,PIX,Cartão,Convênio">
            <option ${f == consulta.formaPagamento ? 'selected' : ''}>${f}</option>
        </c:forEach>
    </select>

    <label>Observações</label>
    <textarea name="observacoes" rows="3"><c:out value="${consulta.observacoes}"/></textarea>

    <input class="botao" type="submit" value="Salvar">
</form>
</body>
</html>
