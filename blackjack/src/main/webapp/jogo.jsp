<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.JogoBlackjack" %>
<%@ page import="model.Carta" %>
<%@ page import="model.Jogador" %>
<%@ page import="model.GerenciadorJogos" %>

<%
    String codigoSala = (String) session.getAttribute("codigoSala");
    Integer meuPlayerIndex = (Integer) session.getAttribute("playerIndex");

    if (codigoSala == null || meuPlayerIndex == null) {
        response.sendRedirect("Index.html");
        return;
    }

    JogoBlackjack jogo = GerenciadorJogos.getInstance().getJogo(codigoSala);

    if (jogo == null) {
        response.sendRedirect("Index.html?erro=sala_inexistente");
        return;
    }

    boolean minhaVez = (jogo.isPartidaIniciada() && !jogo.isJogoFinalizado() && jogo.getJogadorAtualIndex() == meuPlayerIndex);
    int totalJogadores = jogo.getJogadores().size();

    /*
        Mapeamento dos assentos:
        - O jogador local sempre fica no assento central, índice 2.
        - Os outros jogadores são distribuídos alternando direita e esquerda.
        - seatPlayerIndex[i] representa o índice real do jogador no assento visual i.
        - Os assentos vão de 0 a 4:
          0 = extremo esquerdo
          1 = esquerda
          2 = centro
          3 = direita
          4 = extremo direito
    */

    int[] seatPlayerIndex = new int[]{-1, -1, -1, -1, -1};

    seatPlayerIndex[2] = meuPlayerIndex;

    int[] leftSlots = {1, 0};
    int[] rightSlots = {3, 4};

    int leftPtr = 0;
    int rightPtr = 0;

    for (int step = 1; step < totalJogadores; step++) {
        int idx = (meuPlayerIndex + step) % totalJogadores;

        if (step % 2 == 1 && rightPtr < 2) {
            seatPlayerIndex[rightSlots[rightPtr]] = idx;
            rightPtr++;
        } else if (leftPtr < 2) {
            seatPlayerIndex[leftSlots[leftPtr]] = idx;
            leftPtr++;
        } else if (rightPtr < 2) {
            seatPlayerIndex[rightSlots[rightPtr]] = idx;
            rightPtr++;
        }
    }

    String[] seatClasses = {"seat-1", "seat-2", "seat-3", "seat-4", "seat-5"};
%>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blackjack 21 - Sala <%= codigoSala %></title>

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/cards.css">
</head>

<body>

    <div class="mesa-wrapper">

        <!-- Borda de madeira da mesa -->
        <div class="mesa-rail"></div>

        <main class="container mesa-jogo">

            <div class="sala-tag">
                SALA: <%= codigoSala %>
            </div>

            <h1>Blackjack - 21</h1>

            <!-- ÁREA DO DEALER -->
            <div class="dealer-secao">

                <h2>Dealer</h2>

                <% if (jogo.isPartidaIniciada()) { %>
                    <div class="mao-cartas dealer-mao">
                        <% for (Carta carta : jogo.getDealer().getMao()) { %>
                            <img 
                                class="carta card"
                                src="cards/<%= carta.getCodigoImagem() %>.svg"
                                alt="<%= carta %>"
                            >
                        <% } %>
                    </div>

                    <p class="pontuacao">
                        <strong>Pontuação: </strong>
                        <%= jogo.getDealer().calcularPontuacao() %>
                    </p>
                <% } else { %>
                    <p class="pontuacao">Aguardando início do jogo...</p>
                <% } %>

            </div>

            <!-- TEXTO DECORATIVO NO CENTRO DA MESA -->
            <div class="mesa-texto-centro">
                <span class="mesa-texto-titulo">BlackJack</span>
            </div>

            <!-- ÁREA DOS JOGADORES -->
            <div class="jogadores-arc-container" data-total="<%= totalJogadores %>">

                <%
                    for (int seat = 0; seat < 5; seat++) {
                        int pIdx = seatPlayerIndex[seat];

                        if (pIdx == -1) {
                            continue;
                        }

                        Jogador jogador = jogo.getJogadores().get(pIdx);

                        boolean isAtivo = (!jogo.isJogoFinalizado() && pIdx == jogo.getJogadorAtualIndex());
                        boolean isSelf = (pIdx == meuPlayerIndex);

                        String classeJogador = "jogador-box " + seatClasses[seat];

                        if (isAtivo) {
                            classeJogador += " ativo";
                        }

                        if (isSelf) {
                            classeJogador += " self-box";
                        }
                %>

                    <div class="<%= classeJogador %>">

                        <% if (isSelf) { %>
                            <div class="badge-voce">Você</div>
                        <% } %>

                        <% if (jogo.isPartidaIniciada()) { %>
                            <div class="mao-cartas">
                                <% for (Carta carta : jogador.getMao()) { %>
                                    <img 
                                        class="carta card"
                                        src="cards/<%= carta.getCodigoImagem() %>.svg"
                                        alt="<%= carta %>"
                                    >
                                <% } %>
                            </div>
                        <% } else { %>
                            <div class="mao-cartas" style="display: flex; align-items: center; justify-content: center; min-height: 82px;">
                                <span style="font-size: 0.8rem; color: var(--text-muted); opacity: 0.6;">Conectado</span>
                            </div>
                        <% } %>

                        <h3>
                            <%= jogador.getNome() %>
                        </h3>

                        <% if (jogo.isPartidaIniciada()) { %>
                            <p class="pontuacao">
                                <strong><%= jogador.calcularPontuacao() %></strong> pts
                            </p>
                        <% } %>

                        <% if (jogo.isJogoFinalizado()) { %>
                            <div class="resultado-box">
                                <span class="resultado-label">
                                    <%= jogo.getResultadoJogador(pIdx) %>
                                </span>
                            </div>
                        <% } %>

                    </div>

                <%
                    }
                %>

            </div>

            <!-- ÁREA DE AÇÕES -->
            <div class="acoes-jogo">

                <% if (!jogo.isPartidaIniciada()) { %>
                    <!-- ESTADO LOBBY -->
                    <% if (meuPlayerIndex == 0) { %>
                        <% if (totalJogadores >= 2) { %>
                            <p class="vez-label">Todos os jogadores entraram?</p>
                            <div class="botoes-acao">
                                <a href="comecar?acao=iniciar" class="botao">Começar Jogo</a>
                            </div>
                        <% } else { %>
                            <p class="aguardando-label">Aguardando outros jogadores entrarem...</p>
                            <div class="loading-spinner"></div>
                        <% } %>
                    <% } else { %>
                        <p class="aguardando-label">Aguardando o host iniciar a partida...</p>
                        <div class="loading-spinner"></div>
                    <% } %>

                <% } else if (!jogo.isJogoFinalizado()) { %>

                    <% if (minhaVez) { %>

                        <p class="vez-label">Sua vez de jogar!</p>

                        <div class="botoes-acao">

                            <form action="comprar" method="post" style="display:inline-block">
                                <button type="submit" class="btn-comprar">
                                    Comprar Carta
                                </button>
                            </form>

                            <form action="parar" method="post" style="display:inline-block">
                                <button type="submit" class="btn-parar">
                                    Parar
                                </button>
                            </form>

                        </div>

                    <% } else { %>

                        <p class="aguardando-label">
                            Aguardando:
                            <strong>
                                <%= jogo.getJogadores().get(jogo.getJogadorAtualIndex()).getNome() %>
                            </strong>
                        </p>

                        <div class="loading-spinner"></div>

                    <% } %>

                <% } else { %>

                    <div class="resultado-final">

                        <h2 class="resultado-titulo">
                            <%= jogo.getMensagemResultado() %>
                        </h2>

                        <% if (meuPlayerIndex == 0) { %>
                            <a href="comecar?acao=reiniciar" class="botao">
                                Jogar Novamente
                            </a>
                        <% } else { %>
                            <p class="aguardando-label" style="margin-bottom: 15px;">
                                Aguardando o host iniciar nova partida...
                            </p>
                        <% } %>

                        <a href="Index.html" class="botao btn-sair">
                            Sair da Sala
                        </a>

                    </div>

                <% } %>

            </div>

        </main>

    </div>

    <%
        boolean precisaReload = false;
        if (!jogo.isPartidaIniciada()) {
            precisaReload = true;
        } else if (!jogo.isJogoFinalizado() && !minhaVez) {
            precisaReload = true;
        } else if (jogo.isJogoFinalizado() && meuPlayerIndex > 0) {
            precisaReload = true;
        }
        if (precisaReload) {
    %>
        <script>
            setTimeout(function () {
                location.reload();
            }, 2000);
        </script>
    <% } %>

</body>

</html>