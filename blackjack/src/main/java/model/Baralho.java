package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Baralho {
	private Stack<Carta> cartas;

	public Baralho() {
		cartas = new Stack<>();
		criarBaralho();
		embaralhar();
	}

	private void criarBaralho() {
		String[] naipes = { "H", "S", "D", "C" };
		String[] valores = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

		ArrayList<Carta> listaCartas = new ArrayList<>();

		for (String naipe : naipes) {
			for (String valor : valores) {
				listaCartas.add(new Carta(valor, naipe));
			}
		}

		Collections.shuffle(listaCartas);

		for (Carta carta : listaCartas) {
			cartas.push(carta);
		}
	}

	public Carta comprarCarta() {
		if (!cartas.empty()) {
			return cartas.pop();
		}

		return null;
	}

	public int quantidadeCartas() {
		return cartas.size();
	}

	private void embaralhar() {
		Collections.shuffle(cartas);
	}
}