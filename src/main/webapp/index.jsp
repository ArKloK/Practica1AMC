<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="src.model.Linea"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <c:if test="${!empty requestScope.linea}">
            <h1>Los puntos mas cercanos son: ${requestScope.linea.p1.id} y ${requestScope.linea.p2.id}</h1>
        </c:if>
        
        
    </body>
</html>
