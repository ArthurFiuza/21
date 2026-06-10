package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GerenciadorJogos;

@WebServlet("/entrar")
public class EntrarSalaServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String codigoSala = request.getParameter("codigoSala");
		if (codigoSala != null) {
			codigoSala = codigoSala.trim().toUpperCase();
		}

		if (codigoSala == null || codigoSala.isEmpty()) {
			response.sendRedirect("Index.html?erro=codigo_invalido");
			return;
		}

		HttpSession session = request.getSession();
		String sessionId = session.getId();

		int playerIndex = GerenciadorJogos.getInstance().adicionarJogador(codigoSala, sessionId);

		if (playerIndex == -1) {
			response.sendRedirect("Index.html?erro=sala_inexistente");
		} else if (playerIndex == -2) {
			response.sendRedirect("Index.html?erro=sala_cheia");
		} else {
			session.setAttribute("codigoSala", codigoSala);
			session.setAttribute("playerIndex", playerIndex);
			response.sendRedirect("jogo.jsp");
		}
	}
}
