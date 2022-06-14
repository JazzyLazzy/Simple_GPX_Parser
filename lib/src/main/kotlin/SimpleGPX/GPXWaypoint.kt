package SimpleGPX

class GPXWaypoint: GPXParserLocation{

    var name: String? = null
    var desc: String? = null
    var sym: String? = null

    constructor(latitude: Double, longitude: Double, elevation:Double, time:Long) :
            super(latitude, longitude, elevation, time)

    constructor(latitude: Double, longitude: Double, elevation:Double, time:Long, name:String) :
            super(latitude, longitude, elevation, time) {
        this.name = name
    }

    constructor(latitude: Double, longitude: Double, elevation:Double, time:Long, name:String, desc:String) :
            super(latitude, longitude, elevation, time) {
        this.name = name
        this.desc = desc
    }

    constructor(latitude: Double, longitude: Double, elevation:Double, time:Long, name:String, desc:String, sym:String) :
            super(latitude, longitude, elevation, time) {
        this.name = name
        this.desc = desc
        this.sym = sym
    }

    constructor(location: GPXParserLocation, name:String) :
            super(location.latitude, location.longitude, location.elevation, location.time) {
        this.name = name
    }

    constructor(location: GPXParserLocation, name:String, desc:String) :
            super(location.latitude, location.longitude, location.elevation, location.time) {
        this.name = name
        this.desc = desc
    }

    constructor(location: GPXParserLocation, name:String, desc:String, sym:String) :
            super(location.latitude, location.longitude, location.elevation, location.time) {
        this.name = name
        this.desc = desc
        this.sym = sym
    }

    constructor(location: GPXParserLocation) :
            super(location.latitude, location.longitude, location.elevation, location.time)

}