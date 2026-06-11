package model;

import java.util.ArrayList;

public class JogoBlackjack {
	private Baralho baralho;
	private ArrayList<Jogador> jogadores;
	private Jogador dealer;
	private int jogadorAtualIndex;
	private boolean jogoFinalizado;
	private boolean partidaIniciada;
	private String mensagemResultado;

	public JogoBlackjack() {
		baralho = new Baralho();
		jogadores = new ArrayList<>();
		dealer = new Jogador("Dealer");
		jogadorAtualIndex = 0;
		jogoFinalizado = false;
		partidaIniciada = false;
		mensagemResultado = "";
	}

	public JogoBlackjack(int numJogadores) {
		this();
		for (int i = 1; i <= numJogadores; i++) {
			jogadores.add(new Jogador("Jogador " + i));
		}
		iniciarPartida();
	}

	public synchronized void adicionarJogador(Jogador jogador) {
		if (!partidaIniciada) {
			jogadores.add(jogador);
		}
	}

	public synchronized void iniciarPartida() {
		if (!partidaIniciada && !jogadores.isEmpty()) {
			baralho = new Baralho();
			dealer = new Jogador("Dealer");
			for (Jogador jogador : jogadores) {
				jogador.getMao().clear();
			}

			for (Jogador jogador : jogadores) {
				jogador.receberCarta(baralho.comprarCarta());
				jogador.receberCarta(baralho.comprarCarta());
			}

			dealer.receberCarta(baralho.comprarCarta());
			dealer.receberCarta(baralho.comprarCarta());

			jogadorAtualIndex = 0;
			jogoFinalizado = false;
			partidaIniciada = true;
			mensagemResultado = "";
		}
	}

	public synchronized void reiniciarJogo() {
		partidaIniciada = false;
		jogoFinalizado = false;
		jogadorAtualIndex = 0;
		mensagemResultado = "";
		dealer = new Jogador("Dealer");
		for (Jogador jogador : jogadores) {
			jogador.getMao().clear();
		}
	}

	public boolean isPartidaIniciada() {
		return partidaIniciada;
	}

	public void jogadorCompraCarta() {
		if (partidaIniciada && !jogoFinalizado) {
			Jogador jogadorAtual = jogadores.get(jogadorAtualIndex);
			jogadorAtual.receberCarta(baralho.comprarCarta());

			if (jogadorAtual.calcularPontuacao() > 21) {
				avancarTurno();
			}
		}
	}

	public void jogadorPara() {
		if (partidaIniciada && !jogoFinalizado) {
			avancarTurno();
		}
	}

	private void avancarTurno() {
		jogadorAtualIndex++;
		if (jogadorAtualIndex >= jogadores.size()) {
			finalizarJogo();
		}
	}

	private void finalizarJogo() {
		boolean algumJogadorAtivo = false;
		for (Jogador j : jogadores) {
			if (j.calcularPontuacao() <= 21) {
				algumJogadorAtivo = true;
				break;
			}
		}

		if (algumJogadorAtivo) {
			while (dealer.calcularPontuacao() < 17) {
				dealer.receberCarta(baralho.comprarCarta());
			}
		}

		int pontosDealer = dealer.calcularPontuacao();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < jogadores.size(); i++) {
			Jogador j = jogadores.get(i);
			int pontosJogador = j.calcularPontuacao();
			sb.append(j.getNome()).append(": ");

			if (pontosJogador > 21) {
				sb.append("Estourou");
			} else if (pontosDealer > 21) {
				sb.append("Venceu (Dealer Estourou)");
			} else if (pontosJogador > pontosDealer) {
				sb.append("Venceu");
			} else if (pontosJogador < pontosDealer) {
				sb.append("Perdeu");
			} else {
				sb.append("Empate");
			}

			if (i < jogadores.size() - 1) {
				sb.append(" | ");
			}
		}

		mensagemResultado = sb.toString();
		jogoFinalizado = true;
	}

	public String getResultadoJogador(int index) {
		if (index < 0 || index >= jogadores.size()) {
			return "";
		}
		Jogador j = jogadores.get(index);
		int pontosJogador = j.calcularPontuacao();
		int pontosDealer = dealer.calcularPontuacao();

		if (pontosJogador > 21) {
			return "Estourou! (Perdeu)";
		}
		if (jogoFinalizado) {
			if (pontosDealer > 21) {
				return "Venceu!";
			} else if (pontosJogador > pontosDealer) {
				return "Venceu!";
			} else if (pontosJogador < pontosDealer) {
				return "Perdeu!";
			} else {
				return "Empate!";
			}
		}
		return "";
	}

	public Jogador getJogador() {
		if (jogadores != null && !jogadores.isEmpty()) {
			return jogadores.get(0);
		}
		return null;
	}

	public ArrayList<Jogador> getJogadores() {
		return jogadores;
	}

	public int getJogadorAtualIndex() {
		return jogadorAtualIndex;
	}

	public Jogador getDealer() {
		return dealer;
	}

	public boolean isJogoFinalizado() {
		return jogoFinalizado;
	}

	public String getMensagemResultado() {
		return mensagemResultado;
	}
}