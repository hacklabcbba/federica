package code
package model
package event

import java.util.{Date, Calendar}

import code.model.project._
import org.joda.time.DateTime
import code.model.proposal.{Program, ActionLine, Area}
import code.model.resource.{ResourcePackage, Room, RoomQuote, Consumer}
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdRefListField}
import code.model.activity.{ActivityType, Activity}
import code.model.resource.CostType._
import code.model.resource.ClassType._
import code.model.productive.ProductiveUnit
import net.liftweb.record.field.StringField

class EventSpec extends BaseMongoSessionWordSpec {

  "Event" should {
    "create, validate, save, and retrieve properly" in {

      val city = City
        .createRecord
        .name("Cochabamba")

      val errsCity = city.validate
      if (errsCity.length > 1) {
        fail("Validation error: " + errsCity.mkString(", "))
      }

      city.validate.length should equal (0)

      city.save(false)

      val country = Country
        .createRecord
        .name("Bolivia")

      val errsCountry = country.validate
      if (errsCountry.length > 1) {
        fail("Validation error: " + errsCountry.mkString(", "))
      }

      country.validate.length should equal (0)

      country.save(false)

      val date: DateTime = new DateTime(2001, 5, 20, 0, 0, 0, 0)

      val schedule = Schedule
        .createRecord
        .startDate(date.toDate)
        .endDate(date.toDate)
        .description("Inauguration")

      val errsSchedule = country.validate
      if (errsSchedule.length > 1) {
        fail("Validation error: " + errsSchedule.mkString(", "))
      }

      schedule.validate.length should equal (0)

      schedule.save(false)

      val organizer = Organizer
        .createRecord
        .name("Jonh")
        .lastName("Smith")

      val errsOrganizer = organizer.validate
      if (errsOrganizer.length > 1) {
        fail("Validation error: " + errsOrganizer.mkString(", "))
      }

      organizer.validate.length should equal (0)
      organizer.save(false)

      val eType1 =  createEventType("festival")
      val eType2 =  createEventType("concierto")

      val dateInfoList = createDateInfoList
      val costInfo = createCostInfo
      val area = createArea
      val program = createProgram
      val productiveUnit = createProductiveUnit


      val activity1 = createActivity("actividad1", "descripcion1", eType1)
      val activity2 = createActivity("actividad2", "descripcion2", eType2)
      val user1 = createUser("Juan Perez")
      val aline1 = createActionLine("linea1", "description")
      val aline2 = createActionLine("linea2", "description2")
      val process = createProcess
      val cost1 = createCostContribution
      val req1 = createEventRequirement("Mayor de 18 años", false)
      val req2 = createEventRequirement("Traer una impresora, Lector de CD o DVD en desuso.", true)

      val event = Event
        .createRecord
        .eventNumber(1000)
        .name("Big History Project")
        .description("Include information about recent international progress in the field of the research, and the " +
          "relationship of this proposal to work in the field generally")
        .schedule(dateInfoList.id.get)
        .costInfo(costInfo.id.get)
        .eventTypes(eType1.id.get:: eType2.id.get :: Nil)
        .area(area.id.get)
        .program(program.id.get)
        .productiveUnit(productiveUnit.id.get)
        .city(city.id.get)
        .country(country.id.get)
        .place("La primera semana en Formarte del Martadero y las 2 siguientes semanas en el FabLab Atlas.")
        .shortDescription("descripcion corta")
        .activities(activity1.id.get :: activity2.id.get :: Nil)
        .description("descripcion larga")
        .requirementsList(req1.id.get :: req2.id.get :: Nil)
        .expositor(user1.id.get :: Nil)
        .organizer(user1.id.get :: Nil)
        .handler(user1.id.get :: Nil)
        .sponsor(user1.id.get :: Nil)
        .support(user1.id.get :: Nil)
        .collaborator(user1.id.get :: Nil)
        .pressRoom(user1.id.get :: Nil)
        .goals("objetivo 1")
        .quote("cupo para 12 personas")
        .tools("alicate, desarmador")
        .supplies(" cinta aislante, fosforos")
        .registration("Informes e inscripciones con Juanito perez")
        .costContributionByUse(cost1.id.get)
        .process(process.id.get)
        .actionLines(aline1.id.get :: aline2.id.get :: Nil)

      val errsEvent = event.validate
      if (errsEvent.length > 1) {
        fail("Validation error: " + errsEvent.mkString(", "))
      }

      event.validate.length should equal (0)
      event.save(false)

    }
  }

  def createEventType(name:String): EventType = {
    val eventType = EventType
      .createRecord
      .name(name)

    val errsList = eventType.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    eventType.validate.length should equal (0)
    eventType.save(false)
    eventType
  }

  def createDateInfoList: DateInfoList = {

    val date1: DateTime = new DateTime(2015, 3, 10, 15, 0, 0, 0)
    val date2: DateTime = new DateTime(2015, 3, 10, 18, 0, 0, 0)
    val date3: DateTime = new DateTime(2015, 3, 11, 18, 0, 0, 0)
    val date4: DateTime = new DateTime(2015, 3, 11, 21, 0, 0, 0)

    val itemList1 = createDateInfo("description1", date1, date2)
    val itemList2 = createDateInfo("description2", date3, date4)

    val dateInfoList = DateInfoList
      .createRecord
      .isCorrelative(true)
      .isAtSameHour(true)
      .itemList(itemList1.id.get :: itemList2.id.get :: Nil)


    val errsList = dateInfoList.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    dateInfoList.validate.length should equal (0)
    dateInfoList.save(false)
    dateInfoList
  }

  def createDateInfo(desc: String, startDate: DateTime, endDate: DateTime): DateInfo = {

    val dateInfo = DateInfo
      .createRecord
      .startDate(startDate.toDate)
      .endDate(startDate.toDate)
      .description(desc)

    val errsList = dateInfo.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    dateInfo.validate.length should equal (0)
    dateInfo.save(false)
    dateInfo
  }

  def createArea: Area = {

    val area = Area.createRecord
      .code("LET")
      .description("")
      .name("Arts and Literature")

    area.validate.length should equal (0)
    area.save(false)
  }

  def createActionLine(name: String, desc: String) = {

    val actionLine = ActionLine
      .createRecord
      .name(name)
      .description(desc)

    val errsActionLine = actionLine.validate
    if (errsActionLine.length > 1) {
      fail("Validation error: " + errsActionLine.mkString(", "))
    }
    actionLine.validate.length should equal (0)
    actionLine.save(false)
  }

  def createCity(name: String) = {
    val city = City
      .createRecord
      .name(name)

    val errsCity = city.validate
    if (errsCity.length > 1) {
      fail("Validation error: " + errsCity.mkString(", "))
    }

    city.validate.length should equal (0)
    city.save(false)
    city
  }

  def createCountry(name: String) = {
    val country = Country
      .createRecord
      .name(name)

    val errsCountry = country.validate
    if (errsCountry.length > 1) {
      fail("Validation error: " + errsCountry.mkString(", "))
    }

    country.validate.length should equal (0)
    country.save(false)
  }

  def createActivity(name: String, desc: String, activityType: EventType): Activity = {

    val packageResource = createPackageResource
    val room = createRoom

    val activity = Activity
      .createRecord
      .activityType(activityType.id.get)
      .description(desc)
      .name(name)
      .rooms(room :: Nil)
      .packages(packageResource :: Nil)

    val errsActivity= activity.validate
    if (errsActivity.length > 1) {
      fail("Validation error: " + errsActivity.mkString(", "))
    }

    activity.validate.length should equal (0)
    activity.save(false)
  }

  def createActivityType(name: String): ActivityType = {

    val activityType = ActivityType
      .createRecord
      .name(name)

    activityType.validate.length should equal (0)
    activityType.save(false)
  }

  def createPackageResource: ResourcePackage = {

    val consumer = Consumer.createRecord
      .areas(Nil)
      .name("associated")
      .description("EMERGING NATIONAL - INTERNATIONAL Autogestionados (40% OFF) *")

    consumer.validate.length should equal (0)
    consumer.save(false)

    val quotePackage = RoomQuote.createRecord
      .costType(PackageCost)
      .cost(280.01)
      .parameter("10 hrs. week")
      .consumer(consumer.id.get)

    quotePackage.validate.length should equal (0)
    quotePackage.save(false)

    val packageResource = ResourcePackage
      .createRecord
      .name("DIGITAL ROOM LAB + 10 PCs + DATA DISPLAY")
      .description("The contributions for use discount is only valid if payment of at least 40% of the contribution " +
        "is made, before the workshop")
      .comboName("PACKAGE FOR WORKSHOPS IN THE DIGITAL LAB")
      .classType(PackageType)
      .cost(quotePackage.id.get)

    val errsPack = packageResource.validate
    if (errsPack.length > 1) {
      fail("Validation error: " + errsPack.mkString(", "))
    }

    packageResource.validate.length should equal (0)
    packageResource.save(false)
  }

  def createRoom: Room = {

    val area2 = Area.createRecord
      .code("LET")
      .description("")
      .name("Arts and Literature")

    area2.validate.length should equal (0)
    area2.save(false)

    val consumer = Consumer.createRecord
      .areas(area2 :: Nil)
      .name("associated")
      .description("EMERGING NATIONAL - INTERNATIONAL Autogestionados (40% OFF) *")

    consumer.validate.length should equal (0)
    consumer.save(false)


    val quoteRoom = RoomQuote.createRecord
      .costType(RoomCost)
      .parameter("10 hrs. week")
      .cost(280.01)
      .characteristics("Weekly (10% OFF)")

      .consumer(consumer.id.get)

    quoteRoom.validate.length should equal (0)
    quoteRoom.save(false)

    val room = Room.createRecord
      .capacity(300)
      .state("Available")
      .name("Trozadero")
      .isReservable(true)
      .plane("plane.jpg")
      .classType(RoomType)
      .cost(quoteRoom.id.get)
      .description("Include information about recent international progress in the field of the research, and the " +
      "relationship of this proposal to work in the field generally")

    val errsRoom = room.validate
    if (errsRoom.length > 1) {
      fail("Validation error: " + errsRoom.mkString(", "))
    }

    room.validate.length should equal (0)
    room.save(false)
  }

  def createUser(name: String): User = {

    val user2 = User.createRecord
    user2
  }

  def createProgram: Program = {

    val program = Program
      .createRecord
      .name("Programa nro1")
      .description("Descripcion del programa")

    val errsProgram = program.validate
    if (errsProgram.length > 1) {
      fail("Validation error: " + errsProgram.mkString(", "))
    }
    program.validate.length should equal (0)
    program.save(false)
  }

  def createCostInfo: CostInfo = {

    val costInfo = CostInfo
      .createRecord
      .cost(100.00)
      .currency("Bs")
      .description("Para cubrir los costos de herramientas e insumos principalmente. Se aceptan becados.")

    val errorsList = costInfo.validate
    if (errorsList.length > 1) {
      fail("Validation error: " + errorsList.mkString(", "))
    }
    costInfo.validate.length should equal (0)
    costInfo.save(false)
  }

  def createProductiveUnit: ProductiveUnit = {

    val productiveUnit = ProductiveUnit.createRecord
    productiveUnit
  }

  def createProcess: Process = {

    val responsible = createUser("Juan sebas")
    val process = Process.createRecord
      .name("Proceso 1")
      .goal("Objetivos del proceso")
      .description("descripcion del proceso")
      .responsible(responsible.id.get)
      .history("Historia extensa")

    val errorsList = process.validate
    if (errorsList.length > 1) {
      fail("Validation error: " + errorsList.mkString(", "))
    }
    process.validate.length should equal (0)
    process.save(false)
  }

  def createCostContribution: CostContributionByUse = {

    val cost = CostContributionByUse
      .createRecord
      .title("Detalle del costo asigando al evento")

    val errorsList = cost.validate
    if (errorsList.length > 1) {
      fail("Validation error: " + errorsList.mkString(", "))
    }
    cost.validate.length should equal (0)
    cost.save(false)
  }

  def createEventRequirement(reqTitle: String, isOptional: Boolean): EventRequirement = {

    val req = EventRequirement
      .createRecord
      .requirement(reqTitle)
      .isOptional(isOptional)

    val errorsList = req.validate
    if (errorsList.length > 1) {
      fail("Validation error: " + errorsList.mkString(", "))
    }
    req.validate.length should equal (0)
    req.save(false)
  }
}
