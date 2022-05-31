/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    August 2021
   @name    tinytool.tasks.SplitTask

   @notes   task to split PDF documents
   @changes
 */

package tinytool.tasks;

import javafx.concurrent.Task;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SplitTask extends Task<Integer> {
  public String workDir;
  public List<File> fileList;
  public String prefix;
  public int pages;

  private final StringBuilder feedBack = new StringBuilder();
  private int counter = 0;

  public void splitTask(String workDir, List<File> fileList, String prefix, int pages) {
    this.workDir = workDir;
    this.fileList = fileList;
    this.prefix = prefix;
    this.pages = pages;
  }

  @Override
  protected Integer call() throws Exception {
    String text;
    int page = 1;

    for (File name:fileList) {
      text = "Splitting file: ";
      this.updater(text, new File(name.getName()));

      try {
        PDDocument document = PDDocument.load(name);
        Splitter splitter = new Splitter();
        List<PDDocument> docPages = splitter.split(document);
        int currentCount = 0;

        if (document.getNumberOfPages() >= 2) {
          for (PDDocument doc:docPages) {
            currentCount++;
            String newFile = workDir + prefix + page++ + ".pdf";
            doc.save(newFile);
            doc.close();
            counter++;
            this.updateProgress(counter,pages);
          }

          text = "Created " + currentCount + " documents from ";
          document.close();
        } else {
          document.close();
          text = "Document has only ONE page, not splitting it.";
        }

        this.updater(text, new File(name.getName()));
        document.close();
        text = "Closing ";
        this.updater(text, new File(name.getName()));
      } catch (IOException e) {
        text = "Error " + e.getMessage() + ". Document ";
        this.updater(text, new File(name.getName()));
      }
    }

    return counter;
  }

  private void updater(String text, File file) throws Exception {
    StringBuilder append = feedBack.append(text).append(file.getName().toLowerCase()).append("\n");
    this.updateMessage(String.valueOf(append));
    Thread.sleep(2);
  }
}