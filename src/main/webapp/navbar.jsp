<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"--%>
         <%--pageEncoding="ISO-8859-1" session="false"%>--%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.epam.lab.optional_courses.entity.User" %>
<%
    Locale locale = (Locale) request.getSession(false).getAttribute("locale");
    if(locale==null){
        locale=Locale.US;
    }
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/"><% out.print(bundle.getString("common.siteName")); %></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="/user" id="users" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <% out.print(bundle.getString("common.user")); %>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="/user/add"><% out.print(bundle.getString("user.add")); %></a>
                    <a class="dropdown-item" href="/user/edit?userId=<% User curUser =(User)request.getAttribute("curUser");
                    if(curUser!=null){
                        out.print(curUser.getId());
                    }
                    %>"><% out.print(bundle.getString("user.edit")); %></a>
                    <a class="dropdown-item" href="/user/all"><% out.print(bundle.getString("user.all")); %></a>
                    <a class="dropdown-item" href="/user/logout"><% out.print(bundle.getString("user.logout")); %></a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="courses" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <% out.print(bundle.getString("common.course")); %>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="/course/add"><% out.print(bundle.getString("course.add")); %></a>
                    <a class="dropdown-item" href="/course"><% out.print(bundle.getString("course.all")); %></a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="languages" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
<% out.print(bundle.getString("common.lang")); %>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="/?lang=ru">EN</a>
                    <a class="dropdown-item" href="/?lang=en">RU</a>
                </div>
            </li>
        </ul>
    </div>
</nav>