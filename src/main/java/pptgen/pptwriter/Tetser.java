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
            XSLFSlide slide = slides.get(40);

            List<XSLFShape> sh = slide.getShapes();

            for (XSLFShape aSh : sh) {
                if (aSh instanceof XSLFTable){
                    XSLFTable tb = (XSLFTable)aSh;
                    tb.getCell(1,0).setText("one");
                    break;
                }
                String name = aSh.getShapeName();
                System.out.println(name);

            }


            OutputStream out = new FileOutputStream("ppts//testout.pptx");
            pptx.write(out);
            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
