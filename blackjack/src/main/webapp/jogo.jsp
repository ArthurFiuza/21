<%@ page import="model.JogoBlackjack" %>
<%@ page import="model.Carta" %>

<%
    JogoBlackjack jogo = (JogoBlackjack) session.getAttribute("jogo");

    if (jogo == null) {
        response.sendRedirect("index.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Blackjack 21</title>
    <link rel="stylesheet" href="css/style.css">
	<link rel="stylesheet" href="css/cards.css">
</head>
<body>
    <main class="container">
        <h1>Blackjack - 21</h1>

        <section class="area-jogo">
            <h2>Suas cartas</h2>

           <div class="mao-cartas">
    			<% for (Carta carta : jogo.getJogador().getMao()) { %>
        			<img class="carta" src="cards/<%= carta.getCodigoImagem() %>.svg" alt="<%= carta %>">
    			<% } %>
			</div>

            <p><strong>Sua pontuação:</strong> <%= jogo.getJogador().calcularPontuacao() %></p>

            <h2>Cartas do Dealer</h2>

            <div class="mao-cartas">
    			<% for (Carta carta : jogo.getDealer().getMao()) { %>
        			<img class="carta" src="cards/<%= carta.getCodigoImagem() %>.svg" alt="<%= carta %>">
   				<% } %>
			</div>

            <p><strong>Pontuação do Dealer:</strong> <%= jogo.getDealer().calcularPontuacao() %></p>

            <% if (!jogo.isJogoFinalizado()) { %>
                <form action="comprar" method="post">
                    <button type="submit">Comprar Carta</button>
                </form>

                <form action="parar" method="post">
                    <button type="submit">Parar</button>
                </form>
            <% } else { %>
                <h2><%= jogo.getMensagemResultado() %></h2>
                <a href="iniciar" class="botao">Jogar Novamente</a>
            <% } %>
        </section>
    </main>
</body>
</html>