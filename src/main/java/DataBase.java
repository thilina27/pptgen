import java.io.File;
import java.sql.*;

/**
 * Created by Thilina on 6/8/2016.
 */
public class DataBase {

    private Connection con;
    private Statement stmnt;
    private int statementId = 0;
    private float avarages[][] = new float[FileReadConstant.MAX_NUM_OF_AVERAGES+1][2];
    private float gradmean[] = new float[2];
    private int numberOfRespondents;
    private int cocID = 0;
    private int themeID = 0;

    /**
     * @author Thilina
     *
     * Constructor for the DataBase class
     *
     * Create a connection to data base and tables withing the database
     *
     * @throws DataBaseException
     * */
    public DataBase() throws DataBaseException {

        try {
            this.createDB();
            this.createTables();
        } catch (ClassNotFoundException e) {
            throw new DataBaseException(e);
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }

        System.out.println("Database added");

    }

    /**
     * @author Thilina
     * 
     * Create data base to store tables
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * */
    private void createDB() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        this.con = DriverManager.getConnection("jdbc:sqlite:" + DataBaseConstant.DATABASE_NAME);
    }

    //Core Statement table methods
    /**
     * @author Thilina
     * 
     * Create all statement tables
     *
     * @throws SQLException
     * */
    private void createTables() throws SQLException {

        stmnt = con.createStatement();
        String createtablesql = DataBaseConstant.getCreateTableQuarry(DataBaseConstant.TABLE_STATEMENT);
        stmnt.executeUpdate(createtablesql);
        createtablesql = DataBaseConstant.getCreateTableQuarry(DataBaseConstant.OTHER_STATEMENTS_TABLE);
        stmnt.executeUpdate(createtablesql);
        createtablesql = DataBaseConstant.getCreateTableQuarry(DataBaseConstant.GPTW_STATEMENTS);
        stmnt.executeUpdate(createtablesql);
        createtablesql = DataBaseConstant.getCreateCOCORTableQuarry(DataBaseConstant.COC_TABLE);
        stmnt.executeUpdate(createtablesql);
        createtablesql = DataBaseConstant.getCreateThemeTableQuarry(DataBaseConstant.THEME_TABLE);
        stmnt.executeUpdate(createtablesql);
        stmnt.close();
        System.out.println("Tables created successfully");
    }

    /**
     * @author Thilina
     *
     * Insert single statement into given statement table with the company and bench mark value
     *
     * @param table name of the table that statement insert into
     * @param statement statement.
     * @param company company value of the statement.
     * @param bench company value of the statement.
     *
     * @throws DataBaseException
     * */
    private void insertIntoStatementsTable(String table, String statement, float company, float bench)
            throws DataBaseException {

        try {
            con.setAutoCommit(false);
            String quarry = DataBaseConstant.getPreparedInsertStatement(table);
            PreparedStatement prestatement= con.prepareStatement(quarry);
            prestatement.setInt(1, statementId);
            prestatement.setString(2,statement);
            prestatement.setFloat(3,company);
            prestatement.setFloat(4,bench);

            prestatement.executeUpdate();
            statementId++;
            con.commit();

            System.out.println("Inset successful in to " + table);

        } catch (SQLException e) {
            throw new DataBaseException(e);
        }

    }

    /**
     * @author Thilina
     *
     * insert data into coc or table wich holds cocor values and stdev values for each statement
     *
     * @param statement statement
     * @param coc COC_OR value of the statement
     * @param stdev STDEV of the statement
     *
     * @throws DataBaseException
     *
     * */
    public void insetIntoCOCTable(String statement, float coc, float stdev) throws DataBaseException {

        try {
            con.setAutoCommit(false);
            String quarry = DataBaseConstant.getPreparedInsetCOCTable();
            PreparedStatement prestatement= con.prepareStatement(quarry);
            prestatement.setInt(1,cocID);
            prestatement.setString(2,statement);
            prestatement.setFloat(3,coc);
            prestatement.setFloat(4,stdev);

            prestatement.executeUpdate();
            cocID++;
            con.commit();

            System.out.println("Inset successful in to COC or");

        } catch (SQLException e) {
            throw new DataBaseException(e);
        }

    }

    public void insertIntoThemeTable(String statement, String theme) throws DataBaseException {

        try {
            con.setAutoCommit(false);
            String quarry = DataBaseConstant.getPreparedInsetThemeTable();
            PreparedStatement prestatement= con.prepareStatement(quarry);
            prestatement.setInt(1,themeID);
            prestatement.setString(2,statement);
            prestatement.setString(3,theme);
            prestatement.executeUpdate();
            themeID++;
            con.commit();

            System.out.println("Inset successful in to theme or");

        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    /**
     * @author Thilina
     *
     * Insert core satement
     *
     * @param statement statement.
     * @param company company value of the statement.
     * @param bench company value of the statement.
     *
     * @throws DataBaseException
     *
     * */
    public void insertCoreStatement(String statement, float company, float bench) throws DataBaseException {
        this.insertIntoStatementsTable(DataBaseConstant.TABLE_STATEMENT,statement,company,bench);
    }

    /**
     * @author Thilina
     *
     * Insert other satement
     *
     * @param statement statement.
     * @param company company value of the statement.
     * @param bench company value of the statement.
     *
     * @throws DataBaseException
     *
     * */
    public void insertOtherStatement(String statement, float company, float bench) throws DataBaseException {
        this.insertIntoStatementsTable(DataBaseConstant.OTHER_STATEMENTS_TABLE,statement,company,bench);
    }

    /**
     * @author Thilina
     *
     * Insert GPTW satement
     *
     * @param statement statement.
     * @param company company value of the statement.
     * @param bench company value of the statement.
     *
     * @throws DataBaseException
     *
     * */
    public void insertGPTWStatement(String statement, float company, float bench) throws DataBaseException {
        this.insertIntoStatementsTable(DataBaseConstant.GPTW_STATEMENTS,statement,company,bench);
    }

    /**
     * @author Thilina
     *
     * Inset avarages in to avarage array
     *
     * @param companyAverage average value of the company
     * @param benchAverage average value of the bench mark
     * @param numberOfAverages index of average array
     * */
    public void insertAverage(float companyAverage, float benchAverage, int numberOfAverages){

        avarages[numberOfAverages][DataBaseConstant.COMPANY_AVERAGE_INDEX] = companyAverage;
        avarages[numberOfAverages][DataBaseConstant.BENCH_MARK_AVERAGE_INDEX] = benchAverage;

    }

    public void setNumberOfRespondents(int val){
        numberOfRespondents = val;
    }

    //get methods
    public int getNumberOfRespondents(){
        return numberOfRespondents;
    }

    /**
     * @author Thilina
     *
     * return result set for a given quary execution
     *
     * @param quarry quarry to be executed
     *
     * @throws SQLException
     * */
    public ResultSet runQuarry(String quarry) throws SQLException {

        con.setAutoCommit(false);
        stmnt = con.createStatement();
        ResultSet rs = stmnt.executeQuery(quarry);

        return rs;
    }

    public float[][] getAvarages(){
        return avarages;
    }

    //other generic DB methods

    /**
     * @author Thilina
     *
     * This will close connection to the Data base
     *
     * */
    public boolean closeConnection(){

        boolean closed = false;

        try {

            con.close();
            this.deleteDataBase();
            closed = true;
        } catch (SQLException e) {
            //todo handle
            e.printStackTrace();
        }

        return closed;
    }

    /**
     * @author Thilina
     *
     * Delete database after use
     * */
    private boolean deleteDataBase(){

        boolean isDelete = false;

        try{

            File file = new File(DataBaseConstant.DATABASE_NAME);

            if(file.delete()){
                isDelete = true;
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }

        }catch(Exception e){

            //todo handle
            e.printStackTrace();

        }

        return isDelete;
    }

}
