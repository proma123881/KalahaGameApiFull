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
<c:choose>
				<c:when test="${message != null}">
					${message}
				</c:when>
	</c:choose>
                </br>
		<c:choose>
			<c:when test="${gameId != null}">
                <c:choose>
                	<c:when test="${(winner == 'Second Player' or player == 'Second Player') and (winner != 'Draw') }">
                		Player 2
                	</c:when>
                	<c:otherwise>
						<c:choose>
							<c:when test="${(winner == 'First Player' or player == 'First Player') and (winner != 'Draw') }">
								Player 1
							</c:when>
							<c:otherwise>
								Draw
							</c:otherwise>
						</c:choose>
                	</c:otherwise>
				</c:choose>
                 <c:choose>
                	<c:when test="${winner == 'Second Player' or winner == 'First Player' or winner == 'Draw'}">
						is winner!
                	</c:when>
                	<c:otherwise>
						is active!
                	</c:otherwise>
                </c:choose>
			</c:when>
			<c:otherwise>
				<form action="/start" method="get">
					<button type="submit" class="myButton lg" value="Submit">START GAME</button>
				</form>
			</c:otherwise>
		</c:choose>

            </div>

				<c:choose>
					<c:when test="${gameId != null}">
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
									<c:when test="${player == 'Second Player'}">
										<td rowspan="2">
												${pits['13']}
										</td>
										<c:forEach var="i" begin="0" end="5" step="1">
											<c:set var="pit" value="${pits[12-i]}" />
											<td>
												<a href="/play?pitId=${12-i+1}&gameId=${gameId}">${pits[12-i]}</a>
											</td>
										</c:forEach>
										<td rowspan="2">
												${pits['6']}
										</td>
									</c:when>
									<c:otherwise>
										<td rowspan="2">
												${pits['13']}
										</td>
										<c:forEach var="i" begin="0" end="5" step="1">
											<c:set var="pit" value="${pits[12-i]}" />
											<td>
													${pits[12-i]}
											</td>
										</c:forEach>
										<td rowspan="2">
												${pits['6']}
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
							<tr>
								<c:choose>
									<c:when test="${player == 'First Player'}">

										<c:forEach var="i" begin="0" end="5" step="1">
											<c:set var="pit" value="${pits[i]}" />
											<td>
												<a href="/play?pitId=${i+1}&gameId=${gameId}">${pits[i]}</a>
											</td>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="i" begin="0" end="5" step="1">
											<c:set var="pit" value="${pits[i]}" />
											<td>
													${pits[i]}
											</td>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tr>
						</table>
					</c:when>
				</c:choose>
            <br/>
            <br/>
        </div>
	</body>
</html>
