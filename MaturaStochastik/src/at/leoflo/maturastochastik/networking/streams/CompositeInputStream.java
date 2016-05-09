package at.leoflo.maturastochastik.networking.streams;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class CompositeInputStream extends DataInputStream {
	private final Charset encoding;
	
	public CompositeInputStream(InputStream input, Charset encoding) {
		super(input);
		
		this.encoding = encoding;
	}
	
	public String readString() throws IOException {
		return readString(encoding);
	}
	
	public String readString(Charset encoding) throws IOException {
		int length = super.readInt();
		byte[] data = new byte[length];
		
		super.read(data);
		
		return new String(data, encoding);
	}
}
