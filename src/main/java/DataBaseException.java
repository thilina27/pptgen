/**
 * Created by Instructor - ICT on 6/8/2016.
 *
 * Exception class for the database handle
 */
class DataBaseException extends Exception{

    private String exceptionmsg;
    private Exception newex;

    DataBaseException(Exception e){

        exceptionmsg = e.getMessage();
        newex = e;
        e.printStackTrace();
    }

    public String toString(){
        return exceptionmsg;
    }

}
