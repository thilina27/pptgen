package pptgen.pptwriter;

import java.io.IOException;

/**
 * Created by Thilina on 6/19/2016.
 */
public class PptWriter {

    private static PptCreator pptx;

    //template
    public static void openPPT(String fileName){
        try {
            pptx = new PptCreator(fileName);
        } catch (IOException e) {
            e.printStackTrace();//// TODO: 6/19/2016
        }
    }

    public static void genarateSlides(String companyName){

        pptx.createCover(companyName);
        pptx.createContext(companyName);
        pptx.createObjectives(companyName);

    }

    public static void savePPTX(String fileName){
        pptx.savePPT(fileName);
    }



}
