<!DOCTYPE html>
<html>

<head>
    <title>Add Student</title>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <link rel="stylesheet" href="css/add-student-style.css" type="text/css">
</head>

<body>
<div id="wrapper">
    <div id="header">
        <h2>FooBar University</h2>
    </div>
</div>

<div id="container">
    <h3>Add Student</h3>
    <form action="StudentControllerServlet" method="GET">
        <input type="hidden" name="command" value="ADD">
        <table>
            <tbody>
            <tr>
                <td><label>First Name:</label></td>
                <td><input type="text" name="firstName" /></td>
            </tr>
            <tr>
                <td><label>Last Name:</label></td>
                <td><input type="text" name="lastName" /></td>
            </tr>
            <tr>
                <td><label>Email:</label></td>
                <td><input type="email" name="email" /></td>
            </tr>
            <tr>
                <td><label></label></td>
                <td><input type="submit" value="Save" class="save"/></td>
            </tr>
            </tbody>
        </table>
    </form>

    <div style="clear: both;">
        <p>
            <a href="index.jsp">Back to Homepage</a>
        </p>
    </div>
    
</div>

</body>

</html>
