<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"--%>
         <%--pageEncoding="ISO-8859-1" session="false"%>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List" %>
<%@ page import="com.epam.lab.optional_courses.service.components.EntryKV" %>

<%
    Locale locale = (Locale) request.getAttribute("locale");
    locale = Locale.US;
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
    List<EntryKV> fields = (List<EntryKV>) request.getAttribute("entryList");
%>

<html>
<head>
    <jsp:include page="header.jsp"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="content">
    <div class="topWrapper">
        <div class="information">
            <form action="" method="POST">
                <%
                    for (EntryKV field:fields) {
                        out.println("<dl class=\"row\">");
                        out.println("<dt class=\"col-sm-3\">" + bundle.getString(field.getName())+"</dt>");
                        out.println("<dt class=\"col-sm-9\"><input class=\"form-control\" name=\"" + field.getName() + "\" value=\""+field.getValue()+"\"></dt>");
                        out.println("</dl>");
                    }

                %>
                <button type="submit" class="btn btn-primary"><% out.print(bundle.getString("common.submit")); %></button>
                <a class="btn btn-primary" href="<% out.print(request.getHeader("referer")); %>" role="button"><% out.print(bundle.getString("common.cancel")); %></a>
            </form>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
