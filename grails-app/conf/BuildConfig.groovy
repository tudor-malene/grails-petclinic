grails.servlet.version = "2.5"
grails.project.work.dir = "target/$grailsVersion"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.plugin.location.easygrid ="../Easygrid"
grails.project.dependency.resolution = {

    inherits "global"
    log "error"
    checksums true

    repositories {
        inherits true

        grailsPlugins()
        grailsHome()
        grailsCentral()
    }

    dependencies {
    }

    plugins {
        runtime':hibernate:3.6.10.10'
        build ":tomcat:7.0.50.1"

        compile ":jquery-ui:1.10.3"
        runtime ":jquery:1.10.2.2"
//        compile ":easygrid:1.5.0"

        runtime ":resources:1.2.7"
        compile ":export:1.5"
    }
}
