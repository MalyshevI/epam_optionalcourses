<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"--%>
         <%--pageEncoding="ISO-8859-1" session="false"%>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List" %>
<%@ page import="com.epam.lab.optional_courses.service.components.EntryKV" %>
<%@ page import="com.epam.lab.optional_courses.entity.User" %>
<%@ page import="com.epam.lab.optional_courses.entity.Course" %>


<%
    Locale locale = (Locale) request.getAttribute("locale");
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
    List<EntryKV> fields = (List<EntryKV>) request.getAttribute("entryList");
    User pageUser = (User) request.getAttribute("pageUser");
    User curUser = (User) request.getAttribute("curUser");
    Course pageCourse = (Course) request.getAttribute("pageCourse");
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
            <a class="btn btn-primary" href="<% out.print(request.getHeader("referer")); %>" role="button"><% out.print(bundle.getString("common.back")); %></a>
            <a class="btn btn-primary" href="<%
            if(pageUser!=null){
                out.print("/user/edit?userId=" + pageUser.getId());
            }
            if(pageCourse!=null){
                out.print("/course/"+ pageCourse.getId() + "/edit");
            }

            %>" role="button"><% out.print(bundle.getString("common.edit")); %></a>
            <%
                if(pageCourse!=null) {
                    boolean isUserOnCourse = (boolean) request.getAttribute("isUserOnCourse");
                    if(isUserOnCourse) {
                        out.print("<form action=\"/course/" + pageCourse.getId() + "/delete\" method=\"POST\">\n" +
                                "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.leave") + " " + bundle.getString("common.from") + " " + bundle.getString("common.course") + "</button>\n" +
                                "<input name=\"userId\" type=\"hidden\" value=\"" + curUser.getId() + "\">" +
                                "</form>");
                    }else {
                        out.print("<form action=\"/course/"+ pageCourse.getId() + "/apply\" method=\"POST\">\n" +
                                "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.apply") + " " + bundle.getString("common.course") + "</button>\n" +
                                "<input name=\"userId\" type=\"hidden\" value=\""+ curUser.getId() +"\">" +
                                "</form>");
                    }
                }
            %>

            <%
                for (EntryKV field:fields) {
                    out.println("<dl class=\"row\">");
                    out.println("<dt class=\"col-sm-3\">"+bundle.getString(field.getName())+"</dt>");
                    out.println("<dt class=\"col-sm-9\">"+field.getValue()+"</dt>");
                    out.println("</dl>");
                }

            %>
        </div>
    </div>
</div>

<div class="table">
<jsp:include page="list.jsp"/>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>