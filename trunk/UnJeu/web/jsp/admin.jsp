<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>Administration</h3>

<c:if test="${not empty requestScope['erreur']}">
    <p class="errorMessage"><c:out value="${requestScope['erreur']}" /></p>
</c:if>
    
<fieldset>
    <legend>L�gende</legend>
    <form method="post" action="inscription.do">
        <table id="formulaire">
            
        </table>
    </form>
</fieldset>