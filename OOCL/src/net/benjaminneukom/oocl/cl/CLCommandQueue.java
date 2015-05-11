package net.benjaminneukom.oocl.cl;

import static org.jocl.CL.*;

import org.jocl.cl_command_queue;
import org.jocl.cl_mem;

public class CLCommandQueue {
	private cl_command_queue queue;

	public CLCommandQueue(cl_command_queue queue) {
		this.queue = queue;
	}

	public void execute(CLKernel kernel, int dimensions, long globalWorkSize, long localWorkSize) {
		clEnqueueNDRangeKernel(queue, kernel.getKernel(), dimensions, null, new long[] { globalWorkSize }, new long[] { localWorkSize }, 0, null, null);
	}

	public void readBuffer(CLMemory<?> memory) {
		clEnqueueReadBuffer(queue, memory.getMemory(), CL_TRUE, 0, memory.getSize(), memory.getPointer(), 0, null, null);
	}

	public void enqueAcquireGLObject(CLMemory<?> memory) {
		clEnqueueAcquireGLObjects(queue, 1, new cl_mem[] { memory.getMemory() }, 0, null, null);
	}
	
	public void enqueueReleaseGLObject(CLMemory<?> memory) {
		clEnqueueReleaseGLObjects(queue, 1, new cl_mem[] { memory.getMemory() }, 0, null, null);
	}

	public void flush() {
		clFlush(queue);
	}

	public void finish() {
		clFinish(queue);
	}
}
