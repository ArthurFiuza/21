package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.JogoBlackjack;

@WebServlet("/parar")
public class PararServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		JogoBlackjack jogo = (JogoBlackjack) session.getAttribute("jogo");

		if (jogo != null) {
			jogo.jogadorPara();
		}

		response.sendRedirect("jogo.jsp");
	}
}