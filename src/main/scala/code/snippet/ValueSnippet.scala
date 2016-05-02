package code
package snippet

import code.config.Site
import code.model.{Definition, Value}
import com.foursquare.rogue.LiftRogue
import com.foursquare.rogue.LiftRogue._
import net.liftweb.common.Full
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.NodeSeq

object ValueSnippet extends SortableSnippet[Value] {

  val meta = Value

  val title = "Principios"

  val addUrl = Site.backendValueAdd.calcHref(Value.createRecord)

  def entityListUrl: String = Site.backendValues.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Value): String = Site.backendValueEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.url)

  def renderViewFrontEnd: CssSel = {
    for {
      value <- Site.principio.currentValue
    } yield {
      "data-name=name *" #> value.name.get &
      "data-name=name [href]" #> Site.principio.calcHref(value) &
      {
        "data-name=imageF" #> {
          value.image.valueBox match {
            case Full(image) =>
              val imageSrc = image.fileId.get
              "data-name=image [src]" #> s"/image/$imageSrc"
            case _ =>
              "*" #> NodeSeq.Empty
          }
        }
      } &
      "data-name=description *" #> value.description.asHtml &
      {
        value.areasDefinitions.getListOfNonEmptyDescription.size > 0 match {
          case true =>
            "data-name=area" #> value.areasDefinitions.getListOfNonEmptyDescription.map(definition => {
              "data-name=area-title *" #> definition.area.obj.dmap("")(_.name.get) &
              "data-name=area-description *" #> definition.description.asHtml
            })
          case false =>
            "data-name=areaArt" #> NodeSeq.Empty
        }
      } &
      {
        value.programsDefinitions.getListOfNonEmptyDescription.size > 0 match {
          case true =>
            "data-name=program" #> value.programsDefinitions.getListOfNonEmptyDescription.map(definition => {
              "data-name=program-title *" #> definition.program.obj.dmap("")(_.name.get) &
              "data-name=program-description *" #> definition.description.asHtml
            })
          case false =>
            "data-name=programH" #> NodeSeq.Empty
        }
      } &
      {
        value.transvesalAreasDefinitions.getListOfNonEmptyDescription.size > 0 match {
          case true =>
            "data-name=areaTransversal" #> value.transvesalAreasDefinitions.getListOfNonEmptyDescription.map(definition => {
              "data-name=areaT-title *" #> definition.transversalArea.obj.dmap("")(_.name.get) &
              "data-name=areaT-description *" #> definition.description.asHtml
            })
          case false =>
            "data-name=areaT" #> NodeSeq.Empty
        }
      } &
      EventSnippet.renderLastThreeEventByFilter(Full(value)) &
      BlogSnippet.renderLastThreePostByFilter(Full(value)) &
      CallSnippet.renderLastThreeCallByFilter(Full(value))
    }
  }

  def updateOrderValue(json: JValue): JsCmd = {
    implicit val formats = net.liftweb.json.DefaultFormats
    for {
      id <- tryo((json \ "id").extract[String])
      order <- tryo((json \ "order").extract[Long])
      item <- meta.find(id)
    } yield meta.where(_.id eqs item.id.get).modify(_.order setTo order).updateOne()
    Noop
  }

}
