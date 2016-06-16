/**
 * Created by Thilina on 6/14/2016.
 *
 * Read data from data base
 * Provide data to other packages
 */
public class DataStore {

    DataBase dataBase;

    public DataStore(DataBase dataBase){
        this.dataBase = dataBase;
    }

    public int getNumberOfRespondents(){
        return dataBase.getNumberOfRespondents();
    }

    
}
