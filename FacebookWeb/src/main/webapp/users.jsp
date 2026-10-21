
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Usuários - FacebookWeb</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
        body {
            background: #f3f5f9;
        }

        .container {
            margin-top: 40px;
        }

        .card {
            border: none;
            border-radius: 14px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, .08);
        }

        .acoes {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .acoes form {
            margin: 0;
        }
    </style>
</head>

<body>
<div class="container mb-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Usuários cadastrados</h2>

        <a href="${pageContext.request.contextPath}/users/form"
           class="btn btn-primary">
            Novo usuário
        </a>
    </div>

    <div class="card">
        <div class="card-body">

            <c:choose>
                <c:when test="${empty users_list}">
                    <div class="alert alert-info mb-0">
                        Nenhum usuário cadastrado.
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover align-middle">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>Sexo</th>
                                <th>E-mail</th>
                                <th>Ações</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="user" items="${users_list}">
                                <tr>
                                    <td><c:out value="${user.id}"/></td>
                                    <td><c:out value="${user.name}"/></td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${user.gender == 'M'}">
                                                Masculino
                                            </c:when>
                                            <c:when test="${user.gender == 'F'}">
                                                Feminino
                                            </c:when>
                                            <c:otherwise>
                                                Não informado
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td><c:out value="${user.email}"/></td>

                                    <td>
                                        <div class="acoes">
                                            <a class="btn btn-warning btn-sm"
                                               href="${pageContext.request.contextPath}/users/update?userId=${user.id}">
                                                Editar
                                            </a>

                                            <form action="${pageContext.request.contextPath}/users/delete"
                                                  method="post"
                                                  onsubmit="return confirm('Tem certeza que deseja excluir este usuário?');">

                                                <input type="hidden"
                                                       name="userId"
                                                       value="${user.id}">

                                                <button type="submit"
                                                        class="btn btn-danger btn-sm">
                                                    Excluir
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

    <a href="${pageContext.request.contextPath}/"
       class="btn btn-secondary mt-3">
        Voltar ao início
    </a>

</div>
</body>
</html>
