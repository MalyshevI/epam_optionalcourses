<%@ page import="com.epam.lab.optional_courses.controller.Common" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.epam.lab.optional_courses.entity.Course" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.sun.javafx.scene.control.skin.DateCellSkin" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="com.epam.lab.optional_courses.entity.User" %>
<%@ page import="java.net.URLClassLoader" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.io.File" %>
<%@ page import="com.epam.lab.optional_courses.entity.Feedback" %>
<!--@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" fmt:setBundle basename="i18n.WelcomeBundle" /> -->

<html>
<head>
    <title>Page Title</title>

    <!-- connect bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css"
          integrity="sha384-Smlep5jCw/wG7hdkwQ/Z5nLIefveQRIY9nfy6xoR1uRYBtpZgI6339F5dgvm/e9B" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
            integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js"
            integrity="sha384-o+RDsa0aLu++PJvFqy8fFScvbHFLtbvScb8AjopnFD+iEQ7wo/CG0xlczd+2O/em"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" type="text/css" href="static/css/list.css">

</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Navbar</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Link</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Dropdown
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="#">Action</a>
                    <a class="dropdown-item" href="#">Another action</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="#">Something else here</a>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link disabled" href="#">Disabled</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>
    </div>
</nav>

<div class="content">
    <table class="table">
        <thead>
        <tr>
                <%
                ResourceBundle bundle = ResourceBundle.getBundle("i18n", (Locale) request.getAttribute("locale"));

                Common.EntityType entityType = (Common.EntityType) request.getAttribute("entityType");
                Long offset = (Long) request.getAttribute("offset");
                System.out.println(offset);
                User curUser = (User) request.getAttribute("curUser");
                long countEntity = (long) request.getAttribute("countEntity");
                long countPages = countEntity / Common.limit + 1;
                long curPageNumber = offset / Common.limit + 1;


                int i;
                switch (entityType) {
                    case COURSE:
                        User pageUser = (User) request.getAttribute("pageUser");
                        List<Course> allCourses = (List<Course>) request.getAttribute("list");
                        List<Boolean> coursesEnrolledByCurUser = null;
                        List<Feedback> feedbackForUserCourses = null;
                        coursesEnrolledByCurUser = (List<Boolean>) request.getAttribute("coursesEnrolledByCurUser");
                        if(pageUser!= null){
                            feedbackForUserCourses = (List<Feedback>) request.getAttribute("feedbackForUserCourses");
                        }

                        // TABLE HEADER
                        out.println("<th width=\"40\" scope=\"col\">#</th>");
                        out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.courseName") + "</th>");
                        out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.tutor") + "</th>");
                        out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("course.status") + "</th>");
                        if(pageUser != null){
                            out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.grade") + "</th>");
                            if(pageUser.equals(curUser)){
                                out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.feedback") + "</th>");
                            }
                        }

                        out.println("</tr>\n" +
                                "        </thead>\n" +
                                "        <tbody>");
                    i = 0;
                    for (Course course: allCourses) {
                        //table records
                        out.println("<tr>");
                        out.println("<td width=\"40\">" + (offset + i + 1) + "</td>");
                        out.println("<td width=\"40\"> <a href=\"/course/" + course.getId() + "\"> " + course.getCourseName() + " </a></td>");
                        out.println("<td width=\"40\"> <a href=\"/user/" + course.getTutor().getId() + "\"> " + course.getTutor().getFirstName() + course.getTutor().getLastName() + " </a></td>");
                        Date curDate = new Date();
                        String status;
                        if (curDate.before(course.getStartDate())) {
                            status = bundle.getString("course.registrationOpen");
                        } else if (curDate.before(course.getFinishDate())) {
                            status = bundle.getString("course.courseGoing");
                        } else {
                            status = bundle.getString("course.courseFinished");
                        }
                        out.println("<td width=\"40\">" + status + "</td>");

                        if(pageUser != null){
                            if(feedbackForUserCourses.get(i) != null){
                                out.println("<td width=\"40\">" + feedbackForUserCourses.get(i).getGrade() + "</td>");
                                if(pageUser.equals(curUser)){
                                    out.println("<td width=\"40\"> <a href=\"/course/" + course.getId() + "getFeedback" + pageUser.getId() + "\"> " + bundle.getString("common.feedback") + " </a></td>");
                                }
                            }
                        }
                        // BUTTONS
                        if (curUser.isAdmin()) {
                            out.print("<td width=\"40\">");
                            out.print("<a class=\"btn btn-outline-primary\"\" href=\"/course/" + course.getId() + "/edit\" role=\"button\">" + bundle.getString("common.edit") + " " + bundle.getString("common.course") + "</a>");
                            out.print("<form action=\"/course/"+ course.getId() + "/delete\" method=\"DELETE\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.delete") + " " + bundle.getString("common.course") + "</button>\n" +
                                        "</form>");
                            out.print("</td>");
                        }
                        if (!curUser.equals(course.getTutor())) {
                            if(!status.equals(bundle.getString("course.courseFinished"))){
                                if (coursesEnrolledByCurUser.get(i)) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\"\" href=\"/course/" + course.getId() + "/leave\" role=\"button\">" + bundle.getString("common.leave") + " " + bundle.getString("common.course") + "</a>");
                                    out.print("</td>");
                                } else {
                                    if(pageUser!= null){
                                        out.print("<td width=\"40\">");
                                        out.print("<a class=\"btn btn-outline-primary\"\" href=\"/course/" + course.getId() + "/apply\" role=\"button\">" + bundle.getString("common.apply") + " " + bundle.getString("common.course") +"</a>");
                                        out.print("</td>");
                                    }
                                }
                            }
                        }
                        out.println("</tr>");
                        i++;

                    }
                    break;
                case USER:
                        List<User> allUsers = (List<User>) request.getAttribute("list");
                        Course pageCourse = (Course) request.getAttribute("pageCourse");
                        List<Feedback> feedbackForUsersOnCourse = null;
                        if(pageCourse != null){
                            feedbackForUsersOnCourse = (List<Feedback>) request.getAttribute("feedbackForUsersOnCourse");
                        }

                        // TABLE HEADER
                        out.println("<th width=\"40\" scope=\"col\">#</th>");
                        out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.student") + "</th>");
                        out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.email") + "</th>");
                        if(pageCourse != null){
                            out.println("<th width=\"40\" scope=\"col\">" + bundle.getString("common.grade") + "</th>");
                        }
                        out.println("</tr>\n" +
                                "        </thead>\n" +
                                "        <tbody>");
                    i = 0;
                    for (User user: allUsers) {

                        //TABLE RECORDS
                        out.println("<tr>");
                        out.println("<td width=\"40\">" + (offset + i + 1) + "</td>");
                        out.println("<td width=\"40\"> <a href=\"/user/" + user.getId() + "\"> " + user.getFirstName() + user.getLastName() + " </a></td>");
                        out.println("<td width=\"40\"> <a href=\"mailto:" + user.getEmail() + "\"> " + user.getEmail() + " </a></td>");

                        //BUTTONS
                        if(pageCourse != null){
                            Feedback feedback = feedbackForUsersOnCourse.get(i);
                            if(feedback != null){
                                out.println("<td width=\"40\">" + feedback.getGrade() + "</td>");
                                if (curUser.equals(pageCourse.getTutor())) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\"\" href=\"/course/" + pageCourse.getId() + "/edit/"+ user.getId() +"\" role=\"button\">" + bundle.getString("common.edit") + " " + bundle.getString("common.feedback") + "</a>");
                                    out.print("</td>");
                                }
                            }else{
                                if (curUser.equals(pageCourse.getTutor())) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\"\" href=\"/course/" + pageCourse.getId() + "/add/"+ user.getId() +"\" role=\"button\">" + bundle.getString("common.add") + " " + bundle.getString("common.feedback") + "</a>");
                                    out.print("</td>");
                                }
                            }

                        } else{
                            if (curUser.isAdmin()) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\"\" href=\"/user/" + user.getId() + "/edit\" role=\"button\">" + bundle.getString("common.edit") + " " + bundle.getString("common.user") + "</a>");
                                    out.print("<a class=\"btn btn-outline-primary\"\" href=\"/user/" + user.getId() + "/delete\" role=\"button\">" + bundle.getString("common.delete") + " " + bundle.getString("common.user") + "</a>");
                                    out.print("</td>");
                                }
                        }
                        out.println("</tr>");
                        i++;

                    }
                    break;
            }
        %>
        </tbody>
    </table>
</div>
<div class="pag">
    <nav aria-label="Page navigation example">
        <ul class="pagination">
            <%
                if (curPageNumber > 1) {
                    out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"/course?offset=" + (offset - Common.limit) + "\">" + bundle.getString("list.previous") + "</a></li>");
                }
            %>
            <li class="page-item"><a class="page-link"
                                     href="#"><% out.print(curPageNumber + " " + bundle.getString("common.of") + " " + countPages); %></a>
            </li>
            <%
                if (curPageNumber < countPages) {
                    out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"/course?offset=" + (offset + Common.limit) + "\">" + bundle.getString("list.next") + "</a></li>");
                }
            %>
        </ul>
    </nav>
</div>
</body>
</html>