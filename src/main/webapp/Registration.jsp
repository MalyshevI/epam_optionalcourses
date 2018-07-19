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
    <form action="/register" method="POST" class="form-signin" >
        <br/>

        <input type="text" id="name" name="name" placeholder=<% out.print(bundle.getString("reg.name")); %>
                 class="form-control" />
        <br/>

        <input type="text" id="lastName" name="lastName" placeholder=<% out.print(bundle.getString("reg.lastName")); %>
                  class="form-control" />
        <br/>

        <input type="text" id="email" name="email"  placeholder=<% out.print(bundle.getString("common.email")); %>
               class="form-control" />
        <br/>

        <input type="password" id="password" name="password" placeholder=<% out.print(bundle.getString("common.password")); %>
                 class="form-control" />
        <br/>
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