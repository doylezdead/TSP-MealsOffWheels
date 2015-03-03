import java.util.*;

public class genericStack<Item> implements genericStackInterface<Item> {
	
	public class element {
		private element next;
		private Item data;
		private element prev;
		
		public element() {}																					// A null constructor
		public element( Item value ) { data = value; }														// A constructor for the "next" element
		public element( Item value, element nex ) { data = value; next = nex; }								// A constructor for the "prev" element
		public element( element pre, Item value, element nex ) { prev = pre; data = value; next = nex; }	// A constructor for inserting elements
		
		public element getNext() { return next; }
		public element getPrev() { return prev; }
		public Item getData() { return data; }
		
		public void setNext( Item value ) { next = new element( this, value ); }
		public void setPrev( Item value ) { prev = new element( getPrev(),value , this ); }
		public void setData( Item value ) { data = value }
	}
	
	private element head;
	
	public Item push( Item cur ) {
		
	}
	
	public Item pop() {
		
	}
}
