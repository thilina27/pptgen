/**
 * Created by Instructor - ICT on 6/8/2016.
 */
public class DataBaseException extends Exception{

    private String exceptionmsg;
    private Exception newex;

    public DataBaseException(Exception e){

        exceptionmsg = e.getMessage();
        newex = e;
        e.printStackTrace();
    }

    public String toString(){
        return exceptionmsg;
    }

}
