import org.moqui.context.ExecutionContext;
import org.moqui.entity.EntityCondition;
import org.moqui.entity.EntityFind;
import org.moqui.entity.EntityList;
import org.moqui.entity.EntityValue;

import java.sql.Date
import java.time.LocalDate;

ExecutionContext ec = context.ec;

Map<String, Object> customer = ec.service.sync().name("PartyServices.find#FindCustomerView").parameters(["emailAddress" : context.emailAddress]).call();

if(!customer.partyIdList) return;

def partyId = customer.partyIdList.get(0);

EntityFind ef = ec.entity.find("partyEntities.party.contact.PartyContactMech").distinct(true);

EntityCondition pId = ec.entity.conditionFactory.makeCondition("partyId",
        EntityCondition.EQUALS, partyId);

EntityCondition activeCMech = ec.entity.conditionFactory.makeCondition("thruDate",
        EntityCondition.IS_NULL, null);

EntityCondition posPri = ec.entity.conditionFactory.makeCondition("contactMechPurposeId",
        EntityCondition.EQUALS, "PostalPrimary");

EntityCondition telePri = ec.entity.conditionFactory.makeCondition("contactMechPurposeId",
        EntityCondition.EQUALS, "PhonePrimary");

EntityCondition primaryCMechs = ec.entity.conditionFactory.makeCondition(
        posPri, EntityCondition.OR, telePri
);

List<EntityCondition> conditionList = new ArrayList<>();
conditionList.add(pId);
conditionList.add(activeCMech);
conditionList.add(primaryCMechs);

ef.condition(ec.entity.conditionFactory.makeCondition(
        conditionList
));

EntityList entityList = ef.list();

if(address1){
    if (!city) return;
    if (!postalCode) return;
    for(EntityValue ev in entityList) {
        if (ev.contactMechPurposeId == "PostalPrimary")
        ev.set("thruDate", Date.valueOf(LocalDate.now())).update();
    }

        EntityValue ev = ec.entity.makeValue("ContactMech").set("contactMechTypeEnumId", "POSTAL_ADDRESS").setSequencedIdPrimary().create();
        EntityValue newPostal = ec.entity.makeValue("PostalAddress");
        createdCMechId = ev.get("contactMechId");
        newPostal.set("contactMechId", createdCMechId);
        newPostal.setAll(context).create();
        ev = ec.entity.makeValue("PartyContactMech");
        ev.set("partyId", partyId);
        ev.set("contactMechId", createdCMechId);
        ev.set("contactMechPurposeId", "PostalPrimary");
        ev.set("fromDate", Date.valueOf(LocalDate.now())).create();
    }

if(countryCode){

    if(!areaCode) return;
    if(!contactNumber) return ;

    for(EntityValue ev in entityList) {
        if (ev.contactMechPurposeId == "PhonePrimary")
        ev.set("thruDate", Date.valueOf(LocalDate.now())).update();
    }

    EntityValue ev = ec.entity.makeValue("ContactMech").set("contactMechTypeEnumId", "TELECOM_NUMBER").setSequencedIdPrimary().create();
    EntityValue newPostal = ec.entity.makeValue("TelecomNumber");
    createdCMechId = ev.get("contactMechId");
    newPostal.set("contactMechId", createdCMechId);
    newPostal.setAll(context).create();
    ev = ec.entity.makeValue("PartyContactMech");
    ev.set("partyId", partyId);
    ev.set("contactMechId", createdCMechId);
    ev.set("contactMechPurposeId", "PhonePrimary");
    ev.set("fromDate", Date.valueOf(LocalDate.now())).create();
}





