<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List" %>
<%@ page import="com.epam.lab.optional_courses.service.components.EntryKV" %>

<%
    Locale locale = (Locale) request.getAttribute("Locale");
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
        <div class="card" style="width: 18rem;">
            <img class="card-img-top" src="<% out.print("static/images/akalji.jpg"); %>" alt="Card image cap"
                 height="320px" width="240">
            <div class="card-body">
                <p class="card-text"><% out.print(request.getAttribute("cardText") != null ? "" : (String) request.getAttribute("cardText")); %></p>
            </div>
        </div>
        <div class="information">
            <form action="" method="POST">
                <%
                    for (EntryKV field : fields) {
                        out.print("<dl class=\"row\">");
                        out.print("<dt class=\"col-sm-3\">" + field.getName() + "</dt>");
                        out.print("<dd class=\"col-sm-9\"><input class=\"form-control\" placeholder=\"" + field.getValue() + "\"></dd>");
                        out.print("</dl>");
                    }
                %>
                <button type="submit" class="btn btn-primary"><% bundle.getString("common.submit"); %></button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
