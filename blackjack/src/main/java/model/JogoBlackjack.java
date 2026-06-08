package model;

public class JogoBlackjack {
	private Baralho baralho;
	private Jogador jogador;
	private Jogador dealer;
	private boolean jogoFinalizado;
	private String mensagemResultado;

	public JogoBlackjack() {
		baralho = new Baralho();
		jogador = new Jogador("Jogador");
		dealer = new Jogador("Dealer");
		jogoFinalizado = false;
		mensagemResultado = "";

		iniciarPartida();
	}

	private void iniciarPartida() {
		jogador.receberCarta(baralho.comprarCarta());
		jogador.receberCarta(baralho.comprarCarta());

		dealer.receberCarta(baralho.comprarCarta());
		dealer.receberCarta(baralho.comprarCarta());
	}

	public void jogadorCompraCarta() {
		if (!jogoFinalizado) {
			jogador.receberCarta(baralho.comprarCarta());

			if (jogador.calcularPontuacao() > 21) {
				jogoFinalizado = true;
				mensagemResultado = "Você passou de 21! Dealer venceu.";
			}
		}
	}

	public void jogadorPara() {
		while (dealer.calcularPontuacao() < 17) {
			dealer.receberCarta(baralho.comprarCarta());
		}

		int pontosJogador = jogador.calcularPontuacao();
		int pontosDealer = dealer.calcularPontuacao();

		if (dealer.calcularPontuacao() > 21) {
			mensagemResultado = "Dealer passou de 21! Você venceu!";
		} else if (pontosJogador > pontosDealer) {
			mensagemResultado = "Você venceu!";
		} else if (pontosJogador < pontosDealer) {
			mensagemResultado = "Dealer venceu!";
		} else {
			mensagemResultado = "Empate!";
		}

		jogoFinalizado = true;
	}

	public Jogador getJogador() {
		return jogador;
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