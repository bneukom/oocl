package net.benjaminneukom.oocl.cl;

import static org.jocl.CL.*;

import java.io.Closeable;
import java.io.IOException;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_kernel;

public class CLKernel implements Closeable {
	private cl_kernel kernel;
	private String kernelName;

	public CLKernel(cl_kernel kernel, String kernelName) {
		this.kernel = kernel;
		this.kernelName = kernelName;
	}

	/**
	 * Creates a {@link CLKernel} from the given {@link CLProgram} with the given kernel name.
	 * 
	 * @param program
	 * @param kernelName
	 * @return
	 */
	public static CLKernel createKernel(CLProgram program, String kernelName) {
		cl_kernel kernel = clCreateKernel(program.getId(), kernelName, null);

		return new CLKernel(kernel, kernelName);
	}

	/**
	 * Sets the arguments of this kernel.
	 * 
	 * @param memory
	 */
	public void setArguments(CLMemory... memory) {
		for (int memoryIndex = 0; memoryIndex < memory.length; ++memoryIndex) {
			clSetKernelArg(kernel, memoryIndex, Sizeof.cl_mem, Pointer.to(memory[memoryIndex].getMemory()));
		}
	}

	/**
	 * Returns the internal kernel id.
	 * 
	 * @return
	 */
	public cl_kernel getKernel() {
		return kernel;
	}

	/**
	 * Returns the name of the kernel.
	 * 
	 * @return
	 */
	public String getKernelName() {
		return kernelName;
	}

	@Override
	public void close() throws IOException {
		clReleaseKernel(kernel);
	}
}
