# Object Oriented OpenCL (OOCL) Java Wrapper #

Simple JOCL based object oriented OpenCL wrapper.

## Example Usage ##

Creating an OpenCL 2.0 based device for an N-body simulation.

```java
final float[] bodiesX = ...
final float[] bodiesY = ...
final float[] bodiesy = ...

final CLDevide device = CLPlatform.getFirst().getDevice(DeviceType.GPU, d -> d.getDeviceVersion() >= 2.0f).orElseThrow(() -> new IllegalStateException());

final CLContext context = device.createContext();
final CLCommandQueue commandQueue = context.createCommandQueue();

final CLMemory<float[]> bodiesXBuffer = context.createBuffer(CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, bodiesX);
final CLMemory<float[]> bodiesYBuffer = context.createBuffer(CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, bodiesY);
final CLMemory<float[]> bodiesZBuffer = context.createBuffer(CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, bodiesZ);

final CLKernel integrateKernel = context.createKernel(new File("kernels/nbody/integrate.cl"), "integrate", BuildOptions.EMPTY);

commandQueue.execute(integrateKernel, 1, globalWorkSize, localWorkSize);
commandQueue.finish();

```

## Future Plans ##

Not all features supported by JOCL have been adapted yet.

