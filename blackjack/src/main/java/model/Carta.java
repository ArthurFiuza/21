package model;

public class Carta {
	private String naipe;
	private String valor;

	public Carta(String valor, String naipe) {
		this.valor = valor;
		this.naipe = naipe;
	}

	public String getNaipe() {
		return naipe;
	}

	public String getValor() {
		return valor;
	}

	public int getPontuacao() {
		if (valor.equals("A")) {
			return 11;
		}

		if (valor.equals("J") || valor.equals("Q") || valor.equals("K")) {
			return 10;
		}

		return Integer.parseInt(valor);
	}

	public String getCodigoImagem() {
		return valor + naipe;
	}

	@Override
	public String toString() {
		return valor + " de " + naipe;
	}
}