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
        document.documentElement.normalize();
        val rootElement: Element = document.documentElement
        val creator:String = rootElement.getAttribute("creator")
        val version:String = rootElement.getAttribute("version")

        var waypoints:ArrayList<GPXWaypoint> = ArrayList()
        var tracks:ArrayList<trk> = ArrayList()
        val waypoint_nodeList:NodeList = rootElement.getElementsByTagName("wpt")
        for (i in 0 until waypoint_nodeList.length){
            val waypoint_node = waypoint_nodeList.item(i);
            var name:String? = null
            var desc:String? = null
            var sym:String? = null
            for (j in 0 until waypoint_node.childNodes.length){
                if (waypoint_node.childNodes.item(j).nodeName == "name"){
                    name = waypoint_node.childNodes.item(j).nodeValue
                }else if (waypoint_node.childNodes.item(j).nodeName == "desc"){
                    desc = waypoint_node.childNodes.item(j).nodeValue
                }else if (waypoint_node.childNodes.item(j).nodeName == "sym"){
                    sym = waypoint_node.childNodes.item(j).nodeValue
                }
            }
            var waypoint = GPXWaypoint(valueOf(waypoint_node.attributes.getNamedItem("lat").nodeValue),
                valueOf(waypoint_node.attributes.getNamedItem("lon").nodeValue), 0.0,
                System.currentTimeMillis(), name.toString(), desc.toString(), sym.toString())

            waypoints.add(waypoint)

        }
        gpx = GPX(creator, version, waypoints, tracks)
        return gpx
    }

}

