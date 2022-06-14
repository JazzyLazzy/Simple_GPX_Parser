package SimpleGPX

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class NotGPXFileException(message:String) : Exception(message) {

}