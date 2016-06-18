import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Thilina on 6/14/2016.
 *
 * Read data from data base
 * Provide data to other packages
 */
@SuppressWarnings("Duplicates")
public class DataStore {

    private static DataBase dataBase;
    private static int numberOfDemography = 0;

    /**
     * Call these to read and store data in to tables
     * */
    public void initFileRead(){
        ReadFile.init();
    }

    /**
     * Read statements from excel file
     *
     * @param fileName Excel file name
     * @param companyName name of the company as in excel sheet
     * @param workSheetName name of the work sheet that contains statements
     *
     * **/
    public void readSatements(String fileName, String companyName, String workSheetName){
        ReadFile.readStatements(fileName,companyName,workSheetName);
    }

    /**
     *
     * read COC_OR from excel
     *
     * @param fileName excel file that has coc or values
     * @param worksheet sheet name that has coc or values
     * */
    public void readCOCOR(String fileName, String worksheet){
        ReadFile.readCOCOR(fileName,worksheet);
    }

    /**
     *
     * read COC_OR from excel
     *
     * @param fileName excel file that has themes
     * @param worksheet sheet name that has themes
     * */
    public static void readThemes(String fileName, String worksheet){
        ReadFile.readThemes(fileName,worksheet);
    }

    /**
     *
     * read COC_OR from excel
     *
     * @param fileName excel file that has Demography data
     * @param worksheet sheet name that has Demography data
     * */
    public static void readDemography(String fileName, String worksheet){
        ReadFile.readDemoFactors(fileName,worksheet);
    }

    public static void InitDataStore(DataBase dataBaseOut){
        dataBase = dataBaseOut;
    }

    /**
     * Return number of respondents
     * */
    public static int getNumberOfRespondents(){
        return dataBase.getNumberOfRespondents();
    }

    /**
     * Return trust index (Company Grand Mean)
     * */
    public static float getTrustIndexCompany(){
        return dataBase.getCompanyGrandMean();
    }

    /**
     * Return Overriding statement company value
     * */
        public static float getOverridingStatementCompanyValue(){

            float orCompVal = 0;
            String getOvrValQuarry = "SELECT " +DataBaseConstant.COMPANY_COLUMN
                    + " FROM " +DataBaseConstant.GPTW_STATEMENTS+ ";";

            try {
                ResultSet rs = dataBase.runQuarry(getOvrValQuarry);
                orCompVal = rs.getFloat(DataBaseConstant.COMPANY_COLUMN);
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return orCompVal;
        }

    /**
     * Return trust index benchmark ( grand mean of bench mark )
     * */
    public static float getTrustIndexBenchMark(){
        return dataBase.getBenchMarkGrandMean();
    }

    /**
     * Return Overriding statement bench mark value
     * */
    public static float getOverridingStatementBenchMarkValue(){

        float orBenchVal = 0;
        String getOvrValQuarry = "SELECT " +DataBaseConstant.BENCHMARK_COLUMN
                + " FROM " +DataBaseConstant.GPTW_STATEMENTS+ ";";

        try {
            ResultSet rs = dataBase.runQuarry(getOvrValQuarry);
            orBenchVal = rs.getFloat(DataBaseConstant.BENCHMARK_COLUMN);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orBenchVal;
    }

    /**
     * Return all basic averages
     * */
    public static float[][] getBasicAverages(){
        return  dataBase.getAverages();
    }

    /**
     * Return company and bench mark value for a given statement
     * array index 0 - company 1 - benchmark
     * */
    public static float[] getValues(String statement){

        float values[] = new float[2];
        statement = statement.trim();
        String cover = "\'";

        if(statement.contains("\'")){
            cover = "\"";
        }

        String getStavalQua = "SELECT "+DataBaseConstant.TABLE_STATEMENT+","+ DataBaseConstant.BENCHMARK_COLUMN
                + " FROM "+DataBaseConstant.TABLE_STATEMENT
                + " WHERE "+DataBaseConstant.TABLE_STATEMENT
                + "."+DataBaseConstant.TABLE_STATEMENT+" = "+cover+statement+cover+";";

        try {
            ResultSet rs = dataBase.runQuarry(getStavalQua);

            values[DataBaseConstant.COMPANY_AVERAGE_INDEX] = rs.getFloat(DataBaseConstant.COMPANY_COLUMN);
            values[DataBaseConstant.BENCH_MARK_AVERAGE_INDEX] = rs.getFloat(DataBaseConstant.BENCHMARK_COLUMN);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
    }

    /**
     * Return all Demography Themes
     *
     * Return as a string array
     * */
    public static String[] getAllDemography(){

        String quarry = "SELECT DISTINCT "+DataBaseConstant.DEMOGRAPHY_COLUMN+" FROM "
                + DataBaseConstant.DEMOGRAPHY_TABLE+";";

        int size = getNumberOfDemography();

        String data [] = new String[size];

        try {
            ResultSet rs = dataBase.runQuarry(quarry);
            int i=0;
            while (rs.next()){
                data[i++] = rs.getString(DataBaseConstant.DEMOGRAPHY_COLUMN);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Return number of demography
     * */
    public static int getNumberOfDemography(){

        if(numberOfDemography !=0 ){
            return numberOfDemography;
        }

        String quarry = "SELECT COUNT( DISTINCT "+DataBaseConstant.DEMOGRAPHY_COLUMN+") as C FROM "
                + DataBaseConstant.DEMOGRAPHY_TABLE+";";

        try {
            ResultSet rs = dataBase.runQuarry(quarry);
            numberOfDemography = rs.getInt("C");
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfDemography;
    }

    /**
     * Return factor and mean list for given demography
     *
     * return as a string list 1st element is a factor 2nd is the mean value of that factor Etc.
     * */
    public static ArrayList<String> getFactorNmean(String demo){

        ArrayList<String> list = new ArrayList<String>();

        String cover = "\'";

        if(demo.contains("\'")){
            cover = "\"";
        }

        String quarry = "SELECT "+ DataBaseConstant.FACTOR_COLUMN+","+DataBaseConstant.MEAN_COLUMN
                + " FROM "+DataBaseConstant.DEMOGRAPHY_TABLE
                + " WHERE " +DataBaseConstant.DEMOGRAPHY_TABLE+"."+DataBaseConstant.DEMOGRAPHY_COLUMN
                + " = "+cover+demo+cover+";";

        try {
            ResultSet rs =dataBase.runQuarry(quarry);

            while (rs.next()){
                list.add(rs.getString(DataBaseConstant.FACTOR_COLUMN));
                list.add(Float.toString(rs.getFloat(DataBaseConstant.MEAN_COLUMN)));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Return AOI theme set as a string list
     * */
    public static ArrayList<String> getAOIThemes(){

        ArrayList<String> list = new ArrayList<String>();

        String quarry = "SELECT "+ DataBaseConstant.THEME_COLOUMN
                + " FROM "+DataBaseConstant.AOI_TABLE+";";

        return list = getThemes(quarry);
    }

    /**
     * Return AOS theme set as a string list
     * */
    public static ArrayList<String> getAOSThemes(){

        ArrayList<String> list = new ArrayList<String>();

        String quarry = "SELECT "+ DataBaseConstant.THEME_COLOUMN
                + " FROM "+DataBaseConstant.AOS_TABLE+";";

        return list = getThemes(quarry);
    }

    /**
     * AOI and AOS data extractor
     *
     * */
    private static ArrayList<String> getThemes(String quarry) {
        ArrayList<String> list = new ArrayList<String>();

        ResultSet rs = null;
        try {
            rs = dataBase.runQuarry(quarry);

            while (rs.next()){
                list.add(rs.getString(DataBaseConstant.THEME_COLOUMN));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * Return statements for a given theme
     *
     * */
    public static ArrayList<String> getStatement(String theme){

        ArrayList<String> list = new ArrayList<String>();

        String cover = "\'";

        if(theme.contains("\'")){
            cover = "\"";
        }

        String quarry = "SELECT "+ DataBaseConstant.STATEMENT_COLUMN
                + " FROM "+ DataBaseConstant.SORT_TABLE
                + " WHERE " +DataBaseConstant.THEME_COLOUMN+"="+cover+theme+cover+";";

        try {
            ResultSet rs = dataBase.runQuarry(quarry);

            while(rs.next()){
                list.add(rs.getString(DataBaseConstant.STATEMENT_COLUMN));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }
}
