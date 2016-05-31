package cn.ct.em.draw.movie;

public class Interval {
	private long start;
	private long end;
	private String id;
	private long originalStart;
	
	Interval(long s, long e, String taskId) {
		start = s;
		end = e;
		id = taskId;
		originalStart = start;
	}
	
	Interval(long s, long e, String taskId, long os) {
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
