import org.moqui.context.ExecutionContext;
import org.moqui.entity.EntityValue;

ExecutioContext ec = context.ec;

Map<String, Object> customer = ec.service.sync().name("PartyServices.find#FindCustomerView").parameter("emailAddress", emailAddress).call();

def partyId = customer.get("partyIdList");







