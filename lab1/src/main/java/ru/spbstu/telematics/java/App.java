import java.io.*;
public class App
{
    public static void main( String[] args )
    {
        File file1 = new File("/home/mooth/PP-2021/lab1/src/main/java/ru/spbstu/telematics/java/file1"),
             file2 = new File("/home/mooth/PP-2021/lab1/src/main/java/ru/spbstu/telematics/java/file2");
        try {
            FileInputStream fis1  = new FileInputStream(file1), fis2 = new FileInputStream(file2);
            BufferedInputStream bis1 = new BufferedInputStream(fis1), bis2 = new BufferedInputStream(fis2);
            int available1 = bis1.available(), available2 = bis2.available();
            byte[] buf1 = new byte[available1], buf2 = new byte[available2];
            bis1.read(buf1);
            bis2.read(buf2);
            String str1 = new String(buf1), str2 = new String(buf2);
            int last_index1 = -1, last_index2 = -1, first_index1 = -1, first_index2 = -1;
            while(true) {
                first_index1 = last_index1+1;
                first_index2 = last_index2+1;
                last_index1 = str1.indexOf('\n', last_index1+1);
                last_index2 = str2.indexOf('\n', last_index2+1);
                if (last_index1 == -1 || last_index2 == -1)
                    break;
                System.out.println(str1.substring(first_index1, last_index1));
                System.out.println(str2.substring(first_index2, last_index2));
            }
			fis1.close();
	        fis2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
