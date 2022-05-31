/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    August 2021
   @name    tinytool.tasks.MergeTask

   @notes   task to merge PDF documents
   @changes
 */

package tinytool.tasks;

import javafx.concurrent.Task;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergeTask extends Task<Integer> {
  public String workDir;
  public List<File> fileList;
  public String fileName;
  public int docs;

  private final StringBuilder feedBack = new StringBuilder();
  private int counter;

  public void mergeTask(String workDir, List<File> fileList, String fileName, int docs) {
    this.workDir = workDir;
    this.fileList = fileList;
    this.fileName = fileName;
    this.docs = docs;
  }

  @Override
  protected Integer call() throws Exception {
    this.updater("Starting the merge process");

    if (fileList.size() < 2)
      this.updater("Cannot merge a file into itself");
    else {
      PDFMergerUtility mergerUtility = new PDFMergerUtility();
      mergerUtility.setDestinationFileName(workDir + fileName + ".pdf");

      try {
        for (File file:fileList) {
          mergerUtility.addSource(file.getAbsolutePath());
          counter++;
        }

        mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        this.updateProgress(counter, docs);
        this.updater("Merged " + docs + " into " + workDir + fileName + ".pdf");

      } catch (IOException e) {
        this.updater("Error occurred while merging. " + e.getMessage());
      }
    }

    return counter;
  }

  private void updater(String text) throws InterruptedException {
    StringBuilder append = feedBack.append(text).append("\n");
    this.updateMessage(String.valueOf(append));
    Thread.sleep(2);
  }
}