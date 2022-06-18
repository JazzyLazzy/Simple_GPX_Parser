package SimpleGPX

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.String
import kotlin.collections.ArrayList
import kotlin.toString


fun GPXify(gpxString:String):GPX{
    val document = documentBuilder(gpxString);
    return GPXifyDocument(document);
}

internal fun GPXifyDocument(document: Document): GPX {
    document.documentElement.normalize();
    val rootElement: Element = document.documentElement
    val creator: String = rootElement.getAttribute("creator")
    val version: String = rootElement.getAttribute("version")

    val waypoints: ArrayList<GPXWaypoint> = ArrayList()
    val waypoint_nodeList: NodeList = rootElement.getElementsByTagName("wpt")
    for (i in 0 until waypoint_nodeList.length) {
        val waypoint_node = waypoint_nodeList.item(i);
        var name: String? = null
        var desc: String? = null
        var sym: String? = null
        for (j in 0 until waypoint_node.childNodes.length) {
            println(waypoint_node.childNodes.item(j).nodeName)
            if (waypoint_node.childNodes.item(j).nodeName == "name"){
                name = waypoint_node.childNodes.item(j).textContent
            } else if (waypoint_node.childNodes.item(j).nodeName == "desc") {
                desc = waypoint_node.childNodes.item(j).textContent
            } else if (waypoint_node.childNodes.item(j).nodeName == "sym") {
                sym = waypoint_node.childNodes.item(j).textContent
            }
        }
        val waypoint = GPXWaypoint(
            waypoint_node.attributes.getNamedItem("lat").nodeValue.toDouble(),
            waypoint_node.attributes.getNamedItem("lon").nodeValue.toDouble(), 0.0,
            System.currentTimeMillis(), name.toString(), desc.toString(), sym.toString()
        )

        waypoints.add(waypoint)

    }

    /*Ok, let's try to explain this bogusness.
    GPX uses an array of tracks.
    A track is an array of track segments.
    A track segment is an array of track points.*/
    val trk_nodeList: NodeList = rootElement.getElementsByTagName("trk");
    val trkpts = ArrayList<TrackPoint>();
    val trkseg = trkseg(trkpts);
    val trackName:String? = null;
    val track = trk(trackName, ArrayList());
    val tracks = ArrayList<trk>();
    for (i in 0 until trk_nodeList.length) {
        val trk_node = trk_nodeList.item(i);
        println(trk_node.nodeName)
        for (j in 0 until trk_node.childNodes.length) {
            val trkseg_node = trk_node.childNodes.item(j)
            println(trkseg_node.nodeName)
            if (trkseg_node.nodeName != "name"){
                for (k in 0 until trkseg_node.childNodes.length) {
                    val trkpt_node = trkseg_node.childNodes.item(k)
                    if (trkpt_node.nodeName == "trkpt"){
                        val latitude = trkpt_node.attributes.getNamedItem("lat").nodeValue.toDouble()
                        val longitude = trkpt_node.attributes.getNamedItem("lon").nodeValue.toDouble()
                        var elevation: Double? = null;
                        var time:LocalDateTime? = null;
                        for (l in 0 until trkpt_node.childNodes.length){
                            if (trkpt_node.childNodes.item(l).nodeName == "ele"){
                                println(trkpt_node.childNodes.item(l).textContent)
                                elevation = trkpt_node.childNodes.item(l).textContent.toDouble()
                            }else if(trkpt_node.childNodes.item(l).nodeName == "time"){
                                println(trkpt_node.childNodes.item(l).textContent)
                                time = LocalDateTime.parse(trkpt_node.childNodes.item(l).textContent)
                            }
                        }
                        var trkpt: TrackPoint?;
                        if(elevation != null && time != null){
                            trkpt = TrackPoint(
                                latitude,
                                longitude,
                                elevation,
                                time
                            )
                        }else if(elevation != null){
                            val bogusLocation = GPXParserLocation(latitude, longitude, elevation)
                            trkpt = TrackPoint(bogusLocation)
                        }else if (time != null){
                            val bogusLocation = GPXParserLocation(latitude, longitude, time)
                            trkpt = TrackPoint(bogusLocation)
                        }else{
                            val bogusLocation = GPXParserLocation(latitude, longitude)
                            trkpt = TrackPoint(bogusLocation)
                        }
                        trkpts.add(trkpt);
                    }


                }
            }
            trkseg.trkpts = trkpts
            track.trksegs.add(trkseg)
        }
        tracks.add(track)
    }

    return GPX(creator, version, waypoints, tracks)
}
private fun documentBuilder(gpxString:String):Document{
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    documentBuilderFactory.isNamespaceAware = true
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    val inputSource = InputSource(StringReader(gpxString))
    return documentBuilder.parse(inputSource);
}