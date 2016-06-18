import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Thilina on 6/8/2016.
 *
 * Read excel files and insert data to Database
 */
class ReadFile {

    private static DataBase database;
    private static Sheet sheet;
    private static Workbook workBook;
    private static boolean otherStatement = false;
    private static boolean gptw = false;
    private static int grandMeanRow;
    private static  int startDemoColumn;

    private final static Logger logger = Logger.getLogger(ReadFile.class);

    static void init(){

        try {
            database = new DataBase();
        } catch (DataBaseException e) {
            logger.error("Database create fail",e);
        }
    }

    static void readStatements(String filename, String companyName, String sheetName){

        try {

            workBook = new XSSFWorkbook(new FileInputStream(filename));

            sheet = workBook.getSheet(sheetName);

            // find the relavent location to start get data
            int numberOfCols = 0;
            int numberOfRows;
            boolean colFound = false;
            int numberOfAverages = 0;


            for (numberOfRows = 0; numberOfRows<sheet.getLastRowNum() && !colFound; numberOfRows++) {

                Row row = sheet.getRow(numberOfRows);
                numberOfCols = 0;

                for (Cell cell : row) {

                    String cellvalue = cell.toString();

                    if (cellvalue.equals(companyName)) {
                        colFound = true;
                        break;
                    }
                    numberOfCols++;
                }
            } // end find location

            //go 3 columns right to get the relevant column of demography
            startDemoColumn = numberOfCols +3;

            //read number of respondents
            Row row1 = sheet.getRow(numberOfRows);

            Cell cel1 =row1.getCell(numberOfCols);
            if(cel1.toString().equalsIgnoreCase(FileReadConstant.NUMBER_OF_RESPONDENTS)){
                float val = Float.parseFloat(row1.getCell(numberOfCols+1).toString());
                database.setNumberOfRespondents((int)val);
            }

            int tempRowNum = numberOfRows+1;
            // +1 is added to go down one row else existing row contains the number of respondents
            //can add method here to set the number of respondents
            while (tempRowNum<sheet.getLastRowNum()){

                row1 = sheet.getRow(tempRowNum);

                cel1 =row1.getCell(numberOfCols);

                if (cel1 != null) {

                    String val = cel1.toString();
                    val = val.trim();

                    if(val.equalsIgnoreCase(FileReadConstant.GRANDMEAN)){
                        grandMeanRow = tempRowNum;
                        float cVal = Float.parseFloat(row1.getCell(numberOfCols+1).toString());
                        float bVal = Float.parseFloat(row1.getCell(numberOfCols+2).toString());
                        database.insertAverage(cVal,bVal,DataBaseConstant.GRAND_MEAN_INDEX);
                        break;
                    }
                    else if(val.equalsIgnoreCase(FileReadConstant.AVERAGE)) {
                        //set array for averages
                        if(numberOfAverages < FileReadConstant.MAX_NUM_OF_AVERAGES){
                            float cVal = Float.parseFloat(row1.getCell(numberOfCols+1).toString());
                            float bVal = Float.parseFloat(row1.getCell(numberOfCols+2).toString());
                            database.insertAverage(cVal,bVal,numberOfAverages);
                            numberOfAverages++;
                            if (numberOfAverages == FileReadConstant.MAX_NUM_OF_AVERAGES){
                                otherStatement = true;
                                tempRowNum++;
                            }
                        }
                    }
                    //read other statements
                    else if(otherStatement){
                        if(row1.getCell(numberOfCols+1) !=null && !row1.getCell(numberOfCols+1).toString().equalsIgnoreCase("")){
                            float cVal = Float.parseFloat(row1.getCell(numberOfCols+1).toString());
                            float bVal = Float.parseFloat(row1.getCell(numberOfCols+2).toString());
                            database.insertOtherStatement(val,cVal,bVal);
                            database.insertCoreStatement(val,cVal,bVal);
                        }
                        else{
                            otherStatement = false;
                            gptw = true;
                        }
                    }

                    //read great place to work statements
                    else if(gptw){
                        if(row1.getCell(numberOfCols+1) !=null && !row1.getCell(numberOfCols+1).toString().equalsIgnoreCase("")){
                            float cVal = Float.parseFloat(row1.getCell(numberOfCols+1).toString());
                            float bVal = Float.parseFloat(row1.getCell(numberOfCols+2).toString());
                            database.insertGPTWStatement(val,cVal,bVal);
                            database.insertCoreStatement(val,cVal,bVal);
                        }
                        else{
                            gptw = false;
                        }
                    }
                    else {
                        float cVal = Float.parseFloat(row1.getCell(numberOfCols+1).toString());
                        float bVal = Float.parseFloat(row1.getCell(numberOfCols+2).toString());
                        database.insertCoreStatement(val,cVal,bVal);
                    }

                }

                tempRowNum++;
            }

            workBook.close();
            logger.info("Statement read successful");
        } catch (IOException e) {
            logger.error("Read Statements error due to file ",e);
        } catch (DataBaseException e) {
            logger.error("Read Statements error due to database ",e);
        }

    }

    static void readCOCOR(String filename, String sheetName){

        try {

            workBook = new XSSFWorkbook(new FileInputStream(filename));

            sheet = workBook.getSheet(sheetName);

            // find the relavent location to start get data
            int startCol = 0;
            int startRow = 0;
            int stdevCol = 0;
            boolean colFound = false;

            for (int numberOfRows = 0; numberOfRows<sheet.getLastRowNum() && !colFound; numberOfRows++) {

                Row row = sheet.getRow(numberOfRows);
                if (row != null) {
                    int numberOfCols = 0;

                    for (Cell cell : row) {

                        String cellvalue = cell.toString();

                        if (cellvalue.equalsIgnoreCase(FileReadConstant.STATEMENT)) {
                            startRow = numberOfRows + 1;
                            startCol = numberOfCols;
                        } else if (cellvalue.equalsIgnoreCase(FileReadConstant.STDEV)) {
                            stdevCol = numberOfCols;
                            colFound = true;
                        }
                        numberOfCols++;
                    }
                } // end find locations
            }

            if(!colFound){
                logger.error("Given company name not found");
                throw new DataBaseException("Company name not found");
            }

            for (int numberOfRows = startRow; numberOfRows<sheet.getLastRowNum(); numberOfRows++) {

                Row row = sheet.getRow(numberOfRows);

                String statement = row.getCell(startCol).toString();
                float cocor = Float.parseFloat(row.getCell(startCol+1).toString());
                float stdev = Float.parseFloat(row.getCell(stdevCol).toString());
                database.insetIntoCOCTable(statement,cocor,stdev);
            }

            workBook.close();
            logger.info("COCOR read successful");
        } catch (IOException e) {
            logger.error("Read coco or fail due to IO error ",e);
        } catch (DataBaseException e) {
            logger.error("Read coco or fail due to Database error ",e);
        }



    }

    static void readThemes(String filename, String sheetName){

        try {

            workBook = new XSSFWorkbook(new FileInputStream(filename));
            sheet = workBook.getSheet(sheetName);
            Row row;

            for (int numOfRows = FileReadConstant.THEME_SHEET_START_ROW; numOfRows<sheet.getLastRowNum(); numOfRows++) {

                row = sheet.getRow(numOfRows);
                if(row == null){
                    break;
                }
                String statement = row.getCell(FileReadConstant.THEME_SHEET_STATEMENT_COLUMN).toString();
                String theme = row.getCell(FileReadConstant.THEME_SHEET_THEME_COLUMN).toString();
                if(statement.equalsIgnoreCase("") || theme.equalsIgnoreCase("")){
                    break;
                }
                database.insertIntoThemeTable(statement,theme);

            }

            workBook.close();
            logger.info("Theme read successful");
        } catch (IOException e) {
            logger.error("Read theme or fail due to IO error ",e);
        } catch (DataBaseException e) {
            logger.error("Read theme or fail due to Database error ",e);
        }
    }

    /**
     *
     * @author Niroshan
     *
     * Read demography details from worksheet
     *
     * @param filename file name
     * @param sheetName  sheet name of the file
     * */
    static void readDemoFactors(String filename, String sheetName)
    {

        try {

            workBook = new XSSFWorkbook(new FileInputStream(filename));

            sheet = workBook.getSheet(sheetName);

            // find the relavent location to start get data
            String demoName = "";
            String demoCritaria;
            String demoScore;
            float demoValue;
            Row demoNameRow = sheet.getRow(0);
            Row demoCriteriaRow = sheet.getRow(1);
            Row demoScoreRow = sheet.getRow(grandMeanRow);
            boolean isFinish = false;
            int id = 0;

            for(int col = startDemoColumn; !isFinish; col++)
            {
                if(!(demoNameRow.getCell(col)==null) && !demoNameRow.getCell(col).toString().equalsIgnoreCase(""))
                {
                    demoName = demoNameRow.getCell(col).toString();
                }
                demoCritaria = demoCriteriaRow.getCell(col).toString();
                demoScore = demoScoreRow.getCell(col).toString();
                if(!demoScore.equalsIgnoreCase("-")){
                    demoValue = Float.parseFloat(demoScore);
                    database.insertDemography(id++, demoName, demoCritaria, demoValue);
                }
                if((demoNameRow.getCell(col+1)==null || demoNameRow.getCell(col+1).toString().equalsIgnoreCase(""))
                        && (demoCriteriaRow.getCell(col+1)==null
                        || demoCriteriaRow.getCell(col+1).toString().equalsIgnoreCase("")))
                {
                    isFinish = true;
                }
            }
            workBook.close();
            logger.info("Demography read successful");
        } catch (IOException e) {
            logger.error("Read Demography or fail due to IO error ",e);
        } catch (DataBaseException e) {
            logger.error("Read Demography or fail due to Database error ",e);
        }
    }

    static void createSortData(){
        database.sortTable();
    }

    static void closeall(){
        database.closeConnection();

    }

    static DataBase getDataBase(){
        return database;
    }

}
