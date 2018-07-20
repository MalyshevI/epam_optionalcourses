<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"--%>
         <%--pageEncoding="ISO-8859-1" session="false"%>--%>
<%@ page import="com.epam.lab.optional_courses.controller.Common" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.epam.lab.optional_courses.entity.Course" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.epam.lab.optional_courses.entity.User" %>
<%@ page import="com.epam.lab.optional_courses.entity.Feedback" %>
<%@ page import="java.time.LocalDate" %>


<div class="content">
    <%
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", (Locale) request.getAttribute("locale"));

    %>
    <table class="table">
        <thead>
        <tr>
                <%
                Common.EntityType entityType = (Common.EntityType) request.getAttribute("entityType");
                Long offset = (Long) request.getAttribute("offset");
                User curUser = (User) request.getAttribute("curUser");
                long countEntity = (long) request.getAttribute("countEntity");
                long countPages = countEntity / Common.limit;
                countPages += countEntity%Common.limit == 0 ? 0 : 1;
                long curPageNumber = offset / Common.limit + 1;


                int i;
                switch (entityType) {
                    case COURSE:
                        User pageUser = (User) request.getAttribute("pageUser");
                        List<Course> allCourses = (List<Course>) request.getAttribute("list");
                        List<Boolean> coursesEnrolledByCurUser = null;
                        List<Feedback> feedbackForUserCourses = null;
                        List<Long> countUsersOnCourseList = (List<Long>) request.getAttribute("countUsersOnCourseList");
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
                        out.println("<td width=\"40\"> <a href=\"/user/" + course.getTutor().getId() + "\"> " + course.getTutor().getFirstName() + " " +course.getTutor().getLastName() + " </a></td>");
                        LocalDate curDate = LocalDate.now();
                        String status;
                        if (curDate.isBefore(course.getStartDate())) {
                            if(countUsersOnCourseList.get(i)<course.getCapacity()){
                                status = bundle.getString("course.registrationOpen");
                            }else{
                                status = bundle.getString("course.overcrowded");
                            }
                        } else if (curDate.isBefore(course.getFinishDate())) {
                            if(countUsersOnCourseList.get(i)<course.getCapacity()){
                                status = bundle.getString("course.courseGoing");
                            }else{
                                status = bundle.getString("course.overcrowded");
                            }
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
                            }else{
                                out.println("<td width=\"40\"> </td>");
                            }
                        }
                        // BUTTONS
                        if (curUser.isAdmin()) {
                            out.print("<td width=\"40\">");
                            out.print("<a class=\"btn btn-outline-primary\" href=\"/course/" + course.getId() + "/edit\" role=\"button\">" + bundle.getString("common.edit") + " " + bundle.getString("common.course") + "</a>");
                            out.print("<form action=\"/course/"+ course.getId() + "/delete\" method=\"POST\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.delete") + " " + bundle.getString("common.course") + "</button>\n" +
                                        "</form>");
                            out.print("</td>");
                        }
                        if (!curUser.equals(course.getTutor())) {
                            if(!status.equals(bundle.getString("course.courseFinished"))){
                                if (coursesEnrolledByCurUser.get(i)) {
                                    out.print("<td width=\"40\">");
                                    out.print("<form action=\"/course/"+ course.getId() + "/delete\" method=\"POST\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.leave") + " "+ bundle.getString("common.from") + " " + bundle.getString("common.course") + "</button>\n" +
                                        "<input name=\"userId\" type=\"hidden\" value=\""+ curUser.getId() +"\">" +
                                        "</form>");
                                    out.print("</td>");
                                } else {
                                    if(countUsersOnCourseList.get(i)<course.getCapacity()){
                                        System.out.println(curUser + "ALLO, YOBA");
                                        out.print("<td width=\"40\">");
                                         out.print("<form action=\"/course/"+ course.getId() + "/apply\" method=\"POST\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.apply") + " " + bundle.getString("common.course") + "</button>\n" +
                                        "<input name=\"userId\" type=\"hidden\" value=\""+ curUser.getId() +"\">" +
                                        "</form>");
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
                        System.out.println(pageCourse);
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
                        out.println("<th width=\"40\" scope=\"col\">" + "Buttons" +"</th>");
                        out.println("</tr>\n" +
                                "        </thead>\n" +
                                "        <tbody>");
                    i = 0;
                    for (User user: allUsers) {

                        //TABLE RECORDS
                        out.println("<tr>");
                        out.println("<td width=\"40\">" + (offset + i + 1) + "</td>");
                        out.println("<td width=\"40\"> <a href=\"/user/" + user.getId() + "\"> " + user.getFirstName()  + " " + user.getLastName() + " </a></td>");
                        out.println("<td width=\"40\"> <a href=\"mailto:" + user.getEmail() + "\"> " + user.getEmail() + " </a></td>");

                        //BUTTONS
                        if(pageCourse != null){
                            Feedback feedback = feedbackForUsersOnCourse.get(i);
                            if(feedback != null){
                                out.println("<td width=\"40\">" + feedback.getGrade() + "</td>");
                                if (curUser.equals(pageCourse.getTutor()) || curUser.isAdmin()) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\" href=\"/course/" + pageCourse.getId() + "/editFeedback/"+ user.getId() +"\" role=\"button\">" + bundle.getString("common.edit") + " " + bundle.getString("common.feedback") + "</a>");
                                    out.print("<form action=\"/course/"+ pageCourse.getId() + "/deleteFeedback\" method=\"POST\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.delete") + " " +bundle.getString("common.student") + " " + bundle.getString("common.feedback") + "</button>\n" +
                                        "<input name=\"userId\" type=\"hidden\" value=\""+ user.getId() +"\">" +
                                        "</form>");
                                    out.print("</td>");
                                }
                            }else{
                                if (curUser.equals(pageCourse.getTutor())) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\" href=\"/course/" + pageCourse.getId() + "/createFeedback/"+ user.getId() +"\" role=\"button\">" + bundle.getString("common.add") + " " + bundle.getString("common.feedback") + "</a>");
                                    out.print("</td>");
                                }
                            }
                            if(curUser.isAdmin()){
                                 out.print("<td width=\"40\">");
                                out.print("<form action=\"/course/"+ pageCourse.getId() + "/delete\" method=\"POST\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.delete") + " " + bundle.getString("common.student") + " "+ bundle.getString("common.from") + " " + bundle.getString("common.course") + "</button>\n" +
                                        "<input name=\"userId\" type=\"hidden\" value=\""+ user.getId() +"\">" +
                                        "</form>");
                                out.print("</td>");
                            }

                        } else{
                            if (curUser.isAdmin()) {
                                    out.print("<td width=\"40\">");
                                    out.print("<a class=\"btn btn-outline-primary\" href=\"/user/edit?userId="+ user.getId() +"\" role=\"button\">" + bundle.getString("common.edit") + " " + bundle.getString("common.user") + "</a>");
                                    out.print("<form action=\"/user/"+ user.getId() + "/delete\" method=\"POST\">\n" +
                                        "\t<button type=\"submit\" class=\"btn btn-outline-primary\">" + bundle.getString("common.delete") + " " + bundle.getString("common.user") + "</button>\n" +
                                        "</form>");
                                    out.print("</td>");
                                }
                        }
                        out.println("</tr>");
                        i++;

                    }
                    break;
            }
        %>
    </table>
</div>


<%
    if (countPages > 1) {
        out.println("<div class=\"pag\">\n" +
                "    <nav aria-label=\"Page navigation example\">\n" +
                "        <ul class=\"pagination\">");
    switch (entityType) {
        case COURSE:
            if (countPages > 1) {
                out.println("<div class=\"pag\">\n" +
                        "    <nav aria-label=\"Page navigation example\">\n" +
                        "        <ul class=\"pagination\">");

                if (curPageNumber > 1) {
                    out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"?offset=" + (offset - Common.limit) + "\">" + bundle.getString("list.previous") + "</a></li>");
                }
                out.println("<li class=\"page-item\"><a class=\"page-link\"\n" +
                        "                                     href=\"#\">");
                out.print(curPageNumber + " " + bundle.getString("common.of") + " " + countPages + "\n" +
                        "             </a>\n" +
                        "            </li>");

                if (curPageNumber < countPages) {
                    out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"?offset=" + (offset + Common.limit) + "\">" + bundle.getString("list.next") + "</a></li>");
                }

                out.println("       </ul>\n" +
                        "    </nav>\n" +
                        "</div>");
            }
            break;
        case USER:
            if (countPages > 1) {
                out.println("<div class=\"pag\">\n" +
                        "    <nav aria-label=\"Page navigation example\">\n" +
                        "        <ul class=\"pagination\">");

                if (curPageNumber > 1) {
                    out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"/user/all?offset=" + (offset - Common.limit) + "\">" + bundle.getString("list.previous") + "</a></li>");
                }
                out.println("<li class=\"page-item\"><a class=\"page-link\"\n" +
                        "                                     href=\"#\">");
                out.print(curPageNumber + " " + bundle.getString("common.of") + " " + countPages + "\n" +
                        "             </a>\n" +
                        "            </li>");

                if (curPageNumber < countPages) {
                    out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"/user/all?offset=" + (offset + Common.limit) + "\">" + bundle.getString("list.next") + "</a></li>");
                }

                out.println("       </ul>\n" +
                        "    </nav>\n" +
                        "</div>");
            }
            break;
    }
%>