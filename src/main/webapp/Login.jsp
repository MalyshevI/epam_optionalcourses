<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"--%>
         <%--pageEncoding="ISO-8859-1" session="false"%>--%>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %><%
    Locale locale = Locale.US;
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
%>

<html>
<head>
    <jsp:include page="header.jsp"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container">
    <form action="/auth" method="POST" class="form-signin" >
        <h3 class="form-signin-heading" text=<% out.print(bundle.getString("login.welcome")); %>></h3>
        <br/>

        <input type="text" id="email" name="email"  placeholder="Email"
               class="form-control" /> <br/>
        <input type="password"  placeholder=<% out.print(bundle.getString("common.password")); %>
               id="password" name="password" class="form-control" /> <br />
        <input type = "submit" value = <% out.print(bundle.getString("common.submit")); %> />
        <td><span style="color:red"><%
            String errorMSG = (String)request.getAttribute("ErrorMessage");
            if(errorMSG!=null){
                out.print(errorMSG);
            }

        %></span></td>
    </form>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>