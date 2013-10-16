import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
//import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Driver {

	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
	
		String searchTerm = "i3";
		
		String url = new String("http://www.newegg.com/Product/ProductList.aspx?Submit=ENE&N=30000007%20600090099&IsNodeId=1&name=Entry%20Level%20%2f%20Mainstream");
		ElementFinder finder = new ElementFinder();
		
		
		ArrayList<Combo> combos = new ArrayList<Combo>();
		WebDriver browser = new FirefoxDriver();
		// leading the loop
		System.out.println("Getting the first page");
		browser.get(url);
		boolean hasNextPage = true;
		int pageNumber = 1;
		int comboItemId = 0;
		while(hasNextPage){
		System.out.println("Page #" + pageNumber + "-Getting the comboItems");
		List<WebElement> comboItemElements = finder.getElementsByClass("comboCell", browser);//browser.findElements(By.className("comboCell"));
		
		// Total items
		System.out.println("Page #" + pageNumber + "-Total combos items: " + comboItemElements.size());
		for(int i = 0; i < comboItemElements.size(); i++)
		{
			String itemText = comboItemElements.get(i).findElement(By.className("itemDescription")).getText();
			int itemPrice = Integer.parseInt(comboItemElements.get(i).findElement(By.tagName("strong")).getText().replace(",", ""));
			String itemLink = comboItemElements.get(i).findElement(By.className("itemDescription")).getAttribute("href");
			
			Combo combo = new Combo(comboItemId++,itemText,itemPrice,itemLink);
			
//			System.out.println("Id: \t\t" + combo.Id() + "\n" +
//							   "ItemText: \t" + combo.ItemText() + "\n" + 
//							   "ItemPrice: \t" + combo.Price() + "\n" + 
//							   "ItemLink: \t" + combo.ItemLink() + "\n\n");
			System.out.println("Page #" + pageNumber + "-Added comboItem #" + (comboItemId));
			combos.add(combo);
		}
		
		System.out.println("Page #" + pageNumber + "-Getting the next page button");
		
		
		// Check for next page and get it
		try{
		WebElement nextButton = browser.findElement(By.className("next"));
		System.out.println("Page #" + pageNumber + "-Clicking the next page button");
		if(nextButton.isDisplayed()){
		nextButton.click();
		pageNumber++;
		}
		else
		{
			hasNextPage = false;
			System.out.println("\n\n*****************************");
			System.out.println("*Total Pages: "+pageNumber+"              *");
			System.out.println("*There are no more pages... *");
			System.out.println("*****************************\n\n");
		}
		}
		catch(NoSuchElementException e)
		{
			hasNextPage = false;
			System.out.println("\n\n*****************************");
			System.out.println("*Total Pages: "+pageNumber+"              *");
			System.out.println("*There are no more pages... *");
			System.out.println("*****************************\n\n");
		}
		}
		browser.close();
		
		System.out.println("\n***************");
		System.out.println("*Combos Sorted*");
		System.out.println("***************\n");
		Collections.sort(combos);
		
		ArrayList<String[]> output = new ArrayList<String[]>();
		String[] columnHeaders = {"Id","Description","Price","Link"};
		output.add(columnHeaders);
		for(Combo combo : combos)
		{
			if(combo.ItemText().contains(searchTerm)){
			String[] record = new String[4];
			record[0] = String.valueOf(combo.Id());
			record[1] = combo.ItemText();
			record[2] = String.valueOf(combo.Price());
			record[3] = combo.ItemLink();
			output.add(record);
			System.out.println("Id: \t\t" + combo.Id() + "\n" +
					   "ItemText: \t" + combo.ItemText() + "\n" + 
					   "ItemPrice: \t" + combo.Price() + "\n" + 
					   "ItemLink: \t" + combo.ItemLink() + "\n\n");
			}
		}
		System.out.println("Exporting the processed output to excel format...");
//		File file = new File("C:\\Output\\Reports\\report1.xls");
//		if(file.exists()){
//		    System.out.println("File Exists");
//		}else{
//		    boolean wasDirecotoryMade = file.mkdirs();
//		    if(wasDirecotoryMade)System.out.println("Direcoty Created");
//		    else System.out.println("Sorry could not create directory");
//		}
		
		String fileDirectory = "C:\\Output\\Reports\\";
		String fileName = "newEggReport.xls";
		Reporter reporter = new Reporter();
		System.out.println("File location: " + fileDirectory);
		reporter.writeToExcel(output, fileDirectory + fileName);
		System.out.println("Completed");
		
		if(Desktop.isDesktopSupported()){Desktop.getDesktop().open(new File(fileDirectory));}
		
		
	}

}
