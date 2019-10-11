<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Weather forecast Application</h1>
  <hr>

  <div class="form">
    <form action="weatherDetails" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Enter Zip-Code(USA)</td>
          <td><input id="zipCode" name="zipCode"></td>
          <td><input type="submit" value="Submit"></td>
        </tr>
      </table>
    </form>
  </div>

</body>
</html>