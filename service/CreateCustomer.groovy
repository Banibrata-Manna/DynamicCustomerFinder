import org.moqui.context.ExecutionContext;
import org.moqui.entity.EntityValue

import java.sql.Date
import java.time.LocalDate

ExecutionContext ec = context.ec;

//ec.service.sync().name("create#Example").parameters(fields).call()

Map<Object, Object> customer = ec.service.sync().name("PartyServices.find#FindCustomerView").parameters(context).call();
if(!customer.partyIdList) {
    EntityValue createdParty = ec.entity.makeValue("Party").set("partyTypeEnumId", "PERSON").setSequencedIdPrimary().create();

    Map<String, Object> fields = new HashMap<String, Object>();

    def partyId = createdParty.get("partyId");
    fields.put("partyId", partyId);
    fields.put("firstName", firstName);
    fields.put("lastName", lastName);

    ec.entity.makeValue("Person").setAll(fields).create();

    fields.remove("firstName");
    fields.remove("lastName");
    fields.put("roleTypeId", "CUSTOMER");

    ec.entity.makeValue("PartyRole").setAll(fields).create();

    fields.remove("roleTypeId");
    fields.remove("partyId");


    fields.put("contactMechTypeEnumId", "EMAIL_ADDRESS");
    fields.put("infoString", emailAddress);

    EntityValue createdContactMech = ec.entity.makeValue("ContactMech").setAll(fields).setSequencedIdPrimary().create();

    fields.remove("infoString");
    fields.remove("contactMechTypeEnumId");

    fields.put("partyId", partyId);
    fields.put("contactMechId", createdContactMech.get("contactMechId"));
    fields.put("contactMechPurposeId", "EmailPrimary");
    fields.put("fromDate", Date.valueOf(LocalDate.now()));

    ec.entity.makeValue("PartyContactMech").setAll(fields).create();
}