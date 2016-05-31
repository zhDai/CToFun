package cn.ct.em.draw.time;

public class TimeInterval {
	private long start;
	private long end;
	private String id;
	private long originalStart;
	
	TimeInterval(long s, long e, String taskId) {
		start = s;
		end = e;
		id = taskId;
		originalStart = start;
	}
	
	TimeInterval(long s, long e, String taskId, long os) {
		start = s;
		end = e;
		id = taskId;
		originalStart = os;
	}
	
	public long getStart() {
		return start;
	}
	
	public long getEnd() {
		return end;
	}
	
	public String getId() {
		return id;
	}
	
	public long getOriginalStart() {
		return originalStart;
	}
	
}
