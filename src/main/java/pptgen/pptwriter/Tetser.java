package pptgen.pptwriter;

import org.apache.poi.xslf.usermodel.*;

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
            XSLFSlide slide = slides.get(18);

            List<XSLFShape> sh = slide.getShapes();

            for (XSLFShape shape: sh) {
                if (shape instanceof XSLFTextShape) {
                    String name = shape.getShapeName();
                   // System.out.println(name);
                    XSLFTextShape textShape = (XSLFTextShape)shape;
                    String t = textShape.getText();
                    //System.out.println(t);
                    if(name.contains(PptReadConstant.TITLE)){
                        System.out.println(t);
                      t =t.replace("<Demo_1>","fdfdffds");
                        textShape.setText(t);
                        break;
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
