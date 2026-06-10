package model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorJogos {
	private static GerenciadorJogos instance;
	private ConcurrentHashMap<String, JogoBlackjack> jogos;
	private ConcurrentHashMap<String, List<String>> jogadoresPorSala;

	private GerenciadorJogos() {
		jogos = new ConcurrentHashMap<>();
		jogadoresPorSala = new ConcurrentHashMap<>();
	}

	public static synchronized GerenciadorJogos getInstance() {
		if (instance == null) {
			instance = new GerenciadorJogos();
		}
		return instance;
	}

	public String criarJogo() {
		String codigoSala = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
		JogoBlackjack jogo = new JogoBlackjack();
		jogos.put(codigoSala, jogo);
		jogadoresPorSala.put(codigoSala, new ArrayList<>());
		return codigoSala;
	}

	public JogoBlackjack getJogo(String codigoSala) {
		if (codigoSala == null) {
			return null;
		}
		return jogos.get(codigoSala);
	}

	public synchronized int adicionarJogador(String codigoSala, String sessionId) {
		List<String> sessoes = jogadoresPorSala.get(codigoSala);
		if (sessoes == null) {
			return -1;
		}

		int index = sessoes.indexOf(sessionId);
		if (index != -1) {
			return index;
		}

		JogoBlackjack jogo = jogos.get(codigoSala);
		if (jogo == null) {
			return -1;
		}

		if (jogo.isPartidaIniciada()) {
			return -2; // Room is already in progress (cannot join)
		}

		if (sessoes.size() < 5) {
			sessoes.add(sessionId);
			int playerNum = sessoes.size();
			jogo.adicionarJogador(new Jogador("Jogador " + playerNum));
			return sessoes.size() - 1;
		}

		return -2; // Full room
	}

	public synchronized int obterIndexJogador(String codigoSala, String sessionId) {
		List<String> sessoes = jogadoresPorSala.get(codigoSala);
		if (sessoes == null) {
			return -1;
		}
		return sessoes.indexOf(sessionId);
	}

	public void removerJogo(String codigoSala) {
		if (codigoSala != null) {
			jogos.remove(codigoSala);
			jogadoresPorSala.remove(codigoSala);
		}
	}
}
