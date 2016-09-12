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
			wait();
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	public synchronized void leer() {
		valor++;
		notify();
	}
}
