

/**
 * Created by Thilina on 6/8/2016.
 */
public class DataBaseConstant {

    public static final String DATABASE_NAME = "greatplcetowork.db";
    public static final String ID_COLUMN = "ID";
    public static final String STATEMENT_COLUMN = "STATEMENT";
    public static final String COMPANY_COLUMN = "COMPANY";
    public static final String BENCHMARK_COLUMN = "BENCHMARK";
    public static final String TABLE_STATEMENT = "STATEMENT";


    //average array constant
    public static final int COMPANY_AVERAGE_INDEX = 0;
    public static final int BENCH_MARK_AVERAGE_INDEX = 1;
    public static final int GRAND_MEAN_INDEX = 5;

    //table to store other statements after 5 averages
    //Sri Lanka specific statements.
    public static final String OTHER_STATEMENTS_TABLE = "OTHER_STATEMENTS";
    //use same columns as in other statements

    //Grate place to work statements
    public static final String GPTW_STATEMENTS = "GPTWSTATEMETS";
    //use same columns as above two

    //COC_OR table
    public static final String COC_TABLE = "COCTABLE";
    public static final String STDEV_COLUMN = "STDEV";
    public static final String COC_OR_COLUMN = "COC_OR";

    //theme table
    public static final String THEME_TABLE = "THEMETABLE";
    public static final String THEME_COLOUMN = "THEME";

    //sort table
    public static final String SORT_TABLE = "SORTTABLE";
    public static final String DIFF_COLUMN = "DIFF";
    public static final String PRES_COLUMN = "PRESCOLOUMN";

    //sor table quarry
    public static final String GET_JOIN_QUARRY = "SELECT s."+ ID_COLUMN +" s."+ STATEMENT_COLUMN +", s."+COMPANY_COLUMN
            + " , s."+BENCHMARK_COLUMN+", c."+COC_OR_COLUMN+",c."+STDEV_COLUMN+", t."+ THEME_COLOUMN
            + " FROM  "+TABLE_STATEMENT+" s, "+ COC_TABLE +" c , "+THEME_TABLE+" t"
            + " WHERE c."+STATEMENT_COLUMN+" = s."+STDEV_COLUMN+" and t."+STDEV_COLUMN+" = c."+STDEV_COLUMN+" ";

    //get insert in to bale quarry for a given table
    public static String getPreparedInsertStatement(String tableName){

        String INSERT_STATEMENT_TABLE_QUARRY = "INSERT INTO "+ tableName +
                "("+ ID_COLUMN +","+ STATEMENT_COLUMN +
                ","+ COMPANY_COLUMN +","+ BENCHMARK_COLUMN +")";

        String PREPARED_STATEMENT  = INSERT_STATEMENT_TABLE_QUARRY + "VALUES (?,?,?,?)";

        return PREPARED_STATEMENT;

    }

    public static String getCreateTableQuarry(String tableName){

        String CREATE_STATEMENT_TABLE_QUARRY = "CREATE TABLE "+tableName+" " +
                "("+ ID_COLUMN +"            INT     PRIMARY KEY    NOT NULL," +
                " "+ STATEMENT_COLUMN +"     TEXT    NOT NULL, " +
                " "+ COMPANY_COLUMN +"       REAL    NOT NULL, " +
                " "+ BENCHMARK_COLUMN +"     REAL    NOT NULL) ";

        return CREATE_STATEMENT_TABLE_QUARRY;
    }

    public static String getCreateCOCORTableQuarry(String tableName){

        String CREATE_STATEMENT_TABLE_QUARRY = "CREATE TABLE "+tableName+" " +
                "("+ ID_COLUMN +"            INT     PRIMARY KEY    NOT NULL," +
                " "+ STATEMENT_COLUMN +"     TEXT    NOT NULL, " +
                " "+ COC_OR_COLUMN +"       REAL    NOT NULL, " +
                " "+ STDEV_COLUMN +"     REAL    NOT NULL) ";

        return CREATE_STATEMENT_TABLE_QUARRY;
    }

    public static String getCreateThemeTableQuarry(String tableName){

        String CREATE_STATEMENT_TABLE_QUARRY = "CREATE TABLE "+tableName+" " +
                "("+ ID_COLUMN +"            INT     PRIMARY KEY    NOT NULL," +
                " "+ STATEMENT_COLUMN +"     TEXT    NOT NULL, " +
                " "+ THEME_COLOUMN +"        TEXT    NOT NULL) ";

        return CREATE_STATEMENT_TABLE_QUARRY;
    }

    public static String getCreateSortTableQuarry(String tableName){

        String CREATE_STATEMENT_TABLE_QUARRY = "CREATE TABLE "+tableName+" " +
                "("+ ID_COLUMN +"           INT     PRIMARY KEY    NOT NULL," +
                " "+ STATEMENT_COLUMN +"    TEXT    NOT NULL, " +
                " "+ COC_OR_COLUMN +"       REAL    NOT NULL, " +
                " "+ STDEV_COLUMN +"        REAL    NOT NULL, " +
                " "+ DIFF_COLUMN + "        REAL    NOT NULL, " +
                " "+ PRES_COLUMN + "        REAL    NOT NULL, " +
                " "+ THEME_COLOUMN +"       TEXT    NOT NULL) ";

        return CREATE_STATEMENT_TABLE_QUARRY;
    }

    public static String getPreparedInsetCOCTable(){

        String INSERT_STATEMENT_TABLE_QUARRY = "INSERT INTO "+ COC_TABLE +
                "("+ ID_COLUMN +","+ STATEMENT_COLUMN +
                ","+ COC_OR_COLUMN +","+ STDEV_COLUMN +")";

        String PREPARED_STATEMENT  = INSERT_STATEMENT_TABLE_QUARRY + "VALUES (?,?,?,?)";

        return PREPARED_STATEMENT;
    }

    public static String getPreparedInsetThemeTable(){

        String INSERT_STATEMENT_TABLE_QUARRY = "INSERT INTO "+ THEME_TABLE +
                "("+ ID_COLUMN +","+ STATEMENT_COLUMN +
                ","+ THEME_COLOUMN +")";

        String PREPARED_STATEMENT  = INSERT_STATEMENT_TABLE_QUARRY + "VALUES (?,?,?)";

        return PREPARED_STATEMENT;
    }

    public static String getPreparedInsetSortTable(){

        String INSERT_STATEMENT_TABLE_QUARRY = "INSERT INTO "+ SORT_TABLE +
                "("+ ID_COLUMN +","+ STATEMENT_COLUMN +
                ","+ COC_OR_COLUMN +","+ STDEV_COLUMN +","+ DIFF_COLUMN + ","+ THEME_COLOUMN +")";

        String PREPARED_STATEMENT  = INSERT_STATEMENT_TABLE_QUARRY + "VALUES (?,?,?,?,?,?)";

        return PREPARED_STATEMENT;
    }
}
