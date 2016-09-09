package servidor;

import caso1.Buffer;

public class Servidor {
	
	private Buffer buffer;
	
	private int nThreads;

	public Servidor(Buffer buffer, int nThreads) {
		super();
		this.buffer = buffer;
		this.nThreads = nThreads;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}

	public int getnThreads() {
		return nThreads;
	}

	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}
	
}
