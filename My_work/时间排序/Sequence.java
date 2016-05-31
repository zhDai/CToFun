import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Sequence {
//	TimeInterval interval;
	int start;
	int end;
	int interval;
	int number;
	
	public List<Integer> TSequence(List<TimeInterval> a){
		
		Iterator<TimeInterval> b = a.iterator();
		int i = 0;
//		int len = a.size();
		List<Integer> c = new LinkedList<Integer>();
		while(b.hasNext()){
			if (i == 0){
				TimeInterval q = b.next();
				start = q.start;
				end = q.end;
				interval = q.interval;
				number = (end - start)/interval;
				c.add(start);
				for(int j = 0; j < number; j++){
					c.add(start+(j+1)*interval);
				}
			}
			else{
				TimeInterval q = b.next();
				start = q.start;
				end = q.end;
				interval = q.interval;
				number = (end - start)/interval;
				Iterator<Integer> bb = c.iterator();
				int k = 0;//判断在哪个位置开始后插入,初始是在第一个位置
				while(bb.hasNext()){
					int value = bb.next();
					if(value<start){
						k=k+1;
					}
					if(value>=start && value<=end){
						bb.remove();
					}
					if(value>end){
						break;
					}
				}
				//c插入，从尾部开始插入
				c.add(k,end);
				for(int j = 0; j < number; j++){
					c.add(k,end-(j+1)*interval);
				}
			}
			i++;
		}		
		return c;

	}
	public static void main(String[] args) {
		List<TimeInterval> a = new LinkedList<TimeInterval>();
		TimeInterval a1 = new TimeInterval();
		a1.start = 1;
		a1.end = 27;
		a1.interval = 1;
		a.add(a1);
		TimeInterval a2 = new TimeInterval();
		a2.start = 2;
		a2.end = 12;
		a2.interval = 2;
		a.add(a2);
		TimeInterval a3 = new TimeInterval();
		a3.start = 4;
		a3.end = 16;
		a3.interval = 3;
		a.add(a3);
		TimeInterval a4 = new TimeInterval();
		a4.start = 10;
		a4.end = 12;
		a4.interval = 1;
		a.add(a4);
		Sequence t = new Sequence();
		List<Integer> c = t.TSequence(a);
		System.out.println(c);
	}
}
