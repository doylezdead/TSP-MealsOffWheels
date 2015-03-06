import java.util.*;

public interface genericStackInterface< Item > {
	public Item push();
	public Item pop();
	
	public Item get();
	public Item getNext();
	public Item getPrev();
	public Item getData();
	public Item getHead();
	public Item getTail();
	
	public void reverseOrder();
	public void erase();
	
}
