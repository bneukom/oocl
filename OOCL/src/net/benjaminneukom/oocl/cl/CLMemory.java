package net.benjaminneukom.oocl.cl;

import java.io.Closeable;
import java.io.IOException;

import static org.jocl.CL.*;

import org.jocl.Pointer;
import org.jocl.cl_mem;

public class CLMemory implements Closeable {
	private cl_mem memory;
	private Pointer pointer;
	private long size;
	private Object data;

	public CLMemory(cl_mem memory, long size, Pointer pointer, Object data) {
		super();
		this.memory = memory;
		this.size = size;
		this.pointer = pointer;
		this.data = data;
	}

	/* default */Pointer getPointer() {
		return pointer;
	}

	public long getSize() {
		return size;
	}

	public cl_mem getMemory() {
		return memory;
	}
	
	public Object getData() {
		return data;
	}

	@Override
	public void close() throws IOException {
		clReleaseMemObject(memory);
	}
}
