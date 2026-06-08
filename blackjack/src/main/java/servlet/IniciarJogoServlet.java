package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.JogoBlackjack;

@WebServlet("/iniciar")
public class IniciarJogoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JogoBlackjack jogo = new JogoBlackjack();

		HttpSession session = request.getSession();
		session.setAttribute("jogo", jogo);

		response.sendRedirect("jogo.jsp");
	}
}