
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

public class Reporter {

	private static HSSFWorkbook myWorkBook = new HSSFWorkbook();
	private static HSSFSheet mySheet = myWorkBook.createSheet();

	CreationHelper createHelper;
	CellStyle hlink_style;
	Font hlink_font;
	
	
	
	
	public Reporter() {
		// hyperlinks
		createHelper = myWorkBook.getCreationHelper();
		hlink_style = myWorkBook.createCellStyle();
		hlink_font = myWorkBook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);

		
		
	}

	private void excelLog(int row, int col, String value) {
		HSSFRow myRow = mySheet.getRow(row);

		if (myRow == null)
			myRow = mySheet.createRow(row);

		HSSFCell myCell = myRow.createCell(col);

		if (value != null && value.startsWith("http:")) {
			Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
			link.setAddress(value);
			myCell.setHyperlink((org.apache.poi.ss.usermodel.Hyperlink) link);
			myCell.setCellStyle(hlink_style);
			myCell.setCellValue(value);
			
		} else {
			myCell.setCellValue(value);
		}
	}

	public void writeToExcel(ArrayList<String[]> results, String destinationPath)
			throws IOException {
		File dest = new File(destinationPath);

		@SuppressWarnings("unused")
		int numCol = 10; // assume 10 cols -- need to adjust for dynamic largest
							// column width

		for (int i = 0; i < results.size(); i++) {
			String[] record = results.get(i);
			for (int j = 0; j < record.length; j++) {
				excelLog(i, j, record[j]);
			}

		}

		try {
			FileOutputStream out = new FileOutputStream(dest);
			myWorkBook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			if (e.getMessage()
					.contains(
							"The process cannot access the file because it is being used by another process")) {
				System.out
						.println("The process cannot access the file because it is being used by another process");
				System.out
						.println("Attempting to rename the file and try again");

				String path = FilenameUtils.getFullPath(dest.getAbsolutePath());
				String baseFileName = FilenameUtils.getBaseName(
						dest.getAbsolutePath()).concat("_copy");
				String fileExtension = FilenameUtils.getExtension(dest
						.getAbsolutePath());

				System.out.println("Path: " + path + "\n" + "baseFileName: "
						+ baseFileName + "\n" + "fileExtension: "
						+ fileExtension + "\n");

				dest = new File(path, baseFileName.concat("."
						.concat(fileExtension)));
				// dest.renameTo(new
				// File(path,baseFileName.concat(fileExtension)));

				System.out.println(dest.getAbsolutePath());

				try {
					FileOutputStream out2 = new FileOutputStream(dest);
					myWorkBook.write(out2);
					out2.close();
					System.out
							.println("Second attempt to write the file was successful");
				} catch (Exception e2) {
					System.out.println("Second attempt failed");
					System.out
							.println("There was an error when writing the excel file.");
					System.out.println("Excel file was not written.");
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Excel File not exported");
		}

	}

}
