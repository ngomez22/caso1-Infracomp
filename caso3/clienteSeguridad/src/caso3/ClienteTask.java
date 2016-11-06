package caso3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;

import caso2.Cliente;
import uniandes.gload.core.Task;

public class ClienteTask extends Task {
	
	private BufferedWriter f;
	
	public ClienteTask(BufferedWriter f) {
		this.f = f;
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
		Cliente cliente = new Cliente(f);
		try {
			cliente.run();
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
