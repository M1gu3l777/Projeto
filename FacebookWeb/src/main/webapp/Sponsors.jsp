
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Patrocinadores - Facebook CRUD</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body class="bg-light">

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1>Patrocinadores</h1>
            <p class="text-muted mb-0">
                Gerencie os patrocinadores cadastrados no sistema.
            </p>
        </div>

        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/"
               class="btn btn-secondary">
                Voltar
            </a>

            <a href="${pageContext.request.contextPath}/sponsors/new"
               class="btn btn-primary">
                + Novo patrocinador
            </a>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">

            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Investimento (R$)</th>
                            <th>Usuário responsável</th>
                            <th style="width: 190px;">Ações</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:choose>
                            <c:when test="${not empty sponsors_list}">
                                <c:forEach var="s" items="${sponsors_list}">
                                    <tr>
                                        <td>${s.id}</td>
                                        <td><c:out value="${s.name}" /></td>
                                        <td>
                                            R$ <c:out value="${s.investment}" />
                                        </td>
                                        <td><c:out value="${s.user.name}" /></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/sponsors/edit?id=${s.id}"
                                               class="btn btn-warning btn-sm">
                                                Editar
                                            </a>

                                            <form action="${pageContext.request.contextPath}/sponsors/delete"
                                                  method="post"
                                                  class="d-inline"
                                                  onsubmit="return confirm('Deseja realmente excluir este patrocinador?');">
                                                <input type="hidden" name="id" value="${s.id}">

                                                <button type="submit"
                                                        class="btn btn-danger btn-sm">
                                                    Excluir
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-4">
                                        Nenhum patrocinador cadastrado.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

        </div>
    </div>

</div>

</body>
</html>
