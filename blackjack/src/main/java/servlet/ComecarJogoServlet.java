package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GerenciadorJogos;
import model.JogoBlackjack;

@WebServlet("/comecar")
public class ComecarJogoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String codigoSala = (String) session.getAttribute("codigoSala");
		Integer playerIndex = (Integer) session.getAttribute("playerIndex");

		if (codigoSala == null || playerIndex == null) {
			response.sendRedirect("Index.html");
			return;
		}

		JogoBlackjack jogo = GerenciadorJogos.getInstance().getJogo(codigoSala);
		if (jogo != null) {
			String acao = request.getParameter("acao");
			// Only player 0 (the host/creator) can start or restart the game
			if (playerIndex == 0) {
				if ("reiniciar".equals(acao)) {
					jogo.reiniciarJogo();
				} else {
					jogo.iniciarPartida();
				}
			}
		}

		response.sendRedirect("jogo.jsp");
	}
}
