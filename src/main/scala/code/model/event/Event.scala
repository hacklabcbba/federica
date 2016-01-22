package code
package model
package event

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.model.activity.Activity
import code.model.resource.Room
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JArray
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{EnumNameField, StringField, TextareaField}
import net.liftweb.util.Helpers._

import scala.xml.Elem

class Event private() extends MongoRecord[Event] with ObjectIdPk[Event] with BaseModel[Event] {

  override def meta = Event

  def title = "Eventos"

  def entityListUrl = Site.backendEvents.menu.loc.calcDefaultHref

  object eventNumber extends StringField(this, 200) {
    override def displayName = "#"
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
    def toDisableForm = SHtml.span(<b>{get}</b>, Noop)
  }

  object name extends StringField(this, 200) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object schedule extends ObjectIdRefField(this, Schedule) {
    override def toString = {
      this.obj.dmap("")(_.toString)
    }
  }

  object costInfo extends ObjectIdRefField(this, CostInfo) {
    override def displayName = "Costo"
    override def toString = this.obj.dmap("no definido..")(_.cost.get.toString)
  }

  object eventTypes extends ObjectIdRefListField(this, EventType) {
    override def displayName = "Tipo"
    def currentValue = this.objs
    def availableOptions: List[(EventType, String)] = EventType.findAll.map(p => p -> p.name.get)

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        {(list: List[EventType]) => set(list.map(_.id.get))},
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object program extends ObjectIdRefField(this, Program) {
    override def displayName = "Programa"

    override def optional_? = true
    override def toString = this.obj.dmap("")(_.name.get)
    val listProgram = Program.findAll
    val defaultProgram = listProgram.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(listProgram, defaultProgram,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione programa..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object area extends ObjectIdRefField(this, Area) {

    override def displayName = "Área"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = Area.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area..")(p => {
          set(p.id.get)
          Noop
      }))
    }
  }

  object transversalArea extends ObjectIdRefField(this, TransversalArea) {

    override def displayName = "Área transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = TransversalArea.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object process extends ObjectIdRefField(this, Process) {
    override def displayName = "Proceso"
    val list = Process.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione un proceso..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object actionLines extends ObjectIdRefListField(this, ActionLine) {
    override def displayName = "Lineas de acción"
    def currentValue = this.objs
    def availableOptions: List[(ActionLine, String)] = ActionLine.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        (list: List[ActionLine]) => set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios lineas de accion.."
      ))
    }
  }

  object productiveUnit extends ObjectIdRefField(this, ProductiveUnit) {

    override def displayName = "Unidad productiva"
    override def optional_? = true
    override def toString = this.obj.dmap("")(_.name.get)
    val list = ProductiveUnit.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione unidad productiva..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object city extends ObjectIdRefField(this, City) {

    override def displayName = "Ciudad"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = City.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione ciudad..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object country extends ObjectIdRefField(this, Country) {

    override def displayName = "Pais"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = Country.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione pais..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object place extends StringField(this, 500) {
    override def displayName = "Lugar"
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese el lugar del evento.."))
  }

  object shortDescription extends TextareaField(this, 1000) {
    override def displayName = "Descripción corta"
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object activities extends ObjectIdRefListField(this, Activity) {
    override def displayName = "Actividades"
  }

  object description extends TextareaField(this, 1000) {
    override def displayName = "Descripción"
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object requirements extends ObjectIdRefListField(this, EventRequirement) {
    override def displayName = "Requerimientos"
    def currentValue = this.objs
    def availableOptions: List[(EventRequirement, String)] =
      EventRequirement.findAll.map(p => p -> p.title.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, (list: List[EventRequirement]) =>
        set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object expositors extends ObjectIdRefListField(this, User) {
    override def displayName = "Expositores"
    def currentValue = this.objs
    def availableOptions: List[(User, String)] = User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, (list: List[User]) =>
        set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object organizer extends ObjectIdRefField(this, User) {
    override def displayName = "Organiza"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get
    override def toForm: Box[Elem] = {
      Full(SHtml.selectObj(availableOptions, currentValue, (u: User) =>
          set(u.id.get),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione quien organiza este evento.."
      ))
    }
    def currentValue = this.obj
    def availableOptions = User.findAll.map(p => p -> p.name.get)
  }

  object handlers extends ObjectIdRefListField(this, User) {
    override def displayName = "Gestionadores"
    override def toString = {
      this.objs.map(_.name.get).mkString(", ")
    }
    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectElem(availableOptions, currentValue, "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios gestionadores..")((list: List[User]) =>
        set(list.map(_.id.get))
      ))
    }
    override def defaultValue = Nil
    def currentValue = this.objs
    def availableOptions = User.findAll
  }

  object sponsors extends ObjectIdRefListField(this, User) {
    override def displayName = "Auspiciadores"
    override def toString = {
      User.findAll(this.get).map(_.name.get).mkString(", ")
    }

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, (list: List[User]) =>
        set(list.map(_.id.get)),
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios auspiciadores.."
      ))
    }
    override def defaultValue = Nil
    def currentValue = this.objs
    def availableOptions = User.findAll.map(p => p -> p.name.get)
  }

  object supports extends ObjectIdRefListField(this, User) {
    override def toString = {
      this.objs.map(_.name.get).mkString(", ")
    }

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, (list: List[User]) =>
        set(list.map(_.id.get)),
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios soportes.."
      ))
    }
    override def defaultValue = Nil
    def currentValue = this.objs
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object collaborators extends ObjectIdRefListField(this, User) {
    override def displayName = "Colaboradores"
    override def toString = {
      this.objs.map(_.name.get).mkString(", ")
    }
    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, (list: List[User]) =>
        set(list.map(_.id.get)),
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios colaboradores.."
      ))
    }
    override def defaultValue = Nil
    def currentValue = this.objs
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object pressRoom extends ObjectIdRefField(this, PressNotes) {
    override def displayName = "Sala de prensa"
  }

  object goal extends TextareaField(this, 1000) {
    override def displayName = "Objetivo"
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object quote extends StringField(this, "") {
    override def displayName = "Presupuesto"
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese el cupo maximo.."))
  }

  object tools extends TextareaField(this, 1000) {
    override def displayName = "Herramientas"
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object supplies extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object registration extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object costContributionByUse extends ObjectIdRefField(this, CostContributionByUse)
  object status extends EnumNameField(this, StatusType) {
    override def displayName = "Estado"
  }
  object values extends ObjectIdRefListField(this, Values) {
    override def displayName = "Principios"
  }

  object rooms extends ObjectIdRefListField(this, Room) {
    override def shouldDisplay_? = false

    override def asJValue = JArray(objs.map(_.asJValue))
  }


}

object Event extends Event with RogueMetaRecord[Event] {
  override def collectionName = "event.events"
}

object StatusType extends Enumeration {
  type StateType = Value
  val Approved, Rejected, Draft = Value
}

class Values private() extends MongoRecord[Values] with ObjectIdPk[Values] {

  override def meta = Values

  object name extends StringField(this, 100) {
    override def displayName = "Nombre"
  }

}

object Values extends Values with RogueMetaRecord[Values] {
  val Innovation = "Innovación"
  val Research = "Investigación"
  val Experimentation = "Experimentacion"
  val ConceptualAndFormaligor = "Rigor conceptual y formal"
  val Integration = "Integracion"
  val ExchangeOfKnowledgeAndExperiences = "Intercambio de conocimientos y experiencias"
  val Intercultural = "Interculturalidad"
  private lazy val data = List(
    "Innovación",
    "Investigación",
    "Experimentacion",
    "Rigor conceptual y formal",
    "Integracion",
    "Intercambio de conocimientos y experiencias",
    "Interculturalidad"
  )

  def seedData = {
    if (Values.count() == 0) data.foreach(d => {
      Values.createRecord.name(d).save(true)
    })
  }
}

