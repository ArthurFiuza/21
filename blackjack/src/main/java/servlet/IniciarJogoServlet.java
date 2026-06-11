package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GerenciadorJogos;

@WebServlet("/iniciar")
public class IniciarJogoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String sessionId = session.getId();

		// Create room and add the creator
		String codigoSala = GerenciadorJogos.getInstance().criarJogo();
		int playerIndex = GerenciadorJogos.getInstance().adicionarJogador(codigoSala, sessionId);

		session.setAttribute("codigoSala", codigoSala);
		session.setAttribute("playerIndex", playerIndex);

		response.sendRedirect("jogo.jsp");
	}
}