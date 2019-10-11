<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Weather Forecast </title>
</head>
    
<body>
 <h1>Coolest Temperature on ${date}</h1>
  <hr>
	<h2>Time: ${dateTime}</h2>
  	<h2>Temperature: ${temperature} F</h2>
  	<h2>Description: ${wx_desc} </h2>
  	<%-- <c:forEach items="${listOfTimeframe}" var="element"> 
  	<tr>
    <td>${element.time}</td>
    <td>${element.temp_f}</td>  
    <td>${element.wx_desc}</td> 
    </tr>
	</c:forEach> --%>
	
	
	<!-- <span class="totalMin">90</span> Minutes
	<span class="convertedHour">0</span> Hours
	<span class="convertedMin">0</span> Minutes -->
	
	
	<table>
	<c:forEach items="${listOfTimeframe}" var="timeframe">
    <tr>
      
       <td>Time: <c:out value="${timeframe.time}" />  
	
	</td>
        <td>Temperature: <c:out value="${timeframe.temp_f}"/></td>
        <td>Description: <c:out value="${timeframe.wx_desc}"/></td>  
    </tr>
	</c:forEach>
</table>
</body>
</html>