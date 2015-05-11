package net.benjaminneukom.oocl.cl;

import static org.jocl.CL.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.benjaminneukom.oocl.cl.CLProgram.BuildOption;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_mem;
import org.jocl.cl_program;

public class CLContext implements Closeable {
	private final cl_context context;
	private final CLDevice device;

	public CLContext(final cl_context context, final CLDevice device) {
		this.context = context;
		this.device = device;
	}

	public CLCommandQueue createCommandQueue() {
		@SuppressWarnings("deprecation")
		final cl_command_queue commandQueue = clCreateCommandQueue(context, device.getId(), 0, null);
		return new CLCommandQueue(commandQueue);
	}

	/**
	 * Creates a {@link CLProgram} from the given Files.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public CLProgram createProgram(final File... file) throws IOException {
		final String[] programms = Arrays.stream(file).map(f -> {
			try {
				return Files.readAllLines(f.toPath()).stream().reduce("", (accu, l) -> accu + l + System.lineSeparator());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).toArray(String[]::new);

		return createProgram(programms);
	}

	/**
	 * Returns the kernel from the given program file. This method automatically loads and builds the program and returns the kernel.
	 * 
	 * @param file
	 * @param kernel
	 * @param options
	 * @return
	 */
	public CLKernel createKernel(File file, String kernel, BuildOption... options) {
		try {
			final CLProgram program = createProgram(file);
			program.build(options);
			return program.createKernel(kernel);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a program with the given sources.
	 * 
	 * @param sources
	 * @return
	 */
	public CLProgram createProgram(final String... sources) {

		final cl_program program = clCreateProgramWithSource(context, 1, sources, null, null);

		return new CLProgram(program);
	}

	public CLMemory<Void> createFromGLBuffer(long flags, int vbo) {
		final cl_mem mem = clCreateFromGLBuffer(context, CL_MEM_WRITE_ONLY, vbo, null);
		return new CLMemory<Void>(mem, -1, Pointer.to(mem), null);
	}

	/**
	 * Creates a buffer memory object from the given int array.
	 * 
	 * @param flags
	 * @param data
	 * @return
	 */
	public CLMemory<Void> createEmptyBuffer(final long flags, int size) {
		final cl_mem mem = clCreateBuffer(context, flags, size, null, null);
		return new CLMemory<Void>(mem, Sizeof.cl_mem, Pointer.to(mem), null);
	}

	/**
	 * Creates a buffer memory object from the given vbo.
	 * 
	 * @param flags
	 * @param data
	 * @return
	 */
	public CLMemory<int[]> createBufferFromGLBuffer(final long flags, int vbo) {
		return null;
	}

	/**
	 * Creates a buffer memory object from the given int array.
	 * 
	 * @param flags
	 * @param data
	 * @return
	 */
	public CLMemory<int[]> createBuffer(final long flags, final int[] data) {
		final Pointer pointer = Pointer.to(data);
		final cl_mem mem = clCreateBuffer(context, flags, Sizeof.cl_int * data.length, pointer, null);
		return new CLMemory<int[]>(mem, Sizeof.cl_int * data.length, pointer, data);
	}

	/**
	 * Creates a buffer memory object from the given long array.
	 * 
	 * @param flags
	 * @param data
	 * @return
	 */
	public CLMemory<long[]> createBuffer(final long flags, final long[] data) {
		final Pointer pointer = Pointer.to(data);
		final cl_mem mem = clCreateBuffer(context, flags, Sizeof.cl_ulong * data.length, pointer, null);
		return new CLMemory<long[]>(mem, Sizeof.cl_ulong * data.length, pointer, data);
	}

	/**
	 * Creates a buffer memory object from the given float array.
	 * 
	 * @param flags
	 * @param data
	 * @return
	 */
	public CLMemory<float[]> createBuffer(long flags, float[] data) {
		final Pointer pointer = Pointer.to(data);
		final cl_mem mem = clCreateBuffer(context, flags, Sizeof.cl_float * data.length, pointer, null);
		return new CLMemory<float[]>(mem, Sizeof.cl_float * data.length, pointer, data);

	}

	/**
	 * Returns the internal id.
	 * 
	 * @return
	 */
	public cl_context getContext() {
		return context;

	}

	@Override
	public void close() throws IOException {
		clReleaseContext(context);
	}

}
