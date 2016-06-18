package pptgen.data;

/**
 * Created by Instructor - ICT on 6/8/2016.
 */
public class TestDrive {

    public static void main(String arg[]) {

        long startTime = System.currentTimeMillis();
        ReadFile.init();
        ReadFile.readStatements("excl\\test2.xlsx","KBSL Information Technologies Ltd","Worksheet");
        ReadFile.readCOCOR("excl\\cocrep.xlsx", "Worksheet");
        ReadFile.readThemes("excl\\test2.xlsx","Std Themes");
        ReadFile.readDemoFactors("excl\\test2.xlsx","Worksheet");
        ReadFile.createSortData();
        Analyzer.start();
        ReadFile.closeall();

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}

