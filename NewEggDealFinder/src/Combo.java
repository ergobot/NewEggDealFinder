
public class Combo implements Comparable{

	private int id;
	private String itemText;
	private int price;
	private String itemLink;
	
	
	public int Id(){return id;}
	public void Id(int id){this.id = id;}
	public String ItemText(){return itemText;}
	public void ItemText(String itemText){this.itemText = itemText;}
	public int Price(){return price;}
	public void Price(int price){this.price = price;}
	public String ItemLink(){return itemLink;}
	public void ItemLink(String itemLink){this.itemLink = itemLink;}
	
	public Combo(int id,
				 String itemText,
				 int price,
				 String itemLink){
		this.id = id;
		this.itemText = itemText;
		this.price = price;
		this.itemLink = itemLink;
	}

	@Override
	public int compareTo(Object o) {
		Combo combo = (Combo)o;
		return (price - combo.price);
	}
	
	
}
