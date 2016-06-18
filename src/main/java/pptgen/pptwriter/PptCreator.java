package pptgen.pptwriter;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Thilina on 6/18/2016.
 */
public class PptCreator {

    private XMLSlideShow pptx;
    private List<XSLFSlide> slides;

    public PptCreator(String fileName) throws IOException {

        pptx = new XMLSlideShow(new FileInputStream(fileName));
        slides = pptx.getSlides();

    }

    public void createCover(String companyName){

        XSLFSlide slide = slides.get(PptReadConstant.COVER_SLIDE_NUMBER);

        List<XSLFShape> shapes = slide.getShapes();

        for (XSLFShape shape: shapes) {
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape)shape;
                String slideText = textShape.getText();
                if(slideText.contains(PptReadConstant.COMPANY_NAME_TOKEN)){
                    textShape.setText(companyName.toUpperCase());
                    break;
                }
            }
        }
    }

    public void createContext(String companyName){

        XSLFSlide slide = slides.get(PptReadConstant.CONTEXT_SLIDE_NUMBER);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<XSLFShape> shapes = slide.getShapes();

        for (XSLFShape aShape : shapes) {
            String name = aShape.getShapeName();
            if(name != null && name.contains(PptReadConstant.TEXT_BOX)){
                if (aShape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) aShape;
                    String text = textShape.getText();
                    text = text.replace(PptReadConstant.COMPANY_NAME_TOKEN, companyName);
                    text = text.replace(PptReadConstant.YEAR_TOKEN,Integer.toString(year));
                    textShape.setText(text);
                }
            }
        }
    }

    public void createObjectives(String companyName){

        XSLFSlide slide = slides.get(PptReadConstant.OBJECTIVES_SLIDE_NUMBER);
        List<XSLFShape> shapes = slide.getShapes();

        for (XSLFShape aShape : shapes) {
            String name = aShape.getShapeName();
            if(name != null && name.contains(PptReadConstant.TEXT_BOX)){
                if (aShape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) aShape;
                    String text = textShape.getText();
                    if(text.contains(PptReadConstant.COMPANY_NAME_TOKEN)){
                        text = text.replace(PptReadConstant.COMPANY_NAME_TOKEN, companyName);
                        textShape.setText(text);
                        break;
                    }
                }
            }
        }
    }

    public void surveyConstruct(int numberOfStatements, int numberofStatements, int numberOfEmployees,
                                int numberOfRespondents, String mode, String languages, String benchmark){

        XSLFSlide slide = slides.get(PptReadConstant.SURVEY_CONSTRUCT_SLIDE_NUMBER);
        List<XSLFShape> shapes = slide.getShapes();

        for (XSLFShape aShape : shapes) {
            String name = aShape.getShapeName();
            if(name != null && name.contains(PptReadConstant.TEXT_BOX)){
                if (aShape instanceof XSLFTextShape) {

                    XSLFTextShape textShape = (XSLFTextShape)aShape;
                    String text = textShape.getText();

                    if(text.contains(PptReadConstant.NUM_OF_STATEMENTS_TOKEN)){
                        String num = Integer.toString(numberOfStatements);
                        text = text.replace(PptReadConstant.NUM_OF_STATEMENTS_TOKEN,num);
                    }
                    else if(text.contains(PptReadConstant.BENCHMARK_TOKEN)){
                        text = text.replace(PptReadConstant.BENCHMARK_TOKEN,benchmark);
                    }
                    else if(text.contains(PptReadConstant.NUMBER_OF_EMPLOYEE_TOKEN)){
                        String val = Integer.toString(numberOfRespondents);
                        text = text.replace(PptReadConstant.NUMBER_OF_RESPONDENT_TOKEN,val);
                        val = Integer.toString(numberOfEmployees);
                        text = text.replace(PptReadConstant.NUMBER_OF_EMPLOYEE_TOKEN,val);
                        int temp =numberOfEmployees - numberOfRespondents;
                        val = Integer.toString(temp);
                        text = text.replace(PptReadConstant.NUMBER_OF_NON_RESPONDENT_TOKEN,val);
                    }
                    else if(text.contains(PptReadConstant.MODE_TOKEN)){
                        text = text.replace(PptReadConstant.MODE_TOKEN,mode);
                    }
                    else if(text.contains(PptReadConstant.LANGUAGES_TOKEN)){
                        text = text.replace(PptReadConstant.LANGUAGES_TOKEN,languages);
                    }
                    textShape.setText(text);
                }
            }
        }
    }


    public void createDemography(int numberOfDemos, String demo[]){

        XSLFSlide slide = slides.get(PptReadConstant.SURVEY_CONSTRUCT_SLIDE_NUMBER);
        List<XSLFShape> shapes = slide.getShapes();

        //insert demos
        int i = 0;

        for (XSLFShape aShape : shapes) {
            String name = aShape.getShapeName();
            if(name != null && name.contains(PptReadConstant.TEXT_BOX)){
                if(numberOfDemos == i){
                    break;
                }
                if (aShape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) aShape;
                    String text = textShape.getText();

                    if(text.contains(PptReadConstant.DEMOGRAPHY_TOKEN)){
                        text = text.replace(PptReadConstant.DEMOGRAPHY_TOKEN,demo[i]);
                        i++;
                    }
                    textShape.setText(text);
                }
            }
        }

        //remove extra demos
        for (int j=0;j<shapes.size();j++) {
            XSLFShape aSh = shapes.get(j);
            String name = aSh.getShapeName();
            if(name != null && name.contains(PptReadConstant.TEXT_BOX)){
                if (aSh instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape)aSh;
                    String text = textShape.getText();
                    System.out.println(text);
                    if (text.contains(PptReadConstant.DEMOGRAPHY_TOKEN)) {
                        slide.removeShape(aSh);
                    }

                }
            }
        }

        //remove extra numbers
        i++;
        for (int j=0;j<shapes.size();j++) {
            XSLFShape aSh = shapes.get(j);
            String name = aSh.getShapeName();
            if(name != null && name.contains(PptReadConstant.TEXT_BOX)){
                if (aSh instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape)aSh;
                    String text = textShape.getText();
                    System.out.println(text);
                    if (text.contains(Integer.toString(i))){
                        i++;
                        slide.removeShape(aSh);
                    }
                }
            }
        }
    }


    private void replceText1(String text, String replace, int slideNumber){

        XSLFSlide slide = slides.get(slideNumber);

        List<XSLFShape> shapes = slide.getShapes();
        for (XSLFShape shape: shapes) {
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape)shape;
                String slideText = textShape.getText();
                if(slideText.contains(text)){
                    textShape.setText("Ba ba ab");
                }


            }
        }
    }

}
