package pptgen.pptwriter;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Thilina on 6/18/2016.
 */
public class Tetser {

    public static void main(String arg[]){

        XMLSlideShow pptx = null;
        List<XSLFSlide> slides;

        try {

            pptx = new XMLSlideShow(new FileInputStream("ppts//New Presentation format with tags.pptx"));
            slides = pptx.getSlides();
            XSLFSlide slide = slides.get(17);

            List<XSLFShape> sh = slide.getShapes();

            //insert demos
            int i = 0;
            int s = 2;

            for (XSLFShape aSh : sh) {

                String name = aSh.getShapeName();
                if(name != null && name.contains("T")){
                    if(s == i){
                        break;
                    }
                    if (aSh instanceof XSLFTextShape) {
                        XSLFTextShape textShape = (XSLFTextShape)aSh;
                        String text = textShape.getText();

                        if(text.contains("<DEMO>")){
                            text = text.replace("<DEMO>","demos");
                            i++;
                        }
                        textShape.setText(text);
                    }
                }
            }

            //remove extra demos
            for (int j=0;j<sh.size();j++) {

                XSLFShape aSh = sh.get(j);
                String name = aSh.getShapeName();
                if(name != null && name.contains("T")){
                    if (aSh instanceof XSLFTextShape) {
                        XSLFTextShape textShape = (XSLFTextShape)aSh;
                        String text = textShape.getText();
                        System.out.println(text);
                        if (text.contains("<DEMO>")) {
                            slide.removeShape(aSh);
                        }

                    }
                }
            }



            OutputStream out = new FileOutputStream("ppts//testout.pptx");
            pptx.write(out);
            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
