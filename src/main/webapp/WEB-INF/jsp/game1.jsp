<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Kalaha</title>

        <link href='http://fonts.googleapis.com/css?family=Lekton' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Mouse+Memoirs' rel='stylesheet' type='text/css'>
        <link href="css/kalaha.css" rel="stylesheet">
	</head>
	<body>
        <h1 class="title">KALAHA</h1>
        <div class="container">
            <div class="message">
                ${message}
                </br>
                <c:choose>
                	<c:when test="${game.player.name == 'Second Player' or  game.winner == 'Second Player'}">
                		Player 2
                	</c:when>
                	<c:otherwise>
                		Player 1
                	</c:otherwise>
                </c:choose>
                 <c:choose>
                	<c:when test="${game.player.name == 'Second Player' or game.player.name == 'First Player'}">
                		is active!
                	</c:when>
                	<c:otherwise>
                		is winner!
                	</c:otherwise>
                </c:choose>
            </div>
            <table class="gameBoard" border="1">
                <thead>
                    <td>KALAHA<br/>Player 2</td>
                    <td>PIT</td>
                    <td>PIT</td>
                    <td>PIT</td>
                    <td>PIT</td>
                    <td>PIT</td>
                    <td>PIT</td>
                    <td>KALAHA<br/>Player 1</td>
                </thead>
                <tr>
                    <c:choose>
                        <c:when test="${game.player.name == 'Second Player'}">
                            <td rowspan="2">
                                ${game.player.pit.getIndex(13).numberOfStones}
                            </td>
                            <c:forEach var="i" begin="7" end="12" step="1">
    							<c:set var="pit" value="${game.player.pit.getIndex(i).numberOfStones}" />
								<td>
                                    <a href="/play?pitId=${i+1}">${pit.numberOfStones}</a>
                                </td>
							</c:forEach>
                             <td rowspan="2">
                                ${game.player.pit.getIndex(6).numberOfStones}
                            </td>
                        </c:when>
                        <c:otherwise>
                             <td rowspan="2">
                                ${game.player.pit.getIndex(13).numberOfStones}
                            </td>
                            <c:forEach var="i" begin="7" end="12" step="1">
    							<c:set var="pit" value="${game.player.pit.getIndex(i).numberOfStones}" />
								<td>
                                    ${game.player.pit.getIndex(i).numberOfStones}
                                </td>
							</c:forEach>
							 <td rowspan="2">
                                ${game.player.pit.getIndex(6).numberOfStones}
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <c:choose>
                        <c:when test="${game.player.name == 'First Player'}">
                            
                            <c:forEach var="i" begin="0" end="5" step="1">
    							<c:set var="pit" value="${game.player.pit.getIndex(i).numberOfStones}" />
								<td>
                                    <a href="/play?pitId=${i}">${game.player.pit.getIndex(i).numberOfStones}</a>
                                </td>
							</c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="i" begin="0" end="5" step="1">
    							<c:set var="pit" value="${game.player.pit.getIndex(i).numberOfStones}" />
								<td>
                                    ${game.player.pit.getIndex(i).numberOfStones}
                                </td>
							</c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </table>
            <br/>
            <br/>
    		<form action="/start" method="get">
				<button type="submit" class="myButton" value="Submit">RESET GAME</button>
			</form>
        </div>
	</body>
</html>
