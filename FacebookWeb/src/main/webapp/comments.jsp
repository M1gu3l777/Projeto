<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Comentários - Facebook CRUD</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>

<div class="container">

    <div class="col-md-5 mt-3">
        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/">
            Home
        </a>
    </div>

    <div class="row">
        <h2>Comentários</h2>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col">Id</th>
                    <th scope="col">Usuário</th>
                    <th scope="col">Post</th>
                    <th scope="col">Comentário</th>
                    <th scope="col">Data</th>
                    <th scope="col">Ações</th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var="comment" items="${comments_list}">
                    <tr>
                        <th scope="row">
                            <c:out value="${comment.id}"/>
                        </th>

                        <td>
                            <c:out value="${comment.user.name}"/>
                        </td>

                        <td>
                            <c:out value="${comment.post.id}"/>
                        </td>

                        <td>
                            <c:out value="${comment.content}"/>
                        </td>

                        <td>
                            <c:out value="${comment.commentDate}"/>
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/comments/update?id=${comment.id}"
                               class="bi bi-pencil-square"></a>

                            <a href="${pageContext.request.contextPath}/comments/delete?id=${comment.id}"
                               class="bi bi-trash"
                               onclick="return confirm('Deseja realmente excluir este comentário?');"></a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <a href="${pageContext.request.contextPath}/comments/form"
       class="btn btn-primary">
        Cadastrar Comentário
    </a>

</div>

<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</body>
</html>