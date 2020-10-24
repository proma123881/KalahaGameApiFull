<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
		<link href='http://fonts.googleapis.com/css?family=Lekton' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Mouse+Memoirs' rel='stylesheet' type='text/css'>
		<link href="css/kalaha.css" rel="stylesheet">
    	<title>Kalaha</title>
</head>
<body>

	<h1 class="title">Welcome to Kalaha Game!!</h1>

	<div class="container">
    <br/>
    <br/>
	<br/>
	<br/>
	<form action="/start" method="get">
		<button type="submit" class="myButton lg" value="Submit">START GAME</button>
	</form>
</div>
</body>
</html>