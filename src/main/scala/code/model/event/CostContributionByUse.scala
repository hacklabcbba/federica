package code
package model
package event

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField


class CostContributionByUse private() extends MongoRecord[CostContributionByUse] with ObjectIdPk[CostContributionByUse]{

  override def meta = CostContributionByUse

  object title extends StringField(this, "")
  //object itemList extends ObjectIdRefListField(this, CostSelection)
}
object CostContributionByUse extends CostContributionByUse with RogueMetaRecord[CostContributionByUse]{
  override def collectionName = "event.cost_contributions"
}








