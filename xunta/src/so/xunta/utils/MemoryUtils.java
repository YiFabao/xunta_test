package so.xunta.utils;

public class MemoryUtils {
	public static void showCurrentFreeMemory()
	{
		long d=1024*1024;
		long freeMem=Runtime.getRuntime().freeMemory();
		long maxMem=Runtime.getRuntime().maxMemory();
		long totalMemory=Runtime.getRuntime().totalMemory();
		System.out.println("freeMemory:"+freeMem/d+"M");
		System.out.println("maxMem:"+maxMem/d+"M");
		System.out.println("totalMem:"+totalMemory/d+"M");
	}
	public static void main(String args[])
	{
		showCurrentFreeMemory();
	}
}
