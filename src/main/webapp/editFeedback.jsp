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

<div class="topWrapper">
    <div class="card" style="width: 18rem;">
        <img class="card-img-top" src="<% out.print("static/images/akalji.jpg"); %>" alt="Card image cap"
             height="320px" width="240"/>
        <div class="card-body">
            <p class="card-text"><% out.print(request.getAttribute("cardText") != null ? "" : (String) request.getAttribute("cardText")); %></p>
        </div>
    </div>
    <div class="information">
        <%
            for (EntryKV field:fields) {
                out.println("<dl class=\"row\">");
                out.println("<dt class=\"col-sm-3\">"+bundle.getString(field.getName())+"</dt>");
                out.println("<dt class=\"col-sm-9\">"+field.getValue()+"</dt>");
                out.println("</dl>");
            }

        %>
    </div>
    <div class="feedback">
        <form action="" method="POST">
        <dl class="row">
            <dt class="col-sm-3"><% out.print(bundle.getString("feedback.grade")); %></dt>
            <dt class="col-sm-9"><input class="form-control" name="grade" value="<% out.print(request.getAttribute("grade")); %>"/></dt>
        </dl>
        <dl class="row">
            <dt class="col-sm-3"><% out.print(bundle.getString("feedback.feedbackBody")); %></dt>
            <dt class="col-sm-9"><textarea rows="20" cols="60" class="form-control" name="feedbackBody" ><% out.print(request.getAttribute("feedbackBody")); %>  </textarea></dt>
        </dl>
            <button type="submit" class="btn btn-primary"><% bundle.getString("common.submit"); %></button>
    </form>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>