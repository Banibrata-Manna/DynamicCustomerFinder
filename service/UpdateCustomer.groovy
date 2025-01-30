import org.moqui.context.ExecutionContext;
import org.moqui.entity.EntityValue;
import org.moqui.entity.EntityCondition;
import org.moqui.entity.EntityFind;
import org.moqui.entity.EntityList;

import java.sql.Date
import java.time.LocalDate;

ExecutionContext ec = context.ec;

Map<String, Object> customer = ec.service.sync().name("PartyServices.find#FindCustomerView").parameter("emailAddress", infoString).call();

//if(!customer.partyIdList) {System.exit(0);}

def partyId = customer.partyIdList.get(0);

//if(!partyId) System.exit(0);

EntityFind ef = ec.entity.find("partyEntities.party.contact.PartyContactMech");

EntityCondition pId = ec.entity.conditionFactory.makeCondition("partyId",
        EntityCondition.LIKE, partyId);

EntityCondition activeCMech = ec.entity.conditionFactory.makeCondition("thruDate",
        EntityCondition.EQUALS, null);

EntityCondition posPri = ec.entity.conditionFactory.makeCondition("contactMechPurposeId",
        EntityCondition.LIKE, "PostalPrimary");

ef.condition(pId);
ef.condition(activeCMech);
ef.condition(posPri);

EntityList entityList = ef.list();

if(address1 && city && postaLCode){
    for(EntityValue ev in entityList) {
        if (ev.contactMechPurposeId == "PostalPrimary")
        ev.set("thruDate", Date.valueOf(LocalDate.now())).update();
    }

    EntityValue ev = ec.entity.makeValue("ContactMech").set("contactMechTypeEnumId", "POSTAL_ADDRESS").setSequencedIdPrimary().create();
    EntityValue newPostal = ec.entity.makeValue("PostalAddress");
    newPostal.set("contactMechId", ev.get("contactMechId"));
    newPostal.setAll(context).create();
    ev = ec.entity.makeValue("PartyContactMech");
    ev.set("partyId", partyId);
    ev.set("contactMechId", ev.get("contactMechId"));
    ev.set("contactMechPurposeId", "PostalPrimary");
    ev.set("fromDate", Date.valueOf(LocalDate.now())).create();
}

//if(countryCode && areaCode && contactNumber){
//    EntityCondition telePri = ec.entity.conditionFactory.makeCondition("contactMechPurposeId",
//            EntityCondition.LIKE, "TelecomPrimary");
//
//    ef2.condition(telePri);
//
//    EntityList entityList = ef.list();
//
//    for(EntityValue ev in entityList) {
//        ev.set("thruDate", Date.valueOf(LocalDate.now())).update();
//    }
//
//    EntityValue ev = ec.makeValue("ContactMech").set("contactMechTypeEnumId", "TELECOM_NUMBER").setSequencedIdPrimary().create();
//    EntityValue newTelecom = ec.entity.makeValue("TelecomNumber");
//    newTelecom.setAll(context).create();
//    ev = ec.entity.makeValue("PartyContactMech");
//    ev.set("partyId", partyId);
//    ev.set("contactMechId", ev.get("contactMechId"));
//    ev.set("contactMechPurposeId", "PhonePrimary");
//    ev.set("fromDate", Date.valueOf(LocalDate.now())).create();
//}





