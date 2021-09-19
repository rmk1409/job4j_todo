<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<div class="row">
  <ul class="nav">
    <c:if test="${user == null}">
      <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/auth.do">Войти</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="<%=request.getContextPath()%>/reg.do">Регистрация</a>
      </li>
    </c:if>
    <li class="nav-item">
      <a class="nav-link" href="<%=request.getContextPath()%>/items.do">Список
        дел</a>
    </li>
    <c:if test="${user != null}">
      <li class="nav-item">
        <c:out value="${user.email}"/>
      </li>
      <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/logout.do">Выйти</a>
      </li>
    </c:if>
  </ul>
</div>
