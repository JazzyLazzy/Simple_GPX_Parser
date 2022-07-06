package SimpleGPX


import org.w3c.dom.NodeList

open class GPX {

    var creator: String = String()
    var version: String = String()
    var waypoints:ArrayList<GPXWaypoint> = ArrayList()
    var tracks:ArrayList<trk> = ArrayList()

    constructor()

    constructor(creator:String, version:String){
        this.creator = creator
        this.version = version
    }

    constructor(creator:String, version: String, tracks: ArrayList<trk>){
        this.creator = creator
        this.version = version
        this.tracks = tracks
    }

    constructor(creator:String, version:String, waypoints:ArrayList<GPXWaypoint>, tracks:ArrayList<trk>){
        this.creator = creator
        this.version = version
        this.waypoints = waypoints
        this.tracks = tracks
    }

    fun addWaypoint(waypoint:GPXWaypoint){
        waypoints.add(waypoint)
    }

    fun addWaypoints(waypoints: ArrayList<GPXWaypoint>){
        for(i in waypoints){
            this.waypoints.add(i)
        }
    }

    fun removeWaypoint(){

    }

    fun removeWaypoints(){

    }

    fun addTrack(track:trk){
        tracks.add(track)
    }

    fun addTracks(tracks: ArrayList<trk>){
        for (track in tracks){
            this.tracks.add(track)
        }
    }

    //Add track point to the very end of the entire gpx track
    fun addTrackPointToEnd(trkPoint:TrackPoint){
        this.tracks.last().trksegs.last().trkpts.add(trkPoint);
    }

    //Remove the very last track point
    fun removeTrackPointFromEnd(){
        this.tracks.last().trksegs.last().trkpts.removeLast();
    }

    fun remove_allWaypoints(){
        for(i in this.waypoints){
            this.waypoints.remove(i)
        }
    }

    fun remove_allTracks(){
        for (i in tracks){
            tracks.remove(i)
        }
    }
}
