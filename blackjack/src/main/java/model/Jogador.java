package model;

import java.util.ArrayList;

public class Jogador {
	private String nome;
	private ArrayList<Carta> mao;

	public Jogador(String nome) {
		this.nome = nome;
		this.mao = new ArrayList<>();
	}

	public void receberCarta(Carta carta) {
		mao.add(carta);
	}

	public int calcularPontuacao() {
		int total = 0;
		int ases = 0;

		for (Carta carta : mao) {
			total += carta.getPontuacao();

			if (carta.getValor().equals("A")) {
				ases++;
			}
		}

		while (total > 21 && ases > 0) {
			total -= 10;
			ases--;
		}

		return total;
	}

	public ArrayList<Carta> getMao() {
		return mao;
	}

	public String getNome() {
		return nome;
	}
}