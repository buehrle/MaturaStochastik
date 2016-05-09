package at.leoflo.maturastochastik.networking.streams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class CompositeOutputStream extends DataOutputStream {
	private final Charset encoding;

	public CompositeOutputStream(OutputStream output, Charset encoding) {
		super(output);
		
		this.encoding = encoding;
	}

	public void writeString(String s, Charset encoding) throws IOException {
		byte[] data = s.getBytes(encoding);
		
		super.writeInt(data.length);
		super.write(data);
	}
	
	public void writeString(String s) throws IOException {
		writeString(s, encoding);
	}
}
