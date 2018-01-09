<%--
  Created by IntelliJ IDEA.
  User: Dian
  Date: 1/9/2018
  Time: 1:36 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Books Shop Online</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>


<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Shopping Cart Demo</div>

<div class="demo-container">
    <h3>Demo content</h3>

    <ul>
        <li>Buy online</li>
        <li>Admin pages</li>
        <li>Reports</li>
    </ul>
</div>


<jsp:include page="_footer.jsp" />

</body>
</html>