import org.moqui.context.ExecutionContext;
import org.moqui.entity.EntityCondition;
import org.moqui.entity.EntityFind;
import org.moqui.entity.EntityList;
import org.moqui.entity.EntityValue;

ExecutionContext ec = context.ec;

EntityFind ef = ec.entity.find("partyEntities.party.FindCustomerView").distinct(true);

if(firstName) {
    ef.condition(ec.entity.conditionFactory.makeCondition("firstName", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + firstName + "%").ignoreCase());
}
if(lastName) {
    ef.condition(ec.entity.conditionFactory.makeCondition("lastName", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + lastName + "%").ignoreCase());
}
if(contactNumber){
    ef.condition(ec.entity.conditionFactory.makeCondition("contactNumber", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + contactNumber + "%").ignoreCase());
}
if(city){
    ef.condition(ec.entity.conditionFactory.makeCondition("city", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + city + "%").ignoreCase());
}
if(address1){
    ef.condition(ec.entity.conditionFactory.makeCondition("address1", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + address1 + "%").ignoreCase());
}
if(address2){
    ef.condition(ec.entity.conditionFactory.makeCondition("address2", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + address2 + "%").ignoreCase());
}
if(postalCode){
    ef.condition(ec.entity.conditionFactory.makeCondition("postalCode", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + postalCode + "%").ignoreCase());
}
if(emailAddress){
    ef.condition(ec.entity.conditionFactory.makeCondition("contactMechPurposeId", EntityCondition.LIKE, "EmailPrimary"), EntityCondition.JoinOperator.AND,
            ef.condition(ec.entity.conditionFactory.makeCondition("infoString", EntityCondition.LIKE, (leadingWildCard ? "%" : "") + emailAddress + "%")));
}

ef.orderBy(firstName + " " + lastName);

EntityList entityList = ef.list();

ef.selectField("partyId");

partyIdList = []

for(EntityValue ev in entityList) {
    partyIdList.add(ev.partyId);
}

