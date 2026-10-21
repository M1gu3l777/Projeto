<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Comentário - Facebook CRUD</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>

<div class="container">

    <div class="col-md-5 mt-3">
        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/comments">
            Voltar
        </a>
    </div>

    <div class="row mt-3">
        <div class="col-md-6">

            <h2>
                <c:choose>
                    <c:when test="${not empty comment.id}">
                        Editar Comentário
                    </c:when>
                    <c:otherwise>
                        Cadastrar Comentário
                    </c:otherwise>
                </c:choose>
            </h2>

            <c:choose>
                <c:when test="${not empty comment.id}">
                    <c:set var="formAction" value="/comments/update"/>
                </c:when>
                <c:otherwise>
                    <c:set var="formAction" value="/comments/save"/>
                </c:otherwise>
            </c:choose>

            <form method="post"
                  action="${pageContext.request.contextPath}${formAction}">

                <c:if test="${not empty comment.id}">
                    <input type="hidden" name="id"
                           value="${comment.id}">
                </c:if>

                <div class="mb-3">
                    <label for="content" class="form-label">
                        Comentário
                    </label>

                    <textarea id="content"
                              name="content"
                              class="form-control"
                              rows="4"
                              required><c:out value="${comment.content}"/></textarea>
                </div>

                <div class="mb-3">
                    <label for="user_id" class="form-label">
                        Usuário
                    </label>

                    <select id="user_id" name="user_id"
                            class="form-select" required>
                        <option value="">Selecione um usuário</option>

                        <c:forEach var="user" items="${users}">
                            <option value="${user.id}"
                                <c:if test="${not empty comment.user and comment.user.id == user.id}">
                                    selected
                                </c:if>>
                                <c:out value="${user.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="post_id" class="form-label">
                        Post
                    </label>

                    <select id="post_id" name="post_id"
                            class="form-select" required>
                        <option value="">Selecione um post</option>

                        <c:forEach var="post" items="${posts}">
                            <option value="${post.id}"
                                <c:if test="${not empty comment.post and comment.post.id == post.id}">
                                    selected
                                </c:if>>
                                #<c:out value="${post.id}"/> -
                                <c:out value="${post.content}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">
                    Salvar
                </button>

                <a href="${pageContext.request.contextPath}/comments"
                   class="btn btn-secondary">
                    Cancelar
                </a>

            </form>

        </div>
    </div>

</div>

<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</body>
</html>