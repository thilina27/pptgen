package pptgen.pptwriter;

import pptgen.data.DataStore;

import java.io.IOException;
import java.util.ArrayList;

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

    public static void generateStartingSlides(String companyName){

        pptx.createCover(companyName);
        pptx.createContext(companyName);
        pptx.createObjectives(companyName);

    }

    public static void generateSurveyConstructSlide(int numberOfEmployees, String mode, String languages,
                                                    String benchmark){

        int numberOfStatements = DataStore.getNumberOfSattements();
        int numberOfRespondents = DataStore.getNumberOfRespondents();
        pptx.surveyConstruct(numberOfStatements,numberOfEmployees,numberOfRespondents,mode,languages,benchmark);

    }

    public static void generateDemographySildes(){

        int numberOfDemos = DataStore.getNumberOfDemography();
        String demo[] = DataStore.getAllDemography();

        pptx.createDemography(numberOfDemos,demo);
        pptx.createDemographyCharts(numberOfDemos,demo);

    }

    public static void generateAOINAOSList(){

        ArrayList<String> aos = DataStore.getAOSThemes();
        ArrayList<String> aoi = DataStore.getAOIThemes();

        pptx.createAOINAOSList(aoi,aos);
    }

    public static void savePPTX(String fileName){
        pptx.savePPT(fileName);
    }



}
