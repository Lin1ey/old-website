import scala.io.Source
import java.io.File
import java.io.PrintWriter

class CallCpp {
   @native def callCppMethod(user: String, interviewer: String): Array[String]
}

object ScalaLayer {
   
   def writeToPl(csvName: String, writer: PrintWriter) = {
      val csv = io.Source.fromFile(csvName).getLines.toList
      csvName.split("\\/")(1) match {
         case "friended.csv" => {
            val csvList = csv.map("friended(" + _ +  ").\n")
            csvList.map(writer.write(_))
         }
         case "likes.csv" => {
            val csvList = csv.map("likes(" + _ +  ").\n")
            csvList.map(writer.write(_))
         }
         case "p_exclude.csv" => {
            val csvList = csv.map("permission_exclude(" + _ +  ").\n")
            csvList.map(writer.write(_))
         }
         case "p_mfo.csv" => {
            val csvList = csv.map("permission_mfo(" + _ +  ").\n")
            csvList.map(writer.write(_))
         }
         case "posts.csv" => {
            val csvList = csv.map("post(" + _ +  ").\n")
            csvList.map(writer.write(_))
         }
         case "comments.csv" => {
            val csvList = csv.map("comment(" + _ +  ").\n")
            csvList.map(writer.write(_))
         }
         case _ => writer.write("wrong")
      }
   }

   def main(args: Array[String]) {

      //grabs each csv file
      val inputDirectory = new File(args(0))
      val listOfInputFileNames = inputDirectory.listFiles.filter(_.isFile).toList.map(_.getName())
      val listOfCSV = listOfInputFileNames.map(args(0) + "/" + _)

      //create the info database
      val infoPL = new File("info.pl");
      val printWriter = new PrintWriter(infoPL)
      listOfCSV.map(writeToPl(_, printWriter))   

      //close file writer
      printWriter.close()
      
      System.loadLibrary("CallCFromScala")
      val cn = new CallCpp
      val a = (cn.callCppMethod(args(1), args(2)))

      val csvName = args(0).split("_")(1)
      val fileObject = new File(args(1) +"_" + args(2) + ".csv") 
      val outputWriter = new PrintWriter(fileObject)
      
      a.length match {
         case 0 => outputWriter.write("Looking Good!\n")
         case _ => val listOfPost = a.map(_.split(",")(0))
                  val listOfMesages = a.map(_.split(",")(1)).map(x=> "'" + x + "'")
                  val finalList = listOfPost zip listOfMesages
                  val actuallyFinalList = finalList.map(t => t._1 + "," + t._2)
                  actuallyFinalList.map(x => outputWriter.write(x + "\n"))
      }
      
      outputWriter.close()
      
   }
   
}