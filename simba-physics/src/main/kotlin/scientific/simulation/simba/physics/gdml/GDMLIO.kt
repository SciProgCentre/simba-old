package ru.mipt.npm.mcengine.geant4.gdml

import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.File
import java.net.URI
import java.nio.file.Path
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

class GDMLIO private constructor(val document : Document){
    companion object {
        fun read(file: File): GDMLIO {
            val source = StreamSource(file)
            try {
                validator.validate(source)
                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                return GDMLIO(doc)
            }
            catch (ex: SAXException){
                println(file.toString() + " is  not valid because ")
                println(ex.message)
                throw ex
            }
        }
        fun read(path: Path)=read(File(path.toUri()))
        fun read(uri: URI) = read(File(uri))
        fun read(path: String)=read(File(path))

        val schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
                javaClass.getResource("/gdml/schema/gdml.xsd")
        )
        private val validator = schema.newValidator()
    }

}