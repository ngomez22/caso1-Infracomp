package caso1;

public class Mensaje {
	
	private int valor;
	
	public Mensaje (int valor) {
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
	
	public synchronized void esperar() {
		try {
			System.out.println("Esperando a ser respondido");
			wait();
			System.out.println("Me despertaron y me voy");
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	public synchronized void leer() {
		valor++;
		System.out.println("Avisando que cambie valor " + valor);
		notify();
	}
}
