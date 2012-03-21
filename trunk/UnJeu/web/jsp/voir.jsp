<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="beans.JoueurDB"%>
<%@page import="beans.JoueurBean"%>
<%
    JoueurDB jdb = new JoueurDB();
    JoueurBean joueur = jdb.getById(request.getParameter("id"));
%>
<c:set var="joueur" value="${jvoir}"/>
<h3>Profil</h3>
<% if(JoueurBean.estAdmin((Integer)session.getAttribute("groupe"))) {%>
<fieldset class="adminField">
    <legend>Admin</legend>
    je changerai le css apr�s manger :>
</fieldset>
<% } %>
<table>
    <tr><td class="label">Pseudo :</td><td><%= joueur.getPseudo() %></td></tr>
    <tr><td class="label">Pv :</td><td><%= joueur.getPvMax() %></td></tr>
    <tr><td class="label">Nombre de combats :</td><td><%= joueur.getTotalCombats() %></td></tr>
    <tr><td class="label">Nombre de monstres tu�s :</td><td><%= joueur.getTotalMonstres() %></td></tr>
</table>