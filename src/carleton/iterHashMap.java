package carleton;

import java.util.HashMap;
import java.util.List;
public class iterHashMap<K, V,I> extends HashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public iterHashMap() {
		super();
	}
	public boolean iterContainsValue(I i) {
		for(Object o: this.keySet()) {
			List<I> list = (List<I>)(this.get(o));
			if (!(list.contains(i))) continue;
			return true;
		}
		return false;
			
			
		}
	
	public int[] iterContainsValueAt(I i) {
		int [] siteAndClientIdx = new int[2];
		for(Object o: this.keySet()) {
			List<I> list = (List<I>)(this.get(o));
			if (!(list.contains(i))) continue;
			int index = list.indexOf(i);
			siteAndClientIdx [0]=(Integer)(o);
			siteAndClientIdx [1] =index;
			return siteAndClientIdx;
			
		}
		return null;
			
			
		}
}
