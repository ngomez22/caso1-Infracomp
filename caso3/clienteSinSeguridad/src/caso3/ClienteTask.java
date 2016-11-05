package caso3;

import java.io.PrintWriter;
import java.io.Writer;

import caso2.Cliente;
import uniandes.gload.core.Task;

public class ClienteTask extends Task {
	
	private Writer w;
	
	public ClienteTask(Writer w) {
		this.w = w;
	}

	@Override
	public void fail() {
		System.out.println(Task.MENSAJE_FAIL);
	}

	@Override
	public void success() {
		System.out.println(Task.OK_MESSAGE);
	}

	@Override
	public void execute() {
		Cliente cliente = new Cliente(w);
		try {
			cliente.run();
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
