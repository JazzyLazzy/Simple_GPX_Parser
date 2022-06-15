package SimpleGPX

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.lang.Double.valueOf
import java.time.LocalDateTime
import javax.xml.parsers.DocumentBuilderFactory

open class SimpleGPXParser(fileName: String) : SimpleGPXWriter(fileName){

    fun parseGPX():GPX{
        try {
            isGPXFile()
        }catch(error:NotGPXFileException){
            error.printStackTrace()
        }
        val document = buildDocument()
        return GPXifyDocument(document);
    }

}

